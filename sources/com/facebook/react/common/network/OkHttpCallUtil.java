package com.facebook.react.common.network;

import java.util.Iterator;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class OkHttpCallUtil {
    private OkHttpCallUtil() {
    }

    public static void cancelTag(OkHttpClient okHttpClient, Object obj) {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (obj.equals(call.request().tag())) {
                call.cancel();
                return;
            }
        }
        Iterator it = okHttpClient.dispatcher().runningCalls().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Call call2 = (Call) it.next();
            if (obj.equals(call2.request().tag())) {
                call2.cancel();
                break;
            }
        }
    }
}
