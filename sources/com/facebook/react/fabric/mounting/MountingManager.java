package com.facebook.react.fabric.mounting;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.AnyThread;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.fabric.events.EventEmitterWrapper;
import com.facebook.react.touch.JSResponderHandler;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.uimanager.RootViewManager;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.uimanager.ViewManagerRegistry;
import com.facebook.yoga.YogaMeasureMode;
import java.util.concurrent.ConcurrentHashMap;

public class MountingManager {
    private final JSResponderHandler mJSResponderHandler = new JSResponderHandler();
    private final RootViewManager mRootViewManager = new RootViewManager();
    private final ConcurrentHashMap<Integer, ViewState> mTagToViewState = new ConcurrentHashMap<>();
    private final ViewManagerRegistry mViewManagerRegistry;

    private static class ViewState {
        @Nullable
        public ReadableMap mCurrentLocalData;
        @Nullable
        public ReactStylesDiffMap mCurrentProps;
        @Nullable
        public ReadableMap mCurrentState;
        @Nullable
        public EventEmitterWrapper mEventEmitter;
        final boolean mIsRoot;
        final int mReactTag;
        @Nullable
        final View mView;
        @Nullable
        final ViewManager mViewManager;

        private ViewState(int i, @Nullable View view, @Nullable ViewManager viewManager) {
            this(i, view, viewManager, false);
        }

        private ViewState(int i, @Nullable View view, ViewManager viewManager, boolean z) {
            this.mCurrentProps = null;
            this.mCurrentLocalData = null;
            this.mCurrentState = null;
            this.mEventEmitter = null;
            this.mReactTag = i;
            this.mView = view;
            this.mIsRoot = z;
            this.mViewManager = viewManager;
        }

        public String toString() {
            boolean z = this.mViewManager == null;
            StringBuilder sb = new StringBuilder();
            sb.append("ViewState [");
            sb.append(this.mReactTag);
            sb.append("] - isRoot: ");
            sb.append(this.mIsRoot);
            sb.append(" - props: ");
            sb.append(this.mCurrentProps);
            sb.append(" - localData: ");
            sb.append(this.mCurrentLocalData);
            sb.append(" - viewManager: ");
            sb.append(this.mViewManager);
            sb.append(" - isLayoutOnly: ");
            sb.append(z);
            return sb.toString();
        }
    }

    public MountingManager(ViewManagerRegistry viewManagerRegistry) {
        this.mViewManagerRegistry = viewManagerRegistry;
    }

    public void addRootView(int i, View view) {
        if (view.getId() == -1) {
            ConcurrentHashMap<Integer, ViewState> concurrentHashMap = this.mTagToViewState;
            Integer valueOf = Integer.valueOf(i);
            ViewState viewState = new ViewState(i, view, this.mRootViewManager, true);
            concurrentHashMap.put(valueOf, viewState);
            view.setId(i);
            return;
        }
        throw new IllegalViewOperationException("Trying to add a root view with an explicit id already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
    }

    @UiThread
    private void dropView(View view) {
        UiThreadUtil.assertOnUiThread();
        int id = view.getId();
        ViewState viewState = getViewState(id);
        ViewManager viewManager = viewState.mViewManager;
        if (!viewState.mIsRoot && viewManager != null) {
            viewManager.onDropViewInstance(view);
        }
        if ((view instanceof ViewGroup) && (viewManager instanceof ViewGroupManager)) {
            ViewGroup viewGroup = (ViewGroup) view;
            ViewGroupManager viewGroupManager = getViewGroupManager(viewState);
            for (int childCount = viewGroupManager.getChildCount(viewGroup) - 1; childCount >= 0; childCount--) {
                View childAt = viewGroupManager.getChildAt(viewGroup, childCount);
                if (this.mTagToViewState.get(Integer.valueOf(childAt.getId())) != null) {
                    dropView(childAt);
                }
                viewGroupManager.removeViewAt(viewGroup, childCount);
            }
        }
        this.mTagToViewState.remove(Integer.valueOf(id));
    }

    @UiThread
    public void addViewAt(int i, int i2, int i3) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        ViewGroup viewGroup = (ViewGroup) viewState.mView;
        ViewState viewState2 = getViewState(i2);
        View view = viewState2.mView;
        if (view != null) {
            getViewGroupManager(viewState).addView(viewGroup, view, i3);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find view for viewState ");
        sb.append(viewState2);
        sb.append(" and tag ");
        sb.append(i2);
        throw new IllegalStateException(sb.toString());
    }

    private ViewState getViewState(int i) {
        ViewState viewState = (ViewState) this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState != null) {
            return viewState;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find viewState view for tag ");
        sb.append(i);
        throw new IllegalStateException(sb.toString());
    }

    @Deprecated
    public void receiveCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        ViewState viewState = getViewState(i);
        if (viewState.mViewManager == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find viewState manager for tag ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        } else if (viewState.mView != null) {
            viewState.mViewManager.receiveCommand(viewState.mView, i2, readableArray);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find viewState view for tag ");
            sb2.append(i);
            throw new IllegalStateException(sb2.toString());
        }
    }

