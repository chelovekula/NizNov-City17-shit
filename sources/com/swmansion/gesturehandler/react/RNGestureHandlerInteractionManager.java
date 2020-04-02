package com.swmansion.gesturehandler.react;

import android.util.SparseArray;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.gesturehandler.GestureHandler;
import com.swmansion.gesturehandler.GestureHandlerInteractionController;

public class RNGestureHandlerInteractionManager implements GestureHandlerInteractionController {
    private static final String KEY_SIMULTANEOUS_HANDLERS = "simultaneousHandlers";
    private static final String KEY_WAIT_FOR = "waitFor";
    private SparseArray<int[]> mSimultaneousRelations = new SparseArray<>();
    private SparseArray<int[]> mWaitForRelations = new SparseArray<>();

    public boolean shouldHandlerBeCancelledBy(GestureHandler gestureHandler, GestureHandler gestureHandler2) {
        return false;
    }

    public boolean shouldRequireHandlerToWaitForFailure(GestureHandler gestureHandler, GestureHandler gestureHandler2) {
        return false;
    }

    public void dropRelationsForHandlerWithTag(int i) {
        this.mWaitForRelations.remove(i);
        this.mSimultaneousRelations.remove(i);
    }

    private int[] convertHandlerTagsArray(ReadableMap readableMap, String str) {
        ReadableArray array = readableMap.getArray(str);
        int[] iArr = new int[array.size()];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = array.getInt(i);
        }
        return iArr;
    }

    public void configureInteractions(GestureHandler gestureHandler, ReadableMap readableMap) {
        gestureHandler.setInteractionController(this);
        String str = KEY_WAIT_FOR;
        if (readableMap.hasKey(str)) {
            this.mWaitForRelations.put(gestureHandler.getTag(), convertHandlerTagsArray(readableMap, str));
        }
        String str2 = KEY_SIMULTANEOUS_HANDLERS;
        if (readableMap.hasKey(str2)) {
            this.mSimultaneousRelations.put(gestureHandler.getTag(), convertHandlerTagsArray(readableMap, str2));
        }
    }

    public boolean shouldWaitForHandlerFailure(GestureHandler gestureHandler, GestureHandler gestureHandler2) {
        int[] iArr = (int[]) this.mWaitForRelations.get(gestureHandler.getTag());
        if (iArr != null) {
            for (int i : iArr) {
                if (i == gestureHandler2.getTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldRecognizeSimultaneously(GestureHandler gestureHandler, GestureHandler gestureHandler2) {
        int[] iArr = (int[]) this.mSimultaneousRelations.get(gestureHandler.getTag());
        if (iArr != null) {
            for (int i : iArr) {
                if (i == gestureHandler2.getTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        this.mWaitForRelations.clear();
        this.mSimultaneousRelations.clear();
    }
}
