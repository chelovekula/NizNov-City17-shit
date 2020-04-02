package com.facebook.react.jscexecutor;

import com.facebook.react.bridge.JavaScriptExecutor;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.ReactConstants;

public class JSCExecutorFactory implements JavaScriptExecutorFactory {
    private final String mAppName;
    private final String mDeviceName;

    public String toString() {
        return "JSIExecutor+JSCRuntime";
    }

    public JSCExecutorFactory(String str, String str2) {
        this.mAppName = str;
        this.mDeviceName = str2;
    }

    public JavaScriptExecutor create() throws Exception {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putString("OwnerIdentity", ReactConstants.TAG);
        writableNativeMap.putString("AppIdentity", this.mAppName);
        writableNativeMap.putString("DeviceIdentity", this.mDeviceName);
        return new JSCExecutor(writableNativeMap);
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
