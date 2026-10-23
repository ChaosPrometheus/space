package cn.thinkingdata.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
interface IThinkingAnalyticsAPI {
    void clearSuperProperties();

    IThinkingAnalyticsAPI createLightInstance();

    void enableAutoTrack(List<ThinkingAnalyticsSDK.AutoTrackEventType> list);

    void enableTracking(boolean z);

    void flush();

    String getDeviceId();

    String getDistinctId();

    JSONObject getSuperProperties();

    void identify(String str);

    void ignoreAutoTrackActivities(List<Class<?>> list);

    void ignoreAutoTrackActivity(Class<?> cls);

    void ignoreView(View view);

    void ignoreViewType(Class cls);

    void login(String str);

    void logout();

    void optInTracking();

    void optOutTracking();

    void optOutTrackingAndDeleteUser();

    void setDynamicSuperPropertiesTracker(ThinkingAnalyticsSDK.DynamicSuperPropertiesTracker dynamicSuperPropertiesTracker);

    void setJsBridge(WebView webView);

    void setJsBridgeForX5WebView(Object obj);

    void setNetworkType(ThinkingAnalyticsSDK.ThinkingdataNetworkType thinkingdataNetworkType);

    void setSuperProperties(JSONObject jSONObject);

    void setViewID(Dialog dialog, String str);

    void setViewID(View view, String str);

    void setViewProperties(View view, JSONObject jSONObject);

    void timeEvent(String str);

    void track(String str);

    void track(String str, JSONObject jSONObject);

    @Deprecated
    void track(String str, JSONObject jSONObject, Date date);

    void track(String str, JSONObject jSONObject, Date date, TimeZone timeZone);

    void trackAppInstall();

    void trackFragmentAppViewScreen();

    void trackViewScreen(Activity activity);

    void trackViewScreen(Fragment fragment);

    void trackViewScreen(Object obj);

    void unsetSuperProperty(String str);

    void user_add(String str, Number number);

    void user_add(JSONObject jSONObject);

    void user_delete();

    void user_set(JSONObject jSONObject);

    void user_setOnce(JSONObject jSONObject);

    void user_unset(String... strArr);
}
