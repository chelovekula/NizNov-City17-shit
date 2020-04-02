package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.mounting.MountingManager;

public class InsertMountItem implements MountItem {
    private int mIndex;
    private int mParentReactTag;
    private int mReactTag;

    public InsertMountItem(int i, int i2, int i3) {
        this.mReactTag = i;
        this.mParentReactTag = i2;
        this.mIndex = i3;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.addViewAt(this.mParentReactTag, this.mReactTag, this.mIndex);
    }

    public int getParentReactTag() {
        return this.mParentReactTag;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - parentTag: ");
        sb.append(this.mParentReactTag);
        sb.append(" - index: ");
        sb.append(this.mIndex);
        return sb.toString();
    }
}
