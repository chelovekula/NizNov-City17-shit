package com.facebook.react.views.progressbar;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;

class ProgressBarContainerView extends FrameLayout {
    private static final int MAX_PROGRESS = 1000;
    private boolean mAnimating = true;
    @Nullable
    private Integer mColor;
    private boolean mIndeterminate = true;
    private double mProgress;
    @Nullable
    private ProgressBar mProgressBar;

    public ProgressBarContainerView(Context context) {
        super(context);
    }

    public void setStyle(@Nullable String str) {
        this.mProgressBar = ReactProgressBarViewManager.createProgressBar(getContext(), ReactProgressBarViewManager.getStyleFromString(str));
        this.mProgressBar.setMax(1000);
        removeAllViews();
        addView(this.mProgressBar, new LayoutParams(-1, -1));
    }

    public void setColor(@Nullable Integer num) {
        this.mColor = num;
    }

    public void setIndeterminate(boolean z) {
        this.mIndeterminate = z;
    }

    public void setProgress(double d) {
        this.mProgress = d;
    }

    public void setAnimating(boolean z) {
        this.mAnimating = z;
    }

    public void apply() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setIndeterminate(this.mIndeterminate);
            setColor(this.mProgressBar);
            this.mProgressBar.setProgress((int) (this.mProgress * 1000.0d));
            if (this.mAnimating) {
                this.mProgressBar.setVisibility(0);
            } else {
                this.mProgressBar.setVisibility(4);
            }
        } else {
            throw new JSApplicationIllegalArgumentException("setStyle() not called");
        }
    }

    private void setColor(ProgressBar progressBar) {
        Drawable drawable;
        if (progressBar.isIndeterminate()) {
            drawable = progressBar.getIndeterminateDrawable();
        } else {
            drawable = progressBar.getProgressDrawable();
        }
        if (drawable != null) {
            Integer num = this.mColor;
            if (num != null) {
                drawable.setColorFilter(num.intValue(), Mode.SRC_IN);
            } else {
                drawable.clearColorFilter();
            }
        }
    }
}
