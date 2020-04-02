package com.facebook.react.modules.fresco;

import android.util.Pair;
import com.facebook.imagepipeline.listener.BaseRequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.Systrace.EventScope;
import java.util.HashMap;
import java.util.Map;

public class SystraceRequestListener extends BaseRequestListener {
    int mCurrentID = 0;
    Map<String, Pair<Integer, String>> mProducerID = new HashMap();
    Map<String, Pair<Integer, String>> mRequestsID = new HashMap();

    public boolean requiresExtraMap(String str) {
        return false;
    }

    public void onProducerStart(String str, String str2) {
        if (Systrace.isTracing(0)) {
            StringBuilder sb = new StringBuilder();
            sb.append("FRESCO_PRODUCER_");
            sb.append(str2.replace(':', '_'));
            Pair create = Pair.create(Integer.valueOf(this.mCurrentID), sb.toString());
            Systrace.beginAsyncSection(0, (String) create.second, this.mCurrentID);
            this.mProducerID.put(str, create);
            this.mCurrentID++;
        }
    }

    public void onProducerFinishWithSuccess(String str, String str2, Map<String, String> map) {
        if (Systrace.isTracing(0) && this.mProducerID.containsKey(str)) {
            Pair pair = (Pair) this.mProducerID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mProducerID.remove(str);
        }
    }

    public void onProducerFinishWithFailure(String str, String str2, Throwable th, Map<String, String> map) {
        if (Systrace.isTracing(0) && this.mProducerID.containsKey(str)) {
            Pair pair = (Pair) this.mProducerID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mProducerID.remove(str);
        }
    }

    public void onProducerFinishWithCancellation(String str, String str2, Map<String, String> map) {
        if (Systrace.isTracing(0) && this.mProducerID.containsKey(str)) {
            Pair pair = (Pair) this.mProducerID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mProducerID.remove(str);
        }
    }

    public void onProducerEvent(String str, String str2, String str3) {
        if (Systrace.isTracing(0)) {
            StringBuilder sb = new StringBuilder();
            sb.append("FRESCO_PRODUCER_EVENT_");
            sb.append(str.replace(':', '_'));
            String str4 = "_";
            sb.append(str4);
            sb.append(str2.replace(':', '_'));
            sb.append(str4);
            sb.append(str3.replace(':', '_'));
            Systrace.traceInstant(0, sb.toString(), EventScope.THREAD);
        }
    }

    public void onRequestStart(ImageRequest imageRequest, Object obj, String str, boolean z) {
        if (Systrace.isTracing(0)) {
            StringBuilder sb = new StringBuilder();
            sb.append("FRESCO_REQUEST_");
            sb.append(imageRequest.getSourceUri().toString().replace(':', '_'));
            Pair create = Pair.create(Integer.valueOf(this.mCurrentID), sb.toString());
            Systrace.beginAsyncSection(0, (String) create.second, this.mCurrentID);
            this.mRequestsID.put(str, create);
            this.mCurrentID++;
        }
    }

    public void onRequestSuccess(ImageRequest imageRequest, String str, boolean z) {
        if (Systrace.isTracing(0) && this.mRequestsID.containsKey(str)) {
            Pair pair = (Pair) this.mRequestsID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mRequestsID.remove(str);
        }
    }

    public void onRequestFailure(ImageRequest imageRequest, String str, Throwable th, boolean z) {
        if (Systrace.isTracing(0) && this.mRequestsID.containsKey(str)) {
            Pair pair = (Pair) this.mRequestsID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mRequestsID.remove(str);
        }
    }

    public void onRequestCancellation(String str) {
        if (Systrace.isTracing(0) && this.mRequestsID.containsKey(str)) {
            Pair pair = (Pair) this.mRequestsID.get(str);
            Systrace.endAsyncSection(0, (String) pair.second, ((Integer) pair.first).intValue());
            this.mRequestsID.remove(str);
        }
    }
}
