package cn.thinkingdata.android.utils;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class TDLog {
    private static volatile boolean mEnableLog = false;

    public static void d(String str, String str2) {
        if (mEnableLog) {
            getStackTrace();
        }
    }

    public static void e(String str, String str2) {
    }

    private static String getStackTrace() {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        stringBuffer.append(stackTraceElement.getClassName());
        stringBuffer.append(".");
        stringBuffer.append(stackTraceElement.getMethodName());
        stringBuffer.append("() line ");
        stringBuffer.append(stackTraceElement.getLineNumber());
        stringBuffer.append(": ");
        return stringBuffer.toString();
    }

    public static void i(String str, String str2) {
        if (!mEnableLog || str2.length() <= 4000) {
            return;
        }
        largeLog(str, str2);
    }

    private static void largeLog(String str, String str2) {
        if (str2.length() > 4000) {
            str2.substring(0, 4000);
            largeLog(str, str2.substring(4000));
        }
    }

    public static void setEnableLog(boolean z) {
        mEnableLog = z;
    }

    public static void v(String str, String str2) {
    }

    public static void w(String str, String str2) {
        if (mEnableLog) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
    }

    public static void i(String str, String str2, Throwable th) {
    }

    public static void v(String str, String str2, Throwable th) {
    }

    public static void w(String str, String str2, Throwable th) {
        if (mEnableLog) {
            Log.w(str, str2, th);
        }
    }

    public static void i(String str, Throwable th) {
    }
}
