package com.facebook.react.bridge;

import androidx.annotation.Nullable;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;

@DoNotStrip
public class ProxyJavaScriptExecutor extends JavaScriptExecutor {
    @Nullable
    private JavaJSExecutor mJavaJSExecutor;

    public static class Factory implements JavaScriptExecutorFactory {
        private final com.facebook.react.bridge.JavaJSExecutor.Factory mJavaJSExecutorFactory;

        public Factory(com.facebook.react.bridge.JavaJSExecutor.Factory factory) {
            this.mJavaJSExecutorFactory = factory;
        }

        public JavaScriptExecutor create() throws Exception {
            return new ProxyJavaScriptExecutor(this.mJavaJSExecutorFactory.create());
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

    private static native HybridData initHybrid(JavaJSExecutor javaJSExecutor);

    public String getName() {
        return "ProxyJavaScriptExecutor";
    }

    static {
        ReactBridge.staticInit();
    }

    public ProxyJavaScriptExecutor(JavaJSExecutor javaJSExecutor) {
        super(initHybrid(javaJSExecutor));
        this.mJavaJSExecutor = javaJSExecutor;
    }

    public void close() {
        JavaJSExecutor javaJSExecutor = this.mJavaJSExecutor;
        if (javaJSExecutor != null) {
            javaJSExecutor.close();
            this.mJavaJSExecutor = null;
        }
    }
}
