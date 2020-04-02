package com.facebook.react.modules.datepicker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

@SuppressLint({"ValidFragment"})
public class DatePickerDialogFragment extends DialogFragment {
    private static final long DEFAULT_MIN_DATE = -2208988800001L;
    @Nullable
    private OnDateSetListener mOnDateSetListener;
    @Nullable
    private OnDismissListener mOnDismissListener;

    /* renamed from: com.facebook.react.modules.datepicker.DatePickerDialogFragment$1 */
    static /* synthetic */ class C07851 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode = new int[DatePickerMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.facebook.react.modules.datepicker.DatePickerMode[] r0 = com.facebook.react.modules.datepicker.DatePickerMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode = r0
                int[] r0 = $SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.facebook.react.modules.datepicker.DatePickerMode r1 = com.facebook.react.modules.datepicker.DatePickerMode.CALENDAR     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode     // Catch:{ NoSuchFieldError -> 0x001f }
                com.facebook.react.modules.datepicker.DatePickerMode r1 = com.facebook.react.modules.datepicker.DatePickerMode.SPINNER     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode     // Catch:{ NoSuchFieldError -> 0x002a }
                com.facebook.react.modules.datepicker.DatePickerMode r1 = com.facebook.react.modules.datepicker.DatePickerMode.DEFAULT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.datepicker.DatePickerDialogFragment.C07851.<clinit>():void");
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return createDialog(getArguments(), getActivity(), this.mOnDateSetListener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0103  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.app.Dialog createDialog(android.os.Bundle r13, android.content.Context r14, @androidx.annotation.Nullable android.app.DatePickerDialog.OnDateSetListener r15) {
        /*
            java.util.Calendar r0 = java.util.Calendar.getInstance()
            if (r13 == 0) goto L_0x0015
            java.lang.String r1 = "date"
            boolean r2 = r13.containsKey(r1)
            if (r2 == 0) goto L_0x0015
            long r1 = r13.getLong(r1)
            r0.setTimeInMillis(r1)
        L_0x0015:
            r1 = 1
            int r6 = r0.get(r1)
            r8 = 2
            int r7 = r0.get(r8)
            r2 = 5
            int r9 = r0.get(r2)
            com.facebook.react.modules.datepicker.DatePickerMode r2 = com.facebook.react.modules.datepicker.DatePickerMode.DEFAULT
            r3 = 0
            if (r13 == 0) goto L_0x003f
            java.lang.String r4 = "mode"
            java.lang.String r5 = r13.getString(r4, r3)
            if (r5 == 0) goto L_0x003f
            java.lang.String r2 = r13.getString(r4)
            java.util.Locale r4 = java.util.Locale.US
            java.lang.String r2 = r2.toUpperCase(r4)
            com.facebook.react.modules.datepicker.DatePickerMode r2 = com.facebook.react.modules.datepicker.DatePickerMode.valueOf(r2)
        L_0x003f:
            r10 = r2
            int r2 = android.os.Build.VERSION.SDK_INT
            r4 = 21
            r11 = 0
            if (r2 < r4) goto L_0x0099
            int[] r2 = com.facebook.react.modules.datepicker.DatePickerDialogFragment.C07851.$SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode
            int r4 = r10.ordinal()
            r2 = r2[r4]
            if (r2 == r1) goto L_0x007f
            if (r2 == r8) goto L_0x0066
            r1 = 3
            if (r2 == r1) goto L_0x0059
            r1 = r3
            goto L_0x00c8
        L_0x0059:
            com.facebook.react.modules.datepicker.DismissableDatePickerDialog r1 = new com.facebook.react.modules.datepicker.DismissableDatePickerDialog
            r2 = r1
            r3 = r14
            r4 = r15
            r5 = r6
            r6 = r7
            r7 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x00c8
        L_0x0066:
            com.facebook.react.modules.datepicker.DismissableDatePickerDialog r1 = new com.facebook.react.modules.datepicker.DismissableDatePickerDialog
            r4 = 16973939(0x1030073, float:2.4061222E-38)
            r2 = r1
            r3 = r14
            r5 = r15
            r8 = r9
            r2.<init>(r3, r4, r5, r6, r7, r8)
            android.view.Window r14 = r1.getWindow()
            android.graphics.drawable.ColorDrawable r15 = new android.graphics.drawable.ColorDrawable
            r15.<init>(r11)
            r14.setBackgroundDrawable(r15)
            goto L_0x00c8
        L_0x007f:
            com.facebook.react.modules.datepicker.DismissableDatePickerDialog r1 = new com.facebook.react.modules.datepicker.DismissableDatePickerDialog
            android.content.res.Resources r2 = r14.getResources()
            java.lang.String r3 = r14.getPackageName()
            java.lang.String r4 = "CalendarDatePickerDialog"
            java.lang.String r5 = "style"
            int r4 = r2.getIdentifier(r4, r5, r3)
            r2 = r1
            r3 = r14
            r5 = r15
            r8 = r9
            r2.<init>(r3, r4, r5, r6, r7, r8)
            goto L_0x00c8
        L_0x0099:
            com.facebook.react.modules.datepicker.DismissableDatePickerDialog r12 = new com.facebook.react.modules.datepicker.DismissableDatePickerDialog
            r2 = r12
            r3 = r14
            r4 = r15
            r5 = r6
            r6 = r7
            r7 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            int[] r14 = com.facebook.react.modules.datepicker.DatePickerDialogFragment.C07851.$SwitchMap$com$facebook$react$modules$datepicker$DatePickerMode
            int r15 = r10.ordinal()
            r14 = r14[r15]
            if (r14 == r1) goto L_0x00b9
            if (r14 == r8) goto L_0x00b1
            goto L_0x00c7
        L_0x00b1:
            android.widget.DatePicker r14 = r12.getDatePicker()
            r14.setCalendarViewShown(r11)
            goto L_0x00c7
        L_0x00b9:
            android.widget.DatePicker r14 = r12.getDatePicker()
            r14.setCalendarViewShown(r1)
            android.widget.DatePicker r14 = r12.getDatePicker()
            r14.setSpinnersShown(r11)
        L_0x00c7:
            r1 = r12
        L_0x00c8:
            android.widget.DatePicker r14 = r1.getDatePicker()
            r15 = 14
            r2 = 13
            r3 = 12
            r4 = 11
            if (r13 == 0) goto L_0x00f9
            java.lang.String r5 = "minDate"
            boolean r6 = r13.containsKey(r5)
            if (r6 == 0) goto L_0x00f9
            long r5 = r13.getLong(r5)
            r0.setTimeInMillis(r5)
            r0.set(r4, r11)
            r0.set(r3, r11)
            r0.set(r2, r11)
            r0.set(r15, r11)
            long r5 = r0.getTimeInMillis()
            r14.setMinDate(r5)
            goto L_0x0101
        L_0x00f9:
            r5 = -2208988800001(0xfffffdfdae01dbff, double:NaN)
            r14.setMinDate(r5)
        L_0x0101:
            if (r13 == 0) goto L_0x012b
            java.lang.String r5 = "maxDate"
            boolean r6 = r13.containsKey(r5)
            if (r6 == 0) goto L_0x012b
            long r5 = r13.getLong(r5)
            r0.setTimeInMillis(r5)
            r13 = 23
            r0.set(r4, r13)
            r13 = 59
            r0.set(r3, r13)
            r0.set(r2, r13)
            r13 = 999(0x3e7, float:1.4E-42)
            r0.set(r15, r13)
            long r2 = r0.getTimeInMillis()
            r14.setMaxDate(r2)
        L_0x012b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.datepicker.DatePickerDialogFragment.createDialog(android.os.Bundle, android.content.Context, android.app.DatePickerDialog$OnDateSetListener):android.app.Dialog");
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOnDateSetListener(@Nullable OnDateSetListener onDateSetListener) {
        this.mOnDateSetListener = onDateSetListener;
    }

    /* access modifiers changed from: 0000 */
    public void setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }
}
