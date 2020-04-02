package com.facebook.react.views.art;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.annotations.ReactProp;

public class ARTShapeShadowNode extends ARTVirtualNode {
    private static final int CAP_BUTT = 0;
    private static final int CAP_ROUND = 1;
    private static final int CAP_SQUARE = 2;
    private static final int COLOR_TYPE_LINEAR_GRADIENT = 1;
    private static final int COLOR_TYPE_PATTERN = 3;
    private static final int COLOR_TYPE_RADIAL_GRADIENT = 2;
    private static final int COLOR_TYPE_SOLID_COLOR = 0;
    private static final int JOIN_BEVEL = 2;
    private static final int JOIN_MITER = 0;
    private static final int JOIN_ROUND = 1;
    private static final int PATH_TYPE_ARC = 4;
    private static final int PATH_TYPE_CLOSE = 1;
    private static final int PATH_TYPE_CURVETO = 3;
    private static final int PATH_TYPE_LINETO = 2;
    private static final int PATH_TYPE_MOVETO = 0;
    @Nullable
    private float[] mBrushData;
    @Nullable
    protected Path mPath;
    private int mStrokeCap = 1;
    @Nullable
    private float[] mStrokeColor;
    @Nullable
    private float[] mStrokeDash;
    private int mStrokeJoin = 1;
    private float mStrokeWidth = 1.0f;

    private float modulus(float f, float f2) {
        float f3 = f % f2;
        return f3 < 0.0f ? f3 + f2 : f3;
    }

    @ReactProp(name = "d")
    public void setShapePath(@Nullable ReadableArray readableArray) {
        this.mPath = createPath(PropHelper.toFloatArray(readableArray));
        markUpdated();
    }

