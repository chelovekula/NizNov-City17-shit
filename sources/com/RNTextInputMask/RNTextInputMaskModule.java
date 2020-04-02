package com.RNTextInputMask;

import android.text.TextWatcher;
import android.widget.EditText;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener;
import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.model.CaretString;

public class RNTextInputMaskModule extends ReactContextBaseJavaModule {
    private static final int TEXT_CHANGE_LISTENER_TAG_KEY = 123456789;
    ReactApplicationContext reactContext;

    public String getName() {
        return "RNTextInputMask";
    }

    public RNTextInputMaskModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    @ReactMethod
    public void mask(String str, String str2, Callback callback) {
        callback.invoke(new Mask(str).apply(new CaretString(str2, str2.length()), true).getFormattedText().getString());
    }

    @ReactMethod
    public void unmask(String str, String str2, Callback callback) {
        callback.invoke(new Mask(str).apply(new CaretString(str2, str2.length()), true).getExtractedValue());
    }

    @ReactMethod
    public void setMask(final int i, final String str) {
        ((UIManagerModule) this.reactContext.getNativeModule(UIManagerModule.class)).prependUIBlock(new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                final EditText editText = (EditText) nativeViewHierarchyManager.resolveView(i);
                RNTextInputMaskModule.this.reactContext.runOnUiQueueThread(new Runnable() {
                    public void run() {
                        MaskedTextChangedListener maskedTextChangedListener = new MaskedTextChangedListener(str, true, editText, (TextWatcher) null, (ValueListener) null);
                        if (editText.getTag(RNTextInputMaskModule.TEXT_CHANGE_LISTENER_TAG_KEY) != null) {
                            EditText editText = editText;
                            editText.removeTextChangedListener((TextWatcher) editText.getTag(RNTextInputMaskModule.TEXT_CHANGE_LISTENER_TAG_KEY));
                        }
                        editText.setTag(RNTextInputMaskModule.TEXT_CHANGE_LISTENER_TAG_KEY, maskedTextChangedListener);
                        editText.addTextChangedListener(maskedTextChangedListener);
                    }
                });
            }
        });
    }
}
