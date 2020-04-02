package com.reactnativecommunity.viewpager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

class PageScrollEvent extends Event<PageScrollEvent> {
    public static final String EVENT_NAME = "topPageScroll";
    private final float mOffset;
    private final int mPosition;

    public String getEventName() {
        return "topPageScroll";
    }

    protected PageScrollEvent(int i, int i2, float f) {
        super(i);
        this.mPosition = i2;
        if (Float.isInfinite(f) || Float.isNaN(f)) {
            f = 0.0f;
        }
        this.mOffset = f;
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt(ViewProps.POSITION, this.mPosition);
        createMap.putDouble("offset", (double) this.mOffset);
        return createMap;
    }
}