    @ReactProp(name = "stroke")
    public void setStroke(@Nullable ReadableArray readableArray) {
        this.mStrokeColor = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(name = "strokeDash")
    public void setStrokeDash(@Nullable ReadableArray readableArray) {
        this.mStrokeDash = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(name = "fill")
    public void setFill(@Nullable ReadableArray readableArray) {
        this.mBrushData = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeWidth")
    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
        markUpdated();
    }

    @ReactProp(defaultInt = 1, name = "strokeCap")
    public void setStrokeCap(int i) {
        this.mStrokeCap = i;
        markUpdated();
    }

    @ReactProp(defaultInt = 1, name = "strokeJoin")
    public void setStrokeJoin(int i) {
        this.mStrokeJoin = i;
        markUpdated();
    }

    public void draw(Canvas canvas, Paint paint, float f) {
        float f2 = f * this.mOpacity;
        if (f2 > 0.01f) {
            saveAndSetupCanvas(canvas);
            if (this.mPath != null) {
                if (setupFillPaint(paint, f2)) {
                    canvas.drawPath(this.mPath, paint);
                }
                if (setupStrokePaint(paint, f2)) {
                    canvas.drawPath(this.mPath, paint);
                }
                restoreCanvas(canvas);
            } else {
                throw new JSApplicationIllegalArgumentException("Shapes should have a valid path (d) prop");
            }
        }
        markUpdateSeen();
    }

    /* access modifiers changed from: protected */
    public boolean setupStrokePaint(Paint paint, float f) {
        if (this.mStrokeWidth != 0.0f) {
            float[] fArr = this.mStrokeColor;
            if (!(fArr == null || fArr.length == 0)) {
                paint.reset();
                paint.setFlags(1);
                paint.setStyle(Style.STROKE);
                int i = this.mStrokeCap;
                String str = " unrecognized";
                if (i == 0) {
                    paint.setStrokeCap(Cap.BUTT);
                } else if (i == 1) {
                    paint.setStrokeCap(Cap.ROUND);
                } else if (i == 2) {
                    paint.setStrokeCap(Cap.SQUARE);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("strokeCap ");
                    sb.append(this.mStrokeCap);
                    sb.append(str);
                    throw new JSApplicationIllegalArgumentException(sb.toString());
                }
                int i2 = this.mStrokeJoin;
                if (i2 == 0) {
                    paint.setStrokeJoin(Join.MITER);
                } else if (i2 == 1) {
                    paint.setStrokeJoin(Join.ROUND);
                } else if (i2 == 2) {
                    paint.setStrokeJoin(Join.BEVEL);
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("strokeJoin ");
                    sb2.append(this.mStrokeJoin);
                    sb2.append(str);
                    throw new JSApplicationIllegalArgumentException(sb2.toString());
                }
                paint.setStrokeWidth(this.mStrokeWidth * this.mScale);
                float[] fArr2 = this.mStrokeColor;
                int i3 = (int) (fArr2.length > 3 ? fArr2[3] * f * 255.0f : f * 255.0f);
                float[] fArr3 = this.mStrokeColor;
                paint.setARGB(i3, (int) (fArr3[0] * 255.0f), (int) (fArr3[1] * 255.0f), (int) (fArr3[2] * 255.0f));
                float[] fArr4 = this.mStrokeDash;
                if (fArr4 != null && fArr4.length > 0) {
                    paint.setPathEffect(new DashPathEffect(fArr4, 0.0f));
                }
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean setupFillPaint(Paint paint, float f) {
        boolean z;
        float[] fArr;
        int[] iArr;
        Paint paint2 = paint;
        float[] fArr2 = this.mBrushData;
        int i = 0;
        if (fArr2 == null || fArr2.length <= 0) {
            return false;
        }
        paint.reset();
        paint2.setFlags(1);
        paint2.setStyle(Style.FILL);
        float[] fArr3 = this.mBrushData;
        int i2 = (int) fArr3[0];
        if (i2 != 0) {
            String str = ReactConstants.TAG;
            if (i2 != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("ART: Color type ");
                sb.append(i2);
                sb.append(" not supported!");
                FLog.m90w(str, sb.toString());
            } else {
                int i3 = 5;
                if (fArr3.length < 5) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[ARTShapeShadowNode setupFillPaint] expects 5 elements, received ");
                    sb2.append(this.mBrushData.length);
                    FLog.m90w(str, sb2.toString());
                    return false;
                }
                float f2 = fArr3[1] * this.mScale;
                float f3 = this.mBrushData[2] * this.mScale;
                float f4 = this.mBrushData[3] * this.mScale;
                float f5 = this.mBrushData[4] * this.mScale;
                int length = (this.mBrushData.length - 5) / 5;
                if (length > 0) {
                    int[] iArr2 = new int[length];
                    float[] fArr4 = new float[length];
                    while (i < length) {
                        float[] fArr5 = this.mBrushData;
                        fArr4[i] = fArr5[(length * 4) + i3 + i];
                        int i4 = (i * 4) + i3;
                        iArr2[i] = Color.argb((int) (fArr5[i4 + 3] * 255.0f), (int) (fArr5[i4 + 0] * 255.0f), (int) (fArr5[i4 + 1] * 255.0f), (int) (fArr5[i4 + 2] * 255.0f));
                        i++;
                        i3 = 5;
                    }
                    iArr = iArr2;
                    fArr = fArr4;
                } else {
                    iArr = null;
                    fArr = null;
                }
                LinearGradient linearGradient = new LinearGradient(f2, f3, f4, f5, iArr, fArr, TileMode.CLAMP);
                paint2.setShader(linearGradient);
            }
            z = true;
        } else {
            int i5 = (int) (fArr3.length > 4 ? fArr3[4] * f * 255.0f : f * 255.0f);
            float[] fArr6 = this.mBrushData;
            z = true;
            paint2.setARGB(i5, (int) (fArr6[1] * 255.0f), (int) (fArr6[2] * 255.0f), (int) (fArr6[3] * 255.0f));
        }
        return z;
    }

    private Path createPath(float[] fArr) {
        int i;
        int i2;
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        int i3 = 0;
        while (i3 < fArr.length) {
            int i4 = i3 + 1;
            int i5 = (int) fArr[i3];
            if (i5 != 0) {
                boolean z = true;
                if (i5 == 1) {
                    path.close();
                    i3 = i4;
                } else if (i5 != 2) {
                    if (i5 == 3) {
                        int i6 = i4 + 1;
                        int i7 = i6 + 1;
                        float f = this.mScale * fArr[i6];
                        int i8 = i7 + 1;
                        float f2 = this.mScale * fArr[i7];
                        int i9 = i8 + 1;
                        float f3 = this.mScale * fArr[i8];
                        int i10 = i9 + 1;
                        i2 = i10 + 1;
                        path.cubicTo(fArr[i4] * this.mScale, f, f2, f3, this.mScale * fArr[i9], fArr[i10] * this.mScale);
                    } else if (i5 == 4) {
                        int i11 = i4 + 1;
                        float f4 = fArr[i4] * this.mScale;
                        int i12 = i11 + 1;
                        float f5 = fArr[i11] * this.mScale;
                        int i13 = i12 + 1;
                        float f6 = fArr[i12] * this.mScale;
                        int i14 = i13 + 1;
                        float degrees = (float) Math.toDegrees((double) fArr[i13]);
                        int i15 = i14 + 1;
                        float degrees2 = (float) Math.toDegrees((double) fArr[i14]);
                        i2 = i15 + 1;
                        if (fArr[i15] == 1.0f) {
                            z = false;
                        }
                        float f7 = degrees2 - degrees;
                        if (Math.abs(f7) >= 360.0f) {
                            path.addCircle(f4, f5, f6, z ? Direction.CCW : Direction.CW);
                        } else {
                            float modulus = modulus(f7, 360.0f);
                            if (z && modulus < 360.0f) {
                                modulus = (360.0f - modulus) * -1.0f;
                            }
                            path.arcTo(new RectF(f4 - f6, f5 - f6, f4 + f6, f5 + f6), degrees, modulus);
                        }
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unrecognized drawing instruction ");
                        sb.append(i5);
                        throw new JSApplicationIllegalArgumentException(sb.toString());
                    }
                    i3 = i2;
                } else {
                    int i16 = i4 + 1;
                    i = i16 + 1;
                    path.lineTo(fArr[i4] * this.mScale, fArr[i16] * this.mScale);
                }
            } else {
                int i17 = i4 + 1;
                i = i17 + 1;
                path.moveTo(fArr[i4] * this.mScale, fArr[i17] * this.mScale);
            }
            i3 = i;
        }
        return path;
    }
}
