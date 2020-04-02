package com.brentvatne.react;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnTimedMetaDataAvailableListener;
import android.media.MediaPlayer.TrackInfo;
import android.media.TimedMetaData;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import com.RNFetchBlob.RNFetchBlobConst;
import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;
import com.yqritc.scalablevideoview.ScaleManager;
import com.yqritc.scalablevideoview.Size;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

@SuppressLint({"ViewConstructor"})
public class ReactVideoView extends ScalableVideoView implements OnPreparedListener, OnErrorListener, OnBufferingUpdateListener, OnSeekCompleteListener, OnCompletionListener, OnInfoListener, LifecycleEventListener, MediaPlayerControl {
    public static final String EVENT_PROP_CURRENT_TIME = "currentTime";
    public static final String EVENT_PROP_DURATION = "duration";
    public static final String EVENT_PROP_ERROR = "error";
    public static final String EVENT_PROP_EXTRA = "extra";
    public static final String EVENT_PROP_FAST_FORWARD = "canPlayFastForward";
    public static final String EVENT_PROP_HEIGHT = "height";
    public static final String EVENT_PROP_METADATA = "metadata";
    public static final String EVENT_PROP_METADATA_IDENTIFIER = "identifier";
    public static final String EVENT_PROP_METADATA_VALUE = "value";
    public static final String EVENT_PROP_NATURALSIZE = "naturalSize";
    public static final String EVENT_PROP_ORIENTATION = "orientation";
    public static final String EVENT_PROP_PLAYABLE_DURATION = "playableDuration";
    public static final String EVENT_PROP_REVERSE = "canPlayReverse";
    public static final String EVENT_PROP_SEEKABLE_DURATION = "seekableDuration";
    public static final String EVENT_PROP_SEEK_TIME = "seekTime";
    public static final String EVENT_PROP_SLOW_FORWARD = "canPlaySlowForward";
    public static final String EVENT_PROP_SLOW_REVERSE = "canPlaySlowReverse";
    public static final String EVENT_PROP_STEP_BACKWARD = "canStepBackward";
    public static final String EVENT_PROP_STEP_FORWARD = "canStepForward";
    public static final String EVENT_PROP_TARGET = "target";
    public static final String EVENT_PROP_WHAT = "what";
    public static final String EVENT_PROP_WIDTH = "width";
    /* access modifiers changed from: private */
    public boolean isCompleted = false;
    private float mActiveRate = 1.0f;
    /* access modifiers changed from: private */
    public boolean mBackgroundPaused = false;
    /* access modifiers changed from: private */
    public RCTEventEmitter mEventEmitter;
    private boolean mIsFullscreen = false;
    private int mMainVer = 0;
    /* access modifiers changed from: private */
    public boolean mMediaPlayerValid = false;
    private boolean mMuted = false;
    private int mPatchVer = 0;
    /* access modifiers changed from: private */
    public boolean mPaused = false;
    private boolean mPlayInBackground = false;
    /* access modifiers changed from: private */
    public Handler mProgressUpdateHandler = new Handler();
    /* access modifiers changed from: private */
    public float mProgressUpdateInterval = 250.0f;
    /* access modifiers changed from: private */
    public Runnable mProgressUpdateRunnable = null;
    private float mRate = 1.0f;
    private boolean mRepeat = false;
    private ReadableMap mRequestHeaders = null;
    private ScalableType mResizeMode = ScalableType.LEFT_TOP;
    private long mSeekTime = 0;
    private boolean mSrcIsAsset = false;
    private boolean mSrcIsNetwork = false;
    private String mSrcType = "mp4";
    private String mSrcUriString = null;
    private float mStereoPan = 0.0f;
    private ThemedReactContext mThemedReactContext;
    private boolean mUseNativeControls = false;
    /* access modifiers changed from: private */
    public int mVideoBufferedDuration = 0;
    /* access modifiers changed from: private */
    public int mVideoDuration = 0;
    private float mVolume = 1.0f;
    /* access modifiers changed from: private */
    public MediaController mediaController;
    private Handler videoControlHandler = new Handler();

