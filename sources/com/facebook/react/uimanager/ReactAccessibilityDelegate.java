package com.facebook.react.uimanager;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import com.facebook.react.C0604R;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import java.util.HashMap;

public class ReactAccessibilityDelegate extends AccessibilityDelegateCompat {
    private static final String STATE_CHECKED = "checked";
    private static final String STATE_DISABLED = "disabled";
    private static final String STATE_SELECTED = "selected";
    private static final String TAG = "ReactAccessibilityDelegate";
    public static final HashMap<String, Integer> sActionIdMap = new HashMap<>();
    private static int sCounter = 1056964608;
    private final HashMap<Integer, String> mAccessibilityActionsMap = new HashMap<>();

    public enum AccessibilityRole {
        NONE,
        BUTTON,
        LINK,
        SEARCH,
        IMAGE,
        IMAGEBUTTON,
        KEYBOARDKEY,
        TEXT,
        ADJUSTABLE,
        SUMMARY,
        HEADER,
        ALERT,
        CHECKBOX,
        COMBOBOX,
        MENU,
        MENUBAR,
        MENUITEM,
        PROGRESSBAR,
        RADIO,
        RADIOGROUP,
        SCROLLBAR,
        SPINBUTTON,
        SWITCH,
        TAB,
        TABLIST,
        TIMER,
        TOOLBAR;

