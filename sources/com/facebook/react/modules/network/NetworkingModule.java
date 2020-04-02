package com.facebook.react.modules.network;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.StandardCharsets;
import com.facebook.react.common.network.OkHttpCallUtil;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.C1069Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.ByteString;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

@ReactModule(name = "Networking")
public final class NetworkingModule extends ReactContextBaseJavaModule {
    private static final int CHUNK_TIMEOUT_NS = 100000000;
    private static final String CONTENT_ENCODING_HEADER_NAME = "content-encoding";
    private static final String CONTENT_TYPE_HEADER_NAME = "content-type";
    private static final int MAX_CHUNK_SIZE_BETWEEN_FLUSHES = 8192;
    public static final String NAME = "Networking";
    private static final String REQUEST_BODY_KEY_BASE64 = "base64";
    private static final String REQUEST_BODY_KEY_FORMDATA = "formData";
    private static final String REQUEST_BODY_KEY_STRING = "string";
    private static final String REQUEST_BODY_KEY_URI = "uri";
    private static final String TAG = "NetworkingModule";
    private static final String USER_AGENT_HEADER_NAME = "user-agent";
    @Nullable
    private static CustomClientBuilder customClientBuilder;
    /* access modifiers changed from: private */
    public final OkHttpClient mClient;
    private final ForwardingCookieHandler mCookieHandler;
    private final CookieJarContainer mCookieJarContainer;
    @Nullable
    private final String mDefaultUserAgent;
    private final List<RequestBodyHandler> mRequestBodyHandlers;
    private final Set<Integer> mRequestIds;
    /* access modifiers changed from: private */
    public final List<ResponseHandler> mResponseHandlers;
    /* access modifiers changed from: private */
    public boolean mShuttingDown;
    private final List<UriHandler> mUriHandlers;

    public interface CustomClientBuilder {
        void apply(Builder builder);
    }

    public interface RequestBodyHandler {
        boolean supports(ReadableMap readableMap);

        RequestBody toRequestBody(ReadableMap readableMap, String str);
    }

    public interface ResponseHandler {
        boolean supports(String str);

        WritableMap toResponseData(ResponseBody responseBody) throws IOException;
    }

    public interface UriHandler {
        WritableMap fetch(Uri uri) throws IOException;

        boolean supports(Uri uri, String str);
    }

    /* access modifiers changed from: private */
    public static boolean shouldDispatch(long j, long j2) {
        return j2 + 100000000 < j;
    }

    public String getName() {
        return NAME;
    }

    NetworkingModule(ReactApplicationContext reactApplicationContext, @Nullable String str, OkHttpClient okHttpClient, @Nullable List<NetworkInterceptorCreator> list) {
        super(reactApplicationContext);
        this.mRequestBodyHandlers = new ArrayList();
        this.mUriHandlers = new ArrayList();
        this.mResponseHandlers = new ArrayList();
        if (list != null) {
            Builder newBuilder = okHttpClient.newBuilder();
            for (NetworkInterceptorCreator create : list) {
                newBuilder.addNetworkInterceptor(create.create());
            }
            okHttpClient = newBuilder.build();
        }
        this.mClient = okHttpClient;
        this.mCookieHandler = new ForwardingCookieHandler(reactApplicationContext);
        this.mCookieJarContainer = (CookieJarContainer) this.mClient.cookieJar();
        this.mShuttingDown = false;
        this.mDefaultUserAgent = str;
        this.mRequestIds = new HashSet();
    }

    NetworkingModule(ReactApplicationContext reactApplicationContext, @Nullable String str, OkHttpClient okHttpClient) {
        this(reactApplicationContext, str, okHttpClient, null);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext) {
        this(reactApplicationContext, null, OkHttpClientProvider.createClient(reactApplicationContext), null);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext, List<NetworkInterceptorCreator> list) {
        this(reactApplicationContext, null, OkHttpClientProvider.createClient(reactApplicationContext), list);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext, String str) {
        this(reactApplicationContext, str, OkHttpClientProvider.createClient(reactApplicationContext), null);
    }

    public static void setCustomClientBuilder(CustomClientBuilder customClientBuilder2) {
        customClientBuilder = customClientBuilder2;
    }

