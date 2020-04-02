package com.airbnb.android.react.maps;

import android.os.Handler;
import android.os.Looper;
import java.util.Iterator;
import java.util.LinkedList;

public class ViewChangesTracker {
    private static ViewChangesTracker instance;
    private final long fps = 40;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(Looper.myLooper());
    /* access modifiers changed from: private */
    public boolean hasScheduledFrame = false;
    /* access modifiers changed from: private */
    public LinkedList<AirMapMarker> markers = new LinkedList<>();
    private LinkedList<AirMapMarker> markersToRemove = new LinkedList<>();
    /* access modifiers changed from: private */
    public Runnable updateRunnable = new Runnable() {
        public void run() {
            ViewChangesTracker.this.hasScheduledFrame = false;
            ViewChangesTracker.this.update();
            if (ViewChangesTracker.this.markers.size() > 0) {
                ViewChangesTracker.this.handler.postDelayed(ViewChangesTracker.this.updateRunnable, 40);
            }
        }
    };

    private ViewChangesTracker() {
    }

    static ViewChangesTracker getInstance() {
        if (instance == null) {
            synchronized (ViewChangesTracker.class) {
                instance = new ViewChangesTracker();
            }
        }
        return instance;
    }

    public void addMarker(AirMapMarker airMapMarker) {
        this.markers.add(airMapMarker);
        if (!this.hasScheduledFrame) {
            this.hasScheduledFrame = true;
            this.handler.postDelayed(this.updateRunnable, 40);
        }
    }

    public void removeMarker(AirMapMarker airMapMarker) {
        this.markers.remove(airMapMarker);
    }

    public boolean containsMarker(AirMapMarker airMapMarker) {
        return this.markers.contains(airMapMarker);
    }

    public void update() {
        Iterator it = this.markers.iterator();
        while (it.hasNext()) {
            AirMapMarker airMapMarker = (AirMapMarker) it.next();
            if (!airMapMarker.updateCustomForTracking()) {
                this.markersToRemove.add(airMapMarker);
            }
        }
        if (this.markersToRemove.size() > 0) {
            this.markers.removeAll(this.markersToRemove);
            this.markersToRemove.clear();
        }
    }
}
