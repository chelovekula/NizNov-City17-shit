package com.swmansion.gesturehandler.react;

import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.swmansion.gesturehandler.FlingGestureHandler;
import com.swmansion.gesturehandler.GestureHandler;
import com.swmansion.gesturehandler.LongPressGestureHandler;
import com.swmansion.gesturehandler.NativeViewGestureHandler;
import com.swmansion.gesturehandler.OnTouchEventListener;
import com.swmansion.gesturehandler.PanGestureHandler;
import com.swmansion.gesturehandler.PinchGestureHandler;
import com.swmansion.gesturehandler.RotationGestureHandler;
import com.swmansion.gesturehandler.TapGestureHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ReactModule(name = "RNGestureHandlerModule")
public class RNGestureHandlerModule extends ReactContextBaseJavaModule {
    private static final String KEY_DIRECTION = "direction";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_HIT_SLOP = "hitSlop";
    private static final String KEY_HIT_SLOP_BOTTOM = "bottom";
    private static final String KEY_HIT_SLOP_HEIGHT = "height";
    private static final String KEY_HIT_SLOP_HORIZONTAL = "horizontal";
    private static final String KEY_HIT_SLOP_LEFT = "left";
    private static final String KEY_HIT_SLOP_RIGHT = "right";
    private static final String KEY_HIT_SLOP_TOP = "top";
    private static final String KEY_HIT_SLOP_VERTICAL = "vertical";
    private static final String KEY_HIT_SLOP_WIDTH = "width";
    private static final String KEY_LONG_PRESS_MAX_DIST = "maxDist";
    private static final String KEY_LONG_PRESS_MIN_DURATION_MS = "minDurationMs";
    private static final String KEY_NATIVE_VIEW_DISALLOW_INTERRUPTION = "disallowInterruption";
    private static final String KEY_NATIVE_VIEW_SHOULD_ACTIVATE_ON_START = "shouldActivateOnStart";
    private static final String KEY_NUMBER_OF_POINTERS = "numberOfPointers";
    private static final String KEY_PAN_ACTIVE_OFFSET_X_END = "activeOffsetXEnd";
    private static final String KEY_PAN_ACTIVE_OFFSET_X_START = "activeOffsetXStart";
    private static final String KEY_PAN_ACTIVE_OFFSET_Y_END = "activeOffsetYEnd";
    private static final String KEY_PAN_ACTIVE_OFFSET_Y_START = "activeOffsetYStart";
    private static final String KEY_PAN_AVG_TOUCHES = "avgTouches";
    private static final String KEY_PAN_FAIL_OFFSET_RANGE_X_END = "failOffsetXEnd";
    private static final String KEY_PAN_FAIL_OFFSET_RANGE_X_START = "failOffsetXStart";
    private static final String KEY_PAN_FAIL_OFFSET_RANGE_Y_END = "failOffsetYEnd";
    private static final String KEY_PAN_FAIL_OFFSET_RANGE_Y_START = "failOffsetYStart";
    private static final String KEY_PAN_MAX_POINTERS = "maxPointers";
    private static final String KEY_PAN_MIN_DIST = "minDist";
    private static final String KEY_PAN_MIN_POINTERS = "minPointers";
    private static final String KEY_PAN_MIN_VELOCITY = "minVelocity";
    private static final String KEY_PAN_MIN_VELOCITY_X = "minVelocityX";
    private static final String KEY_PAN_MIN_VELOCITY_Y = "minVelocityY";
    private static final String KEY_SHOULD_CANCEL_WHEN_OUTSIDE = "shouldCancelWhenOutside";
    private static final String KEY_TAP_MAX_DELAY_MS = "maxDelayMs";
    private static final String KEY_TAP_MAX_DELTA_X = "maxDeltaX";
    private static final String KEY_TAP_MAX_DELTA_Y = "maxDeltaY";
    private static final String KEY_TAP_MAX_DIST = "maxDist";
    private static final String KEY_TAP_MAX_DURATION_MS = "maxDurationMs";
    private static final String KEY_TAP_MIN_POINTERS = "minPointers";
    private static final String KEY_TAP_NUMBER_OF_TAPS = "numberOfTaps";
    public static final String MODULE_NAME = "RNGestureHandlerModule";
    /* access modifiers changed from: private */
    public List<Integer> mEnqueuedRootViewInit = new ArrayList();
    private OnTouchEventListener mEventListener = new OnTouchEventListener() {
        public void onTouchEvent(GestureHandler gestureHandler, MotionEvent motionEvent) {
            RNGestureHandlerModule.this.onTouchEvent(gestureHandler, motionEvent);
        }

        public void onStateChange(GestureHandler gestureHandler, int i, int i2) {
            RNGestureHandlerModule.this.onStateChange(gestureHandler, i, i2);
        }
    };
    private HandlerFactory[] mHandlerFactories = {new NativeViewGestureHandlerFactory(), new TapGestureHandlerFactory(), new LongPressGestureHandlerFactory(), new PanGestureHandlerFactory(), new PinchGestureHandlerFactory(), new RotationGestureHandlerFactory(), new FlingGestureHandlerFactory()};
    private RNGestureHandlerInteractionManager mInteractionManager = new RNGestureHandlerInteractionManager();
    private final RNGestureHandlerRegistry mRegistry = new RNGestureHandlerRegistry();
    private List<RNGestureHandlerRootHelper> mRoots = new ArrayList();

