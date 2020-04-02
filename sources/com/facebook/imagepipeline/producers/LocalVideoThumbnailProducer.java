package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class LocalVideoThumbnailProducer implements Producer<CloseableReference<CloseableImage>> {
    @VisibleForTesting
    static final String CREATED_THUMBNAIL = "createdThumbnail";
    public static final String PRODUCER_NAME = "VideoThumbnailProducer";
    /* access modifiers changed from: private */
    public final ContentResolver mContentResolver;
    private final Executor mExecutor;

    public LocalVideoThumbnailProducer(Executor executor, ContentResolver contentResolver) {
        this.mExecutor = executor;
        this.mContentResolver = contentResolver;
    }

    public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext producerContext) {
        final ProducerListener listener = producerContext.getListener();
        final String id = producerContext.getId();
        final ImageRequest imageRequest = producerContext.getImageRequest();
        final C05691 r0 = new StatefulProducerRunnable<CloseableReference<CloseableImage>>(consumer, listener, PRODUCER_NAME, id) {
            /* access modifiers changed from: protected */
            public void onSuccess(CloseableReference<CloseableImage> closeableReference) {
                super.onSuccess(closeableReference);
                listener.onUltimateProducerReached(id, LocalVideoThumbnailProducer.PRODUCER_NAME, closeableReference != null);
            }

            /* access modifiers changed from: protected */
            public void onFailure(Exception exc) {
                super.onFailure(exc);
                listener.onUltimateProducerReached(id, LocalVideoThumbnailProducer.PRODUCER_NAME, false);
            }

            /* access modifiers changed from: protected */
            @Nullable
            public CloseableReference<CloseableImage> getResult() throws Exception {
                String str;
                Bitmap bitmap;
                try {
                    str = LocalVideoThumbnailProducer.this.getLocalFilePath(imageRequest);
                } catch (IllegalArgumentException unused) {
                    str = null;
                }
                if (str != null) {
                    bitmap = ThumbnailUtils.createVideoThumbnail(str, LocalVideoThumbnailProducer.calculateKind(imageRequest));
                } else {
                    bitmap = LocalVideoThumbnailProducer.createThumbnailFromContentProvider(LocalVideoThumbnailProducer.this.mContentResolver, imageRequest.getSourceUri());
                }
                if (bitmap == null) {
                    return null;
                }
                return CloseableReference.m114of(new CloseableStaticBitmap(bitmap, (ResourceReleaser<Bitmap>) SimpleBitmapReleaser.getInstance(), ImmutableQualityInfo.FULL_QUALITY, 0));
            }

            /* access modifiers changed from: protected */
            public Map<String, String> getExtraMapOnSuccess(CloseableReference<CloseableImage> closeableReference) {
                return ImmutableMap.m22of(LocalVideoThumbnailProducer.CREATED_THUMBNAIL, String.valueOf(closeableReference != null));
            }

            /* access modifiers changed from: protected */
            public void disposeResult(CloseableReference<CloseableImage> closeableReference) {
                CloseableReference.closeSafely(closeableReference);
            }
        };
        producerContext.addCallbacks(new BaseProducerContextCallbacks() {
            public void onCancellationRequested() {
                r0.cancel();
            }
        });
        this.mExecutor.execute(r0);
    }

    /* access modifiers changed from: private */
    public static int calculateKind(ImageRequest imageRequest) {
        return (imageRequest.getPreferredWidth() > 96 || imageRequest.getPreferredHeight() > 96) ? 1 : 3;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0058 A[SYNTHETIC, Splitter:B:14:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0075  */
    @javax.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getLocalFilePath(com.facebook.imagepipeline.request.ImageRequest r12) {
        /*
            r11 = this;
            android.net.Uri r0 = r12.getSourceUri()
            boolean r1 = com.facebook.common.util.UriUtil.isLocalFileUri(r0)
            if (r1 == 0) goto L_0x0013
            java.io.File r12 = r12.getSourceFile()
            java.lang.String r12 = r12.getPath()
            return r12
        L_0x0013:
            boolean r12 = com.facebook.common.util.UriUtil.isLocalContentUri(r0)
            r1 = 0
            if (r12 == 0) goto L_0x0078
            int r12 = android.os.Build.VERSION.SDK_INT
            r2 = 19
            r3 = 0
            r4 = 1
            if (r12 < r2) goto L_0x0046
            java.lang.String r12 = r0.getAuthority()
            java.lang.String r2 = "com.android.providers.media.documents"
            boolean r12 = r2.equals(r12)
            if (r12 == 0) goto L_0x0046
            java.lang.String r12 = android.provider.DocumentsContract.getDocumentId(r0)
            android.net.Uri r0 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            java.lang.String[] r2 = new java.lang.String[r4]
            java.lang.String r5 = ":"
            java.lang.String[] r12 = r12.split(r5)
            r12 = r12[r4]
            r2[r3] = r12
            java.lang.String r12 = "_id=?"
            r8 = r12
            r6 = r0
            r9 = r2
            goto L_0x0049
        L_0x0046:
            r6 = r0
            r8 = r1
            r9 = r8
        L_0x0049:
            android.content.ContentResolver r5 = r11.mContentResolver
            java.lang.String[] r7 = new java.lang.String[r4]
            java.lang.String r12 = "_data"
            r7[r3] = r12
            r10 = 0
            android.database.Cursor r0 = r5.query(r6, r7, r8, r9, r10)
            if (r0 == 0) goto L_0x0073
            boolean r2 = r0.moveToFirst()     // Catch:{ all -> 0x006c }
            if (r2 == 0) goto L_0x0073
            int r12 = r0.getColumnIndexOrThrow(r12)     // Catch:{ all -> 0x006c }
            java.lang.String r12 = r0.getString(r12)     // Catch:{ all -> 0x006c }
            if (r0 == 0) goto L_0x006b
            r0.close()
        L_0x006b:
            return r12
        L_0x006c:
            r12 = move-exception
            if (r0 == 0) goto L_0x0072
            r0.close()
        L_0x0072:
            throw r12
        L_0x0073:
            if (r0 == 0) goto L_0x0078
            r0.close()
        L_0x0078:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.LocalVideoThumbnailProducer.getLocalFilePath(com.facebook.imagepipeline.request.ImageRequest):java.lang.String");
    }

    /* access modifiers changed from: private */
    @Nullable
    public static Bitmap createThumbnailFromContentProvider(ContentResolver contentResolver, Uri uri) {
        if (VERSION.SDK_INT >= 10) {
            try {
                ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(openFileDescriptor.getFileDescriptor());
                return mediaMetadataRetriever.getFrameAtTime(-1);
            } catch (FileNotFoundException unused) {
            }
        }
        return null;
    }
}
