package com.android.vending.expansion.zipfile;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class APKExpansionSupport {
    private static final String EXP_PATH = "/Android/obb/";

    static String[] getAPKExpansionFiles(Context context, int i, int i2) {
        String packageName = context.getPackageName();
        Vector vector = new Vector();
        if (Environment.getExternalStorageState().equals("mounted")) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            StringBuilder sb = new StringBuilder();
            sb.append(externalStorageDirectory.toString());
            sb.append(EXP_PATH);
            sb.append(packageName);
            File file = new File(sb.toString());
            if (file.exists()) {
                String str = ".obb";
                String str2 = ".";
                if (i > 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(file);
                    sb2.append(File.separator);
                    sb2.append("main.");
                    sb2.append(i);
                    sb2.append(str2);
                    sb2.append(packageName);
                    sb2.append(str);
                    String sb3 = sb2.toString();
                    if (new File(sb3).isFile()) {
                        vector.add(sb3);
                    }
                }
                if (i2 > 0) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(file);
                    sb4.append(File.separator);
                    sb4.append("patch.");
                    sb4.append(i2);
                    sb4.append(str2);
                    sb4.append(packageName);
                    sb4.append(str);
                    String sb5 = sb4.toString();
                    if (new File(sb5).isFile()) {
                        vector.add(sb5);
                    }
                }
            }
        }
        String[] strArr = new String[vector.size()];
        vector.toArray(strArr);
        return strArr;
    }

    public static ZipResourceFile getResourceZipFile(String[] strArr) throws IOException {
        ZipResourceFile zipResourceFile = null;
        for (String str : strArr) {
            if (zipResourceFile == null) {
                zipResourceFile = new ZipResourceFile(str);
            } else {
                zipResourceFile.addPatchFile(str);
            }
        }
        return zipResourceFile;
    }

    public static ZipResourceFile getAPKExpansionZipFile(Context context, int i, int i2) throws IOException {
        return getResourceZipFile(getAPKExpansionFiles(context, i, i2));
    }
}
