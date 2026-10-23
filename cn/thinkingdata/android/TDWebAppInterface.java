package cn.thinkingdata.android;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import cn.thinkingdata.android.utils.TDLog;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class TDWebAppInterface {
    private static final String TAG = "ThinkingAnalytics.TDWebAppInterface";
    private final ThinkingAnalyticsSDK defaultInstance;

    public class TrackFlag {
        private boolean tracked;

        private TrackFlag() {
        }

        public boolean shouldTrack() {
            return !this.tracked;
        }

        public void tracked() {
            this.tracked = true;
        }
    }

    public TDWebAppInterface(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
        this.defaultInstance = thinkingAnalyticsSDK;
    }

    @JavascriptInterface
    public void thinkingdata_track(final String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        TDLog.d(TAG, str);
        try {
            final String string = new JSONObject(str).getString("#app_id");
            final TrackFlag trackFlag = new TrackFlag();
            ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.TDWebAppInterface.1
                @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
                public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                    if (thinkingAnalyticsSDK.getToken().equals(string)) {
                        trackFlag.tracked();
                        thinkingAnalyticsSDK.trackFromH5(str);
                    }
                }
            });
            if (trackFlag.shouldTrack()) {
                this.defaultInstance.trackFromH5(str);
            }
        } catch (JSONException e) {
            TDLog.w(TAG, "Unexpected exception occurred: " + e.toString());
        }
    }
}
