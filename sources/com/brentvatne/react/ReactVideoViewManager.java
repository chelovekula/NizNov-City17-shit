package com.brentvatne.react;

import com.brentvatne.react.ReactVideoView.Events;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.MapBuilder.Builder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.yqritc.scalablevideoview.ScalableType;
import java.util.Map;
import javax.annotation.Nullable;

public class ReactVideoViewManager extends SimpleViewManager<ReactVideoView> {
    public static final String PROP_CONTROLS = "controls";
    public static final String PROP_FULLSCREEN = "fullscreen";
    public static final String PROP_MUTED = "muted";
    public static final String PROP_PAUSED = "paused";
    public static final String PROP_PLAY_IN_BACKGROUND = "playInBackground";
    public static final String PROP_PROGRESS_UPDATE_INTERVAL = "progressUpdateInterval";
    public static final String PROP_RATE = "rate";
    public static final String PROP_REPEAT = "repeat";
    public static final String PROP_RESIZE_MODE = "resizeMode";
    public static final String PROP_SEEK = "seek";
    public static final String PROP_SRC = "src";
    public static final String PROP_SRC_HEADERS = "requestHeaders";
    public static final String PROP_SRC_IS_ASSET = "isAsset";
    public static final String PROP_SRC_IS_NETWORK = "isNetwork";
    public static final String PROP_SRC_MAINVER = "mainVer";
    public static final String PROP_SRC_PATCHVER = "patchVer";
    public static final String PROP_SRC_TYPE = "type";
    public static final String PROP_SRC_URI = "uri";
    public static final String PROP_STEREO_PAN = "stereoPan";
    public static final String PROP_VOLUME = "volume";
    public static final String REACT_CLASS = "RCTVideo";

    public String getName() {
        return REACT_CLASS;
    }

    /* access modifiers changed from: protected */
    public ReactVideoView createViewInstance(ThemedReactContext themedReactContext) {
        return new ReactVideoView(themedReactContext);
    }

    public void onDropViewInstance(ReactVideoView reactVideoView) {
        super.onDropViewInstance(reactVideoView);
        reactVideoView.cleanupMediaPlayerResources();
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Events[] values;
        Builder builder = MapBuilder.builder();
        for (Events events : Events.values()) {
            builder.put(events.toString(), MapBuilder.m125of("registrationName", events.toString()));
        }
        return builder.build();
    }

    @Nullable
    public Map getExportedViewConstants() {
        return MapBuilder.m128of("ScaleNone", Integer.toString(ScalableType.LEFT_TOP.ordinal()), "ScaleToFill", Integer.toString(ScalableType.FIT_XY.ordinal()), "ScaleAspectFit", Integer.toString(ScalableType.FIT_CENTER.ordinal()), "ScaleAspectFill", Integer.toString(ScalableType.CENTER_CROP.ordinal()));
    }

    @ReactProp(name = "src")
    public void setSrc(ReactVideoView reactVideoView, @Nullable ReadableMap readableMap) {
        ReadableMap readableMap2 = readableMap;
        int i = readableMap2.getInt(PROP_SRC_MAINVER);
        int i2 = readableMap2.getInt(PROP_SRC_PATCHVER);
        int i3 = i < 0 ? 0 : i;
        int i4 = i2 < 0 ? 0 : i2;
        String str = PROP_SRC_HEADERS;
        String str2 = PROP_SRC_IS_ASSET;
        String str3 = PROP_SRC_IS_NETWORK;
        String str4 = PROP_SRC_TYPE;
        String str5 = "uri";
        if (i3 > 0) {
            reactVideoView.setSrc(readableMap2.getString(str5), readableMap2.getString(str4), readableMap2.getBoolean(str3), readableMap2.getBoolean(str2), readableMap2.getMap(str), i3, i4);
        } else {
            reactVideoView.setSrc(readableMap2.getString(str5), readableMap2.getString(str4), readableMap2.getBoolean(str3), readableMap2.getBoolean(str2), readableMap2.getMap(str));
        }
    }

    @ReactProp(name = "resizeMode")
    public void setResizeMode(ReactVideoView reactVideoView, String str) {
        reactVideoView.setResizeModeModifier(ScalableType.values()[Integer.parseInt(str)]);
    }

    @ReactProp(defaultBoolean = false, name = "repeat")
    public void setRepeat(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setRepeatModifier(z);
    }

    @ReactProp(defaultBoolean = false, name = "paused")
    public void setPaused(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setPausedModifier(z);
    }

    @ReactProp(defaultBoolean = false, name = "muted")
    public void setMuted(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setMutedModifier(z);
    }

    @ReactProp(defaultFloat = 1.0f, name = "volume")
    public void setVolume(ReactVideoView reactVideoView, float f) {
        reactVideoView.setVolumeModifier(f);
    }

    @ReactProp(name = "stereoPan")
    public void setStereoPan(ReactVideoView reactVideoView, float f) {
        reactVideoView.setStereoPan(f);
    }

    @ReactProp(defaultFloat = 250.0f, name = "progressUpdateInterval")
    public void setProgressUpdateInterval(ReactVideoView reactVideoView, float f) {
        reactVideoView.setProgressUpdateInterval(f);
    }

    @ReactProp(name = "seek")
    public void setSeek(ReactVideoView reactVideoView, float f) {
        reactVideoView.seekTo(Math.round(f * 1000.0f));
    }

    @ReactProp(name = "rate")
    public void setRate(ReactVideoView reactVideoView, float f) {
        reactVideoView.setRateModifier(f);
    }

    @ReactProp(defaultBoolean = false, name = "fullscreen")
    public void setFullscreen(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setFullscreen(z);
    }

    @ReactProp(defaultBoolean = false, name = "playInBackground")
    public void setPlayInBackground(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setPlayInBackground(z);
    }

    @ReactProp(defaultBoolean = false, name = "controls")
    public void setControls(ReactVideoView reactVideoView, boolean z) {
        reactVideoView.setControls(z);
    }
}
