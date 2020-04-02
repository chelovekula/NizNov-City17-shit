package com.facebook.react.fabric.mounting.mountitems;

import android.annotation.TargetApi;
import com.facebook.react.fabric.mounting.MountingManager;

public class UpdateLayoutMountItem implements MountItem {
    private final int mHeight;
    private final int mLayoutDirection;
    private final int mReactTag;
    private final int mWidth;

    /* renamed from: mX */
    private final int f44mX;

    /* renamed from: mY */
    private final int f45mY;

    public UpdateLayoutMountItem(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mReactTag = i;
        this.f44mX = i2;
        this.f45mY = i3;
        this.mWidth = i4;
        this.mHeight = i5;
        this.mLayoutDirection = convertLayoutDirection(i6);
    }

    @TargetApi(19)
    private int convertLayoutDirection(int i) {
        if (i == 0) {
            return 2;
        }
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported layout direction: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.updateLayout(this.mReactTag, this.f44mX, this.f45mY, this.mWidth, this.mHeight);
    }

    public int getX() {
        return this.f44mX;
    }

    public int getY() {
        return this.f45mY;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLayoutMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - x: ");
        sb.append(this.f44mX);
        sb.append(" - y: ");
        sb.append(this.f45mY);
        sb.append(" - height: ");
        sb.append(this.mHeight);
        sb.append(" - width: ");
        sb.append(this.mWidth);
        sb.append(" - layoutDirection: ");
        sb.append(this.mLayoutDirection);
        return sb.toString();
    }
}
