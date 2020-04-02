package com.horcrux.svg;

import android.graphics.Path;
import android.graphics.RectF;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PropHelper {
    private static final int inputMatrixDataSize = 6;

    static class PathParser {
        private static final Pattern DECIMAL_REG_EXP = Pattern.compile("(\\.\\d+)(?=-?\\.)");
        private static final Pattern PATH_REG_EXP = Pattern.compile("[a-df-z]|[\\-+]?(?:[\\d.]e[\\-+]?|[^\\s\\-+,a-z])+", 2);
        private WritableArray mBezierCurves;
        private String mLastCommand;
        private WritableMap mLastStartPoint;
        private String mLastValue;
        private Matcher mMatcher;
        private Path mPath;
        private float mPenDownX;
        private float mPenDownY;
        private float mPenX = 0.0f;
        private float mPenY = 0.0f;
        private boolean mPendDownSet = false;
        private float mPivotX = 0.0f;
        private float mPivotY = 0.0f;
        private final float mScale;
        private final String mString;
        private boolean mValid = true;

        PathParser(String str, float f) {
            this.mScale = f;
            this.mString = str;
        }

        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void executeCommand(java.lang.String r13) {
            /*
                r12 = this;
                int r0 = r13.hashCode()
                java.lang.String r8 = "m"
                java.lang.String r9 = "l"
                java.lang.String r10 = "M"
                java.lang.String r11 = "L"
                switch(r0) {
                    case 65: goto L_0x00dc;
                    case 67: goto L_0x00d1;
                    case 72: goto L_0x00c7;
                    case 76: goto L_0x00bf;
                    case 77: goto L_0x00b7;
                    case 81: goto L_0x00ac;
                    case 83: goto L_0x00a1;
                    case 84: goto L_0x0096;
                    case 86: goto L_0x008c;
                    case 90: goto L_0x0081;
                    case 97: goto L_0x0075;
                    case 99: goto L_0x0069;
                    case 104: goto L_0x005e;
                    case 108: goto L_0x0055;
                    case 109: goto L_0x004c;
                    case 113: goto L_0x0040;
                    case 115: goto L_0x0034;
                    case 116: goto L_0x0028;
                    case 118: goto L_0x001d;
                    case 122: goto L_0x0011;
                    default: goto L_0x000f;
                }
            L_0x000f:
                goto L_0x00e7
            L_0x0011:
                java.lang.String r0 = "z"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 19
                goto L_0x00e8
            L_0x001d:
                java.lang.String r0 = "v"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 6
                goto L_0x00e8
            L_0x0028:
                java.lang.String r0 = "t"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 14
                goto L_0x00e8
            L_0x0034:
                java.lang.String r0 = "s"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 10
                goto L_0x00e8
            L_0x0040:
                java.lang.String r0 = "q"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 12
                goto L_0x00e8
            L_0x004c:
                boolean r0 = r13.equals(r8)
                if (r0 == 0) goto L_0x00e7
                r0 = 0
                goto L_0x00e8
            L_0x0055:
                boolean r0 = r13.equals(r9)
                if (r0 == 0) goto L_0x00e7
                r0 = 2
                goto L_0x00e8
            L_0x005e:
                java.lang.String r0 = "h"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 4
                goto L_0x00e8
            L_0x0069:
                java.lang.String r0 = "c"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 8
                goto L_0x00e8
            L_0x0075:
                java.lang.String r0 = "a"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 16
                goto L_0x00e8
            L_0x0081:
                java.lang.String r0 = "Z"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 18
                goto L_0x00e8
            L_0x008c:
                java.lang.String r0 = "V"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 7
                goto L_0x00e8
            L_0x0096:
                java.lang.String r0 = "T"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 15
                goto L_0x00e8
            L_0x00a1:
                java.lang.String r0 = "S"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 11
                goto L_0x00e8
            L_0x00ac:
                java.lang.String r0 = "Q"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 13
                goto L_0x00e8
            L_0x00b7:
                boolean r0 = r13.equals(r10)
                if (r0 == 0) goto L_0x00e7
                r0 = 1
                goto L_0x00e8
            L_0x00bf:
                boolean r0 = r13.equals(r11)
                if (r0 == 0) goto L_0x00e7
                r0 = 3
                goto L_0x00e8
            L_0x00c7:
                java.lang.String r0 = "H"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 5
                goto L_0x00e8
            L_0x00d1:
                java.lang.String r0 = "C"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 9
                goto L_0x00e8
            L_0x00dc:
                java.lang.String r0 = "A"
                boolean r0 = r13.equals(r0)
                if (r0 == 0) goto L_0x00e7
                r0 = 17
                goto L_0x00e8
            L_0x00e7:
                r0 = -1
            L_0x00e8:
                r1 = 0
                switch(r0) {
                    case 0: goto L_0x022e;
                    case 1: goto L_0x0222;
                    case 2: goto L_0x0216;
                    case 3: goto L_0x020a;
                    case 4: goto L_0x0202;
                    case 5: goto L_0x01f8;
                    case 6: goto L_0x01f0;
                    case 7: goto L_0x01e6;
                    case 8: goto L_0x01c9;
                    case 9: goto L_0x01ab;
                    case 10: goto L_0x0196;
                    case 11: goto L_0x0181;
                    case 12: goto L_0x016c;
                    case 13: goto L_0x0157;
                    case 14: goto L_0x014a;
                    case 15: goto L_0x013d;
                    case 16: goto L_0x011b;
                    case 17: goto L_0x00f9;
                    case 18: goto L_0x00f4;
                    case 19: goto L_0x00f4;
                    default: goto L_0x00ec;
                }
            L_0x00ec:
                r12.mLastValue = r13
                java.lang.String r0 = r12.mLastCommand
                r12.executeCommand(r0)
                return
            L_0x00f4:
                r12.close()
                goto L_0x0239
            L_0x00f9:
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                boolean r4 = r12.getNextBoolean()
                boolean r5 = r12.getNextBoolean()
                float r6 = r12.getNextFloat()
                float r7 = r12.getNextFloat()
                r0 = r12
                r0.arcTo(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x0239
            L_0x011b:
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                boolean r4 = r12.getNextBoolean()
                boolean r5 = r12.getNextBoolean()
                float r6 = r12.getNextFloat()
                float r7 = r12.getNextFloat()
                r0 = r12
                r0.arc(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x0239
            L_0x013d:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.smoothQuadraticBezierCurveTo(r0, r1)
                goto L_0x0239
            L_0x014a:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.smoothQuadraticBezierCurve(r0, r1)
                goto L_0x0239
            L_0x0157:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                r12.quadraticBezierCurveTo(r0, r1, r2, r3)
                goto L_0x0239
            L_0x016c:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                r12.quadraticBezierCurve(r0, r1, r2, r3)
                goto L_0x0239
            L_0x0181:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                r12.smoothCurveTo(r0, r1, r2, r3)
                goto L_0x0239
            L_0x0196:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                r12.smoothCurve(r0, r1, r2, r3)
                goto L_0x0239
            L_0x01ab:
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                float r4 = r12.getNextFloat()
                float r5 = r12.getNextFloat()
                float r6 = r12.getNextFloat()
                r0 = r12
                r0.curveTo(r1, r2, r3, r4, r5, r6)
                goto L_0x0239
            L_0x01c9:
                float r1 = r12.getNextFloat()
                float r2 = r12.getNextFloat()
                float r3 = r12.getNextFloat()
                float r4 = r12.getNextFloat()
                float r5 = r12.getNextFloat()
                float r6 = r12.getNextFloat()
                r0 = r12
                r0.curve(r1, r2, r3, r4, r5, r6)
                goto L_0x0239
            L_0x01e6:
                float r0 = r12.mPenX
                float r1 = r12.getNextFloat()
                r12.lineTo(r0, r1)
                goto L_0x0239
            L_0x01f0:
                float r0 = r12.getNextFloat()
                r12.line(r1, r0)
                goto L_0x0239
            L_0x01f8:
                float r0 = r12.getNextFloat()
                float r1 = r12.mPenY
                r12.lineTo(r0, r1)
                goto L_0x0239
            L_0x0202:
                float r0 = r12.getNextFloat()
                r12.line(r0, r1)
                goto L_0x0239
            L_0x020a:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.lineTo(r0, r1)
                goto L_0x0239
            L_0x0216:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.line(r0, r1)
                goto L_0x0239
            L_0x0222:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.moveTo(r0, r1)
                goto L_0x0239
            L_0x022e:
                float r0 = r12.getNextFloat()
                float r1 = r12.getNextFloat()
                r12.move(r0, r1)
            L_0x0239:
                r12.mLastCommand = r13
                boolean r0 = r13.equals(r8)
                if (r0 == 0) goto L_0x0244
                r12.mLastCommand = r9
                goto L_0x024c
            L_0x0244:
                boolean r0 = r13.equals(r10)
                if (r0 == 0) goto L_0x024c
                r12.mLastCommand = r11
            L_0x024c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.PropHelper.PathParser.executeCommand(java.lang.String):void");
        }

        /* access modifiers changed from: 0000 */
        public Path getPath() {
            this.mPath = new Path();
            this.mBezierCurves = Arguments.createArray();
            this.mMatcher = PATH_REG_EXP.matcher(DECIMAL_REG_EXP.matcher(this.mString).replaceAll("$1,"));
            while (this.mMatcher.find() && this.mValid) {
                executeCommand(this.mMatcher.group());
            }
            return this.mPath;
        }

        private WritableMap getPointMap(float f, float f2) {
            WritableMap createMap = Arguments.createMap();
            createMap.putDouble("x", (double) (f * this.mScale));
            createMap.putDouble("y", (double) (f2 * this.mScale));
            return createMap;
        }

        private WritableMap clonePointMap(WritableMap writableMap) {
            WritableMap createMap = Arguments.createMap();
            String str = "x";
            createMap.putDouble(str, writableMap.getDouble(str));
            String str2 = "y";
            createMap.putDouble(str2, writableMap.getDouble(str2));
            return createMap;
        }

        private boolean getNextBoolean() {
            if (this.mMatcher.find()) {
                return this.mMatcher.group().equals("1");
            }
            this.mValid = false;
            this.mPath = new Path();
            return false;
        }

        private float getNextFloat() {
            String str = this.mLastValue;
            if (str != null) {
                this.mLastValue = null;
                return Float.parseFloat(str);
            } else if (this.mMatcher.find()) {
                return Float.parseFloat(this.mMatcher.group());
            } else {
                this.mValid = false;
                this.mPath = new Path();
                return 0.0f;
            }
        }

        private void move(float f, float f2) {
            moveTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void moveTo(float f, float f2) {
            this.mPenX = f;
            this.mPivotX = f;
            this.mPenDownX = f;
            this.mPenY = f2;
            this.mPivotY = f2;
            this.mPenDownY = f2;
            Path path = this.mPath;
            float f3 = this.mScale;
            path.moveTo(f * f3, f3 * f2);
            this.mLastStartPoint = getPointMap(f, f2);
            WritableArray createArray = Arguments.createArray();
            createArray.pushMap(getPointMap(f, f2));
            this.mBezierCurves.pushArray(createArray);
        }

        private void line(float f, float f2) {
            lineTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void lineTo(float f, float f2) {
            setPenDown();
            this.mPenX = f;
            this.mPivotX = f;
            this.mPenY = f2;
            this.mPivotY = f2;
            Path path = this.mPath;
            float f3 = this.mScale;
            path.lineTo(f * f3, f3 * f2);
            WritableArray createArray = Arguments.createArray();
            createArray.pushMap(getPointMap(f, f2));
            createArray.pushMap(getPointMap(f, f2));
            createArray.pushMap(getPointMap(f, f2));
            this.mBezierCurves.pushArray(createArray);
        }

        private void curve(float f, float f2, float f3, float f4, float f5, float f6) {
            float f7 = this.mPenX;
            float f8 = f + f7;
            float f9 = this.mPenY;
            curveTo(f8, f2 + f9, f3 + f7, f4 + f9, f5 + f7, f6 + f9);
        }

        private void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.mPivotX = f3;
            this.mPivotY = f4;
            cubicTo(f, f2, f3, f4, f5, f6);
        }

        private void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
            setPenDown();
            this.mPenX = f5;
            this.mPenY = f6;
            Path path = this.mPath;
            float f7 = this.mScale;
            path.cubicTo(f * f7, f2 * f7, f3 * f7, f4 * f7, f5 * f7, f6 * f7);
            WritableArray createArray = Arguments.createArray();
            createArray.pushMap(getPointMap(f, f2));
            createArray.pushMap(getPointMap(f3, f4));
            createArray.pushMap(getPointMap(f5, f6));
            this.mBezierCurves.pushArray(createArray);
        }

        private void smoothCurve(float f, float f2, float f3, float f4) {
            float f5 = this.mPenX;
            float f6 = f + f5;
            float f7 = this.mPenY;
            smoothCurveTo(f6, f2 + f7, f3 + f5, f4 + f7);
        }

        private void smoothCurveTo(float f, float f2, float f3, float f4) {
            float f5 = (this.mPenX * 2.0f) - this.mPivotX;
            float f6 = (this.mPenY * 2.0f) - this.mPivotY;
            this.mPivotX = f;
            this.mPivotY = f2;
            cubicTo(f5, f6, f, f2, f3, f4);
        }

        private void quadraticBezierCurve(float f, float f2, float f3, float f4) {
            float f5 = this.mPenX;
            float f6 = f + f5;
            float f7 = this.mPenY;
            quadraticBezierCurveTo(f6, f2 + f7, f3 + f5, f4 + f7);
        }

        private void quadraticBezierCurveTo(float f, float f2, float f3, float f4) {
            this.mPivotX = f;
            this.mPivotY = f2;
            float f5 = f * 2.0f;
            float f6 = f2 * 2.0f;
            cubicTo((this.mPenX + f5) / 3.0f, (this.mPenY + f6) / 3.0f, (f3 + f5) / 3.0f, (f4 + f6) / 3.0f, f3, f4);
        }

        private void smoothQuadraticBezierCurve(float f, float f2) {
            smoothQuadraticBezierCurveTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void smoothQuadraticBezierCurveTo(float f, float f2) {
            quadraticBezierCurveTo((this.mPenX * 2.0f) - this.mPivotX, (this.mPenY * 2.0f) - this.mPivotY, f, f2);
        }

        private void arc(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            arcTo(f, f2, f3, z, z2, f4 + this.mPenX, f5 + this.mPenY);
        }

        private void arcTo(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            float f6;
            float f7;
            float f8;
            boolean z3 = z;
            boolean z4 = z2;
            float f9 = f4;
            float f10 = f5;
            float f11 = this.mPenX;
            float f12 = this.mPenY;
            float f13 = f2 == 0.0f ? f == 0.0f ? f10 - f12 : f : f2;
            float abs = Math.abs(f13);
            float abs2 = Math.abs(f == 0.0f ? f9 - f11 : f);
            if (abs2 == 0.0f || abs == 0.0f || (f9 == f11 && f10 == f12)) {
                lineTo(f9, f10);
                return;
            }
            float radians = (float) Math.toRadians((double) f3);
            double d = (double) radians;
            float cos = (float) Math.cos(d);
            float sin = (float) Math.sin(d);
            float f14 = f9 - f11;
            float f15 = f10 - f12;
            float f16 = ((cos * f14) / 2.0f) + ((sin * f15) / 2.0f);
            float f17 = -sin;
            float f18 = ((f17 * f14) / 2.0f) + ((cos * f15) / 2.0f);
            float f19 = abs2 * abs2;
            float f20 = f19 * abs * abs;
            float f21 = abs * abs * f16 * f16;
            float f22 = f19 * f18 * f18;
            float f23 = (f20 - f22) - f21;
            if (f23 < 0.0f) {
                f6 = f17;
                float sqrt = (float) Math.sqrt((double) (1.0f - (f23 / f20)));
                abs2 *= sqrt;
                abs *= sqrt;
                f7 = f15 / 2.0f;
                f8 = f14 / 2.0f;
            } else {
                f6 = f17;
                float sqrt2 = (float) Math.sqrt((double) (f23 / (f22 + f21)));
                if (z3 == z4) {
                    sqrt2 = -sqrt2;
                }
                float f24 = (((-sqrt2) * f18) * abs2) / abs;
                float f25 = ((sqrt2 * f16) * abs) / abs2;
                f8 = ((cos * f24) - (sin * f25)) + (f14 / 2.0f);
                f7 = (f15 / 2.0f) + (f24 * sin) + (f25 * cos);
            }
            float f26 = cos / abs2;
            float f27 = sin / abs2;
            float f28 = f6 / abs;
            float f29 = cos / abs;
            float f30 = -f8;
            float f31 = -f7;
            float f32 = abs2;
            float f33 = abs;
            float f34 = f14 - f8;
            float f35 = f15 - f7;
            float atan2 = (float) Math.atan2((double) ((f28 * f30) + (f29 * f31)), (double) ((f30 * f26) + (f31 * f27)));
            float atan22 = (float) Math.atan2((double) ((f28 * f34) + (f29 * f35)), (double) ((f26 * f34) + (f27 * f35)));
            float f36 = f8 + f11;
            float f37 = f7 + f12;
            float f38 = f14 + f11;
            float f39 = f15 + f12;
            setPenDown();
            this.mPivotX = f38;
            this.mPenX = f38;
            this.mPivotY = f39;
            this.mPenY = f39;
            if (f32 == f33 && radians == 0.0f) {
                float degrees = (float) Math.toDegrees((double) atan2);
                float abs3 = Math.abs((degrees - ((float) Math.toDegrees((double) atan22))) % 360.0f);
                if (!z ? abs3 > 180.0f : abs3 < 180.0f) {
                    abs3 = 360.0f - abs3;
                }
                if (!z2) {
                    abs3 = -abs3;
                }
                float f40 = f36 - f32;
                float f41 = this.mScale;
                this.mPath.arcTo(new RectF(f40 * f41, (f37 - f32) * f41, (f36 + f32) * f41, (f37 + f32) * f41), degrees, abs3);
            } else {
                arcToBezier(f36, f37, f32, f33, atan2, atan22, z2, radians);
            }
        }

        private void close() {
            if (this.mPendDownSet) {
                this.mPenX = this.mPenDownX;
                this.mPenY = this.mPenDownY;
                this.mPendDownSet = false;
                this.mPath.close();
                WritableArray createArray = Arguments.createArray();
                createArray.pushMap(clonePointMap(this.mLastStartPoint));
                createArray.pushMap(clonePointMap(this.mLastStartPoint));
                createArray.pushMap(clonePointMap(this.mLastStartPoint));
                this.mBezierCurves.pushArray(createArray);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x0073 A[LOOP:0: B:10:0x0071->B:11:0x0073, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void arcToBezier(float r25, float r26, float r27, float r28, float r29, float r30, boolean r31, float r32) {
            /*
                r24 = this;
                r0 = r24
                r1 = r29
                r2 = r32
                double r2 = (double) r2
                double r4 = java.lang.Math.cos(r2)
                float r4 = (float) r4
                double r2 = java.lang.Math.sin(r2)
                float r2 = (float) r2
                float r3 = r4 * r27
                float r5 = -r2
                float r5 = r5 * r28
                float r2 = r2 * r27
                float r4 = r4 * r28
                float r6 = r30 - r1
                r7 = 4618760256179416344(0x401921fb54442d18, double:6.283185307179586)
                r9 = 0
                int r10 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                if (r10 >= 0) goto L_0x002f
                if (r31 == 0) goto L_0x002f
                double r9 = (double) r6
                java.lang.Double.isNaN(r9)
                double r9 = r9 + r7
            L_0x002d:
                float r6 = (float) r9
                goto L_0x003b
            L_0x002f:
                int r9 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                if (r9 <= 0) goto L_0x003b
                if (r31 != 0) goto L_0x003b
                double r9 = (double) r6
                java.lang.Double.isNaN(r9)
                double r9 = r9 - r7
                goto L_0x002d
            L_0x003b:
                double r7 = (double) r6
                r9 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
                java.lang.Double.isNaN(r7)
                double r7 = r7 / r9
                double r7 = r0.round(r7)
                double r7 = java.lang.Math.abs(r7)
                double r7 = java.lang.Math.ceil(r7)
                int r7 = (int) r7
                float r8 = (float) r7
                float r6 = r6 / r8
                r8 = 4608683618675807573(0x3ff5555555555555, double:1.3333333333333333)
                r10 = 1082130432(0x40800000, float:4.0)
                float r10 = r6 / r10
                double r10 = (double) r10
                double r10 = java.lang.Math.tan(r10)
                double r10 = r10 * r8
                float r8 = (float) r10
                double r9 = (double) r1
                double r11 = java.lang.Math.cos(r9)
                float r11 = (float) r11
                double r9 = java.lang.Math.sin(r9)
                float r9 = (float) r9
                r10 = 0
            L_0x0071:
                if (r10 >= r7) goto L_0x00e3
                float r12 = r8 * r9
                float r12 = r11 - r12
                float r11 = r11 * r8
                float r9 = r9 + r11
                float r1 = r1 + r6
                double r13 = (double) r1
                r27 = r6
                r11 = r7
                double r6 = java.lang.Math.cos(r13)
                float r6 = (float) r6
                double r13 = java.lang.Math.sin(r13)
                float r7 = (float) r13
                float r13 = r8 * r7
                float r13 = r13 + r6
                float r14 = r8 * r6
                float r14 = r7 - r14
                float r15 = r3 * r12
                float r15 = r25 + r15
                float r16 = r5 * r9
                float r15 = r15 + r16
                float r12 = r12 * r2
                float r12 = r26 + r12
                float r9 = r9 * r4
                float r12 = r12 + r9
                float r9 = r3 * r13
                float r9 = r25 + r9
                float r16 = r5 * r14
                float r9 = r9 + r16
                float r13 = r13 * r2
                float r13 = r26 + r13
                float r14 = r14 * r4
                float r13 = r13 + r14
                float r14 = r3 * r6
                float r14 = r25 + r14
                float r16 = r5 * r7
                float r14 = r14 + r16
                float r16 = r2 * r6
                float r16 = r26 + r16
                float r17 = r4 * r7
                float r16 = r16 + r17
                r28 = r1
                android.graphics.Path r1 = r0.mPath
                r32 = r2
                float r2 = r0.mScale
                float r18 = r15 * r2
                float r19 = r12 * r2
                float r20 = r9 * r2
                float r21 = r13 * r2
                float r22 = r14 * r2
                float r23 = r16 * r2
                r17 = r1
                r17.cubicTo(r18, r19, r20, r21, r22, r23)
                int r10 = r10 + 1
                r1 = r28
                r2 = r32
                r9 = r7
                r7 = r11
                r11 = r6
                r6 = r27
                goto L_0x0071
            L_0x00e3:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.PropHelper.PathParser.arcToBezier(float, float, float, float, float, float, boolean, float):void");
        }

        private void setPenDown() {
            if (!this.mPendDownSet) {
                this.mPenDownX = this.mPenX;
                this.mPenDownY = this.mPenY;
                this.mPendDownSet = true;
            }
        }

        private double round(double d) {
            double pow = Math.pow(10.0d, 4.0d);
            double round = (double) Math.round(d * pow);
            Double.isNaN(round);
            return round / pow;
        }
    }

    PropHelper() {
    }

    static int toMatrixData(ReadableArray readableArray, float[] fArr, float f) {
        int size = readableArray.size();
        if (size != 6) {
            return size;
        }
        fArr[0] = (float) readableArray.getDouble(0);
        fArr[1] = (float) readableArray.getDouble(2);
        fArr[2] = ((float) readableArray.getDouble(4)) * f;
        fArr[3] = (float) readableArray.getDouble(1);
        fArr[4] = (float) readableArray.getDouble(3);
        fArr[5] = ((float) readableArray.getDouble(5)) * f;
        return 6;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00a9, code lost:
        r14 = 1.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00c2, code lost:
        r7 = java.lang.Double.valueOf(r7.substring(0, r8)).doubleValue() * r14;
     */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static double fromRelative(java.lang.String r7, double r8, double r10, double r12, double r14) {
        /*
            java.lang.String r7 = r7.trim()
            int r0 = r7.length()
            int r1 = r0 + -1
            if (r0 == 0) goto L_0x00dd
            java.lang.String r2 = "normal"
            boolean r2 = r7.equals(r2)
            if (r2 == 0) goto L_0x0016
            goto L_0x00dd
        L_0x0016:
            int r2 = r7.codePointAt(r1)
            r3 = 37
            r4 = 0
            if (r2 != r3) goto L_0x0032
            java.lang.String r7 = r7.substring(r4, r1)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            double r12 = r7.doubleValue()
            r14 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r12 = r12 / r14
            double r12 = r12 * r8
            double r12 = r12 + r10
            return r12
        L_0x0032:
            int r8 = r0 + -2
            if (r8 <= 0) goto L_0x00d4
            java.lang.String r9 = r7.substring(r8)
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r3 = -1
            int r5 = r9.hashCode()
            r6 = 3178(0xc6a, float:4.453E-42)
            if (r5 == r6) goto L_0x009a
            r6 = 3240(0xca8, float:4.54E-42)
            if (r5 == r6) goto L_0x0090
            r6 = 3365(0xd25, float:4.715E-42)
            if (r5 == r6) goto L_0x0086
            r6 = 3488(0xda0, float:4.888E-42)
            if (r5 == r6) goto L_0x007c
            r6 = 3571(0xdf3, float:5.004E-42)
            if (r5 == r6) goto L_0x0072
            r6 = 3588(0xe04, float:5.028E-42)
            if (r5 == r6) goto L_0x0068
            r6 = 3592(0xe08, float:5.033E-42)
            if (r5 == r6) goto L_0x005e
            goto L_0x00a4
        L_0x005e:
            java.lang.String r5 = "px"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 0
            goto L_0x00a5
        L_0x0068:
            java.lang.String r5 = "pt"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 2
            goto L_0x00a5
        L_0x0072:
            java.lang.String r5 = "pc"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 3
            goto L_0x00a5
        L_0x007c:
            java.lang.String r5 = "mm"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 4
            goto L_0x00a5
        L_0x0086:
            java.lang.String r5 = "in"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 6
            goto L_0x00a5
        L_0x0090:
            java.lang.String r5 = "em"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 1
            goto L_0x00a5
        L_0x009a:
            java.lang.String r5 = "cm"
            boolean r9 = r9.equals(r5)
            if (r9 == 0) goto L_0x00a4
            r9 = 5
            goto L_0x00a5
        L_0x00a4:
            r9 = -1
        L_0x00a5:
            switch(r9) {
                case 0: goto L_0x00a9;
                case 1: goto L_0x00c2;
                case 2: goto L_0x00c0;
                case 3: goto L_0x00bd;
                case 4: goto L_0x00b7;
                case 5: goto L_0x00b1;
                case 6: goto L_0x00ab;
                default: goto L_0x00a8;
            }
        L_0x00a8:
            r8 = r0
        L_0x00a9:
            r14 = r1
            goto L_0x00c2
        L_0x00ab:
            r14 = 4636033603912859648(0x4056800000000000, double:90.0)
            goto L_0x00c2
        L_0x00b1:
            r14 = 4630183578586017914(0x4041b76ed677707a, double:35.43307)
            goto L_0x00c2
        L_0x00b7:
            r14 = 4615161236842447043(0x400c58b1572580c3, double:3.543307)
            goto L_0x00c2
        L_0x00bd:
            r14 = 4624633867356078080(0x402e000000000000, double:15.0)
            goto L_0x00c2
        L_0x00c0:
            r14 = 4608308318706860032(0x3ff4000000000000, double:1.25)
        L_0x00c2:
            java.lang.String r7 = r7.substring(r4, r8)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            double r7 = r7.doubleValue()
            double r7 = r7 * r14
        L_0x00d0:
            double r7 = r7 * r12
            double r7 = r7 + r10
            return r7
        L_0x00d4:
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            double r7 = r7.doubleValue()
            goto L_0x00d0
        L_0x00dd:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.PropHelper.fromRelative(java.lang.String, double, double, double, double):double");
    }

    static double fromRelative(SVGLength sVGLength, double d, double d2, double d3, double d4) {
        double d5;
        if (sVGLength == null) {
            return d2;
        }
        SVGLengthUnitType sVGLengthUnitType = sVGLength.unit;
        double d6 = sVGLength.value;
        switch (sVGLengthUnitType) {
            case SVG_LENGTHTYPE_NUMBER:
            case SVG_LENGTHTYPE_PX:
                d4 = 1.0d;
                break;
            case SVG_LENGTHTYPE_PERCENTAGE:
                d5 = (d6 / 100.0d) * d;
                break;
            case SVG_LENGTHTYPE_EMS:
                break;
            case SVG_LENGTHTYPE_EXS:
                d4 /= 2.0d;
                break;
            case SVG_LENGTHTYPE_CM:
                d4 = 35.43307d;
                break;
            case SVG_LENGTHTYPE_MM:
                d4 = 3.543307d;
                break;
            case SVG_LENGTHTYPE_IN:
                d4 = 90.0d;
                break;
            case SVG_LENGTHTYPE_PT:
                d4 = 1.25d;
                break;
            case SVG_LENGTHTYPE_PC:
                d4 = 15.0d;
                break;
        }
        d6 *= d4;
        d5 = d6 * d3;
        return d5 + d2;
    }
}
