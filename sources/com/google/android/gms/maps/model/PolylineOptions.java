package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Class(creator = "PolylineOptionsCreator")
@Reserved({1})
public final class PolylineOptions extends AbstractSafeParcelable {
    public static final Creator<PolylineOptions> CREATOR = new zzl();
    @Field(getter = "getColor", mo12619id = 4)
    private int color;
    @Field(getter = "getWidth", mo12619id = 3)
    private float width;
    @Field(getter = "getZIndex", mo12619id = 5)
    private float zzcs;
    @Field(getter = "isVisible", mo12619id = 6)
    private boolean zzct;
    @Field(getter = "isClickable", mo12619id = 8)
    private boolean zzcu;
    @Field(getter = "getPoints", mo12619id = 2)
    private final List<LatLng> zzdx;
    @Field(getter = "isGeodesic", mo12619id = 7)
    private boolean zzdz;
    @Field(getter = "getStartCap", mo12619id = 9)
    @NonNull
    private Cap zzec;
    @Field(getter = "getEndCap", mo12619id = 10)
    @NonNull
    private Cap zzed;
    @Field(getter = "getJointType", mo12619id = 11)
    private int zzee;
    @Field(getter = "getPattern", mo12619id = 12)
    @Nullable
    private List<PatternItem> zzef;

    public PolylineOptions() {
        this.width = 10.0f;
        this.color = ViewCompat.MEASURED_STATE_MASK;
        this.zzcs = 0.0f;
        this.zzct = true;
        this.zzdz = false;
        this.zzcu = false;
        this.zzec = new ButtCap();
        this.zzed = new ButtCap();
        this.zzee = 0;
        this.zzef = null;
        this.zzdx = new ArrayList();
    }

    @Constructor
    PolylineOptions(@Param(mo12622id = 2) List list, @Param(mo12622id = 3) float f, @Param(mo12622id = 4) int i, @Param(mo12622id = 5) float f2, @Param(mo12622id = 6) boolean z, @Param(mo12622id = 7) boolean z2, @Param(mo12622id = 8) boolean z3, @Param(mo12622id = 9) @Nullable Cap cap, @Param(mo12622id = 10) @Nullable Cap cap2, @Param(mo12622id = 11) int i2, @Param(mo12622id = 12) @Nullable List<PatternItem> list2) {
        this.width = 10.0f;
        this.color = ViewCompat.MEASURED_STATE_MASK;
        this.zzcs = 0.0f;
        this.zzct = true;
        this.zzdz = false;
        this.zzcu = false;
        this.zzec = new ButtCap();
        this.zzed = new ButtCap();
        this.zzee = 0;
        this.zzef = null;
        this.zzdx = list;
        this.width = f;
        this.color = i;
        this.zzcs = f2;
        this.zzct = z;
        this.zzdz = z2;
        this.zzcu = z3;
        if (cap != null) {
            this.zzec = cap;
        }
        if (cap2 != null) {
            this.zzed = cap2;
        }
        this.zzee = i2;
        this.zzef = list2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 2, getPoints(), false);
        SafeParcelWriter.writeFloat(parcel, 3, getWidth());
        SafeParcelWriter.writeInt(parcel, 4, getColor());
        SafeParcelWriter.writeFloat(parcel, 5, getZIndex());
        SafeParcelWriter.writeBoolean(parcel, 6, isVisible());
        SafeParcelWriter.writeBoolean(parcel, 7, isGeodesic());
        SafeParcelWriter.writeBoolean(parcel, 8, isClickable());
        SafeParcelWriter.writeParcelable(parcel, 9, getStartCap(), i, false);
        SafeParcelWriter.writeParcelable(parcel, 10, getEndCap(), i, false);
        SafeParcelWriter.writeInt(parcel, 11, getJointType());
        SafeParcelWriter.writeTypedList(parcel, 12, getPattern(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final PolylineOptions add(LatLng latLng) {
        this.zzdx.add(latLng);
        return this;
    }

    public final PolylineOptions add(LatLng... latLngArr) {
        this.zzdx.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public final PolylineOptions addAll(Iterable<LatLng> iterable) {
        for (LatLng add : iterable) {
            this.zzdx.add(add);
        }
        return this;
    }

    public final PolylineOptions width(float f) {
        this.width = f;
        return this;
    }

    public final PolylineOptions color(int i) {
        this.color = i;
        return this;
    }

    public final PolylineOptions startCap(@NonNull Cap cap) {
        this.zzec = (Cap) Preconditions.checkNotNull(cap, "startCap must not be null");
        return this;
    }

    public final PolylineOptions endCap(@NonNull Cap cap) {
        this.zzed = (Cap) Preconditions.checkNotNull(cap, "endCap must not be null");
        return this;
    }

    public final PolylineOptions jointType(int i) {
        this.zzee = i;
        return this;
    }

    public final PolylineOptions pattern(@Nullable List<PatternItem> list) {
        this.zzef = list;
        return this;
    }

    public final PolylineOptions zIndex(float f) {
        this.zzcs = f;
        return this;
    }

    public final PolylineOptions visible(boolean z) {
        this.zzct = z;
        return this;
    }

    public final PolylineOptions geodesic(boolean z) {
        this.zzdz = z;
        return this;
    }

    public final PolylineOptions clickable(boolean z) {
        this.zzcu = z;
        return this;
    }

    public final List<LatLng> getPoints() {
        return this.zzdx;
    }

    public final float getWidth() {
        return this.width;
    }

    public final int getColor() {
        return this.color;
    }

    @NonNull
    public final Cap getStartCap() {
        return this.zzec;
    }

    @NonNull
    public final Cap getEndCap() {
        return this.zzed;
    }

    public final int getJointType() {
        return this.zzee;
    }

    @Nullable
    public final List<PatternItem> getPattern() {
        return this.zzef;
    }

    public final float getZIndex() {
        return this.zzcs;
    }

    public final boolean isVisible() {
        return this.zzct;
    }

    public final boolean isGeodesic() {
        return this.zzdz;
    }

    public final boolean isClickable() {
        return this.zzcu;
    }
}
