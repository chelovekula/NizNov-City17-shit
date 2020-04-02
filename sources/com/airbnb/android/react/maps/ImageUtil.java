package com.airbnb.android.react.maps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static Bitmap convert(String str) throws IllegalArgumentException {
        byte[] decode = Base64.decode(str.substring(str.indexOf(",") + 1), 0);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }
}
