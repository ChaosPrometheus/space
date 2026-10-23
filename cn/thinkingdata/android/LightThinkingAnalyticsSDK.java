package cn.thinkingdata.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import cn.thinkingdata.android.utils.PropertyUtils;
import cn.thinkingdata.android.utils.TDUtils;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class LightThinkingAnalyticsSDK extends ThinkingAnalyticsSDK {
    private String mAccountId;
    private String mDistinctId;
    private boolean mEnabled;
    private final JSONObject mSuperProperties;

    public LightThinkingAnalyticsSDK(TDConfig tDConfig) {
        super(tDConfig, true);
        this.mEnabled = true;
        this.mSuperProperties = new JSONObject();
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void clearSuperProperties() {
        if (hasDisabled()) {
            return;
        }
        synchronized (this.mSuperProperties) {
            try {
                Iterator<String> itKeys = this.mSuperProperties.keys();
                while (itKeys.hasNext()) {
                    itKeys.next();
                    itKeys.remove();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void enableAutoTrack(List<ThinkingAnalyticsSDK.AutoTrackEventType> list) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void enableTracking(boolean z) {
        this.mEnabled = z;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public String getDistinctId() {
        String str = this.mDistinctId;
        return str != null ? str : getRandomID();
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK
    public String getLoginId() {
        return this.mAccountId;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public JSONObject getSuperProperties() {
        return this.mSuperProperties;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK
    public boolean hasOptOut() {
        return false;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void identify(String str) {
        this.mDistinctId = str;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreAutoTrackActivities(List<Class<?>> list) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreAutoTrackActivity(Class<?> cls) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreView(View view) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreViewType(Class cls) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK
    public boolean isEnabled() {
        return this.mEnabled;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void login(String str) {
        if (hasDisabled()) {
            return;
        }
        this.mAccountId = str;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void logout() {
        if (hasDisabled()) {
            return;
        }
        this.mAccountId = null;
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optInTracking() {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optOutTracking() {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optOutTrackingAndDeleteUser() {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setJsBridge(WebView webView) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setJsBridgeForX5WebView(Object obj) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setNetworkType(ThinkingAnalyticsSDK.ThinkingdataNetworkType thinkingdataNetworkType) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setSuperProperties(JSONObject jSONObject) {
        if (hasDisabled() || jSONObject == null) {
            return;
        }
        try {
            if (PropertyUtils.checkProperty(jSONObject)) {
                synchronized (this.mSuperProperties) {
                    TDUtils.mergeJSONObject(jSONObject, this.mSuperProperties);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewID(Dialog dialog, String str) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewProperties(View view, JSONObject jSONObject) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackFragmentAppViewScreen() {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Activity activity) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void unsetSuperProperty(String str) {
        if (hasDisabled() || str == null) {
            return;
        }
        try {
            synchronized (this.mSuperProperties) {
                try {
                    this.mSuperProperties.remove(str);
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewID(View view, String str) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Fragment fragment) {
    }

    @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK, cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Object obj) {
    }
}
