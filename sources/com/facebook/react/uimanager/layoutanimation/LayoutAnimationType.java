package com.facebook.react.uimanager.layoutanimation;

enum LayoutAnimationType {
    CREATE,
    UPDATE,
    DELETE;

    /* renamed from: com.facebook.react.uimanager.layoutanimation.LayoutAnimationType$1 */
    static /* synthetic */ class C08561 {

        /* renamed from: $SwitchMap$com$facebook$react$uimanager$layoutanimation$LayoutAnimationType */
        static final /* synthetic */ int[] f55x364296ff = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.facebook.react.uimanager.layoutanimation.LayoutAnimationType[] r0 = com.facebook.react.uimanager.layoutanimation.LayoutAnimationType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f55x364296ff = r0
                int[] r0 = f55x364296ff     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.facebook.react.uimanager.layoutanimation.LayoutAnimationType r1 = com.facebook.react.uimanager.layoutanimation.LayoutAnimationType.CREATE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f55x364296ff     // Catch:{ NoSuchFieldError -> 0x001f }
                com.facebook.react.uimanager.layoutanimation.LayoutAnimationType r1 = com.facebook.react.uimanager.layoutanimation.LayoutAnimationType.UPDATE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f55x364296ff     // Catch:{ NoSuchFieldError -> 0x002a }
                com.facebook.react.uimanager.layoutanimation.LayoutAnimationType r1 = com.facebook.react.uimanager.layoutanimation.LayoutAnimationType.DELETE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.layoutanimation.LayoutAnimationType.C08561.<clinit>():void");
        }
    }

    public static String toString(LayoutAnimationType layoutAnimationType) {
        int i = C08561.f55x364296ff[layoutAnimationType.ordinal()];
        if (i == 1) {
            return "create";
        }
        if (i == 2) {
            return "update";
        }
        if (i == 3) {
            return "delete";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported LayoutAnimationType: ");
        sb.append(layoutAnimationType);
        throw new IllegalArgumentException(sb.toString());
    }
}
