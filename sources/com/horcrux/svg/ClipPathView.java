package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.ReactConstants;

@SuppressLint({"ViewConstructor"})
class ClipPathView extends GroupView {
    /* access modifiers changed from: 0000 */
    public int hitTest(float[] fArr) {
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public boolean isResponsible() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void mergeProperties(RenderableView renderableView) {
    }

    /* access modifiers changed from: 0000 */
    public void resetProperties() {
    }

    public ClipPathView(ReactContext reactContext) {
        super(reactContext);
        this.mClipRule = 1;
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        FLog.m90w(ReactConstants.TAG, "RNSVG: ClipPath can't be drawn, it should be defined as a child component for `Defs` ");
    }

    /* access modifiers changed from: 0000 */
    public void saveDefinition() {
        getSvgView().defineClipPath(this, this.mName);
    }
}
