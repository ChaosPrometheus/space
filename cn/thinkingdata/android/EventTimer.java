package cn.thinkingdata.android;

import android.os.SystemClock;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
class EventTimer {
    private final TimeUnit timeUnit;
    private long startTime = SystemClock.elapsedRealtime();
    private long eventAccumulatedDuration = 0;

    public EventTimer(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0041 A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:7:0x0019, B:20:0x003b, B:23:0x0041, B:26:0x0048, B:28:0x004d, B:10:0x0020, B:12:0x0026, B:13:0x0029, B:15:0x002f, B:16:0x0031, B:17:0x0033, B:19:0x0037, B:30:0x005a), top: B:34:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:26:0x0048 A[Catch: Exception -> 0x0046, TRY_LEAVE, TryCatch #0 {Exception -> 0x0046, blocks: (B:7:0x0019, B:20:0x003b, B:23:0x0041, B:26:0x0048, B:28:0x004d, B:10:0x0020, B:12:0x0026, B:13:0x0029, B:15:0x002f, B:16:0x0031, B:17:0x0033, B:19:0x0037, B:30:0x005a), top: B:34:0x000f }] */
    public String duration() {
        float f;
        float f2;
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.startTime) + this.eventAccumulatedDuration;
        try {
            if (jElapsedRealtime < 0 || jElapsedRealtime > 86400000) {
                return String.valueOf(0);
            }
            TimeUnit timeUnit = this.timeUnit;
            if (timeUnit != TimeUnit.MILLISECONDS) {
                if (timeUnit == TimeUnit.SECONDS) {
                    f2 = jElapsedRealtime / 1000.0f;
                } else {
                    if (timeUnit == TimeUnit.MINUTES) {
                        f = jElapsedRealtime / 1000.0f;
                    } else if (timeUnit == TimeUnit.HOURS) {
                        f = (jElapsedRealtime / 1000.0f) / 60.0f;
                    }
                    f2 = f / 60.0f;
                }
                return f2 < 0.0f ? String.valueOf(0) : String.format(Locale.CHINA, "%.3f", Float.valueOf(f2));
            }
            f2 = jElapsedRealtime;
            if (f2 < 0.0f) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(0);
        }
    }

    public long getEventAccumulatedDuration() {
        return this.eventAccumulatedDuration;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEventAccumulatedDuration(long j) {
        this.eventAccumulatedDuration = j;
    }

    public void setStartTime(long j) {
        this.startTime = j;
    }
}
