package com.facebook.react.animated;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class StyleAnimatedNode extends AnimatedNode {
    private final NativeAnimatedNodesManager mNativeAnimatedNodesManager;
    private final Map<String, Integer> mPropMapping = new HashMap();

    StyleAnimatedNode(ReadableMap readableMap, NativeAnimatedNodesManager nativeAnimatedNodesManager) {
        ReadableMap map = readableMap.getMap("style");
        ReadableMapKeySetIterator keySetIterator = map.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String nextKey = keySetIterator.nextKey();
            this.mPropMapping.put(nextKey, Integer.valueOf(map.getInt(nextKey)));
        }
        this.mNativeAnimatedNodesManager = nativeAnimatedNodesManager;
    }

    public void collectViewUpdates(JavaOnlyMap javaOnlyMap) {
        for (Entry entry : this.mPropMapping.entrySet()) {
            AnimatedNode nodeById = this.mNativeAnimatedNodesManager.getNodeById(((Integer) entry.getValue()).intValue());
            if (nodeById == null) {
                throw new IllegalArgumentException("Mapped style node does not exists");
            } else if (nodeById instanceof TransformAnimatedNode) {
                ((TransformAnimatedNode) nodeById).collectViewUpdates(javaOnlyMap);
            } else if (nodeById instanceof ValueAnimatedNode) {
                javaOnlyMap.putDouble((String) entry.getKey(), ((ValueAnimatedNode) nodeById).getValue());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported type of node used in property node ");
                sb.append(nodeById.getClass());
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }
}
