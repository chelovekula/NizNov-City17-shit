package com.facebook.react.uimanager;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.react.C0604R;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.react.touch.JSResponderHandler;
import com.facebook.react.uimanager.layoutanimation.LayoutAnimationController;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;
import java.util.Arrays;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class NativeViewHierarchyManager {
    private static final String TAG = "NativeViewHierarchyManager";
    private final RectF mBoundingBox;
    private final int[] mDroppedViewArray;
    private int mDroppedViewIndex;
    private final JSResponderHandler mJSResponderHandler;
    private boolean mLayoutAnimationEnabled;
    private final LayoutAnimationController mLayoutAnimator;
    private PopupMenu mPopupMenu;
    private final SparseBooleanArray mRootTags;
    private final RootViewManager mRootViewManager;
    private final SparseArray<SparseIntArray> mTagsToPendingIndicesToDelete;
    private final SparseArray<ViewManager> mTagsToViewManagers;
    private final SparseArray<View> mTagsToViews;
    private final ViewManagerRegistry mViewManagers;

    private static class PopupMenuCallbackHandler implements OnMenuItemClickListener, OnDismissListener {
        boolean mConsumed;
        final Callback mSuccess;

        private PopupMenuCallbackHandler(Callback callback) {
            this.mConsumed = false;
            this.mSuccess = callback;
        }

        public void onDismiss(PopupMenu popupMenu) {
            if (!this.mConsumed) {
                this.mSuccess.invoke(UIManagerModuleConstants.ACTION_DISMISSED);
                this.mConsumed = true;
            }
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            if (this.mConsumed) {
                return false;
            }
            this.mSuccess.invoke(UIManagerModuleConstants.ACTION_ITEM_SELECTED, Integer.valueOf(menuItem.getOrder()));
            this.mConsumed = true;
            return true;
        }
    }

    public NativeViewHierarchyManager(ViewManagerRegistry viewManagerRegistry) {
        this(viewManagerRegistry, new RootViewManager());
    }

    public NativeViewHierarchyManager(ViewManagerRegistry viewManagerRegistry, RootViewManager rootViewManager) {
        this.mJSResponderHandler = new JSResponderHandler();
        this.mLayoutAnimator = new LayoutAnimationController();
        this.mTagsToPendingIndicesToDelete = new SparseArray<>();
        this.mDroppedViewArray = new int[100];
        this.mBoundingBox = new RectF();
        this.mDroppedViewIndex = 0;
        this.mViewManagers = viewManagerRegistry;
        this.mTagsToViews = new SparseArray<>();
        this.mTagsToViewManagers = new SparseArray<>();
        this.mRootTags = new SparseBooleanArray();
        this.mRootViewManager = rootViewManager;
    }

    public final synchronized View resolveView(int i) {
        View view;
        view = (View) this.mTagsToViews.get(i);
        if (view == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to resolve view with tag ");
            sb.append(i);
            sb.append(" which doesn't exist");
            throw new IllegalViewOperationException(sb.toString());
        }
        return view;
    }

    public final synchronized ViewManager resolveViewManager(int i) {
        ViewManager viewManager;
        viewManager = (ViewManager) this.mTagsToViewManagers.get(i);
        if (viewManager == null) {
            boolean contains = Arrays.asList(new int[][]{this.mDroppedViewArray}).contains(Integer.valueOf(i));
            StringBuilder sb = new StringBuilder();
            sb.append("ViewManager for tag ");
            sb.append(i);
            sb.append(" could not be found.\n View already dropped? ");
            sb.append(contains);
            sb.append(".\nLast index ");
            sb.append(this.mDroppedViewIndex);
            sb.append(" in last 100 views");
            sb.append(this.mDroppedViewArray.toString());
            throw new IllegalViewOperationException(sb.toString());
        }
        return viewManager;
    }

    public void setLayoutAnimationEnabled(boolean z) {
        this.mLayoutAnimationEnabled = z;
    }

    public synchronized void updateInstanceHandle(int i, long j) {
        UiThreadUtil.assertOnUiThread();
        try {
            updateInstanceHandle(resolveView(i), j);
        } catch (IllegalViewOperationException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to update properties for view tag ");
            sb.append(i);
            FLog.m51e(str, sb.toString(), (Throwable) e);
        }
        return;
    }

    public synchronized void updateProperties(int i, ReactStylesDiffMap reactStylesDiffMap) {
        UiThreadUtil.assertOnUiThread();
        try {
            ViewManager resolveViewManager = resolveViewManager(i);
            View resolveView = resolveView(i);
            if (reactStylesDiffMap != null) {
                resolveViewManager.updateProperties(resolveView, reactStylesDiffMap);
            }
        } catch (IllegalViewOperationException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to update properties for view tag ");
            sb.append(i);
            FLog.m51e(str, sb.toString(), (Throwable) e);
        }
        return;
    }

    public synchronized void updateViewExtraData(int i, Object obj) {
        UiThreadUtil.assertOnUiThread();
        resolveViewManager(i).updateExtraData(resolveView(i), obj);
    }

    public synchronized void updateLayout(int i, int i2, int i3, int i4, int i5, int i6) {
        UiThreadUtil.assertOnUiThread();
        SystraceMessage.beginSection(0, "NativeViewHierarchyManager_updateLayout").arg("parentTag", i).arg("tag", i2).flush();
        try {
            View resolveView = resolveView(i2);
            resolveView.measure(MeasureSpec.makeMeasureSpec(i5, 1073741824), MeasureSpec.makeMeasureSpec(i6, 1073741824));
            ViewParent parent = resolveView.getParent();
            if (parent instanceof RootView) {
                parent.requestLayout();
            }
            if (!this.mRootTags.get(i)) {
                ViewManager viewManager = (ViewManager) this.mTagsToViewManagers.get(i);
                if (viewManager instanceof IViewManagerWithChildren) {
                    IViewManagerWithChildren iViewManagerWithChildren = (IViewManagerWithChildren) viewManager;
                    if (iViewManagerWithChildren != null && !iViewManagerWithChildren.needsCustomLayoutForChildren()) {
                        updateLayout(resolveView, i3, i4, i5, i6);
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Trying to use view with tag ");
                    sb.append(i);
                    sb.append(" as a parent, but its Manager doesn't implement IViewManagerWithChildren");
                    throw new IllegalViewOperationException(sb.toString());
                }
            } else {
                updateLayout(resolveView, i3, i4, i5, i6);
            }
        } finally {
            Systrace.endSection(0);
        }
    }

    private void updateInstanceHandle(View view, long j) {
        UiThreadUtil.assertOnUiThread();
        view.setTag(C0604R.C0606id.view_tag_instance_handle, Long.valueOf(j));
    }

    @Nullable
    public long getInstanceHandle(int i) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            Long l = (Long) view.getTag(C0604R.C0606id.view_tag_instance_handle);
            if (l != null) {
                return l.longValue();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find instanceHandle for tag: ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unable to find view for tag: ");
        sb2.append(i);
        throw new IllegalViewOperationException(sb2.toString());
    }

    private void updateLayout(View view, int i, int i2, int i3, int i4) {
        if (!this.mLayoutAnimationEnabled || !this.mLayoutAnimator.shouldAnimateLayout(view)) {
            view.layout(i, i2, i3 + i, i4 + i2);
        } else {
            this.mLayoutAnimator.applyLayoutUpdate(view, i, i2, i3, i4);
        }
    }

    public synchronized void createView(ThemedReactContext themedReactContext, int i, String str, @Nullable ReactStylesDiffMap reactStylesDiffMap) {
        UiThreadUtil.assertOnUiThread();
        SystraceMessage.beginSection(0, "NativeViewHierarchyManager_createView").arg("tag", i).arg("className", (Object) str).flush();
        try {
            ViewManager viewManager = this.mViewManagers.get(str);
            View createView = viewManager.createView(themedReactContext, null, null, this.mJSResponderHandler);
            this.mTagsToViews.put(i, createView);
            this.mTagsToViewManagers.put(i, viewManager);
            createView.setId(i);
            if (reactStylesDiffMap != null) {
                viewManager.updateProperties(createView, reactStylesDiffMap);
            }
        } finally {
            Systrace.endSection(0);
        }
    }

    private static String constructManageChildrenErrorMessage(ViewGroup viewGroup, ViewGroupManager viewGroupManager, @Nullable int[] iArr, @Nullable ViewAtIndex[] viewAtIndexArr, @Nullable int[] iArr2) {
        StringBuilder sb = new StringBuilder();
        String str = " ],\n";
        String str2 = ",";
        String str3 = "): [\n";
        String str4 = ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE;
        if (viewGroup != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("View tag:");
            sb2.append(viewGroup.getId());
            sb2.append(str4);
            sb.append(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  children(");
            sb3.append(viewGroupManager.getChildCount(viewGroup));
            sb3.append(str3);
            sb.append(sb3.toString());
            for (int i = 0; i < viewGroupManager.getChildCount(viewGroup); i += 16) {
                int i2 = 0;
                while (true) {
                    int i3 = i + i2;
                    if (i3 >= viewGroupManager.getChildCount(viewGroup) || i2 >= 16) {
                        sb.append(str4);
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(viewGroupManager.getChildAt(viewGroup, i3).getId());
                        sb4.append(str2);
                        sb.append(sb4.toString());
                        i2++;
                    }
                }
                sb.append(str4);
            }
            sb.append(str);
        }
        if (iArr != null) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  indicesToRemove(");
            sb5.append(iArr.length);
            sb5.append(str3);
            sb.append(sb5.toString());
            for (int i4 = 0; i4 < iArr.length; i4 += 16) {
                int i5 = 0;
                while (true) {
                    int i6 = i4 + i5;
                    if (i6 >= iArr.length || i5 >= 16) {
                        sb.append(str4);
                    } else {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(iArr[i6]);
                        sb6.append(str2);
                        sb.append(sb6.toString());
                        i5++;
                    }
                }
                sb.append(str4);
            }
            sb.append(str);
        }
        if (viewAtIndexArr != null) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("  viewsToAdd(");
            sb7.append(viewAtIndexArr.length);
            sb7.append(str3);
            sb.append(sb7.toString());
            for (int i7 = 0; i7 < viewAtIndexArr.length; i7 += 16) {
                int i8 = 0;
                while (true) {
                    int i9 = i7 + i8;
                    if (i9 >= viewAtIndexArr.length || i8 >= 16) {
                        sb.append(str4);
                    } else {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("[");
                        sb8.append(viewAtIndexArr[i9].mIndex);
                        sb8.append(str2);
                        sb8.append(viewAtIndexArr[i9].mTag);
                        sb8.append("],");
                        sb.append(sb8.toString());
                        i8++;
                    }
                }
                sb.append(str4);
            }
            sb.append(str);
        }
        if (iArr2 != null) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("  tagsToDelete(");
            sb9.append(iArr2.length);
            sb9.append(str3);
            sb.append(sb9.toString());
            for (int i10 = 0; i10 < iArr2.length; i10 += 16) {
                int i11 = 0;
                while (true) {
                    int i12 = i10 + i11;
                    if (i12 >= iArr2.length || i11 >= 16) {
                        sb.append(str4);
                    } else {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(iArr2[i12]);
                        sb10.append(str2);
                        sb.append(sb10.toString());
                        i11++;
                    }
                }
                sb.append(str4);
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    private int normalizeIndex(int i, SparseIntArray sparseIntArray) {
        int i2 = i;
        for (int i3 = 0; i3 <= i; i3++) {
            i2 += sparseIntArray.get(i3);
        }
        return i2;
    }

    private SparseIntArray getOrCreatePendingIndicesToDelete(int i) {
        SparseIntArray sparseIntArray = (SparseIntArray) this.mTagsToPendingIndicesToDelete.get(i);
        if (sparseIntArray != null) {
            return sparseIntArray;
        }
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        this.mTagsToPendingIndicesToDelete.put(i, sparseIntArray2);
        return sparseIntArray2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:63:0x01ad, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void manageChildren(int r17, @androidx.annotation.Nullable int[] r18, @androidx.annotation.Nullable com.facebook.react.uimanager.ViewAtIndex[] r19, @androidx.annotation.Nullable int[] r20, @androidx.annotation.Nullable int[] r21) {
        /*
            r16 = this;
            r8 = r16
            r0 = r17
            r9 = r18
            r10 = r19
            r11 = r20
            monitor-enter(r16)
            com.facebook.react.bridge.UiThreadUtil.assertOnUiThread()     // Catch:{ all -> 0x01d3 }
            android.util.SparseIntArray r12 = r16.getOrCreatePendingIndicesToDelete(r17)     // Catch:{ all -> 0x01d3 }
            android.util.SparseArray<android.view.View> r1 = r8.mTagsToViews     // Catch:{ all -> 0x01d3 }
            java.lang.Object r1 = r1.get(r0)     // Catch:{ all -> 0x01d3 }
            r13 = r1
            android.view.ViewGroup r13 = (android.view.ViewGroup) r13     // Catch:{ all -> 0x01d3 }
            com.facebook.react.uimanager.ViewManager r1 = r16.resolveViewManager(r17)     // Catch:{ all -> 0x01d3 }
            r14 = r1
            com.facebook.react.uimanager.ViewGroupManager r14 = (com.facebook.react.uimanager.ViewGroupManager) r14     // Catch:{ all -> 0x01d3 }
            if (r13 == 0) goto L_0x01ae
            int r1 = r14.getChildCount(r13)     // Catch:{ all -> 0x01d3 }
            if (r9 == 0) goto L_0x00f2
            int r2 = r9.length     // Catch:{ all -> 0x01d3 }
            int r2 = r2 + -1
        L_0x002d:
            if (r2 < 0) goto L_0x00f2
            r3 = r9[r2]     // Catch:{ all -> 0x01d3 }
            if (r3 < 0) goto L_0x00c7
            int r4 = r14.getChildCount(r13)     // Catch:{ all -> 0x01d3 }
            if (r3 < r4) goto L_0x0074
            android.util.SparseBooleanArray r1 = r8.mRootTags     // Catch:{ all -> 0x01d3 }
            boolean r1 = r1.get(r0)     // Catch:{ all -> 0x01d3 }
            if (r1 == 0) goto L_0x0049
            int r1 = r14.getChildCount(r13)     // Catch:{ all -> 0x01d3 }
            if (r1 != 0) goto L_0x0049
            monitor-exit(r16)
            return
        L_0x0049:
            com.facebook.react.uimanager.IllegalViewOperationException r1 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r2.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r4 = "Trying to remove a view index above child count "
            r2.append(r4)     // Catch:{ all -> 0x01d3 }
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            java.lang.String r3 = " view tag: "
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = "\n detail: "
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = constructManageChildrenErrorMessage(r13, r14, r9, r10, r11)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x01d3 }
            r1.<init>(r0)     // Catch:{ all -> 0x01d3 }
            throw r1     // Catch:{ all -> 0x01d3 }
        L_0x0074:
            if (r3 >= r1) goto L_0x009c
            int r1 = r8.normalizeIndex(r3, r12)     // Catch:{ all -> 0x01d3 }
            android.view.View r4 = r14.getChildAt(r13, r1)     // Catch:{ all -> 0x01d3 }
            boolean r5 = r8.mLayoutAnimationEnabled     // Catch:{ all -> 0x01d3 }
            if (r5 == 0) goto L_0x0095
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r5 = r8.mLayoutAnimator     // Catch:{ all -> 0x01d3 }
            boolean r5 = r5.shouldAnimateLayout(r4)     // Catch:{ all -> 0x01d3 }
            if (r5 == 0) goto L_0x0095
            int r4 = r4.getId()     // Catch:{ all -> 0x01d3 }
            boolean r4 = r8.arrayContains(r11, r4)     // Catch:{ all -> 0x01d3 }
            if (r4 == 0) goto L_0x0095
            goto L_0x0098
        L_0x0095:
            r14.removeViewAt(r13, r1)     // Catch:{ all -> 0x01d3 }
        L_0x0098:
            int r2 = r2 + -1
            r1 = r3
            goto L_0x002d
        L_0x009c:
            com.facebook.react.uimanager.IllegalViewOperationException r1 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r2.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r4 = "Trying to remove an out of order view index:"
            r2.append(r4)     // Catch:{ all -> 0x01d3 }
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            java.lang.String r3 = " view tag: "
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = "\n detail: "
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = constructManageChildrenErrorMessage(r13, r14, r9, r10, r11)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x01d3 }
            r1.<init>(r0)     // Catch:{ all -> 0x01d3 }
            throw r1     // Catch:{ all -> 0x01d3 }
        L_0x00c7:
            com.facebook.react.uimanager.IllegalViewOperationException r1 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r2.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r4 = "Trying to remove a negative view index:"
            r2.append(r4)     // Catch:{ all -> 0x01d3 }
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            java.lang.String r3 = " view tag: "
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = "\n detail: "
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = constructManageChildrenErrorMessage(r13, r14, r9, r10, r11)     // Catch:{ all -> 0x01d3 }
            r2.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x01d3 }
            r1.<init>(r0)     // Catch:{ all -> 0x01d3 }
            throw r1     // Catch:{ all -> 0x01d3 }
        L_0x00f2:
            r0 = 0
            if (r11 == 0) goto L_0x0165
            r15 = 0
        L_0x00f6:
            int r1 = r11.length     // Catch:{ all -> 0x01d3 }
            if (r15 >= r1) goto L_0x0165
            r1 = r11[r15]     // Catch:{ all -> 0x01d3 }
            r7 = r21[r15]     // Catch:{ all -> 0x01d3 }
            android.util.SparseArray<android.view.View> r2 = r8.mTagsToViews     // Catch:{ all -> 0x01d3 }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ all -> 0x01d3 }
            r6 = r2
            android.view.View r6 = (android.view.View) r6     // Catch:{ all -> 0x01d3 }
            if (r6 == 0) goto L_0x013e
            boolean r1 = r8.mLayoutAnimationEnabled     // Catch:{ all -> 0x01d3 }
            if (r1 == 0) goto L_0x0132
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r1 = r8.mLayoutAnimator     // Catch:{ all -> 0x01d3 }
            boolean r1 = r1.shouldAnimateLayout(r6)     // Catch:{ all -> 0x01d3 }
            if (r1 == 0) goto L_0x0132
            int r1 = r12.get(r7, r0)     // Catch:{ all -> 0x01d3 }
            int r1 = r1 + 1
            r12.put(r7, r1)     // Catch:{ all -> 0x01d3 }
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r5 = r8.mLayoutAnimator     // Catch:{ all -> 0x01d3 }
            com.facebook.react.uimanager.NativeViewHierarchyManager$1 r4 = new com.facebook.react.uimanager.NativeViewHierarchyManager$1     // Catch:{ all -> 0x01d3 }
            r1 = r4
            r2 = r16
            r3 = r14
            r0 = r4
            r4 = r13
            r9 = r5
            r5 = r6
            r10 = r6
            r6 = r12
            r1.<init>(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x01d3 }
            r9.deleteView(r10, r0)     // Catch:{ all -> 0x01d3 }
            goto L_0x0136
        L_0x0132:
            r10 = r6
            r8.dropView(r10)     // Catch:{ all -> 0x01d3 }
        L_0x0136:
            int r15 = r15 + 1
            r9 = r18
            r10 = r19
            r0 = 0
            goto L_0x00f6
        L_0x013e:
            com.facebook.react.uimanager.IllegalViewOperationException r0 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r2.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r3 = "Trying to destroy unknown view tag: "
            r2.append(r3)     // Catch:{ all -> 0x01d3 }
            r2.append(r1)     // Catch:{ all -> 0x01d3 }
            java.lang.String r1 = "\n detail: "
            r2.append(r1)     // Catch:{ all -> 0x01d3 }
            r1 = r18
            r3 = r19
            java.lang.String r1 = constructManageChildrenErrorMessage(r13, r14, r1, r3, r11)     // Catch:{ all -> 0x01d3 }
            r2.append(r1)     // Catch:{ all -> 0x01d3 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x01d3 }
            r0.<init>(r1)     // Catch:{ all -> 0x01d3 }
            throw r0     // Catch:{ all -> 0x01d3 }
        L_0x0165:
            r1 = r9
            r3 = r10
            if (r3 == 0) goto L_0x01ac
            r0 = 0
        L_0x016a:
            int r2 = r3.length     // Catch:{ all -> 0x01d3 }
            if (r0 >= r2) goto L_0x01ac
            r2 = r3[r0]     // Catch:{ all -> 0x01d3 }
            android.util.SparseArray<android.view.View> r4 = r8.mTagsToViews     // Catch:{ all -> 0x01d3 }
            int r5 = r2.mTag     // Catch:{ all -> 0x01d3 }
            java.lang.Object r4 = r4.get(r5)     // Catch:{ all -> 0x01d3 }
            android.view.View r4 = (android.view.View) r4     // Catch:{ all -> 0x01d3 }
            if (r4 == 0) goto L_0x0187
            int r2 = r2.mIndex     // Catch:{ all -> 0x01d3 }
            int r2 = r8.normalizeIndex(r2, r12)     // Catch:{ all -> 0x01d3 }
            r14.addView(r13, r4, r2)     // Catch:{ all -> 0x01d3 }
            int r0 = r0 + 1
            goto L_0x016a
        L_0x0187:
            com.facebook.react.uimanager.IllegalViewOperationException r0 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r4.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r5 = "Trying to add unknown view tag: "
            r4.append(r5)     // Catch:{ all -> 0x01d3 }
            int r2 = r2.mTag     // Catch:{ all -> 0x01d3 }
            r4.append(r2)     // Catch:{ all -> 0x01d3 }
            java.lang.String r2 = "\n detail: "
            r4.append(r2)     // Catch:{ all -> 0x01d3 }
            java.lang.String r1 = constructManageChildrenErrorMessage(r13, r14, r1, r3, r11)     // Catch:{ all -> 0x01d3 }
            r4.append(r1)     // Catch:{ all -> 0x01d3 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x01d3 }
            r0.<init>(r1)     // Catch:{ all -> 0x01d3 }
            throw r0     // Catch:{ all -> 0x01d3 }
        L_0x01ac:
            monitor-exit(r16)
            return
        L_0x01ae:
            r1 = r9
            r3 = r10
            com.facebook.react.uimanager.IllegalViewOperationException r2 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x01d3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d3 }
            r4.<init>()     // Catch:{ all -> 0x01d3 }
            java.lang.String r5 = "Trying to manageChildren view with tag "
            r4.append(r5)     // Catch:{ all -> 0x01d3 }
            r4.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = " which doesn't exist\n detail: "
            r4.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = constructManageChildrenErrorMessage(r13, r14, r1, r3, r11)     // Catch:{ all -> 0x01d3 }
            r4.append(r0)     // Catch:{ all -> 0x01d3 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x01d3 }
            r2.<init>(r0)     // Catch:{ all -> 0x01d3 }
            throw r2     // Catch:{ all -> 0x01d3 }
        L_0x01d3:
            r0 = move-exception
            monitor-exit(r16)
            goto L_0x01d7
        L_0x01d6:
            throw r0
        L_0x01d7:
            goto L_0x01d6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.NativeViewHierarchyManager.manageChildren(int, int[], com.facebook.react.uimanager.ViewAtIndex[], int[], int[]):void");
    }

    private boolean arrayContains(@Nullable int[] iArr, int i) {
        if (iArr == null) {
            return false;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private static String constructSetChildrenErrorMessage(ViewGroup viewGroup, ViewGroupManager viewGroupManager, ReadableArray readableArray) {
        ViewAtIndex[] viewAtIndexArr = new ViewAtIndex[readableArray.size()];
        for (int i = 0; i < readableArray.size(); i++) {
            viewAtIndexArr[i] = new ViewAtIndex(readableArray.getInt(i), i);
        }
        return constructManageChildrenErrorMessage(viewGroup, viewGroupManager, null, viewAtIndexArr, null);
    }

    public synchronized void setChildren(int i, ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        ViewGroup viewGroup = (ViewGroup) this.mTagsToViews.get(i);
        ViewGroupManager viewGroupManager = (ViewGroupManager) resolveViewManager(i);
        int i2 = 0;
        while (i2 < readableArray.size()) {
            View view = (View) this.mTagsToViews.get(readableArray.getInt(i2));
            if (view != null) {
                viewGroupManager.addView(viewGroup, view, i2);
                i2++;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Trying to add unknown view tag: ");
                sb.append(readableArray.getInt(i2));
                sb.append("\n detail: ");
                sb.append(constructSetChildrenErrorMessage(viewGroup, viewGroupManager, readableArray));
                throw new IllegalViewOperationException(sb.toString());
            }
        }
    }

    public synchronized void addRootView(int i, View view) {
        addRootViewGroup(i, view);
    }

    /* access modifiers changed from: protected */
    public final synchronized void addRootViewGroup(int i, View view) {
        if (view.getId() != -1) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to add a root view with an explicit id (");
            sb.append(view.getId());
            sb.append(") already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
            FLog.m50e(str, sb.toString());
        }
        this.mTagsToViews.put(i, view);
        this.mTagsToViewManagers.put(i, this.mRootViewManager);
        this.mRootTags.put(i, true);
        view.setId(i);
    }

    private void cacheDroppedTag(int i) {
        int[] iArr = this.mDroppedViewArray;
        int i2 = this.mDroppedViewIndex;
        iArr[i2] = i;
        this.mDroppedViewIndex = (i2 + 1) % 100;
    }

    /* access modifiers changed from: protected */
    public synchronized void dropView(View view) {
        UiThreadUtil.assertOnUiThread();
        if (view != null) {
            if (ReactFeatureFlags.logDroppedViews) {
                cacheDroppedTag(view.getId());
            }
            if (this.mTagsToViewManagers.get(view.getId()) != null) {
                if (!this.mRootTags.get(view.getId())) {
                    resolveViewManager(view.getId()).onDropViewInstance(view);
                }
                ViewManager viewManager = (ViewManager) this.mTagsToViewManagers.get(view.getId());
                if ((view instanceof ViewGroup) && (viewManager instanceof ViewGroupManager)) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    ViewGroupManager viewGroupManager = (ViewGroupManager) viewManager;
                    for (int childCount = viewGroupManager.getChildCount(viewGroup) - 1; childCount >= 0; childCount--) {
                        View childAt = viewGroupManager.getChildAt(viewGroup, childCount);
                        if (childAt == null) {
                            FLog.m50e(TAG, "Unable to drop null child view");
                        } else if (this.mTagsToViews.get(childAt.getId()) != null) {
                            dropView(childAt);
                        }
                    }
                    viewGroupManager.removeAllViews(viewGroup);
                }
                this.mTagsToPendingIndicesToDelete.remove(view.getId());
                this.mTagsToViews.remove(view.getId());
                this.mTagsToViewManagers.remove(view.getId());
            }
        }
    }

    public synchronized void removeRootView(int i) {
        UiThreadUtil.assertOnUiThread();
        if (!this.mRootTags.get(i)) {
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" is not registered as a root view");
            SoftAssertions.assertUnreachable(sb.toString());
        }
        dropView((View) this.mTagsToViews.get(i));
        this.mRootTags.delete(i);
    }

    public synchronized void measure(int i, int[] iArr) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            View view2 = (View) RootViewUtil.getRootView(view);
            if (view2 != null) {
                computeBoundingBox(view2, iArr);
                int i2 = iArr[0];
                int i3 = iArr[1];
                computeBoundingBox(view, iArr);
                iArr[0] = iArr[0] - i2;
                iArr[1] = iArr[1] - i3;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Native view ");
                sb.append(i);
                sb.append(" is no longer on screen");
                throw new NoSuchNativeViewException(sb.toString());
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("No native view for ");
            sb2.append(i);
            sb2.append(" currently exists");
            throw new NoSuchNativeViewException(sb2.toString());
        }
    }

    private void computeBoundingBox(View view, int[] iArr) {
        this.mBoundingBox.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        mapRectFromViewToWindowCoords(view, this.mBoundingBox);
        iArr[0] = Math.round(this.mBoundingBox.left);
        iArr[1] = Math.round(this.mBoundingBox.top);
        iArr[2] = Math.round(this.mBoundingBox.right - this.mBoundingBox.left);
        iArr[3] = Math.round(this.mBoundingBox.bottom - this.mBoundingBox.top);
    }

    private void mapRectFromViewToWindowCoords(View view, RectF rectF) {
        Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            matrix.mapRect(rectF);
        }
        rectF.offset((float) view.getLeft(), (float) view.getTop());
        ViewParent parent = view.getParent();
        while (parent instanceof View) {
            View view2 = (View) parent;
            rectF.offset((float) (-view2.getScrollX()), (float) (-view2.getScrollY()));
            Matrix matrix2 = view2.getMatrix();
            if (!matrix2.isIdentity()) {
                matrix2.mapRect(rectF);
            }
            rectF.offset((float) view2.getLeft(), (float) view2.getTop());
            parent = view2.getParent();
        }
    }

    public synchronized void measureInWindow(int i, int[] iArr) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            view.getLocationOnScreen(iArr);
            Resources resources = view.getContext().getResources();
            int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                iArr[1] = iArr[1] - ((int) resources.getDimension(identifier));
            }
            iArr[2] = view.getWidth();
            iArr[3] = view.getHeight();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("No native view for ");
            sb.append(i);
            sb.append(" currently exists");
            throw new NoSuchNativeViewException(sb.toString());
        }
    }

    public synchronized int findTargetTagForTouch(int i, float f, float f2) {
        View view;
        UiThreadUtil.assertOnUiThread();
        view = (View) this.mTagsToViews.get(i);
        if (view != null) {
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not find view with tag ");
            sb.append(i);
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        return TouchTargetHelper.findTargetTagForTouch(f, f2, (ViewGroup) view);
    }

    public synchronized void setJSResponder(int i, int i2, boolean z) {
        if (!z) {
            this.mJSResponderHandler.setJSResponder(i2, null);
            return;
        }
        View view = (View) this.mTagsToViews.get(i);
        if (i2 == i || !(view instanceof ViewParent)) {
            if (this.mRootTags.get(i)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot block native responder on ");
                sb.append(i);
                sb.append(" that is a root view");
                SoftAssertions.assertUnreachable(sb.toString());
            }
            this.mJSResponderHandler.setJSResponder(i2, view.getParent());
            return;
        }
        this.mJSResponderHandler.setJSResponder(i2, (ViewParent) view);
    }

    public void clearJSResponder() {
        this.mJSResponderHandler.clearJSResponder();
    }

    /* access modifiers changed from: 0000 */
    public void configureLayoutAnimation(ReadableMap readableMap, Callback callback) {
        this.mLayoutAnimator.initializeFromConfig(readableMap, callback);
    }

    /* access modifiers changed from: 0000 */
    public void clearLayoutAnimation() {
        this.mLayoutAnimator.reset();
    }

    @Deprecated
    public synchronized void dispatchCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            resolveViewManager(i).receiveCommand(view, i2, readableArray);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to send command to a non-existing view with tag ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    public synchronized void dispatchCommand(int i, String str, @Nullable ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            resolveViewManager(i).receiveCommand(view, str, readableArray);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to send command to a non-existing view with tag ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    public synchronized void showPopupMenu(int i, ReadableArray readableArray, Callback callback, Callback callback2) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't display popup. Could not find view with tag ");
            sb.append(i);
            callback2.invoke(sb.toString());
            return;
        }
        this.mPopupMenu = new PopupMenu(getReactContextForView(i), view);
        Menu menu = this.mPopupMenu.getMenu();
        for (int i2 = 0; i2 < readableArray.size(); i2++) {
            menu.add(0, 0, i2, readableArray.getString(i2));
        }
        PopupMenuCallbackHandler popupMenuCallbackHandler = new PopupMenuCallbackHandler(callback);
        this.mPopupMenu.setOnMenuItemClickListener(popupMenuCallbackHandler);
        this.mPopupMenu.setOnDismissListener(popupMenuCallbackHandler);
        this.mPopupMenu.show();
    }

    public void dismissPopupMenu() {
        PopupMenu popupMenu = this.mPopupMenu;
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
    }

    private ThemedReactContext getReactContextForView(int i) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            return (ThemedReactContext) view.getContext();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find view with tag ");
        sb.append(i);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    public void sendAccessibilityEvent(int i, int i2) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            view.sendAccessibilityEvent(i2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find view with tag ");
        sb.append(i);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }
}
