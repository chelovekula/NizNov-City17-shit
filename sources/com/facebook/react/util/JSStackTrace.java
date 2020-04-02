package com.facebook.react.util;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSStackTrace {
    private static final String COLUMN_KEY = "column";
    private static final Pattern FILE_ID_PATTERN = Pattern.compile("\\b((?:seg-\\d+(?:_\\d+)?|\\d+)\\.js)");
    private static final String FILE_KEY = "file";
    private static final String LINE_NUMBER_KEY = "lineNumber";
    private static final String METHOD_NAME_KEY = "methodName";

    public static String format(String str, ReadableArray readableArray) {
        StringBuilder sb = new StringBuilder(str);
        sb.append(", stack:\n");
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableMap map = readableArray.getMap(i);
            sb.append(map.getString(METHOD_NAME_KEY));
            sb.append("@");
            sb.append(parseFileId(map));
            String str2 = "lineNumber";
            if (!map.hasKey(str2) || map.isNull(str2) || map.getType(str2) != ReadableType.Number) {
                sb.append(-1);
            } else {
                sb.append(map.getInt(str2));
            }
            String str3 = "column";
            if (map.hasKey(str3) && !map.isNull(str3) && map.getType(str3) == ReadableType.Number) {
                sb.append(":");
                sb.append(map.getInt(str3));
            }
            sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
        }
        return sb.toString();
    }

    private static String parseFileId(ReadableMap readableMap) {
        String str = "file";
        if (readableMap.hasKey(str) && !readableMap.isNull(str) && readableMap.getType(str) == ReadableType.String) {
            String string = readableMap.getString(str);
            if (string != null) {
                Matcher matcher = FILE_ID_PATTERN.matcher(string);
                if (matcher.find()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(matcher.group(1));
                    sb.append(":");
                    return sb.toString();
                }
            }
        }
        return "";
    }
}
