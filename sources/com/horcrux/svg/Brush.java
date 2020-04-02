package com.horcrux.svg;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import androidx.core.view.ViewCompat;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.ReactConstants;

class Brush {
    private ReadableArray mColors;
    private Matrix mMatrix;
    private PatternView mPattern;
    private final SVGLength[] mPoints;
    private final BrushType mType;
    private boolean mUseContentObjectBoundingBoxUnits;
    private final boolean mUseObjectBoundingBox;
    private Rect mUserSpaceBoundingBox;

    enum BrushType {
        LINEAR_GRADIENT,
        RADIAL_GRADIENT,
        PATTERN
    }

    enum BrushUnits {
        OBJECT_BOUNDING_BOX,
        USER_SPACE_ON_USE
    }

    Brush(BrushType brushType, SVGLength[] sVGLengthArr, BrushUnits brushUnits) {
        this.mType = brushType;
        this.mPoints = sVGLengthArr;
        this.mUseObjectBoundingBox = brushUnits == BrushUnits.OBJECT_BOUNDING_BOX;
    }

    /* access modifiers changed from: 0000 */
    public void setContentUnits(BrushUnits brushUnits) {
        this.mUseContentObjectBoundingBoxUnits = brushUnits == BrushUnits.OBJECT_BOUNDING_BOX;
    }

    /* access modifiers changed from: 0000 */
    public void setPattern(PatternView patternView) {
        this.mPattern = patternView;
    }

    private static void parseGradientStops(ReadableArray readableArray, int i, float[] fArr, int[] iArr, float f) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = i2 * 2;
            fArr[i2] = (float) readableArray.getDouble(i3);
            int i4 = readableArray.getInt(i3 + 1);
            iArr[i2] = (i4 & ViewCompat.MEASURED_SIZE_MASK) | (Math.round(((float) (i4 >>> 24)) * f) << 24);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setUserSpaceBoundingBox(Rect rect) {
        this.mUserSpaceBoundingBox = rect;
    }

    /* access modifiers changed from: 0000 */
    public void setGradientColors(ReadableArray readableArray) {
        this.mColors = readableArray;
    }

    /* access modifiers changed from: 0000 */
    public void setGradientTransform(Matrix matrix) {
        this.mMatrix = matrix;
    }

    private RectF getPaintRect(RectF rectF) {
        float f;
        if (!this.mUseObjectBoundingBox) {
            rectF = new RectF(this.mUserSpaceBoundingBox);
        }
        float width = rectF.width();
        float height = rectF.height();
        float f2 = 0.0f;
        if (this.mUseObjectBoundingBox) {
            f2 = rectF.left;
            f = rectF.top;
        } else {
            f = 0.0f;
        }
        return new RectF(f2, f, width + f2, height + f);
    }

    private double getVal(SVGLength sVGLength, double d, float f, float f2) {
        double d2;
        if (!this.mUseObjectBoundingBox) {
            SVGLength sVGLength2 = sVGLength;
        } else if (sVGLength.unit == SVGLengthUnitType.SVG_LENGTHTYPE_NUMBER) {
            d2 = d;
            return PropHelper.fromRelative(sVGLength, d, 0.0d, d2, (double) f2);
        }
        d2 = (double) f;
        return PropHelper.fromRelative(sVGLength, d, 0.0d, d2, (double) f2);
    }

