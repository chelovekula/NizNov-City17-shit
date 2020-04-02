package com.facebook.react.uimanager;

import android.widget.ImageView.ScaleType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.events.TouchEventType;
import com.facebook.react.views.picker.events.PickerItemSelectEvent;
import com.reactnativecommunity.webview.events.TopLoadingErrorEvent;
import com.reactnativecommunity.webview.events.TopLoadingFinishEvent;
import com.reactnativecommunity.webview.events.TopLoadingStartEvent;
import com.reactnativecommunity.webview.events.TopMessageEvent;
import java.util.HashMap;
import java.util.Map;

class UIManagerModuleConstants {
    public static final String ACTION_DISMISSED = "dismissed";
    public static final String ACTION_ITEM_SELECTED = "itemSelected";

    UIManagerModuleConstants() {
    }

    static Map getBubblingEventTypeConstants() {
        String str = "captured";
        String str2 = "bubbled";
        String str3 = "phasedRegistrationNames";
        String str4 = "topChange";
        return MapBuilder.builder().put(str4, MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onChange", str, "onChangeCapture"))).put(PickerItemSelectEvent.EVENT_NAME, MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onSelect", str, "onSelectCapture"))).put(TouchEventType.getJSEventName(TouchEventType.START), MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onTouchStart", str, "onTouchStartCapture"))).put(TouchEventType.getJSEventName(TouchEventType.MOVE), MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onTouchMove", str, "onTouchMoveCapture"))).put(TouchEventType.getJSEventName(TouchEventType.END), MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onTouchEnd", str, "onTouchEndCapture"))).put(TouchEventType.getJSEventName(TouchEventType.CANCEL), MapBuilder.m125of(str3, MapBuilder.m126of(str2, "onTouchCancel", str, "onTouchCancelCapture"))).build();
    }

    static Map getDirectEventTypeConstants() {
        String str = "registrationName";
        String str2 = "topLayout";
        String str3 = "topSelectionChange";
        String str4 = "topClick";
        String str5 = "topScrollBeginDrag";
        String str6 = "topScrollEndDrag";
        String str7 = "topScroll";
        String str8 = "topMomentumScrollBegin";
        return MapBuilder.builder().put("topContentSizeChange", MapBuilder.m125of(str, "onContentSizeChange")).put(str2, MapBuilder.m125of(str, ViewProps.ON_LAYOUT)).put(TopLoadingErrorEvent.EVENT_NAME, MapBuilder.m125of(str, "onLoadingError")).put(TopLoadingFinishEvent.EVENT_NAME, MapBuilder.m125of(str, "onLoadingFinish")).put(TopLoadingStartEvent.EVENT_NAME, MapBuilder.m125of(str, "onLoadingStart")).put(str3, MapBuilder.m125of(str, "onSelectionChange")).put(TopMessageEvent.EVENT_NAME, MapBuilder.m125of(str, "onMessage")).put(str4, MapBuilder.m125of(str, "onClick")).put(str5, MapBuilder.m125of(str, "onScrollBeginDrag")).put(str6, MapBuilder.m125of(str, "onScrollEndDrag")).put(str7, MapBuilder.m125of(str, "onScroll")).put(str8, MapBuilder.m125of(str, "onMomentumScrollBegin")).put("topMomentumScrollEnd", MapBuilder.m125of(str, "onMomentumScrollEnd")).build();
    }

    public static Map<String, Object> getConstants() {
        HashMap newHashMap = MapBuilder.newHashMap();
        newHashMap.put("UIView", MapBuilder.m125of("ContentMode", MapBuilder.m127of("ScaleAspectFit", Integer.valueOf(ScaleType.FIT_CENTER.ordinal()), "ScaleAspectFill", Integer.valueOf(ScaleType.CENTER_CROP.ordinal()), "ScaleAspectCenter", Integer.valueOf(ScaleType.CENTER_INSIDE.ordinal()))));
        newHashMap.put("StyleConstants", MapBuilder.m125of("PointerEventsValues", MapBuilder.m128of(ViewProps.NONE, Integer.valueOf(PointerEvents.NONE.ordinal()), "boxNone", Integer.valueOf(PointerEvents.BOX_NONE.ordinal()), "boxOnly", Integer.valueOf(PointerEvents.BOX_ONLY.ordinal()), "unspecified", Integer.valueOf(PointerEvents.AUTO.ordinal()))));
        String str = ACTION_ITEM_SELECTED;
        String str2 = ACTION_DISMISSED;
        newHashMap.put("PopupMenu", MapBuilder.m126of(str2, str2, str, str));
        newHashMap.put("AccessibilityEventTypes", MapBuilder.m127of("typeWindowStateChanged", Integer.valueOf(32), "typeViewFocused", Integer.valueOf(8), "typeViewClicked", Integer.valueOf(1)));
        return newHashMap;
    }
}