    public enum Events {
        EVENT_LOAD_START("onVideoLoadStart"),
        EVENT_LOAD("onVideoLoad"),
        EVENT_ERROR("onVideoError"),
        EVENT_PROGRESS("onVideoProgress"),
        EVENT_TIMED_METADATA("onTimedMetadata"),
        EVENT_SEEK("onVideoSeek"),
        EVENT_END("onVideoEnd"),
        EVENT_STALLED("onPlaybackStalled"),
        EVENT_RESUME("onPlaybackResume"),
        EVENT_READY_FOR_DISPLAY("onReadyForDisplay"),
        EVENT_FULLSCREEN_WILL_PRESENT("onVideoFullscreenPlayerWillPresent"),
        EVENT_FULLSCREEN_DID_PRESENT("onVideoFullscreenPlayerDidPresent"),
        EVENT_FULLSCREEN_WILL_DISMISS("onVideoFullscreenPlayerWillDismiss"),
        EVENT_FULLSCREEN_DID_DISMISS("onVideoFullscreenPlayerDidDismiss");
        
        private final String mName;

        private Events(String str) {
            this.mName = str;
        }

        public String toString() {
            return this.mName;
        }
    }

    @TargetApi(23)
    public class TimedMetaDataAvailableListener implements OnTimedMetaDataAvailableListener {
        public TimedMetaDataAvailableListener() {
        }

