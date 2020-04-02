package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.fabric.mounting.MountingManager;

public class UpdatePropsMountItem implements MountItem {
    private final int mReactTag;
    private final ReadableMap mUpdatedProps;

    public UpdatePropsMountItem(int i, ReadableMap readableMap) {
        this.mReactTag = i;
        this.mUpdatedProps = readableMap;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.updateProps(this.mReactTag, this.mUpdatedProps);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdatePropsMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - props: ");
        sb.append(this.mUpdatedProps);
        return sb.toString();
    }
}
