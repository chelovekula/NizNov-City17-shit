package com.swmansion.reanimated.nodes;

import com.brentvatne.react.ReactVideoView;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.NodesManager;

public class FunctionNode extends Node {
    private final int mWhatNodeID;

    public FunctionNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mWhatNodeID = readableMap.getInt(ReactVideoView.EVENT_PROP_WHAT);
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        return this.mNodesManager.findNodeById(this.mWhatNodeID, Node.class).value();
    }
}