    private static void applyCustomBuilder(Builder builder) {
        CustomClientBuilder customClientBuilder2 = customClientBuilder;
        if (customClientBuilder2 != null) {
            customClientBuilder2.apply(builder);
        }
    }

    public void initialize() {
        this.mCookieJarContainer.setCookieJar(new JavaNetCookieJar(this.mCookieHandler));
    }

    public void onCatalystInstanceDestroy() {
        this.mShuttingDown = true;
        cancelAllRequests();
        this.mCookieHandler.destroy();
        this.mCookieJarContainer.removeCookieJar();
        this.mRequestBodyHandlers.clear();
        this.mResponseHandlers.clear();
        this.mUriHandlers.clear();
    }

    public void addUriHandler(UriHandler uriHandler) {
        this.mUriHandlers.add(uriHandler);
    }

    public void addRequestBodyHandler(RequestBodyHandler requestBodyHandler) {
        this.mRequestBodyHandlers.add(requestBodyHandler);
    }

    public void addResponseHandler(ResponseHandler responseHandler) {
        this.mResponseHandlers.add(responseHandler);
    }

    public void removeUriHandler(UriHandler uriHandler) {
        this.mUriHandlers.remove(uriHandler);
    }

    public void removeRequestBodyHandler(RequestBodyHandler requestBodyHandler) {
        this.mRequestBodyHandlers.remove(requestBodyHandler);
    }

    public void removeResponseHandler(ResponseHandler responseHandler) {
        this.mResponseHandlers.remove(responseHandler);
    }

