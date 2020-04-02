package com.swmansion.reanimated.nodes;

import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.MapUtils;
import com.swmansion.reanimated.NodesManager;

public class CondNode extends Node {
    private final int mCondID;
    private final int mElseBlockID;
    private final int mIfBlockID;

    public CondNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mCondID = MapUtils.getInt(readableMap, "cond", "Reanimated: First argument passed to cond node is either of wrong type or is missing.");
        String str = "Reanimated: Second argument passed to cond node is either of wrong type or is missing.";
        this.mIfBlockID = MapUtils.getInt(readableMap, "ifBlock", str);
        String str2 = "elseBlock";
        this.mElseBlockID = readableMap.hasKey(str2) ? MapUtils.getInt(readableMap, str2, str) : -1;
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        Object nodeValue = this.mNodesManager.getNodeValue(this.mCondID);
        if (!(nodeValue instanceof Number) || ((Number) nodeValue).doubleValue() == 0.0d) {
            return this.mElseBlockID != -1 ? this.mNodesManager.getNodeValue(this.mElseBlockID) : ZERO;
        }
        return this.mIfBlockID != -1 ? this.mNodesManager.getNodeValue(this.mIfBlockID) : ZERO;
    }
}
