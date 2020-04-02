package com.facebook.react.fabric.mounting.mountitems;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;

public class CreateMountItem implements MountItem {
    private final String mComponent;
    private final ThemedReactContext mContext;
    private final boolean mIsLayoutable;
    @Nullable
    private final ReadableMap mProps;
    private final int mReactTag;
    private final int mRootTag;
    @Nullable
    private final StateWrapper mStateWrapper;

    public CreateMountItem(ThemedReactContext themedReactContext, int i, int i2, String str, @Nullable ReadableMap readableMap, StateWrapper stateWrapper, boolean z) {
        this.mContext = themedReactContext;
        this.mComponent = str;
        this.mRootTag = i;
        this.mReactTag = i2;
        this.mProps = readableMap;
        this.mStateWrapper = stateWrapper;
        this.mIsLayoutable = z;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.createView(this.mContext, this.mComponent, this.mReactTag, this.mProps, this.mStateWrapper, this.mIsLayoutable);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - component: ");
        sb.append(this.mComponent);
        sb.append(" - rootTag: ");
        sb.append(this.mRootTag);
        sb.append(" - isLayoutable: ");
        sb.append(this.mIsLayoutable);
        return sb.toString();
    }
}
