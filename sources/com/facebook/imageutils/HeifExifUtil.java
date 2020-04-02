package com.facebook.imageutils;

import android.media.ExifInterface;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import com.facebook.common.logging.FLog;
import com.facebook.soloader.DoNotOptimize;
import java.io.IOException;
import java.io.InputStream;

public class HeifExifUtil {
    public static final String TAG = "HeifExifUtil";

    @DoNotOptimize
    private static class HeifExifUtilAndroidN {
        private HeifExifUtilAndroidN() {
        }

        @RequiresApi(api = 24)
        static int getOrientation(InputStream inputStream) {
            try {
                return new ExifInterface(inputStream).getAttributeInt("Orientation", 1);
            } catch (IOException e) {
                FLog.m43d(HeifExifUtil.TAG, "Failed reading Heif Exif orientation -> ignoring", (Throwable) e);
                return 0;
            }
        }
    }

    public static int getOrientation(InputStream inputStream) {
        if (VERSION.SDK_INT >= 24) {
            return HeifExifUtilAndroidN.getOrientation(inputStream);
        }
        FLog.m38d(TAG, "Trying to read Heif Exif information before Android N -> ignoring");
        return 0;
    }
}
