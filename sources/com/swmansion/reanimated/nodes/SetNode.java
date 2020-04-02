package com.swmansion.reanimated.nodes;

import com.brentvatne.react.ReactVideoView;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.MapUtils;
import com.swmansion.reanimated.NodesManager;

public class SetNode extends Node {
    private int mValueNodeID;
    private int mWhatNodeID;

    public SetNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mWhatNodeID = MapUtils.getInt(readableMap, ReactVideoView.EVENT_PROP_WHAT, "Reanimated: First argument passed to set node is either of wrong type or is missing.");
        this.mValueNodeID = MapUtils.getInt(readableMap, ReactVideoView.EVENT_PROP_METADATA_VALUE, "Reanimated: Second argument passed to set node is either of wrong type or is missing.");
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        Object nodeValue = this.mNodesManager.getNodeValue(this.mValueNodeID);
        ((ValueNode) this.mNodesManager.findNodeById(this.mWhatNodeID, ValueNode.class)).setValue(nodeValue);
        return nodeValue;
    }
}
