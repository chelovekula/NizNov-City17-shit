package com.google.maps.android.data.kml;

public class KmlBoolean {
    public static boolean parseBoolean(String str) {
        return "1".equals(str) || "true".equals(str);
    }
}
