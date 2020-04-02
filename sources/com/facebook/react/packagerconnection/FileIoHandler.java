package com.facebook.react.packagerconnection;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.common.util.UriUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class FileIoHandler implements Runnable {
    private static final long FILE_TTL = 30000;
    private static final String TAG = JSPackagerClient.class.getSimpleName();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mNextHandle = 1;
    /* access modifiers changed from: private */
    public final Map<Integer, TtlFileInputStream> mOpenFiles = new HashMap();
    private final Map<String, RequestHandler> mRequestHandlers = new HashMap();

    private static class TtlFileInputStream {
        private final FileInputStream mStream;
        private long mTtl = (System.currentTimeMillis() + FileIoHandler.FILE_TTL);

        public TtlFileInputStream(String str) throws FileNotFoundException {
            this.mStream = new FileInputStream(str);
        }

        private void extendTtl() {
            this.mTtl = System.currentTimeMillis() + FileIoHandler.FILE_TTL;
        }

        public boolean expiredTtl() {
            return System.currentTimeMillis() >= this.mTtl;
        }

        public String read(int i) throws IOException {
            extendTtl();
            byte[] bArr = new byte[i];
            return Base64.encodeToString(bArr, 0, this.mStream.read(bArr), 0);
        }

        public void close() throws IOException {
            this.mStream.close();
        }
    }

    public FileIoHandler() {
        this.mRequestHandlers.put("fopen", new RequestOnlyHandler() {
            public void onRequest(@Nullable Object obj, Responder responder) {
                synchronized (FileIoHandler.this.mOpenFiles) {
                    try {
                        JSONObject jSONObject = (JSONObject) obj;
                        if (jSONObject != null) {
                            String optString = jSONObject.optString("mode");
                            if (optString != null) {
                                String optString2 = jSONObject.optString("filename");
                                if (optString2 == null) {
                                    throw new Exception("missing params.filename");
                                } else if (optString.equals("r")) {
                                    responder.respond(Integer.valueOf(FileIoHandler.this.addOpenFile(optString2)));
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("unsupported mode: ");
                                    sb.append(optString);
                                    throw new IllegalArgumentException(sb.toString());
                                }
                            } else {
                                throw new Exception("missing params.mode");
                            }
                        } else {
                            throw new Exception("params must be an object { mode: string, filename: string }");
                        }
                    } catch (Exception e) {
                        responder.error(e.toString());
                    }
                }
            }
        });
        this.mRequestHandlers.put("fclose", new RequestOnlyHandler() {
            public void onRequest(@Nullable Object obj, Responder responder) {
                synchronized (FileIoHandler.this.mOpenFiles) {
                    try {
                        if (obj instanceof Number) {
                            TtlFileInputStream ttlFileInputStream = (TtlFileInputStream) FileIoHandler.this.mOpenFiles.get(Integer.valueOf(((Integer) obj).intValue()));
                            if (ttlFileInputStream != null) {
                                FileIoHandler.this.mOpenFiles.remove(Integer.valueOf(((Integer) obj).intValue()));
                                ttlFileInputStream.close();
                                responder.respond("");
                            } else {
                                throw new Exception("invalid file handle, it might have timed out");
                            }
                        } else {
                            throw new Exception("params must be a file handle");
                        }
                    } catch (Exception e) {
                        responder.error(e.toString());
                    }
                }
            }
        });
        this.mRequestHandlers.put("fread", new RequestOnlyHandler() {
            public void onRequest(@Nullable Object obj, Responder responder) {
                synchronized (FileIoHandler.this.mOpenFiles) {
                    try {
                        JSONObject jSONObject = (JSONObject) obj;
                        if (jSONObject != null) {
                            int optInt = jSONObject.optInt(UriUtil.LOCAL_FILE_SCHEME);
                            if (optInt != 0) {
                                int optInt2 = jSONObject.optInt("size");
                                if (optInt2 != 0) {
                                    TtlFileInputStream ttlFileInputStream = (TtlFileInputStream) FileIoHandler.this.mOpenFiles.get(Integer.valueOf(optInt));
                                    if (ttlFileInputStream != null) {
                                        responder.respond(ttlFileInputStream.read(optInt2));
                                    } else {
                                        throw new Exception("invalid file handle, it might have timed out");
                                    }
                                } else {
                                    throw new Exception("invalid or missing read size");
                                }
                            } else {
                                throw new Exception("invalid or missing file handle");
                            }
                        } else {
                            throw new Exception("params must be an object { file: handle, size: number }");
                        }
                    } catch (Exception e) {
                        responder.error(e.toString());
                    }
                }
            }
        });
    }

    public Map<String, RequestHandler> handlers() {
        return this.mRequestHandlers;
    }

    /* access modifiers changed from: private */
    public int addOpenFile(String str) throws FileNotFoundException {
        int i = this.mNextHandle;
        this.mNextHandle = i + 1;
        this.mOpenFiles.put(Integer.valueOf(i), new TtlFileInputStream(str));
        if (this.mOpenFiles.size() == 1) {
            this.mHandler.postDelayed(this, FILE_TTL);
        }
        return i;
    }

    public void run() {
        synchronized (this.mOpenFiles) {
            Iterator it = this.mOpenFiles.values().iterator();
            while (it.hasNext()) {
                TtlFileInputStream ttlFileInputStream = (TtlFileInputStream) it.next();
                if (ttlFileInputStream.expiredTtl()) {
                    it.remove();
                    try {
                        ttlFileInputStream.close();
                    } catch (IOException e) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("closing expired file failed: ");
                        sb.append(e.toString());
                        FLog.m50e(str, sb.toString());
                    }
                }
            }
            if (!this.mOpenFiles.isEmpty()) {
                this.mHandler.postDelayed(this, FILE_TTL);
            }
        }
    }
}
