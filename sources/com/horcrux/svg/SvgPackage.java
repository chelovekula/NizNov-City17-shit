package com.horcrux.svg;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SvgPackage implements ReactPackage {
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new ViewManager[]{new GroupViewManager(), new PathViewManager(), new CircleViewManager(), new EllipseViewManager(), new LineViewManager(), new RectViewManager(), new TextViewManager(), new TSpanViewManager(), new TextPathViewManager(), new ImageViewManager(), new ClipPathViewManager(), new DefsViewManager(), new UseViewManager(), new SymbolManager(), new LinearGradientManager(), new RadialGradientManager(), new PatternManager(), new MaskManager(), new SvgViewManager()});
    }

    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return Collections.singletonList(new SvgViewModule(reactApplicationContext));
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
}
