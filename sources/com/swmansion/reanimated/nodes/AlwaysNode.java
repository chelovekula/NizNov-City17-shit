package com.swmansion.reanimated.nodes;

import com.brentvatne.react.ReactVideoView;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.MapUtils;
import com.swmansion.reanimated.NodesManager;

public class AlwaysNode extends Node implements FinalNode {
    private int mNodeToBeEvaluated;

    public AlwaysNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mNodeToBeEvaluated = MapUtils.getInt(readableMap, ReactVideoView.EVENT_PROP_WHAT, "Reanimated: Argument passed to always node is either of wrong type or is missing.");
    }

    public void update() {
        value();
    }

    /* access modifiers changed from: protected */
    public Double evaluate() {
        this.mNodesManager.findNodeById(this.mNodeToBeEvaluated, Node.class).value();
        return ZERO;
    }
}
