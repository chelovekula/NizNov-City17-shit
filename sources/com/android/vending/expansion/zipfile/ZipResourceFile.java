package com.android.vending.expansion.zipfile;

import android.content.res.AssetFileDescriptor;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import kotlin.UShort;

public class ZipResourceFile {
    static final boolean LOGV = false;
    static final String LOG_TAG = "zipro";
    static final int kCDECRC = 16;
    static final int kCDECommentLen = 32;
    static final int kCDECompLen = 20;
    static final int kCDEExtraLen = 30;
    static final int kCDELen = 46;
    static final int kCDELocalOffset = 42;
    static final int kCDEMethod = 10;
    static final int kCDEModWhen = 12;
    static final int kCDENameLen = 28;
    static final int kCDESignature = 33639248;
    static final int kCDEUncompLen = 24;
    static final int kCompressDeflated = 8;
    static final int kCompressStored = 0;
    static final int kEOCDFileOffset = 16;
    static final int kEOCDLen = 22;
    static final int kEOCDNumEntries = 8;
    static final int kEOCDSignature = 101010256;
    static final int kEOCDSize = 12;
    static final int kLFHExtraLen = 28;
    static final int kLFHLen = 30;
    static final int kLFHNameLen = 26;
    static final int kLFHSignature = 67324752;
    static final int kMaxCommentLen = 65535;
    static final int kMaxEOCDSearch = 65557;
    static final int kZipEntryAdj = 10000;
    private HashMap<String, ZipEntryRO> mHashMap = new HashMap<>();
    ByteBuffer mLEByteBuffer = ByteBuffer.allocate(4);
    public HashMap<File, ZipFile> mZipFiles = new HashMap<>();

    public static final class ZipEntryRO {
        public long mCRC32;
        public long mCompressedLength;
        public final File mFile;
        public final String mFileName;
        public long mLocalHdrOffset;
        public int mMethod;
        public long mOffset = -1;
        public long mUncompressedLength;
        public long mWhenModified;
        public final String mZipFileName;

        public ZipEntryRO(String str, File file, String str2) {
            this.mFileName = str2;
            this.mZipFileName = str;
            this.mFile = file;
        }

