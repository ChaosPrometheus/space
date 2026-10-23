package cn.thinkingdata.android;

import android.content.Context;
import android.content.SharedPreferences;
import cn.thinkingdata.android.persistence.StorageFlushBulkSize;
import cn.thinkingdata.android.persistence.StorageFlushInterval;
import cn.thinkingdata.android.utils.TDLog;
import com.lbe.parallel.model.JSONConstants;
import com.lbe.parallel.q;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Future;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class TDConfig {
    static final int DEFAULT_FLUSH_BULK_SIZE = 20;
    static final int DEFAULT_FLUSH_INTERVAL = 15000;
    private static final String PREFERENCE_NAME_PREFIX = "cn.thinkingdata.android.config";
    private static final String TAG = "ThinkingAnalytics.TDConfig";
    public static final String VERSION = "2.3.1";
    private volatile boolean mAllowedDebug;
    private final String mConfigUrl;
    final Context mContext;
    private final TDContextConfig mContextConfig;
    private final String mDebugUrl;
    private TimeZone mDefaultTimeZone;
    private final StorageFlushBulkSize mFlushBulkSize;
    private final StorageFlushInterval mFlushInterval;
    private SSLSocketFactory mSSLSocketFactory;
    private final String mServerUrl;
    final String mToken;
    private static final SharedPreferencesLoader sPrefsLoader = new SharedPreferencesLoader();
    private static final Map<Context, Map<String, TDConfig>> sInstances = new HashMap();
    private volatile ModeEnum mMode = ModeEnum.NORMAL;
    private int mNetworkType = 30;
    private volatile boolean mTrackOldData = true;

    /* JADX INFO: renamed from: cn.thinkingdata.android.TDConfig$2, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$cn$thinkingdata$android$ThinkingAnalyticsSDK$ThinkingdataNetworkType;

        static {
            int[] iArr = new int[ThinkingAnalyticsSDK.ThinkingdataNetworkType.values().length];
            $SwitchMap$cn$thinkingdata$android$ThinkingAnalyticsSDK$ThinkingdataNetworkType = iArr;
            try {
                iArr[ThinkingAnalyticsSDK.ThinkingdataNetworkType.NETWORKTYPE_DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$cn$thinkingdata$android$ThinkingAnalyticsSDK$ThinkingdataNetworkType[ThinkingAnalyticsSDK.ThinkingdataNetworkType.NETWORKTYPE_WIFI.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$cn$thinkingdata$android$ThinkingAnalyticsSDK$ThinkingdataNetworkType[ThinkingAnalyticsSDK.ThinkingdataNetworkType.NETWORKTYPE_ALL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public enum ModeEnum {
        NORMAL,
        DEBUG,
        DEBUG_ONLY
    }

    public final class NetworkType {
        public static final int TYPE_2G = 1;
        public static final int TYPE_3G = 2;
        public static final int TYPE_4G = 4;
        public static final int TYPE_5G = 16;
        public static final int TYPE_ALL = 255;
        public static final int TYPE_WIFI = 8;

        private NetworkType() {
        }
    }

    private TDConfig(Context context, String str, String str2) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        Future<SharedPreferences> futureLoadPreferences = sPrefsLoader.loadPreferences(applicationContext, q.u("cn.thinkingdata.android.config_", str));
        this.mContextConfig = TDContextConfig.getInstance(applicationContext);
        this.mToken = str;
        this.mServerUrl = q.j(str2, "/sync");
        this.mDebugUrl = q.j(str2, "/data_debug");
        this.mConfigUrl = q.k(str2, "/config?appid=", str);
        this.mFlushInterval = new StorageFlushInterval(futureLoadPreferences, DEFAULT_FLUSH_INTERVAL);
        this.mFlushBulkSize = new StorageFlushBulkSize(futureLoadPreferences, 20);
    }

    private int convertToNetworkType(String str) {
        if ("NULL".equals(str)) {
            return NetworkType.TYPE_ALL;
        }
        if ("WIFI".equals(str)) {
            return 8;
        }
        if ("2G".equals(str)) {
            return 1;
        }
        if ("3G".equals(str)) {
            return 2;
        }
        if ("4G".equals(str)) {
            return 4;
        }
        if ("5G".equals(str)) {
            return 16;
        }
        return NetworkType.TYPE_ALL;
    }

    public static TDConfig getInstance(Context context, String str) {
        try {
            return getInstance(context, str, "");
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    private void getRemoteConfig() {
        new Thread(new Runnable() { // from class: cn.thinkingdata.android.TDConfig.1
            /* JADX WARN: Code duplicated, block: B:109:? A[SYNTHETIC] */
            /* JADX WARN: Code duplicated, block: B:91:0x017a  */
            /* JADX WARN: Code duplicated, block: B:98:0x0170 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                HttpURLConnection httpURLConnection;
                InputStream inputStream = null;
                byte b = 0;
                InputStream inputStream2 = null;
                byte b2 = 0;
                byte b3 = 0;
                try {
                    try {
                        httpURLConnection = (HttpURLConnection) new URL(TDConfig.this.mConfigUrl).openConnection();
                        try {
                            try {
                                SSLSocketFactory sSLSocketFactory = TDConfig.this.getSSLSocketFactory();
                                if (sSLSocketFactory != null && (httpURLConnection instanceof HttpsURLConnection)) {
                                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLSocketFactory);
                                }
                                httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                                if (200 == httpURLConnection.getResponseCode()) {
                                    inputStream2 = httpURLConnection.getInputStream();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream2));
                                    StringBuffer stringBuffer = new StringBuffer();
                                    while (true) {
                                        String line = bufferedReader.readLine();
                                        if (line == null) {
                                            break;
                                        } else {
                                            stringBuffer.append(line);
                                        }
                                    }
                                    JSONObject jSONObject = new JSONObject(stringBuffer.toString());
                                    if (jSONObject.getString("code").equals("0")) {
                                        int iIntValue = ((Integer) TDConfig.this.mFlushInterval.get()).intValue();
                                        int iIntValue2 = ((Integer) TDConfig.this.mFlushBulkSize.get()).intValue();
                                        try {
                                            JSONObject jSONObject2 = jSONObject.getJSONObject(JSONConstants.JK_URL_DATA);
                                            iIntValue = jSONObject2.getInt("sync_interval") * 1000;
                                            iIntValue2 = jSONObject2.getInt("sync_batch_size");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Fetched remote config for (");
                                        String str = TDConfig.this.mToken;
                                        sb.append(str.substring(str.length() - 4));
                                        sb.append(") newUploadInterval is ");
                                        sb.append(iIntValue);
                                        sb.append(", newUploadSize is ");
                                        sb.append(iIntValue2);
                                        TDLog.d(TDConfig.TAG, sb.toString());
                                        if (((Integer) TDConfig.this.mFlushBulkSize.get()).intValue() != iIntValue2) {
                                            TDConfig.this.mFlushBulkSize.put(Integer.valueOf(iIntValue2));
                                        }
                                        if (((Integer) TDConfig.this.mFlushInterval.get()).intValue() != iIntValue) {
                                            TDConfig.this.mFlushInterval.put(Integer.valueOf(iIntValue));
                                        }
                                    }
                                    inputStream2.close();
                                    bufferedReader.close();
                                } else {
                                    TDLog.d(TDConfig.TAG, "getConfig failed, responseCode is " + httpURLConnection.getResponseCode());
                                }
                                if (inputStream2 != null) {
                                    try {
                                        inputStream2.close();
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            } catch (JSONException e3) {
                                e = e3;
                                e.printStackTrace();
                                if (0 != 0) {
                                    try {
                                        (b3 == true ? 1 : 0).close();
                                    } catch (IOException e4) {
                                        e4.printStackTrace();
                                    }
                                }
                                if (httpURLConnection == null) {
                                    return;
                                }
                            }
                        } catch (IOException e5) {
                            e = e5;
                            e.printStackTrace();
                            if (0 != 0) {
                                try {
                                    (b2 == true ? 1 : 0).close();
                                } catch (IOException e6) {
                                    e6.printStackTrace();
                                }
                            }
                            if (httpURLConnection == null) {
                                return;
                            }
                        }
                    } catch (IOException e7) {
                        e = e7;
                        httpURLConnection = null;
                    } catch (JSONException e8) {
                        e = e8;
                        httpURLConnection = null;
                    } catch (Throwable th) {
                        th = th;
                        if (0 != 0) {
                            try {
                                inputStream.close();
                            } catch (IOException e9) {
                                e9.printStackTrace();
                            }
                        }
                        if (0 != 0) {
                            throw th;
                        }
                        (b == true ? 1 : 0).disconnect();
                        throw th;
                    }
                    httpURLConnection.disconnect();
                } catch (Throwable th2) {
                    th = th2;
                    if (0 != 0) {
                        inputStream.close();
                    }
                    if (0 != 0) {
                        throw th;
                    }
                    (b == true ? 1 : 0).disconnect();
                    throw th;
                }
            }
        }).start();
    }

    public String getDebugUrl() {
        return this.mDebugUrl;
    }

    public synchronized TimeZone getDefaultTimeZone() {
        TimeZone timeZone;
        timeZone = this.mDefaultTimeZone;
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        return timeZone;
    }

    public int getFlushBulkSize() {
        return ((Integer) this.mFlushBulkSize.get()).intValue();
    }

    public int getFlushInterval() {
        return ((Integer) this.mFlushInterval.get()).intValue();
    }

    public String getMainProcessName() {
        return this.mContextConfig.getMainProcessName();
    }

    public ModeEnum getMode() {
        return this.mMode;
    }

    public int getModeInt() {
        return this.mMode.ordinal();
    }

    public synchronized SSLSocketFactory getSSLSocketFactory() {
        return this.mSSLSocketFactory;
    }

    public String getServerUrl() {
        return this.mServerUrl;
    }

    public boolean isDebug() {
        return ModeEnum.DEBUG.equals(this.mMode);
    }

    public boolean isDebugOnly() {
        return ModeEnum.DEBUG_ONLY.equals(this.mMode);
    }

    public boolean isNormal() {
        return ModeEnum.NORMAL.equals(this.mMode);
    }

    public synchronized boolean isShouldFlush(String str) {
        return (convertToNetworkType(str) & this.mNetworkType) != 0;
    }

    public void setAllowDebug() {
        this.mAllowedDebug = true;
    }

    public synchronized void setDefaultTimeZone(TimeZone timeZone) {
        this.mDefaultTimeZone = timeZone;
    }

    public void setMode(ModeEnum modeEnum) {
        this.mMode = modeEnum;
    }

    public void setModeInt(int i) {
        if (i < 0 || i > 2) {
            TDLog.d(TAG, "Invalid mode value");
        } else {
            this.mMode = ModeEnum.values()[i];
        }
    }

    public synchronized void setNetworkType(ThinkingAnalyticsSDK.ThinkingdataNetworkType thinkingdataNetworkType) {
        try {
            int i = AnonymousClass2.$SwitchMap$cn$thinkingdata$android$ThinkingAnalyticsSDK$ThinkingdataNetworkType[thinkingdataNetworkType.ordinal()];
            if (i == 1) {
                this.mNetworkType = 30;
            } else if (i == 2) {
                this.mNetworkType = 8;
            } else if (i == 3) {
                this.mNetworkType = 31;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        if (sSLSocketFactory != null) {
            this.mSSLSocketFactory = sSLSocketFactory;
            getRemoteConfig();
        }
    }

    public void setTrackOldData(boolean z) {
        this.mTrackOldData = z;
    }

    public boolean shouldThrowException() {
        return this.mAllowedDebug && (isDebug() || isDebugOnly());
    }

    public boolean trackOldData() {
        return this.mTrackOldData;
    }

    public static TDConfig getInstance(Context context, String str, String str2) {
        TDConfig tDConfig;
        String str3;
        Context applicationContext = context.getApplicationContext();
        Map<Context, Map<String, TDConfig>> map = sInstances;
        synchronized (map) {
            try {
                Map<String, TDConfig> map2 = map.get(applicationContext);
                if (map2 == null) {
                    map2 = new HashMap<>();
                    map.put(applicationContext, map2);
                }
                tDConfig = map2.get(str);
                if (tDConfig == null) {
                    try {
                        URL url = new URL(str2);
                        StringBuilder sb = new StringBuilder();
                        sb.append(url.getProtocol());
                        sb.append("://");
                        sb.append(url.getHost());
                        if (url.getPort() > 0) {
                            str3 = ":" + url.getPort();
                        } else {
                            str3 = "";
                        }
                        sb.append(str3);
                        TDConfig tDConfig2 = new TDConfig(applicationContext, str, sb.toString());
                        map2.put(str, tDConfig2);
                        tDConfig2.getRemoteConfig();
                        tDConfig = tDConfig2;
                    } catch (MalformedURLException e) {
                        TDLog.e(TAG, "Invalid server URL: " + str2);
                        throw new IllegalArgumentException(e);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return tDConfig;
    }
}
