package com.swmansion.gesturehandler.react;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.ReactConstants;
import com.swmansion.gesturehandler.GestureHandler;
import com.swmansion.gesturehandler.GestureHandlerOrchestrator;

public class RNGestureHandlerRootHelper {
    private static final float MIN_ALPHA_FOR_TOUCH = 0.1f;
    private final ReactContext mContext;
    private final GestureHandler mJSGestureHandler;
    private final GestureHandlerOrchestrator mOrchestrator;
    private boolean mPassingTouch = false;
    /* access modifiers changed from: private */
    public final ReactRootView mReactRootView;
    /* access modifiers changed from: private */
    public boolean mShouldIntercept = false;

    private class RootViewGestureHandler extends GestureHandler {
        private RootViewGestureHandler() {
        }

        /* access modifiers changed from: protected */
        public void onHandle(MotionEvent motionEvent) {
            if (getState() == 0) {
                begin();
                RNGestureHandlerRootHelper.this.mShouldIntercept = false;
            }
            if (motionEvent.getActionMasked() == 1) {
                end();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancel() {
            RNGestureHandlerRootHelper.this.mShouldIntercept = true;
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            obtain.setAction(3);
            RNGestureHandlerRootHelper.this.mReactRootView.onChildStartedNativeGesture(obtain);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=android.view.ViewGroup, code=android.view.ViewParent, for r3v0, types: [android.view.ViewParent, java.lang.Object, android.view.ViewGroup] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.facebook.react.ReactRootView findRootViewTag(android.view.ViewParent r3) {
        /*
            com.facebook.react.bridge.UiThreadUtil.assertOnUiThread()
            r0 = r3
        L_0x0004:
            if (r0 == 0) goto L_0x000f
            boolean r1 = r0 instanceof com.facebook.react.ReactRootView
            if (r1 != 0) goto L_0x000f
            android.view.ViewParent r0 = r0.getParent()
            goto L_0x0004
        L_0x000f:
            if (r0 == 0) goto L_0x0014
            com.facebook.react.ReactRootView r0 = (com.facebook.react.ReactRootView) r0
            return r0
        L_0x0014:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "View "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = " has not been mounted under ReactRootView"
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            goto L_0x0031
        L_0x0030:
            throw r0
        L_0x0031:
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: com.swmansion.gesturehandler.react.RNGestureHandlerRootHelper.findRootViewTag(android.view.ViewGroup):com.facebook.react.ReactRootView");
    }

    public RNGestureHandlerRootHelper(ReactContext reactContext, ViewGroup viewGroup) {
        UiThreadUtil.assertOnUiThread();
        int id = viewGroup.getId();
        if (id >= 1) {
            RNGestureHandlerModule rNGestureHandlerModule = (RNGestureHandlerModule) reactContext.getNativeModule(RNGestureHandlerModule.class);
            RNGestureHandlerRegistry registry = rNGestureHandlerModule.getRegistry();
            this.mReactRootView = findRootViewTag(viewGroup);
            StringBuilder sb = new StringBuilder();
            sb.append("[GESTURE HANDLER] Initialize gesture handler for root view ");
            sb.append(this.mReactRootView);
            Log.i(ReactConstants.TAG, sb.toString());
            this.mContext = reactContext;
            this.mOrchestrator = new GestureHandlerOrchestrator(viewGroup, registry, new RNViewConfigurationHelper());
            this.mOrchestrator.setMinimumAlphaForTraversal(MIN_ALPHA_FOR_TOUCH);
            this.mJSGestureHandler = new RootViewGestureHandler();
            this.mJSGestureHandler.setTag(-id);
            registry.registerHandler(this.mJSGestureHandler);
            registry.attachHandlerToView(this.mJSGestureHandler.getTag(), id);
            rNGestureHandlerModule.registerRootHelper(this);
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expect view tag to be set for ");
        sb2.append(viewGroup);
        throw new IllegalStateException(sb2.toString());
    }

    public void tearDown() {
        StringBuilder sb = new StringBuilder();
        sb.append("[GESTURE HANDLER] Tearing down gesture handler registered for root view ");
        sb.append(this.mReactRootView);
        Log.i(ReactConstants.TAG, sb.toString());
        RNGestureHandlerModule rNGestureHandlerModule = (RNGestureHandlerModule) this.mContext.getNativeModule(RNGestureHandlerModule.class);
        rNGestureHandlerModule.getRegistry().dropHandler(this.mJSGestureHandler.getTag());
        rNGestureHandlerModule.unregisterRootHelper(this);
    }

    public ReactRootView getRootView() {
        return this.mReactRootView;
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (this.mOrchestrator != null && !this.mPassingTouch) {
            tryCancelAllHandlers();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.mPassingTouch = true;
        this.mOrchestrator.onTouchEvent(motionEvent);
        this.mPassingTouch = false;
        if (this.mShouldIntercept) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void tryCancelAllHandlers() {
        GestureHandler gestureHandler = this.mJSGestureHandler;
        if (gestureHandler != null && gestureHandler.getState() == 2) {
            this.mJSGestureHandler.activate();
            this.mJSGestureHandler.end();
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleSetJSResponder(int i, boolean z) {
        if (z) {
            UiThreadUtil.runOnUiThread(new Runnable() {
                public void run() {
                    RNGestureHandlerRootHelper.this.tryCancelAllHandlers();
                }
            });
        }
    }
}
