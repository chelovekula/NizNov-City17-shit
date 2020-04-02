package com.facebook.react.uimanager.layoutanimation;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import com.brentvatne.react.ReactVideoView;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.IllegalViewOperationException;
import java.util.Map;

abstract class AbstractLayoutAnimation {
    private static final Map<InterpolatorType, BaseInterpolator> INTERPOLATOR = MapBuilder.m128of(InterpolatorType.LINEAR, new LinearInterpolator(), InterpolatorType.EASE_IN, new AccelerateInterpolator(), InterpolatorType.EASE_OUT, new DecelerateInterpolator(), InterpolatorType.EASE_IN_EASE_OUT, new AccelerateDecelerateInterpolator());
    private static final boolean SLOWDOWN_ANIMATION_MODE = false;
    @Nullable
    protected AnimatedPropertyType mAnimatedProperty;
    private int mDelayMs;
    protected int mDurationMs;
    @Nullable
    private Interpolator mInterpolator;

    /* access modifiers changed from: 0000 */
    @Nullable
    public abstract Animation createAnimationImpl(View view, int i, int i2, int i3, int i4);

    /* access modifiers changed from: 0000 */
    public abstract boolean isValid();

    AbstractLayoutAnimation() {
    }

    public void reset() {
        this.mAnimatedProperty = null;
        this.mDurationMs = 0;
        this.mDelayMs = 0;
        this.mInterpolator = null;
    }

    public void initializeFromConfig(ReadableMap readableMap, int i) {
        String str = "property";
        this.mAnimatedProperty = readableMap.hasKey(str) ? AnimatedPropertyType.fromString(readableMap.getString(str)) : null;
        String str2 = ReactVideoView.EVENT_PROP_DURATION;
        if (readableMap.hasKey(str2)) {
            i = readableMap.getInt(str2);
        }
        this.mDurationMs = i;
        String str3 = "delay";
        this.mDelayMs = readableMap.hasKey(str3) ? readableMap.getInt(str3) : 0;
        String str4 = ReactVideoViewManager.PROP_SRC_TYPE;
        if (readableMap.hasKey(str4)) {
            this.mInterpolator = getInterpolator(InterpolatorType.fromString(readableMap.getString(str4)), readableMap);
            if (!isValid()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid layout animation : ");
                sb.append(readableMap);
                throw new IllegalViewOperationException(sb.toString());
            }
            return;
        }
        throw new IllegalArgumentException("Missing interpolation type.");
    }

    @Nullable
    public final Animation createAnimation(View view, int i, int i2, int i3, int i4) {
        if (!isValid()) {
            return null;
        }
        Animation createAnimationImpl = createAnimationImpl(view, i, i2, i3, i4);
        if (createAnimationImpl != null) {
            createAnimationImpl.setDuration((long) (this.mDurationMs * 1));
            createAnimationImpl.setStartOffset((long) (this.mDelayMs * 1));
            createAnimationImpl.setInterpolator(this.mInterpolator);
        }
        return createAnimationImpl;
    }

    private static Interpolator getInterpolator(InterpolatorType interpolatorType, ReadableMap readableMap) {
        Interpolator interpolator;
        if (interpolatorType.equals(InterpolatorType.SPRING)) {
            interpolator = new SimpleSpringInterpolator(SimpleSpringInterpolator.getSpringDamping(readableMap));
        } else {
            interpolator = (Interpolator) INTERPOLATOR.get(interpolatorType);
        }
        if (interpolator != null) {
            return interpolator;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Missing interpolator for type : ");
        sb.append(interpolatorType);
        throw new IllegalArgumentException(sb.toString());
    }
}
