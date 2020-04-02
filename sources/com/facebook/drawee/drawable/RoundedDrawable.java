package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.util.Arrays;

public abstract class RoundedDrawable extends Drawable implements Rounded, TransformAwareDrawable {
    @VisibleForTesting
    final RectF mBitmapBounds = new RectF();
    protected int mBorderColor = 0;
    protected final Path mBorderPath = new Path();
    @VisibleForTesting
    final float[] mBorderRadii = new float[8];
    protected float mBorderWidth = 0.0f;
    @VisibleForTesting
    final Matrix mBoundsTransform = new Matrix();
    private final float[] mCornerRadii = new float[8];
    private final Drawable mDelegate;
    @VisibleForTesting
    final RectF mDrawableBounds = new RectF();
    @VisibleForTesting
    @Nullable
    RectF mInsideBorderBounds;
    @VisibleForTesting
    @Nullable
    float[] mInsideBorderRadii;
    @VisibleForTesting
    @Nullable
    Matrix mInsideBorderTransform;
    @VisibleForTesting
    final Matrix mInverseParentTransform = new Matrix();
    protected boolean mIsCircle = false;
    private boolean mIsPathDirty = true;
    protected boolean mIsShaderTransformDirty = true;
    private float mPadding = 0.0f;
    private boolean mPaintFilterBitmap = false;
    @VisibleForTesting
    final Matrix mParentTransform = new Matrix();
    protected final Path mPath = new Path();
    @VisibleForTesting
    final Matrix mPrevBoundsTransform = new Matrix();
    @VisibleForTesting
    @Nullable
    Matrix mPrevInsideBorderTransform;
    @VisibleForTesting
    final Matrix mPrevParentTransform = new Matrix();
    @VisibleForTesting
    final RectF mPrevRootBounds = new RectF();
    protected boolean mRadiiNonZero = false;
    @VisibleForTesting
    final RectF mRootBounds = new RectF();
    private boolean mScaleDownInsideBorders = false;
    @VisibleForTesting
    final Matrix mTransform = new Matrix();
    @Nullable
    private TransformCallback mTransformCallback;

    RoundedDrawable(Drawable drawable) {
        this.mDelegate = drawable;
    }

    public void setCircle(boolean z) {
        this.mIsCircle = z;
        this.mIsPathDirty = true;
        invalidateSelf();
    }

    public boolean isCircle() {
        return this.mIsCircle;
    }

    public void setRadius(float f) {
        boolean z = false;
        Preconditions.checkState(f >= 0.0f);
        Arrays.fill(this.mCornerRadii, f);
        if (f != 0.0f) {
            z = true;
        }
        this.mRadiiNonZero = z;
        this.mIsPathDirty = true;
        invalidateSelf();
    }

    public void setRadii(float[] fArr) {
        if (fArr == null) {
            Arrays.fill(this.mCornerRadii, 0.0f);
            this.mRadiiNonZero = false;
        } else {
            Preconditions.checkArgument(fArr.length == 8, "radii should have exactly 8 values");
            System.arraycopy(fArr, 0, this.mCornerRadii, 0, 8);
            this.mRadiiNonZero = false;
            for (int i = 0; i < 8; i++) {
                this.mRadiiNonZero |= fArr[i] > 0.0f;
            }
        }
        this.mIsPathDirty = true;
        invalidateSelf();
    }

    public float[] getRadii() {
        return this.mCornerRadii;
    }

