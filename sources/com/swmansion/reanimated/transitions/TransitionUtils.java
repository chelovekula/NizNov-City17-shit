package com.swmansion.reanimated.transitions;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionSet;
import androidx.transition.Visibility;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ViewProps;
import javax.annotation.Nullable;

class TransitionUtils {
    TransitionUtils() {
    }

    @Nullable
    static Transition inflate(ReadableMap readableMap) {
        String string = readableMap.getString(ReactVideoViewManager.PROP_SRC_TYPE);
        if ("group".equals(string)) {
            return inflateGroup(readableMap);
        }
        if ("in".equals(string)) {
            return inflateIn(readableMap);
        }
        if ("out".equals(string)) {
            return inflateOut(readableMap);
        }
        if ("change".equals(string)) {
            return inflateChange(readableMap);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized transition type ");
        sb.append(string);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    @Nullable
    private static Transition inflateGroup(ReadableMap readableMap) {
        TransitionSet transitionSet = new TransitionSet();
        String str = "sequence";
        if (!readableMap.hasKey(str) || !readableMap.getBoolean(str)) {
            transitionSet.setOrdering(0);
        } else {
            transitionSet.setOrdering(1);
        }
        ReadableArray array = readableMap.getArray("transitions");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            Transition inflate = inflate(array.getMap(i));
            if (inflate != null) {
                transitionSet.addTransition(inflate);
            }
        }
        return transitionSet;
    }

    static Visibility createVisibilityTransition(String str) {
        if (str == null || ViewProps.NONE.equals(str)) {
            return null;
        }
        if ("fade".equals(str)) {
            return new Fade(3);
        }
        if ("scale".equals(str)) {
            return new Scale();
        }
        if ("slide-top".equals(str)) {
            return new Slide(48);
        }
        if ("slide-bottom".equals(str)) {
            return new Slide(80);
        }
        if ("slide-right".equals(str)) {
            return new Slide(5);
        }
        if ("slide-left".equals(str)) {
            return new Slide(3);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid transition type ");
        sb.append(str);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    private static Transition inflateIn(ReadableMap readableMap) {
        Visibility createTransition = createTransition(readableMap.getString("animation"));
        if (createTransition == null) {
            return null;
        }
        createTransition.setMode(1);
        configureTransition(createTransition, readableMap);
        return createTransition;
    }

    private static Transition inflateOut(ReadableMap readableMap) {
        Visibility createTransition = createTransition(readableMap.getString("animation"));
        if (createTransition == null) {
            return null;
        }
        createTransition.setMode(2);
        configureTransition(createTransition, readableMap);
        return createTransition;
    }

    private static Transition inflateChange(ReadableMap readableMap) {
        ChangeBounds changeBounds = new ChangeBounds();
        ChangeTransform changeTransform = new ChangeTransform();
        configureTransition(changeBounds, readableMap);
        configureTransition(changeTransform, readableMap);
        return new TransitionSet().addTransition(changeBounds).addTransition(changeTransform);
    }

    private static Visibility createTransition(String str) {
        if (str == null || ViewProps.NONE.equals(str)) {
            return null;
        }
        if ("fade".equals(str)) {
            return new Fade(3);
        }
        if ("scale".equals(str)) {
            return new Scale();
        }
        if ("slide-top".equals(str)) {
            return new Slide(48);
        }
        if ("slide-bottom".equals(str)) {
            return new Slide(80);
        }
        if ("slide-right".equals(str)) {
            return new Slide(5);
        }
        if ("slide-left".equals(str)) {
            return new Slide(3);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid transition type ");
        sb.append(str);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    private static void configureTransition(Transition transition, ReadableMap readableMap) {
        String str = "durationMs";
        if (readableMap.hasKey(str)) {
            transition.setDuration((long) readableMap.getInt(str));
        }
        String str2 = "interpolation";
        if (readableMap.hasKey(str2)) {
            String string = readableMap.getString(str2);
            if (string.equals("easeIn")) {
                transition.setInterpolator(new AccelerateInterpolator());
            } else if (string.equals("easeOut")) {
                transition.setInterpolator(new DecelerateInterpolator());
            } else if (string.equals("easeInOut")) {
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
            } else if (string.equals("linear")) {
                transition.setInterpolator(new LinearInterpolator());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid interpolation type ");
                sb.append(string);
                throw new JSApplicationIllegalArgumentException(sb.toString());
            }
        }
        String str3 = "propagation";
        if (readableMap.hasKey(str3)) {
            String string2 = readableMap.getString(str3);
            SaneSidePropagation saneSidePropagation = new SaneSidePropagation();
            if (ViewProps.TOP.equals(string2)) {
                saneSidePropagation.setSide(80);
            } else if (ViewProps.BOTTOM.equals(string2)) {
                saneSidePropagation.setSide(48);
            } else if (ViewProps.LEFT.equals(string2)) {
                saneSidePropagation.setSide(5);
            } else if (ViewProps.RIGHT.equals(string2)) {
                saneSidePropagation.setSide(3);
            }
            transition.setPropagation(saneSidePropagation);
        } else {
            transition.setPropagation(null);
        }
        String str4 = "delayMs";
        if (readableMap.hasKey(str4)) {
            transition.setStartDelay((long) readableMap.getInt(str4));
        }
    }
}
