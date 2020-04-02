package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewParent;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.ArrayList;
import javax.annotation.Nullable;

@SuppressLint({"ViewConstructor"})
class TSpanView extends TextView {
    private static final String FONTS = "fonts/";
    private static final String OTF = ".otf";
    private static final String TTF = ".ttf";
    private static final double radToDeg = 57.29577951308232d;
    private static final double tau = 6.283185307179586d;
    ArrayList<String> emoji = new ArrayList<>();
    ArrayList<Matrix> emojiTransforms = new ArrayList<>();
    private Path mCachedPath;
    @Nullable
    String mContent;
    private TextPathView textPath;

    /* renamed from: com.horcrux.svg.TSpanView$1 */
    static /* synthetic */ class C09301 {
        static final /* synthetic */ int[] $SwitchMap$com$horcrux$svg$TextProperties$TextAnchor = new int[TextAnchor.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$horcrux$svg$TextProperties$TextLengthAdjust = new int[TextLengthAdjust.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(43:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|47|48|(3:49|50|52)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(45:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|47|48|49|50|52) */
        /* JADX WARNING: Can't wrap try/catch for region: R(46:0|(2:1|2)|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|47|48|49|50|52) */
        /* JADX WARNING: Can't wrap try/catch for region: R(47:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|47|48|49|50|52) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0072 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x007d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0089 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0095 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00a1 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ad */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00b9 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00c5 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00d1 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00dd */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00fc */
        static {
            /*
                com.horcrux.svg.TextProperties$TextAnchor[] r0 = com.horcrux.svg.TextProperties.TextAnchor.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$horcrux$svg$TextProperties$TextAnchor = r0
                r0 = 1
                int[] r1 = $SwitchMap$com$horcrux$svg$TextProperties$TextAnchor     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.horcrux.svg.TextProperties$TextAnchor r2 = com.horcrux.svg.TextProperties.TextAnchor.start     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$TextAnchor     // Catch:{ NoSuchFieldError -> 0x001f }
                com.horcrux.svg.TextProperties$TextAnchor r3 = com.horcrux.svg.TextProperties.TextAnchor.middle     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = $SwitchMap$com$horcrux$svg$TextProperties$TextAnchor     // Catch:{ NoSuchFieldError -> 0x002a }
                com.horcrux.svg.TextProperties$TextAnchor r4 = com.horcrux.svg.TextProperties.TextAnchor.end     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                com.horcrux.svg.TextProperties$AlignmentBaseline[] r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline = r3
                int[] r3 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x003d }
                com.horcrux.svg.TextProperties$AlignmentBaseline r4 = com.horcrux.svg.TextProperties.AlignmentBaseline.baseline     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r3 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0047 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r4 = com.horcrux.svg.TextProperties.AlignmentBaseline.textBottom     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r3 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0051 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r4 = com.horcrux.svg.TextProperties.AlignmentBaseline.afterEdge     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x005c }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.textAfterEdge     // Catch:{ NoSuchFieldError -> 0x005c }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x005c }
                r4 = 4
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x005c }
            L_0x005c:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0067 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.alphabetic     // Catch:{ NoSuchFieldError -> 0x0067 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0067 }
                r4 = 5
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0067 }
            L_0x0067:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0072 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.ideographic     // Catch:{ NoSuchFieldError -> 0x0072 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0072 }
                r4 = 6
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0072 }
            L_0x0072:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x007d }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.middle     // Catch:{ NoSuchFieldError -> 0x007d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x007d }
                r4 = 7
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x007d }
            L_0x007d:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0089 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.central     // Catch:{ NoSuchFieldError -> 0x0089 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0089 }
                r4 = 8
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0089 }
            L_0x0089:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x0095 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.mathematical     // Catch:{ NoSuchFieldError -> 0x0095 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0095 }
                r4 = 9
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0095 }
            L_0x0095:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00a1 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.hanging     // Catch:{ NoSuchFieldError -> 0x00a1 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a1 }
                r4 = 10
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00a1 }
            L_0x00a1:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00ad }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.textTop     // Catch:{ NoSuchFieldError -> 0x00ad }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ad }
                r4 = 11
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00ad }
            L_0x00ad:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00b9 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.beforeEdge     // Catch:{ NoSuchFieldError -> 0x00b9 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b9 }
                r4 = 12
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00b9 }
            L_0x00b9:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00c5 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.textBeforeEdge     // Catch:{ NoSuchFieldError -> 0x00c5 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c5 }
                r4 = 13
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00c5 }
            L_0x00c5:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00d1 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.bottom     // Catch:{ NoSuchFieldError -> 0x00d1 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d1 }
                r4 = 14
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00d1 }
            L_0x00d1:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00dd }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.center     // Catch:{ NoSuchFieldError -> 0x00dd }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00dd }
                r4 = 15
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00dd }
            L_0x00dd:
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline     // Catch:{ NoSuchFieldError -> 0x00e9 }
                com.horcrux.svg.TextProperties$AlignmentBaseline r3 = com.horcrux.svg.TextProperties.AlignmentBaseline.top     // Catch:{ NoSuchFieldError -> 0x00e9 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e9 }
                r4 = 16
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x00e9 }
            L_0x00e9:
                com.horcrux.svg.TextProperties$TextLengthAdjust[] r2 = com.horcrux.svg.TextProperties.TextLengthAdjust.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$com$horcrux$svg$TextProperties$TextLengthAdjust = r2
                int[] r2 = $SwitchMap$com$horcrux$svg$TextProperties$TextLengthAdjust     // Catch:{ NoSuchFieldError -> 0x00fc }
                com.horcrux.svg.TextProperties$TextLengthAdjust r3 = com.horcrux.svg.TextProperties.TextLengthAdjust.spacing     // Catch:{ NoSuchFieldError -> 0x00fc }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00fc }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x00fc }
            L_0x00fc:
                int[] r0 = $SwitchMap$com$horcrux$svg$TextProperties$TextLengthAdjust     // Catch:{ NoSuchFieldError -> 0x0106 }
                com.horcrux.svg.TextProperties$TextLengthAdjust r2 = com.horcrux.svg.TextProperties.TextLengthAdjust.spacingAndGlyphs     // Catch:{ NoSuchFieldError -> 0x0106 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0106 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0106 }
            L_0x0106:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.TSpanView.C09301.<clinit>():void");
        }
    }

    public TSpanView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(name = "content")
    public void setContent(@Nullable String str) {
        this.mContent = str;
        invalidate();
    }

    public void invalidate() {
        this.mCachedPath = null;
        super.invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void clearCache() {
        this.mCachedPath = null;
        super.clearCache();
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Paint paint, float f) {
        if (this.mContent != null) {
            int size = this.emoji.size();
            if (size > 0) {
                applyTextPropertiesToPaint(paint, getTextRootGlyphContext().getFont());
                for (int i = 0; i < size; i++) {
                    String str = (String) this.emoji.get(i);
                    Matrix matrix = (Matrix) this.emojiTransforms.get(i);
                    canvas.save();
                    canvas.concat(matrix);
                    canvas.drawText(str, 0.0f, 0.0f, paint);
                    canvas.restore();
                }
            }
            drawPath(canvas, paint, f);
            return;
        }
        clip(canvas, paint);
        drawGroup(canvas, paint, f);
    }

    /* access modifiers changed from: 0000 */
    public Path getPath(Canvas canvas, Paint paint) {
        Path path = this.mCachedPath;
        if (path != null) {
            return path;
        }
        if (this.mContent == null) {
            this.mCachedPath = getGroupPath(canvas, paint);
            return this.mCachedPath;
        }
        setupTextPath();
        pushGlyphContext();
        this.mCachedPath = getLinePath(this.mContent, paint, canvas);
        popGlyphContext();
        return this.mCachedPath;
    }

    /* access modifiers changed from: 0000 */
    public double getSubtreeTextChunksTotalAdvance(Paint paint) {
        if (!Double.isNaN(this.cachedAdvance)) {
            return this.cachedAdvance;
        }
        String str = this.mContent;
        int i = 0;
        double d = 0.0d;
        if (str == null) {
            while (i < getChildCount()) {
                View childAt = getChildAt(i);
                if (childAt instanceof TextView) {
                    d += ((TextView) childAt).getSubtreeTextChunksTotalAdvance(paint);
                }
                i++;
            }
            this.cachedAdvance = d;
            return d;
        } else if (str.length() == 0) {
            this.cachedAdvance = 0.0d;
            return 0.0d;
        } else {
            FontData font = getTextRootGlyphContext().getFont();
            applyTextPropertiesToPaint(paint, font);
            double d2 = font.letterSpacing;
            if (d2 == 0.0d && font.fontVariantLigatures == FontVariantLigatures.normal) {
                i = 1;
            }
            if (VERSION.SDK_INT >= 21) {
                StringBuilder sb = new StringBuilder();
                sb.append("'rlig', 'liga', 'clig', 'calt', 'locl', 'ccmp', 'mark', 'mkmk',");
                sb.append("'kern', ");
                String sb2 = sb.toString();
                if (i != 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(sb2);
                    sb3.append("'hlig', 'cala', ");
                    sb3.append(font.fontFeatureSettings);
                    paint.setFontFeatureSettings(sb3.toString());
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(sb2);
                    sb4.append("'liga' 0, 'clig' 0, 'dlig' 0, 'hlig' 0, 'cala' 0, ");
                    sb4.append(font.fontFeatureSettings);
                    paint.setFontFeatureSettings(sb4.toString());
                }
                double d3 = font.fontSize;
                double d4 = (double) this.mScale;
                Double.isNaN(d4);
                paint.setLetterSpacing((float) (d2 / (d3 * d4)));
            }
            this.cachedAdvance = (double) paint.measureText(str);
            return this.cachedAdvance;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01f6, code lost:
        r0 = r0 * r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0224, code lost:
        r0 = -r8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x02d9 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x027c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Path getLinePath(java.lang.String r73, android.graphics.Paint r74, android.graphics.Canvas r75) {
        /*
            r72 = this;
            r6 = r72
            r14 = r74
            r15 = r75
            int r13 = r73.length()
            android.graphics.Path r12 = new android.graphics.Path
            r12.<init>()
            if (r13 != 0) goto L_0x0012
            return r12
        L_0x0012:
            r0 = 0
            com.horcrux.svg.TextPathView r1 = r6.textPath
            r11 = 0
            if (r1 == 0) goto L_0x001b
            r16 = 1
            goto L_0x001d
        L_0x001b:
            r16 = 0
        L_0x001d:
            r17 = 0
            if (r16 == 0) goto L_0x003e
            android.graphics.PathMeasure r0 = new android.graphics.PathMeasure
            com.horcrux.svg.TextPathView r1 = r6.textPath
            android.graphics.Path r1 = r1.getTextPath(r15, r14)
            r0.<init>(r1, r11)
            float r1 = r0.getLength()
            double r1 = (double) r1
            boolean r3 = r0.isClosed()
            int r4 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r4 != 0) goto L_0x003a
            return r12
        L_0x003a:
            r4 = r0
            r8 = r1
            r7 = r3
            goto L_0x0042
        L_0x003e:
            r4 = r0
            r8 = r17
            r7 = 0
        L_0x0042:
            com.horcrux.svg.GlyphContext r5 = r72.getTextRootGlyphContext()
            com.horcrux.svg.FontData r0 = r5.getFont()
            r6.applyTextPropertiesToPaint(r14, r0)
            com.horcrux.svg.GlyphPathBag r2 = new com.horcrux.svg.GlyphPathBag
            r2.<init>(r14)
            boolean[] r3 = new boolean[r13]
            char[] r19 = r73.toCharArray()
            r20 = r12
            double r11 = r0.kerning
            r22 = r11
            double r11 = r0.wordSpacing
            r25 = r11
            double r10 = r0.letterSpacing
            boolean r1 = r0.manualKerning
            r12 = 1
            r27 = r1 ^ 1
            int r1 = (r10 > r17 ? 1 : (r10 == r17 ? 0 : -1))
            if (r1 != 0) goto L_0x0075
            com.horcrux.svg.TextProperties$FontVariantLigatures r1 = r0.fontVariantLigatures
            com.horcrux.svg.TextProperties$FontVariantLigatures r12 = com.horcrux.svg.TextProperties.FontVariantLigatures.normal
            if (r1 != r12) goto L_0x0075
            r1 = 1
            goto L_0x0076
        L_0x0075:
            r1 = 0
        L_0x0076:
            int r12 = android.os.Build.VERSION.SDK_INT
            r28 = r2
            r2 = 21
            if (r12 < r2) goto L_0x00c6
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r12 = "'rlig', 'liga', 'clig', 'calt', 'locl', 'ccmp', 'mark', 'mkmk',"
            r2.append(r12)
            java.lang.String r12 = "'kern', "
            r2.append(r12)
            java.lang.String r2 = r2.toString()
            if (r1 == 0) goto L_0x00ad
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r2)
            java.lang.String r2 = "'hlig', 'cala', "
            r1.append(r2)
            java.lang.String r2 = r0.fontFeatureSettings
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r14.setFontFeatureSettings(r1)
            goto L_0x00c6
        L_0x00ad:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r2)
            java.lang.String r2 = "'liga' 0, 'clig' 0, 'dlig' 0, 'hlig' 0, 'cala' 0, "
            r1.append(r2)
            java.lang.String r2 = r0.fontFeatureSettings
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r14.setFontFeatureSettings(r1)
        L_0x00c6:
            com.facebook.react.bridge.ReadableMap r12 = r0.fontData
            float[] r2 = new float[r13]
            r1 = r73
            r14.getTextWidths(r1, r2)
            com.horcrux.svg.TextProperties$TextAnchor r1 = r0.textAnchor
            r29 = r2
            com.horcrux.svg.TextView r2 = r72.getTextAnchorRoot()
            r30 = r10
            double r10 = r2.getSubtreeTextChunksTotalAdvance(r14)
            double r32 = r6.getTextAnchorOffset(r1, r10)
            r6.applyTextPropertiesToPaint(r14, r0)
            double r43 = r5.getFontSize()
            r45 = -1
            r46 = 4611686018427387904(0x4000000000000000, double:2.0)
            if (r16 == 0) goto L_0x0140
            com.horcrux.svg.TextPathView r0 = r6.textPath
            com.horcrux.svg.TextProperties$TextPathMidLine r0 = r0.getMidLine()
            com.horcrux.svg.TextProperties$TextPathMidLine r2 = com.horcrux.svg.TextProperties.TextPathMidLine.sharp
            if (r0 != r2) goto L_0x00fb
            r34 = 1
            goto L_0x00fd
        L_0x00fb:
            r34 = 0
        L_0x00fd:
            com.horcrux.svg.TextPathView r0 = r6.textPath
            com.horcrux.svg.TextProperties$TextPathSide r0 = r0.getSide()
            com.horcrux.svg.TextProperties$TextPathSide r2 = com.horcrux.svg.TextProperties.TextPathSide.right
            if (r0 != r2) goto L_0x010a
            r35 = -1
            goto L_0x010c
        L_0x010a:
            r35 = 1
        L_0x010c:
            com.horcrux.svg.TextPathView r0 = r6.textPath
            com.horcrux.svg.SVGLength r2 = r0.getStartOffset()
            r0 = r72
            r15 = r1
            r1 = r2
            r48 = r28
            r28 = r3
            r2 = r8
            r50 = r4
            r49 = r5
            r4 = r43
            double r0 = r0.getAbsoluteStartOffset(r1, r2, r4)
            double r32 = r32 + r0
            if (r7 == 0) goto L_0x0138
            double r2 = r8 / r46
            com.horcrux.svg.TextProperties$TextAnchor r4 = com.horcrux.svg.TextProperties.TextAnchor.middle
            if (r15 != r4) goto L_0x0131
            double r2 = -r2
            goto L_0x0133
        L_0x0131:
            r2 = r17
        L_0x0133:
            double r0 = r0 + r2
            double r2 = r0 + r8
            r4 = r0
            goto L_0x013b
        L_0x0138:
            r2 = r8
            r4 = r17
        L_0x013b:
            r1 = r34
            r0 = r35
            goto L_0x014d
        L_0x0140:
            r50 = r4
            r49 = r5
            r48 = r28
            r28 = r3
            r2 = r8
            r4 = r17
            r0 = 1
            r1 = 0
        L_0x014d:
            r51 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            com.horcrux.svg.SVGLength r7 = r6.mTextLength
            if (r7 == 0) goto L_0x0198
            com.horcrux.svg.SVGLength r7 = r6.mTextLength
            int r15 = r75.getWidth()
            r53 = r8
            double r8 = (double) r15
            r37 = 0
            float r15 = r6.mScale
            r55 = r4
            double r4 = (double) r15
            r34 = r7
            r35 = r8
            r39 = r4
            r41 = r43
            double r4 = com.horcrux.svg.PropHelper.fromRelative(r34, r35, r37, r39, r41)
            int r7 = (r4 > r17 ? 1 : (r4 == r17 ? 0 : -1))
            if (r7 < 0) goto L_0x0190
            int[] r7 = com.horcrux.svg.TSpanView.C09301.$SwitchMap$com$horcrux$svg$TextProperties$TextLengthAdjust
            com.horcrux.svg.TextProperties$TextLengthAdjust r8 = r6.mLengthAdjust
            int r8 = r8.ordinal()
            r7 = r7[r8]
            r8 = 2
            if (r7 == r8) goto L_0x018d
            double r4 = r4 - r10
            int r7 = r13 + -1
            double r7 = (double) r7
            java.lang.Double.isNaN(r7)
            double r4 = r4 / r7
            double r10 = r30 + r4
            r30 = r10
            goto L_0x019c
        L_0x018d:
            double r51 = r4 / r10
            goto L_0x019c
        L_0x0190:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Negative textLength value"
            r0.<init>(r1)
            throw r0
        L_0x0198:
            r55 = r4
            r53 = r8
        L_0x019c:
            double r4 = (double) r0
            java.lang.Double.isNaN(r4)
            double r10 = r51 * r4
            android.graphics.Paint$FontMetrics r7 = r74.getFontMetrics()
            float r8 = r7.descent
            double r8 = (double) r8
            float r15 = r7.leading
            r57 = r10
            double r10 = (double) r15
            java.lang.Double.isNaN(r8)
            java.lang.Double.isNaN(r10)
            double r10 = r10 + r8
            float r15 = r7.ascent
            float r15 = -r15
            r59 = r0
            float r0 = r7.leading
            float r15 = r15 + r0
            r60 = r1
            double r0 = (double) r15
            float r7 = r7.top
            float r7 = -r7
            r61 = r2
            double r2 = (double) r7
            java.lang.Double.isNaN(r2)
            double r34 = r2 + r10
            java.lang.String r7 = r72.getBaselineShift()
            com.horcrux.svg.TextProperties$AlignmentBaseline r15 = r72.getAlignmentBaseline()
            if (r15 == 0) goto L_0x0226
            int[] r36 = com.horcrux.svg.TSpanView.C09301.$SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline
            int r37 = r15.ordinal()
            r36 = r36[r37]
            switch(r36) {
                case 2: goto L_0x0220;
                case 3: goto L_0x0220;
                case 4: goto L_0x0220;
                case 5: goto L_0x0226;
                case 6: goto L_0x021b;
                case 7: goto L_0x0204;
                case 8: goto L_0x01f9;
                case 9: goto L_0x01f1;
                case 10: goto L_0x01e8;
                case 11: goto L_0x0202;
                case 12: goto L_0x0202;
                case 13: goto L_0x0202;
                case 14: goto L_0x01e6;
                case 15: goto L_0x01e3;
                case 16: goto L_0x01e1;
                default: goto L_0x01e0;
            }
        L_0x01e0:
            goto L_0x0226
        L_0x01e1:
            r0 = r2
            goto L_0x0202
        L_0x01e3:
            double r0 = r34 / r46
            goto L_0x0202
        L_0x01e6:
            r0 = r10
            goto L_0x0202
        L_0x01e8:
            r2 = 4605380978949069210(0x3fe999999999999a, double:0.8)
            java.lang.Double.isNaN(r0)
            goto L_0x01f6
        L_0x01f1:
            r2 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Double.isNaN(r0)
        L_0x01f6:
            double r0 = r0 * r2
            goto L_0x0202
        L_0x01f9:
            java.lang.Double.isNaN(r0)
            java.lang.Double.isNaN(r8)
            double r0 = r0 - r8
            double r0 = r0 / r46
        L_0x0202:
            r2 = 0
            goto L_0x0229
        L_0x0204:
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            java.lang.String r1 = "x"
            r2 = 0
            r3 = 1
            r14.getTextBounds(r1, r2, r3, r0)
            int r0 = r0.height()
            double r0 = (double) r0
            java.lang.Double.isNaN(r0)
            double r0 = r0 / r46
            goto L_0x0229
        L_0x021b:
            r2 = 0
            java.lang.Double.isNaN(r8)
            goto L_0x0224
        L_0x0220:
            r2 = 0
            java.lang.Double.isNaN(r8)
        L_0x0224:
            double r0 = -r8
            goto L_0x0229
        L_0x0226:
            r2 = 0
            r0 = r17
        L_0x0229:
            if (r7 == 0) goto L_0x0315
            boolean r3 = r7.isEmpty()
            if (r3 != 0) goto L_0x0315
            int[] r3 = com.horcrux.svg.TSpanView.C09301.$SwitchMap$com$horcrux$svg$TextProperties$AlignmentBaseline
            int r8 = r15.ordinal()
            r3 = r3[r8]
            r8 = 14
            if (r3 == r8) goto L_0x0315
            r8 = 16
            if (r3 == r8) goto L_0x0315
            int r3 = r7.hashCode()
            r8 = -1720785339(0xffffffff996ee645, float:-1.2350814E-23)
            if (r3 == r8) goto L_0x0269
            r8 = 114240(0x1be40, float:1.60084E-40)
            if (r3 == r8) goto L_0x025f
            r8 = 109801339(0x68b6f7b, float:5.2449795E-35)
            if (r3 == r8) goto L_0x0255
            goto L_0x0273
        L_0x0255:
            java.lang.String r3 = "super"
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x0273
            r3 = 1
            goto L_0x0274
        L_0x025f:
            java.lang.String r3 = "sub"
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x0273
            r3 = 0
            goto L_0x0274
        L_0x0269:
            java.lang.String r3 = "baseline"
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x0273
            r3 = 2
            goto L_0x0274
        L_0x0273:
            r3 = -1
        L_0x0274:
            java.lang.String r8 = "os2"
            java.lang.String r9 = "unitsPerEm"
            java.lang.String r10 = "tables"
            if (r3 == 0) goto L_0x02d9
            r11 = 1
            if (r3 == r11) goto L_0x029c
            r11 = 2
            if (r3 == r11) goto L_0x0315
            float r3 = r6.mScale
            double r8 = (double) r3
            java.lang.Double.isNaN(r8)
            double r35 = r8 * r43
            r37 = 0
            float r3 = r6.mScale
            double r8 = (double) r3
            r34 = r7
            r39 = r8
            r41 = r43
            double r7 = com.horcrux.svg.PropHelper.fromRelative(r34, r35, r37, r39, r41)
            double r0 = r0 - r7
            goto L_0x0315
        L_0x029c:
            if (r12 == 0) goto L_0x0315
            boolean r3 = r12.hasKey(r10)
            if (r3 == 0) goto L_0x0315
            boolean r3 = r12.hasKey(r9)
            if (r3 == 0) goto L_0x0315
            int r3 = r12.getInt(r9)
            com.facebook.react.bridge.ReadableMap r7 = r12.getMap(r10)
            boolean r9 = r7.hasKey(r8)
            if (r9 == 0) goto L_0x0315
            com.facebook.react.bridge.ReadableMap r7 = r7.getMap(r8)
            java.lang.String r8 = "ySuperscriptYOffset"
            boolean r9 = r7.hasKey(r8)
            if (r9 == 0) goto L_0x0315
            double r7 = r7.getDouble(r8)
            float r9 = r6.mScale
            double r9 = (double) r9
            java.lang.Double.isNaN(r9)
            double r9 = r9 * r43
            double r9 = r9 * r7
            double r7 = (double) r3
            java.lang.Double.isNaN(r7)
            double r9 = r9 / r7
            double r0 = r0 - r9
            goto L_0x0315
        L_0x02d9:
            if (r12 == 0) goto L_0x0315
            boolean r3 = r12.hasKey(r10)
            if (r3 == 0) goto L_0x0315
            boolean r3 = r12.hasKey(r9)
            if (r3 == 0) goto L_0x0315
            int r3 = r12.getInt(r9)
            com.facebook.react.bridge.ReadableMap r7 = r12.getMap(r10)
            boolean r9 = r7.hasKey(r8)
            if (r9 == 0) goto L_0x0315
            com.facebook.react.bridge.ReadableMap r7 = r7.getMap(r8)
            java.lang.String r8 = "ySubscriptYOffset"
            boolean r9 = r7.hasKey(r8)
            if (r9 == 0) goto L_0x0315
            double r7 = r7.getDouble(r8)
            float r9 = r6.mScale
            double r9 = (double) r9
            java.lang.Double.isNaN(r9)
            double r9 = r9 * r43
            double r9 = r9 * r7
            double r7 = (double) r3
            java.lang.Double.isNaN(r7)
            double r9 = r9 / r7
            double r0 = r0 + r9
        L_0x0315:
            android.graphics.Matrix r3 = new android.graphics.Matrix
            r3.<init>()
            android.graphics.Matrix r15 = new android.graphics.Matrix
            r15.<init>()
            android.graphics.Matrix r12 = new android.graphics.Matrix
            r12.<init>()
            r7 = 9
            float[] r11 = new float[r7]
            float[] r10 = new float[r7]
            java.util.ArrayList<java.lang.String> r7 = r6.emoji
            r7.clear()
            java.util.ArrayList<android.graphics.Matrix> r7 = r6.emojiTransforms
            r7.clear()
            r9 = 0
        L_0x0335:
            if (r9 >= r13) goto L_0x05d1
            char r7 = r19[r9]
            java.lang.String r8 = java.lang.String.valueOf(r7)
            boolean r21 = r28[r9]
            r2 = 0
            if (r21 == 0) goto L_0x034a
            java.lang.String r8 = ""
            r2 = r8
            r37 = r13
            r35 = 0
            goto L_0x0385
        L_0x034a:
            r63 = r8
            r8 = r9
            r24 = 1
            r35 = 0
        L_0x0351:
            int r8 = r8 + 1
            if (r8 >= r13) goto L_0x0380
            r36 = r29[r8]
            int r36 = (r36 > r2 ? 1 : (r36 == r2 ? 0 : -1))
            if (r36 <= 0) goto L_0x035c
            goto L_0x0380
        L_0x035c:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r37 = r13
            r13 = r63
            r2.append(r13)
            char r13 = r19[r8]
            java.lang.String r13 = java.lang.String.valueOf(r13)
            r2.append(r13)
            java.lang.String r63 = r2.toString()
            r2 = 1
            r28[r8] = r2
            r13 = r37
            r2 = 0
            r24 = 1
            r35 = 1
            goto L_0x0351
        L_0x0380:
            r37 = r13
            r13 = r63
            r2 = r13
        L_0x0385:
            float r8 = r14.measureText(r2)
            double r13 = (double) r8
            java.lang.Double.isNaN(r13)
            double r13 = r13 * r51
            if (r27 == 0) goto L_0x039f
            r8 = r29[r9]
            r38 = r9
            double r8 = (double) r8
            java.lang.Double.isNaN(r8)
            double r8 = r8 * r51
            double r8 = r8 - r13
            r22 = r8
            goto L_0x03a1
        L_0x039f:
            r38 = r9
        L_0x03a1:
            r8 = 32
            if (r7 != r8) goto L_0x03a7
            r8 = 1
            goto L_0x03a8
        L_0x03a7:
            r8 = 0
        L_0x03a8:
            if (r8 == 0) goto L_0x03ad
            r39 = r25
            goto L_0x03af
        L_0x03ad:
            r39 = r17
        L_0x03af:
            double r39 = r39 + r30
            double r39 = r13 + r39
            if (r21 == 0) goto L_0x03bc
            r41 = r7
            r6 = r17
            r9 = r49
            goto L_0x03c6
        L_0x03bc:
            double r41 = r22 + r39
            r9 = r49
            r70 = r41
            r41 = r7
            r6 = r70
        L_0x03c6:
            double r6 = r9.nextX(r6)
            r42 = r0
            double r0 = r9.nextY()
            double r44 = r9.nextDeltaX()
            double r63 = r9.nextDeltaY()
            r65 = r0
            double r0 = r9.nextRotation()
            if (r21 != 0) goto L_0x058b
            if (r8 == 0) goto L_0x03e4
            goto L_0x058b
        L_0x03e4:
            java.lang.Double.isNaN(r4)
            double r39 = r39 * r4
            java.lang.Double.isNaN(r4)
            double r13 = r13 * r4
            double r6 = r6 + r44
            java.lang.Double.isNaN(r4)
            double r6 = r6 * r4
            double r6 = r32 + r6
            double r6 = r6 - r39
            if (r16 == 0) goto L_0x04cd
            r49 = r9
            double r8 = r6 + r13
            double r13 = r13 / r46
            r39 = r0
            double r0 = r6 + r13
            int r21 = (r0 > r61 ? 1 : (r0 == r61 ? 0 : -1))
            if (r21 <= 0) goto L_0x042b
        L_0x0409:
            r8 = r75
            r14 = r10
            r6 = r11
            r1 = r15
            r9 = r20
            r24 = r25
            r34 = r37
            r44 = r38
            r0 = r48
            r38 = r49
            r35 = r50
            r40 = r53
            r49 = r57
            r15 = r59
            r2 = 1
            r26 = 0
            r10 = r72
            r11 = r74
            goto L_0x05ab
        L_0x042b:
            int r21 = (r0 > r55 ? 1 : (r0 == r55 ? 0 : -1))
            if (r21 >= 0) goto L_0x0430
            goto L_0x0409
        L_0x0430:
            r21 = r2
            r2 = 3
            if (r60 == 0) goto L_0x043f
            float r0 = (float) r0
            r1 = r50
            r1.getMatrix(r0, r15, r2)
            r2 = r1
            r1 = r15
            goto L_0x04b4
        L_0x043f:
            r2 = r50
            int r45 = (r6 > r17 ? 1 : (r6 == r17 ? 0 : -1))
            if (r45 >= 0) goto L_0x0452
            r67 = r13
            r13 = 3
            r14 = 0
            r2.getMatrix(r14, r3, r13)
            float r6 = (float) r6
            r3.preTranslate(r6, r14)
            r13 = 1
            goto L_0x0459
        L_0x0452:
            r67 = r13
            float r6 = (float) r6
            r13 = 1
            r2.getMatrix(r6, r3, r13)
        L_0x0459:
            float r0 = (float) r0
            r2.getMatrix(r0, r15, r13)
            int r0 = (r8 > r53 ? 1 : (r8 == r53 ? 0 : -1))
            if (r0 <= 0) goto L_0x046f
            r0 = r53
            float r6 = (float) r0
            r7 = 3
            r2.getMatrix(r6, r12, r7)
            double r8 = r8 - r0
            float r6 = (float) r8
            r7 = 0
            r12.preTranslate(r6, r7)
            goto L_0x0475
        L_0x046f:
            r0 = r53
            float r6 = (float) r8
            r2.getMatrix(r6, r12, r13)
        L_0x0475:
            r3.getValues(r11)
            r12.getValues(r10)
            r14 = 2
            r6 = r11[r14]
            double r6 = (double) r6
            r8 = 5
            r9 = r11[r8]
            double r8 = (double) r9
            r13 = r10[r14]
            r44 = r15
            double r14 = (double) r13
            r13 = 5
            r13 = r10[r13]
            r53 = r0
            double r0 = (double) r13
            java.lang.Double.isNaN(r14)
            java.lang.Double.isNaN(r6)
            double r14 = r14 - r6
            java.lang.Double.isNaN(r0)
            java.lang.Double.isNaN(r8)
            double r0 = r0 - r8
            double r0 = java.lang.Math.atan2(r0, r14)
            r6 = 4633260481411531256(0x404ca5dc1a63c1f8, double:57.29577951308232)
            double r0 = r0 * r6
            java.lang.Double.isNaN(r4)
            double r0 = r0 * r4
            float r0 = (float) r0
            r1 = r44
            r1.preRotate(r0)
            r13 = r67
        L_0x04b4:
            double r6 = -r13
            float r0 = (float) r6
            double r6 = r63 + r42
            float r6 = (float) r6
            r1.preTranslate(r0, r6)
            r13 = r57
            float r0 = (float) r13
            r15 = r59
            float r6 = (float) r15
            r1.preScale(r0, r6)
            r8 = r65
            float r0 = (float) r8
            r6 = 0
            r1.postTranslate(r6, r0)
            goto L_0x04e5
        L_0x04cd:
            r39 = r0
            r21 = r2
            r49 = r9
            r1 = r15
            r2 = r50
            r13 = r57
            r15 = r59
            r8 = r65
            float r0 = (float) r6
            double r6 = r8 + r63
            double r6 = r6 + r42
            float r6 = (float) r6
            r1.setTranslate(r0, r6)
        L_0x04e5:
            r6 = r39
            float r0 = (float) r6
            r1.preRotate(r0)
            if (r35 == 0) goto L_0x0526
            android.graphics.Path r0 = new android.graphics.Path
            r0.<init>()
            r9 = 0
            int r6 = r21.length()
            r35 = 0
            r39 = 0
            r7 = r74
            r40 = r53
            r8 = r21
            r44 = r38
            r38 = r49
            r49 = r13
            r13 = 1
            r14 = r10
            r10 = r6
            r6 = r11
            r24 = r25
            r26 = 0
            r11 = r35
            r69 = r20
            r20 = r12
            r12 = r39
            r35 = r2
            r34 = r37
            r2 = 1
            r13 = r0
            r7.getTextPath(r8, r9, r10, r11, r12, r13)
            r7 = r0
            r13 = r21
            r0 = r48
            goto L_0x0547
        L_0x0526:
            r35 = r2
            r6 = r11
            r69 = r20
            r24 = r25
            r34 = r37
            r44 = r38
            r7 = r41
            r0 = r48
            r38 = r49
            r40 = r53
            r2 = 1
            r26 = 0
            r20 = r12
            r49 = r13
            r13 = r21
            r14 = r10
            android.graphics.Path r7 = r0.getOrCreateAndCache(r7, r13)
        L_0x0547:
            android.graphics.RectF r8 = new android.graphics.RectF
            r8.<init>()
            r7.computeBounds(r8, r2)
            float r8 = r8.width()
            r9 = 0
            int r8 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1))
            if (r8 != 0) goto L_0x057c
            r75.save()
            r8 = r75
            r8.concat(r1)
            r10 = r72
            java.util.ArrayList<java.lang.String> r7 = r10.emoji
            r7.add(r13)
            java.util.ArrayList<android.graphics.Matrix> r7 = r10.emojiTransforms
            android.graphics.Matrix r11 = new android.graphics.Matrix
            r11.<init>(r1)
            r7.add(r11)
            r11 = r74
            r8.drawText(r13, r9, r9, r11)
            r75.restore()
            r9 = r69
            goto L_0x05ad
        L_0x057c:
            r10 = r72
            r11 = r74
            r8 = r75
            r7.transform(r1)
            r9 = r69
            r9.addPath(r7)
            goto L_0x05ad
        L_0x058b:
            r8 = r75
            r14 = r10
            r6 = r11
            r1 = r15
            r24 = r25
            r34 = r37
            r44 = r38
            r0 = r48
            r35 = r50
            r40 = r53
            r49 = r57
            r15 = r59
            r2 = 1
            r26 = 0
            r10 = r72
            r11 = r74
            r38 = r9
            r9 = r20
        L_0x05ab:
            r20 = r12
        L_0x05ad:
            int r7 = r44 + 1
            r48 = r0
            r59 = r15
            r12 = r20
            r25 = r24
            r13 = r34
            r53 = r40
            r57 = r49
            r2 = 0
            r15 = r1
            r20 = r9
            r50 = r35
            r49 = r38
            r0 = r42
            r9 = r7
            r70 = r11
            r11 = r6
            r6 = r10
            r10 = r14
            r14 = r70
            goto L_0x0335
        L_0x05d1:
            r10 = r6
            r9 = r20
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.TSpanView.getLinePath(java.lang.String, android.graphics.Paint, android.graphics.Canvas):android.graphics.Path");
    }

    private double getAbsoluteStartOffset(SVGLength sVGLength, double d, double d2) {
        return PropHelper.fromRelative(sVGLength, d, 0.0d, (double) this.mScale, d2);
    }

    private double getTextAnchorOffset(TextAnchor textAnchor, double d) {
        int i = C09301.$SwitchMap$com$horcrux$svg$TextProperties$TextAnchor[textAnchor.ordinal()];
        if (i == 2) {
            return (-d) / 2.0d;
        }
        if (i != 3) {
            return 0.0d;
        }
        return -d;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r5 = new java.lang.StringBuilder();
        r5.append(r0);
        r5.append(r11);
        r5.append(TTF);
        r4 = android.graphics.Typeface.createFromAsset(r1, r5.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r4 = com.facebook.react.views.text.ReactFontManager.getInstance().getTypeface(r11, r6, r1);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0054 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x006d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyTextPropertiesToPaint(android.graphics.Paint r10, com.horcrux.svg.FontData r11) {
        /*
            r9 = this;
            java.lang.String r0 = "fonts/"
            com.facebook.react.bridge.ReactContext r1 = r9.mContext
            android.content.res.Resources r1 = r1.getResources()
            android.content.res.AssetManager r1 = r1.getAssets()
            double r2 = r11.fontSize
            float r4 = r9.mScale
            double r4 = (double) r4
            java.lang.Double.isNaN(r4)
            double r2 = r2 * r4
            com.horcrux.svg.TextProperties$FontWeight r4 = r11.fontWeight
            com.horcrux.svg.TextProperties$FontWeight r5 = com.horcrux.svg.TextProperties.FontWeight.Bold
            r6 = 1
            r7 = 0
            if (r4 != r5) goto L_0x0020
            r4 = 1
            goto L_0x0021
        L_0x0020:
            r4 = 0
        L_0x0021:
            com.horcrux.svg.TextProperties$FontStyle r5 = r11.fontStyle
            com.horcrux.svg.TextProperties$FontStyle r8 = com.horcrux.svg.TextProperties.FontStyle.italic
            if (r5 != r8) goto L_0x0029
            r5 = 1
            goto L_0x002a
        L_0x0029:
            r5 = 0
        L_0x002a:
            if (r4 == 0) goto L_0x0030
            if (r5 == 0) goto L_0x0030
            r6 = 3
            goto L_0x0038
        L_0x0030:
            if (r4 == 0) goto L_0x0033
            goto L_0x0038
        L_0x0033:
            if (r5 == 0) goto L_0x0037
            r6 = 2
            goto L_0x0038
        L_0x0037:
            r6 = 0
        L_0x0038:
            r4 = 0
            java.lang.String r11 = r11.fontFamily
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0054 }
            r5.<init>()     // Catch:{ Exception -> 0x0054 }
            r5.append(r0)     // Catch:{ Exception -> 0x0054 }
            r5.append(r11)     // Catch:{ Exception -> 0x0054 }
            java.lang.String r7 = ".otf"
            r5.append(r7)     // Catch:{ Exception -> 0x0054 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0054 }
            android.graphics.Typeface r4 = android.graphics.Typeface.createFromAsset(r1, r5)     // Catch:{ Exception -> 0x0054 }
            goto L_0x0077
        L_0x0054:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006d }
            r5.<init>()     // Catch:{ Exception -> 0x006d }
            r5.append(r0)     // Catch:{ Exception -> 0x006d }
            r5.append(r11)     // Catch:{ Exception -> 0x006d }
            java.lang.String r0 = ".ttf"
            r5.append(r0)     // Catch:{ Exception -> 0x006d }
            java.lang.String r0 = r5.toString()     // Catch:{ Exception -> 0x006d }
            android.graphics.Typeface r4 = android.graphics.Typeface.createFromAsset(r1, r0)     // Catch:{ Exception -> 0x006d }
            goto L_0x0077
        L_0x006d:
            com.facebook.react.views.text.ReactFontManager r0 = com.facebook.react.views.text.ReactFontManager.getInstance()     // Catch:{ Exception -> 0x0076 }
            android.graphics.Typeface r4 = r0.getTypeface(r11, r6, r1)     // Catch:{ Exception -> 0x0076 }
            goto L_0x0077
        L_0x0076:
        L_0x0077:
            r10.setTypeface(r4)
            float r11 = (float) r2
            r10.setTextSize(r11)
            android.graphics.Paint$Align r11 = android.graphics.Paint.Align.LEFT
            r10.setTextAlign(r11)
            int r11 = android.os.Build.VERSION.SDK_INT
            r0 = 21
            if (r11 < r0) goto L_0x008d
            r11 = 0
            r10.setLetterSpacing(r11)
        L_0x008d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.horcrux.svg.TSpanView.applyTextPropertiesToPaint(android.graphics.Paint, com.horcrux.svg.FontData):void");
    }

    private void setupTextPath() {
        ViewParent parent = getParent();
        while (parent != null) {
            if (parent.getClass() == TextPathView.class) {
                this.textPath = (TextPathView) parent;
                return;
            } else if (parent instanceof TextView) {
                parent = parent.getParent();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int hitTest(float[] fArr) {
        if (this.mContent == null) {
            return super.hitTest(fArr);
        }
        if (this.mPath != null && this.mInvertible && this.mTransformInvertible) {
            float[] fArr2 = new float[2];
            this.mInvMatrix.mapPoints(fArr2, fArr);
            this.mInvTransform.mapPoints(fArr2);
            int round = Math.round(fArr2[0]);
            int round2 = Math.round(fArr2[1]);
            if (this.mRegion == null && this.mFillPath != null) {
                this.mRegion = getRegion(this.mFillPath);
            }
            if (this.mRegion == null && this.mPath != null) {
                this.mRegion = getRegion(this.mPath);
            }
            if (this.mStrokeRegion == null && this.mStrokePath != null) {
                this.mStrokeRegion = getRegion(this.mStrokePath);
            }
            if ((this.mRegion != null && this.mRegion.contains(round, round2)) || (this.mStrokeRegion != null && this.mStrokeRegion.contains(round, round2))) {
                Path clipPath = getClipPath();
                if (clipPath != null) {
                    if (this.mClipRegionPath != clipPath) {
                        this.mClipRegionPath = clipPath;
                        this.mClipRegion = getRegion(clipPath);
                    }
                    if (!this.mClipRegion.contains(round, round2)) {
                        return -1;
                    }
                }
                return getId();
            }
        }
        return -1;
    }
}