        public static String getValue(AccessibilityRole accessibilityRole) {
            switch (accessibilityRole) {
                case BUTTON:
                    return "android.widget.Button";
                case SEARCH:
                    return "android.widget.EditText";
                case IMAGE:
                    return "android.widget.ImageView";
                case IMAGEBUTTON:
                    return "android.widget.ImageButon";
                case KEYBOARDKEY:
                    return "android.inputmethodservice.Keyboard$Key";
                case TEXT:
                    return "android.widget.TextView";
                case ADJUSTABLE:
                    return "android.widget.SeekBar";
                case CHECKBOX:
                    return "android.widget.CheckBox";
                case RADIO:
                    return "android.widget.RadioButton";
                case SPINBUTTON:
                    return "android.widget.SpinButton";
                case SWITCH:
                    return "android.widget.Switch";
                case NONE:
                case LINK:
                case SUMMARY:
                case HEADER:
                case ALERT:
                case COMBOBOX:
                case MENU:
                case MENUBAR:
                case MENUITEM:
                case PROGRESSBAR:
                case RADIOGROUP:
                case SCROLLBAR:
                case TAB:
                case TABLIST:
                case TIMER:
                case TOOLBAR:
                    return "android.view.View";
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid accessibility role value: ");
                    sb.append(accessibilityRole);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public static AccessibilityRole fromValue(@Nullable String str) {
            AccessibilityRole[] values;
            for (AccessibilityRole accessibilityRole : values()) {
                if (accessibilityRole.name().equalsIgnoreCase(str)) {
                    return accessibilityRole;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid accessibility role value: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    static {
        sActionIdMap.put("activate", Integer.valueOf(AccessibilityActionCompat.ACTION_CLICK.getId()));
        sActionIdMap.put("longpress", Integer.valueOf(AccessibilityActionCompat.ACTION_LONG_CLICK.getId()));
        sActionIdMap.put("increment", Integer.valueOf(AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId()));
        sActionIdMap.put("decrement", Integer.valueOf(AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId()));
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        AccessibilityRole accessibilityRole = (AccessibilityRole) view.getTag(C0604R.C0606id.accessibility_role);
        if (accessibilityRole != null) {
            setRole(accessibilityNodeInfoCompat, accessibilityRole, view.getContext());
        }
        ReadableArray readableArray = (ReadableArray) view.getTag(C0604R.C0606id.accessibility_states);
        ReadableMap readableMap = (ReadableMap) view.getTag(C0604R.C0606id.accessibility_state);
        if (readableArray != null) {
            setStates(accessibilityNodeInfoCompat, readableArray, view.getContext());
        }
        if (readableMap != null) {
            setState(accessibilityNodeInfoCompat, readableMap, view.getContext());
        }
        ReadableArray readableArray2 = (ReadableArray) view.getTag(C0604R.C0606id.accessibility_actions);
        if (readableArray2 != null) {
            int i = 0;
            while (i < readableArray2.size()) {
                ReadableMap map = readableArray2.getMap(i);
                String str = "name";
                if (map.hasKey(str)) {
                    int i2 = sCounter;
                    String str2 = "label";
                    String string = map.hasKey(str2) ? map.getString(str2) : null;
                    if (sActionIdMap.containsKey(map.getString(str))) {
                        i2 = ((Integer) sActionIdMap.get(map.getString(str))).intValue();
                    } else {
                        sCounter++;
                    }
                    this.mAccessibilityActionsMap.put(Integer.valueOf(i2), map.getString(str));
                    accessibilityNodeInfoCompat.addAction(new AccessibilityActionCompat(i2, string));
                    i++;
                } else {
                    throw new IllegalArgumentException("Unknown accessibility action.");
                }
            }
        }
    }

    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (!this.mAccessibilityActionsMap.containsKey(Integer.valueOf(i))) {
            return super.performAccessibilityAction(view, i, bundle);
        }
        WritableMap createMap = Arguments.createMap();
        createMap.putString("actionName", (String) this.mAccessibilityActionsMap.get(Integer.valueOf(i)));
        ((RCTEventEmitter) ((ReactContext) view.getContext()).getJSModule(RCTEventEmitter.class)).receiveEvent(view.getId(), "topAccessibilityAction", createMap);
        return true;
    }

    private static void setStates(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ReadableArray readableArray, Context context) {
        for (int i = 0; i < readableArray.size(); i++) {
            String string = readableArray.getString(i);
            char c = 65535;
            switch (string.hashCode()) {
                case -1840852242:
                    if (string.equals("unchecked")) {
                        c = 3;
                        break;
                    }
                    break;
                case 270940796:
                    if (string.equals(STATE_DISABLED)) {
                        c = 1;
                        break;
                    }
                    break;
                case 742313895:
                    if (string.equals(STATE_CHECKED)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1191572123:
                    if (string.equals(STATE_SELECTED)) {
                        c = 0;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                accessibilityNodeInfoCompat.setSelected(true);
            } else if (c == 1) {
                accessibilityNodeInfoCompat.setEnabled(false);
            } else if (c == 2) {
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(true);
                if (accessibilityNodeInfoCompat.getClassName().equals(AccessibilityRole.getValue(AccessibilityRole.SWITCH))) {
                    accessibilityNodeInfoCompat.setText(context.getString(C0604R.string.state_on_description));
                }
            } else if (c == 3) {
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(false);
                if (accessibilityNodeInfoCompat.getClassName().equals(AccessibilityRole.getValue(AccessibilityRole.SWITCH))) {
                    accessibilityNodeInfoCompat.setText(context.getString(C0604R.string.state_off_description));
                }
            }
        }
    }

    private static void setState(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ReadableMap readableMap, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("setState ");
        sb.append(readableMap);
        Log.d(TAG, sb.toString());
        ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String nextKey = keySetIterator.nextKey();
            Dynamic dynamic = readableMap.getDynamic(nextKey);
            if (nextKey.equals(STATE_SELECTED) && dynamic.getType() == ReadableType.Boolean) {
                accessibilityNodeInfoCompat.setSelected(dynamic.asBoolean());
            } else if (nextKey.equals(STATE_DISABLED) && dynamic.getType() == ReadableType.Boolean) {
                accessibilityNodeInfoCompat.setEnabled(!dynamic.asBoolean());
            } else if (nextKey.equals(STATE_CHECKED) && dynamic.getType() == ReadableType.Boolean) {
                boolean asBoolean = dynamic.asBoolean();
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(asBoolean);
                if (accessibilityNodeInfoCompat.getClassName().equals(AccessibilityRole.getValue(AccessibilityRole.SWITCH))) {
                    accessibilityNodeInfoCompat.setText(context.getString(asBoolean ? C0604R.string.state_on_description : C0604R.string.state_off_description));
                }
            }
        }
    }

    public static void setRole(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityRole accessibilityRole, Context context) {
        if (accessibilityRole == null) {
            accessibilityRole = AccessibilityRole.NONE;
        }
        accessibilityNodeInfoCompat.setClassName(AccessibilityRole.getValue(accessibilityRole));
        if (accessibilityRole.equals(AccessibilityRole.LINK)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.link_description));
            String str = "";
            if (accessibilityNodeInfoCompat.getContentDescription() != null) {
                SpannableString spannableString = new SpannableString(accessibilityNodeInfoCompat.getContentDescription());
                spannableString.setSpan(new URLSpan(str), 0, spannableString.length(), 0);
                accessibilityNodeInfoCompat.setContentDescription(spannableString);
            }
            if (accessibilityNodeInfoCompat.getText() != null) {
                SpannableString spannableString2 = new SpannableString(accessibilityNodeInfoCompat.getText());
                spannableString2.setSpan(new URLSpan(str), 0, spannableString2.length(), 0);
                accessibilityNodeInfoCompat.setText(spannableString2);
            }
        } else if (accessibilityRole.equals(AccessibilityRole.SEARCH)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.search_description));
        } else if (accessibilityRole.equals(AccessibilityRole.IMAGE)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.image_description));
        } else if (accessibilityRole.equals(AccessibilityRole.IMAGEBUTTON)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.imagebutton_description));
            accessibilityNodeInfoCompat.setClickable(true);
        } else if (accessibilityRole.equals(AccessibilityRole.SUMMARY)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.summary_description));
        } else if (accessibilityRole.equals(AccessibilityRole.HEADER)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.header_description));
            accessibilityNodeInfoCompat.setCollectionItemInfo(CollectionItemInfoCompat.obtain(0, 1, 0, 1, true));
        } else if (accessibilityRole.equals(AccessibilityRole.ALERT)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.alert_description));
        } else if (accessibilityRole.equals(AccessibilityRole.COMBOBOX)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.combobox_description));
        } else if (accessibilityRole.equals(AccessibilityRole.MENU)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.menu_description));
        } else if (accessibilityRole.equals(AccessibilityRole.MENUBAR)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.menubar_description));
        } else if (accessibilityRole.equals(AccessibilityRole.MENUITEM)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.menuitem_description));
        } else if (accessibilityRole.equals(AccessibilityRole.PROGRESSBAR)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.progressbar_description));
        } else if (accessibilityRole.equals(AccessibilityRole.RADIOGROUP)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.radiogroup_description));
        } else if (accessibilityRole.equals(AccessibilityRole.SCROLLBAR)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.scrollbar_description));
        } else if (accessibilityRole.equals(AccessibilityRole.SPINBUTTON)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.spinbutton_description));
        } else if (accessibilityRole.equals(AccessibilityRole.TAB)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.rn_tab_description));
        } else if (accessibilityRole.equals(AccessibilityRole.TABLIST)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.tablist_description));
        } else if (accessibilityRole.equals(AccessibilityRole.TIMER)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.timer_description));
        } else if (accessibilityRole.equals(AccessibilityRole.TOOLBAR)) {
            accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0604R.string.toolbar_description));
        }
    }

    public static void setDelegate(View view) {
        if (ViewCompat.hasAccessibilityDelegate(view)) {
            return;
        }
        if (view.getTag(C0604R.C0606id.accessibility_role) != null || view.getTag(C0604R.C0606id.accessibility_states) != null || view.getTag(C0604R.C0606id.accessibility_state) != null || view.getTag(C0604R.C0606id.accessibility_actions) != null) {
            ViewCompat.setAccessibilityDelegate(view, new ReactAccessibilityDelegate());
        }
    }
}
