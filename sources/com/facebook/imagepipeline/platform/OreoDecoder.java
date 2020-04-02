package com.facebook.imagepipeline.platform;

import android.annotation.TargetApi;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import androidx.core.util.Pools.SynchronizedPool;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imageutils.BitmapUtil;
import javax.annotation.concurrent.ThreadSafe;

@TargetApi(26)
@ThreadSafe
public class OreoDecoder extends DefaultDecoder {
    public OreoDecoder(BitmapPool bitmapPool, int i, SynchronizedPool synchronizedPool) {
        super(bitmapPool, i, synchronizedPool);
    }

    public int getBitmapSize(int i, int i2, Options options) {
        if (hasColorGamutMismatch(options)) {
            return i * i2 * 8;
        }
        return BitmapUtil.getSizeInByteForBitmap(i, i2, options.inPreferredConfig);
    }

    private static boolean hasColorGamutMismatch(Options options) {
        return (options.outColorSpace == null || !options.outColorSpace.isWideGamut() || options.inPreferredConfig == Config.RGBA_F16) ? false : true;
    }
}
