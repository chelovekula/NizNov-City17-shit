package com.airbnb.android.react.maps;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class AirMapWMSTile extends AirMapFeature {
    private static final double FULL = 4.007501669578488E7d;
    /* access modifiers changed from: private */
    public static final double[] mapBound = {-2.003750834789244E7d, 2.003750834789244E7d};
    /* access modifiers changed from: private */
    public float maximumZ;
    /* access modifiers changed from: private */
    public float minimumZ;
    private float opacity;
    private TileOverlay tileOverlay;
    private TileOverlayOptions tileOverlayOptions;
    private AIRMapGSUrlTileProvider tileProvider;
    private int tileSize;
    private String urlTemplate;
    private float zIndex;

    class AIRMapGSUrlTileProvider extends UrlTileProvider {
        private int height;
        private String urlTemplate;
        private int width;

        public AIRMapGSUrlTileProvider(int i, int i2, String str) {
            super(i, i2);
            this.urlTemplate = str;
            this.width = i;
            this.height = i2;
        }

        public synchronized URL getTileUrl(int i, int i2, int i3) {
            if (AirMapWMSTile.this.maximumZ > 0.0f && ((float) i3) > AirMapWMSTile.this.maximumZ) {
                return null;
            }
            if (AirMapWMSTile.this.minimumZ > 0.0f && ((float) i3) < AirMapWMSTile.this.minimumZ) {
                return null;
            }
            double[] boundingBox = getBoundingBox(i, i2, i3);
            try {
                return new URL(this.urlTemplate.replace("{minX}", Double.toString(boundingBox[0])).replace("{minY}", Double.toString(boundingBox[1])).replace("{maxX}", Double.toString(boundingBox[2])).replace("{maxY}", Double.toString(boundingBox[3])).replace("{width}", Integer.toString(this.width)).replace("{height}", Integer.toString(this.height)));
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        private double[] getBoundingBox(int i, int i2, int i3) {
            double pow = AirMapWMSTile.FULL / Math.pow(2.0d, (double) i3);
            double d = AirMapWMSTile.mapBound[0];
            double d2 = (double) i;
            Double.isNaN(d2);
            double d3 = AirMapWMSTile.mapBound[1];
            double d4 = (double) (i2 + 1);
            Double.isNaN(d4);
            double d5 = AirMapWMSTile.mapBound[0];
            double d6 = (double) (i + 1);
            Double.isNaN(d6);
            double d7 = AirMapWMSTile.mapBound[1];
            double d8 = (double) i2;
            Double.isNaN(d8);
            return new double[]{d + (d2 * pow), d3 - (d4 * pow), d5 + (d6 * pow), d7 - (d8 * pow)};
        }

        public void setUrlTemplate(String str) {
            this.urlTemplate = str;
        }
    }

    public AirMapWMSTile(Context context) {
        super(context);
    }

    public void setUrlTemplate(String str) {
        this.urlTemplate = str;
        AIRMapGSUrlTileProvider aIRMapGSUrlTileProvider = this.tileProvider;
        if (aIRMapGSUrlTileProvider != null) {
            aIRMapGSUrlTileProvider.setUrlTemplate(str);
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

    public void setTileSize(int i) {
        this.tileSize = i;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.clearTileCache();
        }
    }

    public void setOpacity(float f) {
        this.opacity = f;
        TileOverlay tileOverlay2 = this.tileOverlay;
        if (tileOverlay2 != null) {
            tileOverlay2.setTransparency(1.0f - f);
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
        tileOverlayOptions2.transparency(1.0f - this.opacity);
        int i = this.tileSize;
        this.tileProvider = new AIRMapGSUrlTileProvider(i, i, this.urlTemplate);
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
