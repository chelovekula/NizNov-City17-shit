package com.facebook.react.modules.network;

import android.content.Context;
import android.os.Build.VERSION;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.TlsVersion;

public class OkHttpClientProvider {
    @Nullable
    private static OkHttpClient sClient;
    @Nullable
    private static OkHttpClientFactory sFactory;

    public static void setOkHttpClientFactory(OkHttpClientFactory okHttpClientFactory) {
        sFactory = okHttpClientFactory;
    }

    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            sClient = createClient();
        }
        return sClient;
    }

    public static OkHttpClient createClient() {
        OkHttpClientFactory okHttpClientFactory = sFactory;
        if (okHttpClientFactory != null) {
            return okHttpClientFactory.createNewNetworkModuleClient();
        }
        return createClientBuilder().build();
    }

    public static OkHttpClient createClient(Context context) {
        OkHttpClientFactory okHttpClientFactory = sFactory;
        if (okHttpClientFactory != null) {
            return okHttpClientFactory.createNewNetworkModuleClient();
        }
        return createClientBuilder(context).build();
    }

    public static Builder createClientBuilder() {
        Builder cookieJar = new Builder().connectTimeout(0, TimeUnit.MILLISECONDS).readTimeout(0, TimeUnit.MILLISECONDS).writeTimeout(0, TimeUnit.MILLISECONDS).cookieJar(new ReactCookieJarContainer());
        try {
            Security.insertProviderAt((Provider) Class.forName("org.conscrypt.OpenSSLProvider").newInstance(), 1);
            return cookieJar;
        } catch (Exception unused) {
            return enableTls12OnPreLollipop(cookieJar);
        }
    }

    public static Builder createClientBuilder(Context context) {
        return createClientBuilder(context, 10485760);
    }

    public static Builder createClientBuilder(Context context, int i) {
        Builder createClientBuilder = createClientBuilder();
        if (i == 0) {
            return createClientBuilder;
        }
        return createClientBuilder.cache(new Cache(new File(context.getCacheDir(), "http-cache"), (long) i));
    }

    public static Builder enableTls12OnPreLollipop(Builder builder) {
        if (VERSION.SDK_INT <= 19) {
            try {
                builder.sslSocketFactory(new TLSSocketFactory());
                ConnectionSpec build = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                ArrayList arrayList = new ArrayList();
                arrayList.add(build);
                arrayList.add(ConnectionSpec.COMPATIBLE_TLS);
                arrayList.add(ConnectionSpec.CLEARTEXT);
                builder.connectionSpecs(arrayList);
            } catch (Exception e) {
                FLog.m51e("OkHttpClientProvider", "Error while enabling TLS 1.2", (Throwable) e);
            }
        }
        return builder;
    }
}
