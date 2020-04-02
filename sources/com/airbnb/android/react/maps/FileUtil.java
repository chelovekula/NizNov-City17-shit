package com.airbnb.android.react.maps;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import com.facebook.common.logging.FLog;
import com.facebook.common.util.UriUtil;
import com.facebook.react.common.ReactConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileUtil extends AsyncTask<String, Void, InputStream> {
    private final String NAME = "FileUtil";
    private final String TEMP_FILE_SUFFIX = "temp";
    private Context context;
    private Exception exception;

    public FileUtil(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public InputStream doInBackground(String... strArr) {
        try {
            Uri parse = Uri.parse(strArr[0]);
            if (parse.getScheme().startsWith(UriUtil.HTTP_SCHEME)) {
                return getDownloadFileInputStream(this.context, parse);
            }
            return this.context.getContentResolver().openInputStream(parse);
        } catch (Exception e) {
            this.exception = e;
            StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve file for contentUri ");
            sb.append(strArr[0]);
            FLog.m51e(ReactConstants.TAG, sb.toString(), (Throwable) e);
            return null;
        }
    }

    private InputStream getDownloadFileInputStream(Context context2, Uri uri) throws IOException {
        FileOutputStream fileOutputStream;
        File createTempFile = File.createTempFile("FileUtil", "temp", context2.getApplicationContext().getCacheDir());
        createTempFile.deleteOnExit();
        InputStream openStream = new URL(uri.toString()).openStream();
        try {
            ReadableByteChannel newChannel = Channels.newChannel(openStream);
            try {
                fileOutputStream = new FileOutputStream(createTempFile);
                fileOutputStream.getChannel().transferFrom(newChannel, 0, Long.MAX_VALUE);
                FileInputStream fileInputStream = new FileInputStream(createTempFile);
                fileOutputStream.close();
                newChannel.close();
                return fileInputStream;
            } catch (Throwable th) {
                newChannel.close();
                throw th;
            }
        } finally {
            openStream.close();
        }
    }
}