    private static class FlingGestureHandlerFactory extends HandlerFactory<FlingGestureHandler> {
        public String getName() {
            return "FlingGestureHandler";
        }

        private FlingGestureHandlerFactory() {
            super();
        }

        public Class<FlingGestureHandler> getType() {
            return FlingGestureHandler.class;
        }

        public FlingGestureHandler create(Context context) {
            return new FlingGestureHandler();
        }

        public void configure(FlingGestureHandler flingGestureHandler, ReadableMap readableMap) {
            super.configure(flingGestureHandler, readableMap);
            String str = RNGestureHandlerModule.KEY_NUMBER_OF_POINTERS;
            if (readableMap.hasKey(str)) {
                flingGestureHandler.setNumberOfPointersRequired(readableMap.getInt(str));
            }
            String str2 = RNGestureHandlerModule.KEY_DIRECTION;
            if (readableMap.hasKey(str2)) {
                flingGestureHandler.setDirection(readableMap.getInt(str2));
            }
        }

        public void extractEventData(FlingGestureHandler flingGestureHandler, WritableMap writableMap) {
            super.extractEventData(flingGestureHandler, writableMap);
            writableMap.putDouble("x", (double) PixelUtil.toDIPFromPixel(flingGestureHandler.getLastRelativePositionX()));
            writableMap.putDouble("y", (double) PixelUtil.toDIPFromPixel(flingGestureHandler.getLastRelativePositionY()));
            writableMap.putDouble("absoluteX", (double) PixelUtil.toDIPFromPixel(flingGestureHandler.getLastAbsolutePositionX()));
            writableMap.putDouble("absoluteY", (double) PixelUtil.toDIPFromPixel(flingGestureHandler.getLastAbsolutePositionY()));
        }
    }

    private static abstract class HandlerFactory<T extends GestureHandler> implements RNGestureHandlerEventDataExtractor<T> {
        public abstract T create(Context context);

        public abstract String getName();

        public abstract Class<T> getType();

        private HandlerFactory() {
        }

        public void configure(T t, ReadableMap readableMap) {
            String str = RNGestureHandlerModule.KEY_SHOULD_CANCEL_WHEN_OUTSIDE;
            if (readableMap.hasKey(str)) {
                t.setShouldCancelWhenOutside(readableMap.getBoolean(str));
            }
            String str2 = "enabled";
            if (readableMap.hasKey(str2)) {
                t.setEnabled(readableMap.getBoolean(str2));
            }
            if (readableMap.hasKey(RNGestureHandlerModule.KEY_HIT_SLOP)) {
                RNGestureHandlerModule.handleHitSlopProperty(t, readableMap);
            }
        }

        public void extractEventData(T t, WritableMap writableMap) {
            writableMap.putDouble(RNGestureHandlerModule.KEY_NUMBER_OF_POINTERS, (double) t.getNumberOfPointers());
        }
    }

    private static class LongPressGestureHandlerFactory extends HandlerFactory<LongPressGestureHandler> {
        public String getName() {
            return "LongPressGestureHandler";
        }

        private LongPressGestureHandlerFactory() {
            super();
        }

        public Class<LongPressGestureHandler> getType() {
            return LongPressGestureHandler.class;
        }

        public LongPressGestureHandler create(Context context) {
            return new LongPressGestureHandler(context);
        }

