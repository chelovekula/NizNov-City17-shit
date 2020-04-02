package com.facebook.react.jstasks;

public class NoRetryPolicy implements HeadlessJsTaskRetryPolicy {
    public static final NoRetryPolicy INSTANCE = new NoRetryPolicy();

    public boolean canRetry() {
        return false;
    }

    public HeadlessJsTaskRetryPolicy copy() {
        return this;
    }

    private NoRetryPolicy() {
    }

    public int getDelay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Should not retrieve delay as canRetry is: ");
        sb.append(canRetry());
        throw new IllegalStateException(sb.toString());
    }

    public HeadlessJsTaskRetryPolicy update() {
        StringBuilder sb = new StringBuilder();
        sb.append("Should not update as canRetry is: ");
        sb.append(canRetry());
        throw new IllegalStateException(sb.toString());
    }
}
