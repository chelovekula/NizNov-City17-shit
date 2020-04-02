package com.facebook.react.views.image;

import android.graphics.Shader.TileMode;
import androidx.annotation.Nullable;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;

public class ImageResizeMode {
    public static ScaleType toScaleType(@Nullable String str) {
        if ("contain".equals(str)) {
            return ScaleType.FIT_CENTER;
        }
        if ("cover".equals(str)) {
            return ScaleType.CENTER_CROP;
        }
        if ("stretch".equals(str)) {
            return ScaleType.FIT_XY;
        }
        if ("center".equals(str)) {
            return ScaleType.CENTER_INSIDE;
        }
        if (ReactVideoViewManager.PROP_REPEAT.equals(str)) {
            return ScaleTypeStartInside.INSTANCE;
        }
        if (str == null) {
            return defaultValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid resize mode: '");
        sb.append(str);
        sb.append("'");
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    public static TileMode toTileMode(@Nullable String str) {
        if ("contain".equals(str) || "cover".equals(str) || "stretch".equals(str) || "center".equals(str)) {
            return TileMode.CLAMP;
        }
        if (ReactVideoViewManager.PROP_REPEAT.equals(str)) {
            return TileMode.REPEAT;
        }
        if (str == null) {
            return defaultTileMode();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid resize mode: '");
        sb.append(str);
        sb.append("'");
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    public static ScaleType defaultValue() {
        return ScaleType.CENTER_CROP;
    }

    public static TileMode defaultTileMode() {
        return TileMode.CLAMP;
    }
}
