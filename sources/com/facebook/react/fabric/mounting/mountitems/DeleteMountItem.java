package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.mounting.MountingManager;

public class DeleteMountItem implements MountItem {
    private int mReactTag;

    public DeleteMountItem(int i) {
        this.mReactTag = i;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.deleteView(this.mReactTag);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeleteMountItem [");
        sb.append(this.mReactTag);
        sb.append("]");
        return sb.toString();
    }
}
