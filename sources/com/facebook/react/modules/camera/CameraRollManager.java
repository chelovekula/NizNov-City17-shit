package com.facebook.react.modules.camera;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Files;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.android.vending.expansion.zipfile.APEZProvider;
import com.brentvatne.react.ReactVideoView;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.module.annotations.ReactModule;
import java.io.IOException;
import java.util.ArrayList;

@ReactModule(name = "CameraRollManager")
public class CameraRollManager extends ReactContextBaseJavaModule {
    private static final String ASSET_TYPE_ALL = "All";
    private static final String ASSET_TYPE_PHOTOS = "Photos";
    private static final String ASSET_TYPE_VIDEOS = "Videos";
    private static final String ERROR_UNABLE_TO_FILTER = "E_UNABLE_TO_FILTER";
    private static final String ERROR_UNABLE_TO_LOAD = "E_UNABLE_TO_LOAD";
    private static final String ERROR_UNABLE_TO_LOAD_PERMISSION = "E_UNABLE_TO_LOAD_PERMISSION";
    private static final String ERROR_UNABLE_TO_SAVE = "E_UNABLE_TO_SAVE";
    public static final String NAME = "CameraRollManager";
    /* access modifiers changed from: private */
    public static final String[] PROJECTION = {APEZProvider.FILEID, "mime_type", "bucket_display_name", "datetaken", "width", "height", "longitude", "latitude", "_data"};
    private static final String SELECTION_BUCKET = "bucket_display_name = ?";
    private static final String SELECTION_DATE_TAKEN = "datetaken < ?";

    private static class GetMediaTask extends GuardedAsyncTask<Void, Void> {
        @Nullable
        private final String mAfter;
        private final String mAssetType;
        private final Context mContext;
        private final int mFirst;
        @Nullable
        private final String mGroupName;
        @Nullable
        private final ReadableArray mMimeTypes;
        private final Promise mPromise;

        private GetMediaTask(ReactContext reactContext, int i, @Nullable String str, @Nullable String str2, @Nullable ReadableArray readableArray, String str3, Promise promise) {
            super(reactContext);
            this.mContext = reactContext;
            this.mFirst = i;
            this.mAfter = str;
            this.mGroupName = str2;
            this.mMimeTypes = readableArray;
            this.mPromise = promise;
            this.mAssetType = str3;
        }

