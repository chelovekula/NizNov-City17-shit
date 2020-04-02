package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.view.View;
import android.view.ViewParent;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.DisplayMetricsHolder;
import com.facebook.react.uimanager.OnLayoutEvent;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import javax.annotation.Nullable;

@SuppressLint({"ViewConstructor"})
public abstract class VirtualView extends ReactViewGroup {
    private static final int CLIP_RULE_EVENODD = 0;
    static final int CLIP_RULE_NONZERO = 1;
    static final float MIN_OPACITY_FOR_DRAW = 0.01f;
    private static final double M_SQRT1_2l = 0.7071067811865476d;
    private static final float[] sRawMatrix = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private double canvasDiagonal = -1.0d;
    private float canvasHeight = -1.0f;
    private float canvasWidth = -1.0f;
    private double fontSize = -1.0d;
    private GlyphContext glyphContext;
    RectF mBox;
    private Path mCachedClipPath;
    private RectF mClientRect;
    @Nullable
    private String mClipPath;
    Region mClipRegion;
    Path mClipRegionPath;
    int mClipRule;
    final ReactContext mContext;
    Path mFillPath;
    Matrix mInvMatrix = new Matrix();
    Matrix mInvTransform = new Matrix();
    boolean mInvertible = true;
    @Nullable
    String mMask;
    Matrix mMatrix = new Matrix();
    String mName;
    float mOpacity = 1.0f;
    Path mPath;
    Region mRegion;
    private boolean mResponsible;
    final float mScale;
    Path mStrokePath;
    Region mStrokeRegion;
    private GroupView mTextRoot;
    Matrix mTransform = new Matrix();
    boolean mTransformInvertible = true;
    private SvgView svgView;

    /* access modifiers changed from: 0000 */
    public abstract void draw(Canvas canvas, Paint paint, float f);

    /* access modifiers changed from: 0000 */
    public abstract Path getPath(Canvas canvas, Paint paint);

    /* access modifiers changed from: 0000 */
    public abstract int hitTest(float[] fArr);

    VirtualView(ReactContext reactContext) {
        super(reactContext);
        this.mContext = reactContext;
        this.mScale = DisplayMetricsHolder.getScreenDisplayMetrics().density;
    }

    public void invalidate() {
        if (!(this instanceof RenderableView) || this.mPath != null) {
            clearCache();
            clearParentCache();
            super.invalidate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearCache() {
        this.canvasDiagonal = -1.0d;
        this.canvasHeight = -1.0f;
        this.canvasWidth = -1.0f;
        this.fontSize = -1.0d;
        this.mStrokeRegion = null;
        this.mRegion = null;
        this.mPath = null;
    }

    /* access modifiers changed from: 0000 */
    public void clearChildCache() {
        clearCache();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof VirtualView) {
                ((VirtualView) childAt).clearChildCache();
            }
        }
    }

