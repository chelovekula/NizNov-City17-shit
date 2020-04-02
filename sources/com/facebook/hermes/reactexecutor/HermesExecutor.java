package com.facebook.hermes.reactexecutor;

import com.facebook.hermes.instrumentation.HermesMemoryDumper;
import com.facebook.jni.HybridData;
import com.facebook.react.bridge.JavaScriptExecutor;
import com.facebook.soloader.SoLoader;
import javax.annotation.Nullable;

public class HermesExecutor extends JavaScriptExecutor {
    private static String mode_;

    public static native boolean canLoadFile(String str);

    private static native HybridData initHybrid(long j, boolean z, int i, boolean z2, @Nullable HermesMemoryDumper hermesMemoryDumper, long j2, long j3);

    private static native HybridData initHybridDefaultConfig();

    static {
        SoLoader.loadLibrary("hermes");
        try {
            SoLoader.loadLibrary("hermes-executor-release");
            mode_ = "Release";
        } catch (UnsatisfiedLinkError unused) {
            SoLoader.loadLibrary("hermes-executor-debug");
            mode_ = "Debug";
        }
    }

    HermesExecutor(@Nullable RuntimeConfig runtimeConfig) {
        HybridData hybridData;
        if (runtimeConfig == null) {
            hybridData = initHybridDefaultConfig();
        } else {
            hybridData = initHybrid(runtimeConfig.heapSizeMB, runtimeConfig.es6Symbol, runtimeConfig.bytecodeWarmupPercent, runtimeConfig.tripWireEnabled, runtimeConfig.heapDumper, runtimeConfig.tripWireCooldownMS, runtimeConfig.tripWireLimitBytes);
        }
        super(hybridData);
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("HermesExecutor");
        sb.append(mode_);
        return sb.toString();
    }
}
