package cn.thinkingdata.android;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import cn.thinkingdata.android.persistence.StorageEnableFlag;
import cn.thinkingdata.android.persistence.StorageIdentifyId;
import cn.thinkingdata.android.persistence.StorageLoginID;
import cn.thinkingdata.android.persistence.StorageOptOutFlag;
import cn.thinkingdata.android.persistence.StorageRandomID;
import cn.thinkingdata.android.persistence.StorageSuperProperties;
import cn.thinkingdata.android.utils.PropertyUtils;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import cn.thinkingdata.android.utils.TDUtils;
import com.lbe.parallel.model.JSONConstants;
import com.lbe.parallel.q;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ThinkingAnalyticsSDK implements IThinkingAnalyticsAPI {
    private static final String PREFERENCE_NAME = "com.thinkingdata.analyse";
    private static final String TAG = "ThinkingAnalyticsSDK";
    private static StorageLoginID sOldLoginId;
    private static StorageRandomID sRandomID;
    private static Future<SharedPreferences> sStoredSharedPrefs;
    private boolean mAutoTrack;
    private List<AutoTrackEventType> mAutoTrackEventTypeList;
    private List<Integer> mAutoTrackIgnoredActivities;
    private TDConfig mConfig;
    private DynamicSuperPropertiesTracker mDynamicSuperPropertiesTracker;
    private final StorageEnableFlag mEnableFlag;
    private final boolean mEnableTrackOldData;
    private final StorageIdentifyId mIdentifyId;
    private String mLastScreenUrl;
    private final StorageLoginID mLoginId;
    private final DataHandle mMessages;
    private final StorageOptOutFlag mOptOutFlag;
    private final StorageSuperProperties mSuperProperties;
    private SystemInformation mSystemInformation;
    private boolean mTrackCrash;
    private boolean mTrackFragmentAppViewScreen;
    private final Map<String, EventTimer> mTrackTimer;
    private static final SharedPreferencesLoader sPrefsLoader = new SharedPreferencesLoader();
    private static final Object sOldLoginIdLock = new Object();
    private static final Object sRandomIDLock = new Object();
    private static final Map<Context, Map<String, ThinkingAnalyticsSDK>> sInstanceMap = new HashMap();
    private static final Map<Context, List<String>> sAppFirstInstallationMap = new HashMap();
    private List<Class> mIgnoredViewTypeList = new ArrayList();
    private final boolean SAVE_DATA_TO_DATABASE = true;

    public enum AutoTrackEventType {
        APP_START(TDConstants.APP_START_EVENT_NAME),
        APP_END(TDConstants.APP_END_EVENT_NAME),
        APP_CLICK(TDConstants.APP_CLICK_EVENT_NAME),
        APP_VIEW_SCREEN(TDConstants.APP_VIEW_EVENT_NAME),
        APP_CRASH(TDConstants.APP_CRASH_EVENT_NAME),
        APP_INSTALL(TDConstants.APP_INSTALL_EVENT_NAME);

        private final String eventName;

        AutoTrackEventType(String str) {
            this.eventName = str;
        }

        public static AutoTrackEventType autoTrackEventTypeFromEventName(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            str.getClass();
            switch (str) {
                case "ta_app_install":
                    return APP_INSTALL;
                case "ta_app_click":
                    return APP_CLICK;
                case "ta_app_crash":
                    return APP_CRASH;
                case "ta_app_start":
                    return APP_START;
                case "ta_app_end":
                    return APP_END;
                case "ta_app_view":
                    return APP_VIEW_SCREEN;
                default:
                    return null;
            }
        }

        public String getEventName() {
            return this.eventName;
        }
    }

    public interface DynamicSuperPropertiesTracker {
        JSONObject getDynamicSuperProperties();
    }

    public class EventDescription extends DataDescription {
        public EventDescription(String str, JSONObject jSONObject) {
            super();
            this.type = TDConstants.TYPE_TRACK;
            this.eventName = str;
            this.properties = jSONObject;
        }
    }

    public interface InstanceProcessor {
        void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK);
    }

    public enum TIME_VALUE_TYPE {
        NONE,
        TIME_ONLY,
        ALL
    }

    public enum ThinkingdataNetworkType {
        NETWORKTYPE_DEFAULT,
        NETWORKTYPE_WIFI,
        NETWORKTYPE_ALL
    }

    public ThinkingAnalyticsSDK(TDConfig tDConfig, boolean... zArr) {
        this.mConfig = tDConfig;
        if (zArr.length > 0 && zArr[0]) {
            this.mLoginId = null;
            this.mIdentifyId = null;
            this.mSuperProperties = null;
            this.mOptOutFlag = null;
            this.mEnableFlag = null;
            this.mEnableTrackOldData = false;
            this.mTrackTimer = new HashMap();
            this.mMessages = getDataHandleInstance(tDConfig.mContext);
            this.mSystemInformation = SystemInformation.getInstance(tDConfig.mContext);
            return;
        }
        if (sStoredSharedPrefs == null) {
            Future<SharedPreferences> futureLoadPreferences = sPrefsLoader.loadPreferences(tDConfig.mContext, PREFERENCE_NAME);
            sStoredSharedPrefs = futureLoadPreferences;
            sRandomID = new StorageRandomID(futureLoadPreferences);
            sOldLoginId = new StorageLoginID(sStoredSharedPrefs);
        }
        if (!tDConfig.trackOldData() || isOldDataTracked()) {
            this.mEnableTrackOldData = false;
        } else {
            this.mEnableTrackOldData = true;
        }
        Future<SharedPreferences> futureLoadPreferences2 = sPrefsLoader.loadPreferences(tDConfig.mContext, "com.thinkingdata.analyse_" + tDConfig.mToken);
        this.mLoginId = new StorageLoginID(futureLoadPreferences2);
        this.mIdentifyId = new StorageIdentifyId(futureLoadPreferences2);
        this.mSuperProperties = new StorageSuperProperties(futureLoadPreferences2);
        this.mOptOutFlag = new StorageOptOutFlag(futureLoadPreferences2);
        this.mEnableFlag = new StorageEnableFlag(futureLoadPreferences2);
        this.mSystemInformation = SystemInformation.getInstance(tDConfig.mContext);
        DataHandle dataHandleInstance = getDataHandleInstance(tDConfig.mContext);
        this.mMessages = dataHandleInstance;
        if (this.mEnableTrackOldData) {
            dataHandleInstance.flushOldData(tDConfig.mToken);
        }
        this.mTrackTimer = new HashMap();
        this.mAutoTrackIgnoredActivities = new ArrayList();
        this.mAutoTrackEventTypeList = new ArrayList();
        ((Application) tDConfig.mContext.getApplicationContext()).registerActivityLifecycleCallbacks(new ThinkingDataActivityLifecycleCallbacks(this, this.mConfig.getMainProcessName()));
        if (!tDConfig.isNormal()) {
            enableTrackLog(true);
        }
        TDLog.i(TAG, "Thank you very much for using Thinking Data. We will do our best to provide you with the best service.");
        String strName = tDConfig.getMode().name();
        String str = tDConfig.mToken;
        String strSubstring = str.substring(str.length() - 4);
        String deviceId = getDeviceId();
        StringBuilder sbN = q.n("Thinking Data SDK version: 2.3.1, Initial mode: ", strName, ", APP ID ends with: ", strSubstring, ", DeviceId: ");
        sbN.append(deviceId);
        TDLog.i(TAG, sbN.toString());
    }

    public static void addInstance(ThinkingAnalyticsSDK thinkingAnalyticsSDK, Context context, String str) {
        Map<Context, Map<String, ThinkingAnalyticsSDK>> map = sInstanceMap;
        synchronized (map) {
            try {
                Map<String, ThinkingAnalyticsSDK> map2 = map.get(context);
                if (map2 == null) {
                    map2 = new HashMap<>();
                    map.put(context, map2);
                }
                map2.put(str, thinkingAnalyticsSDK);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void allInstances(InstanceProcessor instanceProcessor) {
        Map<Context, Map<String, ThinkingAnalyticsSDK>> map = sInstanceMap;
        synchronized (map) {
            try {
                Iterator<Map<String, ThinkingAnalyticsSDK>> it = map.values().iterator();
                while (it.hasNext()) {
                    Iterator<ThinkingAnalyticsSDK> it2 = it.next().values().iterator();
                    while (it2.hasNext()) {
                        instanceProcessor.process(it2.next());
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void enableTrackLog(boolean z) {
        TDLog.setEnableLog(z);
    }

    private String getIdentifyID() {
        String str;
        synchronized (this.mIdentifyId) {
            str = (String) this.mIdentifyId.get();
        }
        return str;
    }

    private static boolean isOldDataTracked() {
        Map<Context, Map<String, ThinkingAnalyticsSDK>> map = sInstanceMap;
        synchronized (map) {
            try {
                if (map.size() > 0) {
                    Iterator<Map<String, ThinkingAnalyticsSDK>> it = map.values().iterator();
                    while (it.hasNext()) {
                        Iterator<ThinkingAnalyticsSDK> it2 = it.next().values().iterator();
                        while (it2.hasNext()) {
                            if (it2.next().mEnableTrackOldData) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static ThinkingAnalyticsSDK sharedInstance(Context context, String str) {
        return sharedInstance(context, str, null, false);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void trackInternal(DataDescription dataDescription) {
        String str;
        double timezoneOffset;
        EventTimer eventTimer;
        JSONObject dynamicSuperProperties;
        if (TextUtils.isEmpty(dataDescription.type)) {
            TDLog.d(TAG, "EventType could not be empty");
            return;
        }
        if (dataDescription.type.equals(TDConstants.TYPE_TRACK) && PropertyUtils.isInvalidName(dataDescription.eventName)) {
            TDLog.w(TAG, "Event name[" + dataDescription.eventName + "] is invalid. Event name must be string that starts with English letter, and contains letter, number, and '_'. The max length of the event name is 50.");
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException("Invalid event name: " + dataDescription.eventName);
            }
            return;
        }
        if (!dataDescription.autoTrack && !PropertyUtils.checkProperty(dataDescription.properties)) {
            TDLog.w(TAG, "The data will not be tracked due to properties checking failure: " + dataDescription.properties.toString());
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException("Invalid properties. Please refer to SDK debug log for detail reasons.");
            }
            return;
        }
        try {
            if (dataDescription.timeValueType == TIME_VALUE_TYPE.NONE) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TDConstants.TIME_PATTERN, Locale.CHINA);
                TimeZone defaultTimeZone = this.mConfig.getDefaultTimeZone();
                simpleDateFormat.setTimeZone(defaultTimeZone);
                Date date = new Date();
                timezoneOffset = TDUtils.getTimezoneOffset(date.getTime(), defaultTimeZone);
                str = simpleDateFormat.format(date);
            } else {
                str = dataDescription.timeString;
                timezoneOffset = dataDescription.zoneOffset;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(TDConstants.KEY_TYPE, dataDescription.type);
            jSONObject.put(TDConstants.KEY_TIME, str);
            if (dataDescription.type.equals(TDConstants.TYPE_TRACK)) {
                jSONObject.put(TDConstants.KEY_EVENT_NAME, dataDescription.eventName);
            }
            if (!TextUtils.isEmpty(getLoginId())) {
                jSONObject.put(TDConstants.KEY_ACCOUNT_ID, getLoginId());
            }
            jSONObject.put(TDConstants.KEY_DISTINCT_ID, getDistinctId());
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = dataDescription.properties;
            if (jSONObject3 != null) {
                Iterator<String> itKeys = jSONObject3.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    if (!dataDescription.properties.isNull(next)) {
                        jSONObject2.put(next, dataDescription.properties.get(next));
                    }
                }
            }
            JSONObject jSONObject4 = new JSONObject();
            if (dataDescription.type.equals(TDConstants.TYPE_TRACK)) {
                TDUtils.mergeJSONObject(getSuperProperties(), jSONObject4);
                try {
                    DynamicSuperPropertiesTracker dynamicSuperPropertiesTracker = this.mDynamicSuperPropertiesTracker;
                    if (dynamicSuperPropertiesTracker != null && (dynamicSuperProperties = dynamicSuperPropertiesTracker.getDynamicSuperProperties()) != null && PropertyUtils.checkProperty(dynamicSuperProperties)) {
                        TDUtils.mergeJSONObject(dynamicSuperProperties, jSONObject4);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TDUtils.mergeJSONObject(jSONObject2, jSONObject4);
            if (dataDescription.type.equals(TDConstants.TYPE_TRACK)) {
                jSONObject4.put(TDConstants.KEY_NETWORK_TYPE, this.mSystemInformation.getNetworkType());
                if (!TextUtils.isEmpty(this.mSystemInformation.getAppVersionName())) {
                    jSONObject4.put(TDConstants.KEY_APP_VERSION, this.mSystemInformation.getAppVersionName());
                }
                if (dataDescription.timeValueType != TIME_VALUE_TYPE.TIME_ONLY) {
                    jSONObject4.put(TDConstants.KEY_ZONE_OFFSET, timezoneOffset);
                }
                synchronized (this.mTrackTimer) {
                    eventTimer = this.mTrackTimer.get(dataDescription.eventName);
                    this.mTrackTimer.remove(dataDescription.eventName);
                }
                if (eventTimer != null) {
                    try {
                        Double dValueOf = Double.valueOf(eventTimer.duration());
                        if (dValueOf.doubleValue() > 0.0d) {
                            jSONObject4.put(TDConstants.KEY_DURATION, dValueOf);
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            jSONObject.put(TDConstants.KEY_PROPERTIES, jSONObject4);
            if (!this.mConfig.isDebugOnly() && !this.mConfig.isDebug()) {
                if (dataDescription.saveData) {
                    this.mMessages.saveClickData(jSONObject, getToken());
                    return;
                } else {
                    this.mMessages.postClickData(jSONObject, getToken());
                    return;
                }
            }
            this.mMessages.postToDebug(jSONObject, getToken());
        } catch (Exception e3) {
            TDLog.w(TAG, "Exception occurred in track data: " + dataDescription.type + ": " + dataDescription.properties);
            e3.printStackTrace();
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException(e3);
            }
        }
    }

    public void appBecomeActive() {
        EventTimer value;
        TDQuitSafelyService.getInstance(this.mConfig.mContext).start();
        synchronized (this.mTrackTimer) {
            try {
                for (Map.Entry<String, EventTimer> entry : this.mTrackTimer.entrySet()) {
                    if (entry != null && (value = entry.getValue()) != null) {
                        value.setStartTime(SystemClock.elapsedRealtime());
                    }
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("appBecomeActive error:");
                sb.append(e.getMessage());
                TDLog.i(TAG, sb.toString());
            }
        }
    }

    public void appEnterBackground() {
        EventTimer value;
        synchronized (this.mTrackTimer) {
            try {
                for (Map.Entry<String, EventTimer> entry : this.mTrackTimer.entrySet()) {
                    if (entry != null && !TDConstants.APP_END_EVENT_NAME.equals(entry.getKey().toString()) && (value = entry.getValue()) != null) {
                        value.setEventAccumulatedDuration((value.getEventAccumulatedDuration() + SystemClock.elapsedRealtime()) - value.getStartTime());
                        value.setStartTime(SystemClock.elapsedRealtime());
                    }
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("appEnterBackground error:");
                sb.append(e.getMessage());
                TDLog.i(TAG, sb.toString());
            }
        }
    }

    public void autoTrack(String str, JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        EventDescription eventDescription = new EventDescription(str, jSONObject);
        eventDescription.setAutoTrackFlag();
        trackInternal(eventDescription);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void clearSuperProperties() {
        if (hasDisabled()) {
            return;
        }
        synchronized (this.mSuperProperties) {
            this.mSuperProperties.put(new JSONObject());
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void enableAutoTrack(List<AutoTrackEventType> list) {
        if (hasDisabled()) {
            return;
        }
        this.mAutoTrack = true;
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.contains(AutoTrackEventType.APP_CRASH)) {
            this.mTrackCrash = true;
            TDQuitSafelyService tDQuitSafelyService = TDQuitSafelyService.getInstance(this.mConfig.mContext);
            if (tDQuitSafelyService != null) {
                tDQuitSafelyService.initExceptionHandler();
            }
        }
        if (list.contains(AutoTrackEventType.APP_END)) {
            timeEvent(TDConstants.APP_END_EVENT_NAME);
        }
        if (list.contains(AutoTrackEventType.APP_INSTALL)) {
            synchronized (sInstanceMap) {
                try {
                    Map<Context, List<String>> map = sAppFirstInstallationMap;
                    if (map.containsKey(this.mConfig.mContext) && map.get(this.mConfig.mContext).contains(getToken())) {
                        track(TDConstants.APP_INSTALL_EVENT_NAME);
                        map.get(this.mConfig.mContext).remove(getToken());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        this.mAutoTrackEventTypeList.clear();
        this.mAutoTrackEventTypeList.addAll(list);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void enableTracking(boolean z) {
        TDLog.d(TAG, "enableTracking: " + z);
        if (isEnabled() && !z) {
            flush();
        }
        this.mEnableFlag.put(Boolean.valueOf(z));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void flush() {
        if (hasDisabled()) {
            return;
        }
        this.mMessages.flush(getToken());
    }

    public DataHandle getDataHandleInstance(Context context) {
        return DataHandle.getInstance(context);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public String getDeviceId() {
        if (this.mSystemInformation.getDeviceInfo().containsKey(TDConstants.KEY_DEVICE_ID)) {
            return (String) this.mSystemInformation.getDeviceInfo().get(TDConstants.KEY_DEVICE_ID);
        }
        return null;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public String getDistinctId() {
        String identifyID = getIdentifyID();
        return identifyID == null ? getRandomID() : identifyID;
    }

    public List<Class> getIgnoredViewTypeList() {
        if (this.mIgnoredViewTypeList == null) {
            this.mIgnoredViewTypeList = new ArrayList();
        }
        return this.mIgnoredViewTypeList;
    }

    public String getLoginId() {
        String str;
        String str2;
        synchronized (this.mLoginId) {
            try {
                str = (String) this.mLoginId.get();
                if (TextUtils.isEmpty(str) && this.mEnableTrackOldData) {
                    synchronized (sOldLoginIdLock) {
                        try {
                            str2 = (String) sOldLoginId.get();
                            if (!TextUtils.isEmpty(str2)) {
                                this.mLoginId.put(str2);
                                sOldLoginId.put(null);
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    str = str2;
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        return str;
    }

    public String getRandomID() {
        String str;
        synchronized (sRandomIDLock) {
            str = (String) sRandomID.get();
        }
        return str;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public JSONObject getSuperProperties() {
        JSONObject jSONObject;
        synchronized (this.mSuperProperties) {
            jSONObject = (JSONObject) this.mSuperProperties.get();
        }
        return jSONObject;
    }

    public String getToken() {
        return this.mConfig.mToken;
    }

    public boolean hasDisabled() {
        return !isEnabled() || hasOptOut();
    }

    public boolean hasOptOut() {
        return ((Boolean) this.mOptOutFlag.get()).booleanValue();
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void identify(String str) {
        if (hasDisabled()) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            TDLog.w(TAG, "The identity cannot be empty.");
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException("distinct id cannot be empty");
            }
        } else {
            synchronized (this.mIdentifyId) {
                this.mIdentifyId.put(str);
            }
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreAutoTrackActivities(List<Class<?>> list) {
        if (hasDisabled() || list == null || list.size() == 0) {
            return;
        }
        if (this.mAutoTrackIgnoredActivities == null) {
            this.mAutoTrackIgnoredActivities = new ArrayList();
        }
        for (Class<?> cls : list) {
            if (cls != null && !this.mAutoTrackIgnoredActivities.contains(Integer.valueOf(cls.hashCode()))) {
                this.mAutoTrackIgnoredActivities.add(Integer.valueOf(cls.hashCode()));
            }
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreAutoTrackActivity(Class<?> cls) {
        if (hasDisabled() || cls == null) {
            return;
        }
        if (this.mAutoTrackIgnoredActivities == null) {
            this.mAutoTrackIgnoredActivities = new ArrayList();
        }
        if (this.mAutoTrackIgnoredActivities.contains(Integer.valueOf(cls.hashCode()))) {
            return;
        }
        this.mAutoTrackIgnoredActivities.add(Integer.valueOf(cls.hashCode()));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreView(View view) {
        if (hasDisabled() || view == null) {
            return;
        }
        TDUtils.setTag(getToken(), view, R.id.thinking_analytics_tag_view_ignored, "1");
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void ignoreViewType(Class cls) {
        if (hasDisabled() || cls == null) {
            return;
        }
        if (this.mIgnoredViewTypeList == null) {
            this.mIgnoredViewTypeList = new ArrayList();
        }
        if (this.mIgnoredViewTypeList.contains(cls)) {
            return;
        }
        this.mIgnoredViewTypeList.add(cls);
    }

    public boolean isActivityAutoTrackAppClickIgnored(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        List<Integer> list = this.mAutoTrackIgnoredActivities;
        if (list != null && list.contains(Integer.valueOf(cls.hashCode()))) {
            return true;
        }
        ThinkingDataIgnoreTrackAppViewScreenAndAppClick thinkingDataIgnoreTrackAppViewScreenAndAppClick = (ThinkingDataIgnoreTrackAppViewScreenAndAppClick) cls.getAnnotation(ThinkingDataIgnoreTrackAppViewScreenAndAppClick.class);
        if (thinkingDataIgnoreTrackAppViewScreenAndAppClick != null && (TextUtils.isEmpty(thinkingDataIgnoreTrackAppViewScreenAndAppClick.appId()) || getToken().equals(thinkingDataIgnoreTrackAppViewScreenAndAppClick.appId()))) {
            return true;
        }
        ThinkingDataIgnoreTrackAppClick thinkingDataIgnoreTrackAppClick = (ThinkingDataIgnoreTrackAppClick) cls.getAnnotation(ThinkingDataIgnoreTrackAppClick.class);
        if (thinkingDataIgnoreTrackAppClick != null) {
            return TextUtils.isEmpty(thinkingDataIgnoreTrackAppClick.appId()) || getToken().equals(thinkingDataIgnoreTrackAppClick.appId());
        }
        return false;
    }

    public boolean isActivityAutoTrackAppViewScreenIgnored(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        List<Integer> list = this.mAutoTrackIgnoredActivities;
        if (list != null && list.contains(Integer.valueOf(cls.hashCode()))) {
            return true;
        }
        ThinkingDataIgnoreTrackAppViewScreenAndAppClick thinkingDataIgnoreTrackAppViewScreenAndAppClick = (ThinkingDataIgnoreTrackAppViewScreenAndAppClick) cls.getAnnotation(ThinkingDataIgnoreTrackAppViewScreenAndAppClick.class);
        if (thinkingDataIgnoreTrackAppViewScreenAndAppClick != null && (TextUtils.isEmpty(thinkingDataIgnoreTrackAppViewScreenAndAppClick.appId()) || getToken().equals(thinkingDataIgnoreTrackAppViewScreenAndAppClick.appId()))) {
            return true;
        }
        ThinkingDataIgnoreTrackAppViewScreen thinkingDataIgnoreTrackAppViewScreen = (ThinkingDataIgnoreTrackAppViewScreen) cls.getAnnotation(ThinkingDataIgnoreTrackAppViewScreen.class);
        return thinkingDataIgnoreTrackAppViewScreen != null && (TextUtils.isEmpty(thinkingDataIgnoreTrackAppViewScreen.appId()) || getToken().equals(thinkingDataIgnoreTrackAppViewScreen.appId()));
    }

    public boolean isAutoTrackEnabled() {
        if (hasDisabled()) {
            return false;
        }
        return this.mAutoTrack;
    }

    public boolean isAutoTrackEventTypeIgnored(AutoTrackEventType autoTrackEventType) {
        return (autoTrackEventType == null || this.mAutoTrackEventTypeList.contains(autoTrackEventType)) ? false : true;
    }

    public boolean isEnabled() {
        return ((Boolean) this.mEnableFlag.get()).booleanValue();
    }

    public boolean isTrackFragmentAppViewScreenEnabled() {
        return this.mTrackFragmentAppViewScreen;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void login(String str) {
        if (hasDisabled()) {
            return;
        }
        try {
            if (TextUtils.isEmpty(str)) {
                TDLog.d(TAG, "The account id cannot be empty.");
                if (this.mConfig.shouldThrowException()) {
                    throw new TDDebugException("account id cannot be empty");
                }
            } else {
                synchronized (this.mLoginId) {
                    try {
                        if (!str.equals(this.mLoginId.get())) {
                            this.mLoginId.put(str);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void logout() {
        if (hasDisabled()) {
            return;
        }
        try {
            synchronized (this.mLoginId) {
                try {
                    this.mLoginId.put(null);
                    if (this.mEnableTrackOldData) {
                        synchronized (sOldLoginIdLock) {
                            try {
                                if (!TextUtils.isEmpty((CharSequence) sOldLoginId.get())) {
                                    sOldLoginId.put(null);
                                }
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optInTracking() {
        TDLog.d(TAG, "optInTracking...");
        this.mOptOutFlag.put(Boolean.FALSE);
        this.mMessages.flush(getToken());
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optOutTracking() {
        TDLog.d(TAG, "optOutTracking...");
        this.mOptOutFlag.put(Boolean.TRUE);
        this.mMessages.emptyMessageQueue(getToken());
        synchronized (this.mTrackTimer) {
            this.mTrackTimer.clear();
        }
        this.mIdentifyId.put(null);
        this.mLoginId.put(null);
        synchronized (this.mSuperProperties) {
            this.mSuperProperties.put(new JSONObject());
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void optOutTrackingAndDeleteUser() {
        DataDescription dataDescription = new DataDescription(TDConstants.TYPE_USER_DEL, null);
        dataDescription.setNoCache();
        trackInternal(dataDescription);
        optOutTracking();
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setDynamicSuperPropertiesTracker(DynamicSuperPropertiesTracker dynamicSuperPropertiesTracker) {
        if (hasDisabled()) {
            return;
        }
        this.mDynamicSuperPropertiesTracker = dynamicSuperPropertiesTracker;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setJsBridge(WebView webView) {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new TDWebAppInterface(this), "ThinkingData_APP_JS_Bridge");
        } else {
            TDLog.d(TAG, "SetJsBridge failed due to parameter webView is null");
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException("webView cannot be null for setJsBridge");
            }
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setJsBridgeForX5WebView(Object obj) {
        if (obj == null) {
            TDLog.d(TAG, "SetJsBridge failed due to parameter webView is null");
            return;
        }
        try {
            Method method = obj.getClass().getMethod("addJavascriptInterface", Object.class, String.class);
            if (method == null) {
                return;
            }
            Object[] objArr = new Object[2];
            objArr[0] = new TDWebAppInterface(this);
            objArr[1] = "ThinkingData_APP_JS_Bridge";
            method.invoke(obj, objArr);
        } catch (Exception e) {
            TDLog.w(TAG, "setJsBridgeForX5WebView failed: " + e.toString());
        }
    }

    public void setNetworkType(int i) {
        ThinkingdataNetworkType thinkingdataNetworkType;
        if (i == 0) {
            thinkingdataNetworkType = ThinkingdataNetworkType.NETWORKTYPE_DEFAULT;
        } else if (i == 1) {
            thinkingdataNetworkType = ThinkingdataNetworkType.NETWORKTYPE_WIFI;
        } else if (i != 2) {
            return;
        } else {
            thinkingdataNetworkType = ThinkingdataNetworkType.NETWORKTYPE_ALL;
        }
        setNetworkType(thinkingdataNetworkType);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setSuperProperties(JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        if (jSONObject != null) {
            try {
                if (PropertyUtils.checkProperty(jSONObject)) {
                    synchronized (this.mSuperProperties) {
                        JSONObject jSONObject2 = (JSONObject) this.mSuperProperties.get();
                        TDUtils.mergeJSONObject(jSONObject, jSONObject2);
                        this.mSuperProperties.put(jSONObject2);
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (this.mConfig.shouldThrowException()) {
            throw new TDDebugException("Set super properties failed. Please refer to the SDK debug log for details.");
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewID(Dialog dialog, String str) {
        if (hasDisabled() || dialog == null) {
            return;
        }
        try {
            if (TextUtils.isEmpty(str) || dialog.getWindow() == null) {
                return;
            }
            TDUtils.setTag(getToken(), dialog.getWindow().getDecorView(), R.id.thinking_analytics_tag_view_id, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewProperties(View view, JSONObject jSONObject) {
        if (hasDisabled() || view == null || jSONObject == null) {
            return;
        }
        TDUtils.setTag(getToken(), view, R.id.thinking_analytics_tag_view_properties, jSONObject);
    }

    public boolean shouldTrackCrash() {
        if (hasDisabled()) {
            return false;
        }
        return this.mTrackCrash;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void timeEvent(String str) {
        if (hasDisabled()) {
            return;
        }
        try {
            if (!PropertyUtils.isInvalidName(str)) {
                synchronized (this.mTrackTimer) {
                    this.mTrackTimer.put(str, new EventTimer(TimeUnit.SECONDS));
                }
            } else {
                StringBuilder sb = new StringBuilder("timeEvent event name[");
                sb.append(str);
                sb.append("] is not valid");
                TDLog.d(TAG, sb.toString());
                if (this.mConfig.shouldThrowException()) {
                    throw new TDDebugException("Invalid event name for time event");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void track(String str) {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new EventDescription(str, null));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackAppInstall() {
        if (hasDisabled()) {
            return;
        }
        enableAutoTrack(new ArrayList(Collections.singletonList(AutoTrackEventType.APP_INSTALL)));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackFragmentAppViewScreen() {
        if (hasDisabled()) {
            return;
        }
        this.mTrackFragmentAppViewScreen = true;
    }

    public void trackFromH5(String str) {
        TIME_VALUE_TYPE time_value_type;
        double d;
        if (hasDisabled() || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray(JSONConstants.JK_URL_DATA);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String string = jSONObject.getString(TDConstants.KEY_TIME);
                TIME_VALUE_TYPE time_value_type2 = TIME_VALUE_TYPE.TIME_ONLY;
                if (jSONObject.has(TDConstants.KEY_ZONE_OFFSET)) {
                    d = jSONObject.getDouble(TDConstants.KEY_ZONE_OFFSET);
                    time_value_type = TIME_VALUE_TYPE.ALL;
                } else {
                    time_value_type = time_value_type2;
                    d = 0.1d;
                }
                String string2 = jSONObject.getString(TDConstants.KEY_TYPE);
                JSONObject jSONObject2 = jSONObject.getJSONObject(TDConstants.KEY_PROPERTIES);
                Iterator<String> itKeys = jSONObject2.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    if (next.equals(TDConstants.KEY_ACCOUNT_ID) || next.equals(TDConstants.KEY_DISTINCT_ID) || this.mSystemInformation.getDeviceInfo().containsKey(next)) {
                        itKeys.remove();
                    }
                }
                DataDescription eventDescription = string2.equals(TDConstants.TYPE_TRACK) ? new EventDescription(jSONObject.getString(TDConstants.KEY_EVENT_NAME), jSONObject2) : new DataDescription(string2, jSONObject2);
                eventDescription.setTime(string, d, time_value_type);
                eventDescription.setAutoTrackFlag();
                trackInternal(eventDescription);
            }
        } catch (Exception e) {
            TDLog.w(TAG, "Exception occurred when track data from H5.");
            e.printStackTrace();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Activity activity) {
        if (hasDisabled() || activity == 0) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(TDConstants.SCREEN_NAME, activity.getClass().getCanonicalName());
            TDUtils.getScreenNameAndTitleFromActivity(jSONObject, activity);
            if (!(activity instanceof ScreenAutoTracker)) {
                autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
                return;
            }
            ScreenAutoTracker screenAutoTracker = (ScreenAutoTracker) activity;
            String screenUrl = screenAutoTracker.getScreenUrl();
            JSONObject trackProperties = screenAutoTracker.getTrackProperties();
            if (trackProperties != null) {
                TDUtils.mergeJSONObject(trackProperties, jSONObject);
            }
            trackViewScreenInternal(screenUrl, jSONObject);
        } catch (Exception e) {
            TDLog.i(TAG, "trackViewScreen:" + e);
        }
    }

    public void trackViewScreenInternal(String str, JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        try {
            if (TextUtils.isEmpty(str) && jSONObject == null) {
                return;
            }
            JSONObject jSONObject2 = new JSONObject();
            if (!TextUtils.isEmpty(this.mLastScreenUrl)) {
                jSONObject2.put(TDConstants.KEY_REFERRER, this.mLastScreenUrl);
            }
            jSONObject2.put(TDConstants.KEY_URL, str);
            this.mLastScreenUrl = str;
            if (jSONObject != null) {
                TDUtils.mergeJSONObject(jSONObject, jSONObject2);
            }
            autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject2);
        } catch (JSONException e) {
            TDLog.i(TAG, "trackViewScreen:" + e);
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void unsetSuperProperty(String str) {
        if (hasDisabled() || str == null) {
            return;
        }
        try {
            synchronized (this.mSuperProperties) {
                JSONObject jSONObject = (JSONObject) this.mSuperProperties.get();
                jSONObject.remove(str);
                this.mSuperProperties.put(jSONObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_add(String str, Number number) {
        if (hasDisabled()) {
            return;
        }
        try {
            if (number == null) {
                TDLog.d(TAG, "user_add value must be Number");
                if (this.mConfig.shouldThrowException()) {
                    throw new TDDebugException("Invalid property values for user add.");
                }
            } else {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(str, number);
                user_add(jSONObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (this.mConfig.shouldThrowException()) {
                throw new TDDebugException(e);
            }
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_delete() {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new DataDescription(TDConstants.TYPE_USER_DEL, null));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_set(JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new DataDescription(TDConstants.TYPE_USER_SET, jSONObject));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_setOnce(JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new DataDescription(TDConstants.TYPE_USER_SET_ONCE, jSONObject));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_unset(String... strArr) {
        if (hasDisabled()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        for (String str : strArr) {
            try {
                jSONObject.put(str, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jSONObject.length() > 0) {
            trackInternal(new DataDescription(TDConstants.TYPE_USER_UNSET, jSONObject));
        }
    }

    public class DataDescription {
        boolean autoTrack;
        String eventName;
        JSONObject properties;
        boolean saveData;
        String timeString;
        TIME_VALUE_TYPE timeValueType;
        String type;
        double zoneOffset;

        private DataDescription() {
            this.timeValueType = TIME_VALUE_TYPE.NONE;
            this.saveData = true;
        }

        public void setAutoTrackFlag() {
            this.autoTrack = true;
        }

        public void setNoCache() {
            this.saveData = false;
        }

        public void setTime(String str, double d, TIME_VALUE_TYPE time_value_type) {
            this.timeString = str;
            this.zoneOffset = d;
            this.timeValueType = time_value_type;
        }

        public DataDescription(String str, JSONObject jSONObject) {
            this.timeValueType = TIME_VALUE_TYPE.NONE;
            this.saveData = true;
            this.type = str;
            this.properties = jSONObject;
        }
    }

    public static ThinkingAnalyticsSDK sharedInstance(Context context, String str, String str2) {
        return sharedInstance(context, str, str2, true);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public ThinkingAnalyticsSDK createLightInstance() {
        return new LightThinkingAnalyticsSDK(this.mConfig);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setNetworkType(ThinkingdataNetworkType thinkingdataNetworkType) {
        if (hasDisabled()) {
            return;
        }
        this.mConfig.setNetworkType(thinkingdataNetworkType);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void setViewID(View view, String str) {
        if (hasDisabled() || view == null || TextUtils.isEmpty(str)) {
            return;
        }
        TDUtils.setTag(getToken(), view, R.id.thinking_analytics_tag_view_id, str);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void track(String str, JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new EventDescription(str, jSONObject));
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Fragment fragment) {
        if (hasDisabled() || fragment == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            String canonicalName = fragment.getClass().getCanonicalName();
            String titleFromFragment = TDUtils.getTitleFromFragment(fragment, getToken());
            Activity activity = fragment.getActivity();
            if (activity != null) {
                if (TextUtils.isEmpty(titleFromFragment)) {
                    titleFromFragment = TDUtils.getActivityTitle(activity);
                }
                Locale locale = Locale.CHINA;
                canonicalName = activity.getClass().getCanonicalName() + "|" + canonicalName;
            }
            if (!TextUtils.isEmpty(titleFromFragment)) {
                jSONObject.put(TDConstants.TITLE, titleFromFragment);
            }
            jSONObject.put(TDConstants.SCREEN_NAME, canonicalName);
            autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
        } catch (Exception e) {
            TDLog.i(TAG, "trackViewScreen:" + e);
        }
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void user_add(JSONObject jSONObject) {
        if (hasDisabled()) {
            return;
        }
        trackInternal(new DataDescription(TDConstants.TYPE_USER_ADD, jSONObject));
    }

    public static ThinkingAnalyticsSDK sharedInstance(Context context, String str, String str2, boolean z) {
        String str3;
        if (context == null) {
            str3 = "App context is required to get SDK instance.";
        } else if (TextUtils.isEmpty(str)) {
            str3 = "APP ID is required to get SDK instance.";
        } else {
            TDConfig tDConfig = TDConfig.getInstance(context, str, str2);
            if (tDConfig != null) {
                tDConfig.setTrackOldData(z);
                return sharedInstance(tDConfig);
            }
            str3 = "Cannot get valid TDConfig instance. Returning null";
        }
        TDLog.w(TAG, str3);
        return null;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void track(String str, JSONObject jSONObject, Date date) {
        if (hasDisabled()) {
            return;
        }
        EventDescription eventDescription = new EventDescription(str, jSONObject);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TDConstants.TIME_PATTERN, Locale.CHINA);
        simpleDateFormat.setTimeZone(this.mConfig.getDefaultTimeZone());
        eventDescription.setTime(simpleDateFormat.format(date), 0.1d, TIME_VALUE_TYPE.TIME_ONLY);
        trackInternal(eventDescription);
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void trackViewScreen(Object obj) {
        Class<androidx.fragment.app.Fragment> cls;
        Class<?> cls2;
        Class<androidx.fragment.app.Fragment> cls3 = androidx.fragment.app.Fragment.class;
        if (hasDisabled() || obj == null) {
            return;
        }
        Activity activity = null;
        try {
            int i = androidx.fragment.app.Fragment.a;
            cls = cls3;
        } catch (Exception unused) {
            cls = null;
        }
        try {
            cls2 = Class.forName("android.app.Fragment");
        } catch (Exception unused2) {
            cls2 = null;
        }
        try {
            int i2 = androidx.fragment.app.Fragment.a;
        } catch (Exception unused3) {
            cls3 = null;
        }
        if ((cls == null || !cls.isInstance(obj)) && ((cls2 == null || !cls2.isInstance(obj)) && (cls3 == null || !cls3.isInstance(obj)))) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            String canonicalName = obj.getClass().getCanonicalName();
            String titleFromFragment = TDUtils.getTitleFromFragment(obj, getToken());
            try {
                Method method = obj.getClass().getMethod("getActivity", new Class[0]);
                if (method != null) {
                    activity = (Activity) method.invoke(obj, new Object[0]);
                }
            } catch (Exception unused4) {
            }
            if (activity != null) {
                if (TextUtils.isEmpty(titleFromFragment)) {
                    titleFromFragment = TDUtils.getActivityTitle(activity);
                }
                Locale locale = Locale.CHINA;
                canonicalName = activity.getClass().getCanonicalName() + "|" + canonicalName;
            }
            if (!TextUtils.isEmpty(titleFromFragment)) {
                jSONObject.put(TDConstants.TITLE, titleFromFragment);
            }
            jSONObject.put(TDConstants.SCREEN_NAME, canonicalName);
            autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ThinkingAnalyticsSDK sharedInstance(TDConfig tDConfig) {
        ThinkingAnalyticsSDK thinkingAnalyticsSDK;
        if (tDConfig == null) {
            TDLog.w(TAG, "Cannot initial SDK instance with null config instance.");
            return null;
        }
        Map<Context, Map<String, ThinkingAnalyticsSDK>> map = sInstanceMap;
        synchronized (map) {
            try {
                Map<String, ThinkingAnalyticsSDK> map2 = map.get(tDConfig.mContext);
                if (map2 == null) {
                    map2 = new HashMap<>();
                    map.put(tDConfig.mContext, map2);
                    if (DatabaseAdapter.dbNotExist(tDConfig.mContext) && SystemInformation.getInstance(tDConfig.mContext).hasNotBeenUpdatedSinceInstall()) {
                        sAppFirstInstallationMap.put(tDConfig.mContext, new LinkedList());
                    }
                    TDQuitSafelyService.getInstance(tDConfig.mContext).start();
                }
                thinkingAnalyticsSDK = map2.get(tDConfig.mToken);
                if (thinkingAnalyticsSDK == null) {
                    thinkingAnalyticsSDK = new ThinkingAnalyticsSDK(tDConfig, new boolean[0]);
                    map2.put(tDConfig.mToken, thinkingAnalyticsSDK);
                    Map<Context, List<String>> map3 = sAppFirstInstallationMap;
                    if (map3.containsKey(tDConfig.mContext)) {
                        map3.get(tDConfig.mContext).add(tDConfig.mToken);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return thinkingAnalyticsSDK;
    }

    @Override // cn.thinkingdata.android.IThinkingAnalyticsAPI
    public void track(String str, JSONObject jSONObject, Date date, TimeZone timeZone) {
        if (hasDisabled()) {
            return;
        }
        if (timeZone == null) {
            track(str, jSONObject, date);
            return;
        }
        EventDescription eventDescription = new EventDescription(str, jSONObject);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TDConstants.TIME_PATTERN, Locale.CHINA);
        simpleDateFormat.setTimeZone(timeZone);
        eventDescription.setTime(simpleDateFormat.format(date), TDUtils.getTimezoneOffset(date.getTime(), timeZone), TIME_VALUE_TYPE.ALL);
        trackInternal(eventDescription);
    }
}
