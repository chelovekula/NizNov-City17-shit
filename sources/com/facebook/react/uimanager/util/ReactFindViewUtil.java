package com.facebook.react.uimanager.util;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.facebook.react.C0604R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ReactFindViewUtil {
    private static final Map<OnMultipleViewsFoundListener, Set<String>> mOnMultipleViewsFoundListener = new HashMap();
    private static final List<OnViewFoundListener> mOnViewFoundListeners = new ArrayList();

    public interface OnMultipleViewsFoundListener {
        void onViewFound(View view, String str);
    }

    public interface OnViewFoundListener {
        String getNativeId();

        void onViewFound(View view);
    }

    @Nullable
    public static View findView(View view, String str) {
        String nativeId = getNativeId(view);
        if (nativeId != null && nativeId.equals(str)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View findView = findView(viewGroup.getChildAt(i), str);
                if (findView != null) {
                    return findView;
                }
            }
        }
        return null;
    }

    public static void findView(View view, OnViewFoundListener onViewFoundListener) {
        View findView = findView(view, onViewFoundListener.getNativeId());
        if (findView != null) {
            onViewFoundListener.onViewFound(findView);
        }
        addViewListener(onViewFoundListener);
    }

    public static void addViewListener(OnViewFoundListener onViewFoundListener) {
        mOnViewFoundListeners.add(onViewFoundListener);
    }

    public static void removeViewListener(OnViewFoundListener onViewFoundListener) {
        mOnViewFoundListeners.remove(onViewFoundListener);
    }

    public static void addViewsListener(OnMultipleViewsFoundListener onMultipleViewsFoundListener, Set<String> set) {
        mOnMultipleViewsFoundListener.put(onMultipleViewsFoundListener, set);
    }

    public static void removeViewsListener(OnMultipleViewsFoundListener onMultipleViewsFoundListener) {
        mOnMultipleViewsFoundListener.remove(onMultipleViewsFoundListener);
    }

    public static void notifyViewRendered(View view) {
        String nativeId = getNativeId(view);
        if (nativeId != null) {
            Iterator it = mOnViewFoundListeners.iterator();
            while (it.hasNext()) {
                OnViewFoundListener onViewFoundListener = (OnViewFoundListener) it.next();
                if (nativeId != null && nativeId.equals(onViewFoundListener.getNativeId())) {
                    onViewFoundListener.onViewFound(view);
                    it.remove();
                }
            }
            for (Entry entry : mOnMultipleViewsFoundListener.entrySet()) {
                Set set = (Set) entry.getValue();
                if (set != null && set.contains(nativeId)) {
                    ((OnMultipleViewsFoundListener) entry.getKey()).onViewFound(view, nativeId);
                }
            }
        }
    }

    @Nullable
    private static String getNativeId(View view) {
        Object tag = view.getTag(C0604R.C0606id.view_tag_native_id);
        if (tag instanceof String) {
            return (String) tag;
        }
        return null;
    }
}
