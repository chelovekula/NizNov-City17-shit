package com.facebook.common.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.ResourceReleaser;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.concurrent.NotThreadSafe;
import kotlin.UByte;

@NotThreadSafe
public class PooledByteArrayBufferedInputStream extends InputStream {
    private static final String TAG = "PooledByteInputStream";
    private int mBufferOffset = 0;
    private int mBufferedSize = 0;
    private final byte[] mByteArray;
    private boolean mClosed = false;
    private final InputStream mInputStream;
    private final ResourceReleaser<byte[]> mResourceReleaser;

    public PooledByteArrayBufferedInputStream(InputStream inputStream, byte[] bArr, ResourceReleaser<byte[]> resourceReleaser) {
        this.mInputStream = (InputStream) Preconditions.checkNotNull(inputStream);
        this.mByteArray = (byte[]) Preconditions.checkNotNull(bArr);
        this.mResourceReleaser = (ResourceReleaser) Preconditions.checkNotNull(resourceReleaser);
    }

    public int read() throws IOException {
        Preconditions.checkState(this.mBufferOffset <= this.mBufferedSize);
        ensureNotClosed();
        if (!ensureDataInBuffer()) {
            return -1;
        }
        byte[] bArr = this.mByteArray;
        int i = this.mBufferOffset;
        this.mBufferOffset = i + 1;
        return bArr[i] & UByte.MAX_VALUE;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        Preconditions.checkState(this.mBufferOffset <= this.mBufferedSize);
        ensureNotClosed();
        if (!ensureDataInBuffer()) {
            return -1;
        }
        int min = Math.min(this.mBufferedSize - this.mBufferOffset, i2);
        System.arraycopy(this.mByteArray, this.mBufferOffset, bArr, i, min);
        this.mBufferOffset += min;
        return min;
    }

    public int available() throws IOException {
        Preconditions.checkState(this.mBufferOffset <= this.mBufferedSize);
        ensureNotClosed();
        return (this.mBufferedSize - this.mBufferOffset) + this.mInputStream.available();
    }

    public void close() throws IOException {
        if (!this.mClosed) {
            this.mClosed = true;
            this.mResourceReleaser.release(this.mByteArray);
            super.close();
        }
    }

    public long skip(long j) throws IOException {
        Preconditions.checkState(this.mBufferOffset <= this.mBufferedSize);
        ensureNotClosed();
        int i = this.mBufferedSize;
        int i2 = this.mBufferOffset;
        long j2 = (long) (i - i2);
        if (j2 >= j) {
            this.mBufferOffset = (int) (((long) i2) + j);
            return j;
        }
        this.mBufferOffset = i;
        return j2 + this.mInputStream.skip(j - j2);
    }

    private boolean ensureDataInBuffer() throws IOException {
        if (this.mBufferOffset < this.mBufferedSize) {
            return true;
        }
        int read = this.mInputStream.read(this.mByteArray);
        if (read <= 0) {
            return false;
        }
        this.mBufferedSize = read;
        this.mBufferOffset = 0;
        return true;
    }

    private void ensureNotClosed() throws IOException {
        if (this.mClosed) {
            throw new IOException("stream already closed");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (!this.mClosed) {
            FLog.m50e(TAG, "Finalized without closing");
            close();
        }
        super.finalize();
    }
}
