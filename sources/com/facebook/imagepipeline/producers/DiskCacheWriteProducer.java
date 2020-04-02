package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequest.CacheChoice;

public class DiskCacheWriteProducer implements Producer<EncodedImage> {
    @VisibleForTesting
    static final String PRODUCER_NAME = "DiskCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final Producer<EncodedImage> mInputProducer;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;

    private static class DiskCacheWriteConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final CacheKeyFactory mCacheKeyFactory;
        private final BufferedDiskCache mDefaultBufferedDiskCache;
        private final ProducerContext mProducerContext;
        private final BufferedDiskCache mSmallImageBufferedDiskCache;

        private DiskCacheWriteConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext, BufferedDiskCache bufferedDiskCache, BufferedDiskCache bufferedDiskCache2, CacheKeyFactory cacheKeyFactory) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mDefaultBufferedDiskCache = bufferedDiskCache;
            this.mSmallImageBufferedDiskCache = bufferedDiskCache2;
            this.mCacheKeyFactory = cacheKeyFactory;
        }

        public void onNewResultImpl(EncodedImage encodedImage, int i) {
            if (isNotLast(i) || encodedImage == null || statusHasAnyFlag(i, 10) || encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
                getConsumer().onNewResult(encodedImage, i);
                return;
            }
            ImageRequest imageRequest = this.mProducerContext.getImageRequest();
            CacheKey encodedCacheKey = this.mCacheKeyFactory.getEncodedCacheKey(imageRequest, this.mProducerContext.getCallerContext());
            if (imageRequest.getCacheChoice() == CacheChoice.SMALL) {
                this.mSmallImageBufferedDiskCache.put(encodedCacheKey, encodedImage);
            } else {
                this.mDefaultBufferedDiskCache.put(encodedCacheKey, encodedImage);
            }
            getConsumer().onNewResult(encodedImage, i);
        }
    }

    public DiskCacheWriteProducer(BufferedDiskCache bufferedDiskCache, BufferedDiskCache bufferedDiskCache2, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> producer) {
        this.mDefaultBufferedDiskCache = bufferedDiskCache;
        this.mSmallImageBufferedDiskCache = bufferedDiskCache2;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = producer;
    }

    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        maybeStartInputProducer(consumer, producerContext);
    }

    /* JADX WARNING: type inference failed for: r9v1, types: [com.facebook.imagepipeline.producers.Consumer] */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void maybeStartInputProducer(com.facebook.imagepipeline.producers.Consumer<com.facebook.imagepipeline.image.EncodedImage> r9, com.facebook.imagepipeline.producers.ProducerContext r10) {
        /*
            r8 = this;
            com.facebook.imagepipeline.request.ImageRequest$RequestLevel r0 = r10.getLowestPermittedRequestLevel()
            int r0 = r0.getValue()
            com.facebook.imagepipeline.request.ImageRequest$RequestLevel r1 = com.facebook.imagepipeline.request.ImageRequest.RequestLevel.DISK_CACHE
            int r1 = r1.getValue()
            if (r0 < r1) goto L_0x0016
            r10 = 0
            r0 = 1
            r9.onNewResult(r10, r0)
            goto L_0x0035
        L_0x0016:
            com.facebook.imagepipeline.request.ImageRequest r0 = r10.getImageRequest()
            boolean r0 = r0.isDiskCacheEnabled()
            if (r0 == 0) goto L_0x0030
            com.facebook.imagepipeline.producers.DiskCacheWriteProducer$DiskCacheWriteConsumer r0 = new com.facebook.imagepipeline.producers.DiskCacheWriteProducer$DiskCacheWriteConsumer
            com.facebook.imagepipeline.cache.BufferedDiskCache r4 = r8.mDefaultBufferedDiskCache
            com.facebook.imagepipeline.cache.BufferedDiskCache r5 = r8.mSmallImageBufferedDiskCache
            com.facebook.imagepipeline.cache.CacheKeyFactory r6 = r8.mCacheKeyFactory
            r7 = 0
            r1 = r0
            r2 = r9
            r3 = r10
            r1.<init>(r2, r3, r4, r5, r6)
            r9 = r0
        L_0x0030:
            com.facebook.imagepipeline.producers.Producer<com.facebook.imagepipeline.image.EncodedImage> r0 = r8.mInputProducer
            r0.produceResults(r9, r10)
        L_0x0035:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.DiskCacheWriteProducer.maybeStartInputProducer(com.facebook.imagepipeline.producers.Consumer, com.facebook.imagepipeline.producers.ProducerContext):void");
    }
}
