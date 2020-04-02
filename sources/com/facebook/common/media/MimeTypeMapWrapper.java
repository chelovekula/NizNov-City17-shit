package com.facebook.common.media;

import android.webkit.MimeTypeMap;
import com.facebook.common.internal.ImmutableMap;
import java.util.Map;

public class MimeTypeMapWrapper {
    private static final Map<String, String> sExtensionToMimeTypeMap;
    private static final MimeTypeMap sMimeTypeMap = MimeTypeMap.getSingleton();
    private static final Map<String, String> sMimeTypeToExtensionMap;

    static {
        String str = "heic";
        String str2 = "image/heic";
        String str3 = "heif";
        String str4 = "image/heif";
        sMimeTypeToExtensionMap = ImmutableMap.m23of(str4, str3, str2, str);
        sExtensionToMimeTypeMap = ImmutableMap.m23of(str3, str4, str, str2);
    }

    public static String getExtensionFromMimeType(String str) {
        String str2 = (String) sMimeTypeToExtensionMap.get(str);
        if (str2 != null) {
            return str2;
        }
        return sMimeTypeMap.getExtensionFromMimeType(str);
    }

    public static String getMimeTypeFromExtension(String str) {
        String str2 = (String) sExtensionToMimeTypeMap.get(str);
        if (str2 != null) {
            return str2;
        }
        return sMimeTypeMap.getMimeTypeFromExtension(str);
    }

    public static boolean hasExtension(String str) {
        return sExtensionToMimeTypeMap.containsKey(str) || sMimeTypeMap.hasExtension(str);
    }

    public static boolean hasMimeType(String str) {
        return sMimeTypeToExtensionMap.containsKey(str) || sMimeTypeMap.hasMimeType(str);
    }
}
