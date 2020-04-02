package com.facebook.react.views.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.uimanager.ViewProps;

public class ReactDrawableHelper {
    private static final TypedValue sResolveOutValue = new TypedValue();

    @TargetApi(21)
    public static Drawable createDrawableFromJSDescription(Context context, ReadableMap readableMap) {
        int i;
        Drawable drawable;
        String string = readableMap.getString(ReactVideoViewManager.PROP_SRC_TYPE);
        if ("ThemeAttrAndroid".equals(string)) {
            String string2 = readableMap.getString("attribute");
            SoftAssertions.assertNotNull(string2);
            int identifier = context.getResources().getIdentifier(string2, "attr", "android");
            String str = "Attribute ";
            if (identifier == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(string2);
                sb.append(" couldn't be found in the resource list");
                throw new JSApplicationIllegalArgumentException(sb.toString());
            } else if (!context.getTheme().resolveAttribute(identifier, sResolveOutValue, true)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(string2);
                sb2.append(" couldn't be resolved into a drawable");
                throw new JSApplicationIllegalArgumentException(sb2.toString());
            } else if (VERSION.SDK_INT >= 21) {
                return context.getResources().getDrawable(sResolveOutValue.resourceId, context.getTheme());
            } else {
                return context.getResources().getDrawable(sResolveOutValue.resourceId);
            }
        } else if (!"RippleAndroid".equals(string)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Invalid type for android drawable: ");
            sb3.append(string);
            throw new JSApplicationIllegalArgumentException(sb3.toString());
        } else if (VERSION.SDK_INT >= 21) {
            String str2 = ViewProps.COLOR;
            if (readableMap.hasKey(str2) && !readableMap.isNull(str2)) {
                i = readableMap.getInt(str2);
            } else if (context.getTheme().resolveAttribute(16843820, sResolveOutValue, true)) {
                i = context.getResources().getColor(sResolveOutValue.resourceId);
            } else {
                throw new JSApplicationIllegalArgumentException("Attribute colorControlHighlight couldn't be resolved into a drawable");
            }
            String str3 = "borderless";
            if (!readableMap.hasKey(str3) || readableMap.isNull(str3) || !readableMap.getBoolean(str3)) {
                drawable = new ColorDrawable(-1);
            } else {
                drawable = null;
            }
            return new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{i}), null, drawable);
        } else {
            throw new JSApplicationIllegalArgumentException("Ripple drawable is not available on android API <21");
        }
    }
}
