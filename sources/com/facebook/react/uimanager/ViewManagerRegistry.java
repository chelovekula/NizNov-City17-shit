package com.facebook.react.uimanager;

import androidx.annotation.Nullable;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.UIManagerModule.ViewManagerResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ViewManagerRegistry {
    @Nullable
    private final ViewManagerResolver mViewManagerResolver;
    private final Map<String, ViewManager> mViewManagers;

    public ViewManagerRegistry(ViewManagerResolver viewManagerResolver) {
        this.mViewManagers = MapBuilder.newHashMap();
        this.mViewManagerResolver = viewManagerResolver;
    }

    public ViewManagerRegistry(List<ViewManager> list) {
        HashMap newHashMap = MapBuilder.newHashMap();
        for (ViewManager viewManager : list) {
            newHashMap.put(viewManager.getName(), viewManager);
        }
        this.mViewManagers = newHashMap;
        this.mViewManagerResolver = null;
    }

    public ViewManagerRegistry(Map<String, ViewManager> map) {
        if (map == null) {
            map = MapBuilder.newHashMap();
        }
        this.mViewManagers = map;
        this.mViewManagerResolver = null;
    }

    public ViewManager get(String str) {
        ViewManager viewManager = (ViewManager) this.mViewManagers.get(str);
        if (viewManager != null) {
            return viewManager;
        }
        ViewManagerResolver viewManagerResolver = this.mViewManagerResolver;
        if (viewManagerResolver != null) {
            ViewManager viewManager2 = viewManagerResolver.getViewManager(str);
            if (viewManager2 != null) {
                this.mViewManagers.put(str, viewManager2);
                return viewManager2;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No ViewManager defined for class ");
        sb.append(str);
        throw new IllegalViewOperationException(sb.toString());
    }
}
