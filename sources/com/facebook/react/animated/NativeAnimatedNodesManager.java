package com.facebook.react.animated;

import android.util.SparseArray;
import androidx.annotation.Nullable;
import com.brentvatne.react.ReactVideoView;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.UIManagerModule.CustomEventNamesResolver;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class NativeAnimatedNodesManager implements EventDispatcherListener {
    private final SparseArray<AnimationDriver> mActiveAnimations = new SparseArray<>();
    private int mAnimatedGraphBFSColor = 0;
    private final SparseArray<AnimatedNode> mAnimatedNodes = new SparseArray<>();
    private final CustomEventNamesResolver mCustomEventNamesResolver;
    private final Map<String, List<EventAnimationDriver>> mEventDrivers = new HashMap();
    private final List<AnimatedNode> mRunUpdateNodeList = new LinkedList();
    private final UIManagerModule mUIManagerModule;
    private final SparseArray<AnimatedNode> mUpdatedNodes = new SparseArray<>();

    public NativeAnimatedNodesManager(UIManagerModule uIManagerModule) {
        this.mUIManagerModule = uIManagerModule;
        uIManagerModule.getEventDispatcher().addListener(this);
        this.mCustomEventNamesResolver = uIManagerModule.getDirectEventNamesResolver();
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public AnimatedNode getNodeById(int i) {
        return (AnimatedNode) this.mAnimatedNodes.get(i);
    }

    public boolean hasActiveAnimations() {
        return this.mActiveAnimations.size() > 0 || this.mUpdatedNodes.size() > 0;
    }

    public void createAnimatedNode(int i, ReadableMap readableMap) {
        AnimatedNode animatedNode;
        if (this.mAnimatedNodes.get(i) == null) {
            String string = readableMap.getString(ReactVideoViewManager.PROP_SRC_TYPE);
            if ("style".equals(string)) {
                animatedNode = new StyleAnimatedNode(readableMap, this);
            } else if (ReactVideoView.EVENT_PROP_METADATA_VALUE.equals(string)) {
                animatedNode = new ValueAnimatedNode(readableMap);
            } else if ("props".equals(string)) {
                animatedNode = new PropsAnimatedNode(readableMap, this, this.mUIManagerModule);
            } else if ("interpolation".equals(string)) {
                animatedNode = new InterpolationAnimatedNode(readableMap);
            } else if ("addition".equals(string)) {
                animatedNode = new AdditionAnimatedNode(readableMap, this);
            } else if ("subtraction".equals(string)) {
                animatedNode = new SubtractionAnimatedNode(readableMap, this);
            } else if ("division".equals(string)) {
                animatedNode = new DivisionAnimatedNode(readableMap, this);
            } else if ("multiplication".equals(string)) {
                animatedNode = new MultiplicationAnimatedNode(readableMap, this);
            } else if ("modulus".equals(string)) {
                animatedNode = new ModulusAnimatedNode(readableMap, this);
            } else if ("diffclamp".equals(string)) {
                animatedNode = new DiffClampAnimatedNode(readableMap, this);
            } else if (ViewProps.TRANSFORM.equals(string)) {
                animatedNode = new TransformAnimatedNode(readableMap, this);
            } else if ("tracking".equals(string)) {
                animatedNode = new TrackingAnimatedNode(readableMap, this);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported node type: ");
                sb.append(string);
                throw new JSApplicationIllegalArgumentException(sb.toString());
            }
            animatedNode.mTag = i;
            this.mAnimatedNodes.put(i, animatedNode);
            this.mUpdatedNodes.put(i, animatedNode);
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Animated node with tag ");
        sb2.append(i);
        sb2.append(" already exists");
        throw new JSApplicationIllegalArgumentException(sb2.toString());
    }

    public void dropAnimatedNode(int i) {
        this.mAnimatedNodes.remove(i);
        this.mUpdatedNodes.remove(i);
    }

    public void startListeningToAnimatedNodeValue(int i, AnimatedNodeValueListener animatedNodeValueListener) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        ((ValueAnimatedNode) animatedNode).setValueListener(animatedNodeValueListener);
    }

    public void stopListeningToAnimatedNodeValue(int i) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        ((ValueAnimatedNode) animatedNode).setValueListener(null);
    }

    public void setAnimatedNodeValue(int i, double d) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        stopAnimationsForNode(animatedNode);
        ((ValueAnimatedNode) animatedNode).mValue = d;
        this.mUpdatedNodes.put(i, animatedNode);
    }

    public void setAnimatedNodeOffset(int i, double d) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        ((ValueAnimatedNode) animatedNode).mOffset = d;
        this.mUpdatedNodes.put(i, animatedNode);
    }

    public void flattenAnimatedNodeOffset(int i) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        ((ValueAnimatedNode) animatedNode).flattenOffset();
    }

    public void extractAnimatedNodeOffset(int i) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null || !(animatedNode instanceof ValueAnimatedNode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists or is not a 'value' node");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        ((ValueAnimatedNode) animatedNode).extractOffset();
    }

    public void startAnimatingNode(int i, int i2, ReadableMap readableMap, Callback callback) {
        AnimationDriver animationDriver;
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i2);
        if (animatedNode == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i2);
            sb.append(" does not exists");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        } else if (animatedNode instanceof ValueAnimatedNode) {
            AnimationDriver animationDriver2 = (AnimationDriver) this.mActiveAnimations.get(i);
            if (animationDriver2 != null) {
                animationDriver2.resetConfig(readableMap);
                return;
            }
            String string = readableMap.getString(ReactVideoViewManager.PROP_SRC_TYPE);
            if ("frames".equals(string)) {
                animationDriver = new FrameBasedAnimationDriver(readableMap);
            } else if ("spring".equals(string)) {
                animationDriver = new SpringAnimation(readableMap);
            } else if ("decay".equals(string)) {
                animationDriver = new DecayAnimation(readableMap);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unsupported animation type: ");
                sb2.append(string);
                throw new JSApplicationIllegalArgumentException(sb2.toString());
            }
            animationDriver.mId = i;
            animationDriver.mEndCallback = callback;
            animationDriver.mAnimatedValue = (ValueAnimatedNode) animatedNode;
            this.mActiveAnimations.put(i, animationDriver);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Animated node should be of type ");
            sb3.append(ValueAnimatedNode.class.getName());
            throw new JSApplicationIllegalArgumentException(sb3.toString());
        }
    }

    private void stopAnimationsForNode(AnimatedNode animatedNode) {
        int i = 0;
        while (i < this.mActiveAnimations.size()) {
            AnimationDriver animationDriver = (AnimationDriver) this.mActiveAnimations.valueAt(i);
            if (animatedNode.equals(animationDriver.mAnimatedValue)) {
                if (animationDriver.mEndCallback != null) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putBoolean("finished", false);
                    animationDriver.mEndCallback.invoke(createMap);
                }
                this.mActiveAnimations.removeAt(i);
                i--;
            }
            i++;
        }
    }

    public void stopAnimation(int i) {
        for (int i2 = 0; i2 < this.mActiveAnimations.size(); i2++) {
            AnimationDriver animationDriver = (AnimationDriver) this.mActiveAnimations.valueAt(i2);
            if (animationDriver.mId == i) {
                if (animationDriver.mEndCallback != null) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putBoolean("finished", false);
                    animationDriver.mEndCallback.invoke(createMap);
                }
                this.mActiveAnimations.removeAt(i2);
                return;
            }
        }
    }

    public void connectAnimatedNodes(int i, int i2) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        String str = " does not exists";
        String str2 = "Animated node with tag ";
        if (animatedNode != null) {
            AnimatedNode animatedNode2 = (AnimatedNode) this.mAnimatedNodes.get(i2);
            if (animatedNode2 != null) {
                animatedNode.addChild(animatedNode2);
                this.mUpdatedNodes.put(i2, animatedNode2);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(i2);
            sb.append(str);
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(i);
        sb2.append(str);
        throw new JSApplicationIllegalArgumentException(sb2.toString());
    }

    public void disconnectAnimatedNodes(int i, int i2) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        String str = " does not exists";
        String str2 = "Animated node with tag ";
        if (animatedNode != null) {
            AnimatedNode animatedNode2 = (AnimatedNode) this.mAnimatedNodes.get(i2);
            if (animatedNode2 != null) {
                animatedNode.removeChild(animatedNode2);
                this.mUpdatedNodes.put(i2, animatedNode2);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(i2);
            sb.append(str);
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(i);
        sb2.append(str);
        throw new JSApplicationIllegalArgumentException(sb2.toString());
    }

    public void connectAnimatedNodeToView(int i, int i2) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        } else if (animatedNode instanceof PropsAnimatedNode) {
            ((PropsAnimatedNode) animatedNode).connectToView(i2);
            this.mUpdatedNodes.put(i, animatedNode);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Animated node connected to view should beof type ");
            sb2.append(PropsAnimatedNode.class.getName());
            throw new JSApplicationIllegalArgumentException(sb2.toString());
        }
    }

    public void disconnectAnimatedNodeFromView(int i, int i2) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i);
            sb.append(" does not exists");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        } else if (animatedNode instanceof PropsAnimatedNode) {
            ((PropsAnimatedNode) animatedNode).disconnectFromView(i2);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Animated node connected to view should beof type ");
            sb2.append(PropsAnimatedNode.class.getName());
            throw new JSApplicationIllegalArgumentException(sb2.toString());
        }
    }

    public void restoreDefaultValues(int i, int i2) {
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i);
        if (animatedNode != null) {
            if (animatedNode instanceof PropsAnimatedNode) {
                ((PropsAnimatedNode) animatedNode).restoreDefaultValues();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node connected to view should beof type ");
            sb.append(PropsAnimatedNode.class.getName());
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
    }

    public void addAnimatedEventToView(int i, String str, ReadableMap readableMap) {
        int i2 = readableMap.getInt("animatedValueTag");
        AnimatedNode animatedNode = (AnimatedNode) this.mAnimatedNodes.get(i2);
        if (animatedNode == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Animated node with tag ");
            sb.append(i2);
            sb.append(" does not exists");
            throw new JSApplicationIllegalArgumentException(sb.toString());
        } else if (animatedNode instanceof ValueAnimatedNode) {
            ReadableArray array = readableMap.getArray("nativeEventPath");
            ArrayList arrayList = new ArrayList(array.size());
            for (int i3 = 0; i3 < array.size(); i3++) {
                arrayList.add(array.getString(i3));
            }
            EventAnimationDriver eventAnimationDriver = new EventAnimationDriver(arrayList, (ValueAnimatedNode) animatedNode);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(i);
            sb2.append(str);
            String sb3 = sb2.toString();
            if (this.mEventDrivers.containsKey(sb3)) {
                ((List) this.mEventDrivers.get(sb3)).add(eventAnimationDriver);
                return;
            }
            ArrayList arrayList2 = new ArrayList(1);
            arrayList2.add(eventAnimationDriver);
            this.mEventDrivers.put(sb3, arrayList2);
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Animated node connected to event should beof type ");
            sb4.append(ValueAnimatedNode.class.getName());
            throw new JSApplicationIllegalArgumentException(sb4.toString());
        }
    }

    public void removeAnimatedEventFromView(int i, String str, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(str);
        String sb2 = sb.toString();
        if (this.mEventDrivers.containsKey(sb2)) {
            List list = (List) this.mEventDrivers.get(sb2);
            if (list.size() == 1) {
                Map<String, List<EventAnimationDriver>> map = this.mEventDrivers;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(i);
                sb3.append(str);
                map.remove(sb3.toString());
                return;
            }
            ListIterator listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                if (((EventAnimationDriver) listIterator.next()).mValueNode.mTag == i2) {
                    listIterator.remove();
                    return;
                }
            }
        }
    }

    public void onEventDispatch(final Event event) {
        if (UiThreadUtil.isOnUiThread()) {
            handleEvent(event);
        } else {
            UiThreadUtil.runOnUiThread(new Runnable() {
                public void run() {
                    NativeAnimatedNodesManager.this.handleEvent(event);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleEvent(Event event) {
        if (!this.mEventDrivers.isEmpty()) {
            String resolveCustomEventName = this.mCustomEventNamesResolver.resolveCustomEventName(event.getEventName());
            Map<String, List<EventAnimationDriver>> map = this.mEventDrivers;
            StringBuilder sb = new StringBuilder();
            sb.append(event.getViewTag());
            sb.append(resolveCustomEventName);
            List<EventAnimationDriver> list = (List) map.get(sb.toString());
            if (list != null) {
                for (EventAnimationDriver eventAnimationDriver : list) {
                    stopAnimationsForNode(eventAnimationDriver.mValueNode);
                    event.dispatch(eventAnimationDriver);
                    this.mRunUpdateNodeList.add(eventAnimationDriver.mValueNode);
                }
                updateNodes(this.mRunUpdateNodeList);
                this.mRunUpdateNodeList.clear();
            }
        }
    }

    public void runUpdates(long j) {
        UiThreadUtil.assertOnUiThread();
        for (int i = 0; i < this.mUpdatedNodes.size(); i++) {
            this.mRunUpdateNodeList.add((AnimatedNode) this.mUpdatedNodes.valueAt(i));
        }
        this.mUpdatedNodes.clear();
        boolean z = false;
        for (int i2 = 0; i2 < this.mActiveAnimations.size(); i2++) {
            AnimationDriver animationDriver = (AnimationDriver) this.mActiveAnimations.valueAt(i2);
            animationDriver.runAnimationStep(j);
            this.mRunUpdateNodeList.add(animationDriver.mAnimatedValue);
            if (animationDriver.mHasFinished) {
                z = true;
            }
        }
        updateNodes(this.mRunUpdateNodeList);
        this.mRunUpdateNodeList.clear();
        if (z) {
            for (int size = this.mActiveAnimations.size() - 1; size >= 0; size--) {
                AnimationDriver animationDriver2 = (AnimationDriver) this.mActiveAnimations.valueAt(size);
                if (animationDriver2.mHasFinished) {
                    if (animationDriver2.mEndCallback != null) {
                        WritableMap createMap = Arguments.createMap();
                        createMap.putBoolean("finished", true);
                        animationDriver2.mEndCallback.invoke(createMap);
                    }
                    this.mActiveAnimations.removeAt(size);
                }
            }
        }
    }

    private void updateNodes(List<AnimatedNode> list) {
        this.mAnimatedGraphBFSColor++;
        int i = this.mAnimatedGraphBFSColor;
        if (i == 0) {
            this.mAnimatedGraphBFSColor = i + 1;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        int i2 = 0;
        for (AnimatedNode animatedNode : list) {
            int i3 = animatedNode.mBFSColor;
            int i4 = this.mAnimatedGraphBFSColor;
            if (i3 != i4) {
                animatedNode.mBFSColor = i4;
                i2++;
                arrayDeque.add(animatedNode);
            }
        }
        while (!arrayDeque.isEmpty()) {
            AnimatedNode animatedNode2 = (AnimatedNode) arrayDeque.poll();
            if (animatedNode2.mChildren != null) {
                int i5 = i2;
                for (int i6 = 0; i6 < animatedNode2.mChildren.size(); i6++) {
                    AnimatedNode animatedNode3 = (AnimatedNode) animatedNode2.mChildren.get(i6);
                    animatedNode3.mActiveIncomingNodes++;
                    int i7 = animatedNode3.mBFSColor;
                    int i8 = this.mAnimatedGraphBFSColor;
                    if (i7 != i8) {
                        animatedNode3.mBFSColor = i8;
                        i5++;
                        arrayDeque.add(animatedNode3);
                    }
                }
                i2 = i5;
            }
        }
        this.mAnimatedGraphBFSColor++;
        int i9 = this.mAnimatedGraphBFSColor;
        if (i9 == 0) {
            this.mAnimatedGraphBFSColor = i9 + 1;
        }
        int i10 = 0;
        for (AnimatedNode animatedNode4 : list) {
            if (animatedNode4.mActiveIncomingNodes == 0) {
                int i11 = animatedNode4.mBFSColor;
                int i12 = this.mAnimatedGraphBFSColor;
                if (i11 != i12) {
                    animatedNode4.mBFSColor = i12;
                    i10++;
                    arrayDeque.add(animatedNode4);
                }
            }
        }
        while (!arrayDeque.isEmpty()) {
            AnimatedNode animatedNode5 = (AnimatedNode) arrayDeque.poll();
            animatedNode5.update();
            if (animatedNode5 instanceof PropsAnimatedNode) {
                try {
                    ((PropsAnimatedNode) animatedNode5).updateView();
                } catch (IllegalViewOperationException e) {
                    FLog.m51e(ReactConstants.TAG, "Native animation workaround, frame lost as result of race condition", (Throwable) e);
                }
            }
            if (animatedNode5 instanceof ValueAnimatedNode) {
                ((ValueAnimatedNode) animatedNode5).onValueUpdate();
            }
            if (animatedNode5.mChildren != null) {
                int i13 = i10;
                for (int i14 = 0; i14 < animatedNode5.mChildren.size(); i14++) {
                    AnimatedNode animatedNode6 = (AnimatedNode) animatedNode5.mChildren.get(i14);
                    animatedNode6.mActiveIncomingNodes--;
                    if (animatedNode6.mBFSColor != this.mAnimatedGraphBFSColor && animatedNode6.mActiveIncomingNodes == 0) {
                        animatedNode6.mBFSColor = this.mAnimatedGraphBFSColor;
                        i13++;
                        arrayDeque.add(animatedNode6);
                    }
                }
                i10 = i13;
            }
        }
        if (i2 != i10) {
            StringBuilder sb = new StringBuilder();
            sb.append("Looks like animated nodes graph has cycles, there are ");
            sb.append(i2);
            sb.append(" but toposort visited only ");
            sb.append(i10);
            throw new IllegalStateException(sb.toString());
        }
    }
}
