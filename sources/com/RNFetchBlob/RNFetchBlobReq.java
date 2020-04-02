package com.RNFetchBlob;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.os.Build.VERSION;
import androidx.core.app.NotificationCompat;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.C1069Response;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.TlsVersion;

public class RNFetchBlobReq extends BroadcastReceiver implements Runnable {
    public static HashMap<String, Long> androidDownloadManagerTaskTable = new HashMap<>();
    static ConnectionPool pool = new ConnectionPool();
    static HashMap<String, RNFetchBlobProgressConfig> progressReport = new HashMap<>();
    public static HashMap<String, Call> taskTable = new HashMap<>();
    static HashMap<String, RNFetchBlobProgressConfig> uploadProgressReport = new HashMap<>();
    Callback callback;
    OkHttpClient client;
    long contentLength;
    String destPath;
    long downloadManagerId;
    ReadableMap headers;
    String method;
    RNFetchBlobConfig options;
    String rawRequestBody;
    ReadableArray rawRequestBodyArray;
    ArrayList<String> redirects = new ArrayList<>();
    RNFetchBlobBody requestBody;
    RequestType requestType;
    WritableMap respInfo;
    ResponseFormat responseFormat = ResponseFormat.Auto;
    ResponseType responseType;
    String taskId;
    boolean timeout = false;
    String url;