    public void receiveCommand(int i, String str, @Nullable ReadableArray readableArray) {
        ViewState viewState = getViewState(i);
        if (viewState.mViewManager == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find viewState manager for tag ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        } else if (viewState.mView != null) {
            viewState.mViewManager.receiveCommand(viewState.mView, str, readableArray);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find viewState view for tag ");
            sb2.append(i);
            throw new IllegalStateException(sb2.toString());
        }
    }

    private static ViewGroupManager<ViewGroup> getViewGroupManager(ViewState viewState) {
        if (viewState.mViewManager != null) {
            return (ViewGroupManager) viewState.mViewManager;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find ViewManager for view: ");
        sb.append(viewState);
        throw new IllegalStateException(sb.toString());
    }

    @UiThread
    public void removeViewAt(int i, int i2) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        ViewGroup viewGroup = (ViewGroup) viewState.mView;
        if (viewGroup != null) {
            getViewGroupManager(viewState).removeViewAt(viewGroup, i2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find view for tag ");
        sb.append(i);
        throw new IllegalStateException(sb.toString());
    }

    @UiThread
    public void createView(ThemedReactContext themedReactContext, String str, int i, @Nullable ReadableMap readableMap, @Nullable StateWrapper stateWrapper, boolean z) {
        ViewManager viewManager;
        View view;
        if (this.mTagToViewState.get(Integer.valueOf(i)) == null) {
            ReadableNativeMap readableNativeMap = null;
            ReactStylesDiffMap reactStylesDiffMap = readableMap != null ? new ReactStylesDiffMap(readableMap) : null;
            if (z) {
                viewManager = this.mViewManagerRegistry.get(str);
                view = viewManager.createView(themedReactContext, reactStylesDiffMap, stateWrapper, this.mJSResponderHandler);
                view.setId(i);
            } else {
                view = null;
                viewManager = null;
            }
            ViewState viewState = new ViewState(i, view, viewManager);
            viewState.mCurrentProps = reactStylesDiffMap;
            if (stateWrapper != null) {
                readableNativeMap = stateWrapper.getState();
            }
            viewState.mCurrentState = readableNativeMap;
            this.mTagToViewState.put(Integer.valueOf(i), viewState);
        }
    }

    @UiThread
    public void updateProps(int i, ReadableMap readableMap) {
        if (readableMap != null) {
            UiThreadUtil.assertOnUiThread();
            ViewState viewState = getViewState(i);
            viewState.mCurrentProps = new ReactStylesDiffMap(readableMap);
            View view = viewState.mView;
            if (view != null) {
                ((ViewManager) Assertions.assertNotNull(viewState.mViewManager)).updateProperties(view, viewState.mCurrentProps);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find view for tag ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }

    @UiThread
    public void updateLayout(int i, int i2, int i3, int i4, int i5) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        if (!viewState.mIsRoot) {
            View view = viewState.mView;
            if (view != null) {
                view.measure(MeasureSpec.makeMeasureSpec(i4, 1073741824), MeasureSpec.makeMeasureSpec(i5, 1073741824));
                ViewParent parent = view.getParent();
                if (parent instanceof RootView) {
                    parent.requestLayout();
                }
                view.layout(i2, i3, i4 + i2, i5 + i3);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find View for tag: ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }

    @UiThread
    public void deleteView(int i) {
        UiThreadUtil.assertOnUiThread();
        View view = getViewState(i).mView;
        if (view != null) {
            dropView(view);
        } else {
            this.mTagToViewState.remove(Integer.valueOf(i));
        }
    }

    @UiThread
    public void updateLocalData(int i, ReadableMap readableMap) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        if (viewState.mCurrentProps != null) {
            if (viewState.mCurrentLocalData != null) {
                String str = "hash";
                if (readableMap.hasKey(str) && viewState.mCurrentLocalData.getDouble(str) == readableMap.getDouble(str) && viewState.mCurrentLocalData.equals(readableMap)) {
                    return;
                }
            }
            viewState.mCurrentLocalData = readableMap;
            ViewManager viewManager = viewState.mViewManager;
            if (viewManager != null) {
                Object updateLocalData = viewManager.updateLocalData(viewState.mView, viewState.mCurrentProps, new ReactStylesDiffMap(viewState.mCurrentLocalData));
                if (updateLocalData != null) {
                    viewManager.updateExtraData(viewState.mView, updateLocalData);
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find ViewManager for view: ");
            sb.append(viewState);
            throw new IllegalStateException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Can not update local data to view without props: ");
        sb2.append(i);
        throw new IllegalStateException(sb2.toString());
    }

    @UiThread
    public void updateState(int i, StateWrapper stateWrapper) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        ReadableNativeMap state = stateWrapper.getState();
        if (viewState.mCurrentState == null || !viewState.mCurrentState.equals(state)) {
            viewState.mCurrentState = state;
            ViewManager viewManager = viewState.mViewManager;
            if (viewManager != null) {
                Object updateState = viewManager.updateState(viewState.mView, viewState.mCurrentProps, stateWrapper);
                if (updateState != null) {
                    viewManager.updateExtraData(viewState.mView, updateState);
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find ViewManager for tag: ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }

    @UiThread
    public void preallocateView(ThemedReactContext themedReactContext, String str, int i, @Nullable ReadableMap readableMap, @Nullable StateWrapper stateWrapper, boolean z) {
        if (this.mTagToViewState.get(Integer.valueOf(i)) == null) {
            createView(themedReactContext, str, i, readableMap, stateWrapper, z);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View for component ");
        sb.append(str);
        sb.append(" with tag ");
        sb.append(i);
        sb.append(" already exists.");
        throw new IllegalStateException(sb.toString());
    }

    @UiThread
    public void updateEventEmitter(int i, EventEmitterWrapper eventEmitterWrapper) {
        UiThreadUtil.assertOnUiThread();
        getViewState(i).mEventEmitter = eventEmitterWrapper;
    }

    @UiThread
    public synchronized void setJSResponder(int i, int i2, boolean z) {
        if (!z) {
            this.mJSResponderHandler.setJSResponder(i2, null);
            return;
        }
        ViewState viewState = getViewState(i);
        View view = viewState.mView;
        if (i2 != i && (view instanceof ViewParent)) {
            this.mJSResponderHandler.setJSResponder(i2, (ViewParent) view);
        } else if (view == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find view for tag ");
            sb.append(i);
            sb.append(".");
            SoftAssertions.assertUnreachable(sb.toString());
        } else {
            if (viewState.mIsRoot) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot block native responder on ");
                sb2.append(i);
                sb2.append(" that is a root view");
                SoftAssertions.assertUnreachable(sb2.toString());
            }
            this.mJSResponderHandler.setJSResponder(i2, view.getParent());
        }
    }

    @UiThread
    public void clearJSResponder() {
        this.mJSResponderHandler.clearJSResponder();
    }

    @AnyThread
    public long measure(Context context, String str, ReadableMap readableMap, ReadableMap readableMap2, ReadableMap readableMap3, float f, YogaMeasureMode yogaMeasureMode, float f2, YogaMeasureMode yogaMeasureMode2) {
        String str2 = str;
        return this.mViewManagerRegistry.get(str).measure(context, readableMap, readableMap2, readableMap3, f, yogaMeasureMode, f2, yogaMeasureMode2);
    }

    @AnyThread
    @Nullable
    public EventEmitterWrapper getEventEmitter(int i) {
        ViewState viewState = (ViewState) this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState == null) {
            return null;
        }
        return viewState.mEventEmitter;
    }
}
