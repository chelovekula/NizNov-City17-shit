package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.annotations.ReactProp;

@SuppressLint({"ViewConstructor"})
class UseView extends RenderableView {

    /* renamed from: mH */
    private SVGLength f82mH;
    private String mHref;

    /* renamed from: mW */
    private SVGLength f83mW;

    /* renamed from: mX */
    private SVGLength f84mX;

    /* renamed from: mY */
    private SVGLength f85mY;

    public UseView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(name = "href")
    public void setHref(String str) {
        this.mHref = str;
        invalidate();
    }

    @ReactProp(name = "x")
    public void setX(Dynamic dynamic) {
        this.f84mX = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "y")
    public void setY(Dynamic dynamic) {
        this.f85mY = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "width")
    public void setWidth(Dynamic dynamic) {
        this.f83mW = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "height")
    public void setHeight(Dynamic dynamic) {
        this.f82mH = SVGLength.from(dynamic);
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        VirtualView definedTemplate = getSvgView().getDefinedTemplate(this.mHref);
        if (definedTemplate == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("`Use` element expected a pre-defined svg template as `href` prop, template named: ");
            sb.append(this.mHref);
            sb.append(" is not defined.");
            FLog.m90w(ReactConstants.TAG, sb.toString());
            return;
        }
        definedTemplate.clearCache();
        canvas.translate((float) relativeOnWidth(this.f84mX), (float) relativeOnHeight(this.f85mY));
        boolean z = definedTemplate instanceof RenderableView;
        if (z) {
            ((RenderableView) definedTemplate).mergeProperties(this);
        }
        int saveAndSetupCanvas = definedTemplate.saveAndSetupCanvas(canvas);
        clip(canvas, paint);
        if (definedTemplate instanceof SymbolView) {
            ((SymbolView) definedTemplate).drawSymbol(canvas, paint, f, (float) relativeOnWidth(this.f83mW), (float) relativeOnHeight(this.f82mH));
        } else {
            definedTemplate.draw(canvas, paint, f * this.mOpacity);
        }
        setClientRect(definedTemplate.getClientRect());
        definedTemplate.restoreCanvas(canvas, saveAndSetupCanvas);
        if (z) {
            ((RenderableView) definedTemplate).resetProperties();
        }
    }

    /* access modifiers changed from: 0000 */
    public int hitTest(float[] fArr) {
        if (this.mInvertible && this.mTransformInvertible) {
            float[] fArr2 = new float[2];
            this.mInvMatrix.mapPoints(fArr2, fArr);
            this.mInvTransform.mapPoints(fArr2);
            VirtualView definedTemplate = getSvgView().getDefinedTemplate(this.mHref);
            if (definedTemplate == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("`Use` element expected a pre-defined svg template as `href` prop, template named: ");
                sb.append(this.mHref);
                sb.append(" is not defined.");
                FLog.m90w(ReactConstants.TAG, sb.toString());
                return -1;
            }
            int hitTest = definedTemplate.hitTest(fArr2);
            if (hitTest != -1) {
                if (!definedTemplate.isResponsible() && hitTest == definedTemplate.getId()) {
                    hitTest = getId();
                }
                return hitTest;
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint) {
        VirtualView definedTemplate = getSvgView().getDefinedTemplate(this.mHref);
        if (definedTemplate == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("`Use` element expected a pre-defined svg template as `href` prop, template named: ");
            sb.append(this.mHref);
            sb.append(" is not defined.");
            FLog.m90w(ReactConstants.TAG, sb.toString());
            return null;
        }
        Path path = definedTemplate.getPath(canvas, paint);
        Path path2 = new Path();
        Matrix matrix = new Matrix();
        matrix.setTranslate((float) relativeOnWidth(this.f84mX), (float) relativeOnHeight(this.f85mY));
        path.transform(matrix, path2);
        return path2;
    }
}
