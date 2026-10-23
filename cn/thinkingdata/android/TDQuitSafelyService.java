package cn.thinkingdata.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import cn.thinkingdata.android.utils.PropertyUtils;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class TDQuitSafelyService {
    static final String TAG = "ThinkingAnalytics.Quit";
    private static TDQuitSafelyService sInstance;
    private Context mContext;
    private boolean mExceptionHandlerInitialed;

    public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private static final int CRASH_REASON_LENGTH_LIMIT = 16384;
        private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        public ExceptionHandler() {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        private void killProcessAndExit() {
            Process.killProcess(Process.myPid());
            System.exit(10);
        }

        private void processException(Throwable th) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            do {
                th.printStackTrace(printWriter);
                th = th.getCause();
            } while (th != null);
            printWriter.close();
            final String strReplaceAll = stringWriter.toString().replaceAll("(\r\n|\n\r|\n|\r)", "<br>");
            ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.TDQuitSafelyService.ExceptionHandler.1
                @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
                public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                    if (thinkingAnalyticsSDK.shouldTrackCrash()) {
                        try {
                            JSONObject jSONObject = new JSONObject();
                            try {
                                if (strReplaceAll.getBytes("UTF-8").length > ExceptionHandler.CRASH_REASON_LENGTH_LIMIT) {
                                    jSONObject.put(TDConstants.KEY_CRASH_REASON, new String(PropertyUtils.cutToBytes(strReplaceAll, ExceptionHandler.CRASH_REASON_LENGTH_LIMIT), "UTF-8"));
                                } else {
                                    jSONObject.put(TDConstants.KEY_CRASH_REASON, strReplaceAll);
                                }
                            } catch (UnsupportedEncodingException unused) {
                                TDLog.d(TDQuitSafelyService.TAG, "Exception occurred in getBytes. ");
                                if (strReplaceAll.length() > 8192) {
                                    jSONObject.put(TDConstants.KEY_CRASH_REASON, strReplaceAll.substring(0, 8192));
                                }
                            }
                            thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CRASH_EVENT_NAME, jSONObject);
                        } catch (JSONException unused2) {
                        }
                    }
                }
            });
            TDQuitSafelyService.this.quit();
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            Throwable cause = th;
            while (true) {
                if (cause == null) {
                    processException(th);
                    break;
                } else {
                    if (cause instanceof TDDebugException) {
                        TDQuitSafelyService.this.mContext.stopService(new Intent(TDQuitSafelyService.this.mContext, (Class<?>) TDKeepAliveService.class));
                        break;
                    }
                    cause = cause.getCause();
                }
            }
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultExceptionHandler;
            if (uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(thread, th);
            } else {
                killProcessAndExit();
            }
        }
    }

    public class ShutDownHooksThread extends Thread {
        private ShutDownHooksThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            TDLog.d(TDQuitSafelyService.TAG, "ShutdownHook start");
            TDQuitSafelyService.this.quit();
            TDLog.d(TDQuitSafelyService.TAG, "ShutdownHook end");
        }
    }

    public static class TDKeepAliveService extends Service {
        @Override // android.app.Service
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override // android.app.Service
        public void onDestroy() {
            TDLog.d(TDQuitSafelyService.TAG, "KeepAliveService onDestroy");
            super.onDestroy();
        }

        @Override // android.app.Service
        public int onStartCommand(Intent intent, int i, int i2) {
            TDLog.d(TDQuitSafelyService.TAG, "onStartCommand: pid=" + Process.myPid());
            return 2;
        }

        @Override // android.app.Service
        public void onTaskRemoved(Intent intent) {
            super.onTaskRemoved(intent);
            TDQuitSafelyService.getInstance(this).quit();
        }
    }

    private TDQuitSafelyService(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        if (TDContextConfig.getInstance(applicationContext).quitSafelyEnabled()) {
            Runtime.getRuntime().addShutdownHook(new ShutDownHooksThread());
            initExceptionHandler();
        }
    }

    public static TDQuitSafelyService getInstance(Context context) {
        if (sInstance == null) {
            if (context == null) {
                return null;
            }
            synchronized (ExceptionHandler.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new TDQuitSafelyService(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quit() {
        if (TDContextConfig.getInstance(this.mContext).quitSafelyEnabled()) {
            TDLog.i(TAG, "The App is quiting...");
            ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.TDQuitSafelyService.1
                @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
                public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                    thinkingAnalyticsSDK.flush();
                }
            });
            quitSafely("thinkingData.sdk.saveMessageWorker", TDContextConfig.getInstance(this.mContext).getQuitSafelyTimeout());
            quitSafely("thinkingData.sdk.sendMessageWorker", TDContextConfig.getInstance(this.mContext).getQuitSafelyTimeout());
            Context context = this.mContext;
            context.stopService(new Intent(context, (Class<?>) TDKeepAliveService.class));
        }
    }

    private void quitSafely(String str, long j) {
        Looper looper;
        try {
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread.getName().equals(str)) {
                    try {
                        if ((thread instanceof HandlerThread) && (looper = ((HandlerThread) thread).getLooper()) != null) {
                            looper.quitSafely();
                            if (j > 0) {
                                thread.join(j);
                            } else {
                                thread.join(500L);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public synchronized void initExceptionHandler() {
        if (!this.mExceptionHandlerInitialed) {
            new ExceptionHandler();
            this.mExceptionHandlerInitialed = true;
        }
    }

    public void start() {
        try {
            if (TDContextConfig.getInstance(this.mContext).quitSafelyEnabled()) {
                this.mContext.startService(new Intent(this.mContext, (Class<?>) TDKeepAliveService.class));
            }
        } catch (Exception e) {
            TDLog.w(TAG, "Unexpected exception occurred: " + e.getMessage());
        }
    }
}
