package com.facebook.react.modules.websocket;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.facebook.react.modules.network.ForwardingCookieHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.WebSocket;
import okio.ByteString;

@ReactModule(hasConstants = false, name = "WebSocketModule")
public final class WebSocketModule extends ReactContextBaseJavaModule {
    public static final String NAME = "WebSocketModule";
    /* access modifiers changed from: private */
    public final Map<Integer, ContentHandler> mContentHandlers = new ConcurrentHashMap();
    private ForwardingCookieHandler mCookieHandler;
    private ReactContext mReactContext;
    /* access modifiers changed from: private */
    public final Map<Integer, WebSocket> mWebSocketConnections = new ConcurrentHashMap();

    public interface ContentHandler {
        void onMessage(String str, WritableMap writableMap);

        void onMessage(ByteString byteString, WritableMap writableMap);
    }

    public String getName() {
        return NAME;
    }

    public WebSocketModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.mReactContext = reactApplicationContext;
        this.mCookieHandler = new ForwardingCookieHandler(reactApplicationContext);
    }

    /* access modifiers changed from: private */
    public void sendEvent(String str, WritableMap writableMap) {
        ((RCTDeviceEventEmitter) this.mReactContext.getJSModule(RCTDeviceEventEmitter.class)).emit(str, writableMap);
    }

    public void setContentHandler(int i, ContentHandler contentHandler) {
        if (contentHandler != null) {
            this.mContentHandlers.put(Integer.valueOf(i), contentHandler);
        } else {
            this.mContentHandlers.remove(Integer.valueOf(i));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e4  */
    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(java.lang.String r7, @androidx.annotation.Nullable com.facebook.react.bridge.ReadableArray r8, @androidx.annotation.Nullable com.facebook.react.bridge.ReadableMap r9, final int r10) {
        /*
            r6 = this;
            okhttp3.OkHttpClient$Builder r0 = new okhttp3.OkHttpClient$Builder
            r0.<init>()
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS
            r2 = 10
            okhttp3.OkHttpClient$Builder r0 = r0.connectTimeout(r2, r1)
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS
            okhttp3.OkHttpClient$Builder r0 = r0.writeTimeout(r2, r1)
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MINUTES
            r2 = 0
            okhttp3.OkHttpClient$Builder r0 = r0.readTimeout(r2, r1)
            okhttp3.OkHttpClient r0 = r0.build()
            okhttp3.Request$Builder r1 = new okhttp3.Request$Builder
            r1.<init>()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r10)
            okhttp3.Request$Builder r1 = r1.tag(r2)
            okhttp3.Request$Builder r1 = r1.url(r7)
            java.lang.String r2 = r6.getCookie(r7)
            if (r2 == 0) goto L_0x003b
            java.lang.String r3 = "Cookie"
            r1.addHeader(r3, r2)
        L_0x003b:
            java.lang.String r2 = "origin"
            if (r9 == 0) goto L_0x00a2
            java.lang.String r3 = "headers"
            boolean r4 = r9.hasKey(r3)
            if (r4 == 0) goto L_0x00a2
            com.facebook.react.bridge.ReadableType r4 = r9.getType(r3)
            com.facebook.react.bridge.ReadableType r5 = com.facebook.react.bridge.ReadableType.Map
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x00a2
            com.facebook.react.bridge.ReadableMap r9 = r9.getMap(r3)
            com.facebook.react.bridge.ReadableMapKeySetIterator r3 = r9.keySetIterator()
            boolean r4 = r9.hasKey(r2)
            if (r4 != 0) goto L_0x0068
            java.lang.String r7 = getDefaultOrigin(r7)
            r1.addHeader(r2, r7)
        L_0x0068:
            boolean r7 = r3.hasNextKey()
            if (r7 == 0) goto L_0x00a9
            java.lang.String r7 = r3.nextKey()
            com.facebook.react.bridge.ReadableType r2 = com.facebook.react.bridge.ReadableType.String
            com.facebook.react.bridge.ReadableType r4 = r9.getType(r7)
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0086
            java.lang.String r2 = r9.getString(r7)
            r1.addHeader(r7, r2)
            goto L_0x0068
        L_0x0086:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "Ignoring: requested "
            r2.append(r4)
            r2.append(r7)
            java.lang.String r7 = ", value not a string"
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            java.lang.String r2 = "ReactNative"
            com.facebook.common.logging.FLog.m90w(r2, r7)
            goto L_0x0068
        L_0x00a2:
            java.lang.String r7 = getDefaultOrigin(r7)
            r1.addHeader(r2, r7)
        L_0x00a9:
            if (r8 == 0) goto L_0x00fa
            int r7 = r8.size()
            if (r7 <= 0) goto L_0x00fa
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r9 = ""
            r7.<init>(r9)
            r2 = 0
        L_0x00b9:
            int r3 = r8.size()
            if (r2 >= r3) goto L_0x00de
            java.lang.String r3 = r8.getString(r2)
            java.lang.String r3 = r3.trim()
            boolean r4 = r3.isEmpty()
            if (r4 != 0) goto L_0x00db
            java.lang.String r4 = ","
            boolean r5 = r3.contains(r4)
            if (r5 != 0) goto L_0x00db
            r7.append(r3)
            r7.append(r4)
        L_0x00db:
            int r2 = r2 + 1
            goto L_0x00b9
        L_0x00de:
            int r8 = r7.length()
            if (r8 <= 0) goto L_0x00fa
            int r8 = r7.length()
            int r8 = r8 + -1
            int r2 = r7.length()
            r7.replace(r8, r2, r9)
            java.lang.String r7 = r7.toString()
            java.lang.String r8 = "Sec-WebSocket-Protocol"
            r1.addHeader(r8, r7)
        L_0x00fa:
            okhttp3.Request r7 = r1.build()
            com.facebook.react.modules.websocket.WebSocketModule$1 r8 = new com.facebook.react.modules.websocket.WebSocketModule$1
            r8.<init>(r10)
            r0.newWebSocket(r7, r8)
            okhttp3.Dispatcher r7 = r0.dispatcher()
            java.util.concurrent.ExecutorService r7 = r7.executorService()
            r7.shutdown()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.websocket.WebSocketModule.connect(java.lang.String, com.facebook.react.bridge.ReadableArray, com.facebook.react.bridge.ReadableMap, int):void");
    }

    @ReactMethod
    public void close(int i, String str, int i2) {
        WebSocket webSocket = (WebSocket) this.mWebSocketConnections.get(Integer.valueOf(i2));
        if (webSocket != null) {
            try {
                webSocket.close(i, str);
                this.mWebSocketConnections.remove(Integer.valueOf(i2));
                this.mContentHandlers.remove(Integer.valueOf(i2));
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Could not close WebSocket connection for id ");
                sb.append(i2);
                FLog.m51e(ReactConstants.TAG, sb.toString(), (Throwable) e);
            }
        }
    }

    @ReactMethod
    public void send(String str, int i) {
        WebSocket webSocket = (WebSocket) this.mWebSocketConnections.get(Integer.valueOf(i));
        if (webSocket == null) {
            WritableMap createMap = Arguments.createMap();
            String str2 = "id";
            createMap.putInt(str2, i);
            String str3 = "client is null";
            createMap.putString("message", str3);
            sendEvent("websocketFailed", createMap);
            WritableMap createMap2 = Arguments.createMap();
            createMap2.putInt(str2, i);
            createMap2.putInt("code", 0);
            createMap2.putString("reason", str3);
            sendEvent("websocketClosed", createMap2);
            this.mWebSocketConnections.remove(Integer.valueOf(i));
            this.mContentHandlers.remove(Integer.valueOf(i));
            return;
        }
        try {
            webSocket.send(str);
        } catch (Exception e) {
            notifyWebSocketFailed(i, e.getMessage());
        }
    }

    @ReactMethod
    public void sendBinary(String str, int i) {
        WebSocket webSocket = (WebSocket) this.mWebSocketConnections.get(Integer.valueOf(i));
        if (webSocket == null) {
            WritableMap createMap = Arguments.createMap();
            String str2 = "id";
            createMap.putInt(str2, i);
            String str3 = "client is null";
            createMap.putString("message", str3);
            sendEvent("websocketFailed", createMap);
            WritableMap createMap2 = Arguments.createMap();
            createMap2.putInt(str2, i);
            createMap2.putInt("code", 0);
            createMap2.putString("reason", str3);
            sendEvent("websocketClosed", createMap2);
            this.mWebSocketConnections.remove(Integer.valueOf(i));
            this.mContentHandlers.remove(Integer.valueOf(i));
            return;
        }
        try {
            webSocket.send(ByteString.decodeBase64(str));
        } catch (Exception e) {
            notifyWebSocketFailed(i, e.getMessage());
        }
    }

    public void sendBinary(ByteString byteString, int i) {
        WebSocket webSocket = (WebSocket) this.mWebSocketConnections.get(Integer.valueOf(i));
        if (webSocket == null) {
            WritableMap createMap = Arguments.createMap();
            String str = "id";
            createMap.putInt(str, i);
            String str2 = "client is null";
            createMap.putString("message", str2);
            sendEvent("websocketFailed", createMap);
            WritableMap createMap2 = Arguments.createMap();
            createMap2.putInt(str, i);
            createMap2.putInt("code", 0);
            createMap2.putString("reason", str2);
            sendEvent("websocketClosed", createMap2);
            this.mWebSocketConnections.remove(Integer.valueOf(i));
            this.mContentHandlers.remove(Integer.valueOf(i));
            return;
        }
        try {
            webSocket.send(byteString);
        } catch (Exception e) {
            notifyWebSocketFailed(i, e.getMessage());
        }
    }

    @ReactMethod
    public void ping(int i) {
        WebSocket webSocket = (WebSocket) this.mWebSocketConnections.get(Integer.valueOf(i));
        if (webSocket == null) {
            WritableMap createMap = Arguments.createMap();
            String str = "id";
            createMap.putInt(str, i);
            String str2 = "client is null";
            createMap.putString("message", str2);
            sendEvent("websocketFailed", createMap);
            WritableMap createMap2 = Arguments.createMap();
            createMap2.putInt(str, i);
            createMap2.putInt("code", 0);
            createMap2.putString("reason", str2);
            sendEvent("websocketClosed", createMap2);
            this.mWebSocketConnections.remove(Integer.valueOf(i));
            this.mContentHandlers.remove(Integer.valueOf(i));
            return;
        }
        try {
            webSocket.send(ByteString.EMPTY);
        } catch (Exception e) {
            notifyWebSocketFailed(i, e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void notifyWebSocketFailed(int i, String str) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("id", i);
        createMap.putString("message", str);
        sendEvent("websocketFailed", createMap);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0053 A[Catch:{ URISyntaxException -> 0x00bf }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007e A[Catch:{ URISyntaxException -> 0x00bf }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0093 A[Catch:{ URISyntaxException -> 0x00bf }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00ae A[Catch:{ URISyntaxException -> 0x00bf }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getDefaultOrigin(java.lang.String r12) {
        /*
            java.lang.String r0 = ""
            java.net.URI r1 = new java.net.URI     // Catch:{ URISyntaxException -> 0x00bf }
            r1.<init>(r12)     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r2 = r1.getScheme()     // Catch:{ URISyntaxException -> 0x00bf }
            int r3 = r2.hashCode()     // Catch:{ URISyntaxException -> 0x00bf }
            r4 = 3804(0xedc, float:5.33E-42)
            java.lang.String r5 = "https"
            java.lang.String r6 = "http"
            r7 = -1
            r8 = 0
            r9 = 3
            r10 = 2
            r11 = 1
            if (r3 == r4) goto L_0x0046
            r4 = 118039(0x1cd17, float:1.65408E-40)
            if (r3 == r4) goto L_0x003c
            r4 = 3213448(0x310888, float:4.503E-39)
            if (r3 == r4) goto L_0x0034
            r4 = 99617003(0x5f008eb, float:2.2572767E-35)
            if (r3 == r4) goto L_0x002c
            goto L_0x0050
        L_0x002c:
            boolean r2 = r2.equals(r5)     // Catch:{ URISyntaxException -> 0x00bf }
            if (r2 == 0) goto L_0x0050
            r2 = 3
            goto L_0x0051
        L_0x0034:
            boolean r2 = r2.equals(r6)     // Catch:{ URISyntaxException -> 0x00bf }
            if (r2 == 0) goto L_0x0050
            r2 = 2
            goto L_0x0051
        L_0x003c:
            java.lang.String r3 = "wss"
            boolean r2 = r2.equals(r3)     // Catch:{ URISyntaxException -> 0x00bf }
            if (r2 == 0) goto L_0x0050
            r2 = 0
            goto L_0x0051
        L_0x0046:
            java.lang.String r3 = "ws"
            boolean r2 = r2.equals(r3)     // Catch:{ URISyntaxException -> 0x00bf }
            if (r2 == 0) goto L_0x0050
            r2 = 1
            goto L_0x0051
        L_0x0050:
            r2 = -1
        L_0x0051:
            if (r2 == 0) goto L_0x007e
            if (r2 == r11) goto L_0x006e
            if (r2 == r10) goto L_0x005a
            if (r2 == r9) goto L_0x005a
            goto L_0x008d
        L_0x005a:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ URISyntaxException -> 0x00bf }
            r2.<init>()     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r0)     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r1.getScheme()     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r0)     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r2.toString()     // Catch:{ URISyntaxException -> 0x00bf }
            goto L_0x008d
        L_0x006e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ URISyntaxException -> 0x00bf }
            r2.<init>()     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r0)     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r6)     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r2.toString()     // Catch:{ URISyntaxException -> 0x00bf }
            goto L_0x008d
        L_0x007e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ URISyntaxException -> 0x00bf }
            r2.<init>()     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r0)     // Catch:{ URISyntaxException -> 0x00bf }
            r2.append(r5)     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r2.toString()     // Catch:{ URISyntaxException -> 0x00bf }
        L_0x008d:
            int r2 = r1.getPort()     // Catch:{ URISyntaxException -> 0x00bf }
            if (r2 == r7) goto L_0x00ae
            java.lang.String r2 = "%s://%s:%s"
            java.lang.Object[] r3 = new java.lang.Object[r9]     // Catch:{ URISyntaxException -> 0x00bf }
            r3[r8] = r0     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r1.getHost()     // Catch:{ URISyntaxException -> 0x00bf }
            r3[r11] = r0     // Catch:{ URISyntaxException -> 0x00bf }
            int r0 = r1.getPort()     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ URISyntaxException -> 0x00bf }
            r3[r10] = r0     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r12 = java.lang.String.format(r2, r3)     // Catch:{ URISyntaxException -> 0x00bf }
            goto L_0x00be
        L_0x00ae:
            java.lang.String r2 = "%s://%s"
            java.lang.Object[] r3 = new java.lang.Object[r10]     // Catch:{ URISyntaxException -> 0x00bf }
            r3[r8] = r0     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r0 = r1.getHost()     // Catch:{ URISyntaxException -> 0x00bf }
            r3[r11] = r0     // Catch:{ URISyntaxException -> 0x00bf }
            java.lang.String r12 = java.lang.String.format(r2, r3)     // Catch:{ URISyntaxException -> 0x00bf }
        L_0x00be:
            return r12
        L_0x00bf:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unable to set "
            r1.append(r2)
            r1.append(r12)
            java.lang.String r12 = " as default origin header"
            r1.append(r12)
            java.lang.String r12 = r1.toString()
            r0.<init>(r12)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.websocket.WebSocketModule.getDefaultOrigin(java.lang.String):java.lang.String");
    }

    private String getCookie(String str) {
        try {
            List list = (List) this.mCookieHandler.get(new URI(getDefaultOrigin(str)), new HashMap()).get("Cookie");
            if (list != null) {
                if (!list.isEmpty()) {
                    return (String) list.get(0);
                }
            }
            return null;
        } catch (IOException | URISyntaxException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to get cookie from ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
