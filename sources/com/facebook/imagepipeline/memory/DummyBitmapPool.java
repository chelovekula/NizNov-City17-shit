package com.facebook.imagepipeline.memory;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimType;

public class DummyBitmapPool implements BitmapPool {
    public void trim(MemoryTrimType memoryTrimType) {
    }

    public Bitmap get(int i) {
        double d = (double) i;
        Double.isNaN(d);
        return Bitmap.createBitmap(1, (int) Math.ceil(d / 2.0d), Config.RGB_565);
    }

    public void release(Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap);
        bitmap.recycle();
    }
}