    private void clearParentCache() {
        VirtualView virtualView = this;
        while (true) {
            ViewParent parent = virtualView.getParent();
            if (parent instanceof VirtualView) {
                virtualView = (VirtualView) parent;
                if (virtualView.mPath != null) {
                    virtualView.clearCache();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public GroupView getTextRoot() {
        if (this.mTextRoot == null) {
            VirtualView virtualView = this;
            while (true) {
                if (virtualView == null) {
                    break;
                }
                if (virtualView instanceof GroupView) {
                    GroupView groupView = (GroupView) virtualView;
                    if (groupView.getGlyphContext() != null) {
                        this.mTextRoot = groupView;
                        break;
                    }
                }
                ViewParent parent = virtualView.getParent();
                if (!(parent instanceof VirtualView)) {
                    virtualView = null;
                } else {
                    virtualView = (VirtualView) parent;
                }
            }
        }
        return this.mTextRoot;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public GroupView getParentTextRoot() {
        ViewParent parent = getParent();
        if (!(parent instanceof VirtualView)) {
            return null;
        }
        return ((VirtualView) parent).getTextRoot();
    }

    private double getFontSizeFromContext() {
        double d = this.fontSize;
        if (d != -1.0d) {
            return d;
        }
        GroupView textRoot = getTextRoot();
        if (textRoot == null) {
            return 12.0d;
        }
        if (this.glyphContext == null) {
            this.glyphContext = textRoot.getGlyphContext();
        }
        this.fontSize = this.glyphContext.getFontSize();
        return this.fontSize;
    }

    /* access modifiers changed from: 0000 */
    public void render(Canvas canvas, Paint paint, float f) {
        draw(canvas, paint, f);
    }

    /* access modifiers changed from: 0000 */
    public int saveAndSetupCanvas(Canvas canvas) {
        int save = canvas.save();
        canvas.concat(this.mMatrix);
        canvas.concat(this.mTransform);
        return save;
    }

    /* access modifiers changed from: 0000 */
    public void restoreCanvas(Canvas canvas, int i) {
        canvas.restoreToCount(i);
    }

    @ReactProp(name = "name")
    public void setName(String str) {
        this.mName = str;
        invalidate();
    }

    @ReactProp(name = "mask")
    public void setMask(String str) {
        this.mMask = str;
        invalidate();
    }

    @ReactProp(name = "clipPath")
    public void setClipPath(String str) {
        this.mCachedClipPath = null;
        this.mClipPath = str;
        invalidate();
    }

    @ReactProp(defaultInt = 1, name = "clipRule")
    public void setClipRule(int i) {
        this.mClipRule = i;
        invalidate();
    }

    @ReactProp(defaultFloat = 1.0f, name = "opacity")
    public void setOpacity(float f) {
        this.mOpacity = f;
        invalidate();
    }

    @ReactProp(name = "matrix")
    public void setMatrix(Dynamic dynamic) {
        ReadableType type = dynamic.getType();
        if (dynamic.isNull() || !type.equals(ReadableType.Array)) {
            this.mMatrix = null;
            this.mInvMatrix = null;
            this.mInvertible = false;
        } else {
            int matrixData = PropHelper.toMatrixData(dynamic.asArray(), sRawMatrix, this.mScale);
            if (matrixData == 6) {
                if (this.mMatrix == null) {
                    this.mMatrix = new Matrix();
                    this.mInvMatrix = new Matrix();
                }
                this.mMatrix.setValues(sRawMatrix);
                this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
            } else if (matrixData != -1) {
                FLog.m90w(ReactConstants.TAG, "RNSVG: Transform matrices must be of size 6");
            }
        }
        super.invalidate();
        clearParentCache();
    }

    @ReactProp(name = "responsible")
    public void setResponsible(boolean z) {
        this.mResponsible = z;
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Path getClipPath() {
        return this.mCachedClipPath;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Path getClipPath(Canvas canvas, Paint paint) {
        Path path;
        if (this.mClipPath != null) {
            ClipPathView clipPathView = (ClipPathView) getSvgView().getDefinedClipPath(this.mClipPath);
            String str = ReactConstants.TAG;
            if (clipPathView != null) {
                if (clipPathView.mClipRule == 0) {
                    path = clipPathView.getPath(canvas, paint);
                } else {
                    path = clipPathView.getPath(canvas, paint, Op.UNION);
                }
                int i = clipPathView.mClipRule;
                if (i == 0) {
                    path.setFillType(FillType.EVEN_ODD);
                } else if (i != 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("RNSVG: clipRule: ");
                    sb.append(this.mClipRule);
                    sb.append(" unrecognized");
                    FLog.m90w(str, sb.toString());
                }
                this.mCachedClipPath = path;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("RNSVG: Undefined clipPath: ");
                sb2.append(this.mClipPath);
                FLog.m90w(str, sb2.toString());
            }
        }
        return getClipPath();
    }

    /* access modifiers changed from: 0000 */
    public void clip(Canvas canvas, Paint paint) {
        Path clipPath = getClipPath(canvas, paint);
        if (clipPath != null) {
            canvas.clipPath(clipPath);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isResponsible() {
        return this.mResponsible;
    }

    /* access modifiers changed from: 0000 */
    public SvgView getSvgView() {
        SvgView svgView2 = this.svgView;
        if (svgView2 != null) {
            return svgView2;
        }
        ViewParent parent = getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof SvgView) {
            this.svgView = (SvgView) parent;
        } else if (parent instanceof VirtualView) {
            this.svgView = ((VirtualView) parent).getSvgView();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("RNSVG: ");
            sb.append(getClass().getName());
            sb.append(" should be descendant of a SvgView.");
            FLog.m50e(ReactConstants.TAG, sb.toString());
        }
        return this.svgView;
    }

    /* access modifiers changed from: 0000 */
    public double relativeOnWidth(SVGLength sVGLength) {
        double d;
        double canvasWidth2;
        SVGLengthUnitType sVGLengthUnitType = sVGLength.unit;
        if (sVGLengthUnitType == SVGLengthUnitType.SVG_LENGTHTYPE_NUMBER) {
            d = sVGLength.value;
            canvasWidth2 = (double) this.mScale;
            Double.isNaN(canvasWidth2);
        } else if (sVGLengthUnitType != SVGLengthUnitType.SVG_LENGTHTYPE_PERCENTAGE) {
            return fromRelativeFast(sVGLength);
        } else {
            d = sVGLength.value / 100.0d;
            canvasWidth2 = (double) getCanvasWidth();
            Double.isNaN(canvasWidth2);
        }
        return d * canvasWidth2;
    }

    /* access modifiers changed from: 0000 */
    public double relativeOnHeight(SVGLength sVGLength) {
        double d;
        double canvasHeight2;
        SVGLengthUnitType sVGLengthUnitType = sVGLength.unit;
        if (sVGLengthUnitType == SVGLengthUnitType.SVG_LENGTHTYPE_NUMBER) {
            d = sVGLength.value;
            canvasHeight2 = (double) this.mScale;
            Double.isNaN(canvasHeight2);
        } else if (sVGLengthUnitType != SVGLengthUnitType.SVG_LENGTHTYPE_PERCENTAGE) {
            return fromRelativeFast(sVGLength);
        } else {
            d = sVGLength.value / 100.0d;
            canvasHeight2 = (double) getCanvasHeight();
            Double.isNaN(canvasHeight2);
        }
        return d * canvasHeight2;
    }

    /* access modifiers changed from: 0000 */
    public double relativeOnOther(SVGLength sVGLength) {
        double d;
        double canvasDiagonal2;
        SVGLengthUnitType sVGLengthUnitType = sVGLength.unit;
        if (sVGLengthUnitType == SVGLengthUnitType.SVG_LENGTHTYPE_NUMBER) {
            d = sVGLength.value;
            canvasDiagonal2 = (double) this.mScale;
            Double.isNaN(canvasDiagonal2);
        } else if (sVGLengthUnitType != SVGLengthUnitType.SVG_LENGTHTYPE_PERCENTAGE) {
            return fromRelativeFast(sVGLength);
        } else {
            d = sVGLength.value / 100.0d;
            canvasDiagonal2 = getCanvasDiagonal();
        }
        return d * canvasDiagonal2;
    }

    /* access modifiers changed from: 0000 */
    public double fromRelativeFast(SVGLength sVGLength) {
        double d;
        switch (sVGLength.unit) {
            case SVG_LENGTHTYPE_EMS:
                d = getFontSizeFromContext();
                break;
            case SVG_LENGTHTYPE_EXS:
                d = getFontSizeFromContext() / 2.0d;
                break;
            case SVG_LENGTHTYPE_CM:
                d = 35.43307d;
                break;
            case SVG_LENGTHTYPE_MM:
                d = 3.543307d;
                break;
            case SVG_LENGTHTYPE_IN:
                d = 90.0d;
                break;
            case SVG_LENGTHTYPE_PT:
                d = 1.25d;
                break;
            case SVG_LENGTHTYPE_PC:
                d = 15.0d;
                break;
            default:
                d = 1.0d;
                break;
        }
        double d2 = sVGLength.value * d;
        double d3 = (double) this.mScale;
        Double.isNaN(d3);
        return d2 * d3;
    }

    private float getCanvasWidth() {
        float f = this.canvasWidth;
        if (f != -1.0f) {
            return f;
        }
        GroupView textRoot = getTextRoot();
        if (textRoot == null) {
            this.canvasWidth = (float) getSvgView().getCanvasBounds().width();
        } else {
            this.canvasWidth = textRoot.getGlyphContext().getWidth();
        }
        return this.canvasWidth;
    }

    private float getCanvasHeight() {
        float f = this.canvasHeight;
        if (f != -1.0f) {
            return f;
        }
        GroupView textRoot = getTextRoot();
        if (textRoot == null) {
            this.canvasHeight = (float) getSvgView().getCanvasBounds().height();
        } else {
            this.canvasHeight = textRoot.getGlyphContext().getHeight();
        }
        return this.canvasHeight;
    }

    private double getCanvasDiagonal() {
        double d = this.canvasDiagonal;
        if (d != -1.0d) {
            return d;
        }
        this.canvasDiagonal = Math.sqrt(Math.pow((double) getCanvasWidth(), 2.0d) + Math.pow((double) getCanvasHeight(), 2.0d)) * M_SQRT1_2l;
        return this.canvasDiagonal;
    }

    /* access modifiers changed from: 0000 */
    public void saveDefinition() {
        if (this.mName != null) {
            getSvgView().defineTemplate(this, this.mName);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        RectF rectF = this.mClientRect;
        if (rectF != null) {
            i3 = (int) Math.ceil((double) rectF.width());
        } else {
            i3 = getDefaultSize(getSuggestedMinimumWidth(), i);
        }
        RectF rectF2 = this.mClientRect;
        if (rectF2 != null) {
            i4 = (int) Math.ceil((double) rectF2.height());
        } else {
            i4 = getDefaultSize(getSuggestedMinimumHeight(), i2);
        }
        setMeasuredDimension(i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        RectF rectF = this.mClientRect;
        if (rectF != null) {
            if (!(this instanceof GroupView)) {
                int floor = (int) Math.floor((double) this.mClientRect.top);
                int ceil = (int) Math.ceil((double) this.mClientRect.right);
                int ceil2 = (int) Math.ceil((double) this.mClientRect.bottom);
                setLeft((int) Math.floor((double) rectF.left));
                setTop(floor);
                setRight(ceil);
                setBottom(ceil2);
            }
            setMeasuredDimension((int) Math.ceil((double) this.mClientRect.width()), (int) Math.ceil((double) this.mClientRect.height()));
        }
    }

    /* access modifiers changed from: 0000 */
    public void setClientRect(RectF rectF) {
        RectF rectF2 = this.mClientRect;
        if (rectF2 == null || !rectF2.equals(rectF)) {
            this.mClientRect = rectF;
            RectF rectF3 = this.mClientRect;
            if (rectF3 != null) {
                int floor = (int) Math.floor((double) rectF3.left);
                int floor2 = (int) Math.floor((double) this.mClientRect.top);
                int ceil = (int) Math.ceil((double) this.mClientRect.right);
                int ceil2 = (int) Math.ceil((double) this.mClientRect.bottom);
                int ceil3 = (int) Math.ceil((double) this.mClientRect.width());
                int ceil4 = (int) Math.ceil((double) this.mClientRect.height());
                if (!(this instanceof GroupView)) {
                    setLeft(floor);
                    setTop(floor2);
                    setRight(ceil);
                    setBottom(ceil2);
                }
                setMeasuredDimension(ceil3, ceil4);
                ((UIManagerModule) this.mContext.getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(OnLayoutEvent.obtain(getId(), floor, floor2, ceil3, ceil4));
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public RectF getClientRect() {
        return this.mClientRect;
    }
}
