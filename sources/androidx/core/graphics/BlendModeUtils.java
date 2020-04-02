package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.PorterDuff.Mode;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class BlendModeUtils {
    @RequiresApi(29)
    @Nullable
    static BlendMode obtainBlendModeFromCompat(@NonNull BlendModeCompat blendModeCompat) {
        switch (blendModeCompat) {
            case CLEAR:
                return BlendMode.CLEAR;
            case SRC:
                return BlendMode.SRC;
            case DST:
                return BlendMode.DST;
            case SRC_OVER:
                return BlendMode.SRC_OVER;
            case DST_OVER:
                return BlendMode.DST_OVER;
            case SRC_IN:
                return BlendMode.SRC_IN;
            case DST_IN:
                return BlendMode.DST_IN;
            case SRC_OUT:
                return BlendMode.SRC_OUT;
            case DST_OUT:
                return BlendMode.DST_OUT;
            case SRC_ATOP:
                return BlendMode.SRC_ATOP;
            case DST_ATOP:
                return BlendMode.DST_ATOP;
            case XOR:
                return BlendMode.XOR;
            case PLUS:
                return BlendMode.PLUS;
            case MODULATE:
                return BlendMode.MODULATE;
            case SCREEN:
                return BlendMode.SCREEN;
            case OVERLAY:
                return BlendMode.OVERLAY;
            case DARKEN:
                return BlendMode.DARKEN;
            case LIGHTEN:
                return BlendMode.LIGHTEN;
            case COLOR_DODGE:
                return BlendMode.COLOR_DODGE;
            case COLOR_BURN:
                return BlendMode.COLOR_BURN;
            case HARD_LIGHT:
                return BlendMode.HARD_LIGHT;
            case SOFT_LIGHT:
                return BlendMode.SOFT_LIGHT;
            case DIFFERENCE:
                return BlendMode.DIFFERENCE;
            case EXCLUSION:
                return BlendMode.EXCLUSION;
            case MULTIPLY:
                return BlendMode.MULTIPLY;
            case HUE:
                return BlendMode.HUE;
            case SATURATION:
                return BlendMode.SATURATION;
            case COLOR:
                return BlendMode.COLOR;
            case LUMINOSITY:
                return BlendMode.LUMINOSITY;
            default:
                return null;
        }
    }

    @Nullable
    static Mode obtainPorterDuffFromCompat(@Nullable BlendModeCompat blendModeCompat) {
        if (blendModeCompat == null) {
            return null;
        }
        switch (blendModeCompat) {
            case CLEAR:
                return Mode.CLEAR;
            case SRC:
                return Mode.SRC;
            case DST:
                return Mode.DST;
            case SRC_OVER:
                return Mode.SRC_OVER;
            case DST_OVER:
                return Mode.DST_OVER;
            case SRC_IN:
                return Mode.SRC_IN;
            case DST_IN:
                return Mode.DST_IN;
            case SRC_OUT:
                return Mode.SRC_OUT;
            case DST_OUT:
                return Mode.DST_OUT;
            case SRC_ATOP:
                return Mode.SRC_ATOP;
            case DST_ATOP:
                return Mode.DST_ATOP;
            case XOR:
                return Mode.XOR;
            case PLUS:
                return Mode.ADD;
            case MODULATE:
                return Mode.MULTIPLY;
            case SCREEN:
                return Mode.SCREEN;
            case OVERLAY:
                return Mode.OVERLAY;
            case DARKEN:
                return Mode.DARKEN;
            case LIGHTEN:
                return Mode.LIGHTEN;
            default:
                return null;
        }
    }

    private BlendModeUtils() {
    }
}
