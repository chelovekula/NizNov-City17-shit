package com.nizhniy_mobile;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import com.RNFetchBlob.RNFetchBlobPackage;
import com.RNTextInputMask.RNTextInputMaskPackage;
import com.airbnb.android.react.maps.MapsPackage;
import com.brentvatne.react.ReactVideoPackage;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.horcrux.svg.SvgPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.reactnativecommunity.geolocation.GeolocationPackage;
import com.reactnativecommunity.netinfo.NetInfoPackage;
import com.reactnativecommunity.slider.ReactSliderPackage;
import com.reactnativecommunity.viewpager.RNCViewPagerPackage;
import com.reactnativecommunity.webview.RNCWebViewPackage;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.swmansion.reanimated.ReanimatedPackage;
import java.util.Arrays;
import java.util.List;
import org.devio.p010rn.splashscreen.SplashScreenReactPackage;

public class MainApplication extends Application implements ReactApplication {
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        /* access modifiers changed from: protected */
        public String getJSMainModuleName() {
            return "index";
        }

        public boolean getUseDeveloperSupport() {
            return false;
        }

        /* access modifiers changed from: protected */
        public List<ReactPackage> getPackages() {
            return Arrays.asList(new ReactPackage[]{new MainReactPackage(), new ReactSliderPackage(), new RNFetchBlobPackage(), new ReactVideoPackage(), new VectorIconsPackage(), new ReactVideoPackage(), new GeolocationPackage(), new MapsPackage(), new RNTextInputMaskPackage(), new NetInfoPackage(), new RNCViewPagerPackage(), new RNGestureHandlerPackage(), new ReanimatedPackage(), new SvgPackage(), new RNCWebViewPackage(), new SplashScreenReactPackage()});
        }
    };

    private static void initializeFlipper(Context context) {
    }

    public ReactNativeHost getReactNativeHost() {
        return this.mReactNativeHost;
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        SoLoader.init((Context) this, false);
        initializeFlipper(this);
    }
}