        public void configure(LongPressGestureHandler longPressGestureHandler, ReadableMap readableMap) {
            super.configure(longPressGestureHandler, readableMap);
            String str = RNGestureHandlerModule.KEY_LONG_PRESS_MIN_DURATION_MS;
            if (readableMap.hasKey(str)) {
                longPressGestureHandler.setMinDurationMs((long) readableMap.getInt(str));
            }
            String str2 = "maxDist";
            if (readableMap.hasKey(str2)) {
                longPressGestureHandler.setMaxDist(PixelUtil.toPixelFromDIP(readableMap.getDouble(str2)));
            }
        }

        public void extractEventData(LongPressGestureHandler longPressGestureHandler, WritableMap writableMap) {
            super.extractEventData(longPressGestureHandler, writableMap);
            writableMap.putDouble("x", (double) PixelUtil.toDIPFromPixel(longPressGestureHandler.getLastRelativePositionX()));
            writableMap.putDouble("y", (double) PixelUtil.toDIPFromPixel(longPressGestureHandler.getLastRelativePositionY()));
            writableMap.putDouble("absoluteX", (double) PixelUtil.toDIPFromPixel(longPressGestureHandler.getLastAbsolutePositionX()));
            writableMap.putDouble("absoluteY", (double) PixelUtil.toDIPFromPixel(longPressGestureHandler.getLastAbsolutePositionY()));
        }
    }

    private static class NativeViewGestureHandlerFactory extends HandlerFactory<NativeViewGestureHandler> {
        public String getName() {
            return "NativeViewGestureHandler";
        }

        private NativeViewGestureHandlerFactory() {
            super();
        }

        public Class<NativeViewGestureHandler> getType() {
            return NativeViewGestureHandler.class;
        }

        public NativeViewGestureHandler create(Context context) {
            return new NativeViewGestureHandler();
        }

        public void configure(NativeViewGestureHandler nativeViewGestureHandler, ReadableMap readableMap) {
            super.configure(nativeViewGestureHandler, readableMap);
            String str = RNGestureHandlerModule.KEY_NATIVE_VIEW_SHOULD_ACTIVATE_ON_START;
            if (readableMap.hasKey(str)) {
                nativeViewGestureHandler.setShouldActivateOnStart(readableMap.getBoolean(str));
            }
            String str2 = RNGestureHandlerModule.KEY_NATIVE_VIEW_DISALLOW_INTERRUPTION;
            if (readableMap.hasKey(str2)) {
                nativeViewGestureHandler.setDisallowInterruption(readableMap.getBoolean(str2));
            }
        }

        public void extractEventData(NativeViewGestureHandler nativeViewGestureHandler, WritableMap writableMap) {
            super.extractEventData(nativeViewGestureHandler, writableMap);
            writableMap.putBoolean("pointerInside", nativeViewGestureHandler.isWithinBounds());
        }
    }

    private static class PanGestureHandlerFactory extends HandlerFactory<PanGestureHandler> {
        public String getName() {
            return "PanGestureHandler";
        }

        private PanGestureHandlerFactory() {
            super();
        }

        public Class<PanGestureHandler> getType() {
            return PanGestureHandler.class;
        }

        public PanGestureHandler create(Context context) {
            return new PanGestureHandler(context);
        }

