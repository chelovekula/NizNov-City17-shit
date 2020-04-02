package com.google.maps.android.data.kml;

import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class KmlStyleParser {
    private static final String COLOR_STYLE_COLOR = "color";
    private static final String COLOR_STYLE_MODE = "colorMode";
    private static final String ICON_STYLE_HEADING = "heading";
    private static final String ICON_STYLE_HOTSPOT = "hotSpot";
    private static final String ICON_STYLE_SCALE = "scale";
    private static final String ICON_STYLE_URL = "Icon";
    private static final String LINE_STYLE_WIDTH = "width";
    private static final String POLY_STYLE_FILL = "fill";
    private static final String POLY_STYLE_OUTLINE = "outline";
    private static final String STYLE_MAP_KEY = "key";
    private static final String STYLE_MAP_NORMAL_STYLE = "normal";
    private static final String STYLE_TAG = "styleUrl";

    KmlStyleParser() {
    }

    static KmlStyle createStyle(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        KmlStyle kmlStyle = new KmlStyle();
        setStyleId(xmlPullParser.getAttributeValue(null, "id"), kmlStyle);
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == 3 && xmlPullParser.getName().equals("Style")) {
                return kmlStyle;
            }
            if (eventType == 2) {
                if (xmlPullParser.getName().equals("IconStyle")) {
                    createIconStyle(xmlPullParser, kmlStyle);
                } else if (xmlPullParser.getName().equals("LineStyle")) {
                    createLineStyle(xmlPullParser, kmlStyle);
                } else if (xmlPullParser.getName().equals("PolyStyle")) {
                    createPolyStyle(xmlPullParser, kmlStyle);
                } else if (xmlPullParser.getName().equals("BalloonStyle")) {
                    createBalloonStyle(xmlPullParser, kmlStyle);
                }
            }
            eventType = xmlPullParser.next();
        }
    }

    private static void setStyleId(String str, KmlStyle kmlStyle) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(str);
            kmlStyle.setStyleId(sb.toString());
        }
    }

    private static void createIconStyle(XmlPullParser xmlPullParser, KmlStyle kmlStyle) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 3 || !xmlPullParser.getName().equals("IconStyle")) {
                if (eventType == 2) {
                    if (xmlPullParser.getName().equals(ICON_STYLE_HEADING)) {
                        kmlStyle.setHeading(Float.parseFloat(xmlPullParser.nextText()));
                    } else if (xmlPullParser.getName().equals(ICON_STYLE_URL)) {
                        setIconUrl(xmlPullParser, kmlStyle);
                    } else if (xmlPullParser.getName().equals(ICON_STYLE_HOTSPOT)) {
                        setIconHotSpot(xmlPullParser, kmlStyle);
                    } else if (xmlPullParser.getName().equals(ICON_STYLE_SCALE)) {
                        kmlStyle.setIconScale(Double.parseDouble(xmlPullParser.nextText()));
                    } else if (xmlPullParser.getName().equals("color")) {
                        kmlStyle.setMarkerColor(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals(COLOR_STYLE_MODE)) {
                        kmlStyle.setIconColorMode(xmlPullParser.nextText());
                    }
                }
                eventType = xmlPullParser.next();
            } else {
                return;
            }
        }
    }

    static HashMap<String, String> createStyleMap(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        Boolean valueOf = Boolean.valueOf(false);
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(xmlPullParser.getAttributeValue(null, "id"));
        String sb2 = sb.toString();
        int eventType = xmlPullParser.getEventType();
        Boolean bool = valueOf;
        while (true) {
            if (eventType == 3 && xmlPullParser.getName().equals("StyleMap")) {
                return hashMap;
            }
            if (eventType == 2) {
                if (xmlPullParser.getName().equals(STYLE_MAP_KEY) && xmlPullParser.nextText().equals(STYLE_MAP_NORMAL_STYLE)) {
                    bool = Boolean.valueOf(true);
                } else if (xmlPullParser.getName().equals(STYLE_TAG) && bool.booleanValue()) {
                    hashMap.put(sb2, xmlPullParser.nextText());
                    bool = valueOf;
                }
            }
            eventType = xmlPullParser.next();
        }
    }

    private static void createBalloonStyle(XmlPullParser xmlPullParser, KmlStyle kmlStyle) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 3 || !xmlPullParser.getName().equals("BalloonStyle")) {
                if (eventType == 2 && xmlPullParser.getName().equals("text")) {
                    kmlStyle.setInfoWindowText(xmlPullParser.nextText());
                }
                eventType = xmlPullParser.next();
            } else {
                return;
            }
        }
    }

    private static void setIconUrl(XmlPullParser xmlPullParser, KmlStyle kmlStyle) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 3 || !xmlPullParser.getName().equals(ICON_STYLE_URL)) {
                if (eventType == 2 && xmlPullParser.getName().equals("href")) {
                    kmlStyle.setIconUrl(xmlPullParser.nextText());
                }
                eventType = xmlPullParser.next();
            } else {
                return;
            }
        }
    }

    private static void setIconHotSpot(XmlPullParser xmlPullParser, KmlStyle kmlStyle) {
        Float valueOf = Float.valueOf(Float.parseFloat(xmlPullParser.getAttributeValue(null, "x")));
        Float valueOf2 = Float.valueOf(Float.parseFloat(xmlPullParser.getAttributeValue(null, "y")));
        kmlStyle.setHotSpot(valueOf.floatValue(), valueOf2.floatValue(), xmlPullParser.getAttributeValue(null, "xunits"), xmlPullParser.getAttributeValue(null, "yunits"));
    }

    private static void createLineStyle(XmlPullParser xmlPullParser, KmlStyle kmlStyle) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 3 || !xmlPullParser.getName().equals("LineStyle")) {
                if (eventType == 2) {
                    if (xmlPullParser.getName().equals("color")) {
                        kmlStyle.setOutlineColor(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals("width")) {
                        kmlStyle.setWidth(Float.valueOf(xmlPullParser.nextText()));
                    } else if (xmlPullParser.getName().equals(COLOR_STYLE_MODE)) {
                        kmlStyle.setLineColorMode(xmlPullParser.nextText());
                    }
                }
                eventType = xmlPullParser.next();
            } else {
                return;
            }
        }
    }

    private static void createPolyStyle(XmlPullParser xmlPullParser, KmlStyle kmlStyle) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 3 || !xmlPullParser.getName().equals("PolyStyle")) {
                if (eventType == 2) {
                    if (xmlPullParser.getName().equals("color")) {
                        kmlStyle.setFillColor(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals(POLY_STYLE_OUTLINE)) {
                        kmlStyle.setOutline(KmlBoolean.parseBoolean(xmlPullParser.nextText()));
                    } else if (xmlPullParser.getName().equals(POLY_STYLE_FILL)) {
                        kmlStyle.setFill(KmlBoolean.parseBoolean(xmlPullParser.nextText()));
                    } else if (xmlPullParser.getName().equals(COLOR_STYLE_MODE)) {
                        kmlStyle.setPolyColorMode(xmlPullParser.nextText());
                    }
                }
                eventType = xmlPullParser.next();
            } else {
                return;
            }
        }
    }
}
