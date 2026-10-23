package cn.thinkingdata.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import cn.thinkingdata.android.utils.HttpService;
import cn.thinkingdata.android.utils.RemoteService;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import cn.thinkingdata.android.utils.TDUtils;
import com.lbe.parallel.q;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DataHandle {
    private static final String KEY_DATA_STRING = "dataString";
    private static final String TAG = "ThinkingAnalytics.DataHandle";
    static final String THREAD_NAME_SAVE_WORKER = "thinkingData.sdk.saveMessageWorker";
    static final String THREAD_NAME_SEND_WORKER = "thinkingData.sdk.sendMessageWorker";
    private static final Map<Context, DataHandle> sInstances = new HashMap();
    private final Context mContext;
    private final DatabaseAdapter mDbAdapter;
    private final SaveMessageWorker mSaveMessageWorker;
    private final SendMessageWorker mSendMessageWorker;
    private final SystemInformation mSystemInformation;

    public class SaveMessageWorker {
        private static final int EMPTY_QUEUE = 1;
        private static final int ENQUEUE_EVENTS = 0;
        private static final int TRIGGER_FLUSH = 2;
        private final Handler mHandler;

        public class AnalyticsSaveMessageHandler extends Handler {
            public AnalyticsSaveMessageHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int iAddJSON;
                int i = message.what;
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            DataHandle.this.mSendMessageWorker.postToServer((String) message.obj);
                            return;
                        }
                        return;
                    }
                    String str = (String) message.obj;
                    DataHandle.this.mSendMessageWorker.emptyQueue(str);
                    synchronized (SaveMessageWorker.this.mHandler) {
                        SaveMessageWorker.this.mHandler.removeMessages(2, str);
                        SaveMessageWorker.this.mHandler.removeMessages(0, str);
                    }
                    synchronized (DataHandle.this.mDbAdapter) {
                        DataHandle.this.mDbAdapter.cleanupEvents(DatabaseAdapter.Table.EVENTS, (String) message.obj);
                    }
                    return;
                }
                try {
                    String str2 = (String) message.obj;
                    String string = message.getData().getString(DataHandle.KEY_DATA_STRING);
                    if (string == null) {
                        return;
                    }
                    JSONObject jSONObject = new JSONObject(string);
                    try {
                        jSONObject.put(TDConstants.DATA_ID, UUID.randomUUID().toString());
                    } catch (JSONException unused) {
                    }
                    synchronized (DataHandle.this.mDbAdapter) {
                        iAddJSON = DataHandle.this.mDbAdapter.addJSON(jSONObject, DatabaseAdapter.Table.EVENTS, str2);
                    }
                    if (iAddJSON < 0) {
                        TDLog.w(DataHandle.TAG, "Save data to database failed.");
                    } else {
                        StringBuilder sb = new StringBuilder("Data enqueued(");
                        sb.append(str2.substring(str2.length() - 4));
                        sb.append("):\n");
                        sb.append(jSONObject.toString(4));
                        TDLog.i(DataHandle.TAG, sb.toString());
                    }
                    SaveMessageWorker.this.checkSendStrategy(str2, iAddJSON);
                } catch (Exception e) {
                    TDLog.w(DataHandle.TAG, "handleData error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        public SaveMessageWorker() {
            HandlerThread handlerThread = new HandlerThread(DataHandle.THREAD_NAME_SAVE_WORKER, 1);
            handlerThread.start();
            this.mHandler = new AnalyticsSaveMessageHandler(handlerThread.getLooper());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkSendStrategy(String str, int i) {
            if (i >= DataHandle.this.getFlushBulkSize(str)) {
                DataHandle.this.mSendMessageWorker.postToServer(str);
            } else {
                DataHandle.this.mSendMessageWorker.posterToServerDelayed(str, DataHandle.this.getFlushInterval(str));
            }
        }

        public void emptyQueue(String str) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 1;
            messageObtain.obj = str;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendMessageAtFrontOfQueue(messageObtain);
            }
        }

        public void saveToDatabase(JSONObject jSONObject, String str) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 0;
            messageObtain.obj = str;
            Bundle bundle = new Bundle();
            bundle.putString(DataHandle.KEY_DATA_STRING, jSONObject.toString());
            messageObtain.setData(bundle);
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendMessage(messageObtain);
            }
        }

        public void triggerFlush(String str) {
            if (!this.mHandler.hasMessages(0, str)) {
                DataHandle.this.mSendMessageWorker.postToServer(str);
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 2;
            messageObtain.obj = str;
            this.mHandler.sendMessage(messageObtain);
        }
    }

    public class SendMessageWorker {
        private static final int EMPTY_FLUSH_QUEUE = 3;
        private static final int FLUSH_QUEUE = 0;
        private static final int FLUSH_QUEUE_OLD = 2;
        private static final int FLUSH_QUEUE_PROCESSING = 1;
        private static final String KEY_APP_ID = "#app_id";
        private static final String KEY_AUTOMATIC_DATA = "automaticData";
        private static final String KEY_DATA = "data";
        private static final int SEND_TO_DEBUG = 5;
        private static final int SEND_TO_SERVER = 4;
        private final JSONObject mDeviceInfo;
        private Handler mHandler;
        private final RemoteService mPoster;
        private final Object mHandlerLock = new Object();
        private Map<String, Boolean> mToastShown = new HashMap();

        public class AnalyticsMessageHandler extends Handler {
            public AnalyticsMessageHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) throws Throwable {
                String string;
                JSONObject jSONObject;
                String str = (String) message.obj;
                TDConfig config = DataHandle.this.getConfig(str);
                if (config == null) {
                    TDLog.w(DataHandle.TAG, "Could found config object for token. Canceling...");
                    return;
                }
                int i = message.what;
                if (i == 0) {
                    synchronized (SendMessageWorker.this.mHandlerLock) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 1;
                        messageObtain.obj = str;
                        SendMessageWorker.this.mHandler.sendMessage(messageObtain);
                        removeMessages(0, str);
                    }
                    try {
                        SendMessageWorker.this.sendData(config);
                    } catch (RuntimeException e) {
                        TDLog.w(DataHandle.TAG, "Send data to server failed due to unexpected exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    synchronized (SendMessageWorker.this.mHandlerLock) {
                        removeMessages(1, str);
                        SendMessageWorker sendMessageWorker = SendMessageWorker.this;
                        sendMessageWorker.posterToServerDelayed(str, DataHandle.this.getFlushInterval(str));
                    }
                    return;
                }
                if (i == 2) {
                    try {
                        SendMessageWorker.this.sendData("", config);
                        return;
                    } catch (RuntimeException e2) {
                        TDLog.w(DataHandle.TAG, "Send old data failed due to unexpected exception: " + e2.getMessage());
                        e2.printStackTrace();
                        return;
                    }
                }
                if (i == 3) {
                    synchronized (SendMessageWorker.this.mHandlerLock) {
                        removeMessages(0, str);
                    }
                    return;
                }
                if (i == 4) {
                    try {
                        String string2 = message.getData().getString(DataHandle.KEY_DATA_STRING);
                        if (string2 == null) {
                            return;
                        }
                        SendMessageWorker.this.sendData(config, new JSONObject(string2));
                        return;
                    } catch (Exception e3) {
                        TDLog.e(DataHandle.TAG, "Exception occurred when sending message to Server: " + e3.getMessage());
                        return;
                    }
                }
                if (i != 5) {
                    return;
                }
                try {
                    String string3 = message.getData().getString(DataHandle.KEY_DATA_STRING);
                    if (string3 == null) {
                        return;
                    }
                    SendMessageWorker.this.sendDebugData(config, new JSONObject(string3));
                } catch (Exception e4) {
                    TDLog.e(DataHandle.TAG, "Exception occurred when sending message to Server: " + e4.getMessage());
                    if (config.shouldThrowException()) {
                        throw new TDDebugException(e4);
                    }
                    if (config.isDebugOnly() || (string = message.getData().getString(DataHandle.KEY_DATA_STRING)) == null) {
                        return;
                    }
                    try {
                        jSONObject = new JSONObject(string);
                    } catch (JSONException e5) {
                        e5.printStackTrace();
                        jSONObject = null;
                    }
                    DataHandle.this.saveClickData(jSONObject, str);
                }
            }
        }

        public SendMessageWorker() {
            HandlerThread handlerThread = new HandlerThread(DataHandle.THREAD_NAME_SEND_WORKER, 1);
            handlerThread.start();
            this.mHandler = new AnalyticsMessageHandler(handlerThread.getLooper());
            this.mPoster = DataHandle.this.getPoster();
            this.mDeviceInfo = new JSONObject(DataHandle.this.mSystemInformation.getDeviceInfo());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendData(TDConfig tDConfig) throws Throwable {
            sendData(tDConfig.mToken, tDConfig);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendDebugData(TDConfig tDConfig, JSONObject jSONObject) throws JSONException {
            if (tDConfig.isNormal()) {
                DataHandle.this.saveClickData(jSONObject, tDConfig.mToken);
                return;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject(TDConstants.KEY_PROPERTIES);
            if (TDConstants.TYPE_TRACK.equals(jSONObject.getString(TDConstants.KEY_TYPE))) {
                JSONObject jSONObject3 = new JSONObject();
                TDUtils.mergeJSONObject(this.mDeviceInfo, jSONObject3);
                TDUtils.mergeJSONObject(jSONObject2, jSONObject3);
                jSONObject.put(TDConstants.KEY_PROPERTIES, jSONObject3);
            }
            StringBuilder sb = new StringBuilder("appid=");
            sb.append(tDConfig.mToken);
            sb.append("&deviceId=");
            sb.append(this.mDeviceInfo.getString(TDConstants.KEY_DEVICE_ID));
            sb.append("&source=client&data=");
            sb.append(URLEncoder.encode(jSONObject.toString()));
            if (tDConfig.isDebugOnly()) {
                sb.append("&dryRun=1");
            }
            String str = tDConfig.mToken;
            String strSubstring = str.substring(str.length() - 4);
            TDLog.d(DataHandle.TAG, "uploading message(" + strSubstring + "):\n" + jSONObject.toString(4));
            JSONObject jSONObject4 = new JSONObject(this.mPoster.performRequest(tDConfig.getDebugUrl(), sb.toString(), true, tDConfig.getSSLSocketFactory()));
            int i = jSONObject4.getInt("errorLevel");
            if (i == -1) {
                if (tDConfig.isDebugOnly()) {
                    TDLog.w(DataHandle.TAG, "The data will be discarded due to this device is not allowed to debug for: " + strSubstring);
                    return;
                } else {
                    TDLog.d(DataHandle.TAG, "fallback to normal mode due to this device is not allowed to debug for: " + strSubstring);
                    tDConfig.setMode(TDConfig.ModeEnum.NORMAL);
                    jSONObject.put(TDConstants.KEY_PROPERTIES, jSONObject2);
                    DataHandle.this.saveClickData(jSONObject, tDConfig.mToken);
                    return;
                }
            }
            Boolean bool = this.mToastShown.get(tDConfig.mToken);
            if (bool == null || !bool.booleanValue()) {
                Toast.makeText(DataHandle.this.mContext, "Debug Mode Enabled for: " + strSubstring, 1).show();
                this.mToastShown.put(tDConfig.mToken, Boolean.TRUE);
                tDConfig.setAllowDebug();
            }
            if (i == 0) {
                TDLog.d(DataHandle.TAG, "Upload debug data successfully for " + strSubstring);
                return;
            }
            if (jSONObject4.has("errorProperties")) {
                TDLog.d(DataHandle.TAG, " Error Properties: \n" + jSONObject4.getJSONArray("errorProperties").toString(4));
            }
            if (jSONObject4.has("errorReasons")) {
                TDLog.d(DataHandle.TAG, "Error Reasons: \n" + jSONObject4.getJSONArray("errorReasons").toString(4));
            }
            if (tDConfig.shouldThrowException()) {
                if (1 == i) {
                    throw new TDDebugException("Invalid properties. Please refer to the logcat log for detail info.");
                }
                if (2 != i) {
                    throw new TDDebugException(q.g("Unknown error level: ", i));
                }
                throw new TDDebugException("Invalid data format. Please refer to the logcat log for detail info.");
            }
        }

        public void emptyQueue(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 3;
            messageObtain.obj = str;
            this.mHandler.sendMessageAtFrontOfQueue(messageObtain);
        }

        public void postOldDataToServer(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 2;
            messageObtain.obj = str;
            this.mHandler.sendMessage(messageObtain);
        }

        public void postToDebug(JSONObject jSONObject, String str) {
            if (jSONObject == null) {
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 5;
            messageObtain.obj = str;
            Bundle bundle = new Bundle();
            bundle.putString(DataHandle.KEY_DATA_STRING, jSONObject.toString());
            messageObtain.setData(bundle);
            this.mHandler.sendMessage(messageObtain);
        }

        public void postToServer(String str) {
            synchronized (this.mHandlerLock) {
                try {
                    Handler handler = this.mHandler;
                    if (handler != null && !handler.hasMessages(1, str)) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 0;
                        messageObtain.obj = str;
                        this.mHandler.sendMessage(messageObtain);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void posterToServerDelayed(String str, long j) {
            synchronized (this.mHandlerLock) {
                Handler handler = this.mHandler;
                if (handler != null && !handler.hasMessages(0, str) && !this.mHandler.hasMessages(1, str)) {
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 0;
                    messageObtain.obj = str;
                    try {
                        this.mHandler.sendMessageDelayed(messageObtain, j);
                    } catch (IllegalStateException e) {
                        TDLog.w(DataHandle.TAG, "The app might be quiting: " + e.getMessage());
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendData(TDConfig tDConfig, JSONObject jSONObject) throws JSONException {
            if (TextUtils.isEmpty(tDConfig.mToken)) {
                return;
            }
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("data", jSONArray);
            jSONObject2.put(KEY_AUTOMATIC_DATA, this.mDeviceInfo);
            jSONObject2.put(KEY_APP_ID, tDConfig.mToken);
            TDLog.i(DataHandle.TAG, "ret code: " + new JSONObject(this.mPoster.performRequest(tDConfig.getServerUrl(), jSONObject2.toString(), false, tDConfig.getSSLSocketFactory())).getString("code") + ", upload message:\n" + jSONObject2.toString(4));
        }

        public void postToServer(JSONObject jSONObject, String str) {
            if (jSONObject == null) {
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 4;
            messageObtain.obj = str;
            Bundle bundle = new Bundle();
            bundle.putString(DataHandle.KEY_DATA_STRING, jSONObject.toString());
            messageObtain.setData(bundle);
            this.mHandler.sendMessage(messageObtain);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:118:0x01f6  */
        /* JADX WARN: Code duplicated, block: B:120:0x01fd  */
        /* JADX WARN: Code duplicated, block: B:134:0x0204 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        public void sendData(String str, TDConfig tDConfig) throws Throwable {
            DatabaseAdapter.Table table;
            String[] strArrGenerateDataString;
            String string;
            boolean z;
            int iCleanupEvents;
            String str2;
            if (tDConfig == null) {
                TDLog.w(DataHandle.TAG, "Could found config object for sendToken. Canceling...");
                return;
            }
            if (TextUtils.isEmpty(tDConfig.mToken)) {
                return;
            }
            try {
                if (!DataHandle.this.mSystemInformation.isOnline() || !tDConfig.isShouldFlush(DataHandle.this.mSystemInformation.getNetworkType())) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            do {
                synchronized (DataHandle.this.mDbAdapter) {
                    DatabaseAdapter databaseAdapter = DataHandle.this.mDbAdapter;
                    table = DatabaseAdapter.Table.EVENTS;
                    strArrGenerateDataString = databaseAdapter.generateDataString(table, str, 50);
                }
                if (strArrGenerateDataString == null) {
                    return;
                }
                String str3 = strArrGenerateDataString[0];
                String str4 = strArrGenerateDataString[1];
                try {
                    try {
                        try {
                            try {
                                JSONArray jSONArray = new JSONArray(str4);
                                try {
                                    JSONObject jSONObject = new JSONObject();
                                    try {
                                        jSONObject.put("data", jSONArray);
                                        jSONObject.put(KEY_AUTOMATIC_DATA, this.mDeviceInfo);
                                        jSONObject.put(KEY_APP_ID, tDConfig.mToken);
                                        try {
                                            String string2 = new JSONObject(this.mPoster.performRequest(tDConfig.getServerUrl(), jSONObject.toString(), false, tDConfig.getSSLSocketFactory())).getString("code");
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("ret code: ");
                                            sb.append(string2);
                                            sb.append(", upload message:\n");
                                            sb.append(jSONObject.toString(4));
                                            TDLog.i(DataHandle.TAG, sb.toString());
                                            if (!TextUtils.isEmpty(null)) {
                                                TDLog.d(DataHandle.TAG, null);
                                            }
                                            synchronized (DataHandle.this.mDbAdapter) {
                                                iCleanupEvents = DataHandle.this.mDbAdapter.cleanupEvents(str3, table, str);
                                            }
                                            str2 = String.format(Locale.CHINA, "Events flushed. [left = %d]", Integer.valueOf(iCleanupEvents));
                                        } catch (MalformedInputException unused) {
                                            z = true;
                                            try {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("Cannot interpret ");
                                                sb2.append(tDConfig.getServerUrl());
                                                sb2.append(" as a URL. The data will be deleted.");
                                                String string3 = sb2.toString();
                                                if (!TextUtils.isEmpty(string3)) {
                                                    TDLog.d(DataHandle.TAG, string3);
                                                }
                                                if (!z) {
                                                    return;
                                                }
                                                synchronized (DataHandle.this.mDbAdapter) {
                                                    iCleanupEvents = DataHandle.this.mDbAdapter.cleanupEvents(str3, DatabaseAdapter.Table.EVENTS, str);
                                                }
                                                str2 = String.format(Locale.CHINA, "Events flushed. [left = %d]", Integer.valueOf(iCleanupEvents));
                                            } catch (Throwable th) {
                                                th = th;
                                                if (!TextUtils.isEmpty(null)) {
                                                    TDLog.d(DataHandle.TAG, null);
                                                }
                                                if (z) {
                                                    synchronized (DataHandle.this.mDbAdapter) {
                                                        int iCleanupEvents2 = DataHandle.this.mDbAdapter.cleanupEvents(str3, DatabaseAdapter.Table.EVENTS, str);
                                                    }
                                                    TDLog.i(DataHandle.TAG, String.format(Locale.CHINA, "Events flushed. [left = %d]", Integer.valueOf(iCleanupEvents2)));
                                                }
                                                throw th;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            z = true;
                                            if (!TextUtils.isEmpty(null)) {
                                                TDLog.d(DataHandle.TAG, null);
                                            }
                                            if (z) {
                                                synchronized (DataHandle.this.mDbAdapter) {
                                                    int iCleanupEvents3 = DataHandle.this.mDbAdapter.cleanupEvents(str3, DatabaseAdapter.Table.EVENTS, str);
                                                    TDLog.i(DataHandle.TAG, String.format(Locale.CHINA, "Events flushed. [left = %d]", Integer.valueOf(iCleanupEvents3)));
                                                }
                                            }
                                            throw th;
                                        }
                                    } catch (JSONException e2) {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Invalid data: ");
                                        sb3.append(jSONObject.toString());
                                        TDLog.w(DataHandle.TAG, sb3.toString());
                                        throw e2;
                                    }
                                } catch (JSONException unused2) {
                                    if (!TextUtils.isEmpty("Cannot post message due to JSONException, the data will be deleted")) {
                                        TDLog.d(DataHandle.TAG, "Cannot post message due to JSONException, the data will be deleted");
                                    }
                                    synchronized (DataHandle.this.mDbAdapter) {
                                        iCleanupEvents = DataHandle.this.mDbAdapter.cleanupEvents(str3, DatabaseAdapter.Table.EVENTS, str);
                                        str2 = String.format(Locale.CHINA, "Events flushed. [left = %d]", Integer.valueOf(iCleanupEvents));
                                    }
                                }
                            } catch (RemoteService.ServiceUnavailableException unused3) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Cannot post message to ");
                                sb4.append(tDConfig.getServerUrl());
                                string = sb4.toString();
                                if (TextUtils.isEmpty(string)) {
                                    return;
                                }
                                TDLog.d(DataHandle.TAG, string);
                                return;
                            } catch (IOException unused4) {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Cannot post message to ");
                                sb5.append(tDConfig.getServerUrl());
                                string = sb5.toString();
                                if (TextUtils.isEmpty(string)) {
                                    return;
                                }
                                TDLog.d(DataHandle.TAG, string);
                                return;
                            }
                        } catch (JSONException e3) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("The data is invalid: ");
                            sb6.append(str4);
                            TDLog.w(DataHandle.TAG, sb6.toString());
                            throw e3;
                        }
                    } catch (MalformedInputException unused5) {
                        z = false;
                    }
                    TDLog.i(DataHandle.TAG, str2);
                } catch (Throwable th3) {
                    th = th3;
                    z = false;
                }
            } while (iCleanupEvents > 0);
        }
    }

    public DataHandle(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        TDContextConfig tDContextConfig = TDContextConfig.getInstance(applicationContext);
        this.mSystemInformation = SystemInformation.getInstance(applicationContext);
        DatabaseAdapter dbAdapter = getDbAdapter(applicationContext);
        this.mDbAdapter = dbAdapter;
        dbAdapter.cleanupEvents(System.currentTimeMillis() - tDContextConfig.getDataExpiration(), DatabaseAdapter.Table.EVENTS);
        this.mSendMessageWorker = new SendMessageWorker();
        this.mSaveMessageWorker = new SaveMessageWorker();
    }

    public static DataHandle getInstance(Context context) {
        DataHandle dataHandle;
        Map<Context, DataHandle> map = sInstances;
        synchronized (map) {
            try {
                Context applicationContext = context.getApplicationContext();
                if (map.containsKey(applicationContext)) {
                    dataHandle = map.get(applicationContext);
                } else {
                    dataHandle = new DataHandle(applicationContext);
                    map.put(applicationContext, dataHandle);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return dataHandle;
    }

    public void emptyMessageQueue(String str) {
        this.mSaveMessageWorker.emptyQueue(str);
    }

    public void flush(String str) {
        this.mSaveMessageWorker.triggerFlush(str);
    }

    public void flushOldData(String str) {
        this.mSendMessageWorker.postOldDataToServer(str);
    }

    public TDConfig getConfig(String str) {
        return TDConfig.getInstance(this.mContext, str);
    }

    public DatabaseAdapter getDbAdapter(Context context) {
        return DatabaseAdapter.getInstance(context);
    }

    public int getFlushBulkSize(String str) {
        TDConfig config = getConfig(str);
        if (config == null) {
            return 20;
        }
        return config.getFlushBulkSize();
    }

    public int getFlushInterval(String str) {
        TDConfig config = getConfig(str);
        if (config == null) {
            return 15000;
        }
        return config.getFlushInterval();
    }

    public RemoteService getPoster() {
        return new HttpService();
    }

    public void postClickData(JSONObject jSONObject, String str) {
        this.mSendMessageWorker.postToServer(jSONObject, str);
    }

    public void postToDebug(JSONObject jSONObject, String str) {
        this.mSendMessageWorker.postToDebug(jSONObject, str);
    }

    public void saveClickData(JSONObject jSONObject, String str) {
        this.mSaveMessageWorker.saveToDatabase(jSONObject, str);
    }
}