        public void configure(PanGestureHandler panGestureHandler, ReadableMap readableMap) {
            boolean z;
            super.configure(panGestureHandler, readableMap);
            String str = RNGestureHandlerModule.KEY_PAN_ACTIVE_OFFSET_X_START;
            if (readableMap.hasKey(str)) {
                panGestureHandler.setActiveOffsetXStart(PixelUtil.toPixelFromDIP(readableMap.getDouble(str)));
                z = true;
            } else {
                z = false;
            }
            String str2 = RNGestureHandlerModule.KEY_PAN_ACTIVE_OFFSET_X_END;
            if (readableMap.hasKey(str2)) {
                panGestureHandler.setActiveOffsetXEnd(PixelUtil.toPixelFromDIP(readableMap.getDouble(str2)));
                z = true;
            }
            String str3 = RNGestureHandlerModule.KEY_PAN_FAIL_OFFSET_RANGE_X_START;
            if (readableMap.hasKey(str3)) {
                panGestureHandler.setFailOffsetXStart(PixelUtil.toPixelFromDIP(readableMap.getDouble(str3)));
                z = true;
            }
            String str4 = RNGestureHandlerModule.KEY_PAN_FAIL_OFFSET_RANGE_X_END;
            if (readableMap.hasKey(str4)) {
                panGestureHandler.setFailOffsetXEnd(PixelUtil.toPixelFromDIP(readableMap.getDouble(str4)));
                z = true;
            }
            String str5 = RNGestureHandlerModule.KEY_PAN_ACTIVE_OFFSET_Y_START;
            if (readableMap.hasKey(str5)) {
                panGestureHandler.setActiveOffsetYStart(PixelUtil.toPixelFromDIP(readableMap.getDouble(str5)));
                z = true;
            }
            String str6 = RNGestureHandlerModule.KEY_PAN_ACTIVE_OFFSET_Y_END;
            if (readableMap.hasKey(str6)) {
                panGestureHandler.setActiveOffsetYEnd(PixelUtil.toPixelFromDIP(readableMap.getDouble(str6)));
                z = true;
            }
            String str7 = RNGestureHandlerModule.KEY_PAN_FAIL_OFFSET_RANGE_Y_START;
            if (readableMap.hasKey(str7)) {
                panGestureHandler.setFailOffsetYStart(PixelUtil.toPixelFromDIP(readableMap.getDouble(str7)));
                z = true;
            }
            String str8 = RNGestureHandlerModule.KEY_PAN_FAIL_OFFSET_RANGE_Y_END;
            if (readableMap.hasKey(str8)) {
                panGestureHandler.setFailOffsetYEnd(PixelUtil.toPixelFromDIP(readableMap.getDouble(str8)));
                z = true;
            }
            String str9 = RNGestureHandlerModule.KEY_PAN_MIN_VELOCITY;
            if (readableMap.hasKey(str9)) {
                panGestureHandler.setMinVelocity(PixelUtil.toPixelFromDIP(readableMap.getDouble(str9)));
                z = true;
            }
            String str10 = RNGestureHandlerModule.KEY_PAN_MIN_VELOCITY_X;
            if (readableMap.hasKey(str10)) {
                panGestureHandler.setMinVelocityX(PixelUtil.toPixelFromDIP(readableMap.getDouble(str10)));
                z = true;
            }
            String str11 = RNGestureHandlerModule.KEY_PAN_MIN_VELOCITY_Y;
            if (readableMap.hasKey(str11)) {
                panGestureHandler.setMinVelocityY(PixelUtil.toPixelFromDIP(readableMap.getDouble(str11)));
                z = true;
            }
            String str12 = RNGestureHandlerModule.KEY_PAN_MIN_DIST;
            if (readableMap.hasKey(str12)) {
                panGestureHandler.setMinDist(PixelUtil.toPixelFromDIP(readableMap.getDouble(str12)));
            } else if (z) {
                panGestureHandler.setMinDist(Float.MAX_VALUE);
            }
            String str13 = "minPointers";
            if (readableMap.hasKey(str13)) {
                panGestureHandler.setMinPointers(readableMap.getInt(str13));
            }
            String str14 = RNGestureHandlerModule.KEY_PAN_MAX_POINTERS;
            if (readableMap.hasKey(str14)) {
                panGestureHandler.setMaxPointers(readableMap.getInt(str14));
            }
            if (readableMap.hasKey(RNGestureHandlerModule.KEY_PAN_AVG_TOUCHES)) {
                panGestureHandler.setAverageTouches(readableMap.getBoolean(RNGestureHandlerModule.KEY_PAN_AVG_TOUCHES));
            }
        }

