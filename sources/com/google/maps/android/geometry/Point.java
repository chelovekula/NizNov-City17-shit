package com.google.maps.android.geometry;

public class Point {

    /* renamed from: x */
    public final double f58x;

    /* renamed from: y */
    public final double f59y;

    public Point(double d, double d2) {
        this.f58x = d;
        this.f59y = d2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point{x=");
        sb.append(this.f58x);
        sb.append(", y=");
        sb.append(this.f59y);
        sb.append('}');
        return sb.toString();
    }
}
