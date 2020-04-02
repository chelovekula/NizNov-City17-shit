package com.swmansion.reanimated.nodes;

import com.brentvatne.react.ReactVideoView;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.swmansion.reanimated.NodesManager;
import javax.annotation.Nullable;

public class ValueNode extends Node {
    private Object mValue;

    public ValueNode(int i, @Nullable ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        if (readableMap != null) {
            String str = ReactVideoView.EVENT_PROP_METADATA_VALUE;
            if (readableMap.hasKey(str)) {
                ReadableType type = readableMap.getType(str);
                if (type == ReadableType.String) {
                    this.mValue = readableMap.getString(str);
                } else if (type == ReadableType.Number) {
                    this.mValue = Double.valueOf(readableMap.getDouble(str));
                } else if (type == ReadableType.Null) {
                    this.mValue = null;
                } else {
                    throw new IllegalStateException("Not supported value type. Must be boolean, number or string");
                }
                return;
            }
        }
        this.mValue = null;
    }

    public void setValue(Object obj) {
        this.mValue = obj;
        forceUpdateMemoizedValue(this.mValue);
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        return this.mValue;
    }
}