        /* access modifiers changed from: protected */
        public void doInBackgroundGuarded(Void... voidArr) {
            Cursor query;
            StringBuilder sb = new StringBuilder("1");
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(this.mAfter)) {
                sb.append(" AND datetaken < ?");
                arrayList.add(this.mAfter);
            }
            if (!TextUtils.isEmpty(this.mGroupName)) {
                sb.append(" AND bucket_display_name = ?");
                arrayList.add(this.mGroupName);
            }
            String str = this.mAssetType;
            char c = 65535;
            int hashCode = str.hashCode();
            String str2 = CameraRollManager.ASSET_TYPE_ALL;
            String str3 = CameraRollManager.ASSET_TYPE_VIDEOS;
            String str4 = CameraRollManager.ASSET_TYPE_PHOTOS;
            if (hashCode != -1905167199) {
                if (hashCode != -1732810888) {
                    if (hashCode == 65921 && str.equals(str2)) {
                        c = 2;
                    }
                } else if (str.equals(str3)) {
                    c = 1;
                }
            } else if (str.equals(str4)) {
                c = 0;
            }
            if (c == 0) {
                sb.append(" AND media_type = 1");
            } else if (c == 1) {
                sb.append(" AND media_type = 3");
            } else if (c != 2) {
                Promise promise = this.mPromise;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid filter option: '");
                sb2.append(this.mAssetType);
                sb2.append("'. Expected one of '");
                sb2.append(str4);
                sb2.append("', '");
                sb2.append(str3);
                sb2.append("' or '");
                sb2.append(str2);
                sb2.append("'.");
                promise.reject(CameraRollManager.ERROR_UNABLE_TO_FILTER, sb2.toString());
                return;
            } else {
                sb.append(" AND media_type IN (3,1)");
            }
            ReadableArray readableArray = this.mMimeTypes;
            if (readableArray != null && readableArray.size() > 0) {
                sb.append(" AND mime_type IN (");
                for (int i = 0; i < this.mMimeTypes.size(); i++) {
                    sb.append("?,");
                    arrayList.add(this.mMimeTypes.getString(i));
                }
                sb.replace(sb.length() - 1, sb.length(), ")");
            }
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            ContentResolver contentResolver = this.mContext.getContentResolver();
            try {
                Uri contentUri = Files.getContentUri("external");
                String[] access$200 = CameraRollManager.PROJECTION;
                String sb3 = sb.toString();
                String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("datetaken DESC, date_modified DESC LIMIT ");
                sb4.append(this.mFirst + 1);
                query = contentResolver.query(contentUri, access$200, sb3, strArr, sb4.toString());
                if (query == null) {
                    this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_LOAD, "Could not get media");
                } else {
                    CameraRollManager.putEdges(contentResolver, query, writableNativeMap, this.mFirst);
                    CameraRollManager.putPageInfo(query, writableNativeMap, this.mFirst);
                    query.close();
                    this.mPromise.resolve(writableNativeMap);
                }
            } catch (SecurityException e) {
                this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_LOAD_PERMISSION, "Could not get media: need READ_EXTERNAL_STORAGE permission", (Throwable) e);
            } catch (Throwable th) {
                query.close();
                this.mPromise.resolve(writableNativeMap);
                throw th;
            }
        }
    }

    private static class SaveToCameraRoll extends GuardedAsyncTask<Void, Void> {
        private static final int SAVE_BUFFER_SIZE = 1048576;
        private final Context mContext;
        /* access modifiers changed from: private */
        public final Promise mPromise;
        private final Uri mUri;

        public SaveToCameraRoll(ReactContext reactContext, Uri uri, Promise promise) {
            super(reactContext);
            this.mContext = reactContext;
            this.mUri = uri;
            this.mPromise = promise;
        }

        /* JADX WARNING: type inference failed for: r3v2 */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.nio.channels.ReadableByteChannel] */
        /* JADX WARNING: type inference failed for: r3v3, types: [java.nio.channels.FileChannel] */
        /* JADX WARNING: type inference failed for: r4v2, types: [java.nio.channels.FileChannel] */
        /* JADX WARNING: type inference failed for: r3v4, types: [java.nio.channels.ReadableByteChannel] */
        /* JADX WARNING: type inference failed for: r12v0 */
        /* JADX WARNING: type inference failed for: r4v3 */
        /* JADX WARNING: type inference failed for: r3v5 */
        /* JADX WARNING: type inference failed for: r4v4 */
        /* JADX WARNING: type inference failed for: r4v5 */
        /* JADX WARNING: type inference failed for: r4v8, types: [java.nio.channels.ReadableByteChannel] */
        /* JADX WARNING: type inference failed for: r12v1 */
        /* JADX WARNING: type inference failed for: r4v9 */
        /* JADX WARNING: type inference failed for: r3v6 */
        /* JADX WARNING: type inference failed for: r12v2 */
        /* JADX WARNING: type inference failed for: r4v10 */
        /* JADX WARNING: type inference failed for: r2v14, types: [java.nio.channels.FileChannel] */
        /* JADX WARNING: type inference failed for: r12v3 */
        /* JADX WARNING: type inference failed for: r4v11 */
        /* JADX WARNING: type inference failed for: r3v9 */
        /* JADX WARNING: type inference failed for: r4v14, types: [java.nio.channels.ReadableByteChannel] */
        /* JADX WARNING: type inference failed for: r4v17, types: [java.nio.channels.FileChannel] */
        /* JADX WARNING: type inference failed for: r3v12 */
        /* JADX WARNING: type inference failed for: r3v13 */
        /* JADX WARNING: type inference failed for: r3v14 */
        /* JADX WARNING: type inference failed for: r4v18 */
        /* JADX WARNING: type inference failed for: r4v19 */
        /* JADX WARNING: type inference failed for: r4v20 */
        /* JADX WARNING: type inference failed for: r4v21 */
        /* JADX WARNING: type inference failed for: r4v22 */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v9
          assigns: []
          uses: []
          mth insns count: 161
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0055 A[Catch:{ IOException -> 0x0131, all -> 0x012f }] */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x006f A[SYNTHETIC, Splitter:B:21:0x006f] */
        /* JADX WARNING: Unknown variable types count: 12 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doInBackgroundGuarded(java.lang.Void... r14) {
            /*
                r13 = this;
                java.lang.String r14 = "Could not close output channel"
                java.lang.String r0 = "Could not close input channel"
                java.lang.String r1 = "ReactNative"
                java.io.File r2 = new java.io.File
                android.net.Uri r3 = r13.mUri
                java.lang.String r3 = r3.getPath()
                r2.<init>(r3)
                r3 = 0
                android.net.Uri r4 = r13.mUri     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.lang.String r4 = r4.getScheme()     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.lang.String r5 = "http"
                boolean r5 = r4.equals(r5)     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                if (r5 != 0) goto L_0x0033
                java.lang.String r5 = "https"
                boolean r4 = r4.equals(r5)     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                if (r4 == 0) goto L_0x0029
                goto L_0x0033
            L_0x0029:
                java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                r4.<init>(r2)     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.nio.channels.FileChannel r4 = r4.getChannel()     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                goto L_0x0046
            L_0x0033:
                java.net.URL r4 = new java.net.URL     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                android.net.Uri r5 = r13.mUri     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                r4.<init>(r5)     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.io.InputStream r4 = r4.openStream()     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
                java.nio.channels.ReadableByteChannel r4 = java.nio.channels.Channels.newChannel(r4)     // Catch:{ IOException -> 0x0139, all -> 0x0136 }
            L_0x0046:
                java.lang.String r5 = android.os.Environment.DIRECTORY_DCIM     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.io.File r5 = android.os.Environment.getExternalStoragePublicDirectory(r5)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r5.mkdirs()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                boolean r6 = r5.isDirectory()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                if (r6 != 0) goto L_0x006f
                com.facebook.react.bridge.Promise r2 = r13.mPromise     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r5 = "E_UNABLE_TO_LOAD"
                java.lang.String r6 = "External media storage directory not available"
                r2.reject(r5, r6)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                if (r4 == 0) goto L_0x006e
                boolean r14 = r4.isOpen()
                if (r14 == 0) goto L_0x006e
                r4.close()     // Catch:{ IOException -> 0x006a }
                goto L_0x006e
            L_0x006a:
                r14 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r0, r14)
            L_0x006e:
                return
            L_0x006f:
                java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r7 = r2.getName()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r6.<init>(r5, r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r2 = r2.getName()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r7 = 46
                int r8 = r2.indexOf(r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r9 = 0
                if (r8 < 0) goto L_0x009a
                int r8 = r2.lastIndexOf(r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r8 = r2.substring(r9, r8)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                int r7 = r2.lastIndexOf(r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r2 = r2.substring(r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r7 = 0
                r12 = r8
                r8 = r2
                r2 = r12
                goto L_0x009e
            L_0x009a:
                java.lang.String r7 = ""
                r8 = r7
                r7 = 0
            L_0x009e:
                boolean r10 = r6.createNewFile()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                if (r10 != 0) goto L_0x00c4
                java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r10.<init>()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r10.append(r2)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r11 = "_"
                r10.append(r11)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                int r11 = r7 + 1
                r10.append(r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r10.append(r8)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.lang.String r7 = r10.toString()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r6.<init>(r5, r7)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r7 = r11
                goto L_0x009e
            L_0x00c4:
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r2.<init>(r6)     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                java.nio.channels.FileChannel r2 = r2.getChannel()     // Catch:{ IOException -> 0x0131, all -> 0x012f }
                r5 = 1048576(0x100000, float:1.469368E-39)
                java.nio.ByteBuffer r5 = java.nio.ByteBuffer.allocate(r5)     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
            L_0x00d3:
                int r7 = r4.read(r5)     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                if (r7 <= 0) goto L_0x00e3
                r5.flip()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r2.write(r5)     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r5.compact()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                goto L_0x00d3
            L_0x00e3:
                r5.flip()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
            L_0x00e6:
                boolean r7 = r5.hasRemaining()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                if (r7 == 0) goto L_0x00f0
                r2.write(r5)     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                goto L_0x00e6
            L_0x00f0:
                r4.close()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r2.close()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                android.content.Context r5 = r13.mContext     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r7 = 1
                java.lang.String[] r7 = new java.lang.String[r7]     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                java.lang.String r6 = r6.getAbsolutePath()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r7[r9] = r6     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                com.facebook.react.modules.camera.CameraRollManager$SaveToCameraRoll$1 r6 = new com.facebook.react.modules.camera.CameraRollManager$SaveToCameraRoll$1     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                r6.<init>()     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                android.media.MediaScannerConnection.scanFile(r5, r7, r3, r6)     // Catch:{ IOException -> 0x012a, all -> 0x0125 }
                if (r4 == 0) goto L_0x0119
                boolean r3 = r4.isOpen()
                if (r3 == 0) goto L_0x0119
                r4.close()     // Catch:{ IOException -> 0x0115 }
                goto L_0x0119
            L_0x0115:
                r3 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r0, r3)
            L_0x0119:
                if (r2 == 0) goto L_0x0160
                boolean r0 = r2.isOpen()
                if (r0 == 0) goto L_0x0160
                r2.close()     // Catch:{ IOException -> 0x015c }
                goto L_0x0160
            L_0x0125:
                r3 = move-exception
                r12 = r3
                r3 = r2
                r2 = r12
                goto L_0x0165
            L_0x012a:
                r3 = move-exception
                r12 = r4
                r4 = r2
                r2 = r3
                goto L_0x0134
            L_0x012f:
                r2 = move-exception
                goto L_0x0165
            L_0x0131:
                r2 = move-exception
                r12 = r4
                r4 = r3
            L_0x0134:
                r3 = r12
                goto L_0x013b
            L_0x0136:
                r2 = move-exception
                r4 = r3
                goto L_0x0165
            L_0x0139:
                r2 = move-exception
                r4 = r3
            L_0x013b:
                com.facebook.react.bridge.Promise r5 = r13.mPromise     // Catch:{ all -> 0x0161 }
                r5.reject(r2)     // Catch:{ all -> 0x0161 }
                if (r3 == 0) goto L_0x0150
                boolean r2 = r3.isOpen()
                if (r2 == 0) goto L_0x0150
                r3.close()     // Catch:{ IOException -> 0x014c }
                goto L_0x0150
            L_0x014c:
                r2 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r0, r2)
            L_0x0150:
                if (r4 == 0) goto L_0x0160
                boolean r0 = r4.isOpen()
                if (r0 == 0) goto L_0x0160
                r4.close()     // Catch:{ IOException -> 0x015c }
                goto L_0x0160
            L_0x015c:
                r0 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r14, r0)
            L_0x0160:
                return
            L_0x0161:
                r2 = move-exception
                r12 = r4
                r4 = r3
                r3 = r12
            L_0x0165:
                if (r4 == 0) goto L_0x0175
                boolean r5 = r4.isOpen()
                if (r5 == 0) goto L_0x0175
                r4.close()     // Catch:{ IOException -> 0x0171 }
                goto L_0x0175
            L_0x0171:
                r4 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r0, r4)
            L_0x0175:
                if (r3 == 0) goto L_0x0185
                boolean r0 = r3.isOpen()
                if (r0 == 0) goto L_0x0185
                r3.close()     // Catch:{ IOException -> 0x0181 }
                goto L_0x0185
            L_0x0181:
                r0 = move-exception
                com.facebook.common.logging.FLog.m51e(r1, r14, r0)
            L_0x0185:
                goto L_0x0187
            L_0x0186:
                throw r2
            L_0x0187:
                goto L_0x0186
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.camera.CameraRollManager.SaveToCameraRoll.doInBackgroundGuarded(java.lang.Void[]):void");
        }
    }

    public String getName() {
        return NAME;
    }

    public CameraRollManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void saveToCameraRoll(String str, String str2, Promise promise) {
        new SaveToCameraRoll(getReactApplicationContext(), Uri.parse(str), promise).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @ReactMethod
    public void getPhotos(ReadableMap readableMap, Promise promise) {
        int i = readableMap.getInt("first");
        String str = "after";
        String string = readableMap.hasKey(str) ? readableMap.getString(str) : null;
        String str2 = "groupName";
        String string2 = readableMap.hasKey(str2) ? readableMap.getString(str2) : null;
        String str3 = "assetType";
        String string3 = readableMap.hasKey(str3) ? readableMap.getString(str3) : ASSET_TYPE_PHOTOS;
        String str4 = "mimeTypes";
        ReadableArray array = readableMap.hasKey(str4) ? readableMap.getArray(str4) : null;
        if (!readableMap.hasKey("groupTypes")) {
            GetMediaTask getMediaTask = new GetMediaTask(getReactApplicationContext(), i, string, string2, array, string3, promise);
            getMediaTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        throw new JSApplicationIllegalArgumentException("groupTypes is not supported on Android");
    }

    /* access modifiers changed from: private */
    public static void putPageInfo(Cursor cursor, WritableMap writableMap, int i) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putBoolean("has_next_page", i < cursor.getCount());
        if (i < cursor.getCount()) {
            cursor.moveToPosition(i - 1);
            writableNativeMap.putString("end_cursor", cursor.getString(cursor.getColumnIndex("datetaken")));
        }
        writableMap.putMap("page_info", writableNativeMap);
    }

    /* access modifiers changed from: private */
    public static void putEdges(ContentResolver contentResolver, Cursor cursor, WritableMap writableMap, int i) {
        WritableNativeArray writableNativeArray;
        Cursor cursor2 = cursor;
        WritableNativeArray writableNativeArray2 = new WritableNativeArray();
        cursor.moveToFirst();
        int columnIndex = cursor2.getColumnIndex(APEZProvider.FILEID);
        int columnIndex2 = cursor2.getColumnIndex("mime_type");
        int columnIndex3 = cursor2.getColumnIndex("bucket_display_name");
        int columnIndex4 = cursor2.getColumnIndex("datetaken");
        int columnIndex5 = cursor2.getColumnIndex("width");
        int columnIndex6 = cursor2.getColumnIndex("height");
        int columnIndex7 = cursor2.getColumnIndex("longitude");
        int columnIndex8 = cursor2.getColumnIndex("latitude");
        int columnIndex9 = cursor2.getColumnIndex("_data");
        int i2 = i;
        int i3 = 0;
        while (i3 < i2 && !cursor.isAfterLast()) {
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            WritableNativeMap writableNativeMap2 = new WritableNativeMap();
            WritableNativeMap writableNativeMap3 = writableNativeMap2;
            WritableNativeArray writableNativeArray3 = writableNativeArray2;
            WritableNativeMap writableNativeMap4 = writableNativeMap;
            int i4 = i3;
            int i5 = columnIndex;
            int i6 = columnIndex8;
            int i7 = columnIndex5;
            int i8 = columnIndex7;
            if (putImageInfo(contentResolver, cursor, writableNativeMap2, columnIndex, columnIndex5, columnIndex6, columnIndex9, columnIndex2)) {
                WritableNativeMap writableNativeMap5 = writableNativeMap3;
                putBasicNodeInfo(cursor2, writableNativeMap5, columnIndex2, columnIndex3, columnIndex4);
                putLocationInfo(cursor2, writableNativeMap5, i8, i6);
                writableNativeMap4.putMap("node", writableNativeMap5);
                writableNativeArray = writableNativeArray3;
                writableNativeArray.pushMap(writableNativeMap4);
            } else {
                writableNativeArray = writableNativeArray3;
                i4--;
            }
            cursor.moveToNext();
            i3 = i4 + 1;
            i2 = i;
            writableNativeArray2 = writableNativeArray;
            columnIndex8 = i6;
            columnIndex7 = i8;
            columnIndex = i5;
            columnIndex5 = i7;
        }
        WritableMap writableMap2 = writableMap;
        writableMap2.putArray("edges", writableNativeArray2);
    }

    private static void putBasicNodeInfo(Cursor cursor, WritableMap writableMap, int i, int i2, int i3) {
        writableMap.putString(ReactVideoViewManager.PROP_SRC_TYPE, cursor.getString(i));
        writableMap.putString("group_name", cursor.getString(i2));
        double d = (double) cursor.getLong(i3);
        Double.isNaN(d);
        writableMap.putDouble("timestamp", d / 1000.0d);
    }

    private static boolean putImageInfo(ContentResolver contentResolver, Cursor cursor, WritableMap writableMap, int i, int i2, int i3, int i4, int i5) {
        AssetFileDescriptor openAssetFileDescriptor;
        MediaMetadataRetriever mediaMetadataRetriever;
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(cursor.getString(i4));
        Uri parse = Uri.parse(sb.toString());
        writableNativeMap.putString("uri", parse.toString());
        float f = (float) cursor.getInt(i2);
        float f2 = (float) cursor.getInt(i3);
        String string = cursor.getString(i5);
        String str = "r";
        String str2 = ReactConstants.TAG;
        if (string != null && string.startsWith("video")) {
            try {
                openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(parse, str);
                mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(openAssetFileDescriptor.getFileDescriptor());
                if (f <= 0.0f || f2 <= 0.0f) {
                    try {
                        f = (float) Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
                        f2 = (float) Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
                    } catch (NumberFormatException e) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Number format exception occurred while trying to fetch video metadata for ");
                        sb2.append(parse.toString());
                        FLog.m51e(str2, sb2.toString(), (Throwable) e);
                        mediaMetadataRetriever.release();
                        openAssetFileDescriptor.close();
                        return false;
                    }
                }
                writableNativeMap.putInt(ReactVideoView.EVENT_PROP_PLAYABLE_DURATION, Integer.parseInt(mediaMetadataRetriever.extractMetadata(9)) / 1000);
                mediaMetadataRetriever.release();
                openAssetFileDescriptor.close();
            } catch (Exception e2) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Could not get video metadata for ");
                sb3.append(parse.toString());
                FLog.m51e(str2, sb3.toString(), (Throwable) e2);
                return false;
            } catch (Throwable th) {
                mediaMetadataRetriever.release();
                openAssetFileDescriptor.close();
                throw th;
            }
        }
        if (f <= 0.0f || f2 <= 0.0f) {
            try {
                AssetFileDescriptor openAssetFileDescriptor2 = contentResolver.openAssetFileDescriptor(parse, str);
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(openAssetFileDescriptor2.getFileDescriptor(), null, options);
                float f3 = (float) options.outWidth;
                float f4 = (float) options.outHeight;
                openAssetFileDescriptor2.close();
                float f5 = f3;
                f2 = f4;
                f = f5;
            } catch (IOException e3) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Could not get width/height for ");
                sb4.append(parse.toString());
                FLog.m51e(str2, sb4.toString(), (Throwable) e3);
                return false;
            }
        }
        writableNativeMap.putDouble("width", (double) f);
        writableNativeMap.putDouble("height", (double) f2);
        writableMap.putMap("image", writableNativeMap);
        return true;
    }

    private static void putLocationInfo(Cursor cursor, WritableMap writableMap, int i, int i2) {
        double d = cursor.getDouble(i);
        double d2 = cursor.getDouble(i2);
        if (d > 0.0d || d2 > 0.0d) {
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            writableNativeMap.putDouble("longitude", d);
            writableNativeMap.putDouble("latitude", d2);
            writableMap.putMap("location", writableNativeMap);
        }
    }
}