    /* access modifiers changed from: 0000 */
    public void setupPaint(Paint paint, RectF rectF, float f, float f2) {
        float[] fArr;
        int[] iArr;
        Paint paint2 = paint;
        float f3 = f;
        float f4 = f2;
        RectF paintRect = getPaintRect(rectF);
        float width = paintRect.width();
        float height = paintRect.height();
        float f5 = paintRect.left;
        float f6 = paintRect.top;
        float textSize = paint.getTextSize();
        if (this.mType == BrushType.PATTERN) {
            double d = (double) width;
            double d2 = d;
            double val = getVal(this.mPoints[0], d, f, textSize);
            double d3 = (double) height;
            double d4 = d3;
            double d5 = val;
            double val2 = getVal(this.mPoints[1], d3, f, textSize);
            double d6 = val2;
            double val3 = getVal(this.mPoints[2], d2, f, textSize);
            double d7 = val3;
            double val4 = getVal(this.mPoints[3], d4, f, textSize);
            if (d7 > 1.0d && val4 > 1.0d) {
                Bitmap createBitmap = Bitmap.createBitmap((int) d7, (int) val4, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                RectF viewBox = this.mPattern.getViewBox();
                if (viewBox != null && viewBox.width() > 0.0f && viewBox.height() > 0.0f) {
                    canvas.concat(ViewBox.getTransform(viewBox, new RectF((float) d5, (float) d6, (float) d7, (float) val4), this.mPattern.mAlign, this.mPattern.mMeetOrSlice));
                }
                if (this.mUseContentObjectBoundingBoxUnits) {
                    float f7 = f;
                    canvas.scale(width / f7, height / f7);
                }
                this.mPattern.draw(canvas, new Paint(), f4);
                Matrix matrix = new Matrix();
                Matrix matrix2 = this.mMatrix;
                if (matrix2 != null) {
                    matrix.preConcat(matrix2);
                }
                BitmapShader bitmapShader = new BitmapShader(createBitmap, TileMode.REPEAT, TileMode.REPEAT);
                bitmapShader.setLocalMatrix(matrix);
                paint.setShader(bitmapShader);
            }
            return;
        }
        Paint paint3 = paint2;
        float f8 = f3;
        int size = this.mColors.size() / 2;
        int[] iArr2 = new int[size];
        float[] fArr2 = new float[size];
        parseGradientStops(this.mColors, size, fArr2, iArr2, f4);
        if (fArr2.length == 1) {
            iArr = new int[]{iArr2[0], iArr2[0]};
            fArr = new float[]{fArr2[0], fArr2[0]};
            FLog.m90w(ReactConstants.TAG, "Gradient contains only on stop");
        } else {
            iArr = iArr2;
            fArr = fArr2;
        }
        if (this.mType == BrushType.LINEAR_GRADIENT) {
            double d8 = (double) width;
            double d9 = (double) f8;
            double d10 = (double) f5;
            double d11 = (double) textSize;
            float f9 = height;
            double fromRelative = PropHelper.fromRelative(this.mPoints[0], d8, d10, d9, d11);
            double d12 = (double) f9;
            double d13 = fromRelative;
            double d14 = (double) f6;
            double d15 = d9;
            double d16 = d11;
            double d17 = d14;
            double fromRelative2 = PropHelper.fromRelative(this.mPoints[1], d12, d14, d15, d16);
            LinearGradient linearGradient = new LinearGradient((float) d13, (float) fromRelative2, (float) PropHelper.fromRelative(this.mPoints[2], d8, d10, d15, d16), (float) PropHelper.fromRelative(this.mPoints[3], d12, d17, d15, d16), iArr, fArr, TileMode.CLAMP);
            if (this.mMatrix != null) {
                Matrix matrix3 = new Matrix();
                matrix3.preConcat(this.mMatrix);
                linearGradient.setLocalMatrix(matrix3);
            }
            paint.setShader(linearGradient);
        } else {
            Paint paint4 = paint3;
            float f10 = height;
            if (this.mType == BrushType.RADIAL_GRADIENT) {
                double d18 = (double) width;
                double d19 = (double) f8;
                double d20 = (double) textSize;
                double fromRelative3 = PropHelper.fromRelative(this.mPoints[2], d18, 0.0d, d19, d20);
                double d21 = (double) f10;
                double d22 = d19;
                double d23 = d20;
                double fromRelative4 = PropHelper.fromRelative(this.mPoints[3], d21, 0.0d, d22, d23);
                double fromRelative5 = PropHelper.fromRelative(this.mPoints[4], d18, (double) f5, d19, d20);
                double d24 = fromRelative4 / fromRelative3;
                RadialGradient radialGradient = new RadialGradient((float) fromRelative5, (float) (PropHelper.fromRelative(this.mPoints[5], d21, (double) f6, d22, d23) / d24), (float) fromRelative3, iArr, fArr, TileMode.CLAMP);
                Matrix matrix4 = new Matrix();
                matrix4.preScale(1.0f, (float) d24);
                Matrix matrix5 = this.mMatrix;
                if (matrix5 != null) {
                    matrix4.preConcat(matrix5);
                }
                radialGradient.setLocalMatrix(matrix4);
                paint.setShader(radialGradient);
            }
        }
    }
}