    public void setBorder(int i, float f) {
        if (this.mBorderColor != i || this.mBorderWidth != f) {
            this.mBorderColor = i;
            this.mBorderWidth = f;
            this.mIsPathDirty = true;
            invalidateSelf();
        }
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setPadding(float f) {
        if (this.mPadding != f) {
            this.mPadding = f;
            this.mIsPathDirty = true;
            invalidateSelf();
        }
    }

    public float getPadding() {
        return this.mPadding;
    }

    public void setScaleDownInsideBorders(boolean z) {
        if (this.mScaleDownInsideBorders != z) {
            this.mScaleDownInsideBorders = z;
            this.mIsPathDirty = true;
            invalidateSelf();
        }
    }

    public boolean getScaleDownInsideBorders() {
        return this.mScaleDownInsideBorders;
    }

    public void setPaintFilterBitmap(boolean z) {
        if (this.mPaintFilterBitmap != z) {
            this.mPaintFilterBitmap = z;
            invalidateSelf();
        }
    }

    public boolean getPaintFilterBitmap() {
        return this.mPaintFilterBitmap;
    }

    public void setTransformCallback(@Nullable TransformCallback transformCallback) {
        this.mTransformCallback = transformCallback;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00a0, code lost:
        if (r0.equals(r4.mPrevInsideBorderTransform) == false) goto L_0x00a2;
     */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateTransform() {
        /*
            r4 = this;
            com.facebook.drawee.drawable.TransformCallback r0 = r4.mTransformCallback
            if (r0 == 0) goto L_0x0011
            android.graphics.Matrix r1 = r4.mParentTransform
            r0.getTransform(r1)
            com.facebook.drawee.drawable.TransformCallback r0 = r4.mTransformCallback
            android.graphics.RectF r1 = r4.mRootBounds
            r0.getRootBounds(r1)
            goto L_0x001f
        L_0x0011:
            android.graphics.Matrix r0 = r4.mParentTransform
            r0.reset()
            android.graphics.RectF r0 = r4.mRootBounds
            android.graphics.Rect r1 = r4.getBounds()
            r0.set(r1)
        L_0x001f:
            android.graphics.RectF r0 = r4.mBitmapBounds
            int r1 = r4.getIntrinsicWidth()
            float r1 = (float) r1
            int r2 = r4.getIntrinsicHeight()
            float r2 = (float) r2
            r3 = 0
            r0.set(r3, r3, r1, r2)
            android.graphics.RectF r0 = r4.mDrawableBounds
            android.graphics.drawable.Drawable r1 = r4.mDelegate
            android.graphics.Rect r1 = r1.getBounds()
            r0.set(r1)
            android.graphics.Matrix r0 = r4.mBoundsTransform
            android.graphics.RectF r1 = r4.mBitmapBounds
            android.graphics.RectF r2 = r4.mDrawableBounds
            android.graphics.Matrix$ScaleToFit r3 = android.graphics.Matrix.ScaleToFit.FILL
            r0.setRectToRect(r1, r2, r3)
            boolean r0 = r4.mScaleDownInsideBorders
            if (r0 == 0) goto L_0x007a
            android.graphics.RectF r0 = r4.mInsideBorderBounds
            if (r0 != 0) goto L_0x0057
            android.graphics.RectF r0 = new android.graphics.RectF
            android.graphics.RectF r1 = r4.mRootBounds
            r0.<init>(r1)
            r4.mInsideBorderBounds = r0
            goto L_0x005c
        L_0x0057:
            android.graphics.RectF r1 = r4.mRootBounds
            r0.set(r1)
        L_0x005c:
            android.graphics.RectF r0 = r4.mInsideBorderBounds
            float r1 = r4.mBorderWidth
            r0.inset(r1, r1)
            android.graphics.Matrix r0 = r4.mInsideBorderTransform
            if (r0 != 0) goto L_0x006e
            android.graphics.Matrix r0 = new android.graphics.Matrix
            r0.<init>()
            r4.mInsideBorderTransform = r0
        L_0x006e:
            android.graphics.Matrix r0 = r4.mInsideBorderTransform
            android.graphics.RectF r1 = r4.mRootBounds
            android.graphics.RectF r2 = r4.mInsideBorderBounds
            android.graphics.Matrix$ScaleToFit r3 = android.graphics.Matrix.ScaleToFit.FILL
            r0.setRectToRect(r1, r2, r3)
            goto L_0x0081
        L_0x007a:
            android.graphics.Matrix r0 = r4.mInsideBorderTransform
            if (r0 == 0) goto L_0x0081
            r0.reset()
        L_0x0081:
            android.graphics.Matrix r0 = r4.mParentTransform
            android.graphics.Matrix r1 = r4.mPrevParentTransform
            boolean r0 = r0.equals(r1)
            r1 = 1
            if (r0 == 0) goto L_0x00a2
            android.graphics.Matrix r0 = r4.mBoundsTransform
            android.graphics.Matrix r2 = r4.mPrevBoundsTransform
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x00a2
            android.graphics.Matrix r0 = r4.mInsideBorderTransform
            if (r0 == 0) goto L_0x00f1
            android.graphics.Matrix r2 = r4.mPrevInsideBorderTransform
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x00f1
        L_0x00a2:
            r4.mIsShaderTransformDirty = r1
            android.graphics.Matrix r0 = r4.mParentTransform
            android.graphics.Matrix r2 = r4.mInverseParentTransform
            r0.invert(r2)
            android.graphics.Matrix r0 = r4.mTransform
            android.graphics.Matrix r2 = r4.mParentTransform
            r0.set(r2)
            boolean r0 = r4.mScaleDownInsideBorders
            if (r0 == 0) goto L_0x00bd
            android.graphics.Matrix r0 = r4.mTransform
            android.graphics.Matrix r2 = r4.mInsideBorderTransform
            r0.postConcat(r2)
        L_0x00bd:
            android.graphics.Matrix r0 = r4.mTransform
            android.graphics.Matrix r2 = r4.mBoundsTransform
            r0.preConcat(r2)
            android.graphics.Matrix r0 = r4.mPrevParentTransform
            android.graphics.Matrix r2 = r4.mParentTransform
            r0.set(r2)
            android.graphics.Matrix r0 = r4.mPrevBoundsTransform
            android.graphics.Matrix r2 = r4.mBoundsTransform
            r0.set(r2)
            boolean r0 = r4.mScaleDownInsideBorders
            if (r0 == 0) goto L_0x00ea
            android.graphics.Matrix r0 = r4.mPrevInsideBorderTransform
            if (r0 != 0) goto L_0x00e4
            android.graphics.Matrix r0 = new android.graphics.Matrix
            android.graphics.Matrix r2 = r4.mInsideBorderTransform
            r0.<init>(r2)
            r4.mPrevInsideBorderTransform = r0
            goto L_0x00f1
        L_0x00e4:
            android.graphics.Matrix r2 = r4.mInsideBorderTransform
            r0.set(r2)
            goto L_0x00f1
        L_0x00ea:
            android.graphics.Matrix r0 = r4.mPrevInsideBorderTransform
            if (r0 == 0) goto L_0x00f1
            r0.reset()
        L_0x00f1:
            android.graphics.RectF r0 = r4.mRootBounds
            android.graphics.RectF r2 = r4.mPrevRootBounds
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x0104
            r4.mIsPathDirty = r1
            android.graphics.RectF r0 = r4.mPrevRootBounds
            android.graphics.RectF r1 = r4.mRootBounds
            r0.set(r1)
        L_0x0104:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.drawee.drawable.RoundedDrawable.updateTransform():void");
    }

    /* access modifiers changed from: protected */
    public void updatePath() {
        float[] fArr;
        if (this.mIsPathDirty) {
            this.mBorderPath.reset();
            RectF rectF = this.mRootBounds;
            float f = this.mBorderWidth;
            rectF.inset(f / 2.0f, f / 2.0f);
            if (this.mIsCircle) {
                this.mBorderPath.addCircle(this.mRootBounds.centerX(), this.mRootBounds.centerY(), Math.min(this.mRootBounds.width(), this.mRootBounds.height()) / 2.0f, Direction.CW);
            } else {
                int i = 0;
                while (true) {
                    fArr = this.mBorderRadii;
                    if (i >= fArr.length) {
                        break;
                    }
                    fArr[i] = (this.mCornerRadii[i] + this.mPadding) - (this.mBorderWidth / 2.0f);
                    i++;
                }
                this.mBorderPath.addRoundRect(this.mRootBounds, fArr, Direction.CW);
            }
            RectF rectF2 = this.mRootBounds;
            float f2 = this.mBorderWidth;
            rectF2.inset((-f2) / 2.0f, (-f2) / 2.0f);
            this.mPath.reset();
            float f3 = this.mPadding + (this.mScaleDownInsideBorders ? this.mBorderWidth : 0.0f);
            this.mRootBounds.inset(f3, f3);
            if (this.mIsCircle) {
                this.mPath.addCircle(this.mRootBounds.centerX(), this.mRootBounds.centerY(), Math.min(this.mRootBounds.width(), this.mRootBounds.height()) / 2.0f, Direction.CW);
            } else if (this.mScaleDownInsideBorders) {
                if (this.mInsideBorderRadii == null) {
                    this.mInsideBorderRadii = new float[8];
                }
                for (int i2 = 0; i2 < this.mBorderRadii.length; i2++) {
                    this.mInsideBorderRadii[i2] = this.mCornerRadii[i2] - this.mBorderWidth;
                }
                this.mPath.addRoundRect(this.mRootBounds, this.mInsideBorderRadii, Direction.CW);
            } else {
                this.mPath.addRoundRect(this.mRootBounds, this.mCornerRadii, Direction.CW);
            }
            float f4 = -f3;
            this.mRootBounds.inset(f4, f4);
            this.mPath.setFillType(FillType.WINDING);
            this.mIsPathDirty = false;
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean shouldRound() {
        return this.mIsCircle || this.mRadiiNonZero || this.mBorderWidth > 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mDelegate.setBounds(rect);
    }

    public int getIntrinsicWidth() {
        return this.mDelegate.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.mDelegate.getIntrinsicHeight();
    }

    public int getOpacity() {
        return this.mDelegate.getOpacity();
    }

    public void setColorFilter(int i, @NonNull Mode mode) {
        this.mDelegate.setColorFilter(i, mode);
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.mDelegate.setColorFilter(colorFilter);
    }

    @RequiresApi(api = 21)
    @Nullable
    public ColorFilter getColorFilter() {
        return this.mDelegate.getColorFilter();
    }

    public void clearColorFilter() {
        this.mDelegate.clearColorFilter();
    }

    @RequiresApi(api = 19)
    public int getAlpha() {
        return this.mDelegate.getAlpha();
    }

    public void setAlpha(int i) {
        this.mDelegate.setAlpha(i);
    }

    public void draw(@NonNull Canvas canvas) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("RoundedDrawable#draw");
        }
        this.mDelegate.draw(canvas);
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }
}
