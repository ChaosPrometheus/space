package cn.thinkingdata.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.core.content.ContextCompat;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import com.lbe.doubleagent.client.hook.C0176v;
import com.lbe.doubleagent.client.hook.x0;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class SystemInformation {
    public static final String KEY_LIB = "#lib";
    public static final String KEY_LIB_VERSION = "#lib_version";
    public static final String KEY_OS = "#os";
    private static final String TAG = "ThinkingAnalytics.SystemInformation";
    private static SystemInformation sInstance;
    private static final Object sInstanceLock = new Object();
    private boolean hasNotUpdated;
    private String mAppVersionName;
    private final Context mContext;
    private final Map<String, Object> mDeviceInfo;
    private final boolean mHasPermission;

    private SystemInformation(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mHasPermission = checkHasPermission(applicationContext, "android.permission.ACCESS_NETWORK_STATE");
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.mAppVersionName = packageInfo.versionName;
            long j = packageInfo.firstInstallTime;
            this.hasNotUpdated = j == packageInfo.lastUpdateTime;
            TDLog.d(TAG, String.valueOf(j));
            TDLog.d(TAG, String.valueOf(packageInfo.lastUpdateTime));
        } catch (Exception unused) {
            TDLog.d(TAG, "Exception occurred in getting app version");
        }
        this.mDeviceInfo = setupDeviceInfo(context);
    }

    private boolean checkHasPermission(Context context, String str) {
        Class<ContextCompat> cls;
        Class<ContextCompat> cls2 = ContextCompat.class;
        try {
            int i = ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS;
            cls = cls2;
        } catch (Exception unused) {
            cls = null;
        }
        if (cls == null) {
            try {
                int i2 = ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS;
            } catch (Exception unused2) {
                cls2 = cls;
            }
        } else {
            cls2 = cls;
        }
        if (cls2 == null) {
            return true;
        }
        try {
            if (((Integer) cls2.getMethod("checkSelfPermission", Context.class, String.class).invoke(null, context, str)).intValue() == 0) {
                return true;
            }
            StringBuilder sb = new StringBuilder("You can fix this by adding the following to your AndroidManifest.xml file:\n<uses-permission android:name=\"");
            sb.append(str);
            sb.append("\" />");
            TDLog.i(TAG, sb.toString());
            return false;
        } catch (Exception e) {
            TDLog.i(TAG, e.toString());
            return true;
        }
    }

    private String getAndroidID(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getCarrier(Context context) {
        HashMap<String, String> map = new HashMap<String, String>() { // from class: cn.thinkingdata.android.SystemInformation.1
            {
                put("46000", "中国移动");
                put("46002", "中国移动");
                put("46007", "中国移动");
                put("46008", "中国移动");
                put("46001", "中国联通");
                put("46006", "中国联通");
                put("46009", "中国联通");
                put("46003", "中国电信");
                put("46005", "中国电信");
                put("46011", "中国电信");
                put("46004", "中国卫通");
                put("46020", "中国铁通");
            }
        };
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(x0.h);
            String simOperator = telephonyManager.getSimOperator();
            if (!TextUtils.isEmpty(simOperator) && map.containsKey(simOperator)) {
                return map.get(simOperator);
            }
            String simOperatorName = telephonyManager.getSimOperatorName();
            if (TextUtils.isEmpty(simOperatorName)) {
                return null;
            }
            return simOperatorName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SystemInformation getInstance(Context context) {
        SystemInformation systemInformation;
        synchronized (sInstanceLock) {
            try {
                if (sInstance == null) {
                    sInstance = new SystemInformation(context);
                }
                systemInformation = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return systemInformation;
    }

    private Map<String, Object> setupDeviceInfo(Context context) {
        HashMap map = new HashMap();
        map.put(KEY_LIB, "Android");
        map.put(KEY_LIB_VERSION, "2.3.1");
        map.put(KEY_OS, "Android");
        String str = Build.VERSION.RELEASE;
        if (str == null) {
            str = "UNKNOWN";
        }
        map.put(TDConstants.KEY_OS_VERSION, str);
        String str2 = Build.MANUFACTURER;
        if (str2 == null) {
            str2 = "UNKNOWN";
        }
        map.put(TDConstants.KEY_MANUFACTURER, str2);
        String str3 = Build.MODEL;
        if (str3 == null) {
            str3 = "UNKNOWN";
        }
        map.put(TDConstants.KEY_DEVICE_MODEL, str3);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        map.put(TDConstants.KEY_SCREEN_HEIGHT, Integer.valueOf(displayMetrics.heightPixels));
        map.put(TDConstants.KEY_SCREEN_WIDTH, Integer.valueOf(displayMetrics.widthPixels));
        String carrier = getCarrier(context);
        if (TextUtils.isEmpty(carrier)) {
            map.put(TDConstants.KEY_CARRIER, "UNKNOWN");
        } else {
            map.put(TDConstants.KEY_CARRIER, carrier);
        }
        String androidID = getAndroidID(context);
        if (!TextUtils.isEmpty(androidID)) {
            map.put(TDConstants.KEY_DEVICE_ID, androidID);
        }
        return Collections.unmodifiableMap(map);
    }

    public String getAppVersionName() {
        return this.mAppVersionName;
    }

    public Map<String, Object> getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public String getNetworkType() {
        NetworkInfo networkInfo;
        if (!this.mHasPermission) {
            return "NULL";
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(C0176v.h);
        if (connectivityManager != null && (networkInfo = connectivityManager.getNetworkInfo(1)) != null && networkInfo.isConnectedOrConnecting()) {
            return "WIFI";
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(x0.h);
        if (telephonyManager != null) {
            int networkType = telephonyManager.getNetworkType();
            if (networkType == 20) {
                return "5G";
            }
            switch (networkType) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return "2G";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3G";
                case 13:
                    return "4G";
            }
        }
        return "NULL";
    }

    public boolean hasNotBeenUpdatedSinceInstall() {
        return this.hasNotUpdated;
    }

    public boolean isOnline() {
        if (!this.mHasPermission) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService(C0176v.h)).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