        public void extractEventData(PanGestureHandler panGestureHandler, WritableMap writableMap) {
            super.extractEventData(panGestureHandler, writableMap);
            writableMap.putDouble("x", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getLastRelativePositionX()));
            writableMap.putDouble("y", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getLastRelativePositionY()));
            writableMap.putDouble("absoluteX", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getLastAbsolutePositionX()));
            writableMap.putDouble("absoluteY", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getLastAbsolutePositionY()));
            writableMap.putDouble("translationX", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getTranslationX()));
            writableMap.putDouble("translationY", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getTranslationY()));
            writableMap.putDouble("velocityX", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getVelocityX()));
            writableMap.putDouble("velocityY", (double) PixelUtil.toDIPFromPixel(panGestureHandler.getVelocityY()));
        }
    }

    private static class PinchGestureHandlerFactory extends HandlerFactory<PinchGestureHandler> {
        public String getName() {
            return "PinchGestureHandler";
        }

        private PinchGestureHandlerFactory() {
            super();
        }

        public Class<PinchGestureHandler> getType() {
            return PinchGestureHandler.class;
        }

        public PinchGestureHandler create(Context context) {
            return new PinchGestureHandler();
        }

        public void extractEventData(PinchGestureHandler pinchGestureHandler, WritableMap writableMap) {
            super.extractEventData(pinchGestureHandler, writableMap);
            writableMap.putDouble("scale", pinchGestureHandler.getScale());
            writableMap.putDouble("focalX", (double) PixelUtil.toDIPFromPixel(pinchGestureHandler.getFocalPointX()));
            writableMap.putDouble("focalY", (double) PixelUtil.toDIPFromPixel(pinchGestureHandler.getFocalPointY()));
            writableMap.putDouble("velocity", pinchGestureHandler.getVelocity());
        }
    }

    private static class RotationGestureHandlerFactory extends HandlerFactory<RotationGestureHandler> {
        public String getName() {
            return "RotationGestureHandler";
        }

        private RotationGestureHandlerFactory() {
            super();
        }

        public Class<RotationGestureHandler> getType() {
            return RotationGestureHandler.class;
        }

        public RotationGestureHandler create(Context context) {
            return new RotationGestureHandler();
        }

        public void extractEventData(RotationGestureHandler rotationGestureHandler, WritableMap writableMap) {
            super.extractEventData(rotationGestureHandler, writableMap);
            writableMap.putDouble(ViewProps.ROTATION, rotationGestureHandler.getRotation());
            writableMap.putDouble("anchorX", (double) PixelUtil.toDIPFromPixel(rotationGestureHandler.getAnchorX()));
            writableMap.putDouble("anchorY", (double) PixelUtil.toDIPFromPixel(rotationGestureHandler.getAnchorY()));
            writableMap.putDouble("velocity", rotationGestureHandler.getVelocity());
        }
    }

    private static class TapGestureHandlerFactory extends HandlerFactory<TapGestureHandler> {
        public String getName() {
            return "TapGestureHandler";
        }

        private TapGestureHandlerFactory() {
            super();
        }

        public Class<TapGestureHandler> getType() {
            return TapGestureHandler.class;
        }

        public TapGestureHandler create(Context context) {
            return new TapGestureHandler();
        }

        public void configure(TapGestureHandler tapGestureHandler, ReadableMap readableMap) {
            super.configure(tapGestureHandler, readableMap);
            String str = RNGestureHandlerModule.KEY_TAP_NUMBER_OF_TAPS;
            if (readableMap.hasKey(str)) {
                tapGestureHandler.setNumberOfTaps(readableMap.getInt(str));
            }
            String str2 = RNGestureHandlerModule.KEY_TAP_MAX_DURATION_MS;
            if (readableMap.hasKey(str2)) {
                tapGestureHandler.setMaxDurationMs((long) readableMap.getInt(str2));
            }
            String str3 = RNGestureHandlerModule.KEY_TAP_MAX_DELAY_MS;
            if (readableMap.hasKey(str3)) {
                tapGestureHandler.setMaxDelayMs((long) readableMap.getInt(str3));
            }
            String str4 = RNGestureHandlerModule.KEY_TAP_MAX_DELTA_X;
            if (readableMap.hasKey(str4)) {
                tapGestureHandler.setMaxDx(PixelUtil.toPixelFromDIP(readableMap.getDouble(str4)));
            }
            String str5 = RNGestureHandlerModule.KEY_TAP_MAX_DELTA_Y;
            if (readableMap.hasKey(str5)) {
                tapGestureHandler.setMaxDy(PixelUtil.toPixelFromDIP(readableMap.getDouble(str5)));
            }
            String str6 = "maxDist";
            if (readableMap.hasKey(str6)) {
                tapGestureHandler.setMaxDist(PixelUtil.toPixelFromDIP(readableMap.getDouble(str6)));
            }
            String str7 = "minPointers";
            if (readableMap.hasKey(str7)) {
                tapGestureHandler.setMinNumberOfPointers(readableMap.getInt(str7));
            }
        }

        public void extractEventData(TapGestureHandler tapGestureHandler, WritableMap writableMap) {
            super.extractEventData(tapGestureHandler, writableMap);
            writableMap.putDouble("x", (double) PixelUtil.toDIPFromPixel(tapGestureHandler.getLastRelativePositionX()));
            writableMap.putDouble("y", (double) PixelUtil.toDIPFromPixel(tapGestureHandler.getLastRelativePositionY()));
            writableMap.putDouble("absoluteX", (double) PixelUtil.toDIPFromPixel(tapGestureHandler.getLastAbsolutePositionX()));
            writableMap.putDouble("absoluteY", (double) PixelUtil.toDIPFromPixel(tapGestureHandler.getLastAbsolutePositionY()));
        }
    }

    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void handleClearJSResponder() {
    }

    public RNGestureHandlerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void createGestureHandler(String str, int i, ReadableMap readableMap) {
        int i2 = 0;
        while (true) {
            HandlerFactory[] handlerFactoryArr = this.mHandlerFactories;
            if (i2 < handlerFactoryArr.length) {
                HandlerFactory handlerFactory = handlerFactoryArr[i2];
                if (handlerFactory.getName().equals(str)) {
                    GestureHandler create = handlerFactory.create(getReactApplicationContext());
                    create.setTag(i);
                    create.setOnTouchEventListener(this.mEventListener);
                    this.mRegistry.registerHandler(create);
                    this.mInteractionManager.configureInteractions(create, readableMap);
                    handlerFactory.configure(create, readableMap);
                    return;
                }
                i2++;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid handler name ");
                sb.append(str);
                throw new JSApplicationIllegalArgumentException(sb.toString());
            }
        }
    }

    @ReactMethod
    public void attachGestureHandler(int i, int i2) {
        tryInitializeHandlerForReactRootView(i2);
        if (!this.mRegistry.attachHandlerToView(i, i2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Handler with tag ");
            sb.append(i);
            sb.append(" does not exists");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
    }

    @ReactMethod
    public void updateGestureHandler(int i, ReadableMap readableMap) {
        GestureHandler handler = this.mRegistry.getHandler(i);
        if (handler != null) {
            HandlerFactory findFactoryForHandler = findFactoryForHandler(handler);
            if (findFactoryForHandler != null) {
                this.mInteractionManager.dropRelationsForHandlerWithTag(i);
                this.mInteractionManager.configureInteractions(handler, readableMap);
                findFactoryForHandler.configure(handler, readableMap);
            }
        }
    }

    @ReactMethod
    public void dropGestureHandler(int i) {
        this.mInteractionManager.dropRelationsForHandlerWithTag(i);
        this.mRegistry.dropHandler(i);
    }

    @ReactMethod
    public void handleSetJSResponder(int i, boolean z) {
        if (this.mRegistry != null) {
            RNGestureHandlerRootHelper findRootHelperForViewAncestor = findRootHelperForViewAncestor(i);
            if (findRootHelperForViewAncestor != null) {
                findRootHelperForViewAncestor.handleSetJSResponder(i, z);
            }
        }
    }

    @Nullable
    public Map getConstants() {
        Integer valueOf = Integer.valueOf(0);
        Integer valueOf2 = Integer.valueOf(2);
        Integer valueOf3 = Integer.valueOf(4);
        Integer valueOf4 = Integer.valueOf(3);
        Integer valueOf5 = Integer.valueOf(1);
        Integer num = valueOf2;
        Integer num2 = valueOf3;
        Integer num3 = valueOf5;
        Map of = MapBuilder.m130of("UNDETERMINED", valueOf, "BEGAN", num, "ACTIVE", num2, "CANCELLED", valueOf4, "FAILED", num3, "END", Integer.valueOf(5));
        Integer num4 = valueOf5;
        Integer num5 = valueOf2;
        Integer num6 = valueOf3;
        return MapBuilder.m126of("State", of, "Direction", MapBuilder.m128of("RIGHT", num4, "LEFT", num5, "UP", num6, "DOWN", Integer.valueOf(8)));
    }

    public RNGestureHandlerRegistry getRegistry() {
        return this.mRegistry;
    }

    public void onCatalystInstanceDestroy() {
        this.mRegistry.dropAllHandlers();
        this.mInteractionManager.reset();
        synchronized (this.mRoots) {
            while (!this.mRoots.isEmpty()) {
                int size = this.mRoots.size();
                RNGestureHandlerRootHelper rNGestureHandlerRootHelper = (RNGestureHandlerRootHelper) this.mRoots.get(0);
                ReactRootView rootView = rNGestureHandlerRootHelper.getRootView();
                if (rootView instanceof RNGestureHandlerEnabledRootView) {
                    ((RNGestureHandlerEnabledRootView) rootView).tearDown();
                } else {
                    rNGestureHandlerRootHelper.tearDown();
                }
                if (this.mRoots.size() >= size) {
                    throw new IllegalStateException("Expected root helper to get unregistered while tearing down");
                }
            }
        }
        super.onCatalystInstanceDestroy();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0037, code lost:
        r5 = r4.mEnqueuedRootViewInit;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0039, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0044, code lost:
        if (r4.mEnqueuedRootViewInit.contains(java.lang.Integer.valueOf(r1)) == false) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0047, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0048, code lost:
        r4.mEnqueuedRootViewInit.add(java.lang.Integer.valueOf(r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0051, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0052, code lost:
        r0.addUIBlock(new com.swmansion.gesturehandler.react.RNGestureHandlerModule.C09872(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void tryInitializeHandlerForReactRootView(int r5) {
        /*
            r4 = this;
            com.facebook.react.bridge.ReactApplicationContext r0 = r4.getReactApplicationContext()
            java.lang.Class<com.facebook.react.uimanager.UIManagerModule> r1 = com.facebook.react.uimanager.UIManagerModule.class
            com.facebook.react.bridge.NativeModule r0 = r0.getNativeModule(r1)
            com.facebook.react.uimanager.UIManagerModule r0 = (com.facebook.react.uimanager.UIManagerModule) r0
            int r1 = r0.resolveRootTagFromReactTag(r5)
            r2 = 1
            if (r1 < r2) goto L_0x0061
            java.util.List<com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper> r2 = r4.mRoots
            monitor-enter(r2)
            r5 = 0
        L_0x0017:
            java.util.List<com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper> r3 = r4.mRoots     // Catch:{ all -> 0x005e }
            int r3 = r3.size()     // Catch:{ all -> 0x005e }
            if (r5 >= r3) goto L_0x0036
            java.util.List<com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper> r3 = r4.mRoots     // Catch:{ all -> 0x005e }
            java.lang.Object r3 = r3.get(r5)     // Catch:{ all -> 0x005e }
            com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper r3 = (com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper) r3     // Catch:{ all -> 0x005e }
            com.facebook.react.ReactRootView r3 = r3.getRootView()     // Catch:{ all -> 0x005e }
            int r3 = r3.getRootViewTag()     // Catch:{ all -> 0x005e }
            if (r3 != r1) goto L_0x0033
            monitor-exit(r2)     // Catch:{ all -> 0x005e }
            return
        L_0x0033:
            int r5 = r5 + 1
            goto L_0x0017
        L_0x0036:
            monitor-exit(r2)     // Catch:{ all -> 0x005e }
            java.util.List<java.lang.Integer> r5 = r4.mEnqueuedRootViewInit
            monitor-enter(r5)
            java.util.List<java.lang.Integer> r2 = r4.mEnqueuedRootViewInit     // Catch:{ all -> 0x005b }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x005b }
            boolean r2 = r2.contains(r3)     // Catch:{ all -> 0x005b }
            if (r2 == 0) goto L_0x0048
            monitor-exit(r5)     // Catch:{ all -> 0x005b }
            return
        L_0x0048:
            java.util.List<java.lang.Integer> r2 = r4.mEnqueuedRootViewInit     // Catch:{ all -> 0x005b }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x005b }
            r2.add(r3)     // Catch:{ all -> 0x005b }
            monitor-exit(r5)     // Catch:{ all -> 0x005b }
            com.swmansion.gesturehandler.react.RNGestureHandlerModule$2 r5 = new com.swmansion.gesturehandler.react.RNGestureHandlerModule$2
            r5.<init>(r1)
            r0.addUIBlock(r5)
            return
        L_0x005b:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x005b }
            throw r0
        L_0x005e:
            r5 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x005e }
            throw r5
        L_0x0061:
            com.facebook.react.bridge.JSApplicationIllegalArgumentException r0 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Could find root view for a given ancestor with tag "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r0.<init>(r5)
            goto L_0x0079
        L_0x0078:
            throw r0
        L_0x0079:
            goto L_0x0078
        */
        throw new UnsupportedOperationException("Method not decompiled: com.swmansion.gesturehandler.react.RNGestureHandlerModule.tryInitializeHandlerForReactRootView(int):void");
    }

    public void registerRootHelper(RNGestureHandlerRootHelper rNGestureHandlerRootHelper) {
        synchronized (this.mRoots) {
            if (!this.mRoots.contains(rNGestureHandlerRootHelper)) {
                this.mRoots.add(rNGestureHandlerRootHelper);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Root helper");
                sb.append(rNGestureHandlerRootHelper);
                sb.append(" already registered");
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public void unregisterRootHelper(RNGestureHandlerRootHelper rNGestureHandlerRootHelper) {
        synchronized (this.mRoots) {
            this.mRoots.remove(rNGestureHandlerRootHelper);
        }
    }

    @Nullable
    private RNGestureHandlerRootHelper findRootHelperForViewAncestor(int i) {
        int resolveRootTagFromReactTag = ((UIManagerModule) getReactApplicationContext().getNativeModule(UIManagerModule.class)).resolveRootTagFromReactTag(i);
        if (resolveRootTagFromReactTag < 1) {
            return null;
        }
        synchronized (this.mRoots) {
            for (int i2 = 0; i2 < this.mRoots.size(); i2++) {
                RNGestureHandlerRootHelper rNGestureHandlerRootHelper = (RNGestureHandlerRootHelper) this.mRoots.get(i2);
                if (rNGestureHandlerRootHelper.getRootView().getRootViewTag() == resolveRootTagFromReactTag) {
                    return rNGestureHandlerRootHelper;
                }
            }
            return null;
        }
    }

    @Nullable
    private HandlerFactory findFactoryForHandler(GestureHandler gestureHandler) {
        int i = 0;
        while (true) {
            HandlerFactory[] handlerFactoryArr = this.mHandlerFactories;
            if (i >= handlerFactoryArr.length) {
                return null;
            }
            HandlerFactory handlerFactory = handlerFactoryArr[i];
            if (handlerFactory.getType().equals(gestureHandler.getClass())) {
                return handlerFactory;
            }
            i++;
        }
    }

    /* access modifiers changed from: private */
    public void onTouchEvent(GestureHandler gestureHandler, MotionEvent motionEvent) {
        if (gestureHandler.getTag() >= 0 && gestureHandler.getState() == 4) {
            ((UIManagerModule) getReactApplicationContext().getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(RNGestureHandlerEvent.obtain(gestureHandler, findFactoryForHandler(gestureHandler)));
        }
    }

    /* access modifiers changed from: private */
    public void onStateChange(GestureHandler gestureHandler, int i, int i2) {
        if (gestureHandler.getTag() >= 0) {
            ((UIManagerModule) getReactApplicationContext().getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(RNGestureHandlerStateChangeEvent.obtain(gestureHandler, i, i2, findFactoryForHandler(gestureHandler)));
        }
    }

    /* access modifiers changed from: private */
    public static void handleHitSlopProperty(GestureHandler gestureHandler, ReadableMap readableMap) {
        float f;
        float f2;
        float f3;
        float f4;
        String str = KEY_HIT_SLOP;
        if (readableMap.getType(str) == ReadableType.Number) {
            float pixelFromDIP = PixelUtil.toPixelFromDIP(readableMap.getDouble(str));
            gestureHandler.setHitSlop(pixelFromDIP, pixelFromDIP, pixelFromDIP, pixelFromDIP, Float.NaN, Float.NaN);
            return;
        }
        ReadableMap map = readableMap.getMap(str);
        String str2 = KEY_HIT_SLOP_HORIZONTAL;
        if (map.hasKey(str2)) {
            f2 = PixelUtil.toPixelFromDIP(map.getDouble(str2));
            f = f2;
        } else {
            f2 = Float.NaN;
            f = Float.NaN;
        }
        String str3 = KEY_HIT_SLOP_VERTICAL;
        if (map.hasKey(str3)) {
            f4 = PixelUtil.toPixelFromDIP(map.getDouble(str3));
            f3 = f4;
        } else {
            f4 = Float.NaN;
            f3 = Float.NaN;
        }
        String str4 = "left";
        if (map.hasKey(str4)) {
            f2 = PixelUtil.toPixelFromDIP(map.getDouble(str4));
        }
        float f5 = f2;
        String str5 = "top";
        if (map.hasKey(str5)) {
            f3 = PixelUtil.toPixelFromDIP(map.getDouble(str5));
        }
        float f6 = f3;
        String str6 = "right";
        if (map.hasKey(str6)) {
            f = PixelUtil.toPixelFromDIP(map.getDouble(str6));
        }
        float f7 = f;
        String str7 = "bottom";
        if (map.hasKey(str7)) {
            f4 = PixelUtil.toPixelFromDIP(map.getDouble(str7));
        }
        float f8 = f4;
        String str8 = "width";
        float pixelFromDIP2 = map.hasKey(str8) ? PixelUtil.toPixelFromDIP(map.getDouble(str8)) : Float.NaN;
        String str9 = "height";
        gestureHandler.setHitSlop(f5, f6, f7, f8, pixelFromDIP2, map.hasKey(str9) ? PixelUtil.toPixelFromDIP(map.getDouble(str9)) : Float.NaN);
    }
}
