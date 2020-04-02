package com.swmansion.gesturehandler.react;

import androidx.annotation.Nullable;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import java.util.Map;

@ReactModule(name = "GestureHandlerRootView")
public class RNGestureHandlerRootViewManager extends ViewGroupManager<RNGestureHandlerRootView> {
    public static final String REACT_CLASS = "GestureHandlerRootView";

    public String getName() {
        return REACT_CLASS;
    }

    /* access modifiers changed from: protected */
    public RNGestureHandlerRootView createViewInstance(ThemedReactContext themedReactContext) {
        return new RNGestureHandlerRootView(themedReactContext);
    }

    public void onDropViewInstance(RNGestureHandlerRootView rNGestureHandlerRootView) {
        rNGestureHandlerRootView.tearDown();
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "registrationName";
        String str2 = RNGestureHandlerEvent.EVENT_NAME;
        Map of = MapBuilder.m125of(str, str2);
        String str3 = RNGestureHandlerStateChangeEvent.EVENT_NAME;
        return MapBuilder.m126of(str2, of, str3, MapBuilder.m125of(str, str3));
    }
}
