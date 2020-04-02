package com.facebook.react.uimanager;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.facebook.react.common.SingleThreadAsserter;

public class ShadowNodeRegistry {
    private final SparseBooleanArray mRootTags = new SparseBooleanArray();
    private final SparseArray<ReactShadowNode> mTagsToCSSNodes = new SparseArray<>();
    private final SingleThreadAsserter mThreadAsserter = new SingleThreadAsserter();

    public void addRootNode(ReactShadowNode reactShadowNode) {
        this.mThreadAsserter.assertNow();
        int reactTag = reactShadowNode.getReactTag();
        this.mTagsToCSSNodes.put(reactTag, reactShadowNode);
        this.mRootTags.put(reactTag, true);
    }

    public void removeRootNode(int i) {
        this.mThreadAsserter.assertNow();
        if (i != -1) {
            if (this.mRootTags.get(i)) {
                this.mTagsToCSSNodes.remove(i);
                this.mRootTags.delete(i);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" is not registered as a root view");
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    public void addNode(ReactShadowNode reactShadowNode) {
        this.mThreadAsserter.assertNow();
        this.mTagsToCSSNodes.put(reactShadowNode.getReactTag(), reactShadowNode);
    }

    public void removeNode(int i) {
        this.mThreadAsserter.assertNow();
        if (!this.mRootTags.get(i)) {
            this.mTagsToCSSNodes.remove(i);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Trying to remove root node ");
        sb.append(i);
        sb.append(" without using removeRootNode!");
        throw new IllegalViewOperationException(sb.toString());
    }

    public ReactShadowNode getNode(int i) {
        this.mThreadAsserter.assertNow();
        return (ReactShadowNode) this.mTagsToCSSNodes.get(i);
    }

    public boolean isRootNode(int i) {
        this.mThreadAsserter.assertNow();
        return this.mRootTags.get(i);
    }

    public int getRootNodeCount() {
        this.mThreadAsserter.assertNow();
        return this.mRootTags.size();
    }

    public int getRootTag(int i) {
        this.mThreadAsserter.assertNow();
        return this.mRootTags.keyAt(i);
    }
}
