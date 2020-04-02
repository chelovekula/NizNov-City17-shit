package com.facebook.imagepipeline.platform;

import android.annotation.TargetApi;
import android.graphics.Bitmap.Config;
import com.facebook.soloader.DoNotOptimize;

@DoNotOptimize
class PreverificationHelper {
    PreverificationHelper() {
    }

    /* access modifiers changed from: 0000 */
    @DoNotOptimize
    @TargetApi(26)
    public boolean shouldUseHardwareBitmapConfig(Config config) {
        return config == Config.HARDWARE;
    }
}
