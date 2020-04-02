package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;

public class GingerbreadBitmapFactory extends PlatformBitmapFactory {
    public CloseableReference<Bitmap> createBitmapInternal(int i, int i2, Config config) {
        return CloseableReference.m116of(Bitmap.createBitmap(i, i2, config), (ResourceReleaser<T>) SimpleBitmapReleaser.getInstance());
    }
}
