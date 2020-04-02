package com.facebook.react.bridge;

import com.facebook.infer.annotation.Assertions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSIModuleRegistry {
    private final Map<JSIModuleType, JSIModuleHolder> mModules = new HashMap();

    public JSIModule getModule(JSIModuleType jSIModuleType) {
        JSIModuleHolder jSIModuleHolder = (JSIModuleHolder) this.mModules.get(jSIModuleType);
        if (jSIModuleHolder != null) {
            return (JSIModule) Assertions.assertNotNull(jSIModuleHolder.getJSIModule());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find JSIModule for class ");
        sb.append(jSIModuleType);
        throw new IllegalArgumentException(sb.toString());
    }

    public void registerModules(List<JSIModuleSpec> list) {
        for (JSIModuleSpec jSIModuleSpec : list) {
            this.mModules.put(jSIModuleSpec.getJSIModuleType(), new JSIModuleHolder(jSIModuleSpec));
        }
    }

    public void notifyJSInstanceDestroy() {
        for (Entry entry : this.mModules.entrySet()) {
            if (((JSIModuleType) entry.getKey()) != JSIModuleType.TurboModuleManager) {
                ((JSIModuleHolder) entry.getValue()).notifyJSInstanceDestroy();
            }
        }
    }
}
