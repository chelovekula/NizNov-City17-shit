package com.facebook.react.uimanager;

import android.os.SystemClock;
import android.view.View;
import android.view.View.MeasureSpec;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.i18nmanager.I18nUtil;
import com.facebook.react.uimanager.UIManagerModule.ViewManagerResolver;
import com.facebook.react.uimanager.debug.NotThreadSafeViewHierarchyUpdateDebugListener;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;
import com.facebook.yoga.YogaDirection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UIImplementation {
    protected final EventDispatcher mEventDispatcher;
    private long mLastCalculateLayoutTime;
    @Nullable
    protected LayoutUpdateListener mLayoutUpdateListener;
    private final int[] mMeasureBuffer;
    private final NativeViewHierarchyOptimizer mNativeViewHierarchyOptimizer;
    private final UIViewOperationQueue mOperationsQueue;
    protected final ReactApplicationContext mReactContext;
    protected final ShadowNodeRegistry mShadowNodeRegistry;
    private final ViewManagerRegistry mViewManagers;
    protected Object uiImplementationThreadLock;

    public interface LayoutUpdateListener {
        void onLayoutUpdated(ReactShadowNode reactShadowNode);
    }

    public void onHostDestroy() {
    }

    public UIImplementation(ReactApplicationContext reactApplicationContext, ViewManagerResolver viewManagerResolver, EventDispatcher eventDispatcher, int i) {
        this(reactApplicationContext, new ViewManagerRegistry(viewManagerResolver), eventDispatcher, i);
    }

    public UIImplementation(ReactApplicationContext reactApplicationContext, List<ViewManager> list, EventDispatcher eventDispatcher, int i) {
        this(reactApplicationContext, new ViewManagerRegistry(list), eventDispatcher, i);
    }

    UIImplementation(ReactApplicationContext reactApplicationContext, ViewManagerRegistry viewManagerRegistry, EventDispatcher eventDispatcher, int i) {
        this(reactApplicationContext, viewManagerRegistry, new UIViewOperationQueue(reactApplicationContext, new NativeViewHierarchyManager(viewManagerRegistry), i), eventDispatcher);
    }

    protected UIImplementation(ReactApplicationContext reactApplicationContext, ViewManagerRegistry viewManagerRegistry, UIViewOperationQueue uIViewOperationQueue, EventDispatcher eventDispatcher) {
        this.uiImplementationThreadLock = new Object();
        this.mShadowNodeRegistry = new ShadowNodeRegistry();
        this.mMeasureBuffer = new int[4];
        this.mLastCalculateLayoutTime = 0;
        this.mReactContext = reactApplicationContext;
        this.mViewManagers = viewManagerRegistry;
        this.mOperationsQueue = uIViewOperationQueue;
        this.mNativeViewHierarchyOptimizer = new NativeViewHierarchyOptimizer(this.mOperationsQueue, this.mShadowNodeRegistry);
        this.mEventDispatcher = eventDispatcher;
    }

    /* access modifiers changed from: protected */
    public ReactShadowNode createRootShadowNode() {
        ReactShadowNodeImpl reactShadowNodeImpl = new ReactShadowNodeImpl();
        if (I18nUtil.getInstance().isRTL(this.mReactContext)) {
            reactShadowNodeImpl.setLayoutDirection(YogaDirection.RTL);
        }
        reactShadowNodeImpl.setViewClassName("Root");
        return reactShadowNodeImpl;
    }

    /* access modifiers changed from: protected */
    public ReactShadowNode createShadowNode(String str) {
        return this.mViewManagers.get(str).createShadowNodeInstance(this.mReactContext);
    }

    public final ReactShadowNode resolveShadowNode(int i) {
        return this.mShadowNodeRegistry.getNode(i);
    }

    /* access modifiers changed from: protected */
    public final ViewManager resolveViewManager(String str) {
        return this.mViewManagers.get(str);
    }

    /* access modifiers changed from: 0000 */
    public UIViewOperationQueue getUIViewOperationQueue() {
        return this.mOperationsQueue;
    }

    public void updateRootView(int i, int i2, int i3) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to update non-existent root tag: ");
            sb.append(i);
            FLog.m90w(ReactConstants.TAG, sb.toString());
            return;
        }
        updateRootView(node, i2, i3);
    }

    public void updateRootView(ReactShadowNode reactShadowNode, int i, int i2) {
        reactShadowNode.setMeasureSpecs(i, i2);
    }

    public <T extends View> void registerRootView(T t, int i, ThemedReactContext themedReactContext) {
        synchronized (this.uiImplementationThreadLock) {
            final ReactShadowNode createRootShadowNode = createRootShadowNode();
            createRootShadowNode.setReactTag(i);
            createRootShadowNode.setThemedContext(themedReactContext);
            themedReactContext.runOnNativeModulesQueueThread(new Runnable() {
                public void run() {
                    UIImplementation.this.mShadowNodeRegistry.addRootNode(createRootShadowNode);
                }
            });
            this.mOperationsQueue.addRootView(i, t);
        }
    }

    public void removeRootView(int i) {
        removeRootShadowNode(i);
        this.mOperationsQueue.enqueueRemoveRootView(i);
    }

    public void removeRootShadowNode(int i) {
        synchronized (this.uiImplementationThreadLock) {
            this.mShadowNodeRegistry.removeRootNode(i);
        }
    }

    public void updateNodeSize(int i, int i2, int i3) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to update size of non-existent tag: ");
            sb.append(i);
            FLog.m90w(ReactConstants.TAG, sb.toString());
            return;
        }
        node.setStyleWidth((float) i2);
        node.setStyleHeight((float) i3);
        dispatchViewUpdatesIfNeeded();
    }

    public void setViewLocalData(int i, Object obj) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Attempt to set local data for view with unknown tag: ");
            sb.append(i);
            FLog.m90w(ReactConstants.TAG, sb.toString());
            return;
        }
        node.setLocalData(obj);
        dispatchViewUpdatesIfNeeded();
    }

    public void profileNextBatch() {
        this.mOperationsQueue.profileNextBatch();
    }

    public Map<String, Long> getProfiledBatchPerfCounters() {
        return this.mOperationsQueue.getProfiledBatchPerfCounters();
    }

    public void createView(int i, String str, int i2, ReadableMap readableMap) {
        synchronized (this.uiImplementationThreadLock) {
            ReactShadowNode createShadowNode = createShadowNode(str);
            ReactShadowNode node = this.mShadowNodeRegistry.getNode(i2);
            StringBuilder sb = new StringBuilder();
            sb.append("Root node with tag ");
            sb.append(i2);
            sb.append(" doesn't exist");
            Assertions.assertNotNull(node, sb.toString());
            createShadowNode.setReactTag(i);
            createShadowNode.setViewClassName(str);
            createShadowNode.setRootTag(node.getReactTag());
            createShadowNode.setThemedContext(node.getThemedContext());
            this.mShadowNodeRegistry.addNode(createShadowNode);
            ReactStylesDiffMap reactStylesDiffMap = null;
            if (readableMap != null) {
                reactStylesDiffMap = new ReactStylesDiffMap(readableMap);
                createShadowNode.updateProperties(reactStylesDiffMap);
            }
            handleCreateView(createShadowNode, i2, reactStylesDiffMap);
        }
    }

    /* access modifiers changed from: protected */
    public void handleCreateView(ReactShadowNode reactShadowNode, int i, @Nullable ReactStylesDiffMap reactStylesDiffMap) {
        if (!reactShadowNode.isVirtual()) {
            this.mNativeViewHierarchyOptimizer.handleCreateView(reactShadowNode, reactShadowNode.getThemedContext(), reactStylesDiffMap);
        }
    }

    public void updateView(int i, String str, ReadableMap readableMap) {
        if (this.mViewManagers.get(str) != null) {
            ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
            if (node == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Trying to update non-existent view with tag ");
                sb.append(i);
                throw new IllegalViewOperationException(sb.toString());
            } else if (readableMap != null) {
                ReactStylesDiffMap reactStylesDiffMap = new ReactStylesDiffMap(readableMap);
                node.updateProperties(reactStylesDiffMap);
                handleUpdateView(node, str, reactStylesDiffMap);
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Got unknown view type: ");
            sb2.append(str);
            throw new IllegalViewOperationException(sb2.toString());
        }
    }

    public void synchronouslyUpdateViewOnUIThread(int i, ReactStylesDiffMap reactStylesDiffMap) {
        UiThreadUtil.assertOnUiThread();
        this.mOperationsQueue.getNativeViewHierarchyManager().updateProperties(i, reactStylesDiffMap);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateView(ReactShadowNode reactShadowNode, String str, ReactStylesDiffMap reactStylesDiffMap) {
        if (!reactShadowNode.isVirtual()) {
            this.mNativeViewHierarchyOptimizer.handleUpdateView(reactShadowNode, str, reactStylesDiffMap);
        }
    }

    public void manageChildren(int i, @Nullable ReadableArray readableArray, @Nullable ReadableArray readableArray2, @Nullable ReadableArray readableArray3, @Nullable ReadableArray readableArray4, @Nullable ReadableArray readableArray5) {
        int i2;
        int i3;
        int i4;
        int i5 = i;
        ReadableArray readableArray6 = readableArray;
        ReadableArray readableArray7 = readableArray2;
        ReadableArray readableArray8 = readableArray3;
        ReadableArray readableArray9 = readableArray4;
        ReadableArray readableArray10 = readableArray5;
        synchronized (this.uiImplementationThreadLock) {
            try {
                ReactShadowNode node = this.mShadowNodeRegistry.getNode(i5);
                if (readableArray6 == null) {
                    i2 = 0;
                } else {
                    i2 = readableArray.size();
                }
                if (readableArray8 == null) {
                    i3 = 0;
                } else {
                    i3 = readableArray3.size();
                }
                if (readableArray10 == null) {
                    i4 = 0;
                } else {
                    i4 = readableArray5.size();
                }
                if (i2 != 0) {
                    if (readableArray7 == null || i2 != readableArray2.size()) {
                        throw new IllegalViewOperationException("Size of moveFrom != size of moveTo!");
                    }
                }
                if (i3 != 0) {
                    if (readableArray9 == null || i3 != readableArray4.size()) {
                        throw new IllegalViewOperationException("Size of addChildTags != size of addAtIndices!");
                    }
                }
                ViewAtIndex[] viewAtIndexArr = new ViewAtIndex[(i2 + i3)];
                int[] iArr = new int[(i2 + i4)];
                int[] iArr2 = new int[iArr.length];
                int[] iArr3 = new int[i4];
                try {
                    int[] iArr4 = new int[i4];
                    if (i2 > 0) {
                        Assertions.assertNotNull(readableArray);
                        Assertions.assertNotNull(readableArray2);
                        int i6 = 0;
                        while (i6 < i2) {
                            int[] iArr5 = iArr4;
                            int i7 = readableArray6.getInt(i6);
                            int reactTag = node.getChildAt(i7).getReactTag();
                            int[] iArr6 = iArr3;
                            ReactShadowNode reactShadowNode = node;
                            viewAtIndexArr[i6] = new ViewAtIndex(reactTag, readableArray7.getInt(i6));
                            iArr[i6] = i7;
                            iArr2[i6] = reactTag;
                            i6++;
                            readableArray6 = readableArray;
                            iArr4 = iArr5;
                            iArr3 = iArr6;
                            node = reactShadowNode;
                        }
                    }
                    int[] iArr7 = iArr4;
                    int[] iArr8 = iArr3;
                    ReactShadowNode reactShadowNode2 = node;
                    if (i3 > 0) {
                        Assertions.assertNotNull(readableArray3);
                        Assertions.assertNotNull(readableArray4);
                        for (int i8 = 0; i8 < i3; i8++) {
                            viewAtIndexArr[i2 + i8] = new ViewAtIndex(readableArray8.getInt(i8), readableArray9.getInt(i8));
                        }
                    }
                    if (i4 > 0) {
                        Assertions.assertNotNull(readableArray5);
                        int i9 = 0;
                        while (i9 < i4) {
                            int i10 = readableArray10.getInt(i9);
                            ReactShadowNode reactShadowNode3 = reactShadowNode2;
                            int reactTag2 = reactShadowNode3.getChildAt(i10).getReactTag();
                            int i11 = i2 + i9;
                            iArr[i11] = i10;
                            iArr2[i11] = reactTag2;
                            iArr8[i9] = reactTag2;
                            iArr7[i9] = i10;
                            i9++;
                            reactShadowNode2 = reactShadowNode3;
                        }
                    }
                    ReactShadowNode reactShadowNode4 = reactShadowNode2;
                    Arrays.sort(viewAtIndexArr, ViewAtIndex.COMPARATOR);
                    Arrays.sort(iArr);
                    int length = iArr.length - 1;
                    int i12 = -1;
                    while (length >= 0) {
                        if (iArr[length] != i12) {
                            reactShadowNode4.removeChildAt(iArr[length]);
                            i12 = iArr[length];
                            length--;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Repeated indices in Removal list for view tag: ");
                            sb.append(i);
                            throw new IllegalViewOperationException(sb.toString());
                        }
                    }
                    int i13 = 0;
                    while (i13 < viewAtIndexArr.length) {
                        ViewAtIndex viewAtIndex = viewAtIndexArr[i13];
                        int[] iArr9 = iArr7;
                        ReactShadowNode node2 = this.mShadowNodeRegistry.getNode(viewAtIndex.mTag);
                        if (node2 != null) {
                            reactShadowNode4.addChildAt(node2, viewAtIndex.mIndex);
                            i13++;
                            iArr7 = iArr9;
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Trying to add unknown view tag: ");
                            sb2.append(viewAtIndex.mTag);
                            throw new IllegalViewOperationException(sb2.toString());
                        }
                    }
                    this.mNativeViewHierarchyOptimizer.handleManageChildren(reactShadowNode4, iArr, iArr2, viewAtIndexArr, iArr8, iArr7);
                    int[] iArr10 = iArr8;
                    for (int node3 : iArr10) {
                        removeShadowNode(this.mShadowNodeRegistry.getNode(node3));
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public void setChildren(int i, ReadableArray readableArray) {
        synchronized (this.uiImplementationThreadLock) {
            ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
            int i2 = 0;
            while (i2 < readableArray.size()) {
                ReactShadowNode node2 = this.mShadowNodeRegistry.getNode(readableArray.getInt(i2));
                if (node2 != null) {
                    node.addChildAt(node2, i2);
                    i2++;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Trying to add unknown view tag: ");
                    sb.append(readableArray.getInt(i2));
                    throw new IllegalViewOperationException(sb.toString());
                }
            }
            this.mNativeViewHierarchyOptimizer.handleSetChildren(node, readableArray);
        }
    }

    public void replaceExistingNonRootView(int i, int i2) {
        if (this.mShadowNodeRegistry.isRootNode(i) || this.mShadowNodeRegistry.isRootNode(i2)) {
            throw new IllegalViewOperationException("Trying to add or replace a root tag!");
        }
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node != null) {
            ReactShadowNode parent = node.getParent();
            if (parent != null) {
                int indexOf = parent.indexOf(node);
                if (indexOf >= 0) {
                    WritableArray createArray = Arguments.createArray();
                    createArray.pushInt(i2);
                    WritableArray createArray2 = Arguments.createArray();
                    createArray2.pushInt(indexOf);
                    WritableArray createArray3 = Arguments.createArray();
                    createArray3.pushInt(indexOf);
                    manageChildren(parent.getReactTag(), null, null, createArray, createArray2, createArray3);
                    return;
                }
                throw new IllegalStateException("Didn't find child tag in parent");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Node is not attached to a parent: ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Trying to replace unknown view tag: ");
        sb2.append(i);
        throw new IllegalViewOperationException(sb2.toString());
    }

    public void removeSubviewsFromContainerWithID(int i) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node != null) {
            WritableArray createArray = Arguments.createArray();
            for (int i2 = 0; i2 < node.getChildCount(); i2++) {
                createArray.pushInt(i2);
            }
            manageChildren(i, null, null, null, null, createArray);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Trying to remove subviews of an unknown view tag: ");
        sb.append(i);
        throw new IllegalViewOperationException(sb.toString());
    }

    public void findSubviewIn(int i, float f, float f2, Callback callback) {
        this.mOperationsQueue.enqueueFindTargetForTouch(i, f, f2, callback);
    }

    @Deprecated
    public void viewIsDescendantOf(int i, int i2, Callback callback) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        ReactShadowNode node2 = this.mShadowNodeRegistry.getNode(i2);
        if (node == null || node2 == null) {
            callback.invoke(Boolean.valueOf(false));
            return;
        }
        callback.invoke(Boolean.valueOf(node.isDescendantOf(node2)));
    }

    public void measure(int i, Callback callback) {
        this.mOperationsQueue.enqueueMeasure(i, callback);
    }

    public void measureInWindow(int i, Callback callback) {
        this.mOperationsQueue.enqueueMeasureInWindow(i, callback);
    }

    public void measureLayout(int i, int i2, Callback callback, Callback callback2) {
        try {
            measureLayout(i, i2, this.mMeasureBuffer);
            callback2.invoke(Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[0])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[1])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[2])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[3])));
        } catch (IllegalViewOperationException e) {
            callback.invoke(e.getMessage());
        }
    }

    public void measureLayoutRelativeToParent(int i, Callback callback, Callback callback2) {
        try {
            measureLayoutRelativeToParent(i, this.mMeasureBuffer);
            callback2.invoke(Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[0])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[1])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[2])), Float.valueOf(PixelUtil.toDIPFromPixel((float) this.mMeasureBuffer[3])));
        } catch (IllegalViewOperationException e) {
            callback.invoke(e.getMessage());
        }
    }

    public void dispatchViewUpdates(int i) {
        SystraceMessage.beginSection(0, "UIImplementation.dispatchViewUpdates").arg("batchId", i).flush();
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            updateViewHierarchy();
            this.mNativeViewHierarchyOptimizer.onBatchComplete();
            this.mOperationsQueue.dispatchViewUpdates(i, uptimeMillis, this.mLastCalculateLayoutTime);
        } finally {
            Systrace.endSection(0);
        }
    }

    private void dispatchViewUpdatesIfNeeded() {
        if (this.mOperationsQueue.isEmpty()) {
            dispatchViewUpdates(-1);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0068, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006c, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0071, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateViewHierarchy() {
        /*
            r7 = this;
            java.lang.String r0 = "rootTag"
            r1 = 0
            java.lang.String r3 = "UIImplementation.updateViewHierarchy"
            com.facebook.systrace.Systrace.beginSection(r1, r3)
            r3 = 0
        L_0x000a:
            com.facebook.react.uimanager.ShadowNodeRegistry r4 = r7.mShadowNodeRegistry     // Catch:{ all -> 0x0079 }
            int r4 = r4.getRootNodeCount()     // Catch:{ all -> 0x0079 }
            if (r3 >= r4) goto L_0x0075
            com.facebook.react.uimanager.ShadowNodeRegistry r4 = r7.mShadowNodeRegistry     // Catch:{ all -> 0x0079 }
            int r4 = r4.getRootTag(r3)     // Catch:{ all -> 0x0079 }
            com.facebook.react.uimanager.ShadowNodeRegistry r5 = r7.mShadowNodeRegistry     // Catch:{ all -> 0x0079 }
            com.facebook.react.uimanager.ReactShadowNode r4 = r5.getNode(r4)     // Catch:{ all -> 0x0079 }
            java.lang.Integer r5 = r4.getWidthMeasureSpec()     // Catch:{ all -> 0x0079 }
            if (r5 == 0) goto L_0x0072
            java.lang.Integer r5 = r4.getHeightMeasureSpec()     // Catch:{ all -> 0x0079 }
            if (r5 == 0) goto L_0x0072
            java.lang.String r5 = "UIImplementation.notifyOnBeforeLayoutRecursive"
            com.facebook.systrace.SystraceMessage$Builder r5 = com.facebook.systrace.SystraceMessage.beginSection(r1, r5)     // Catch:{ all -> 0x0079 }
            int r6 = r4.getReactTag()     // Catch:{ all -> 0x0079 }
            com.facebook.systrace.SystraceMessage$Builder r5 = r5.arg(r0, r6)     // Catch:{ all -> 0x0079 }
            r5.flush()     // Catch:{ all -> 0x0079 }
            r7.notifyOnBeforeLayoutRecursive(r4)     // Catch:{ all -> 0x006d }
            com.facebook.systrace.Systrace.endSection(r1)     // Catch:{ all -> 0x0079 }
            r7.calculateRootLayout(r4)     // Catch:{ all -> 0x0079 }
            java.lang.String r5 = "UIImplementation.applyUpdatesRecursive"
            com.facebook.systrace.SystraceMessage$Builder r5 = com.facebook.systrace.SystraceMessage.beginSection(r1, r5)     // Catch:{ all -> 0x0079 }
            int r6 = r4.getReactTag()     // Catch:{ all -> 0x0079 }
            com.facebook.systrace.SystraceMessage$Builder r5 = r5.arg(r0, r6)     // Catch:{ all -> 0x0079 }
            r5.flush()     // Catch:{ all -> 0x0079 }
            r5 = 0
            r7.applyUpdatesRecursive(r4, r5, r5)     // Catch:{ all -> 0x0068 }
            com.facebook.systrace.Systrace.endSection(r1)     // Catch:{ all -> 0x0079 }
            com.facebook.react.uimanager.UIImplementation$LayoutUpdateListener r5 = r7.mLayoutUpdateListener     // Catch:{ all -> 0x0079 }
            if (r5 == 0) goto L_0x0072
            com.facebook.react.uimanager.UIViewOperationQueue r5 = r7.mOperationsQueue     // Catch:{ all -> 0x0079 }
            com.facebook.react.uimanager.UIImplementation$LayoutUpdateListener r6 = r7.mLayoutUpdateListener     // Catch:{ all -> 0x0079 }
            r5.enqueueLayoutUpdateFinished(r4, r6)     // Catch:{ all -> 0x0079 }
            goto L_0x0072
        L_0x0068:
            r0 = move-exception
            com.facebook.systrace.Systrace.endSection(r1)     // Catch:{ all -> 0x0079 }
            throw r0     // Catch:{ all -> 0x0079 }
        L_0x006d:
            r0 = move-exception
            com.facebook.systrace.Systrace.endSection(r1)     // Catch:{ all -> 0x0079 }
            throw r0     // Catch:{ all -> 0x0079 }
        L_0x0072:
            int r3 = r3 + 1
            goto L_0x000a
        L_0x0075:
            com.facebook.systrace.Systrace.endSection(r1)
            return
        L_0x0079:
            r0 = move-exception
            com.facebook.systrace.Systrace.endSection(r1)
            goto L_0x007f
        L_0x007e:
            throw r0
        L_0x007f:
            goto L_0x007e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.UIImplementation.updateViewHierarchy():void");
    }

    public void setLayoutAnimationEnabledExperimental(boolean z) {
        this.mOperationsQueue.enqueueSetLayoutAnimationEnabled(z);
    }

    public void configureNextLayoutAnimation(ReadableMap readableMap, Callback callback) {
        this.mOperationsQueue.enqueueConfigureLayoutAnimation(readableMap, callback);
    }

    public void setJSResponder(int i, boolean z) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node != null) {
            while (node.getNativeKind() == NativeKind.NONE) {
                node = node.getParent();
            }
            this.mOperationsQueue.enqueueSetJSResponder(node.getReactTag(), i, z);
        }
    }

    public void clearJSResponder() {
        this.mOperationsQueue.enqueueClearJSResponder();
    }

    @Deprecated
    public void dispatchViewManagerCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        assertViewExists(i, "dispatchViewManagerCommand");
        this.mOperationsQueue.enqueueDispatchCommand(i, i2, readableArray);
    }

    public void dispatchViewManagerCommand(int i, String str, @Nullable ReadableArray readableArray) {
        assertViewExists(i, "dispatchViewManagerCommand");
        this.mOperationsQueue.enqueueDispatchCommand(i, str, readableArray);
    }

    public void showPopupMenu(int i, ReadableArray readableArray, Callback callback, Callback callback2) {
        assertViewExists(i, "showPopupMenu");
        this.mOperationsQueue.enqueueShowPopupMenu(i, readableArray, callback, callback2);
    }

    public void dismissPopupMenu() {
        this.mOperationsQueue.enqueueDismissPopupMenu();
    }

    public void sendAccessibilityEvent(int i, int i2) {
        this.mOperationsQueue.enqueueSendAccessibilityEvent(i, i2);
    }

    public void onHostResume() {
        this.mOperationsQueue.resumeFrameCallback();
    }

    public void onHostPause() {
        this.mOperationsQueue.pauseFrameCallback();
    }

    public void setViewHierarchyUpdateDebugListener(@Nullable NotThreadSafeViewHierarchyUpdateDebugListener notThreadSafeViewHierarchyUpdateDebugListener) {
        this.mOperationsQueue.setViewHierarchyUpdateDebugListener(notThreadSafeViewHierarchyUpdateDebugListener);
    }

    /* access modifiers changed from: protected */
    public final void removeShadowNode(ReactShadowNode reactShadowNode) {
        removeShadowNodeRecursive(reactShadowNode);
        reactShadowNode.dispose();
    }

    private void removeShadowNodeRecursive(ReactShadowNode reactShadowNode) {
        NativeViewHierarchyOptimizer.handleRemoveNode(reactShadowNode);
        this.mShadowNodeRegistry.removeNode(reactShadowNode.getReactTag());
        for (int childCount = reactShadowNode.getChildCount() - 1; childCount >= 0; childCount--) {
            removeShadowNodeRecursive(reactShadowNode.getChildAt(childCount));
        }
        reactShadowNode.removeAndDisposeAllChildren();
    }

    private void measureLayout(int i, int i2, int[] iArr) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        ReactShadowNode node2 = this.mShadowNodeRegistry.getNode(i2);
        String str = "Tag ";
        if (node == null || node2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            if (node != null) {
                i = i2;
            }
            sb.append(i);
            sb.append(" does not exist");
            throw new IllegalViewOperationException(sb.toString());
        }
        if (node != node2) {
            ReactShadowNode parent = node.getParent();
            while (parent != node2) {
                if (parent != null) {
                    parent = parent.getParent();
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(i2);
                    sb2.append(" is not an ancestor of tag ");
                    sb2.append(i);
                    throw new IllegalViewOperationException(sb2.toString());
                }
            }
        }
        measureLayoutRelativeToVerifiedAncestor(node, node2, iArr);
    }

    private void measureLayoutRelativeToParent(int i, int[] iArr) {
        ReactShadowNode node = this.mShadowNodeRegistry.getNode(i);
        if (node != null) {
            ReactShadowNode parent = node.getParent();
            if (parent != null) {
                measureLayoutRelativeToVerifiedAncestor(node, parent, iArr);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" doesn't have a parent!");
            throw new IllegalViewOperationException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("No native view for tag ");
        sb2.append(i);
        sb2.append(" exists!");
        throw new IllegalViewOperationException(sb2.toString());
    }

    private void measureLayoutRelativeToVerifiedAncestor(ReactShadowNode reactShadowNode, ReactShadowNode reactShadowNode2, int[] iArr) {
        int i;
        int i2;
        if (reactShadowNode != reactShadowNode2) {
            i2 = Math.round(reactShadowNode.getLayoutX());
            i = Math.round(reactShadowNode.getLayoutY());
            for (ReactShadowNode parent = reactShadowNode.getParent(); parent != reactShadowNode2; parent = parent.getParent()) {
                Assertions.assertNotNull(parent);
                assertNodeDoesNotNeedCustomLayoutForChildren(parent);
                i2 += Math.round(parent.getLayoutX());
                i += Math.round(parent.getLayoutY());
            }
            assertNodeDoesNotNeedCustomLayoutForChildren(reactShadowNode2);
        } else {
            i2 = 0;
            i = 0;
        }
        iArr[0] = i2;
        iArr[1] = i;
        iArr[2] = reactShadowNode.getScreenWidth();
        iArr[3] = reactShadowNode.getScreenHeight();
    }

    private void assertViewExists(int i, String str) {
        if (this.mShadowNodeRegistry.getNode(i) == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to execute operation ");
            sb.append(str);
            sb.append(" on view with tag: ");
            sb.append(i);
            sb.append(", since the view does not exists");
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    private void assertNodeDoesNotNeedCustomLayoutForChildren(ReactShadowNode reactShadowNode) {
        ViewManager viewManager = (ViewManager) Assertions.assertNotNull(this.mViewManagers.get(reactShadowNode.getViewClass()));
        if (viewManager instanceof IViewManagerWithChildren) {
            IViewManagerWithChildren iViewManagerWithChildren = (IViewManagerWithChildren) viewManager;
            if (iViewManagerWithChildren != null && iViewManagerWithChildren.needsCustomLayoutForChildren()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Trying to measure a view using measureLayout/measureLayoutRelativeToParent relative to an ancestor that requires custom layout for it's children (");
                sb.append(reactShadowNode.getViewClass());
                sb.append("). Use measure instead.");
                throw new IllegalViewOperationException(sb.toString());
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Trying to use view ");
        sb2.append(reactShadowNode.getViewClass());
        sb2.append(" as a parent, but its Manager doesn't extends ViewGroupManager");
        throw new IllegalViewOperationException(sb2.toString());
    }

    private void notifyOnBeforeLayoutRecursive(ReactShadowNode reactShadowNode) {
        if (reactShadowNode.hasUpdates()) {
            for (int i = 0; i < reactShadowNode.getChildCount(); i++) {
                notifyOnBeforeLayoutRecursive(reactShadowNode.getChildAt(i));
            }
            reactShadowNode.onBeforeLayout(this.mNativeViewHierarchyOptimizer);
        }
    }

    /* access modifiers changed from: protected */
    public void calculateRootLayout(ReactShadowNode reactShadowNode) {
        float f;
        SystraceMessage.beginSection(0, "cssRoot.calculateLayout").arg("rootTag", reactShadowNode.getReactTag()).flush();
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            int intValue = reactShadowNode.getWidthMeasureSpec().intValue();
            int intValue2 = reactShadowNode.getHeightMeasureSpec().intValue();
            float f2 = Float.NaN;
            if (MeasureSpec.getMode(intValue) == 0) {
                f = Float.NaN;
            } else {
                f = (float) MeasureSpec.getSize(intValue);
            }
            if (MeasureSpec.getMode(intValue2) != 0) {
                f2 = (float) MeasureSpec.getSize(intValue2);
            }
            reactShadowNode.calculateLayout(f, f2);
        } finally {
            Systrace.endSection(0);
            this.mLastCalculateLayoutTime = SystemClock.uptimeMillis() - uptimeMillis;
        }
    }

    /* access modifiers changed from: protected */
    public void applyUpdatesRecursive(ReactShadowNode reactShadowNode, float f, float f2) {
        if (reactShadowNode.hasUpdates()) {
            Iterable<ReactShadowNode> calculateLayoutOnChildren = reactShadowNode.calculateLayoutOnChildren();
            if (calculateLayoutOnChildren != null) {
                for (ReactShadowNode applyUpdatesRecursive : calculateLayoutOnChildren) {
                    applyUpdatesRecursive(applyUpdatesRecursive, reactShadowNode.getLayoutX() + f, reactShadowNode.getLayoutY() + f2);
                }
            }
            int reactTag = reactShadowNode.getReactTag();
            if (!this.mShadowNodeRegistry.isRootNode(reactTag) && reactShadowNode.dispatchUpdates(f, f2, this.mOperationsQueue, this.mNativeViewHierarchyOptimizer) && reactShadowNode.shouldNotifyOnLayout()) {
                this.mEventDispatcher.dispatchEvent(OnLayoutEvent.obtain(reactTag, reactShadowNode.getScreenX(), reactShadowNode.getScreenY(), reactShadowNode.getScreenWidth(), reactShadowNode.getScreenHeight()));
            }
            reactShadowNode.markUpdateSeen();
        }
    }

    public void addUIBlock(UIBlock uIBlock) {
        this.mOperationsQueue.enqueueUIBlock(uIBlock);
    }

    public void prependUIBlock(UIBlock uIBlock) {
        this.mOperationsQueue.prependUIBlock(uIBlock);
    }

    public int resolveRootTagFromReactTag(int i) {
        if (this.mShadowNodeRegistry.isRootNode(i)) {
            return i;
        }
        ReactShadowNode resolveShadowNode = resolveShadowNode(i);
        int i2 = 0;
        if (resolveShadowNode != null) {
            i2 = resolveShadowNode.getRootTag();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Warning : attempted to resolve a non-existent react shadow node. reactTag=");
            sb.append(i);
            FLog.m90w(ReactConstants.TAG, sb.toString());
        }
        return i2;
    }

    public void setLayoutUpdateListener(LayoutUpdateListener layoutUpdateListener) {
        this.mLayoutUpdateListener = layoutUpdateListener;
    }

    public void removeLayoutUpdateListener() {
        this.mLayoutUpdateListener = null;
    }
}
