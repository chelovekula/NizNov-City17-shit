package com.facebook.react.uimanager.layoutanimation;

import android.view.animation.Interpolator;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

class SimpleSpringInterpolator implements Interpolator {
    private static final float FACTOR = 0.5f;
    public static final String PARAM_SPRING_DAMPING = "springDamping";
    private final float mSpringDamping;

    public static float getSpringDamping(ReadableMap readableMap) {
        String str = PARAM_SPRING_DAMPING;
        return readableMap.getType(str).equals(ReadableType.Number) ? (float) readableMap.getDouble(str) : FACTOR;
    }

    public SimpleSpringInterpolator() {
        this.mSpringDamping = FACTOR;
    }

    public SimpleSpringInterpolator(float f) {
        this.mSpringDamping = f;
    }

    public float getInterpolation(float f) {
        double pow = Math.pow(2.0d, (double) (-10.0f * f));
        float f2 = this.mSpringDamping;
        double d = (double) (f - (f2 / 4.0f));
        Double.isNaN(d);
        double d2 = d * 3.141592653589793d * 2.0d;
        double d3 = (double) f2;
        Double.isNaN(d3);
        return (float) ((pow * Math.sin(d2 / d3)) + 1.0d);
    }
}
