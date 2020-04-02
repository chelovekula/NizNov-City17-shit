package com.facebook.react.modules.common;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.common.ReactConstants;

public class ModuleDataCleaner {

    public interface Cleanable {
        void clearSensitiveData();
    }

    public static void cleanDataFromModules(CatalystInstance catalystInstance) {
        for (NativeModule nativeModule : catalystInstance.getNativeModules()) {
            if (nativeModule instanceof Cleanable) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cleaning data from ");
                sb.append(nativeModule.getName());
                FLog.m38d(ReactConstants.TAG, sb.toString());
                ((Cleanable) nativeModule).clearSensitiveData();
            }
        }
    }
}
