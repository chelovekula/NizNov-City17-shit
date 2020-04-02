package com.swmansion.gesturehandler.react;

import androidx.annotation.Nullable;
import androidx.core.util.Pools.SynchronizedPool;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.swmansion.gesturehandler.GestureHandler;

public class RNGestureHandlerStateChangeEvent extends Event<RNGestureHandlerStateChangeEvent> {
    private static final SynchronizedPool<RNGestureHandlerStateChangeEvent> EVENTS_POOL = new SynchronizedPool<>(7);
    public static final String EVENT_NAME = "onGestureHandlerStateChange";
    private static final int TOUCH_EVENTS_POOL_SIZE = 7;
    private WritableMap mExtraData;

    public boolean canCoalesce() {
        return false;
    }

    public short getCoalescingKey() {
        return 0;
    }

    public String getEventName() {
        return EVENT_NAME;
    }

    public static RNGestureHandlerStateChangeEvent obtain(GestureHandler gestureHandler, int i, int i2, @Nullable RNGestureHandlerEventDataExtractor rNGestureHandlerEventDataExtractor) {
        RNGestureHandlerStateChangeEvent rNGestureHandlerStateChangeEvent = (RNGestureHandlerStateChangeEvent) EVENTS_POOL.acquire();
        if (rNGestureHandlerStateChangeEvent == null) {
            rNGestureHandlerStateChangeEvent = new RNGestureHandlerStateChangeEvent();
        }
        rNGestureHandlerStateChangeEvent.init(gestureHandler, i, i2, rNGestureHandlerEventDataExtractor);
        return rNGestureHandlerStateChangeEvent;
    }

    private RNGestureHandlerStateChangeEvent() {
    }

    private void init(GestureHandler gestureHandler, int i, int i2, @Nullable RNGestureHandlerEventDataExtractor rNGestureHandlerEventDataExtractor) {
        super.init(gestureHandler.getView().getId());
        this.mExtraData = Arguments.createMap();
        if (rNGestureHandlerEventDataExtractor != null) {
            rNGestureHandlerEventDataExtractor.extractEventData(gestureHandler, this.mExtraData);
        }
        this.mExtraData.putInt("handlerTag", gestureHandler.getTag());
        this.mExtraData.putInt("state", i);
        this.mExtraData.putInt("oldState", i2);
    }

    public void onDispose() {
        this.mExtraData = null;
        EVENTS_POOL.release(this);
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), EVENT_NAME, this.mExtraData);
    }
}
