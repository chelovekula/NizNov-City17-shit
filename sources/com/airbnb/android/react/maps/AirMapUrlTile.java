package com.airbnb.android.react.maps;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class AirMapUrlTile extends AirMapFeature {
    /* access modifiers changed from: private */
    public boolean flipY;
    /* access modifiers changed from: private */
    public float maximumZ;
    /* access modifiers changed from: private */
    public float minimumZ;
    private TileOverlay tileOverlay;
    private TileOverlayOptions tileOverlayOptions;
    private AIRMapUrlTileProvider tileProvider;
    private String urlTemplate;
    private float zIndex;

    class AIRMapUrlTileProvider extends UrlTileProvider {
        private String urlTemplate;

        public AIRMapUrlTileProvider(int i, int i2, String str) {
            super(i, i2);
            this.urlTemplate = str;
        }

        public synchronized URL getTileUrl(int i, int i2, int i3) {
            if (AirMapUrlTile.this.flipY) {
                i2 = ((1 << i3) - i2) - 1;
            }
            String replace = this.urlTemplate.replace("{x}", Integer.toString(i)).replace("{y}", Integer.toString(i2)).replace("{z}", Integer.toString(i3));
            if (AirMapUrlTile.this.maximumZ > 0.0f && ((float) i3) > AirMapUrlTile.this.maximumZ) {
                return null;
            }
            if (AirMapUrlTile.this.minimumZ > 0.0f && ((float) i3) < AirMapUrlTile.this.minimumZ) {
                return null;
            }
            try {
                return new URL(replace);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        public void setUrlTemplate(String str) {
            this.urlTemplate = str;
        }
    }

    public AirMapUrlTile(Context context) {
        super(context);
    }

    public void setUrlTemplate(String str) {
        this.urlTemplate = str;
        AIRMapUrlTileProvider aIRMapUrlTileProvider = this.tileProvider;
        if (aIRMapUrlTileProvider != null) {
            aIRMapUrlTileProvider.setUrlTemplate(str);
        }
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.clearTileCache();
        }
    }

    public void setZIndex(float f) {
        this.zIndex = f;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.setZIndex(f);
        }
    }

    public void setMaximumZ(float f) {
        this.maximumZ = f;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.clearTileCache();
        }
    }

    public void setMinimumZ(float f) {
        this.minimumZ = f;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.clearTileCache();
        }
    }

    public void setFlipY(boolean z) {
        this.flipY = z;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.clearTileCache();
        }
    }

    public TileOverlayOptions getTileOverlayOptions() {
        if (this.tileOverlayOptions == null) {
            this.tileOverlayOptions = createTileOverlayOptions();
        }
        return this.tileOverlayOptions;
    }

    private TileOverlayOptions createTileOverlayOptions() {
        TileOverlayOptions tileOverlayOptions2 = new TileOverlayOptions();
        tileOverlayOptions2.zIndex(this.zIndex);
        this.tileProvider = new AIRMapUrlTileProvider(256, 256, this.urlTemplate);
        tileOverlayOptions2.tileProvider(this.tileProvider);
        return tileOverlayOptions2;
    }

    public Object getFeature() {
        return this.tileOverlay;
    }

    public void addToMap(GoogleMap googleMap) {
        this.tileOverlay = googleMap.addTileOverlay(getTileOverlayOptions());
    }

    public void removeFromMap(GoogleMap googleMap) {
        this.tileOverlay.remove();
    }
}
