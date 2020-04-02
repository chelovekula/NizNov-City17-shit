package com.facebook.imagepipeline.datasource;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseListBitmapDataSubscriber extends BaseDataSubscriber<List<CloseableReference<CloseableImage>>> {
    /* access modifiers changed from: protected */
    public abstract void onNewResultListImpl(List<Bitmap> list);

    public void onNewResultImpl(DataSource<List<CloseableReference<CloseableImage>>> dataSource) {
        if (dataSource.isFinished()) {
            List<CloseableReference> list = (List) dataSource.getResult();
            if (list == null) {
                onNewResultListImpl(null);
                return;
            }
            try {
                ArrayList arrayList = new ArrayList(list.size());
                for (CloseableReference closeableReference : list) {
                    if (closeableReference == null || !(closeableReference.get() instanceof CloseableBitmap)) {
                        arrayList.add(null);
                    } else {
                        arrayList.add(((CloseableBitmap) closeableReference.get()).getUnderlyingBitmap());
                    }
                }
                onNewResultListImpl(arrayList);
            } finally {
                for (CloseableReference closeSafely : list) {
                    CloseableReference.closeSafely(closeSafely);
                }
            }
        }
    }
}
