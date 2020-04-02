package com.RNFetchBlob;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

class RNFetchBlobConfig {
    public ReadableMap addAndroidDownloads;
    public String appendExt;
    public Boolean auto;
    public ReadableArray binaryContentTypes;
    public Boolean fileCache;
    public Boolean followRedirect;
    public Boolean increment;
    public String key;
    public String mime;
    public Boolean overwrite;
    public String path;
    public long timeout = 60000;
    public Boolean trusty;
    public Boolean wifiOnly;

    RNFetchBlobConfig(ReadableMap readableMap) {
        boolean z = false;
        Boolean valueOf = Boolean.valueOf(false);
        this.wifiOnly = valueOf;
        Boolean valueOf2 = Boolean.valueOf(true);
        this.overwrite = valueOf2;
        this.increment = valueOf;
        this.followRedirect = valueOf2;
        String str = null;
        this.binaryContentTypes = null;
        if (readableMap != null) {
            String str2 = "fileCache";
            this.fileCache = Boolean.valueOf(readableMap.hasKey(str2) ? readableMap.getBoolean(str2) : false);
            String str3 = RNFetchBlobConst.RNFB_RESPONSE_PATH;
            this.path = readableMap.hasKey(str3) ? readableMap.getString(str3) : null;
            String str4 = "appendExt";
            this.appendExt = readableMap.hasKey(str4) ? readableMap.getString(str4) : "";
            String str5 = "trusty";
            this.trusty = Boolean.valueOf(readableMap.hasKey(str5) ? readableMap.getBoolean(str5) : false);
            String str6 = "wifiOnly";
            this.wifiOnly = Boolean.valueOf(readableMap.hasKey(str6) ? readableMap.getBoolean(str6) : false);
            String str7 = "addAndroidDownloads";
            if (readableMap.hasKey(str7)) {
                this.addAndroidDownloads = readableMap.getMap(str7);
            }
            String str8 = "binaryContentTypes";
            if (readableMap.hasKey(str8)) {
                this.binaryContentTypes = readableMap.getArray(str8);
            }
            String str9 = this.path;
            if (str9 != null && str9.toLowerCase().contains("?append=true")) {
                this.overwrite = valueOf;
            }
            String str10 = "overwrite";
            if (readableMap.hasKey(str10)) {
                this.overwrite = Boolean.valueOf(readableMap.getBoolean(str10));
            }
            String str11 = "followRedirect";
            if (readableMap.hasKey(str11)) {
                this.followRedirect = Boolean.valueOf(readableMap.getBoolean(str11));
            }
            String str12 = "key";
            this.key = readableMap.hasKey(str12) ? readableMap.getString(str12) : null;
            String str13 = "contentType";
            if (readableMap.hasKey(str13)) {
                str = readableMap.getString(str13);
            }
            this.mime = str;
            String str14 = "increment";
            this.increment = Boolean.valueOf(readableMap.hasKey(str14) ? readableMap.getBoolean(str14) : false);
            if (readableMap.hasKey("auto")) {
                z = readableMap.getBoolean("auto");
            }
            this.auto = Boolean.valueOf(z);
            if (readableMap.hasKey("timeout")) {
                this.timeout = (long) readableMap.getInt("timeout");
            }
        }
    }
}
