package com.airbnb.android.react.maps;

import android.content.Context;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.maps.GoogleMap;

public abstract class AirMapFeature extends ReactViewGroup {
    public abstract void addToMap(GoogleMap googleMap);

    public abstract Object getFeature();

    public abstract void removeFromMap(GoogleMap googleMap);

    public AirMapFeature(Context context) {
        super(context);
    }
}