    @ReactMethod
    public void sendRequest(String str, String str2, int i, ReadableArray readableArray, ReadableMap readableMap, String str3, boolean z, int i2, boolean z2) {
        try {
            sendRequestInternal(str, str2, i, readableArray, readableMap, str3, z, i2, z2);
        } catch (Throwable th) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to send url request: ");
            sb.append(str2);
            FLog.m51e(TAG, sb.toString(), th);
            ResponseUtil.onRequestError(getEventEmitter(), i, th.getMessage(), th);
        }
    }

    public void sendRequestInternal(String str, String str2, final int i, ReadableArray readableArray, ReadableMap readableMap, final String str3, boolean z, int i2, boolean z2) {
        RequestBodyHandler requestBodyHandler;
        RequestBody requestBody;
        Charset charset;
        final RCTDeviceEventEmitter eventEmitter = getEventEmitter();
        try {
            Uri parse = Uri.parse(str2);
            for (UriHandler uriHandler : this.mUriHandlers) {
                if (uriHandler.supports(parse, str3)) {
                    ResponseUtil.onDataReceived(eventEmitter, i, uriHandler.fetch(parse));
                    ResponseUtil.onRequestSuccess(eventEmitter, i);
                    return;
                }
            }
            try {
                Request.Builder url = new Request.Builder().url(str2);
                if (i != 0) {
                    url.tag(Integer.valueOf(i));
                }
                Builder newBuilder = this.mClient.newBuilder();
                applyCustomBuilder(newBuilder);
                if (!z2) {
                    newBuilder.cookieJar(CookieJar.NO_COOKIES);
                }
                if (z) {
                    newBuilder.addNetworkInterceptor(new Interceptor() {
                        public C1069Response intercept(Chain chain) throws IOException {
                            C1069Response proceed = chain.proceed(chain.request());
                            return proceed.newBuilder().body(new ProgressResponseBody(proceed.body(), new ProgressListener() {
                                long last = System.nanoTime();

                                public void onProgress(long j, long j2, boolean z) {
                                    long nanoTime = System.nanoTime();
                                    if ((z || NetworkingModule.shouldDispatch(nanoTime, this.last)) && !str3.equals("text")) {
                                        ResponseUtil.onDataReceivedProgress(eventEmitter, i, j, j2);
                                        this.last = nanoTime;
                                    }
                                }
                            })).build();
                        }
                    });
                }
                if (i2 != this.mClient.connectTimeoutMillis()) {
                    newBuilder.connectTimeout((long) i2, TimeUnit.MILLISECONDS);
                }
                OkHttpClient build = newBuilder.build();
                Headers extractHeaders = extractHeaders(readableArray, readableMap);
                if (extractHeaders == null) {
                    ResponseUtil.onRequestError(eventEmitter, i, "Unrecognized headers format", null);
                    return;
                }
                String str4 = extractHeaders.get(CONTENT_TYPE_HEADER_NAME);
                String str5 = extractHeaders.get(CONTENT_ENCODING_HEADER_NAME);
                url.headers(extractHeaders);
                if (readableMap != null) {
                    Iterator it = this.mRequestBodyHandlers.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        requestBodyHandler = (RequestBodyHandler) it.next();
                        if (requestBodyHandler.supports(readableMap)) {
                            break;
                        }
                    }
                    if (readableMap != null || str.toLowerCase().equals("get") || str.toLowerCase().equals("head")) {
                        requestBody = RequestBodyUtil.getEmptyBody(str);
                    } else if (requestBodyHandler != null) {
                        requestBody = requestBodyHandler.toRequestBody(readableMap, str4);
                    } else {
                        String str6 = REQUEST_BODY_KEY_STRING;
                        String str7 = "Payload is set but no content-type header specified";
                        if (!readableMap.hasKey(str6)) {
                            String str8 = "base64";
                            if (!readableMap.hasKey(str8)) {
                                String str9 = "uri";
                                if (!readableMap.hasKey(str9)) {
                                    String str10 = REQUEST_BODY_KEY_FORMDATA;
                                    if (readableMap.hasKey(str10)) {
                                        if (str4 == null) {
                                            str4 = "multipart/form-data";
                                        }
                                        MultipartBody.Builder constructMultipartBody = constructMultipartBody(readableMap.getArray(str10), str4, i);
                                        if (constructMultipartBody != null) {
                                            requestBody = constructMultipartBody.build();
                                        } else {
                                            return;
                                        }
                                    } else {
                                        requestBody = RequestBodyUtil.getEmptyBody(str);
                                    }
                                } else if (str4 == null) {
                                    ResponseUtil.onRequestError(eventEmitter, i, str7, null);
                                    return;
                                } else {
                                    String string = readableMap.getString(str9);
                                    InputStream fileInputStream = RequestBodyUtil.getFileInputStream(getReactApplicationContext(), string);
                                    if (fileInputStream == null) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Could not retrieve file for uri ");
                                        sb.append(string);
                                        ResponseUtil.onRequestError(eventEmitter, i, sb.toString(), null);
                                        return;
                                    }
                                    requestBody = RequestBodyUtil.create(MediaType.parse(str4), fileInputStream);
                                }
                            } else if (str4 == null) {
                                ResponseUtil.onRequestError(eventEmitter, i, str7, null);
                                return;
                            } else {
                                requestBody = RequestBody.create(MediaType.parse(str4), ByteString.decodeBase64(readableMap.getString(str8)));
                            }
                        } else if (str4 == null) {
                            ResponseUtil.onRequestError(eventEmitter, i, str7, null);
                            return;
                        } else {
                            String string2 = readableMap.getString(str6);
                            MediaType parse2 = MediaType.parse(str4);
                            if (RequestBodyUtil.isGzipEncoding(str5)) {
                                requestBody = RequestBodyUtil.createGzip(parse2, string2);
                                if (requestBody == null) {
                                    ResponseUtil.onRequestError(eventEmitter, i, "Failed to gzip request body", null);
                                    return;
                                }
                            } else {
                                if (parse2 == null) {
                                    charset = StandardCharsets.UTF_8;
                                } else {
                                    charset = parse2.charset(StandardCharsets.UTF_8);
                                }
                                requestBody = RequestBody.create(parse2, string2.getBytes(charset));
                            }
                        }
                    }
                    url.method(str, wrapRequestBodyWithProgressEmitter(requestBody, eventEmitter, i));
                    addRequest(i);
                    Call newCall = build.newCall(url.build());
                    final int i3 = i;
                    final String str11 = str3;
                    final boolean z3 = z;
                    C08022 r0 = new Callback() {
                        public void onFailure(Call call, IOException iOException) {
                            String str;
                            if (!NetworkingModule.this.mShuttingDown) {
                                NetworkingModule.this.removeRequest(i3);
                                if (iOException.getMessage() != null) {
                                    str = iOException.getMessage();
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Error while executing request: ");
                                    sb.append(iOException.getClass().getSimpleName());
                                    str = sb.toString();
                                }
                                ResponseUtil.onRequestError(eventEmitter, i3, str, iOException);
                            }
                        }

                        public void onResponse(Call call, C1069Response response) throws IOException {
                            if (!NetworkingModule.this.mShuttingDown) {
                                NetworkingModule.this.removeRequest(i3);
                                ResponseUtil.onResponseReceived(eventEmitter, i3, response.code(), NetworkingModule.translateHeaders(response.headers()), response.request().url().toString());
                                try {
                                    ResponseBody body = response.body();
                                    if ("gzip".equalsIgnoreCase(response.header("Content-Encoding")) && body != null) {
                                        GzipSource gzipSource = new GzipSource(body.source());
                                        String header = response.header("Content-Type");
                                        body = ResponseBody.create(header != null ? MediaType.parse(header) : null, -1, Okio.buffer((Source) gzipSource));
                                    }
                                    for (ResponseHandler responseHandler : NetworkingModule.this.mResponseHandlers) {
                                        if (responseHandler.supports(str11)) {
                                            ResponseUtil.onDataReceived(eventEmitter, i3, responseHandler.toResponseData(body));
                                            ResponseUtil.onRequestSuccess(eventEmitter, i3);
                                            return;
                                        }
                                    }
                                    String str = "text";
                                    if (z3) {
                                        if (str11.equals(str)) {
                                            NetworkingModule.this.readWithProgress(eventEmitter, i3, body);
                                            ResponseUtil.onRequestSuccess(eventEmitter, i3);
                                            return;
                                        }
                                    }
                                    String str2 = "";
                                    if (str11.equals(str)) {
                                        try {
                                            str2 = body.string();
                                        } catch (IOException e) {
                                            if (!response.request().method().equalsIgnoreCase("HEAD")) {
                                                ResponseUtil.onRequestError(eventEmitter, i3, e.getMessage(), e);
                                            }
                                        }
                                    } else if (str11.equals("base64")) {
                                        str2 = Base64.encodeToString(body.bytes(), 2);
                                    }
                                    ResponseUtil.onDataReceived(eventEmitter, i3, str2);
                                    ResponseUtil.onRequestSuccess(eventEmitter, i3);
                                } catch (IOException e2) {
                                    ResponseUtil.onRequestError(eventEmitter, i3, e2.getMessage(), e2);
                                }
                            }
                        }
                    };
                    newCall.enqueue(r0);
                }
                requestBodyHandler = null;
                if (readableMap != null) {
                }
                requestBody = RequestBodyUtil.getEmptyBody(str);
                url.method(str, wrapRequestBodyWithProgressEmitter(requestBody, eventEmitter, i));
                addRequest(i);
                Call newCall2 = build.newCall(url.build());
                final int i32 = i;
                final String str112 = str3;
                final boolean z32 = z;
                C08022 r02 = new Callback() {
                    public void onFailure(Call call, IOException iOException) {
                        String str;
                        if (!NetworkingModule.this.mShuttingDown) {
                            NetworkingModule.this.removeRequest(i32);
                            if (iOException.getMessage() != null) {
                                str = iOException.getMessage();
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Error while executing request: ");
                                sb.append(iOException.getClass().getSimpleName());
                                str = sb.toString();
                            }
                            ResponseUtil.onRequestError(eventEmitter, i32, str, iOException);
                        }
                    }

                    public void onResponse(Call call, C1069Response response) throws IOException {
                        if (!NetworkingModule.this.mShuttingDown) {
                            NetworkingModule.this.removeRequest(i32);
                            ResponseUtil.onResponseReceived(eventEmitter, i32, response.code(), NetworkingModule.translateHeaders(response.headers()), response.request().url().toString());
                            try {
                                ResponseBody body = response.body();
                                if ("gzip".equalsIgnoreCase(response.header("Content-Encoding")) && body != null) {
                                    GzipSource gzipSource = new GzipSource(body.source());
                                    String header = response.header("Content-Type");
                                    body = ResponseBody.create(header != null ? MediaType.parse(header) : null, -1, Okio.buffer((Source) gzipSource));
                                }
                                for (ResponseHandler responseHandler : NetworkingModule.this.mResponseHandlers) {
                                    if (responseHandler.supports(str112)) {
                                        ResponseUtil.onDataReceived(eventEmitter, i32, responseHandler.toResponseData(body));
                                        ResponseUtil.onRequestSuccess(eventEmitter, i32);
                                        return;
                                    }
                                }
                                String str = "text";
                                if (z32) {
                                    if (str112.equals(str)) {
                                        NetworkingModule.this.readWithProgress(eventEmitter, i32, body);
                                        ResponseUtil.onRequestSuccess(eventEmitter, i32);
                                        return;
                                    }
                                }
                                String str2 = "";
                                if (str112.equals(str)) {
                                    try {
                                        str2 = body.string();
                                    } catch (IOException e) {
                                        if (!response.request().method().equalsIgnoreCase("HEAD")) {
                                            ResponseUtil.onRequestError(eventEmitter, i32, e.getMessage(), e);
                                        }
                                    }
                                } else if (str112.equals("base64")) {
                                    str2 = Base64.encodeToString(body.bytes(), 2);
                                }
                                ResponseUtil.onDataReceived(eventEmitter, i32, str2);
                                ResponseUtil.onRequestSuccess(eventEmitter, i32);
                            } catch (IOException e2) {
                                ResponseUtil.onRequestError(eventEmitter, i32, e2.getMessage(), e2);
                            }
                        }
                    }
                };
                newCall2.enqueue(r02);
            } catch (Exception e) {
                ResponseUtil.onRequestError(eventEmitter, i, e.getMessage(), null);
            }
        } catch (IOException e2) {
            ResponseUtil.onRequestError(eventEmitter, i, e2.getMessage(), e2);
        }
    }

    private RequestBody wrapRequestBodyWithProgressEmitter(RequestBody requestBody, final RCTDeviceEventEmitter rCTDeviceEventEmitter, final int i) {
        if (requestBody == null) {
            return null;
        }
        return RequestBodyUtil.createProgressRequest(requestBody, new ProgressListener() {
            long last = System.nanoTime();

            public void onProgress(long j, long j2, boolean z) {
                long nanoTime = System.nanoTime();
                if (z || NetworkingModule.shouldDispatch(nanoTime, this.last)) {
                    ResponseUtil.onDataSend(rCTDeviceEventEmitter, i, j, j2);
                    this.last = nanoTime;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void readWithProgress(RCTDeviceEventEmitter rCTDeviceEventEmitter, int i, ResponseBody responseBody) throws IOException {
        long j;
        Charset charset;
        long j2 = -1;
        try {
            ProgressResponseBody progressResponseBody = (ProgressResponseBody) responseBody;
            j = progressResponseBody.totalBytesRead();
            try {
                j2 = progressResponseBody.contentLength();
            } catch (ClassCastException unused) {
            }
        } catch (ClassCastException unused2) {
            j = -1;
        }
        if (responseBody.contentType() == null) {
            charset = StandardCharsets.UTF_8;
        } else {
            charset = responseBody.contentType().charset(StandardCharsets.UTF_8);
        }
        ProgressiveStringDecoder progressiveStringDecoder = new ProgressiveStringDecoder(charset);
        InputStream byteStream = responseBody.byteStream();
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                int read = byteStream.read(bArr);
                if (read != -1) {
                    ResponseUtil.onIncrementalDataReceived(rCTDeviceEventEmitter, i, progressiveStringDecoder.decodeNext(bArr, read), j, j2);
                } else {
                    return;
                }
            }
        } finally {
            byteStream.close();
        }
    }

    private synchronized void addRequest(int i) {
        this.mRequestIds.add(Integer.valueOf(i));
    }

    /* access modifiers changed from: private */
    public synchronized void removeRequest(int i) {
        this.mRequestIds.remove(Integer.valueOf(i));
    }

    private synchronized void cancelAllRequests() {
        for (Integer intValue : this.mRequestIds) {
            cancelRequest(intValue.intValue());
        }
        this.mRequestIds.clear();
    }

    /* access modifiers changed from: private */
    public static WritableMap translateHeaders(Headers headers) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < headers.size(); i++) {
            String name = headers.name(i);
            if (bundle.containsKey(name)) {
                StringBuilder sb = new StringBuilder();
                sb.append(bundle.getString(name));
                sb.append(", ");
                sb.append(headers.value(i));
                bundle.putString(name, sb.toString());
            } else {
                bundle.putString(name, headers.value(i));
            }
        }
        return Arguments.fromBundle(bundle);
    }

    @ReactMethod
    public void abortRequest(int i) {
        cancelRequest(i);
        removeRequest(i);
    }

    private void cancelRequest(final int i) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            /* access modifiers changed from: protected */
            public void doInBackgroundGuarded(Void... voidArr) {
                OkHttpCallUtil.cancelTag(NetworkingModule.this.mClient, Integer.valueOf(i));
            }
        }.execute(new Void[0]);
    }

    @ReactMethod
    public void clearCookies(com.facebook.react.bridge.Callback callback) {
        this.mCookieHandler.clearCookies(callback);
    }

    @Nullable
    private MultipartBody.Builder constructMultipartBody(ReadableArray readableArray, String str, int i) {
        MediaType mediaType;
        RCTDeviceEventEmitter eventEmitter = getEventEmitter();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MediaType.parse(str));
        int size = readableArray.size();
        for (int i2 = 0; i2 < size; i2++) {
            ReadableMap map = readableArray.getMap(i2);
            Headers extractHeaders = extractHeaders(map.getArray("headers"), null);
            if (extractHeaders == null) {
                ResponseUtil.onRequestError(eventEmitter, i, "Missing or invalid header format for FormData part.", null);
                return null;
            }
            String str2 = CONTENT_TYPE_HEADER_NAME;
            String str3 = extractHeaders.get(str2);
            if (str3 != null) {
                mediaType = MediaType.parse(str3);
                extractHeaders = extractHeaders.newBuilder().removeAll(str2).build();
            } else {
                mediaType = null;
            }
            String str4 = REQUEST_BODY_KEY_STRING;
            if (map.hasKey(str4)) {
                builder.addPart(extractHeaders, RequestBody.create(mediaType, map.getString(str4)));
            } else {
                String str5 = "uri";
                if (!map.hasKey(str5)) {
                    ResponseUtil.onRequestError(eventEmitter, i, "Unrecognized FormData part.", null);
                } else if (mediaType == null) {
                    ResponseUtil.onRequestError(eventEmitter, i, "Binary FormData part needs a content-type header.", null);
                    return null;
                } else {
                    String string = map.getString(str5);
                    InputStream fileInputStream = RequestBodyUtil.getFileInputStream(getReactApplicationContext(), string);
                    if (fileInputStream == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Could not retrieve file for uri ");
                        sb.append(string);
                        ResponseUtil.onRequestError(eventEmitter, i, sb.toString(), null);
                        return null;
                    }
                    builder.addPart(extractHeaders, RequestBodyUtil.create(mediaType, fileInputStream));
                }
            }
        }
        return builder;
    }

    @Nullable
    private Headers extractHeaders(@Nullable ReadableArray readableArray, @Nullable ReadableMap readableMap) {
        if (readableArray == null) {
            return null;
        }
        Headers.Builder builder = new Headers.Builder();
        int size = readableArray.size();
        boolean z = false;
        int i = 0;
        while (i < size) {
            ReadableArray array = readableArray.getArray(i);
            if (array != null && array.size() == 2) {
                String stripHeaderName = HeaderUtil.stripHeaderName(array.getString(0));
                String stripHeaderValue = HeaderUtil.stripHeaderValue(array.getString(1));
                if (!(stripHeaderName == null || stripHeaderValue == null)) {
                    builder.add(stripHeaderName, stripHeaderValue);
                    i++;
                }
            }
            return null;
        }
        String str = USER_AGENT_HEADER_NAME;
        if (builder.get(str) == null) {
            String str2 = this.mDefaultUserAgent;
            if (str2 != null) {
                builder.add(str, str2);
            }
        }
        if (readableMap != null && readableMap.hasKey(REQUEST_BODY_KEY_STRING)) {
            z = true;
        }
        if (!z) {
            builder.removeAll(CONTENT_ENCODING_HEADER_NAME);
        }
        return builder.build();
    }

    private RCTDeviceEventEmitter getEventEmitter() {
        return (RCTDeviceEventEmitter) getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class);
    }
}
