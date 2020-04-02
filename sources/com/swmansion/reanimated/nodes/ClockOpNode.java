package com.swmansion.reanimated.nodes;

import com.facebook.react.bridge.ReadableMap;
import com.swmansion.reanimated.MapUtils;
import com.swmansion.reanimated.NodesManager;

public abstract class ClockOpNode extends Node {
    private int clockID;

    public static class ClockStartNode extends ClockOpNode {
        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ Object evaluate() {
            return ClockOpNode.super.evaluate();
        }

        public ClockStartNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
            super(i, readableMap, nodesManager);
        }

        /* access modifiers changed from: protected */
        public Double eval(Node node) {
            if (node instanceof ParamNode) {
                ((ParamNode) node).start();
            } else {
                ((ClockNode) node).start();
            }
            return ZERO;
        }
    }

    public static class ClockStopNode extends ClockOpNode {
        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ Object evaluate() {
            return ClockOpNode.super.evaluate();
        }

        public ClockStopNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
            super(i, readableMap, nodesManager);
        }

        /* access modifiers changed from: protected */
        public Double eval(Node node) {
            if (node instanceof ParamNode) {
                ((ParamNode) node).stop();
            } else {
                ((ClockNode) node).stop();
            }
            return ZERO;
        }
    }

    public static class ClockTestNode extends ClockOpNode {
        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ Object evaluate() {
            return ClockOpNode.super.evaluate();
        }

        public ClockTestNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
            super(i, readableMap, nodesManager);
        }

        /* access modifiers changed from: protected */
        public Double eval(Node node) {
            double d = 1.0d;
            if (node instanceof ParamNode) {
                if (!((ParamNode) node).isRunning()) {
                    d = 0.0d;
                }
                return Double.valueOf(d);
            }
            if (!((ClockNode) node).isRunning) {
                d = 0.0d;
            }
            return Double.valueOf(d);
        }
    }

    /* access modifiers changed from: protected */
    public abstract Double eval(Node node);

    public ClockOpNode(int i, ReadableMap readableMap, NodesManager nodesManager) {
        super(i, readableMap, nodesManager);
        this.clockID = MapUtils.getInt(readableMap, "clock", "Reanimated: Argument passed to clock node is either of wrong type or is missing.");
    }

    /* access modifiers changed from: protected */
    public Double evaluate() {
        return eval(this.mNodesManager.findNodeById(this.clockID, Node.class));
    }
}
