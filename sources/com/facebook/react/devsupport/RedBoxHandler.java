package com.facebook.react.devsupport;

import android.content.Context;
import android.text.SpannedString;
import androidx.annotation.Nullable;
import com.facebook.react.devsupport.interfaces.StackFrame;

public interface RedBoxHandler {

    public enum ErrorType {
        f43JS(r2),
        NATIVE("Native");
        
        private final String name;

        private ErrorType(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }
    }

    public interface ReportCompletedListener {
        void onReportError(SpannedString spannedString);

        void onReportSuccess(SpannedString spannedString);
    }

    void handleRedbox(@Nullable String str, StackFrame[] stackFrameArr, ErrorType errorType);

    boolean isReportEnabled();

    void reportRedbox(Context context, String str, StackFrame[] stackFrameArr, String str2, ReportCompletedListener reportCompletedListener);
}
