package com.swmansion.reanimated.nodes;

import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.C1007Utils;
import com.swmansion.reanimated.NodesManager;

public class BlockNode extends Node {
    private final int[] mBlock;

    public BlockNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mBlock = C1007Utils.processIntArray(readableMap.getArray("block"));
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        Object obj = null;
        for (int findNodeById : this.mBlock) {
            obj = this.mNodesManager.findNodeById(findNodeById, Node.class).value();
        }
        return obj;
    }
}
