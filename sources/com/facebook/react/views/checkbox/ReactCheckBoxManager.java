package com.facebook.react.views.checkbox;

import android.content.Context;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import androidx.appcompat.widget.TintContextWrapper;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;

public class ReactCheckBoxManager extends SimpleViewManager<ReactCheckBox> {
    private static final OnCheckedChangeListener ON_CHECKED_CHANGE_LISTENER = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            ((UIManagerModule) getReactContext(compoundButton).getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(new ReactCheckBoxEvent(compoundButton.getId(), z));
        }

        private ReactContext getReactContext(CompoundButton compoundButton) {
            Context context = compoundButton.getContext();
            if (context instanceof TintContextWrapper) {
                return (ReactContext) ((TintContextWrapper) context).getBaseContext();
            }
            return (ReactContext) compoundButton.getContext();
        }
    };
    private static final String REACT_CLASS = "AndroidCheckBox";

    public String getName() {
        return REACT_CLASS;
    }

    /* access modifiers changed from: protected */
    public void addEventEmitters(ThemedReactContext themedReactContext, ReactCheckBox reactCheckBox) {
        reactCheckBox.setOnCheckedChangeListener(ON_CHECKED_CHANGE_LISTENER);
    }

    /* access modifiers changed from: protected */
    public ReactCheckBox createViewInstance(ThemedReactContext themedReactContext) {
        return new ReactCheckBox(themedReactContext);
    }

    @ReactProp(defaultBoolean = true, name = "enabled")
    public void setEnabled(ReactCheckBox reactCheckBox, boolean z) {
        reactCheckBox.setEnabled(z);
    }

    @ReactProp(name = "on")
    public void setOn(ReactCheckBox reactCheckBox, boolean z) {
        reactCheckBox.setOnCheckedChangeListener(null);
        reactCheckBox.setOn(z);
        reactCheckBox.setOnCheckedChangeListener(ON_CHECKED_CHANGE_LISTENER);
    }

    private static int getThemeColor(Context context, String str) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(getIdentifier(context, str), typedValue, true);
        return typedValue.data;
    }

    private static int getIdentifier(Context context, String str) {
        return context.getResources().getIdentifier(str, "attr", context.getPackageName());
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x001c  */
    @com.facebook.react.uimanager.annotations.ReactProp(name = "tintColors")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTintColors(com.facebook.react.views.checkbox.ReactCheckBox r9, @androidx.annotation.Nullable com.facebook.react.bridge.ReadableMap r10) {
        /*
            r8 = this;
            if (r10 == 0) goto L_0x0010
            java.lang.String r0 = "true"
            boolean r1 = r10.hasKey(r0)
            if (r1 != 0) goto L_0x000b
            goto L_0x0010
        L_0x000b:
            int r0 = r10.getInt(r0)
            goto L_0x001a
        L_0x0010:
            android.content.Context r0 = r9.getContext()
            java.lang.String r1 = "colorAccent"
            int r0 = getThemeColor(r0, r1)
        L_0x001a:
            if (r10 == 0) goto L_0x002a
            java.lang.String r1 = "false"
            boolean r2 = r10.hasKey(r1)
            if (r2 != 0) goto L_0x0025
            goto L_0x002a
        L_0x0025:
            int r10 = r10.getInt(r1)
            goto L_0x0034
        L_0x002a:
            android.content.Context r10 = r9.getContext()
            java.lang.String r1 = "colorPrimaryDark"
            int r10 = getThemeColor(r10, r1)
        L_0x0034:
            android.content.res.ColorStateList r1 = new android.content.res.ColorStateList
            r2 = 2
            int[][] r3 = new int[r2][]
            r4 = 1
            int[] r5 = new int[r4]
            r6 = 16842912(0x10100a0, float:2.3694006E-38)
            r7 = 0
            r5[r7] = r6
            r3[r7] = r5
            int[] r5 = new int[r4]
            r6 = -16842912(0xfffffffffefeff60, float:-1.6947495E38)
            r5[r7] = r6
            r3[r4] = r5
            int[] r2 = new int[r2]
            r2[r7] = r0
            r2[r4] = r10
            r1.<init>(r3, r2)
            androidx.core.widget.CompoundButtonCompat.setButtonTintList(r9, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.checkbox.ReactCheckBoxManager.setTintColors(com.facebook.react.views.checkbox.ReactCheckBox, com.facebook.react.bridge.ReadableMap):void");
    }
}
