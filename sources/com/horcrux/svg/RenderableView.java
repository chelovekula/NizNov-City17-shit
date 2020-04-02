package com.horcrux.svg;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import androidx.core.view.ViewCompat;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class RenderableView extends VirtualView {
    private static final int CAP_BUTT = 0;
    static final int CAP_ROUND = 1;
    private static final int CAP_SQUARE = 2;
    private static final int FILL_RULE_EVENODD = 0;
    static final int FILL_RULE_NONZERO = 1;
    private static final int JOIN_BEVEL = 2;
    private static final int JOIN_MITER = 0;
    static final int JOIN_ROUND = 1;
    static final int VECTOR_EFFECT_DEFAULT = 0;
    static final int VECTOR_EFFECT_INHERIT = 2;
    static final int VECTOR_EFFECT_NON_SCALING_STROKE = 1;
    static final int VECTOR_EFFECT_URI = 3;
    private static final Pattern regex = Pattern.compile("[0-9.-]+");
    @Nullable
    public ReadableArray fill;
    public float fillOpacity = 1.0f;
    public FillType fillRule = FillType.WINDING;
    @Nullable
    ArrayList<String> mAttributeList;
    @Nullable
    ArrayList<String> mLastMergedList;
    @Nullable
    ArrayList<Object> mOriginProperties;
    @Nullable
    ArrayList<String> mPropList;
    @Nullable
    public ReadableArray stroke;
    @Nullable
    public SVGLength[] strokeDasharray;
    public float strokeDashoffset = 0.0f;
    public Cap strokeLinecap = Cap.ROUND;
    public Join strokeLinejoin = Join.ROUND;
    public float strokeMiterlimit = 4.0f;
    public float strokeOpacity = 1.0f;
    public SVGLength strokeWidth = new SVGLength(1.0d);
    public int vectorEffect = 0;

    private static double saturate(double d) {
        if (d <= 0.0d) {
            return 0.0d;
        }
        if (d >= 1.0d) {
            return 1.0d;
        }
        return d;
    }

    /* access modifiers changed from: 0000 */
    public abstract Path getPath(Canvas canvas, Paint paint);

    RenderableView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(defaultInt = 0, name = "vectorEffect")
    public void setVectorEffect(int i) {
        this.vectorEffect = i;
        invalidate();
    }

    @ReactProp(name = "fill")
    public void setFill(@Nullable Dynamic dynamic) {
        if (dynamic == null || dynamic.isNull()) {
            this.fill = null;
            invalidate();
            return;
        }
        if (dynamic.getType().equals(ReadableType.Array)) {
            this.fill = dynamic.asArray();
        } else {
            JavaOnlyArray javaOnlyArray = new JavaOnlyArray();
            int i = 0;
            javaOnlyArray.pushInt(0);
            Matcher matcher = regex.matcher(dynamic.asString());
            while (matcher.find()) {
                Double valueOf = Double.valueOf(Double.parseDouble(matcher.group()));
                int i2 = i + 1;
                javaOnlyArray.pushDouble(i < 3 ? valueOf.doubleValue() / 255.0d : valueOf.doubleValue());
                i = i2;
            }
            this.fill = javaOnlyArray;
        }
        invalidate();
    }

    @ReactProp(defaultFloat = 1.0f, name = "fillOpacity")
    public void setFillOpacity(float f) {
        this.fillOpacity = f;
        invalidate();
    }

    @ReactProp(defaultInt = 1, name = "fillRule")
    public void setFillRule(int i) {
        if (i == 0) {
            this.fillRule = FillType.EVEN_ODD;
        } else if (i != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("fillRule ");
            sb.append(this.fillRule);
            sb.append(" unrecognized");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        invalidate();
    }

    @ReactProp(name = "stroke")
    public void setStroke(@Nullable Dynamic dynamic) {
        if (dynamic == null || dynamic.isNull()) {
            this.stroke = null;
            invalidate();
            return;
        }
        if (dynamic.getType().equals(ReadableType.Array)) {
            this.stroke = dynamic.asArray();
        } else {
            JavaOnlyArray javaOnlyArray = new JavaOnlyArray();
            javaOnlyArray.pushInt(0);
            Matcher matcher = regex.matcher(dynamic.asString());
            while (matcher.find()) {
                javaOnlyArray.pushDouble(Double.valueOf(Double.parseDouble(matcher.group())).doubleValue());
            }
            this.stroke = javaOnlyArray;
        }
        invalidate();
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeOpacity")
    public void setStrokeOpacity(float f) {
        this.strokeOpacity = f;
        invalidate();
    }

    @ReactProp(name = "strokeDasharray")
    public void setStrokeDasharray(@Nullable ReadableArray readableArray) {
        if (readableArray != null) {
            int size = readableArray.size();
            this.strokeDasharray = new SVGLength[size];
            for (int i = 0; i < size; i++) {
                this.strokeDasharray[i] = SVGLength.from(readableArray.getDynamic(i));
            }
        } else {
            this.strokeDasharray = null;
        }
        invalidate();
    }

    @ReactProp(name = "strokeDashoffset")
    public void setStrokeDashoffset(float f) {
        this.strokeDashoffset = f * this.mScale;
        invalidate();
    }

    @ReactProp(name = "strokeWidth")
    public void setStrokeWidth(Dynamic dynamic) {
        this.strokeWidth = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(defaultFloat = 4.0f, name = "strokeMiterlimit")
    public void setStrokeMiterlimit(float f) {
        this.strokeMiterlimit = f;
        invalidate();
    }

    @ReactProp(defaultInt = 1, name = "strokeLinecap")
    public void setStrokeLinecap(int i) {
        if (i == 0) {
            this.strokeLinecap = Cap.BUTT;
        } else if (i == 1) {
            this.strokeLinecap = Cap.ROUND;
        } else if (i == 2) {
            this.strokeLinecap = Cap.SQUARE;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("strokeLinecap ");
            sb.append(this.strokeLinecap);
            sb.append(" unrecognized");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        invalidate();
    }

    @ReactProp(defaultInt = 1, name = "strokeLinejoin")
    public void setStrokeLinejoin(int i) {
        if (i == 0) {
            this.strokeLinejoin = Join.MITER;
        } else if (i == 1) {
            this.strokeLinejoin = Join.ROUND;
        } else if (i == 2) {
            this.strokeLinejoin = Join.BEVEL;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("strokeLinejoin ");
            sb.append(this.strokeLinejoin);
            sb.append(" unrecognized");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        invalidate();
    }

    @ReactProp(name = "propList")
    public void setPropList(@Nullable ReadableArray readableArray) {
        if (readableArray != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            this.mAttributeList = arrayList;
            this.mPropList = arrayList;
            for (int i = 0; i < readableArray.size(); i++) {
                this.mPropList.add(readableArray.getString(i));
            }
        }
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void render(Canvas canvas, Paint paint, float f) {
        Paint paint2 = paint;
        MaskView maskView = this.mMask != null ? (MaskView) getSvgView().getDefinedMask(this.mMask) : null;
        if (maskView != null) {
            Rect clipBounds = canvas.getClipBounds();
            int height = clipBounds.height();
            int width = clipBounds.width();
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Bitmap createBitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Bitmap createBitmap3 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas2 = new Canvas(createBitmap2);
            Canvas canvas3 = new Canvas(createBitmap);
            Canvas canvas4 = new Canvas(createBitmap3);
            int i = width;
            canvas3.clipRect((float) relativeOnWidth(maskView.f69mX), (float) relativeOnWidth(maskView.f70mY), (float) relativeOnWidth(maskView.f68mW), (float) relativeOnWidth(maskView.f67mH));
            Paint paint3 = new Paint(1);
            maskView.draw(canvas3, paint3, 1.0f);
            int i2 = i * height;
            int[] iArr = new int[i2];
            Canvas canvas5 = canvas4;
            Canvas canvas6 = canvas2;
            Bitmap bitmap = createBitmap3;
            createBitmap.getPixels(iArr, 0, i, 0, 0, i, height);
            int i3 = 0;
            while (i3 < i2) {
                int i4 = iArr[i3];
                int i5 = (i4 >> 8) & 255;
                int i6 = i4 & 255;
                int i7 = i4 >>> 24;
                Paint paint4 = paint3;
                int i8 = i2;
                double d = (double) ((i4 >> 16) & 255);
                Double.isNaN(d);
                double d2 = d * 0.299d;
                double d3 = (double) i5;
                Double.isNaN(d3);
                double d4 = d2 + (d3 * 0.587d);
                double d5 = (double) i6;
                Double.isNaN(d5);
                double saturate = saturate((d4 + (d5 * 0.144d)) / 255.0d);
                double d6 = (double) i7;
                Double.isNaN(d6);
                iArr[i3] = ((int) (d6 * saturate)) << 24;
                i3++;
                i2 = i8;
                paint3 = paint4;
            }
            Paint paint5 = paint3;
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, height);
            draw(canvas6, paint2, f);
            Paint paint6 = paint5;
            paint6.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            Canvas canvas7 = canvas5;
            canvas7.drawBitmap(createBitmap2, 0.0f, 0.0f, null);
            canvas7.drawBitmap(createBitmap, 0.0f, 0.0f, paint6);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
            return;
        }
        Canvas canvas8 = canvas;
        float f2 = f;
        draw(canvas, paint, f);
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        float f2 = f * this.mOpacity;
        if (f2 > 0.01f) {
            boolean z = false;
            boolean z2 = this.mPath == null;
            if (z2) {
                this.mPath = getPath(canvas, paint);
                this.mPath.setFillType(this.fillRule);
            }
            if (this.vectorEffect == 1) {
                z = true;
            }
            Path path = this.mPath;
            if (z) {
                path = new Path();
                this.mPath.transform(canvas.getMatrix(), path);
                canvas.setMatrix(null);
            }
            RectF rectF = new RectF();
            path.computeBounds(rectF, true);
            this.mBox = new RectF(rectF);
            new Matrix(canvas.getMatrix()).mapRect(rectF);
            setClientRect(rectF);
            clip(canvas, paint);
            if (setupFillPaint(paint, this.fillOpacity * f2)) {
                if (z2) {
                    this.mFillPath = new Path();
                    paint.getFillPath(path, this.mFillPath);
                }
                canvas.drawPath(path, paint);
            }
            if (setupStrokePaint(paint, f2 * this.strokeOpacity)) {
                if (z2) {
                    this.mStrokePath = new Path();
                    paint.getFillPath(path, this.mStrokePath);
                }
                canvas.drawPath(path, paint);
            }
        }
    }

    private boolean setupFillPaint(Paint paint, float f) {
        ReadableArray readableArray = this.fill;
        if (readableArray == null || readableArray.size() <= 0) {
            return false;
        }
        paint.reset();
        paint.setFlags(385);
        paint.setStyle(Style.FILL);
        setupPaint(paint, f, this.fill);
        return true;
    }

    private boolean setupStrokePaint(Paint paint, float f) {
        paint.reset();
        double relativeOnOther = relativeOnOther(this.strokeWidth);
        if (relativeOnOther != 0.0d) {
            ReadableArray readableArray = this.stroke;
            if (!(readableArray == null || readableArray.size() == 0)) {
                paint.setFlags(385);
                paint.setStyle(Style.STROKE);
                paint.setStrokeCap(this.strokeLinecap);
                paint.setStrokeJoin(this.strokeLinejoin);
                paint.setStrokeMiter(this.strokeMiterlimit * this.mScale);
                paint.setStrokeWidth((float) relativeOnOther);
                setupPaint(paint, f, this.stroke);
                SVGLength[] sVGLengthArr = this.strokeDasharray;
                if (sVGLengthArr != null) {
                    int length = sVGLengthArr.length;
                    float[] fArr = new float[length];
                    for (int i = 0; i < length; i++) {
                        fArr[i] = (float) relativeOnOther(this.strokeDasharray[i]);
                    }
                    paint.setPathEffect(new DashPathEffect(fArr, this.strokeDashoffset));
                }
                return true;
            }
        }
        return false;
    }

    private void setupPaint(Paint paint, float f, ReadableArray readableArray) {
        double d;
        int i = readableArray.getInt(0);
        if (i != 0) {
            if (i == 1) {
                Brush definedBrush = getSvgView().getDefinedBrush(readableArray.getString(1));
                if (definedBrush != null) {
                    definedBrush.setupPaint(paint, this.mBox, this.mScale, f);
                }
            } else if (i == 2) {
                paint.setColor(getSvgView().mTintColor);
            }
        } else if (readableArray.size() == 2) {
            int i2 = readableArray.getInt(1);
            paint.setColor((Math.round(((float) (i2 >>> 24)) * f) << 24) | (i2 & ViewCompat.MEASURED_SIZE_MASK));
        } else {
            if (readableArray.size() > 4) {
                double d2 = readableArray.getDouble(4);
                double d3 = (double) f;
                Double.isNaN(d3);
                d = d2 * d3 * 255.0d;
            } else {
                d = (double) (f * 255.0f);
            }
            paint.setARGB((int) d, (int) (readableArray.getDouble(1) * 255.0d), (int) (readableArray.getDouble(2) * 255.0d), (int) (readableArray.getDouble(3) * 255.0d));
        }
    }

    /* access modifiers changed from: 0000 */
    public int hitTest(float[] fArr) {
        if (this.mPath != null && this.mInvertible && this.mTransformInvertible) {
            float[] fArr2 = new float[2];
            this.mInvMatrix.mapPoints(fArr2, fArr);
            this.mInvTransform.mapPoints(fArr2);
            int round = Math.round(fArr2[0]);
            int round2 = Math.round(fArr2[1]);
            if (this.mRegion == null && this.mFillPath != null) {
                this.mRegion = getRegion(this.mFillPath);
            }
            if (this.mRegion == null && this.mPath != null) {
                this.mRegion = getRegion(this.mPath);
            }
            if (this.mStrokeRegion == null && this.mStrokePath != null) {
                this.mStrokeRegion = getRegion(this.mStrokePath);
            }
            if ((this.mRegion != null && this.mRegion.contains(round, round2)) || (this.mStrokeRegion != null && this.mStrokeRegion.contains(round, round2))) {
                Path clipPath = getClipPath();
                if (clipPath != null) {
                    if (this.mClipRegionPath != clipPath) {
                        this.mClipRegionPath = clipPath;
                        this.mClipRegion = getRegion(clipPath);
                    }
                    if (!this.mClipRegion.contains(round, round2)) {
                        return -1;
                    }
                }
                return getId();
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public Region getRegion(Path path) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) Math.floor((double) rectF.left), (int) Math.floor((double) rectF.top), (int) Math.ceil((double) rectF.right), (int) Math.ceil((double) rectF.bottom)));
        return region;
    }

    private ArrayList<String> getAttributeList() {
        return this.mAttributeList;
    }

    /* access modifiers changed from: 0000 */
    public void mergeProperties(RenderableView renderableView) {
        ArrayList<String> attributeList = renderableView.getAttributeList();
        if (attributeList != null && attributeList.size() != 0) {
            this.mOriginProperties = new ArrayList<>();
            ArrayList<String> arrayList = this.mPropList;
            this.mAttributeList = arrayList == null ? new ArrayList<>() : new ArrayList<>(arrayList);
            int i = 0;
            int size = attributeList.size();
            while (i < size) {
                try {
                    String str = (String) attributeList.get(i);
                    Field field = getClass().getField(str);
                    Object obj = field.get(renderableView);
                    this.mOriginProperties.add(field.get(this));
                    if (!hasOwnProperty(str)) {
                        this.mAttributeList.add(str);
                        field.set(this, obj);
                    }
                    i++;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            this.mLastMergedList = attributeList;
        }
    }

    /* access modifiers changed from: 0000 */
    public void resetProperties() {
        ArrayList<String> arrayList = this.mLastMergedList;
        if (arrayList != null && this.mOriginProperties != null) {
            try {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    getClass().getField((String) this.mLastMergedList.get(size)).set(this, this.mOriginProperties.get(size));
                }
                this.mLastMergedList = null;
                this.mOriginProperties = null;
                this.mAttributeList = this.mPropList;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private boolean hasOwnProperty(String str) {
        ArrayList<String> arrayList = this.mAttributeList;
        return arrayList != null && arrayList.contains(str);
    }
}
