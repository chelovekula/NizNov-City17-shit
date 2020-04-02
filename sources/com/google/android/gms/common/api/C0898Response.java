package com.google.android.gms.common.api;

import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Result;

/* renamed from: com.google.android.gms.common.api.Response */
public class C0898Response<T extends Result> {
    private T zzap;

    public C0898Response() {
    }

    protected C0898Response(@NonNull T t) {
        this.zzap = t;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public T getResult() {
        return this.zzap;
    }

    public void setResult(@NonNull T t) {
        this.zzap = t;
    }
}
