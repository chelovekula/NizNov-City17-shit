package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.common.logging.FLog;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.systrace.Systrace;

@DoNotStrip
public class BatchMountItem implements MountItem {
    private final int mCommitNumber;
    private final MountItem[] mMountItems;
    private final int mSize;

    public BatchMountItem(MountItem[] mountItemArr, int i, int i2) {
        if (mountItemArr == null) {
            throw new NullPointerException();
        } else if (i < 0 || i > mountItemArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid size received by parameter size: ");
            sb.append(i);
            sb.append(" items.size = ");
            sb.append(mountItemArr.length);
            throw new IllegalArgumentException(sb.toString());
        } else {
            this.mMountItems = mountItemArr;
            this.mSize = i;
            this.mCommitNumber = i2;
        }
    }

    public void execute(MountingManager mountingManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("FabricUIManager::mountViews - ");
        sb.append(this.mSize);
        sb.append(" items");
        Systrace.beginSection(0, sb.toString());
        if (this.mCommitNumber > 0) {
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_START, null, this.mCommitNumber);
        }
        for (int i = 0; i < this.mSize; i++) {
            MountItem mountItem = this.mMountItems[i];
            if (FabricUIManager.DEBUG) {
                String str = FabricUIManager.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Executing mountItem: ");
                sb2.append(mountItem);
                FLog.m38d(str, sb2.toString());
            }
            mountItem.execute(mountingManager);
        }
        if (this.mCommitNumber > 0) {
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_END, null, this.mCommitNumber);
        }
        Systrace.endSection(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatchMountItem - size ");
        sb.append(this.mMountItems.length);
        return sb.toString();
    }
}
