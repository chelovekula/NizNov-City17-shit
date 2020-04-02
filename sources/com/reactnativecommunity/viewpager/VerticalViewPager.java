package com.reactnativecommunity.viewpager;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.PageTransformer;

public class VerticalViewPager extends ViewPager {
    private GestureDetector mGestureDetector;
    private boolean mVertical = false;

    private static final class VerticalPageTransformer implements PageTransformer {
        private VerticalPageTransformer() {
        }

        public void transformPage(View view, float f) {
            int width = view.getWidth();
            int height = view.getHeight();
            if (f < -1.0f) {
                view.setAlpha(0.0f);
            } else if (f <= 1.0f) {
                view.setAlpha(1.0f);
                view.setTranslationX(((float) width) * (-f));
                view.setTranslationY(f * ((float) height));
            } else {
                view.setAlpha(0.0f);
            }
        }
    }

    public VerticalViewPager(Context context) {
        super(context);
    }

    public void setOrientation(boolean z) {
        this.mVertical = z;
        if (this.mVertical) {
            setPageTransformer(true, new VerticalPageTransformer());
            this.mGestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
                public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    return Math.abs(f2) > Math.abs(f);
                }
            });
        }
    }

    public boolean canScrollHorizontally(int i) {
        return !canScrollVertically(i);
    }

    public boolean canScrollVertically(int i) {
        return this.mVertical;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(flipXY(motionEvent));
        flipXY(motionEvent);
        if (!this.mVertical || !this.mGestureDetector.onTouchEvent(motionEvent)) {
            return onInterceptTouchEvent;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(flipXY(motionEvent));
        flipXY(motionEvent);
        if (!this.mVertical || !this.mGestureDetector.onTouchEvent(motionEvent)) {
            return onTouchEvent;
        }
        return true;
    }

    private MotionEvent flipXY(MotionEvent motionEvent) {
        if (this.mVertical) {
            float width = (float) getWidth();
            float height = (float) getHeight();
            motionEvent.setLocation((motionEvent.getY() / height) * width, (motionEvent.getX() / width) * height);
        }
        return motionEvent;
    }
}
