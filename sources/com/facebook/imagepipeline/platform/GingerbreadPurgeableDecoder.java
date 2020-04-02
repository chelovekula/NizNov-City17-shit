package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.MemoryFile;
import com.facebook.common.internal.ByteStreams;
import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.webp.WebpBitmapFactory;
import com.facebook.common.webp.WebpSupportStatus;
import com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

public class GingerbreadPurgeableDecoder extends DalvikPurgeableDecoder {
    private static Method sGetFileDescriptorMethod;
    @Nullable
    private final WebpBitmapFactory mWebpBitmapFactory = WebpSupportStatus.loadWebpBitmapFactoryIfExists();

    /* access modifiers changed from: protected */
    public Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, Options options) {
        return decodeFileDescriptorAsPurgeable(closeableReference, ((PooledByteBuffer) closeableReference.get()).size(), null, options);
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, int i, Options options) {
        return decodeFileDescriptorAsPurgeable(closeableReference, i, endsWithEOI(closeableReference, i) ? null : EOI, options);
    }

    private static MemoryFile copyToMemoryFile(CloseableReference<PooledByteBuffer> closeableReference, int i, @Nullable byte[] bArr) throws IOException {
        Closeable closeable;
        InputStream inputStream;
        InputStream inputStream2 = null;
        MemoryFile memoryFile = new MemoryFile(null, (bArr == null ? 0 : bArr.length) + i);
        memoryFile.allowPurging(false);
        try {
            inputStream = new PooledByteBufferInputStream((PooledByteBuffer) closeableReference.get());
            try {
                LimitedInputStream limitedInputStream = new LimitedInputStream(inputStream, i);
                try {
                    OutputStream outputStream = memoryFile.getOutputStream();
                    ByteStreams.copy(limitedInputStream, outputStream);
                    if (bArr != null) {
                        memoryFile.writeBytes(bArr, 0, i, bArr.length);
                    }
                    CloseableReference.closeSafely(closeableReference);
                    Closeables.closeQuietly(inputStream);
                    Closeables.closeQuietly((InputStream) limitedInputStream);
                    Closeables.close(outputStream, true);
                    return memoryFile;
                } catch (Throwable th) {
                    th = th;
                    closeable = null;
                    inputStream2 = limitedInputStream;
                    CloseableReference.closeSafely(closeableReference);
                    Closeables.closeQuietly(inputStream);
                    Closeables.closeQuietly(inputStream2);
                    Closeables.close(closeable, true);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                closeable = null;
                CloseableReference.closeSafely(closeableReference);
                Closeables.closeQuietly(inputStream);
                Closeables.closeQuietly(inputStream2);
                Closeables.close(closeable, true);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            closeable = null;
            inputStream = null;
            CloseableReference.closeSafely(closeableReference);
            Closeables.closeQuietly(inputStream);
            Closeables.closeQuietly(inputStream2);
            Closeables.close(closeable, true);
            throw th;
        }
    }

    private synchronized Method getFileDescriptorMethod() {
        if (sGetFileDescriptorMethod == null) {
            try {
                sGetFileDescriptorMethod = MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
        return sGetFileDescriptorMethod;
    }

    private FileDescriptor getMemoryFileDescriptor(MemoryFile memoryFile) {
        try {
            return (FileDescriptor) getFileDescriptorMethod().invoke(memoryFile, new Object[0]);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0039  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap decodeFileDescriptorAsPurgeable(com.facebook.common.references.CloseableReference<com.facebook.common.memory.PooledByteBuffer> r2, int r3, byte[] r4, android.graphics.BitmapFactory.Options r5) {
        /*
            r1 = this;
            r0 = 0
            android.os.MemoryFile r2 = copyToMemoryFile(r2, r3, r4)     // Catch:{ IOException -> 0x0031 }
            java.io.FileDescriptor r3 = r1.getMemoryFileDescriptor(r2)     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            com.facebook.common.webp.WebpBitmapFactory r4 = r1.mWebpBitmapFactory     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            if (r4 == 0) goto L_0x0021
            com.facebook.common.webp.WebpBitmapFactory r4 = r1.mWebpBitmapFactory     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            android.graphics.Bitmap r3 = r4.decodeFileDescriptor(r3, r0, r5)     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            java.lang.String r4 = "BitmapFactory returned null"
            java.lang.Object r3 = com.facebook.common.internal.Preconditions.checkNotNull(r3, r4)     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            android.graphics.Bitmap r3 = (android.graphics.Bitmap) r3     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            if (r2 == 0) goto L_0x0020
            r2.close()
        L_0x0020:
            return r3
        L_0x0021:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            java.lang.String r4 = "WebpBitmapFactory is null"
            r3.<init>(r4)     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
            throw r3     // Catch:{ IOException -> 0x002b, all -> 0x0029 }
        L_0x0029:
            r3 = move-exception
            goto L_0x0037
        L_0x002b:
            r3 = move-exception
            r0 = r2
            goto L_0x0032
        L_0x002e:
            r3 = move-exception
            r2 = r0
            goto L_0x0037
        L_0x0031:
            r3 = move-exception
        L_0x0032:
            java.lang.RuntimeException r2 = com.facebook.common.internal.Throwables.propagate(r3)     // Catch:{ all -> 0x002e }
            throw r2     // Catch:{ all -> 0x002e }
        L_0x0037:
            if (r2 == 0) goto L_0x003c
            r2.close()
        L_0x003c:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.platform.GingerbreadPurgeableDecoder.decodeFileDescriptorAsPurgeable(com.facebook.common.references.CloseableReference, int, byte[], android.graphics.BitmapFactory$Options):android.graphics.Bitmap");
    }
}
