package com.facebook.react.uimanager.layoutanimation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import com.facebook.react.uimanager.IllegalViewOperationException;

abstract class BaseLayoutAnimation extends AbstractLayoutAnimation {

    /* renamed from: com.facebook.react.uimanager.layoutanimation.BaseLayoutAnimation$1 */
    static /* synthetic */ class C08521 {

        /* renamed from: $SwitchMap$com$facebook$react$uimanager$layoutanimation$AnimatedPropertyType */
        static final /* synthetic */ int[] f54x36728427 = new int[AnimatedPropertyType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType[] r0 = com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f54x36728427 = r0
                int[] r0 = f54x36728427     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r1 = com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.OPACITY     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f54x36728427     // Catch:{ NoSuchFieldError -> 0x001f }
                com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r1 = com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.SCALE_XY     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f54x36728427     // Catch:{ NoSuchFieldError -> 0x002a }
                com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r1 = com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.SCALE_X     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = f54x36728427     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r1 = com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.SCALE_Y     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.layoutanimation.BaseLayoutAnimation.C08521.<clinit>():void");
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract boolean isReverse();

    BaseLayoutAnimation() {
    }

    /* access modifiers changed from: 0000 */
    public boolean isValid() {
        return this.mDurationMs > 0 && this.mAnimatedProperty != null;
    }

    /* access modifiers changed from: 0000 */
    public Animation createAnimationImpl(View view, int i, int i2, int i3, int i4) {
        if (this.mAnimatedProperty != null) {
            int i5 = C08521.f54x36728427[this.mAnimatedProperty.ordinal()];
            float f = 0.0f;
            if (i5 == 1) {
                float alpha = isReverse() ? view.getAlpha() : 0.0f;
                if (!isReverse()) {
                    f = view.getAlpha();
                }
                return new OpacityAnimation(view, alpha, f);
            } else if (i5 == 2) {
                float f2 = isReverse() ? 1.0f : 0.0f;
                float f3 = isReverse() ? 0.0f : 1.0f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(f2, f3, f2, f3, 1, 0.5f, 1, 0.5f);
                return scaleAnimation;
            } else if (i5 == 3) {
                ScaleAnimation scaleAnimation2 = new ScaleAnimation(isReverse() ? 1.0f : 0.0f, isReverse() ? 0.0f : 1.0f, 1.0f, 1.0f, 1, 0.5f, 1, 0.0f);
                return scaleAnimation2;
            } else if (i5 == 4) {
                ScaleAnimation scaleAnimation3 = new ScaleAnimation(1.0f, 1.0f, isReverse() ? 1.0f : 0.0f, isReverse() ? 0.0f : 1.0f, 1, 0.0f, 1, 0.5f);
                return scaleAnimation3;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Missing animation for property : ");
                sb.append(this.mAnimatedProperty);
                throw new IllegalViewOperationException(sb.toString());
            }
        } else {
            throw new IllegalViewOperationException("Missing animated property from animation config");
        }
    }
}
