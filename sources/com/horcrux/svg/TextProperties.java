package com.horcrux.svg;

import com.facebook.react.uimanager.ViewProps;
import java.util.HashMap;
import java.util.Map;

class TextProperties {

    enum AlignmentBaseline {
        baseline(r1),
        textBottom("text-bottom"),
        alphabetic(r4),
        ideographic(r5),
        middle(r6),
        central(r7),
        mathematical(r8),
        textTop("text-top"),
        bottom(r9),
        center("center"),
        top(ViewProps.TOP),
        textBeforeEdge("text-before-edge"),
        textAfterEdge("text-after-edge"),
        beforeEdge("before-edge"),
        afterEdge("after-edge"),
        hanging("hanging");
        
        private static final Map<String, AlignmentBaseline> alignmentToEnum = null;
        private final String alignment;

        static {
            int i;
            AlignmentBaseline[] values;
            alignmentToEnum = new HashMap();
            for (AlignmentBaseline alignmentBaseline : values()) {
                alignmentToEnum.put(alignmentBaseline.alignment, alignmentBaseline);
            }
        }

        private AlignmentBaseline(String str) {
            this.alignment = str;
        }

        static AlignmentBaseline getEnum(String str) {
            if (alignmentToEnum.containsKey(str)) {
                return (AlignmentBaseline) alignmentToEnum.get(str);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown String Value: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }

        public String toString() {
            return this.alignment;
        }
    }

    enum Direction {
        ltr,
        rtl
    }

    enum FontStyle {
        normal,
        italic,
        oblique
    }

    enum FontVariantLigatures {
        normal,
        none
    }

    enum FontWeight {
        Normal("normal"),
        Bold("bold"),
        Bolder("bolder"),
        Lighter("lighter"),
        w100("100"),
        w200("200"),
        w300("300"),
        w400("400"),
        w500("500"),
        w600("600"),
        w700("700"),
        w800("800"),
        w900("900");
        
        private static final Map<String, FontWeight> weightToEnum = null;
        private final String weight;

        static {
            int i;
            FontWeight[] values;
            weightToEnum = new HashMap();
            for (FontWeight fontWeight : values()) {
                weightToEnum.put(fontWeight.weight, fontWeight);
            }
        }

        private FontWeight(String str) {
            this.weight = str;
        }

        static FontWeight getEnum(String str) {
            if (weightToEnum.containsKey(str)) {
                return (FontWeight) weightToEnum.get(str);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown String Value: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }

        public String toString() {
            return this.weight;
        }
    }

    enum TextAnchor {
        start,
        middle,
        end
    }

    enum TextDecoration {
        None(ViewProps.NONE),
        Underline("underline"),
        Overline("overline"),
        LineThrough("line-through"),
        Blink("blink");
        
        private static final Map<String, TextDecoration> decorationToEnum = null;
        private final String decoration;

        static {
            int i;
            TextDecoration[] values;
            decorationToEnum = new HashMap();
            for (TextDecoration textDecoration : values()) {
                decorationToEnum.put(textDecoration.decoration, textDecoration);
            }
        }

        private TextDecoration(String str) {
            this.decoration = str;
        }

        static TextDecoration getEnum(String str) {
            if (decorationToEnum.containsKey(str)) {
                return (TextDecoration) decorationToEnum.get(str);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown String Value: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }

        public String toString() {
            return this.decoration;
        }
    }

    enum TextLengthAdjust {
        spacing,
        spacingAndGlyphs
    }

    enum TextPathMethod {
        align,
        stretch
    }

    enum TextPathMidLine {
        sharp,
        smooth
    }

    enum TextPathSide {
        left,
        right
    }

    enum TextPathSpacing {
        auto,
        exact
    }

    TextProperties() {
    }
}
