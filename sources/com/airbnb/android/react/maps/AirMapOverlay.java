package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Bitmap;
import com.facebook.react.bridge.ReadableArray;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class AirMapOverlay extends AirMapFeature implements ImageReadable {
    private LatLngBounds bounds;
    private GroundOverlay groundOverlay;
    private GroundOverlayOptions groundOverlayOptions;
    private Bitmap iconBitmap;
    private BitmapDescriptor iconBitmapDescriptor;
    private final ImageReader mImageReader;
    private GoogleMap map;
    private float transparency;
    private float zIndex;

    public AirMapOverlay(Context context) {
        super(context);
        this.mImageReader = new ImageReader(context, getResources(), this);
    }

    public void setBounds(ReadableArray readableArray) {
        this.bounds = new LatLngBounds(new LatLng(readableArray.getArray(1).getDouble(0), readableArray.getArray(0).getDouble(1)), new LatLng(readableArray.getArray(0).getDouble(0), readableArray.getArray(1).getDouble(1)));
        GroundOverlay groundOverlay2 = this.groundOverlay;
        if (groundOverlay2 != null) {
            groundOverlay2.setPositionFromBounds(this.bounds);
        }
    }

    public void setZIndex(float f) {
        this.zIndex = f;
        GroundOverlay groundOverlay2 = this.groundOverlay;
        if (groundOverlay2 != null) {
            groundOverlay2.setZIndex(f);
        }
    }

    public void setImage(String str) {
        this.mImageReader.setImage(str);
    }

    public GroundOverlayOptions getGroundOverlayOptions() {
        if (this.groundOverlayOptions == null) {
            this.groundOverlayOptions = createGroundOverlayOptions();
        }
        return this.groundOverlayOptions;
    }

    private GroundOverlayOptions createGroundOverlayOptions() {
        GroundOverlayOptions groundOverlayOptions2 = this.groundOverlayOptions;
        if (groundOverlayOptions2 != null) {
            return groundOverlayOptions2;
        }
        if (this.iconBitmapDescriptor == null) {
            return null;
        }
        GroundOverlayOptions groundOverlayOptions3 = new GroundOverlayOptions();
        groundOverlayOptions3.image(this.iconBitmapDescriptor);
        groundOverlayOptions3.positionFromBounds(this.bounds);
        groundOverlayOptions3.zIndex(this.zIndex);
        return groundOverlayOptions3;
    }

    public Object getFeature() {
        return this.groundOverlay;
    }

    public void addToMap(GoogleMap googleMap) {
        GroundOverlayOptions groundOverlayOptions2 = getGroundOverlayOptions();
        if (groundOverlayOptions2 != null) {
            this.groundOverlay = googleMap.addGroundOverlay(groundOverlayOptions2);
            this.groundOverlay.setClickable(true);
            return;
        }
        this.map = googleMap;
    }

    public void removeFromMap(GoogleMap googleMap) {
        this.map = null;
        GroundOverlay groundOverlay2 = this.groundOverlay;
        if (groundOverlay2 != null) {
            groundOverlay2.remove();
            this.groundOverlay = null;
            this.groundOverlayOptions = null;
        }
    }

    public void setIconBitmap(Bitmap bitmap) {
        this.iconBitmap = bitmap;
    }

    public void setIconBitmapDescriptor(BitmapDescriptor bitmapDescriptor) {
        this.iconBitmapDescriptor = bitmapDescriptor;
    }

    public void update() {
        this.groundOverlay = getGroundOverlay();
        GroundOverlay groundOverlay2 = this.groundOverlay;
        if (groundOverlay2 != null) {
            groundOverlay2.setImage(this.iconBitmapDescriptor);
            this.groundOverlay.setClickable(true);
        }
    }

    private GroundOverlay getGroundOverlay() {
        GroundOverlay groundOverlay2 = this.groundOverlay;
        if (groundOverlay2 != null) {
            return groundOverlay2;
        }
        if (this.map == null) {
            return null;
        }
        GroundOverlayOptions groundOverlayOptions2 = getGroundOverlayOptions();
        if (groundOverlayOptions2 != null) {
            return this.map.addGroundOverlay(groundOverlayOptions2);
        }
        return null;
    }
}
