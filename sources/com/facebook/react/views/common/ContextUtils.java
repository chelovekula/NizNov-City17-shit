package com.facebook.react.views.common;

import android.content.Context;
import android.content.ContextWrapper;
import androidx.annotation.Nullable;

public class ContextUtils {
    @Nullable
    public static <T> T findContextOfType(@Nullable Context context, Class<? extends T> cls) {
        while (!cls.isInstance(context)) {
            if (!(context instanceof ContextWrapper)) {
                return null;
            }
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (context == baseContext) {
                return null;
            }
            context = baseContext;
        }
        return context;
    }
}
