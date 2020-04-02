package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.imagehelper.ImageSource;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;

@SuppressLint({"ViewConstructor"})
class ImageView extends RenderableView {
    private String mAlign;

    /* renamed from: mH */
    private SVGLength f63mH;
    private int mImageHeight;
    private int mImageWidth;
    /* access modifiers changed from: private */
    public final AtomicBoolean mLoading = new AtomicBoolean(false);
    private int mMeetOrSlice;

    /* renamed from: mW */
    private SVGLength f64mW;

    /* renamed from: mX */
    private SVGLength f65mX;

    /* renamed from: mY */
    private SVGLength f66mY;
    private String uriString;

    public ImageView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(name = "x")
    public void setX(Dynamic dynamic) {
        this.f65mX = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "y")
    public void setY(Dynamic dynamic) {
        this.f66mY = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "width")
    public void setWidth(Dynamic dynamic) {
        this.f64mW = SVGLength.from(dynamic);
        invalidate();
    }

    @ReactProp(name = "height")
    public void setHeight(Dynamic dynamic) {
        this.f63mH = SVGLength.from(dynamic);
        invalidate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    @com.facebook.react.uimanager.annotations.ReactProp(name = "src")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSrc(@javax.annotation.Nullable com.facebook.react.bridge.ReadableMap r4) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x004f
            java.lang.String r0 = "uri"
            java.lang.String r0 = r4.getString(r0)
            r3.uriString = r0
            java.lang.String r0 = r3.uriString
            if (r0 == 0) goto L_0x004f
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0015
            goto L_0x004f
        L_0x0015:
            java.lang.String r0 = "width"
            boolean r1 = r4.hasKey(r0)
            if (r1 == 0) goto L_0x0032
            java.lang.String r1 = "height"
            boolean r2 = r4.hasKey(r1)
            if (r2 == 0) goto L_0x0032
            int r0 = r4.getInt(r0)
            r3.mImageWidth = r0
            int r4 = r4.getInt(r1)
            r3.mImageHeight = r4
            goto L_0x0037
        L_0x0032:
            r4 = 0
            r3.mImageWidth = r4
            r3.mImageHeight = r4
        L_0x0037:
            java.lang.String r4 = r3.uriString
            android.net.Uri r4 = android.net.Uri.parse(r4)
            java.lang.String r4 = r4.getScheme()
            if (r4 != 0) goto L_0x004f
            com.facebook.react.views.imagehelper.ResourceDrawableIdHelper r4 = com.facebook.react.views.imagehelper.ResourceDrawableIdHelper.getInstance()
            com.facebook.react.bridge.ReactContext r0 = r3.mContext
            java.lang.String r1 = r3.uriString
            r4.getResourceDrawableUri(r0, r1)
        L_0x004f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.ImageView.setSrc(com.facebook.react.bridge.ReadableMap):void");
    }

    @ReactProp(name = "align")
    public void setAlign(String str) {
        this.mAlign = str;
        invalidate();
    }

