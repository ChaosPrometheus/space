package cn.thinkingdata.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import cn.thinkingdata.android.utils.PropertyUtils;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import cn.thinkingdata.android.utils.TDUtils;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(14)
class ThinkingDataActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ThinkingAnalytics.ThinkingDataActivityLifecycleCallbacks";
    private final String mMainProcessName;
    private final ThinkingAnalyticsSDK mThinkingDataInstance;
    private boolean resumeFromBackground = false;
    private Integer startedActivityCount = 0;
    private final Object mActivityLifecycleCallbacksLock = new Object();
    private boolean onStartReceived = false;

    public ThinkingDataActivityLifecycleCallbacks(ThinkingAnalyticsSDK thinkingAnalyticsSDK, String str) {
        this.mThinkingDataInstance = thinkingAnalyticsSDK;
        this.mMainProcessName = str;
    }

    private String getCurrentProcessName(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        try {
            int iMyPid = Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo != null && runningAppProcessInfo.pid == iMyPid) {
                        return runningAppProcessInfo.processName;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isMainProcess(Context context) {
        if (TextUtils.isEmpty(this.mMainProcessName)) {
            return true;
        }
        String currentProcessName = getCurrentProcessName(context.getApplicationContext());
        return TextUtils.isEmpty(currentProcessName) || this.mMainProcessName.equals(currentProcessName);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        synchronized (this.mActivityLifecycleCallbacksLock) {
            try {
                if (!this.onStartReceived && this.startedActivityCount.intValue() == 0) {
                    TDLog.i(TAG, "activity started before SDK is initialized");
                    this.onStartReceived = true;
                    this.startedActivityCount = 1;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        try {
            boolean z = !this.mThinkingDataInstance.isActivityAutoTrackAppViewScreenIgnored(activity.getClass());
            if (this.mThinkingDataInstance.isAutoTrackEnabled() && z && !this.mThinkingDataInstance.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_VIEW_SCREEN)) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(TDConstants.SCREEN_NAME, activity.getClass().getCanonicalName());
                    TDUtils.getScreenNameAndTitleFromActivity(jSONObject, activity);
                    if (activity instanceof ScreenAutoTracker) {
                        ScreenAutoTracker screenAutoTracker = (ScreenAutoTracker) activity;
                        String screenUrl = screenAutoTracker.getScreenUrl();
                        JSONObject trackProperties = screenAutoTracker.getTrackProperties();
                        if (trackProperties == null || !PropertyUtils.checkProperty(trackProperties)) {
                            TDLog.d(TAG, "invalid properties: " + trackProperties);
                        } else {
                            TDUtils.mergeJSONObject(trackProperties, jSONObject);
                        }
                        this.mThinkingDataInstance.trackViewScreenInternal(screenUrl, jSONObject);
                        return;
                    }
                    ThinkingDataAutoTrackAppViewScreenUrl thinkingDataAutoTrackAppViewScreenUrl = (ThinkingDataAutoTrackAppViewScreenUrl) activity.getClass().getAnnotation(ThinkingDataAutoTrackAppViewScreenUrl.class);
                    if (thinkingDataAutoTrackAppViewScreenUrl == null || !(TextUtils.isEmpty(thinkingDataAutoTrackAppViewScreenUrl.appId()) || this.mThinkingDataInstance.getToken().equals(thinkingDataAutoTrackAppViewScreenUrl.appId()))) {
                        this.mThinkingDataInstance.autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
                        return;
                    }
                    String strUrl = thinkingDataAutoTrackAppViewScreenUrl.url();
                    if (TextUtils.isEmpty(strUrl)) {
                        strUrl = activity.getClass().getCanonicalName();
                    }
                    this.mThinkingDataInstance.trackViewScreenInternal(strUrl, jSONObject);
                } catch (Exception e) {
                    TDLog.i(TAG, e);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        try {
            synchronized (this.mActivityLifecycleCallbacksLock) {
                try {
                    this.onStartReceived = true;
                    if (this.startedActivityCount.intValue() == 0) {
                        try {
                            this.mThinkingDataInstance.appBecomeActive();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isMainProcess(activity)) {
                            if (this.mThinkingDataInstance.isAutoTrackEnabled()) {
                                try {
                                    if (!this.mThinkingDataInstance.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_START)) {
                                        JSONObject jSONObject = new JSONObject();
                                        jSONObject.put(TDConstants.KEY_RESUME_FROM_BACKGROUND, this.resumeFromBackground);
                                        TDUtils.getScreenNameAndTitleFromActivity(jSONObject, activity);
                                        this.mThinkingDataInstance.autoTrack(TDConstants.APP_START_EVENT_NAME, jSONObject);
                                    }
                                    if (!this.mThinkingDataInstance.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END)) {
                                        this.mThinkingDataInstance.timeEvent(TDConstants.APP_END_EVENT_NAME);
                                    }
                                } catch (Exception e2) {
                                    TDLog.i(TAG, e2);
                                }
                            }
                            this.resumeFromBackground = true;
                        }
                    }
                    this.startedActivityCount = Integer.valueOf(this.startedActivityCount.intValue() + 1);
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0051 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        try {
            synchronized (this.mActivityLifecycleCallbacksLock) {
                Integer numValueOf = Integer.valueOf(this.startedActivityCount.intValue() - 1);
                this.startedActivityCount = numValueOf;
                if (numValueOf.intValue() == 0) {
                    try {
                        this.mThinkingDataInstance.appEnterBackground();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isMainProcess(activity) && this.mThinkingDataInstance.isAutoTrackEnabled()) {
                        try {
                            if (this.mThinkingDataInstance.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END)) {
                                this.mThinkingDataInstance.flush();
                            } else {
                                JSONObject jSONObject = new JSONObject();
                                TDUtils.getScreenNameAndTitleFromActivity(jSONObject, activity);
                                this.mThinkingDataInstance.autoTrack(TDConstants.APP_END_EVENT_NAME, jSONObject);
                                this.mThinkingDataInstance.flush();
                            }
                        } catch (Exception e2) {
                            TDLog.i(TAG, e2);
                        }
                    } else {
                        try {
                            this.mThinkingDataInstance.flush();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }
}
