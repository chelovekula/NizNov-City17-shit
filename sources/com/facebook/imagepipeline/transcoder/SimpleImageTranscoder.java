package com.facebook.imagepipeline.transcoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import com.facebook.common.logging.FLog;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.OutputStream;
import javax.annotation.Nullable;

public class SimpleImageTranscoder implements ImageTranscoder {
    private static final String TAG = "SimpleImageTranscoder";
    private final int mMaxBitmapSize;
    private final boolean mResizingEnabled;

    public String getIdentifier() {
        return TAG;
    }

    public SimpleImageTranscoder(boolean z, int i) {
        this.mResizingEnabled = z;
        this.mMaxBitmapSize = i;
    }

    public ImageTranscodeResult transcode(EncodedImage encodedImage, OutputStream outputStream, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, @Nullable ImageFormat imageFormat, @Nullable Integer num) {
        RotationOptions rotationOptions2;
        ResizeOptions resizeOptions2;
        SimpleImageTranscoder simpleImageTranscoder;
        Bitmap bitmap;
        EncodedImage encodedImage2 = encodedImage;
        String str = "Out-Of-Memory during transcode";
        String str2 = TAG;
        Integer valueOf = num == null ? Integer.valueOf(85) : num;
        if (rotationOptions == null) {
            resizeOptions2 = resizeOptions;
            rotationOptions2 = RotationOptions.autoRotate();
            simpleImageTranscoder = this;
        } else {
            simpleImageTranscoder = this;
            rotationOptions2 = rotationOptions;
            resizeOptions2 = resizeOptions;
        }
        int sampleSize = simpleImageTranscoder.getSampleSize(encodedImage2, rotationOptions2, resizeOptions2);
        Options options = new Options();
        options.inSampleSize = sampleSize;
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(encodedImage.getInputStream(), null, options);
            if (decodeStream == null) {
                FLog.m50e(str2, "Couldn't decode the EncodedImage InputStream ! ");
                return new ImageTranscodeResult(2);
            }
            Matrix transformationMatrix = JpegTranscoderUtils.getTransformationMatrix(encodedImage2, rotationOptions2);
            if (transformationMatrix != null) {
                try {
                    bitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), transformationMatrix, false);
                } catch (OutOfMemoryError e) {
                    e = e;
                    bitmap = decodeStream;
                    try {
                        FLog.m51e(str2, str, (Throwable) e);
                        ImageTranscodeResult imageTranscodeResult = new ImageTranscodeResult(2);
                        bitmap.recycle();
                        decodeStream.recycle();
                        return imageTranscodeResult;
                    } catch (Throwable th) {
                        th = th;
                        bitmap.recycle();
                        decodeStream.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bitmap = decodeStream;
                    bitmap.recycle();
                    decodeStream.recycle();
                    throw th;
                }
            } else {
                bitmap = decodeStream;
            }
            try {
                bitmap.compress(getOutputFormat(imageFormat), valueOf.intValue(), outputStream);
                int i = 1;
                if (sampleSize > 1) {
                    i = 0;
                }
                ImageTranscodeResult imageTranscodeResult2 = new ImageTranscodeResult(i);
                bitmap.recycle();
                decodeStream.recycle();
                return imageTranscodeResult2;
            } catch (OutOfMemoryError e2) {
                e = e2;
                FLog.m51e(str2, str, (Throwable) e);
                ImageTranscodeResult imageTranscodeResult3 = new ImageTranscodeResult(2);
                bitmap.recycle();
                decodeStream.recycle();
                return imageTranscodeResult3;
            }
        } catch (OutOfMemoryError e3) {
            FLog.m51e(str2, str, (Throwable) e3);
            return new ImageTranscodeResult(2);
        }
    }

    public boolean canResize(EncodedImage encodedImage, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions) {
        if (rotationOptions == null) {
            rotationOptions = RotationOptions.autoRotate();
        }
        if (!this.mResizingEnabled || DownsampleUtil.determineSampleSize(rotationOptions, resizeOptions, encodedImage, this.mMaxBitmapSize) <= 1) {
            return false;
        }
        return true;
    }

    public boolean canTranscode(ImageFormat imageFormat) {
        return imageFormat == DefaultImageFormats.HEIF || imageFormat == DefaultImageFormats.JPEG;
    }

    private int getSampleSize(EncodedImage encodedImage, RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions) {
        if (!this.mResizingEnabled) {
            return 1;
        }
        return DownsampleUtil.determineSampleSize(rotationOptions, resizeOptions, encodedImage, this.mMaxBitmapSize);
    }

    private static CompressFormat getOutputFormat(@Nullable ImageFormat imageFormat) {
        if (imageFormat == null) {
            return CompressFormat.JPEG;
        }
        if (imageFormat == DefaultImageFormats.JPEG) {
            return CompressFormat.JPEG;
        }
        if (imageFormat == DefaultImageFormats.PNG) {
            return CompressFormat.PNG;
        }
        if (VERSION.SDK_INT < 14 || !DefaultImageFormats.isStaticWebpFormat(imageFormat)) {
            return CompressFormat.JPEG;
        }
        return CompressFormat.WEBP;
    }
}
