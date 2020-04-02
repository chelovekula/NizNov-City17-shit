package com.facebook.react.devsupport;

import androidx.annotation.Nullable;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.devsupport.interfaces.StackFrame;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StackTraceHelper {
    public static final String COLUMN_KEY = "column";
    public static final String LINE_NUMBER_KEY = "lineNumber";
    private static final Pattern STACK_FRAME_PATTERN1 = Pattern.compile("^(?:(.*?)@)?(.*?)\\:([0-9]+)\\:([0-9]+)$");
    private static final Pattern STACK_FRAME_PATTERN2 = Pattern.compile("\\s*(?:at)\\s*(.+?)\\s*[@(](.*):([0-9]+):([0-9]+)[)]$");

    public static class StackFrameImpl implements StackFrame {
        private final int mColumn;
        private final String mFile;
        private final String mFileName;
        private final int mLine;
        private final String mMethod;

        private StackFrameImpl(String str, String str2, int i, int i2) {
            this.mFile = str;
            this.mMethod = str2;
            this.mLine = i;
            this.mColumn = i2;
            this.mFileName = str != null ? new File(str).getName() : "";
        }

        private StackFrameImpl(String str, String str2, String str3, int i, int i2) {
            this.mFile = str;
            this.mFileName = str2;
            this.mMethod = str3;
            this.mLine = i;
            this.mColumn = i2;
        }

        public String getFile() {
            return this.mFile;
        }

        public String getMethod() {
            return this.mMethod;
        }

        public int getLine() {
            return this.mLine;
        }

        public int getColumn() {
            return this.mColumn;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public JSONObject toJSON() {
            return new JSONObject(MapBuilder.m128of(UriUtil.LOCAL_FILE_SCHEME, getFile(), "methodName", getMethod(), StackTraceHelper.LINE_NUMBER_KEY, Integer.valueOf(getLine()), StackTraceHelper.COLUMN_KEY, Integer.valueOf(getColumn())));
        }
    }

    public static StackFrame[] convertJsStackTrace(@Nullable ReadableArray readableArray) {
        int size = readableArray != null ? readableArray.size() : 0;
        StackFrame[] stackFrameArr = new StackFrame[size];
        for (int i = 0; i < size; i++) {
            ReadableType type = readableArray.getType(i);
            if (type == ReadableType.Map) {
                ReadableMap map = readableArray.getMap(i);
                String string = map.getString("methodName");
                String string2 = map.getString(UriUtil.LOCAL_FILE_SCHEME);
                String str = LINE_NUMBER_KEY;
                int i2 = (!map.hasKey(str) || map.isNull(str)) ? -1 : map.getInt(str);
                String str2 = COLUMN_KEY;
                StackFrameImpl stackFrameImpl = new StackFrameImpl(string2, string, i2, (!map.hasKey(str2) || map.isNull(str2)) ? -1 : map.getInt(str2));
                stackFrameArr[i] = stackFrameImpl;
            } else if (type == ReadableType.String) {
                StackFrameImpl stackFrameImpl2 = new StackFrameImpl((String) null, readableArray.getString(i), -1, -1);
                stackFrameArr[i] = stackFrameImpl2;
            }
        }
        return stackFrameArr;
    }

    public static StackFrame[] convertJsStackTrace(JSONArray jSONArray) {
        String str = COLUMN_KEY;
        String str2 = LINE_NUMBER_KEY;
        int i = 0;
        int length = jSONArray != null ? jSONArray.length() : 0;
        StackFrame[] stackFrameArr = new StackFrame[length];
        while (i < length) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                StackFrameImpl stackFrameImpl = new StackFrameImpl(jSONObject.getString(UriUtil.LOCAL_FILE_SCHEME), jSONObject.getString("methodName"), (!jSONObject.has(str2) || jSONObject.isNull(str2)) ? -1 : jSONObject.getInt(str2), (!jSONObject.has(str) || jSONObject.isNull(str)) ? -1 : jSONObject.getInt(str));
                stackFrameArr[i] = stackFrameImpl;
                i++;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return stackFrameArr;
    }

    public static StackFrame[] convertJsStackTrace(String str) {
        String[] split = str.split(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
        StackFrame[] stackFrameArr = new StackFrame[split.length];
        for (int i = 0; i < split.length; i++) {
            Matcher matcher = STACK_FRAME_PATTERN1.matcher(split[i]);
            Matcher matcher2 = STACK_FRAME_PATTERN2.matcher(split[i]);
            if (matcher2.find()) {
                matcher = matcher2;
            } else if (!matcher.find()) {
                StackFrameImpl stackFrameImpl = new StackFrameImpl((String) null, split[i], -1, -1);
                stackFrameArr[i] = stackFrameImpl;
            }
            StackFrameImpl stackFrameImpl2 = new StackFrameImpl(matcher.group(2), matcher.group(1) == null ? "(unknown)" : matcher.group(1), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            stackFrameArr[i] = stackFrameImpl2;
        }
        return stackFrameArr;
    }

    public static StackFrame[] convertJavaStackTrace(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        StackFrame[] stackFrameArr = new StackFrame[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++) {
            StackFrameImpl stackFrameImpl = new StackFrameImpl(stackTrace[i].getClassName(), stackTrace[i].getFileName(), stackTrace[i].getMethodName(), stackTrace[i].getLineNumber(), -1);
            stackFrameArr[i] = stackFrameImpl;
        }
        return stackFrameArr;
    }

    public static String formatFrameSource(StackFrame stackFrame) {
        StringBuilder sb = new StringBuilder();
        sb.append(stackFrame.getFileName());
        int line = stackFrame.getLine();
        if (line > 0) {
            String str = ":";
            sb.append(str);
            sb.append(line);
            int column = stackFrame.getColumn();
            if (column > 0) {
                sb.append(str);
                sb.append(column);
            }
        }
        return sb.toString();
    }

    public static String formatStackTrace(String str, StackFrame[] stackFrameArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        String str2 = ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE;
        sb.append(str2);
        for (StackFrame stackFrame : stackFrameArr) {
            sb.append(stackFrame.getMethod());
            sb.append(str2);
            sb.append("    ");
            sb.append(formatFrameSource(stackFrame));
            sb.append(str2);
        }
        return sb.toString();
    }
}