        public void setOffsetFromFile(RandomAccessFile randomAccessFile, ByteBuffer byteBuffer) throws IOException {
            long j = this.mLocalHdrOffset;
            try {
                randomAccessFile.seek(j);
                randomAccessFile.readFully(byteBuffer.array());
                if (byteBuffer.getInt(0) == ZipResourceFile.kLFHSignature) {
                    this.mOffset = j + 30 + ((long) (byteBuffer.getShort(26) & UShort.MAX_VALUE)) + ((long) (byteBuffer.getShort(28) & UShort.MAX_VALUE));
                    return;
                }
                Log.w(ZipResourceFile.LOG_TAG, "didn't find signature at start of lfh");
                throw new IOException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        public long getOffset() {
            return this.mOffset;
        }

        public boolean isUncompressed() {
            return this.mMethod == 0;
        }

        public AssetFileDescriptor getAssetFileDescriptor() {
            if (this.mMethod == 0) {
                try {
                    AssetFileDescriptor assetFileDescriptor = new AssetFileDescriptor(ParcelFileDescriptor.open(this.mFile, 268435456), getOffset(), this.mUncompressedLength);
                    return assetFileDescriptor;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String getZipFileName() {
            return this.mZipFileName;
        }

        public File getZipFile() {
            return this.mFile;
        }
    }

    private static int swapEndian(int i) {
        return ((i & 255) << 24) + ((65280 & i) << 8) + ((16711680 & i) >>> 8) + ((i >>> 24) & 255);
    }

    private static int swapEndian(short s) {
        return ((s & 65280) >>> 8) | ((s & 255) << 8);
    }

    public ZipResourceFile(String str) throws IOException {
        addPatchFile(str);
    }

    /* access modifiers changed from: 0000 */
    public ZipEntryRO[] getEntriesAt(String str) {
        Vector vector = new Vector();
        Collection<ZipEntryRO> values = this.mHashMap.values();
        if (str == null) {
            str = "";
        }
        int length = str.length();
        for (ZipEntryRO zipEntryRO : values) {
            if (zipEntryRO.mFileName.startsWith(str) && -1 == zipEntryRO.mFileName.indexOf(47, length)) {
                vector.add(zipEntryRO);
            }
        }
        return (ZipEntryRO[]) vector.toArray(new ZipEntryRO[vector.size()]);
    }

    public ZipEntryRO[] getAllEntries() {
        Collection values = this.mHashMap.values();
        return (ZipEntryRO[]) values.toArray(new ZipEntryRO[values.size()]);
    }

    public AssetFileDescriptor getAssetFileDescriptor(String str) {
        ZipEntryRO zipEntryRO = (ZipEntryRO) this.mHashMap.get(str);
        if (zipEntryRO != null) {
            return zipEntryRO.getAssetFileDescriptor();
        }
        return null;
    }

    public InputStream getInputStream(String str) throws IOException {
        ZipEntryRO zipEntryRO = (ZipEntryRO) this.mHashMap.get(str);
        if (zipEntryRO != null) {
            if (zipEntryRO.isUncompressed()) {
                return zipEntryRO.getAssetFileDescriptor().createInputStream();
            }
            ZipFile zipFile = (ZipFile) this.mZipFiles.get(zipEntryRO.getZipFile());
            if (zipFile == null) {
                zipFile = new ZipFile(zipEntryRO.getZipFile(), 1);
                this.mZipFiles.put(zipEntryRO.getZipFile(), zipFile);
            }
            ZipEntry entry = zipFile.getEntry(str);
            if (entry != null) {
                return zipFile.getInputStream(entry);
            }
        }
        return null;
    }

    private static int read4LE(RandomAccessFile randomAccessFile) throws EOFException, IOException {
        return swapEndian(randomAccessFile.readInt());
    }

    /* access modifiers changed from: 0000 */
    public void addPatchFile(String str) throws IOException {
        String str2 = str;
        File file = new File(str2);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long length = randomAccessFile.length();
        if (length >= 22) {
            long j = 65557;
            if (65557 > length) {
                j = length;
            }
            randomAccessFile.seek(0);
            int read4LE = read4LE(randomAccessFile);
            String str3 = LOG_TAG;
            if (read4LE == kEOCDSignature) {
                Log.i(str3, "Found Zip archive, but it looks empty");
                throw new IOException();
            } else if (read4LE == kLFHSignature) {
                randomAccessFile.seek(length - j);
                ByteBuffer allocate = ByteBuffer.allocate((int) j);
                byte[] array = allocate.array();
                randomAccessFile.readFully(array);
                allocate.order(ByteOrder.LITTLE_ENDIAN);
                int length2 = array.length - 22;
                while (length2 >= 0 && (array[length2] != 80 || allocate.getInt(length2) != kEOCDSignature)) {
                    length2--;
                }
                if (length2 < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Zip: EOCD not found, ");
                    sb.append(str2);
                    sb.append(" is not zip");
                    Log.d(str3, sb.toString());
                }
                short s = allocate.getShort(length2 + 8);
                long j2 = ((long) allocate.getInt(length2 + 12)) & 4294967295L;
                long j3 = ((long) allocate.getInt(length2 + 16)) & 4294967295L;
                String str4 = ")";
                if (j3 + j2 > length) {
                    long j4 = j3;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("bad offsets (dir ");
                    sb2.append(j4);
                    sb2.append(", size ");
                    sb2.append(j2);
                    sb2.append(", eocd ");
                    sb2.append(length2);
                    sb2.append(str4);
                    Log.w(str3, sb2.toString());
                    throw new IOException();
                } else if (s != 0) {
                    MappedByteBuffer map = randomAccessFile.getChannel().map(MapMode.READ_ONLY, j3, j2);
                    map.order(ByteOrder.LITTLE_ENDIAN);
                    short s2 = 65535;
                    byte[] bArr = new byte[65535];
                    ByteBuffer allocate2 = ByteBuffer.allocate(30);
                    allocate2.order(ByteOrder.LITTLE_ENDIAN);
                    int i = 0;
                    int i2 = 0;
                    int i3 = 0;
                    while (i2 < s) {
                        if (map.getInt(i3) == kCDESignature) {
                            short s3 = map.getShort(i3 + 28) & s2;
                            short s4 = map.getShort(i3 + 30) & s2;
                            short s5 = map.getShort(i3 + 32) & s2;
                            map.position(i3 + 46);
                            map.get(bArr, i, s3);
                            map.position(i);
                            String str5 = new String(bArr, i, s3);
                            ZipEntryRO zipEntryRO = new ZipEntryRO(str2, file, str5);
                            zipEntryRO.mMethod = map.getShort(i3 + 10) & UShort.MAX_VALUE;
                            File file2 = file;
                            zipEntryRO.mWhenModified = ((long) map.getInt(i3 + 12)) & 4294967295L;
                            zipEntryRO.mCRC32 = map.getLong(i3 + 16) & 4294967295L;
                            zipEntryRO.mCompressedLength = map.getLong(i3 + 20) & 4294967295L;
                            zipEntryRO.mUncompressedLength = map.getLong(i3 + 24) & 4294967295L;
                            zipEntryRO.mLocalHdrOffset = ((long) map.getInt(i3 + 42)) & 4294967295L;
                            allocate2.clear();
                            zipEntryRO.setOffsetFromFile(randomAccessFile, allocate2);
                            this.mHashMap.put(str5, zipEntryRO);
                            i3 += s3 + 46 + s4 + s5;
                            i2++;
                            str2 = str;
                            file = file2;
                            s2 = UShort.MAX_VALUE;
                            i = 0;
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Missed a central dir sig (at ");
                            sb3.append(i3);
                            sb3.append(str4);
                            Log.w(str3, sb3.toString());
                            throw new IOException();
                        }
                    }
                } else {
                    Log.w(str3, "empty archive?");
                    throw new IOException();
                }
            } else {
                Log.v(str3, "Not a Zip archive");
                throw new IOException();
            }
        } else {
            throw new IOException();
        }
    }
}
