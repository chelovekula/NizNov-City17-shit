package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.C0898Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.PendingResultUtil.ResultConverter;

final class zak implements ResultConverter<R, T> {
    private final /* synthetic */ C0898Response zaoz;

    zak(C0898Response response) {
        this.zaoz = response;
    }

    public final /* synthetic */ Object convert(Result result) {
        this.zaoz.setResult(result);
        return this.zaoz;
    }
}
