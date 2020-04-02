package com.airbnb.android.react.maps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class ImageReader {
    private final Context context;
    /* access modifiers changed from: private */
    public DataSource<CloseableReference<CloseableImage>> dataSource;
    /* access modifiers changed from: private */
    public final ImageReadable imp;
    private final DraweeHolder<?> logoHolder;
    private final ControllerListener<ImageInfo> mLogoControllerListener = new BaseControllerListener<ImageInfo>() {
        /* JADX WARNING: Removed duplicated region for block: B:22:0x006a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onFinalImageSet(java.lang.String r3, @javax.annotation.Nullable com.facebook.imagepipeline.image.ImageInfo r4, @javax.annotation.Nullable android.graphics.drawable.Animatable r5) {
            /*
                r2 = this;
                r3 = 0
                com.airbnb.android.react.maps.ImageReader r4 = com.airbnb.android.react.maps.ImageReader.this     // Catch:{ all -> 0x005b }
                com.facebook.datasource.DataSource r4 = r4.dataSource     // Catch:{ all -> 0x005b }
                java.lang.Object r4 = r4.getResult()     // Catch:{ all -> 0x005b }
                com.facebook.common.references.CloseableReference r4 = (com.facebook.common.references.CloseableReference) r4     // Catch:{ all -> 0x005b }
                if (r4 == 0) goto L_0x0043
                java.lang.Object r3 = r4.get()     // Catch:{ all -> 0x0041 }
                com.facebook.imagepipeline.image.CloseableImage r3 = (com.facebook.imagepipeline.image.CloseableImage) r3     // Catch:{ all -> 0x0041 }
                if (r3 == 0) goto L_0x0043
                boolean r5 = r3 instanceof com.facebook.imagepipeline.image.CloseableStaticBitmap     // Catch:{ all -> 0x0041 }
                if (r5 == 0) goto L_0x0043
                com.facebook.imagepipeline.image.CloseableStaticBitmap r3 = (com.facebook.imagepipeline.image.CloseableStaticBitmap) r3     // Catch:{ all -> 0x0041 }
                android.graphics.Bitmap r3 = r3.getUnderlyingBitmap()     // Catch:{ all -> 0x0041 }
                if (r3 == 0) goto L_0x0043
                android.graphics.Bitmap$Config r5 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ all -> 0x0041 }
                r0 = 1
                android.graphics.Bitmap r3 = r3.copy(r5, r0)     // Catch:{ all -> 0x0041 }
                com.airbnb.android.react.maps.ImageReader r5 = com.airbnb.android.react.maps.ImageReader.this     // Catch:{ all -> 0x0041 }
                com.airbnb.android.react.maps.ImageReadable r5 = r5.imp     // Catch:{ all -> 0x0041 }
                r5.setIconBitmap(r3)     // Catch:{ all -> 0x0041 }
                com.airbnb.android.react.maps.ImageReader r5 = com.airbnb.android.react.maps.ImageReader.this     // Catch:{ all -> 0x0041 }
                com.airbnb.android.react.maps.ImageReadable r5 = r5.imp     // Catch:{ all -> 0x0041 }
                com.google.android.gms.maps.model.BitmapDescriptor r3 = com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(r3)     // Catch:{ all -> 0x0041 }
                r5.setIconBitmapDescriptor(r3)     // Catch:{ all -> 0x0041 }
                goto L_0x0043
            L_0x0041:
                r3 = move-exception
                goto L_0x005f
            L_0x0043:
                com.airbnb.android.react.maps.ImageReader r3 = com.airbnb.android.react.maps.ImageReader.this
                com.facebook.datasource.DataSource r3 = r3.dataSource
                r3.close()
                if (r4 == 0) goto L_0x0051
                com.facebook.common.references.CloseableReference.closeSafely(r4)
            L_0x0051:
                com.airbnb.android.react.maps.ImageReader r3 = com.airbnb.android.react.maps.ImageReader.this
                com.airbnb.android.react.maps.ImageReadable r3 = r3.imp
                r3.update()
                return
            L_0x005b:
                r4 = move-exception
                r1 = r4
                r4 = r3
                r3 = r1
            L_0x005f:
                com.airbnb.android.react.maps.ImageReader r5 = com.airbnb.android.react.maps.ImageReader.this
                com.facebook.datasource.DataSource r5 = r5.dataSource
                r5.close()
                if (r4 == 0) goto L_0x006d
                com.facebook.common.references.CloseableReference.closeSafely(r4)
            L_0x006d:
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.android.react.maps.ImageReader.C04561.onFinalImageSet(java.lang.String, com.facebook.imagepipeline.image.ImageInfo, android.graphics.drawable.Animatable):void");
        }
    };
    private final Resources resources;

    public ImageReader(Context context2, Resources resources2, ImageReadable imageReadable) {
        this.context = context2;
        this.resources = resources2;
        this.imp = imageReadable;
        this.logoHolder = DraweeHolder.create(createDraweeHeirarchy(resources2), context2);
        this.logoHolder.onAttach();
    }

    private GenericDraweeHierarchy createDraweeHeirarchy(Resources resources2) {
        return new GenericDraweeHierarchyBuilder(resources2).setActualImageScaleType(ScaleType.FIT_CENTER).setFadeDuration(0).build();
    }

    public void setImage(String str) {
        if (str == null) {
            this.imp.setIconBitmapDescriptor(null);
            this.imp.update();
        } else if (str.startsWith("http://") || str.startsWith("https://") || str.startsWith("file://") || str.startsWith("asset://")) {
            ImageRequest build = ImageRequestBuilder.newBuilderWithSource(Uri.parse(str)).build();
            this.dataSource = Fresco.getImagePipeline().fetchDecodedImage(build, this);
            this.logoHolder.setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setImageRequest(build)).setControllerListener(this.mLogoControllerListener)).setOldController(this.logoHolder.getController())).build());
        } else {
            BitmapDescriptor bitmapDescriptorByName = getBitmapDescriptorByName(str);
            if (bitmapDescriptorByName != null) {
                this.imp.setIconBitmapDescriptor(bitmapDescriptorByName);
                this.imp.setIconBitmap(BitmapFactory.decodeResource(this.resources, getDrawableResourceByName(str)));
            }
            this.imp.update();
        }
    }

    private int getDrawableResourceByName(String str) {
        return this.resources.getIdentifier(str, "drawable", this.context.getPackageName());
    }

    private BitmapDescriptor getBitmapDescriptorByName(String str) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(str));
    }
}