    @ReactProp(name = "meetOrSlice")
    public void setMeetOrSlice(int i) {
        this.mMeetOrSlice = i;
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        if (!this.mLoading.get()) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            ImageRequest fromUri = ImageRequest.fromUri(new ImageSource(this.mContext, this.uriString).getUri());
            if (imagePipeline.isInBitmapMemoryCache(fromUri)) {
                tryRenderFromBitmapCache(imagePipeline, fromUri, canvas, paint, f * this.mOpacity);
                return;
            }
            loadBitmap(imagePipeline, fromUri);
        }
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint) {
        Path path = new Path();
        path.addRect(getRect(), Direction.CW);
        return path;
    }

    private void loadBitmap(ImagePipeline imagePipeline, ImageRequest imageRequest) {
        this.mLoading.set(true);
        imagePipeline.fetchDecodedImage(imageRequest, this.mContext).subscribe(new BaseBitmapDataSubscriber() {
            public void onNewResultImpl(Bitmap bitmap) {
                ImageView.this.mLoading.set(false);
                SvgView svgView = ImageView.this.getSvgView();
                if (svgView != null) {
                    svgView.invalidate();
                }
            }

            public void onFailureImpl(DataSource dataSource) {
                ImageView.this.mLoading.set(false);
                FLog.m93w(ReactConstants.TAG, dataSource.getFailureCause(), "RNSVG: fetchDecodedImage failed!", new Object[0]);
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    @Nonnull
    private RectF getRect() {
        double relativeOnWidth = relativeOnWidth(this.f65mX);
        double relativeOnHeight = relativeOnHeight(this.f66mY);
        double relativeOnWidth2 = relativeOnWidth(this.f64mW);
        double relativeOnHeight2 = relativeOnHeight(this.f63mH);
        if (relativeOnWidth2 == 0.0d) {
            relativeOnWidth2 = (double) (((float) this.mImageWidth) * this.mScale);
        }
        if (relativeOnHeight2 == 0.0d) {
            relativeOnHeight2 = (double) (((float) this.mImageHeight) * this.mScale);
        }
        return new RectF((float) relativeOnWidth, (float) relativeOnHeight, (float) (relativeOnWidth + relativeOnWidth2), (float) (relativeOnHeight + relativeOnHeight2));
    }

    private void doRender(Canvas canvas, Paint paint, Bitmap bitmap, float f) {
        if (this.mImageWidth == 0 || this.mImageHeight == 0) {
            this.mImageWidth = bitmap.getWidth();
            this.mImageHeight = bitmap.getHeight();
        }
        RectF rect = getRect();
        RectF rectF = new RectF(0.0f, 0.0f, (float) this.mImageWidth, (float) this.mImageHeight);
        ViewBox.getTransform(rectF, rect, this.mAlign, this.mMeetOrSlice).mapRect(rectF);
        canvas.clipPath(getPath(canvas, paint));
        Path clipPath = getClipPath(canvas, paint);
        if (clipPath != null) {
            canvas.clipPath(clipPath);
        }
        Paint paint2 = new Paint();
        paint2.setAlpha((int) (f * 255.0f));
        canvas.drawBitmap(bitmap, null, rectF, paint2);
        canvas.getMatrix().mapRect(rectF);
        setClientRect(rectF);
    }

    private void tryRenderFromBitmapCache(ImagePipeline imagePipeline, ImageRequest imageRequest, Canvas canvas, Paint paint, float f) {
        CloseableReference closeableReference;
        DataSource fetchImageFromBitmapCache = imagePipeline.fetchImageFromBitmapCache(imageRequest, this.mContext);
        try {
            closeableReference = (CloseableReference) fetchImageFromBitmapCache.getResult();
            if (closeableReference == null) {
                fetchImageFromBitmapCache.close();
                return;
            }
            CloseableImage closeableImage = (CloseableImage) closeableReference.get();
            if (!(closeableImage instanceof CloseableBitmap)) {
                CloseableReference.closeSafely(closeableReference);
                fetchImageFromBitmapCache.close();
                return;
            }
            Bitmap underlyingBitmap = ((CloseableBitmap) closeableImage).getUnderlyingBitmap();
            if (underlyingBitmap == null) {
                CloseableReference.closeSafely(closeableReference);
                fetchImageFromBitmapCache.close();
                return;
            }
            doRender(canvas, paint, underlyingBitmap, f);
            CloseableReference.closeSafely(closeableReference);
            fetchImageFromBitmapCache.close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } catch (Exception e2) {
            try {
                throw new IllegalStateException(e2);
            } catch (Throwable th) {
                fetchImageFromBitmapCache.close();
                throw th;
            }
        } catch (Throwable th2) {
            CloseableReference.closeSafely(closeableReference);
            throw th2;
        }
    }
}
