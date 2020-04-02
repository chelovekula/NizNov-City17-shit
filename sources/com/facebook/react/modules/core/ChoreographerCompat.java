package com.facebook.react.modules.core;

import android.os.Handler;
import android.view.Choreographer;
import com.facebook.react.bridge.UiThreadUtil;

public class ChoreographerCompat {
    private static final long ONE_FRAME_MILLIS = 17;
    private static ChoreographerCompat sInstance;
    private Choreographer mChoreographer = getChoreographer();
    private Handler mHandler;

    public static abstract class FrameCallback {
        private android.view.Choreographer.FrameCallback mFrameCallback;
        private Runnable mRunnable;

        public abstract void doFrame(long j);

        /* access modifiers changed from: 0000 */
        public android.view.Choreographer.FrameCallback getFrameCallback() {
            if (this.mFrameCallback == null) {
                this.mFrameCallback = new android.view.Choreographer.FrameCallback() {
                    public void doFrame(long j) {
                        FrameCallback.this.doFrame(j);
                    }
                };
            }
            return this.mFrameCallback;
        }

        /* access modifiers changed from: 0000 */
        public Runnable getRunnable() {
            if (this.mRunnable == null) {
                this.mRunnable = new Runnable() {
                    public void run() {
                        FrameCallback.this.doFrame(System.nanoTime());
                    }
                };
            }
            return this.mRunnable;
        }
    }

    public static ChoreographerCompat getInstance() {
        UiThreadUtil.assertOnUiThread();
        if (sInstance == null) {
            sInstance = new ChoreographerCompat();
        }
        return sInstance;
    }

    private ChoreographerCompat() {
    }

    public void postFrameCallback(FrameCallback frameCallback) {
        choreographerPostFrameCallback(frameCallback.getFrameCallback());
    }

    public void postFrameCallbackDelayed(FrameCallback frameCallback, long j) {
        choreographerPostFrameCallbackDelayed(frameCallback.getFrameCallback(), j);
    }

    public void removeFrameCallback(FrameCallback frameCallback) {
        choreographerRemoveFrameCallback(frameCallback.getFrameCallback());
    }

    private Choreographer getChoreographer() {
        return Choreographer.getInstance();
    }

    private void choreographerPostFrameCallback(android.view.Choreographer.FrameCallback frameCallback) {
        this.mChoreographer.postFrameCallback(frameCallback);
    }

    private void choreographerPostFrameCallbackDelayed(android.view.Choreographer.FrameCallback frameCallback, long j) {
        this.mChoreographer.postFrameCallbackDelayed(frameCallback, j);
    }

    private void choreographerRemoveFrameCallback(android.view.Choreographer.FrameCallback frameCallback) {
        this.mChoreographer.removeFrameCallback(frameCallback);
    }
}