    /* renamed from: com.RNFetchBlob.RNFetchBlobReq$4 */
    static /* synthetic */ class C04244 {
        static final /* synthetic */ int[] $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType = new int[RequestType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType = new int[ResponseType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0047 */
        static {
            /*
                com.RNFetchBlob.RNFetchBlobReq$ResponseType[] r0 = com.RNFetchBlob.RNFetchBlobReq.ResponseType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType = r0
                r0 = 1
                int[] r1 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.RNFetchBlob.RNFetchBlobReq$ResponseType r2 = com.RNFetchBlob.RNFetchBlobReq.ResponseType.KeepInMemory     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.RNFetchBlob.RNFetchBlobReq$ResponseType r3 = com.RNFetchBlob.RNFetchBlobReq.ResponseType.FileStorage     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                com.RNFetchBlob.RNFetchBlobReq$RequestType[] r2 = com.RNFetchBlob.RNFetchBlobReq.RequestType.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType = r2
                int[] r2 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ NoSuchFieldError -> 0x0032 }
                com.RNFetchBlob.RNFetchBlobReq$RequestType r3 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ NoSuchFieldError -> 0x0032 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0032 }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0032 }
            L_0x0032:
                int[] r0 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ NoSuchFieldError -> 0x003c }
                com.RNFetchBlob.RNFetchBlobReq$RequestType r2 = com.RNFetchBlob.RNFetchBlobReq.RequestType.AsIs     // Catch:{ NoSuchFieldError -> 0x003c }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x003c }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x003c }
            L_0x003c:
                int[] r0 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ NoSuchFieldError -> 0x0047 }
                com.RNFetchBlob.RNFetchBlobReq$RequestType r1 = com.RNFetchBlob.RNFetchBlobReq.RequestType.Form     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = $SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ NoSuchFieldError -> 0x0052 }
                com.RNFetchBlob.RNFetchBlobReq$RequestType r1 = com.RNFetchBlob.RNFetchBlobReq.RequestType.WithoutBody     // Catch:{ NoSuchFieldError -> 0x0052 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0052 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0052 }
            L_0x0052:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.C04244.<clinit>():void");
        }
    }

    enum RequestType {
        Form,
        SingleFile,
        AsIs,
        WithoutBody,
        Others
    }

    enum ResponseFormat {
        Auto,
        UTF8,
        BASE64
    }

    enum ResponseType {
        KeepInMemory,
        FileStorage
    }

    public RNFetchBlobReq(ReadableMap readableMap, String str, String str2, String str3, ReadableMap readableMap2, String str4, ReadableArray readableArray, OkHttpClient okHttpClient, Callback callback2) {
        this.method = str2.toUpperCase();
        this.options = new RNFetchBlobConfig(readableMap);
        this.taskId = str;
        this.url = str3;
        this.headers = readableMap2;
        this.callback = callback2;
        this.rawRequestBody = str4;
        this.rawRequestBodyArray = readableArray;
        this.client = okHttpClient;
        if (this.options.fileCache.booleanValue() || this.options.path != null) {
            this.responseType = ResponseType.FileStorage;
        } else {
            this.responseType = ResponseType.KeepInMemory;
        }
        if (str4 != null) {
            this.requestType = RequestType.SingleFile;
        } else if (readableArray != null) {
            this.requestType = RequestType.Form;
        } else {
            this.requestType = RequestType.WithoutBody;
        }
    }

    public static void cancelTask(String str) {
        if (taskTable.containsKey(str)) {
            ((Call) taskTable.get(str)).cancel();
            taskTable.remove(str);
        }
        if (androidDownloadManagerTaskTable.containsKey(str)) {
            long longValue = ((Long) androidDownloadManagerTaskTable.get(str)).longValue();
            ((DownloadManager) C0404RNFetchBlob.RCTContext.getApplicationContext().getSystemService("download")).remove(new long[]{longValue});
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:142:0x0358 A[Catch:{ Exception -> 0x04a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x040e A[Catch:{ Exception -> 0x04a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x0458 A[Catch:{ Exception -> 0x04a7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r15 = this;
            java.lang.String r0 = "Content-Type"
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r2 = "path"
            r3 = 2
            r4 = 1
            if (r1 == 0) goto L_0x0118
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r5 = "useDownloadManager"
            boolean r1 = r1.hasKey(r5)
            if (r1 == 0) goto L_0x0118
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            boolean r1 = r1.getBoolean(r5)
            if (r1 == 0) goto L_0x0118
            java.lang.String r0 = r15.url
            android.net.Uri r0 = android.net.Uri.parse(r0)
            android.app.DownloadManager$Request r1 = new android.app.DownloadManager$Request
            r1.<init>(r0)
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r5 = "notification"
            boolean r0 = r0.hasKey(r5)
            if (r0 == 0) goto L_0x0047
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            boolean r0 = r0.getBoolean(r5)
            if (r0 == 0) goto L_0x0047
            r1.setNotificationVisibility(r4)
            goto L_0x004a
        L_0x0047:
            r1.setNotificationVisibility(r3)
        L_0x004a:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r3 = "title"
            boolean r0 = r0.hasKey(r3)
            if (r0 == 0) goto L_0x0061
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r0 = r0.getString(r3)
            r1.setTitle(r0)
        L_0x0061:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r3 = "description"
            boolean r0 = r0.hasKey(r3)
            if (r0 == 0) goto L_0x0078
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r0 = r0.getString(r3)
            r1.setDescription(r0)
        L_0x0078:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            boolean r0 = r0.hasKey(r2)
            if (r0 == 0) goto L_0x00a2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "file://"
            r0.append(r3)
            com.RNFetchBlob.RNFetchBlobConfig r3 = r15.options
            com.facebook.react.bridge.ReadableMap r3 = r3.addAndroidDownloads
            java.lang.String r2 = r3.getString(r2)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.net.Uri r0 = android.net.Uri.parse(r0)
            r1.setDestinationUri(r0)
        L_0x00a2:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r2 = "mime"
            boolean r0 = r0.hasKey(r2)
            if (r0 == 0) goto L_0x00b9
            com.RNFetchBlob.RNFetchBlobConfig r0 = r15.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r0 = r0.getString(r2)
            r1.setMimeType(r0)
        L_0x00b9:
            com.facebook.react.bridge.ReadableMap r0 = r15.headers
            com.facebook.react.bridge.ReadableMapKeySetIterator r0 = r0.keySetIterator()
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options
            com.facebook.react.bridge.ReadableMap r2 = r2.addAndroidDownloads
            java.lang.String r3 = "mediaScannable"
            boolean r2 = r2.hasKey(r3)
            if (r2 == 0) goto L_0x00d8
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options
            com.facebook.react.bridge.ReadableMap r2 = r2.addAndroidDownloads
            boolean r2 = r2.hasKey(r3)
            if (r2 == 0) goto L_0x00d8
            r1.allowScanningByMediaScanner()
        L_0x00d8:
            boolean r2 = r0.hasNextKey()
            if (r2 == 0) goto L_0x00ec
            java.lang.String r2 = r0.nextKey()
            com.facebook.react.bridge.ReadableMap r3 = r15.headers
            java.lang.String r3 = r3.getString(r2)
            r1.addRequestHeader(r2, r3)
            goto L_0x00d8
        L_0x00ec:
            com.facebook.react.bridge.ReactApplicationContext r0 = com.RNFetchBlob.C0404RNFetchBlob.RCTContext
            android.content.Context r0 = r0.getApplicationContext()
            java.lang.String r2 = "download"
            java.lang.Object r2 = r0.getSystemService(r2)
            android.app.DownloadManager r2 = (android.app.DownloadManager) r2
            long r1 = r2.enqueue(r1)
            r15.downloadManagerId = r1
            java.util.HashMap<java.lang.String, java.lang.Long> r1 = androidDownloadManagerTaskTable
            java.lang.String r2 = r15.taskId
            long r3 = r15.downloadManagerId
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1.put(r2, r3)
            android.content.IntentFilter r1 = new android.content.IntentFilter
            java.lang.String r2 = "android.intent.action.DOWNLOAD_COMPLETE"
            r1.<init>(r2)
            r0.registerReceiver(r15, r1)
            return
        L_0x0118:
            java.lang.String r1 = r15.taskId
            com.RNFetchBlob.RNFetchBlobConfig r5 = r15.options
            java.lang.String r5 = r5.appendExt
            boolean r5 = r5.isEmpty()
            java.lang.String r6 = ""
            if (r5 == 0) goto L_0x0128
            r5 = r6
            goto L_0x013d
        L_0x0128:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "."
            r5.append(r7)
            com.RNFetchBlob.RNFetchBlobConfig r7 = r15.options
            java.lang.String r7 = r7.appendExt
            r5.append(r7)
            java.lang.String r5 = r5.toString()
        L_0x013d:
            com.RNFetchBlob.RNFetchBlobConfig r7 = r15.options
            java.lang.String r7 = r7.key
            r8 = 3
            r9 = 0
            r10 = 0
            if (r7 == 0) goto L_0x0182
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options
            java.lang.String r1 = r1.key
            java.lang.String r1 = com.RNFetchBlob.RNFetchBlobUtils.getMD5(r1)
            if (r1 != 0) goto L_0x0152
            java.lang.String r1 = r15.taskId
        L_0x0152:
            java.io.File r7 = new java.io.File
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r1)
            r11.append(r12)
            r11.append(r5)
            java.lang.String r11 = r11.toString()
            r7.<init>(r11)
            boolean r11 = r7.exists()
            if (r11 == 0) goto L_0x0182
            com.facebook.react.bridge.Callback r0 = r15.callback
            java.lang.Object[] r1 = new java.lang.Object[r8]
            r1[r10] = r9
            r1[r4] = r2
            java.lang.String r2 = r7.getAbsolutePath()
            r1[r3] = r2
            r0.invoke(r1)
            return
        L_0x0182:
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options
            java.lang.String r2 = r2.path
            if (r2 == 0) goto L_0x018f
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options
            java.lang.String r1 = r1.path
            r15.destPath = r1
            goto L_0x01ae
        L_0x018f:
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options
            java.lang.Boolean r2 = r2.fileCache
            boolean r2 = r2.booleanValue()
            if (r2 == 0) goto L_0x01ae
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r1 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r1)
            r2.append(r1)
            r2.append(r5)
            java.lang.String r1 = r2.toString()
            r15.destPath = r1
        L_0x01ae:
            com.RNFetchBlob.RNFetchBlobConfig r1 = r15.options     // Catch:{ Exception -> 0x04a7 }
            java.lang.Boolean r1 = r1.trusty     // Catch:{ Exception -> 0x04a7 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x04a7 }
            if (r1 == 0) goto L_0x01bf
            okhttp3.OkHttpClient r1 = r15.client     // Catch:{ Exception -> 0x04a7 }
            okhttp3.OkHttpClient$Builder r1 = com.RNFetchBlob.RNFetchBlobUtils.getUnsafeOkHttpClient(r1)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x01c5
        L_0x01bf:
            okhttp3.OkHttpClient r1 = r15.client     // Catch:{ Exception -> 0x04a7 }
            okhttp3.OkHttpClient$Builder r1 = r1.newBuilder()     // Catch:{ Exception -> 0x04a7 }
        L_0x01c5:
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            java.lang.Boolean r2 = r2.wifiOnly     // Catch:{ Exception -> 0x04a7 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x04a7 }
            if (r2 == 0) goto L_0x0231
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x04a7 }
            r5 = 21
            if (r2 < r5) goto L_0x022c
            com.facebook.react.bridge.ReactApplicationContext r2 = com.RNFetchBlob.C0404RNFetchBlob.RCTContext     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReactApplicationContext r5 = com.RNFetchBlob.C0404RNFetchBlob.RCTContext     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r5 = "connectivity"
            java.lang.Object r2 = r2.getSystemService(r5)     // Catch:{ Exception -> 0x04a7 }
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2     // Catch:{ Exception -> 0x04a7 }
            android.net.Network[] r5 = r2.getAllNetworks()     // Catch:{ Exception -> 0x04a7 }
            int r7 = r5.length     // Catch:{ Exception -> 0x04a7 }
            r11 = 0
        L_0x01e7:
            if (r11 >= r7) goto L_0x0216
            r12 = r5[r11]     // Catch:{ Exception -> 0x04a7 }
            android.net.NetworkInfo r13 = r2.getNetworkInfo(r12)     // Catch:{ Exception -> 0x04a7 }
            android.net.NetworkCapabilities r14 = r2.getNetworkCapabilities(r12)     // Catch:{ Exception -> 0x04a7 }
            if (r14 == 0) goto L_0x0213
            if (r13 != 0) goto L_0x01f8
            goto L_0x0213
        L_0x01f8:
            boolean r13 = r13.isConnected()     // Catch:{ Exception -> 0x04a7 }
            if (r13 != 0) goto L_0x01ff
            goto L_0x0213
        L_0x01ff:
            boolean r13 = r14.hasTransport(r4)     // Catch:{ Exception -> 0x04a7 }
            if (r13 == 0) goto L_0x0213
            java.net.Proxy r2 = java.net.Proxy.NO_PROXY     // Catch:{ Exception -> 0x04a7 }
            r1.proxy(r2)     // Catch:{ Exception -> 0x04a7 }
            javax.net.SocketFactory r2 = r12.getSocketFactory()     // Catch:{ Exception -> 0x04a7 }
            r1.socketFactory(r2)     // Catch:{ Exception -> 0x04a7 }
            r2 = 1
            goto L_0x0217
        L_0x0213:
            int r11 = r11 + 1
            goto L_0x01e7
        L_0x0216:
            r2 = 0
        L_0x0217:
            if (r2 != 0) goto L_0x0231
            com.facebook.react.bridge.Callback r0 = r15.callback     // Catch:{ Exception -> 0x04a7 }
            java.lang.Object[] r1 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r2 = "No available WiFi connections."
            r1[r10] = r2     // Catch:{ Exception -> 0x04a7 }
            r1[r4] = r9     // Catch:{ Exception -> 0x04a7 }
            r1[r3] = r9     // Catch:{ Exception -> 0x04a7 }
            r0.invoke(r1)     // Catch:{ Exception -> 0x04a7 }
            r15.releaseTaskResource()     // Catch:{ Exception -> 0x04a7 }
            return
        L_0x022c:
            java.lang.String r2 = "RNFetchBlob: wifiOnly was set, but SDK < 21. wifiOnly was ignored."
            com.RNFetchBlob.RNFetchBlobUtils.emitWarningEvent(r2)     // Catch:{ Exception -> 0x04a7 }
        L_0x0231:
            okhttp3.Request$Builder r2 = new okhttp3.Request$Builder     // Catch:{ Exception -> 0x04a7 }
            r2.<init>()     // Catch:{ Exception -> 0x04a7 }
            java.net.URL r5 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0241 }
            java.lang.String r7 = r15.url     // Catch:{ MalformedURLException -> 0x0241 }
            r5.<init>(r7)     // Catch:{ MalformedURLException -> 0x0241 }
            r2.url(r5)     // Catch:{ MalformedURLException -> 0x0241 }
            goto L_0x0245
        L_0x0241:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ Exception -> 0x04a7 }
        L_0x0245:
            java.util.HashMap r5 = new java.util.HashMap     // Catch:{ Exception -> 0x04a7 }
            r5.<init>()     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReadableMap r7 = r15.headers     // Catch:{ Exception -> 0x04a7 }
            if (r7 == 0) goto L_0x0295
            com.facebook.react.bridge.ReadableMap r7 = r15.headers     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReadableMapKeySetIterator r7 = r7.keySetIterator()     // Catch:{ Exception -> 0x04a7 }
        L_0x0254:
            boolean r11 = r7.hasNextKey()     // Catch:{ Exception -> 0x04a7 }
            if (r11 == 0) goto L_0x0295
            java.lang.String r11 = r7.nextKey()     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReadableMap r12 = r15.headers     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r12 = r12.getString(r11)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r13 = "RNFB-Response"
            boolean r13 = r11.equalsIgnoreCase(r13)     // Catch:{ Exception -> 0x04a7 }
            if (r13 == 0) goto L_0x0286
            java.lang.String r11 = "base64"
            boolean r11 = r12.equalsIgnoreCase(r11)     // Catch:{ Exception -> 0x04a7 }
            if (r11 == 0) goto L_0x0279
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r11 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.BASE64     // Catch:{ Exception -> 0x04a7 }
            r15.responseFormat = r11     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0254
        L_0x0279:
            java.lang.String r11 = "utf8"
            boolean r11 = r12.equalsIgnoreCase(r11)     // Catch:{ Exception -> 0x04a7 }
            if (r11 == 0) goto L_0x0254
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r11 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.UTF8     // Catch:{ Exception -> 0x04a7 }
            r15.responseFormat = r11     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0254
        L_0x0286:
            java.lang.String r13 = r11.toLowerCase()     // Catch:{ Exception -> 0x04a7 }
            r2.header(r13, r12)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r11 = r11.toLowerCase()     // Catch:{ Exception -> 0x04a7 }
            r5.put(r11, r12)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0254
        L_0x0295:
            java.lang.String r7 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r11 = "post"
            boolean r7 = r7.equalsIgnoreCase(r11)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r11 = "content-type"
            if (r7 != 0) goto L_0x02bc
            java.lang.String r7 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r12 = "put"
            boolean r7 = r7.equalsIgnoreCase(r12)     // Catch:{ Exception -> 0x04a7 }
            if (r7 != 0) goto L_0x02bc
            java.lang.String r7 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r12 = "patch"
            boolean r7 = r7.equalsIgnoreCase(r12)     // Catch:{ Exception -> 0x04a7 }
            if (r7 == 0) goto L_0x02b6
            goto L_0x02bc
        L_0x02b6:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r0 = com.RNFetchBlob.RNFetchBlobReq.RequestType.WithoutBody     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r0     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0340
        L_0x02bc:
            java.lang.String r7 = r15.getHeaderIgnoreCases(r5, r0)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r7 = r7.toLowerCase()     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReadableArray r12 = r15.rawRequestBodyArray     // Catch:{ Exception -> 0x04a7 }
            if (r12 == 0) goto L_0x02cd
            com.RNFetchBlob.RNFetchBlobReq$RequestType r12 = com.RNFetchBlob.RNFetchBlobReq.RequestType.Form     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r12     // Catch:{ Exception -> 0x04a7 }
            goto L_0x02e2
        L_0x02cd:
            boolean r12 = r7.isEmpty()     // Catch:{ Exception -> 0x04a7 }
            if (r12 == 0) goto L_0x02e2
            boolean r12 = r7.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x04a7 }
            if (r12 != 0) goto L_0x02de
            java.lang.String r12 = "application/octet-stream"
            r2.header(r0, r12)     // Catch:{ Exception -> 0x04a7 }
        L_0x02de:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r12 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r12     // Catch:{ Exception -> 0x04a7 }
        L_0x02e2:
            java.lang.String r12 = r15.rawRequestBody     // Catch:{ Exception -> 0x04a7 }
            if (r12 == 0) goto L_0x0340
            java.lang.String r12 = r15.rawRequestBody     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r13 = "RNFetchBlob-file://"
            boolean r12 = r12.startsWith(r13)     // Catch:{ Exception -> 0x04a7 }
            if (r12 != 0) goto L_0x033c
            java.lang.String r12 = r15.rawRequestBody     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r13 = "RNFetchBlob-content://"
            boolean r12 = r12.startsWith(r13)     // Catch:{ Exception -> 0x04a7 }
            if (r12 == 0) goto L_0x02fb
            goto L_0x033c
        L_0x02fb:
            java.lang.String r12 = r7.toLowerCase()     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r13 = ";base64"
            boolean r12 = r12.contains(r13)     // Catch:{ Exception -> 0x04a7 }
            if (r12 != 0) goto L_0x0319
            java.lang.String r12 = r7.toLowerCase()     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r13 = "application/octet"
            boolean r12 = r12.startsWith(r13)     // Catch:{ Exception -> 0x04a7 }
            if (r12 == 0) goto L_0x0314
            goto L_0x0319
        L_0x0314:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r0 = com.RNFetchBlob.RNFetchBlobReq.RequestType.AsIs     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r0     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0340
        L_0x0319:
            java.lang.String r12 = ";base64"
            java.lang.String r7 = r7.replace(r12, r6)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r12 = ";BASE64"
            java.lang.String r6 = r7.replace(r12, r6)     // Catch:{ Exception -> 0x04a7 }
            boolean r7 = r5.containsKey(r11)     // Catch:{ Exception -> 0x04a7 }
            if (r7 == 0) goto L_0x032e
            r5.put(r11, r6)     // Catch:{ Exception -> 0x04a7 }
        L_0x032e:
            boolean r7 = r5.containsKey(r0)     // Catch:{ Exception -> 0x04a7 }
            if (r7 == 0) goto L_0x0337
            r5.put(r0, r6)     // Catch:{ Exception -> 0x04a7 }
        L_0x0337:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r0 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r0     // Catch:{ Exception -> 0x04a7 }
            goto L_0x0340
        L_0x033c:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r0 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x04a7 }
            r15.requestType = r0     // Catch:{ Exception -> 0x04a7 }
        L_0x0340:
            java.lang.String r0 = "Transfer-Encoding"
            java.lang.String r0 = r15.getHeaderIgnoreCases(r5, r0)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r6 = "chunked"
            boolean r0 = r0.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x04a7 }
            int[] r6 = com.RNFetchBlob.RNFetchBlobReq.C04244.$SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r7 = r15.requestType     // Catch:{ Exception -> 0x04a7 }
            int r7 = r7.ordinal()     // Catch:{ Exception -> 0x04a7 }
            r6 = r6[r7]     // Catch:{ Exception -> 0x04a7 }
            if (r6 == r4) goto L_0x040e
            if (r6 == r3) goto L_0x03e1
            if (r6 == r8) goto L_0x0394
            r0 = 4
            if (r6 == r0) goto L_0x0361
            goto L_0x043a
        L_0x0361:
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = "post"
            boolean r0 = r0.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x04a7 }
            if (r0 != 0) goto L_0x0387
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = "put"
            boolean r0 = r0.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x04a7 }
            if (r0 != 0) goto L_0x0387
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = "patch"
            boolean r0 = r0.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x04a7 }
            if (r0 == 0) goto L_0x0380
            goto L_0x0387
        L_0x0380:
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            r2.method(r0, r9)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x043a
        L_0x0387:
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            byte[] r3 = new byte[r10]     // Catch:{ Exception -> 0x04a7 }
            okhttp3.RequestBody r3 = okhttp3.RequestBody.create(r9, r3)     // Catch:{ Exception -> 0x04a7 }
            r2.method(r0, r3)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x043a
        L_0x0394:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04a7 }
            r3.<init>()     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r5 = "RNFetchBlob-"
            r3.append(r5)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r5 = r15.taskId     // Catch:{ Exception -> 0x04a7 }
            r3.append(r5)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r5 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r6 = r15.taskId     // Catch:{ Exception -> 0x04a7 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r5.chunkedEncoding(r0)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r5 = r15.requestType     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setRequestType(r5)     // Catch:{ Exception -> 0x04a7 }
            com.facebook.react.bridge.ReadableArray r5 = r15.rawRequestBodyArray     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setBody(r5)     // Catch:{ Exception -> 0x04a7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04a7 }
            r5.<init>()     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r6 = "multipart/form-data; boundary="
            r5.append(r6)     // Catch:{ Exception -> 0x04a7 }
            r5.append(r3)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r5.toString()     // Catch:{ Exception -> 0x04a7 }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setMIME(r3)     // Catch:{ Exception -> 0x04a7 }
            r15.requestBody = r0     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r3 = r15.requestBody     // Catch:{ Exception -> 0x04a7 }
            r2.method(r0, r3)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x043a
        L_0x03e1:
            com.RNFetchBlob.RNFetchBlobBody r3 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r6 = r15.taskId     // Catch:{ Exception -> 0x04a7 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r3.chunkedEncoding(r0)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r3 = r15.requestType     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setRequestType(r3)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r15.rawRequestBody     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setBody(r3)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r15.getHeaderIgnoreCases(r5, r11)     // Catch:{ Exception -> 0x04a7 }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setMIME(r3)     // Catch:{ Exception -> 0x04a7 }
            r15.requestBody = r0     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r3 = r15.requestBody     // Catch:{ Exception -> 0x04a7 }
            r2.method(r0, r3)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x043a
        L_0x040e:
            com.RNFetchBlob.RNFetchBlobBody r3 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r6 = r15.taskId     // Catch:{ Exception -> 0x04a7 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r3.chunkedEncoding(r0)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r3 = r15.requestType     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setRequestType(r3)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r15.rawRequestBody     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setBody(r3)     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r3 = r15.getHeaderIgnoreCases(r5, r11)     // Catch:{ Exception -> 0x04a7 }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r0 = r0.setMIME(r3)     // Catch:{ Exception -> 0x04a7 }
            r15.requestBody = r0     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r0 = r15.method     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobBody r3 = r15.requestBody     // Catch:{ Exception -> 0x04a7 }
            r2.method(r0, r3)     // Catch:{ Exception -> 0x04a7 }
        L_0x043a:
            okhttp3.Request r0 = r2.build()     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$1 r2 = new com.RNFetchBlob.RNFetchBlobReq$1     // Catch:{ Exception -> 0x04a7 }
            r2.<init>()     // Catch:{ Exception -> 0x04a7 }
            r1.addNetworkInterceptor(r2)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$2 r2 = new com.RNFetchBlob.RNFetchBlobReq$2     // Catch:{ Exception -> 0x04a7 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x04a7 }
            r1.addInterceptor(r2)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            long r2 = r2.timeout     // Catch:{ Exception -> 0x04a7 }
            r5 = 0
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 < 0) goto L_0x046a
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            long r2 = r2.timeout     // Catch:{ Exception -> 0x04a7 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Exception -> 0x04a7 }
            r1.connectTimeout(r2, r5)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            long r2 = r2.timeout     // Catch:{ Exception -> 0x04a7 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Exception -> 0x04a7 }
            r1.readTimeout(r2, r5)     // Catch:{ Exception -> 0x04a7 }
        L_0x046a:
            okhttp3.ConnectionPool r2 = pool     // Catch:{ Exception -> 0x04a7 }
            r1.connectionPool(r2)     // Catch:{ Exception -> 0x04a7 }
            r1.retryOnConnectionFailure(r10)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            java.lang.Boolean r2 = r2.followRedirect     // Catch:{ Exception -> 0x04a7 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x04a7 }
            r1.followRedirects(r2)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobConfig r2 = r15.options     // Catch:{ Exception -> 0x04a7 }
            java.lang.Boolean r2 = r2.followRedirect     // Catch:{ Exception -> 0x04a7 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x04a7 }
            r1.followSslRedirects(r2)     // Catch:{ Exception -> 0x04a7 }
            r1.retryOnConnectionFailure(r4)     // Catch:{ Exception -> 0x04a7 }
            okhttp3.OkHttpClient$Builder r1 = enableTls12OnPreLollipop(r1)     // Catch:{ Exception -> 0x04a7 }
            okhttp3.OkHttpClient r1 = r1.build()     // Catch:{ Exception -> 0x04a7 }
            okhttp3.Call r0 = r1.newCall(r0)     // Catch:{ Exception -> 0x04a7 }
            java.util.HashMap<java.lang.String, okhttp3.Call> r1 = taskTable     // Catch:{ Exception -> 0x04a7 }
            java.lang.String r2 = r15.taskId     // Catch:{ Exception -> 0x04a7 }
            r1.put(r2, r0)     // Catch:{ Exception -> 0x04a7 }
            com.RNFetchBlob.RNFetchBlobReq$3 r1 = new com.RNFetchBlob.RNFetchBlobReq$3     // Catch:{ Exception -> 0x04a7 }
            r1.<init>()     // Catch:{ Exception -> 0x04a7 }
            r0.enqueue(r1)     // Catch:{ Exception -> 0x04a7 }
            goto L_0x04d3
        L_0x04a7:
            r0 = move-exception
            r0.printStackTrace()
            r15.releaseTaskResource()
            com.facebook.react.bridge.Callback r1 = r15.callback
            java.lang.Object[] r2 = new java.lang.Object[r4]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "RNFetchBlob request error: "
            r3.append(r4)
            java.lang.String r4 = r0.getMessage()
            r3.append(r4)
            java.lang.Throwable r0 = r0.getCause()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2[r10] = r0
            r1.invoke(r2)
        L_0x04d3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.run():void");
    }

    /* access modifiers changed from: private */
    public void releaseTaskResource() {
        if (taskTable.containsKey(this.taskId)) {
            taskTable.remove(this.taskId);
        }
        if (androidDownloadManagerTaskTable.containsKey(this.taskId)) {
            androidDownloadManagerTaskTable.remove(this.taskId);
        }
        if (uploadProgressReport.containsKey(this.taskId)) {
            uploadProgressReport.remove(this.taskId);
        }
        if (progressReport.containsKey(this.taskId)) {
            progressReport.remove(this.taskId);
        }
        RNFetchBlobBody rNFetchBlobBody = this.requestBody;
        if (rNFetchBlobBody != null) {
            rNFetchBlobBody.clearRequestBody();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:32|33|34|35|(1:37)(1:38)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x011f */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0125 A[Catch:{ IOException -> 0x014a }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0138 A[Catch:{ IOException -> 0x014a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void done(okhttp3.C1069Response r13) {
        /*
            r12 = this;
            boolean r0 = r12.isBlobResponse(r13)
            com.facebook.react.bridge.WritableMap r1 = r12.getResponseInfo(r13, r0)
            r12.emitStateEvent(r1)
            int[] r1 = com.RNFetchBlob.RNFetchBlobReq.C04244.$SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType
            com.RNFetchBlob.RNFetchBlobReq$ResponseType r2 = r12.responseType
            int r2 = r2.ordinal()
            r1 = r1[r2]
            java.lang.String r2 = "path"
            java.lang.String r3 = "UTF-8"
            java.lang.String r4 = "utf8"
            r5 = 3
            r6 = 0
            r7 = 0
            r8 = 1
            r9 = 2
            if (r1 == r8) goto L_0x008c
            if (r1 == r9) goto L_0x004f
            com.facebook.react.bridge.Callback r0 = r12.callback     // Catch:{ IOException -> 0x0040 }
            java.lang.Object[] r1 = new java.lang.Object[r5]     // Catch:{ IOException -> 0x0040 }
            r1[r7] = r6     // Catch:{ IOException -> 0x0040 }
            r1[r8] = r4     // Catch:{ IOException -> 0x0040 }
            java.lang.String r2 = new java.lang.String     // Catch:{ IOException -> 0x0040 }
            okhttp3.ResponseBody r4 = r13.body()     // Catch:{ IOException -> 0x0040 }
            byte[] r4 = r4.bytes()     // Catch:{ IOException -> 0x0040 }
            r2.<init>(r4, r3)     // Catch:{ IOException -> 0x0040 }
            r1[r9] = r2     // Catch:{ IOException -> 0x0040 }
            r0.invoke(r1)     // Catch:{ IOException -> 0x0040 }
            goto L_0x0157
        L_0x0040:
            com.facebook.react.bridge.Callback r0 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r9]
            java.lang.String r2 = "RNFetchBlob failed to encode response data to UTF8 string."
            r1[r7] = r2
            r1[r8] = r6
            r0.invoke(r1)
            goto L_0x0157
        L_0x004f:
            okhttp3.ResponseBody r0 = r13.body()
            r0.bytes()     // Catch:{ Exception -> 0x0056 }
        L_0x0056:
            com.RNFetchBlob.Response.RNFetchBlobFileResp r0 = (com.RNFetchBlob.Response.RNFetchBlobFileResp) r0
            if (r0 == 0) goto L_0x006f
            boolean r0 = r0.isDownloadComplete()
            if (r0 != 0) goto L_0x006f
            com.facebook.react.bridge.Callback r0 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r9]
            java.lang.String r2 = "Download interrupted."
            r1[r7] = r2
            r1[r8] = r6
            r0.invoke(r1)
            goto L_0x0157
        L_0x006f:
            java.lang.String r0 = r12.destPath
            java.lang.String r1 = "?append=true"
            java.lang.String r3 = ""
            java.lang.String r0 = r0.replace(r1, r3)
            r12.destPath = r0
            com.facebook.react.bridge.Callback r0 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r5]
            r1[r7] = r6
            r1[r8] = r2
            java.lang.String r2 = r12.destPath
            r1[r9] = r2
            r0.invoke(r1)
            goto L_0x0157
        L_0x008c:
            if (r0 == 0) goto L_0x00d7
            com.RNFetchBlob.RNFetchBlobConfig r0 = r12.options     // Catch:{ IOException -> 0x014a }
            java.lang.Boolean r0 = r0.auto     // Catch:{ IOException -> 0x014a }
            boolean r0 = r0.booleanValue()     // Catch:{ IOException -> 0x014a }
            if (r0 == 0) goto L_0x00d7
            java.lang.String r0 = r12.taskId     // Catch:{ IOException -> 0x014a }
            java.lang.String r0 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r0)     // Catch:{ IOException -> 0x014a }
            okhttp3.ResponseBody r1 = r13.body()     // Catch:{ IOException -> 0x014a }
            java.io.InputStream r1 = r1.byteStream()     // Catch:{ IOException -> 0x014a }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x014a }
            java.io.File r4 = new java.io.File     // Catch:{ IOException -> 0x014a }
            r4.<init>(r0)     // Catch:{ IOException -> 0x014a }
            r3.<init>(r4)     // Catch:{ IOException -> 0x014a }
            r4 = 10240(0x2800, float:1.4349E-41)
            byte[] r4 = new byte[r4]     // Catch:{ IOException -> 0x014a }
        L_0x00b4:
            int r10 = r1.read(r4)     // Catch:{ IOException -> 0x014a }
            r11 = -1
            if (r10 == r11) goto L_0x00bf
            r3.write(r4, r7, r10)     // Catch:{ IOException -> 0x014a }
            goto L_0x00b4
        L_0x00bf:
            r1.close()     // Catch:{ IOException -> 0x014a }
            r3.flush()     // Catch:{ IOException -> 0x014a }
            r3.close()     // Catch:{ IOException -> 0x014a }
            com.facebook.react.bridge.Callback r1 = r12.callback     // Catch:{ IOException -> 0x014a }
            java.lang.Object[] r3 = new java.lang.Object[r5]     // Catch:{ IOException -> 0x014a }
            r3[r7] = r6     // Catch:{ IOException -> 0x014a }
            r3[r8] = r2     // Catch:{ IOException -> 0x014a }
            r3[r9] = r0     // Catch:{ IOException -> 0x014a }
            r1.invoke(r3)     // Catch:{ IOException -> 0x014a }
            goto L_0x0157
        L_0x00d7:
            okhttp3.ResponseBody r0 = r13.body()     // Catch:{ IOException -> 0x014a }
            byte[] r0 = r0.bytes()     // Catch:{ IOException -> 0x014a }
            java.nio.charset.Charset r1 = java.nio.charset.Charset.forName(r3)     // Catch:{ IOException -> 0x014a }
            java.nio.charset.CharsetEncoder r1 = r1.newEncoder()     // Catch:{ IOException -> 0x014a }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r2 = r12.responseFormat     // Catch:{ IOException -> 0x014a }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r3 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.BASE64     // Catch:{ IOException -> 0x014a }
            java.lang.String r10 = "base64"
            if (r2 != r3) goto L_0x0101
            com.facebook.react.bridge.Callback r1 = r12.callback     // Catch:{ IOException -> 0x014a }
            java.lang.Object[] r2 = new java.lang.Object[r5]     // Catch:{ IOException -> 0x014a }
            r2[r7] = r6     // Catch:{ IOException -> 0x014a }
            r2[r8] = r10     // Catch:{ IOException -> 0x014a }
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r9)     // Catch:{ IOException -> 0x014a }
            r2[r9] = r0     // Catch:{ IOException -> 0x014a }
            r1.invoke(r2)     // Catch:{ IOException -> 0x014a }
            return
        L_0x0101:
            java.nio.ByteBuffer r2 = java.nio.ByteBuffer.wrap(r0)     // Catch:{ CharacterCodingException -> 0x011f }
            java.nio.CharBuffer r2 = r2.asCharBuffer()     // Catch:{ CharacterCodingException -> 0x011f }
            r1.encode(r2)     // Catch:{ CharacterCodingException -> 0x011f }
            java.lang.String r1 = new java.lang.String     // Catch:{ CharacterCodingException -> 0x011f }
            r1.<init>(r0)     // Catch:{ CharacterCodingException -> 0x011f }
            com.facebook.react.bridge.Callback r2 = r12.callback     // Catch:{ CharacterCodingException -> 0x011f }
            java.lang.Object[] r3 = new java.lang.Object[r5]     // Catch:{ CharacterCodingException -> 0x011f }
            r3[r7] = r6     // Catch:{ CharacterCodingException -> 0x011f }
            r3[r8] = r4     // Catch:{ CharacterCodingException -> 0x011f }
            r3[r9] = r1     // Catch:{ CharacterCodingException -> 0x011f }
            r2.invoke(r3)     // Catch:{ CharacterCodingException -> 0x011f }
            goto L_0x0157
        L_0x011f:
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r1 = r12.responseFormat     // Catch:{ IOException -> 0x014a }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r2 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.UTF8     // Catch:{ IOException -> 0x014a }
            if (r1 != r2) goto L_0x0138
            java.lang.String r1 = new java.lang.String     // Catch:{ IOException -> 0x014a }
            r1.<init>(r0)     // Catch:{ IOException -> 0x014a }
            com.facebook.react.bridge.Callback r0 = r12.callback     // Catch:{ IOException -> 0x014a }
            java.lang.Object[] r2 = new java.lang.Object[r5]     // Catch:{ IOException -> 0x014a }
            r2[r7] = r6     // Catch:{ IOException -> 0x014a }
            r2[r8] = r4     // Catch:{ IOException -> 0x014a }
            r2[r9] = r1     // Catch:{ IOException -> 0x014a }
            r0.invoke(r2)     // Catch:{ IOException -> 0x014a }
            goto L_0x0157
        L_0x0138:
            com.facebook.react.bridge.Callback r1 = r12.callback     // Catch:{ IOException -> 0x014a }
            java.lang.Object[] r2 = new java.lang.Object[r5]     // Catch:{ IOException -> 0x014a }
            r2[r7] = r6     // Catch:{ IOException -> 0x014a }
            r2[r8] = r10     // Catch:{ IOException -> 0x014a }
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r9)     // Catch:{ IOException -> 0x014a }
            r2[r9] = r0     // Catch:{ IOException -> 0x014a }
            r1.invoke(r2)     // Catch:{ IOException -> 0x014a }
            goto L_0x0157
        L_0x014a:
            com.facebook.react.bridge.Callback r0 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r9]
            java.lang.String r2 = "RNFetchBlob failed to encode response data to BASE64 string."
            r1[r7] = r2
            r1[r8] = r6
            r0.invoke(r1)
        L_0x0157:
            okhttp3.ResponseBody r13 = r13.body()
            r13.close()
            r12.releaseTaskResource()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.done(okhttp3.Response):void");
    }

    public static RNFetchBlobProgressConfig getReportProgress(String str) {
        if (!progressReport.containsKey(str)) {
            return null;
        }
        return (RNFetchBlobProgressConfig) progressReport.get(str);
    }

    public static RNFetchBlobProgressConfig getReportUploadProgress(String str) {
        if (!uploadProgressReport.containsKey(str)) {
            return null;
        }
        return (RNFetchBlobProgressConfig) uploadProgressReport.get(str);
    }

    private WritableMap getResponseInfo(C1069Response response, boolean z) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt(NotificationCompat.CATEGORY_STATUS, response.code());
        createMap.putString("state", "2");
        createMap.putString("taskId", this.taskId);
        createMap.putBoolean("timeout", this.timeout);
        WritableMap createMap2 = Arguments.createMap();
        for (int i = 0; i < response.headers().size(); i++) {
            createMap2.putString(response.headers().name(i), response.headers().value(i));
        }
        WritableArray createArray = Arguments.createArray();
        Iterator it = this.redirects.iterator();
        while (it.hasNext()) {
            createArray.pushString((String) it.next());
        }
        createMap.putArray("redirects", createArray);
        createMap.putMap("headers", createMap2);
        Headers headers2 = response.headers();
        String str = "respType";
        if (z) {
            createMap.putString(str, "blob");
        } else {
            String str2 = "content-type";
            if (getHeaderIgnoreCases(headers2, str2).equalsIgnoreCase("text/")) {
                createMap.putString(str, "text");
            } else if (getHeaderIgnoreCases(headers2, str2).contains("application/json")) {
                createMap.putString(str, "json");
            } else {
                createMap.putString(str, "");
            }
        }
        return createMap;
    }

    private boolean isBlobResponse(C1069Response response) {
        boolean z;
        String headerIgnoreCases = getHeaderIgnoreCases(response.headers(), "Content-Type");
        boolean z2 = !headerIgnoreCases.equalsIgnoreCase("text/");
        boolean z3 = !headerIgnoreCases.equalsIgnoreCase("application/json");
        if (this.options.binaryContentTypes != null) {
            int i = 0;
            while (true) {
                if (i >= this.options.binaryContentTypes.size()) {
                    break;
                } else if (headerIgnoreCases.toLowerCase().contains(this.options.binaryContentTypes.getString(i).toLowerCase())) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if ((!z3 || z2) && !z) {
                return false;
            }
            return true;
        }
        z = false;
        if (!z3) {
        }
        return false;
    }

    private String getHeaderIgnoreCases(Headers headers2, String str) {
        String str2 = headers2.get(str);
        if (str2 != null) {
            return str2;
        }
        return headers2.get(str.toLowerCase()) == null ? "" : headers2.get(str.toLowerCase());
    }

    private String getHeaderIgnoreCases(HashMap<String, String> hashMap, String str) {
        String str2 = (String) hashMap.get(str);
        if (str2 != null) {
            return str2;
        }
        String str3 = (String) hashMap.get(str.toLowerCase());
        if (str3 == null) {
            str3 = "";
        }
        return str3;
    }

    private void emitStateEvent(WritableMap writableMap) {
        ((RCTDeviceEventEmitter) C0404RNFetchBlob.RCTContext.getJSModule(RCTDeviceEventEmitter.class)).emit(RNFetchBlobConst.EVENT_HTTP_STATE, writableMap);
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x010b A[SYNTHETIC, Splitter:B:33:0x010b] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0148  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(android.content.Context r14, android.content.Intent r15) {
        /*
            r13 = this;
            java.lang.String r14 = "mime"
            java.lang.String r0 = r15.getAction()
            java.lang.String r1 = "android.intent.action.DOWNLOAD_COMPLETE"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x016f
            com.facebook.react.bridge.ReactApplicationContext r0 = com.RNFetchBlob.C0404RNFetchBlob.RCTContext
            android.content.Context r0 = r0.getApplicationContext()
            android.os.Bundle r15 = r15.getExtras()
            java.lang.String r1 = "extra_download_id"
            long r1 = r15.getLong(r1)
            long r3 = r13.downloadManagerId
            int r15 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r15 != 0) goto L_0x016f
            r13.releaseTaskResource()
            android.app.DownloadManager$Query r15 = new android.app.DownloadManager$Query
            r15.<init>()
            r1 = 1
            long[] r2 = new long[r1]
            long r3 = r13.downloadManagerId
            r5 = 0
            r2[r5] = r3
            r15.setFilterById(r2)
            java.lang.String r2 = "download"
            java.lang.Object r2 = r0.getSystemService(r2)
            android.app.DownloadManager r2 = (android.app.DownloadManager) r2
            r2.query(r15)
            android.database.Cursor r15 = r2.query(r15)
            java.lang.String r2 = "Download manager failed to download from  "
            r3 = 3
            r4 = 2
            r6 = 0
            if (r15 != 0) goto L_0x0071
            com.facebook.react.bridge.Callback r14 = r13.callback
            java.lang.Object[] r15 = new java.lang.Object[r3]
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r2)
            java.lang.String r2 = r13.url
            r0.append(r2)
            java.lang.String r2 = ". Query was unsuccessful "
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            r15[r5] = r0
            r15[r1] = r6
            r15[r4] = r6
            r14.invoke(r15)
            return
        L_0x0071:
            boolean r7 = r15.moveToFirst()     // Catch:{ all -> 0x0168 }
            if (r7 == 0) goto L_0x00f9
            java.lang.String r7 = "status"
            int r7 = r15.getColumnIndex(r7)     // Catch:{ all -> 0x0168 }
            int r7 = r15.getInt(r7)     // Catch:{ all -> 0x0168 }
            r8 = 16
            if (r7 != r8) goto L_0x00b1
            com.facebook.react.bridge.Callback r14 = r13.callback     // Catch:{ all -> 0x0168 }
            java.lang.Object[] r0 = new java.lang.Object[r3]     // Catch:{ all -> 0x0168 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0168 }
            r3.<init>()     // Catch:{ all -> 0x0168 }
            r3.append(r2)     // Catch:{ all -> 0x0168 }
            java.lang.String r2 = r13.url     // Catch:{ all -> 0x0168 }
            r3.append(r2)     // Catch:{ all -> 0x0168 }
            java.lang.String r2 = ". Status Code = "
            r3.append(r2)     // Catch:{ all -> 0x0168 }
            r3.append(r7)     // Catch:{ all -> 0x0168 }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0168 }
            r0[r5] = r2     // Catch:{ all -> 0x0168 }
            r0[r1] = r6     // Catch:{ all -> 0x0168 }
            r0[r4] = r6     // Catch:{ all -> 0x0168 }
            r14.invoke(r0)     // Catch:{ all -> 0x0168 }
            if (r15 == 0) goto L_0x00b0
            r15.close()
        L_0x00b0:
            return
        L_0x00b1:
            java.lang.String r2 = "local_uri"
            int r2 = r15.getColumnIndex(r2)     // Catch:{ all -> 0x0168 }
            java.lang.String r2 = r15.getString(r2)     // Catch:{ all -> 0x0168 }
            if (r2 == 0) goto L_0x00f9
            com.RNFetchBlob.RNFetchBlobConfig r7 = r13.options     // Catch:{ all -> 0x0168 }
            com.facebook.react.bridge.ReadableMap r7 = r7.addAndroidDownloads     // Catch:{ all -> 0x0168 }
            boolean r7 = r7.hasKey(r14)     // Catch:{ all -> 0x0168 }
            if (r7 == 0) goto L_0x00f9
            com.RNFetchBlob.RNFetchBlobConfig r7 = r13.options     // Catch:{ all -> 0x0168 }
            com.facebook.react.bridge.ReadableMap r7 = r7.addAndroidDownloads     // Catch:{ all -> 0x0168 }
            java.lang.String r14 = r7.getString(r14)     // Catch:{ all -> 0x0168 }
            java.lang.String r7 = "image"
            boolean r14 = r14.contains(r7)     // Catch:{ all -> 0x0168 }
            if (r14 == 0) goto L_0x00f9
            android.net.Uri r8 = android.net.Uri.parse(r2)     // Catch:{ all -> 0x0168 }
            android.content.ContentResolver r7 = r0.getContentResolver()     // Catch:{ all -> 0x0168 }
            java.lang.String[] r9 = new java.lang.String[r1]     // Catch:{ all -> 0x0168 }
            java.lang.String r14 = "_data"
            r9[r5] = r14     // Catch:{ all -> 0x0168 }
            r10 = 0
            r11 = 0
            r12 = 0
            android.database.Cursor r14 = r7.query(r8, r9, r10, r11, r12)     // Catch:{ all -> 0x0168 }
            if (r14 == 0) goto L_0x00f9
            r14.moveToFirst()     // Catch:{ all -> 0x0168 }
            java.lang.String r0 = r14.getString(r5)     // Catch:{ all -> 0x0168 }
            r14.close()     // Catch:{ all -> 0x0168 }
            goto L_0x00fa
        L_0x00f9:
            r0 = r6
        L_0x00fa:
            if (r15 == 0) goto L_0x00ff
            r15.close()
        L_0x00ff:
            com.RNFetchBlob.RNFetchBlobConfig r14 = r13.options
            com.facebook.react.bridge.ReadableMap r14 = r14.addAndroidDownloads
            java.lang.String r15 = "path"
            boolean r14 = r14.hasKey(r15)
            if (r14 == 0) goto L_0x0148
            com.RNFetchBlob.RNFetchBlobConfig r14 = r13.options     // Catch:{ Exception -> 0x0134 }
            com.facebook.react.bridge.ReadableMap r14 = r14.addAndroidDownloads     // Catch:{ Exception -> 0x0134 }
            java.lang.String r14 = r14.getString(r15)     // Catch:{ Exception -> 0x0134 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0134 }
            r0.<init>(r14)     // Catch:{ Exception -> 0x0134 }
            boolean r0 = r0.exists()     // Catch:{ Exception -> 0x0134 }
            if (r0 == 0) goto L_0x012c
            com.facebook.react.bridge.Callback r0 = r13.callback     // Catch:{ Exception -> 0x0134 }
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0134 }
            r2[r5] = r6     // Catch:{ Exception -> 0x0134 }
            r2[r1] = r15     // Catch:{ Exception -> 0x0134 }
            r2[r4] = r14     // Catch:{ Exception -> 0x0134 }
            r0.invoke(r2)     // Catch:{ Exception -> 0x0134 }
            goto L_0x016f
        L_0x012c:
            java.lang.Exception r14 = new java.lang.Exception     // Catch:{ Exception -> 0x0134 }
            java.lang.String r15 = "Download manager download failed, the file does not downloaded to destination."
            r14.<init>(r15)     // Catch:{ Exception -> 0x0134 }
            throw r14     // Catch:{ Exception -> 0x0134 }
        L_0x0134:
            r14 = move-exception
            r14.printStackTrace()
            com.facebook.react.bridge.Callback r15 = r13.callback
            java.lang.Object[] r0 = new java.lang.Object[r4]
            java.lang.String r14 = r14.getLocalizedMessage()
            r0[r5] = r14
            r0[r1] = r6
            r15.invoke(r0)
            goto L_0x016f
        L_0x0148:
            if (r0 != 0) goto L_0x015a
            com.facebook.react.bridge.Callback r14 = r13.callback
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r2 = "Download manager could not resolve downloaded file path."
            r0[r5] = r2
            r0[r1] = r15
            r0[r4] = r6
            r14.invoke(r0)
            goto L_0x016f
        L_0x015a:
            com.facebook.react.bridge.Callback r14 = r13.callback
            java.lang.Object[] r2 = new java.lang.Object[r3]
            r2[r5] = r6
            r2[r1] = r15
            r2[r4] = r0
            r14.invoke(r2)
            goto L_0x016f
        L_0x0168:
            r14 = move-exception
            if (r15 == 0) goto L_0x016e
            r15.close()
        L_0x016e:
            throw r14
        L_0x016f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.onReceive(android.content.Context, android.content.Intent):void");
    }

    public static Builder enableTls12OnPreLollipop(Builder builder) {
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
            try {
                TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance.init(null);
                TrustManager[] trustManagers = instance.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected default trust managers:");
                    sb.append(Arrays.toString(trustManagers));
                    throw new IllegalStateException(sb.toString());
                }
                X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
                SSLContext instance2 = SSLContext.getInstance("SSL");
                instance2.init(null, new TrustManager[]{x509TrustManager}, null);
                builder.sslSocketFactory(instance2.getSocketFactory(), x509TrustManager);
                ConnectionSpec build = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                ArrayList arrayList = new ArrayList();
                arrayList.add(build);
                arrayList.add(ConnectionSpec.COMPATIBLE_TLS);
                arrayList.add(ConnectionSpec.CLEARTEXT);
                builder.connectionSpecs(arrayList);
            } catch (Exception e) {
                FLog.m51e("OkHttpClientProvider", "Error while enabling TLS 1.2", (Throwable) e);
            }
        }
        return builder;
    }
}