        public void onTimedMetaDataAvailable(MediaPlayer mediaPlayer, TimedMetaData timedMetaData) {
            WritableMap createMap = Arguments.createMap();
            try {
                String str = new String(timedMetaData.getMetaData(), "UTF-8");
                WritableMap createMap2 = Arguments.createMap();
                createMap2.putString(ReactVideoView.EVENT_PROP_METADATA_VALUE, str.substring(str.lastIndexOf("\u0003") + 1));
                createMap2.putString(ReactVideoView.EVENT_PROP_METADATA_IDENTIFIER, "id3/TDEN");
                WritableNativeArray writableNativeArray = new WritableNativeArray();
                writableNativeArray.pushMap(createMap2);
                createMap.putArray(ReactVideoView.EVENT_PROP_METADATA, writableNativeArray);
                createMap.putDouble("target", (double) ReactVideoView.this.getId());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ReactVideoView.this.mEventEmitter.receiveEvent(ReactVideoView.this.getId(), Events.EVENT_TIMED_METADATA.toString(), createMap);
        }
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public void onHostDestroy() {
    }

    public ReactVideoView(ThemedReactContext themedReactContext) {
        super(themedReactContext);
        this.mThemedReactContext = themedReactContext;
        this.mEventEmitter = (RCTEventEmitter) themedReactContext.getJSModule(RCTEventEmitter.class);
        themedReactContext.addLifecycleEventListener(this);
        initializeMediaPlayerIfNeeded();
        setSurfaceTextureListener(this);
        this.mProgressUpdateRunnable = new Runnable() {
            public void run() {
                if (ReactVideoView.this.mMediaPlayerValid && !ReactVideoView.this.isCompleted && !ReactVideoView.this.mPaused && !ReactVideoView.this.mBackgroundPaused) {
                    WritableMap createMap = Arguments.createMap();
                    double currentPosition = (double) ReactVideoView.this.mMediaPlayer.getCurrentPosition();
                    Double.isNaN(currentPosition);
                    createMap.putDouble(ReactVideoView.EVENT_PROP_CURRENT_TIME, currentPosition / 1000.0d);
                    double access$500 = (double) ReactVideoView.this.mVideoBufferedDuration;
                    Double.isNaN(access$500);
                    createMap.putDouble(ReactVideoView.EVENT_PROP_PLAYABLE_DURATION, access$500 / 1000.0d);
                    double access$600 = (double) ReactVideoView.this.mVideoDuration;
                    Double.isNaN(access$600);
                    createMap.putDouble(ReactVideoView.EVENT_PROP_SEEKABLE_DURATION, access$600 / 1000.0d);
                    ReactVideoView.this.mEventEmitter.receiveEvent(ReactVideoView.this.getId(), Events.EVENT_PROGRESS.toString(), createMap);
                    ReactVideoView.this.mProgressUpdateHandler.postDelayed(ReactVideoView.this.mProgressUpdateRunnable, (long) Math.round(ReactVideoView.this.mProgressUpdateInterval));
                }
            }
        };
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mUseNativeControls) {
            initializeMediaControllerIfNeeded();
            this.mediaController.show();
        }
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z && this.mMediaPlayerValid) {
            int videoWidth = getVideoWidth();
            int videoHeight = getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                Matrix scaleMatrix = new ScaleManager(new Size(getWidth(), getHeight()), new Size(videoWidth, videoHeight)).getScaleMatrix(this.mScalableType);
                if (scaleMatrix != null) {
                    setTransform(scaleMatrix);
                }
            }
        }
    }

    private void initializeMediaPlayerIfNeeded() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayerValid = false;
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this);
            this.mMediaPlayer.setOnErrorListener(this);
            this.mMediaPlayer.setOnPreparedListener(this);
            this.mMediaPlayer.setOnBufferingUpdateListener(this);
            this.mMediaPlayer.setOnSeekCompleteListener(this);
            this.mMediaPlayer.setOnCompletionListener(this);
            this.mMediaPlayer.setOnInfoListener(this);
            if (VERSION.SDK_INT >= 23) {
                this.mMediaPlayer.setOnTimedMetaDataAvailableListener(new TimedMetaDataAvailableListener());
            }
        }
    }

    private void initializeMediaControllerIfNeeded() {
        if (this.mediaController == null) {
            this.mediaController = new MediaController(getContext());
        }
    }

    public void cleanupMediaPlayerResources() {
        MediaController mediaController2 = this.mediaController;
        if (mediaController2 != null) {
            mediaController2.hide();
        }
        if (this.mMediaPlayer != null) {
            if (VERSION.SDK_INT >= 23) {
                this.mMediaPlayer.setOnTimedMetaDataAvailableListener(null);
            }
            this.mMediaPlayerValid = false;
            release();
        }
        if (this.mIsFullscreen) {
            setFullscreen(false);
        }
        ThemedReactContext themedReactContext = this.mThemedReactContext;
        if (themedReactContext != null) {
            themedReactContext.removeLifecycleEventListener(this);
            this.mThemedReactContext = null;
        }
    }

    public void setSrc(String str, String str2, boolean z, boolean z2, ReadableMap readableMap) {
        setSrc(str, str2, z, z2, readableMap, 0, 0);
    }

    public void setSrc(String str, String str2, boolean z, boolean z2, ReadableMap readableMap, int i, int i2) {
        String str3 = ".mp4";
        this.mSrcUriString = str;
        this.mSrcType = str2;
        this.mSrcIsNetwork = z;
        this.mSrcIsAsset = z2;
        this.mRequestHeaders = readableMap;
        this.mMainVer = i;
        this.mPatchVer = i2;
        this.mMediaPlayerValid = false;
        this.mVideoDuration = 0;
        this.mVideoBufferedDuration = 0;
        initializeMediaPlayerIfNeeded();
        this.mMediaPlayer.reset();
        if (z) {
            try {
                CookieManager instance = CookieManager.getInstance();
                Uri parse = Uri.parse(str);
                String cookie = instance.getCookie(parse.buildUpon().build().toString());
                HashMap hashMap = new HashMap();
                if (cookie != null) {
                    hashMap.put("Cookie", cookie);
                }
                if (this.mRequestHeaders != null) {
                    hashMap.putAll(toStringMap(this.mRequestHeaders));
                }
                setDataSource((Context) this.mThemedReactContext, parse, (Map<String, String>) hashMap);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else if (!z2) {
            AssetFileDescriptor assetFileDescriptor = null;
            if (this.mMainVer > 0) {
                try {
                    ZipResourceFile aPKExpansionZipFile = APKExpansionSupport.getAPKExpansionZipFile(this.mThemedReactContext, this.mMainVer, this.mPatchVer);
                    StringBuilder sb = new StringBuilder();
                    sb.append(str.replace(str3, ""));
                    sb.append(str3);
                    assetFileDescriptor = aPKExpansionZipFile.getAssetFileDescriptor(sb.toString());
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (NullPointerException e3) {
                    e3.printStackTrace();
                }
            }
            if (assetFileDescriptor == null) {
                int identifier = this.mThemedReactContext.getResources().getIdentifier(str, "drawable", this.mThemedReactContext.getPackageName());
                if (identifier == 0) {
                    identifier = this.mThemedReactContext.getResources().getIdentifier(str, "raw", this.mThemedReactContext.getPackageName());
                }
                setRawData(identifier);
            } else {
                setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            }
        } else if (str.startsWith(RNFetchBlobConst.FILE_PREFIX_CONTENT)) {
            setDataSource(this.mThemedReactContext, Uri.parse(str));
        } else {
            setDataSource(str);
        }
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        createMap2.merge(this.mRequestHeaders);
        createMap.putString("uri", str);
        createMap.putString(ReactVideoViewManager.PROP_SRC_TYPE, str2);
        createMap.putMap(ReactVideoViewManager.PROP_SRC_HEADERS, createMap2);
        createMap.putBoolean(ReactVideoViewManager.PROP_SRC_IS_NETWORK, z);
        int i3 = this.mMainVer;
        if (i3 > 0) {
            createMap.putInt(ReactVideoViewManager.PROP_SRC_MAINVER, i3);
            int i4 = this.mPatchVer;
            if (i4 > 0) {
                createMap.putInt(ReactVideoViewManager.PROP_SRC_PATCHVER, i4);
            }
        }
        WritableMap createMap3 = Arguments.createMap();
        createMap3.putMap(ReactVideoViewManager.PROP_SRC, createMap);
        this.mEventEmitter.receiveEvent(getId(), Events.EVENT_LOAD_START.toString(), createMap3);
        this.isCompleted = false;
        try {
            prepareAsync(this);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    public void setResizeModeModifier(ScalableType scalableType) {
        this.mResizeMode = scalableType;
        if (this.mMediaPlayerValid) {
            setScalableType(scalableType);
            invalidate();
        }
    }

    public void setRepeatModifier(boolean z) {
        this.mRepeat = z;
        if (this.mMediaPlayerValid) {
            setLooping(z);
        }
    }

    public void setPausedModifier(boolean z) {
        this.mPaused = z;
        if (this.mMediaPlayerValid) {
            if (this.mPaused) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                }
            } else if (!this.mMediaPlayer.isPlaying()) {
                start();
                float f = this.mRate;
                if (f != this.mActiveRate) {
                    setRateModifier(f);
                }
                this.mProgressUpdateHandler.post(this.mProgressUpdateRunnable);
            }
            setKeepScreenOn(!this.mPaused);
        }
    }

    private float calulateRelativeVolume() {
        return new BigDecimal((double) (this.mVolume * (1.0f - Math.abs(this.mStereoPan)))).setScale(1, 4).floatValue();
    }

    public void setMutedModifier(boolean z) {
        this.mMuted = z;
        if (this.mMediaPlayerValid) {
            if (this.mMuted) {
                setVolume(0.0f, 0.0f);
            } else {
                float f = this.mStereoPan;
                if (f < 0.0f) {
                    setVolume(this.mVolume, calulateRelativeVolume());
                } else if (f > 0.0f) {
                    setVolume(calulateRelativeVolume(), this.mVolume);
                } else {
                    float f2 = this.mVolume;
                    setVolume(f2, f2);
                }
            }
        }
    }

    public void setVolumeModifier(float f) {
        this.mVolume = f;
        setMutedModifier(this.mMuted);
    }

    public void setStereoPan(float f) {
        this.mStereoPan = f;
        setMutedModifier(this.mMuted);
    }

    public void setProgressUpdateInterval(float f) {
        this.mProgressUpdateInterval = f;
    }

    public void setRateModifier(float f) {
        this.mRate = f;
        if (this.mMediaPlayerValid) {
            int i = VERSION.SDK_INT;
            String str = ReactVideoViewManager.REACT_CLASS;
            if (i < 23) {
                Log.e(str, "Setting playback rate is not yet supported on Android versions below 6.0");
            } else if (!this.mPaused) {
                try {
                    this.mMediaPlayer.setPlaybackParams(this.mMediaPlayer.getPlaybackParams().setSpeed(f));
                    this.mActiveRate = f;
                } catch (Exception unused) {
                    Log.e(str, "Unable to set rate, unsupported on this device");
                }
            }
        }
    }

    public void setFullscreen(boolean z) {
        if (z != this.mIsFullscreen) {
            this.mIsFullscreen = z;
            Activity currentActivity = this.mThemedReactContext.getCurrentActivity();
            if (currentActivity != null) {
                View decorView = currentActivity.getWindow().getDecorView();
                if (this.mIsFullscreen) {
                    int i = VERSION.SDK_INT >= 19 ? 4102 : 6;
                    this.mEventEmitter.receiveEvent(getId(), Events.EVENT_FULLSCREEN_WILL_PRESENT.toString(), null);
                    decorView.setSystemUiVisibility(i);
                    this.mEventEmitter.receiveEvent(getId(), Events.EVENT_FULLSCREEN_DID_PRESENT.toString(), null);
                } else {
                    this.mEventEmitter.receiveEvent(getId(), Events.EVENT_FULLSCREEN_WILL_DISMISS.toString(), null);
                    decorView.setSystemUiVisibility(0);
                    this.mEventEmitter.receiveEvent(getId(), Events.EVENT_FULLSCREEN_DID_DISMISS.toString(), null);
                }
            }
        }
    }

    public void applyModifiers() {
        setResizeModeModifier(this.mResizeMode);
        setRepeatModifier(this.mRepeat);
        setPausedModifier(this.mPaused);
        setMutedModifier(this.mMuted);
        setProgressUpdateInterval(this.mProgressUpdateInterval);
        setRateModifier(this.mRate);
    }

    public void setPlayInBackground(boolean z) {
        this.mPlayInBackground = z;
    }

    public void setControls(boolean z) {
        this.mUseNativeControls = z;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mMediaPlayerValid = true;
        this.mVideoDuration = mediaPlayer.getDuration();
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("width", mediaPlayer.getVideoWidth());
        createMap.putInt("height", mediaPlayer.getVideoHeight());
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        String str = EVENT_PROP_ORIENTATION;
        if (videoWidth > videoHeight) {
            createMap.putString(str, "landscape");
        } else {
            createMap.putString(str, "portrait");
        }
        WritableMap createMap2 = Arguments.createMap();
        double d = (double) this.mVideoDuration;
        Double.isNaN(d);
        createMap2.putDouble(EVENT_PROP_DURATION, d / 1000.0d);
        double currentPosition = (double) mediaPlayer.getCurrentPosition();
        Double.isNaN(currentPosition);
        createMap2.putDouble(EVENT_PROP_CURRENT_TIME, currentPosition / 1000.0d);
        createMap2.putMap(EVENT_PROP_NATURALSIZE, createMap);
        String str2 = EVENT_PROP_FAST_FORWARD;
        createMap2.putBoolean(str2, true);
        createMap2.putBoolean(EVENT_PROP_SLOW_FORWARD, true);
        createMap2.putBoolean(EVENT_PROP_SLOW_REVERSE, true);
        createMap2.putBoolean(EVENT_PROP_REVERSE, true);
        createMap2.putBoolean(str2, true);
        createMap2.putBoolean(EVENT_PROP_STEP_BACKWARD, true);
        createMap2.putBoolean(EVENT_PROP_STEP_FORWARD, true);
        this.mEventEmitter.receiveEvent(getId(), Events.EVENT_LOAD.toString(), createMap2);
        applyModifiers();
        if (this.mUseNativeControls) {
            initializeMediaControllerIfNeeded();
            this.mediaController.setMediaPlayer(this);
            this.mediaController.setAnchorView(this);
            this.videoControlHandler.post(new Runnable() {
                public void run() {
                    ReactVideoView.this.mediaController.setEnabled(true);
                    ReactVideoView.this.mediaController.show();
                }
            });
        }
        selectTimedMetadataTrack(mediaPlayer);
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt(EVENT_PROP_WHAT, i);
        createMap.putInt(EVENT_PROP_EXTRA, i2);
        WritableMap createMap2 = Arguments.createMap();
        createMap2.putMap(EVENT_PROP_ERROR, createMap);
        this.mEventEmitter.receiveEvent(getId(), Events.EVENT_ERROR.toString(), createMap2);
        return true;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 3) {
            this.mEventEmitter.receiveEvent(getId(), Events.EVENT_READY_FOR_DISPLAY.toString(), Arguments.createMap());
        } else if (i == 701) {
            this.mEventEmitter.receiveEvent(getId(), Events.EVENT_STALLED.toString(), Arguments.createMap());
        } else if (i == 702) {
            this.mEventEmitter.receiveEvent(getId(), Events.EVENT_RESUME.toString(), Arguments.createMap());
        }
        return false;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        selectTimedMetadataTrack(mediaPlayer);
        double d = (double) (this.mVideoDuration * i);
        Double.isNaN(d);
        this.mVideoBufferedDuration = (int) Math.round(d / 100.0d);
    }

    public void onSeekComplete(MediaPlayer mediaPlayer) {
        WritableMap createMap = Arguments.createMap();
        double currentPosition = (double) getCurrentPosition();
        Double.isNaN(currentPosition);
        createMap.putDouble(EVENT_PROP_CURRENT_TIME, currentPosition / 1000.0d);
        double d = (double) this.mSeekTime;
        Double.isNaN(d);
        createMap.putDouble(EVENT_PROP_SEEK_TIME, d / 1000.0d);
        this.mEventEmitter.receiveEvent(getId(), Events.EVENT_SEEK.toString(), createMap);
        this.mSeekTime = 0;
    }

    public void seekTo(int i) {
        if (this.mMediaPlayerValid) {
            this.mSeekTime = (long) i;
            super.seekTo(i);
            if (this.isCompleted) {
                int i2 = this.mVideoDuration;
                if (i2 != 0 && i < i2) {
                    this.isCompleted = false;
                }
            }
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        this.isCompleted = true;
        this.mEventEmitter.receiveEvent(getId(), Events.EVENT_END.toString(), null);
        if (!this.mRepeat) {
            setKeepScreenOn(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mMediaPlayerValid = false;
        super.onDetachedFromWindow();
        setKeepScreenOn(false);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        int i = this.mMainVer;
        if (i > 0) {
            setSrc(this.mSrcUriString, this.mSrcType, this.mSrcIsNetwork, this.mSrcIsAsset, this.mRequestHeaders, i, this.mPatchVer);
        } else {
            setSrc(this.mSrcUriString, this.mSrcType, this.mSrcIsNetwork, this.mSrcIsAsset, this.mRequestHeaders);
        }
        setKeepScreenOn(true);
    }

    public void onHostPause() {
        if (this.mMediaPlayerValid && !this.mPaused && !this.mPlayInBackground) {
            this.mBackgroundPaused = true;
            this.mMediaPlayer.pause();
        }
    }

    public void onHostResume() {
        this.mBackgroundPaused = false;
        if (this.mMediaPlayerValid && !this.mPlayInBackground && !this.mPaused) {
            new Handler().post(new Runnable() {
                public void run() {
                    ReactVideoView.this.setPausedModifier(false);
                }
            });
        }
    }

    public static Map<String, String> toStringMap(@Nullable ReadableMap readableMap) {
        HashMap hashMap = new HashMap();
        if (readableMap == null) {
            return hashMap;
        }
        ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String nextKey = keySetIterator.nextKey();
            hashMap.put(nextKey, readableMap.getString(nextKey));
        }
        return hashMap;
    }

    private void selectTimedMetadataTrack(MediaPlayer mediaPlayer) {
        if (VERSION.SDK_INT >= 23) {
            try {
                TrackInfo[] trackInfo = mediaPlayer.getTrackInfo();
                int i = 0;
                while (true) {
                    if (i >= trackInfo.length) {
                        break;
                    } else if (trackInfo[i].getTrackType() == 3) {
                        mediaPlayer.selectTrack(i);
                        break;
                    } else {
                        i++;
                    }
                }
            } catch (Exception unused) {
            }
        }
    }
}
