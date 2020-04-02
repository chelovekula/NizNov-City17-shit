package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region.Op;
import android.view.View;
import android.view.ViewParent;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.ArrayList;
import javax.annotation.Nullable;

@SuppressLint({"ViewConstructor"})
class TextView extends GroupView {
    double cachedAdvance = Double.NaN;
    AlignmentBaseline mAlignmentBaseline;
    String mBaselineShift = null;
    @Nullable
    ArrayList<SVGLength> mDeltaX;
    @Nullable
    ArrayList<SVGLength> mDeltaY;
    TextLengthAdjust mLengthAdjust = TextLengthAdjust.spacing;
    @Nullable
    ArrayList<SVGLength> mPositionX;
    @Nullable
    ArrayList<SVGLength> mPositionY;
    @Nullable
    ArrayList<SVGLength> mRotate;
    SVGLength mTextLength = null;

    public TextView(ReactContext reactContext) {
        super(reactContext);
    }

    public void invalidate() {
        if (this.mPath != null) {
            super.invalidate();
            getTextContainer().clearChildCache();
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearCache() {
        this.cachedAdvance = Double.NaN;
        super.clearCache();
    }

    @ReactProp(name = "textLength")
    public void setTextLength(Dynamic dynamic) {
        this.mTextLength = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "lengthAdjust")
    public void setLengthAdjust(@Nullable String str) {
        this.mLengthAdjust = TextLengthAdjust.valueOf(str);
        invalidate();
    }

    @ReactProp(name = "alignmentBaseline")
    public void setMethod(@Nullable String str) {
        this.mAlignmentBaseline = AlignmentBaseline.getEnum(str);
        invalidate();
    }

    @ReactProp(name = "baselineShift")
    public void setBaselineShift(Dynamic dynamic) {
        this.mBaselineShift = SVGLength.toString(dynamic);
        invalidate();
    }

    @ReactProp(name = "verticalAlign")
    public void setVerticalAlign(@Nullable String str) {
        if (str != null) {
            String trim = str.trim();
            int lastIndexOf = trim.lastIndexOf(32);
            try {
                this.mAlignmentBaseline = AlignmentBaseline.getEnum(trim.substring(lastIndexOf));
            } catch (IllegalArgumentException unused) {
                this.mAlignmentBaseline = AlignmentBaseline.baseline;
            }
            try {
                this.mBaselineShift = trim.substring(0, lastIndexOf);
            } catch (IndexOutOfBoundsException unused2) {
                this.mBaselineShift = null;
            }
        } else {
            this.mAlignmentBaseline = AlignmentBaseline.baseline;
            this.mBaselineShift = null;
        }
        invalidate();
    }

    @ReactProp(name = "rotate")
    public void setRotate(Dynamic dynamic) {
        this.mRotate = SVGLength.arrayFrom(dynamic);
        invalidate();
    }

    @ReactProp(name = "dx")
    public void setDeltaX(Dynamic dynamic) {
        this.mDeltaX = SVGLength.arrayFrom(dynamic);
        invalidate();
    }

    @ReactProp(name = "dy")
    public void setDeltaY(Dynamic dynamic) {
        this.mDeltaY = SVGLength.arrayFrom(dynamic);
        invalidate();
    }

    @ReactProp(name = "x")
    public void setPositionX(Dynamic dynamic) {
        this.mPositionX = SVGLength.arrayFrom(dynamic);
        invalidate();
    }

    @ReactProp(name = "y")
    public void setPositionY(Dynamic dynamic) {
        this.mPositionY = SVGLength.arrayFrom(dynamic);
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        if (f > 0.01f) {
            setupGlyphContext(canvas);
            clip(canvas, paint);
            getGroupPath(canvas, paint);
            drawGroup(canvas, paint, f);
        }
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint) {
        if (this.mPath != null) {
            return this.mPath;
        }
        setupGlyphContext(canvas);
        return getGroupPath(canvas, paint);
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint, Op op) {
        return getPath(canvas, paint);
    }

    /* access modifiers changed from: 0000 */
    public AlignmentBaseline getAlignmentBaseline() {
        if (this.mAlignmentBaseline == null) {
            for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof TextView) {
                    AlignmentBaseline alignmentBaseline = ((TextView) parent).mAlignmentBaseline;
                    if (alignmentBaseline != null) {
                        this.mAlignmentBaseline = alignmentBaseline;
                        return alignmentBaseline;
                    }
                }
            }
        }
        if (this.mAlignmentBaseline == null) {
            this.mAlignmentBaseline = AlignmentBaseline.baseline;
        }
        return this.mAlignmentBaseline;
    }

    /* access modifiers changed from: 0000 */
    public String getBaselineShift() {
        if (this.mBaselineShift == null) {
            for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof TextView) {
                    String str = ((TextView) parent).mBaselineShift;
                    if (str != null) {
                        this.mBaselineShift = str;
                        return str;
                    }
                }
            }
        }
        return this.mBaselineShift;
    }

    /* access modifiers changed from: 0000 */
    public Path getGroupPath(Canvas canvas, Paint paint) {
        if (this.mPath != null) {
            return this.mPath;
        }
        pushGlyphContext();
        this.mPath = super.getPath(canvas, paint);
        popGlyphContext();
        return this.mPath;
    }

    /* access modifiers changed from: 0000 */
    public void pushGlyphContext() {
        getTextRootGlyphContext().pushContext(!(this instanceof TextPathView) && !(this instanceof TSpanView), this, this.mFont, this.mPositionX, this.mPositionY, this.mDeltaX, this.mDeltaY, this.mRotate);
    }

    /* access modifiers changed from: 0000 */
    public TextView getTextAnchorRoot() {
        ArrayList<FontData> arrayList = getTextRootGlyphContext().mFontContext;
        int size = arrayList.size() - 1;
        ViewParent parent = getParent();
        TextView textView = this;
        while (size >= 0 && (parent instanceof TextView) && ((FontData) arrayList.get(size)).textAnchor != TextAnchor.start && textView.mPositionX == null) {
            textView = (TextView) parent;
            parent = textView.getParent();
            size--;
        }
        return textView;
    }

    /* access modifiers changed from: 0000 */
    public double getSubtreeTextChunksTotalAdvance(Paint paint) {
        if (!Double.isNaN(this.cachedAdvance)) {
            return this.cachedAdvance;
        }
        double d = 0.0d;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof TextView) {
                d += ((TextView) childAt).getSubtreeTextChunksTotalAdvance(paint);
            }
        }
        this.cachedAdvance = d;
        return d;
    }

    /* access modifiers changed from: 0000 */
    public TextView getTextContainer() {
        ViewParent parent = getParent();
        TextView textView = this;
        while (parent instanceof TextView) {
            textView = (TextView) parent;
            parent = textView.getParent();
        }
        return textView;
    }
}
