package com.swmansion.reanimated.nodes;

import com.brentvatne.react.ReactVideoView;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.C1007Utils;
import com.swmansion.reanimated.NodesManager;
import com.swmansion.reanimated.UpdateContext;

public class CallFuncNode extends Node {
    private final int[] mArgs;
    private final int[] mParams;
    private String mPreviousCallID;
    private final int mWhatNodeID;

    public CallFuncNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.mWhatNodeID = readableMap.getInt(ReactVideoView.EVENT_PROP_WHAT);
        this.mParams = C1007Utils.processIntArray(readableMap.getArray("params"));
        this.mArgs = C1007Utils.processIntArray(readableMap.getArray("args"));
    }

    private void beginContext() {
        this.mPreviousCallID = this.mNodesManager.updateContext.callID;
        UpdateContext updateContext = this.mNodesManager.updateContext;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mNodesManager.updateContext.callID);
        sb.append('/');
        sb.append(String.valueOf(this.mNodeID));
        updateContext.callID = sb.toString();
        int i = 0;
        while (true) {
            int[] iArr = this.mParams;
            if (i < iArr.length) {
                ((ParamNode) this.mNodesManager.findNodeById(iArr[i], ParamNode.class)).beginContext(Integer.valueOf(this.mArgs[i]), this.mPreviousCallID);
                i++;
            } else {
                return;
            }
        }
    }

    private void endContext() {
        int i = 0;
        while (true) {
            int[] iArr = this.mParams;
            if (i < iArr.length) {
                ((ParamNode) this.mNodesManager.findNodeById(iArr[i], ParamNode.class)).endContext();
                i++;
            } else {
                this.mNodesManager.updateContext.callID = this.mPreviousCallID;
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object evaluate() {
        beginContext();
        Object value = this.mNodesManager.findNodeById(this.mWhatNodeID, Node.class).value();
        endContext();
        return value;
    }
}
