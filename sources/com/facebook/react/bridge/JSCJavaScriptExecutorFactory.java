package com.facebook.react.bridge;

import com.facebook.react.common.ReactConstants;

public class JSCJavaScriptExecutorFactory implements JavaScriptExecutorFactory {
    private final String mAppName;
    private final String mDeviceName;

    public String toString() {
        return "JSCExecutor";
    }

    public JSCJavaScriptExecutorFactory(String str, String str2) {
        this.mAppName = str;
        this.mDeviceName = str2;
    }

    public JavaScriptExecutor create() throws Exception {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putString("OwnerIdentity", ReactConstants.TAG);
        writableNativeMap.putString("AppIdentity", this.mAppName);
        writableNativeMap.putString("DeviceIdentity", this.mDeviceName);
        return new JSCJavaScriptExecutor(writableNativeMap);
    }

    public void startSamplingProfiler() {
        StringBuilder sb = new StringBuilder();
        sb.append("Starting sampling profiler not supported on ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public void stopSamplingProfiler(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("Stopping sampling profiler not supported on ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }
}
