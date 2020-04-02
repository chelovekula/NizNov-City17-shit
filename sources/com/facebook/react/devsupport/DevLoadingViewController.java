package com.facebook.react.devsupport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.react.C0604R;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.ReactConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class DevLoadingViewController {
    private static boolean sEnabled = true;
    @Nullable
    private PopupWindow mDevLoadingPopup;
    /* access modifiers changed from: private */
    @Nullable
    public TextView mDevLoadingView;
    private final ReactInstanceManagerDevHelper mReactInstanceManagerHelper;

    public static void setDevLoadingEnabled(boolean z) {
        sEnabled = z;
    }

    public DevLoadingViewController(Context context, ReactInstanceManagerDevHelper reactInstanceManagerDevHelper) {
        this.mReactInstanceManagerHelper = reactInstanceManagerDevHelper;
    }

    public void showMessage(final String str) {
        if (sEnabled) {
            UiThreadUtil.runOnUiThread(new Runnable() {
                public void run() {
                    DevLoadingViewController.this.showInternal(str);
                }
            });
        }
    }

    public void showForUrl(String str) {
        Context context = getContext();
        if (context != null) {
            try {
                URL url = new URL(str);
                int i = C0604R.string.catalyst_loading_from_url;
                StringBuilder sb = new StringBuilder();
                sb.append(url.getHost());
                sb.append(":");
                sb.append(url.getPort());
                showMessage(context.getString(i, new Object[]{sb.toString()}));
            } catch (MalformedURLException e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Bundle url format is invalid. \n\n");
                sb2.append(e.toString());
                FLog.m50e(ReactConstants.TAG, sb2.toString());
            }
        }
    }

    public void showForRemoteJSEnabled() {
        Context context = getContext();
        if (context != null) {
            showMessage(context.getString(C0604R.string.catalyst_debug_connecting));
        }
    }

    public void updateProgress(@Nullable final String str, @Nullable final Integer num, @Nullable final Integer num2) {
        if (sEnabled) {
            UiThreadUtil.runOnUiThread(new Runnable() {
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    String str = str;
                    if (str == null) {
                        str = "Loading";
                    }
                    sb.append(str);
                    if (num != null) {
                        Integer num = num2;
                        if (num != null && num.intValue() > 0) {
                            sb.append(String.format(Locale.getDefault(), " %.1f%% (%d/%d)", new Object[]{Float.valueOf((((float) num.intValue()) / ((float) num2.intValue())) * 100.0f), num, num2}));
                        }
                    }
                    sb.append("…");
                    if (DevLoadingViewController.this.mDevLoadingView != null) {
                        DevLoadingViewController.this.mDevLoadingView.setText(sb);
                    }
                }
            });
        }
    }

    public void hide() {
        if (sEnabled) {
            UiThreadUtil.runOnUiThread(new Runnable() {
                public void run() {
                    DevLoadingViewController.this.hideInternal();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void showInternal(String str) {
        PopupWindow popupWindow = this.mDevLoadingPopup;
        if (popupWindow == null || !popupWindow.isShowing()) {
            Activity currentActivity = this.mReactInstanceManagerHelper.getCurrentActivity();
            if (currentActivity == null) {
                FLog.m50e(ReactConstants.TAG, "Unable to display loading message because react activity isn't available");
                return;
            }
            Rect rect = new Rect();
            currentActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int i = rect.top;
            this.mDevLoadingView = (TextView) ((LayoutInflater) currentActivity.getSystemService("layout_inflater")).inflate(C0604R.layout.dev_loading_view, null);
            this.mDevLoadingView.setText(str);
            this.mDevLoadingPopup = new PopupWindow(this.mDevLoadingView, -1, -2);
            this.mDevLoadingPopup.setTouchable(false);
            this.mDevLoadingPopup.showAtLocation(currentActivity.getWindow().getDecorView(), 0, 0, i);
        }
    }

    /* access modifiers changed from: private */
    public void hideInternal() {
        PopupWindow popupWindow = this.mDevLoadingPopup;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.mDevLoadingPopup.dismiss();
            this.mDevLoadingPopup = null;
            this.mDevLoadingView = null;
        }
    }

    @Nullable
    private Context getContext() {
        return this.mReactInstanceManagerHelper.getCurrentActivity();
    }
}
