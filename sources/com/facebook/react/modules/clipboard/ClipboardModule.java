package com.facebook.react.modules.clipboard;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import com.facebook.react.bridge.ContextBaseJavaModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = "Clipboard")
public class ClipboardModule extends ContextBaseJavaModule {
    public static final String NAME = "Clipboard";

    public String getName() {
        return NAME;
    }

    public ClipboardModule(Context context) {
        super(context);
    }

    private ClipboardManager getClipboardService() {
        Context context = getContext();
        getContext();
        return (ClipboardManager) context.getSystemService("clipboard");
    }

    @ReactMethod
    public void getString(Promise promise) {
        try {
            ClipboardManager clipboardService = getClipboardService();
            ClipData primaryClip = clipboardService.getPrimaryClip();
            String str = "";
            if (primaryClip != null) {
                if (primaryClip.getItemCount() >= 1) {
                    Item itemAt = clipboardService.getPrimaryClip().getItemAt(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(itemAt.getText());
                    promise.resolve(sb.toString());
                    return;
                }
            }
            promise.resolve(str);
        } catch (Exception e) {
            promise.reject((Throwable) e);
        }
    }

    @ReactMethod
    public void setString(String str) {
        getClipboardService().setPrimaryClip(ClipData.newPlainText(null, str));
    }
}
