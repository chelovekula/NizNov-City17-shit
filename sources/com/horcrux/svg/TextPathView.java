package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import javax.annotation.Nullable;

@SuppressLint({"ViewConstructor"})
class TextPathView extends TextView {
    private String mHref;
    private TextPathMethod mMethod = TextPathMethod.align;
    private TextPathMidLine mMidLine;
    private TextPathSide mSide;
    private TextPathSpacing mSpacing = TextPathSpacing.exact;
    @Nullable
    private SVGLength mStartOffset;

    /* access modifiers changed from: 0000 */
    public void popGlyphContext() {
    }

    /* access modifiers changed from: 0000 */
    public void pushGlyphContext() {
    }

    public TextPathView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(name = "href")
    public void setHref(String str) {
        this.mHref = str;
        invalidate();
    }

    @ReactProp(name = "startOffset")
    public void setStartOffset(Dynamic dynamic) {
        this.mStartOffset = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "method")
    public void setMethod(@Nullable String str) {
        this.mMethod = TextPathMethod.valueOf(str);
        invalidate();
    }

    @ReactProp(name = "spacing")
    public void setSpacing(@Nullable String str) {
        this.mSpacing = TextPathSpacing.valueOf(str);
        invalidate();
    }

    @ReactProp(name = "side")
    public void setSide(@Nullable String str) {
        this.mSide = TextPathSide.valueOf(str);
        invalidate();
    }

    @ReactProp(name = "midLine")
    public void setSharp(@Nullable String str) {
        this.mMidLine = TextPathMidLine.valueOf(str);
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public TextPathMethod getMethod() {
        return this.mMethod;
    }

    /* access modifiers changed from: 0000 */
    public TextPathSpacing getSpacing() {
        return this.mSpacing;
    }

    /* access modifiers changed from: 0000 */
    public TextPathSide getSide() {
        return this.mSide;
    }

    /* access modifiers changed from: 0000 */
    public TextPathMidLine getMidLine() {
        return this.mMidLine;
    }

    /* access modifiers changed from: 0000 */
    public SVGLength getStartOffset() {
        return this.mStartOffset;
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        drawGroup(canvas, paint, f);
    }

    /* access modifiers changed from: 0000 */
    public Path getTextPath(Canvas canvas, Paint paint) {
        VirtualView definedTemplate = getSvgView().getDefinedTemplate(this.mHref);
        if (definedTemplate == null || !(definedTemplate instanceof RenderableView)) {
            return null;
        }
        return ((RenderableView) definedTemplate).getPath(canvas, paint);
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint) {
        return getGroupPath(canvas, paint);
    }
}
