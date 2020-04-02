package com.airbnb.android.react.maps;

import android.content.Context;
import com.facebook.react.views.view.ReactViewGroup;

public class AirMapCallout extends ReactViewGroup {
    public int height;
    private boolean tooltip = false;
    public int width;

    public AirMapCallout(Context context) {
        super(context);
    }

    public void setTooltip(boolean z) {
        this.tooltip = z;
    }

    public boolean getTooltip() {
        return this.tooltip;
    }
}
