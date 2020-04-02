package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.UnpackingSoSource.Dso;
import com.facebook.soloader.UnpackingSoSource.DsoManifest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;

public class ExtractFromZipSoSource extends UnpackingSoSource {
    protected final File mZipFileName;
    protected final String mZipSearchPattern;

    private static final class ZipDso extends Dso implements Comparable {
        final int abiScore;
        final ZipEntry backingEntry;

        ZipDso(String str, ZipEntry zipEntry, int i) {
            super(str, makePseudoHash(zipEntry));
            this.backingEntry = zipEntry;
            this.abiScore = i;
        }

        private static String makePseudoHash(ZipEntry zipEntry) {
            return String.format("pseudo-zip-hash-1-%s-%s-%s-%s", new Object[]{zipEntry.getName(), Long.valueOf(zipEntry.getSize()), Long.valueOf(zipEntry.getCompressedSize()), Long.valueOf(zipEntry.getCrc())});
        }

        public int compareTo(Object obj) {
            return this.name.compareTo(((ZipDso) obj).name);
        }
    }

    protected class ZipUnpacker extends Unpacker {
        /* access modifiers changed from: private */
        @Nullable
        public ZipDso[] mDsos;
        private final UnpackingSoSource mSoSource;
        /* access modifiers changed from: private */
        public final ZipFile mZipFile;

        private final class ZipBackedInputDsoIterator extends InputDsoIterator {
            private int mCurrentDso;

            private ZipBackedInputDsoIterator() {
            }

            public boolean hasNext() {
                ZipUnpacker.this.ensureDsos();
                return this.mCurrentDso < ZipUnpacker.this.mDsos.length;
            }

            public InputDso next() throws IOException {
                ZipUnpacker.this.ensureDsos();
                ZipDso[] access$100 = ZipUnpacker.this.mDsos;
                int i = this.mCurrentDso;
                this.mCurrentDso = i + 1;
                ZipDso zipDso = access$100[i];
                InputStream inputStream = ZipUnpacker.this.mZipFile.getInputStream(zipDso.backingEntry);
                try {
                    return new InputDso(zipDso, inputStream);
                } catch (Throwable th) {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            }
        }

        /* access modifiers changed from: protected */
        public boolean shouldExtract(ZipEntry zipEntry, String str) {
            return true;
        }

        ZipUnpacker(UnpackingSoSource unpackingSoSource) throws IOException {
            this.mZipFile = new ZipFile(ExtractFromZipSoSource.this.mZipFileName);
            this.mSoSource = unpackingSoSource;
        }

        /* access modifiers changed from: 0000 */
        public final ZipDso[] ensureDsos() {
            if (this.mDsos == null) {
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                HashMap hashMap = new HashMap();
                Pattern compile = Pattern.compile(ExtractFromZipSoSource.this.mZipSearchPattern);
                String[] supportedAbis = SysUtil.getSupportedAbis();
                Enumeration entries = this.mZipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    Matcher matcher = compile.matcher(zipEntry.getName());
                    if (matcher.matches()) {
                        String group = matcher.group(1);
                        String group2 = matcher.group(2);
                        int findAbiScore = SysUtil.findAbiScore(supportedAbis, group);
                        if (findAbiScore >= 0) {
                            linkedHashSet.add(group);
                            ZipDso zipDso = (ZipDso) hashMap.get(group2);
                            if (zipDso == null || findAbiScore < zipDso.abiScore) {
                                hashMap.put(group2, new ZipDso(group2, zipEntry, findAbiScore));
                            }
                        }
                    }
                }
                this.mSoSource.setSoSourceAbis((String[]) linkedHashSet.toArray(new String[linkedHashSet.size()]));
                ZipDso[] zipDsoArr = (ZipDso[]) hashMap.values().toArray(new ZipDso[hashMap.size()]);
                Arrays.sort(zipDsoArr);
                int i = 0;
                for (int i2 = 0; i2 < zipDsoArr.length; i2++) {
                    ZipDso zipDso2 = zipDsoArr[i2];
                    if (shouldExtract(zipDso2.backingEntry, zipDso2.name)) {
                        i++;
                    } else {
                        zipDsoArr[i2] = null;
                    }
                }
                ZipDso[] zipDsoArr2 = new ZipDso[i];
                int i3 = 0;
                for (ZipDso zipDso3 : zipDsoArr) {
                    if (zipDso3 != null) {
                        int i4 = i3 + 1;
                        zipDsoArr2[i3] = zipDso3;
                        i3 = i4;
                    }
                }
                this.mDsos = zipDsoArr2;
            }
            return this.mDsos;
        }

        public void close() throws IOException {
            this.mZipFile.close();
        }

        /* access modifiers changed from: protected */
        public final DsoManifest getDsoManifest() throws IOException {
            return new DsoManifest(ensureDsos());
        }

        /* access modifiers changed from: protected */
        public final InputDsoIterator openDsoIterator() throws IOException {
            return new ZipBackedInputDsoIterator();
        }
    }

    public ExtractFromZipSoSource(Context context, String str, File file, String str2) {
        super(context, str);
        this.mZipFileName = file;
        this.mZipSearchPattern = str2;
    }

    /* access modifiers changed from: protected */
    public Unpacker makeUnpacker() throws IOException {
        return new ZipUnpacker(this);
    }
}
