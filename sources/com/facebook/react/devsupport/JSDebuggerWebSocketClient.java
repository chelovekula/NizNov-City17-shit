package com.facebook.react.devsupport;

import android.util.JsonWriter;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.C1069Response;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class JSDebuggerWebSocketClient extends WebSocketListener {
    private static final String TAG = "JSDebuggerWebSocketClient";
    private final ConcurrentHashMap<Integer, JSDebuggerCallback> mCallbacks = new ConcurrentHashMap<>();
    @Nullable
    private JSDebuggerCallback mConnectCallback;
    @Nullable
    private OkHttpClient mHttpClient;
    private final AtomicInteger mRequestID = new AtomicInteger();
    @Nullable
    private WebSocket mWebSocket;

    public interface JSDebuggerCallback {
        void onFailure(Throwable th);

        void onSuccess(@Nullable String str);
    }

    public void connect(String str, JSDebuggerCallback jSDebuggerCallback) {
        if (this.mHttpClient == null) {
            this.mConnectCallback = jSDebuggerCallback;
            this.mHttpClient = new Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(0, TimeUnit.MINUTES).build();
            this.mHttpClient.newWebSocket(new Request.Builder().url(str).build(), this);
            return;
        }
        throw new IllegalStateException("JSDebuggerWebSocketClient is already initialized.");
    }

    public void prepareJSRuntime(JSDebuggerCallback jSDebuggerCallback) {
        int andIncrement = this.mRequestID.getAndIncrement();
        this.mCallbacks.put(Integer.valueOf(andIncrement), jSDebuggerCallback);
        try {
            StringWriter stringWriter = new StringWriter();
            new JsonWriter(stringWriter).beginObject().name("id").value((long) andIncrement).name("method").value("prepareJSRuntime").endObject().close();
            sendMessage(andIncrement, stringWriter.toString());
        } catch (IOException e) {
            triggerRequestFailure(andIncrement, e);
        }
    }

    public void loadApplicationScript(String str, HashMap<String, String> hashMap, JSDebuggerCallback jSDebuggerCallback) {
        int andIncrement = this.mRequestID.getAndIncrement();
        this.mCallbacks.put(Integer.valueOf(andIncrement), jSDebuggerCallback);
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter beginObject = new JsonWriter(stringWriter).beginObject().name("id").value((long) andIncrement).name("method").value("executeApplicationScript").name(ImagesContract.URL).value(str).name("inject").beginObject();
            for (String str2 : hashMap.keySet()) {
                beginObject.name(str2).value((String) hashMap.get(str2));
            }
            beginObject.endObject().endObject().close();
            sendMessage(andIncrement, stringWriter.toString());
        } catch (IOException e) {
            triggerRequestFailure(andIncrement, e);
        }
    }

    public void executeJSCall(String str, String str2, JSDebuggerCallback jSDebuggerCallback) {
        int andIncrement = this.mRequestID.getAndIncrement();
        this.mCallbacks.put(Integer.valueOf(andIncrement), jSDebuggerCallback);
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.beginObject().name("id").value((long) andIncrement).name("method").value(str);
            stringWriter.append(",\"arguments\":").append(str2);
            jsonWriter.endObject().close();
            sendMessage(andIncrement, stringWriter.toString());
        } catch (IOException e) {
            triggerRequestFailure(andIncrement, e);
        }
    }

    public void closeQuietly() {
        WebSocket webSocket = this.mWebSocket;
        if (webSocket != null) {
            try {
                webSocket.close(1000, "End of session");
            } catch (Exception unused) {
            }
            this.mWebSocket = null;
        }
    }

    private void sendMessage(int i, String str) {
        WebSocket webSocket = this.mWebSocket;
        if (webSocket == null) {
            triggerRequestFailure(i, new IllegalStateException("WebSocket connection no longer valid"));
            return;
        }
        try {
            webSocket.send(str);
        } catch (Exception e) {
            triggerRequestFailure(i, e);
        }
    }

    private void triggerRequestFailure(int i, Throwable th) {
        JSDebuggerCallback jSDebuggerCallback = (JSDebuggerCallback) this.mCallbacks.get(Integer.valueOf(i));
        if (jSDebuggerCallback != null) {
            this.mCallbacks.remove(Integer.valueOf(i));
            jSDebuggerCallback.onFailure(th);
        }
    }

    private void triggerRequestSuccess(int i, @Nullable String str) {
        JSDebuggerCallback jSDebuggerCallback = (JSDebuggerCallback) this.mCallbacks.get(Integer.valueOf(i));
        if (jSDebuggerCallback != null) {
            this.mCallbacks.remove(Integer.valueOf(i));
            jSDebuggerCallback.onSuccess(str);
        }
    }

    /* JADX WARNING: type inference failed for: r5v1, types: [java.lang.Integer] */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r6v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.lang.Integer] */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r6v5, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r5v6, types: [java.lang.Integer] */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v9 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r6v10 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r6v11 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r6v4
      assigns: []
      uses: []
      mth insns count: 57
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 7 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMessage(okhttp3.WebSocket r5, java.lang.String r6) {
        /*
            r4 = this;
            r5 = 0
            android.util.JsonReader r0 = new android.util.JsonReader     // Catch:{ IOException -> 0x0062 }
            java.io.StringReader r1 = new java.io.StringReader     // Catch:{ IOException -> 0x0062 }
            r1.<init>(r6)     // Catch:{ IOException -> 0x0062 }
            r0.<init>(r1)     // Catch:{ IOException -> 0x0062 }
            r0.beginObject()     // Catch:{ IOException -> 0x0062 }
            r6 = r5
        L_0x000f:
            boolean r1 = r0.hasNext()     // Catch:{ IOException -> 0x0062 }
            if (r1 == 0) goto L_0x0058
            java.lang.String r1 = r0.nextName()     // Catch:{ IOException -> 0x0062 }
            android.util.JsonToken r2 = android.util.JsonToken.NULL     // Catch:{ IOException -> 0x0062 }
            android.util.JsonToken r3 = r0.peek()     // Catch:{ IOException -> 0x0062 }
            if (r2 != r3) goto L_0x0025
            r0.skipValue()     // Catch:{ IOException -> 0x0062 }
            goto L_0x000f
        L_0x0025:
            java.lang.String r2 = "replyID"
            boolean r2 = r2.equals(r1)     // Catch:{ IOException -> 0x0062 }
            if (r2 == 0) goto L_0x0036
            int r1 = r0.nextInt()     // Catch:{ IOException -> 0x0062 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r1)     // Catch:{ IOException -> 0x0062 }
            goto L_0x000f
        L_0x0036:
            java.lang.String r2 = "result"
            boolean r2 = r2.equals(r1)     // Catch:{ IOException -> 0x0062 }
            if (r2 == 0) goto L_0x0043
            java.lang.String r6 = r0.nextString()     // Catch:{ IOException -> 0x0062 }
            goto L_0x000f
        L_0x0043:
            java.lang.String r2 = "error"
            boolean r1 = r2.equals(r1)     // Catch:{ IOException -> 0x0062 }
            if (r1 == 0) goto L_0x000f
            java.lang.String r1 = r0.nextString()     // Catch:{ IOException -> 0x0062 }
            com.facebook.react.common.JavascriptException r2 = new com.facebook.react.common.JavascriptException     // Catch:{ IOException -> 0x0062 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x0062 }
            r4.abort(r1, r2)     // Catch:{ IOException -> 0x0062 }
            goto L_0x000f
        L_0x0058:
            if (r5 == 0) goto L_0x0072
            int r0 = r5.intValue()     // Catch:{ IOException -> 0x0062 }
            r4.triggerRequestSuccess(r0, r6)     // Catch:{ IOException -> 0x0062 }
            goto L_0x0072
        L_0x0062:
            r6 = move-exception
            if (r5 == 0) goto L_0x006d
            int r5 = r5.intValue()
            r4.triggerRequestFailure(r5, r6)
            goto L_0x0072
        L_0x006d:
            java.lang.String r5 = "Parsing response message from websocket failed"
            r4.abort(r5, r6)
        L_0x0072:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.devsupport.JSDebuggerWebSocketClient.onMessage(okhttp3.WebSocket, java.lang.String):void");
    }

    public void onFailure(WebSocket webSocket, Throwable th, C1069Response response) {
        abort("Websocket exception", th);
    }

    public void onOpen(WebSocket webSocket, C1069Response response) {
        this.mWebSocket = webSocket;
        ((JSDebuggerCallback) Assertions.assertNotNull(this.mConnectCallback)).onSuccess(null);
        this.mConnectCallback = null;
    }

    public void onClosed(WebSocket webSocket, int i, String str) {
        this.mWebSocket = null;
    }

    private void abort(String str, Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error occurred, shutting down websocket connection: ");
        sb.append(str);
        FLog.m51e(TAG, sb.toString(), th);
        closeQuietly();
        JSDebuggerCallback jSDebuggerCallback = this.mConnectCallback;
        if (jSDebuggerCallback != null) {
            jSDebuggerCallback.onFailure(th);
            this.mConnectCallback = null;
        }
        for (JSDebuggerCallback onFailure : this.mCallbacks.values()) {
            onFailure.onFailure(th);
        }
        this.mCallbacks.clear();
    }
}
