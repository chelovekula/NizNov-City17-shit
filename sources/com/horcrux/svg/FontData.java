package com.horcrux.svg;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.ViewProps;

class FontData {
    static final double DEFAULT_FONT_SIZE = 12.0d;
    private static final double DEFAULT_KERNING = 0.0d;
    private static final double DEFAULT_LETTER_SPACING = 0.0d;
    private static final double DEFAULT_WORD_SPACING = 0.0d;
    static final FontData Defaults = new FontData();
    private static final String FONT_DATA = "fontData";
    private static final String FONT_FEATURE_SETTINGS = "fontFeatureSettings";
    private static final String FONT_VARIANT_LIGATURES = "fontVariantLigatures";
    private static final String KERNING = "kerning";
    private static final String LETTER_SPACING = "letterSpacing";
    private static final String TEXT_ANCHOR = "textAnchor";
    private static final String TEXT_DECORATION = "textDecoration";
    private static final String WORD_SPACING = "wordSpacing";
    final ReadableMap fontData;
    final String fontFamily;
    final String fontFeatureSettings;
    final double fontSize;
    final FontStyle fontStyle;
    final FontVariantLigatures fontVariantLigatures;
    final FontWeight fontWeight;
    final double kerning;
    final double letterSpacing;
    final boolean manualKerning;
    final TextAnchor textAnchor;
    private final TextDecoration textDecoration;
    final double wordSpacing;

    private FontData() {
        this.fontData = null;
        String str = "";
        this.fontFamily = str;
        this.fontStyle = FontStyle.normal;
        this.fontWeight = FontWeight.Normal;
        this.fontFeatureSettings = str;
        this.fontVariantLigatures = FontVariantLigatures.normal;
        this.textAnchor = TextAnchor.start;
        this.textDecoration = TextDecoration.None;
        this.manualKerning = false;
        this.kerning = 0.0d;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.wordSpacing = 0.0d;
        this.letterSpacing = 0.0d;
    }

    private double toAbsolute(String str, double d, double d2) {
        return PropHelper.fromRelative(str, 0.0d, 0.0d, d, d2);
    }

    FontData(ReadableMap readableMap, FontData fontData2, double d) {
        double d2 = fontData2.fontSize;
        String str = ViewProps.FONT_SIZE;
        if (!readableMap.hasKey(str)) {
            this.fontSize = d2;
        } else if (readableMap.getType(str) == ReadableType.Number) {
            this.fontSize = readableMap.getDouble(str);
        } else {
            this.fontSize = PropHelper.fromRelative(readableMap.getString(str), d2, 0.0d, 1.0d, d2);
        }
        String str2 = FONT_DATA;
        this.fontData = readableMap.hasKey(str2) ? readableMap.getMap(str2) : fontData2.fontData;
        String str3 = ViewProps.FONT_FAMILY;
        this.fontFamily = readableMap.hasKey(str3) ? readableMap.getString(str3) : fontData2.fontFamily;
        String str4 = ViewProps.FONT_STYLE;
        this.fontStyle = readableMap.hasKey(str4) ? FontStyle.valueOf(readableMap.getString(str4)) : fontData2.fontStyle;
        String str5 = ViewProps.FONT_WEIGHT;
        this.fontWeight = readableMap.hasKey(str5) ? FontWeight.getEnum(readableMap.getString(str5)) : fontData2.fontWeight;
        String str6 = FONT_FEATURE_SETTINGS;
        this.fontFeatureSettings = readableMap.hasKey(str6) ? readableMap.getString(str6) : fontData2.fontFeatureSettings;
        String str7 = FONT_VARIANT_LIGATURES;
        this.fontVariantLigatures = readableMap.hasKey(str7) ? FontVariantLigatures.valueOf(readableMap.getString(str7)) : fontData2.fontVariantLigatures;
        String str8 = TEXT_ANCHOR;
        this.textAnchor = readableMap.hasKey(str8) ? TextAnchor.valueOf(readableMap.getString(str8)) : fontData2.textAnchor;
        String str9 = TEXT_DECORATION;
        this.textDecoration = readableMap.hasKey(str9) ? TextDecoration.getEnum(readableMap.getString(str9)) : fontData2.textDecoration;
        String str10 = KERNING;
        boolean hasKey = readableMap.hasKey(str10);
        this.manualKerning = hasKey || fontData2.manualKerning;
        this.kerning = hasKey ? toAbsolute(readableMap.getString(str10), d, this.fontSize) : fontData2.kerning;
        String str11 = WORD_SPACING;
        this.wordSpacing = readableMap.hasKey(str11) ? toAbsolute(readableMap.getString(str11), d, this.fontSize) : fontData2.wordSpacing;
        String str12 = "letterSpacing";
        this.letterSpacing = readableMap.hasKey(str12) ? toAbsolute(readableMap.getString(str12), d, this.fontSize) : fontData2.letterSpacing;
    }
}
