package com.airbnb.android.react.maps;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapCalloutManager extends ViewGroupManager<AirMapCallout> {
    public String getName() {
        return "AIRMapCallout";
    }

    public AirMapCallout createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapCallout(themedReactContext);
    }

    @ReactProp(defaultBoolean = false, name = "tooltip")
    public void setTooltip(AirMapCallout airMapCallout, boolean z) {
        airMapCallout.setTooltip(z);
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "onPress";
        return MapBuilder.m125of(str, MapBuilder.m125of("registrationName", str));
    }

    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }

    public void updateExtraData(AirMapCallout airMapCallout, Object obj) {
        Map map = (Map) obj;
        float floatValue = ((Float) map.get("width")).floatValue();
        float floatValue2 = ((Float) map.get("height")).floatValue();
        airMapCallout.width = (int) floatValue;
        airMapCallout.height = (int) floatValue2;
    }
}
