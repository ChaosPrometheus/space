package cn.thinkingdata.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import cn.thinkingdata.android.utils.TDLog;
import com.lbe.doubleagent.service.DAActivityManager;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class TDContextConfig {
    private static final int DEFAULT_MIN_DB_LIMIT = 32;
    private static final int DEFAULT_QUIT_SAFELY_TIME_OUT = 2000;
    private static final int DEFAULT_RETENTION_DAYS = 15;
    private static final String KEY_ENABLE_LOG = "cn.thinkingdata.android.EnableTrackLogging";
    private static final String KEY_ENABLE_QUIT_SAFELY = "cn.thinkingdata.android.EnableQuitSafely";
    private static final String KEY_MAIN_PROCESS_NAME = "cn.thinkingdata.android.MainProcessName";
    private static final String KEY_MIN_DB_LIMIT = "cn.thinkingdata.android.MinimumDatabaseLimit";
    private static final String KEY_QUIT_SAFELY_TIMEOUT = "cn.thinkingdata.android.QuitSafelyTimeout";
    private static final String KEY_RETENTION_DAYS = "cn.thinkingdata.android.RetentionDays";
    private static final Map<Context, TDContextConfig> sInstanceMap = new HashMap();
    private final boolean mEnableQuitSafely;
    private final String mMainProcessName;
    private final int mMinimumDatabaseLimit;
    private final int mQuitSafelyTimeout;
    private final int mRetentionDays;

    private TDContextConfig(Context context) {
        Bundle bundle;
        try {
            bundle = context.getApplicationContext().getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), 128).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            bundle = null;
        }
        bundle = bundle == null ? new Bundle() : bundle;
        this.mMainProcessName = bundle.getString(KEY_MAIN_PROCESS_NAME);
        int i = bundle.getInt(KEY_RETENTION_DAYS, 15);
        this.mRetentionDays = i > 0 ? i : 15;
        this.mEnableQuitSafely = bundle.getBoolean(KEY_ENABLE_QUIT_SAFELY, true);
        int i2 = bundle.getInt(KEY_QUIT_SAFELY_TIMEOUT, 2000);
        this.mQuitSafelyTimeout = i2 > 0 ? i2 : 2000;
        int i3 = bundle.getInt(KEY_MIN_DB_LIMIT, 32);
        this.mMinimumDatabaseLimit = i3 > 0 ? i3 : 32;
        if (bundle.containsKey(KEY_ENABLE_LOG)) {
            TDLog.setEnableLog(bundle.getBoolean(KEY_ENABLE_LOG, false));
        }
    }

    public static TDContextConfig getInstance(Context context) {
        TDContextConfig tDContextConfig;
        Map<Context, TDContextConfig> map = sInstanceMap;
        synchronized (map) {
            try {
                tDContextConfig = map.get(context);
                if (tDContextConfig == null) {
                    tDContextConfig = new TDContextConfig(context);
                    map.put(context, tDContextConfig);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return tDContextConfig;
    }

    public long getDataExpiration() {
        return this.mRetentionDays * DAActivityManager.U1;
    }

    public String getMainProcessName() {
        return this.mMainProcessName;
    }

    public int getMinimumDatabaseLimit() {
        return this.mMinimumDatabaseLimit * 1048576;
    }

    public int getQuitSafelyTimeout() {
        return this.mQuitSafelyTimeout;
    }

    public boolean quitSafelyEnabled() {
        return this.mEnableQuitSafely;
    }
}
