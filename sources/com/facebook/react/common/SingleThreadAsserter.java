package com.facebook.react.common;

import androidx.annotation.Nullable;
import com.facebook.infer.annotation.Assertions;

public class SingleThreadAsserter {
    @Nullable
    private Thread mThread = null;

    public void assertNow() {
        Thread currentThread = Thread.currentThread();
        if (this.mThread == null) {
            this.mThread = currentThread;
        }
        Assertions.assertCondition(this.mThread == currentThread);
    }
}