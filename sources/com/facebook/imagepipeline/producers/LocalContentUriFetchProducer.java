package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import com.android.vending.expansion.zipfile.APEZProvider;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class LocalContentUriFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalContentUriFetchProducer";
    private static final String[] PROJECTION = {APEZProvider.FILEID, "_data"};
    private final ContentResolver mContentResolver;

    /* access modifiers changed from: protected */
    public String getProducerName() {
        return PRODUCER_NAME;
    }

    public LocalContentUriFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, ContentResolver contentResolver) {
        super(executor, pooledByteBufferFactory);
        this.mContentResolver = contentResolver;
    }

    /* access modifiers changed from: protected */
    public EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        InputStream inputStream;
        Uri sourceUri = imageRequest.getSourceUri();
        if (UriUtil.isLocalContactUri(sourceUri)) {
            if (sourceUri.toString().endsWith("/photo")) {
                inputStream = this.mContentResolver.openInputStream(sourceUri);
            } else {
                String str = "Contact photo does not exist: ";
                if (sourceUri.toString().endsWith("/display_photo")) {
                    try {
                        inputStream = this.mContentResolver.openAssetFileDescriptor(sourceUri, "r").createInputStream();
                    } catch (IOException unused) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(sourceUri);
                        throw new IOException(sb.toString());
                    }
                } else {
                    InputStream openContactPhotoInputStream = Contacts.openContactPhotoInputStream(this.mContentResolver, sourceUri);
                    if (openContactPhotoInputStream != null) {
                        inputStream = openContactPhotoInputStream;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(sourceUri);
                        throw new IOException(sb2.toString());
                    }
                }
            }
            return getEncodedImage(inputStream, -1);
        }
        if (UriUtil.isLocalCameraUri(sourceUri)) {
            EncodedImage cameraImage = getCameraImage(sourceUri);
            if (cameraImage != null) {
                return cameraImage;
            }
        }
        return getEncodedImage(this.mContentResolver.openInputStream(sourceUri), -1);
    }

    @Nullable
    private EncodedImage getCameraImage(Uri uri) throws IOException {
        Cursor query = this.mContentResolver.query(uri, PROJECTION, null, null, null);
        if (query == null) {
            return null;
        }
        try {
            if (query.getCount() == 0) {
                return null;
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("_data"));
            if (string != null) {
                EncodedImage encodedImage = getEncodedImage(new FileInputStream(string), getLength(string));
                query.close();
                return encodedImage;
            }
            query.close();
            return null;
        } finally {
            query.close();
        }
    }

    private static int getLength(String str) {
        if (str == null) {
            return -1;
        }
        return (int) new File(str).length();
    }
}
