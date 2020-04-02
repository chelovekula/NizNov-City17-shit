package com.RNFetchBlob.Response;

import androidx.annotation.NonNull;
import com.RNFetchBlob.RNFetchBlobConst;
import com.RNFetchBlob.RNFetchBlobProgressConfig;
import com.RNFetchBlob.RNFetchBlobReq;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;

public class RNFetchBlobFileResp extends ResponseBody {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    long bytesDownloaded = 0;
    boolean isEndMarkerReceived;
    String mPath;
    String mTaskId;
    FileOutputStream ofStream;
    ResponseBody originalBody;
    ReactApplicationContext rctContext;

    private class ProgressReportingSource implements Source {
        public Timeout timeout() {
            return null;
        }

        private ProgressReportingSource() {
        }

        public long read(@NonNull Buffer buffer, long j) throws IOException {
            int i = (int) j;
            try {
                byte[] bArr = new byte[i];
                long read = (long) RNFetchBlobFileResp.this.originalBody.byteStream().read(bArr, 0, i);
                RNFetchBlobFileResp rNFetchBlobFileResp = RNFetchBlobFileResp.this;
                rNFetchBlobFileResp.bytesDownloaded += read > 0 ? read : 0;
                if (read > 0) {
                    RNFetchBlobFileResp.this.ofStream.write(bArr, 0, (int) read);
                } else if (RNFetchBlobFileResp.this.contentLength() == -1 && read == -1) {
                    RNFetchBlobFileResp.this.isEndMarkerReceived = true;
                }
                RNFetchBlobProgressConfig reportProgress = RNFetchBlobReq.getReportProgress(RNFetchBlobFileResp.this.mTaskId);
                if (RNFetchBlobFileResp.this.contentLength() != 0) {
                    float contentLength = RNFetchBlobFileResp.this.contentLength() != -1 ? (float) (RNFetchBlobFileResp.this.bytesDownloaded / RNFetchBlobFileResp.this.contentLength()) : RNFetchBlobFileResp.this.isEndMarkerReceived ? 1.0f : 0.0f;
                    if (reportProgress != null && reportProgress.shouldReport(contentLength)) {
                        if (RNFetchBlobFileResp.this.contentLength() != -1) {
                            reportProgress(RNFetchBlobFileResp.this.mTaskId, RNFetchBlobFileResp.this.bytesDownloaded, RNFetchBlobFileResp.this.contentLength());
                        } else if (!RNFetchBlobFileResp.this.isEndMarkerReceived) {
                            reportProgress(RNFetchBlobFileResp.this.mTaskId, 0, RNFetchBlobFileResp.this.contentLength());
                        } else {
                            reportProgress(RNFetchBlobFileResp.this.mTaskId, RNFetchBlobFileResp.this.bytesDownloaded, RNFetchBlobFileResp.this.bytesDownloaded);
                        }
                    }
                }
                return read;
            } catch (Exception unused) {
                return -1;
            }
        }

        private void reportProgress(String str, long j, long j2) {
            WritableMap createMap = Arguments.createMap();
            createMap.putString("taskId", str);
            createMap.putString("written", String.valueOf(j));
            createMap.putString("total", String.valueOf(j2));
            ((RCTDeviceEventEmitter) RNFetchBlobFileResp.this.rctContext.getJSModule(RCTDeviceEventEmitter.class)).emit(RNFetchBlobConst.EVENT_PROGRESS, createMap);
        }

        public void close() throws IOException {
            RNFetchBlobFileResp.this.ofStream.close();
        }
    }

    public RNFetchBlobFileResp(ReactApplicationContext reactApplicationContext, String str, ResponseBody responseBody, String str2, boolean z) throws IOException {
        this.rctContext = reactApplicationContext;
        this.mTaskId = str;
        this.originalBody = responseBody;
        this.mPath = str2;
        this.isEndMarkerReceived = false;
        if (str2 != null) {
            boolean z2 = !z;
            String replace = str2.replace("?append=true", "");
            this.mPath = replace;
            File file = new File(replace);
            File parentFile = file.getParentFile();
            if (parentFile == null || parentFile.exists() || parentFile.mkdirs()) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                this.ofStream = new FileOutputStream(new File(replace), z2);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Couldn't create dir: ");
            sb.append(parentFile);
            throw new IllegalStateException(sb.toString());
        }
    }

    public MediaType contentType() {
        return this.originalBody.contentType();
    }

    public long contentLength() {
        return this.originalBody.contentLength();
    }

    public boolean isDownloadComplete() {
        return this.bytesDownloaded == contentLength() || (contentLength() == -1 && this.isEndMarkerReceived);
    }

    public BufferedSource source() {
        return Okio.buffer((Source) new ProgressReportingSource());
    }
}
