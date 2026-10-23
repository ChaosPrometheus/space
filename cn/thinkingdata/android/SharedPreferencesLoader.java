package cn.thinkingdata.android;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes.dex */
class SharedPreferencesLoader {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public static class LoadSharedPreferences implements Callable<SharedPreferences> {
        private final Context mContext;
        private final String mPrefsName;

        public LoadSharedPreferences(Context context, String str) {
            this.mContext = context;
            this.mPrefsName = str;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public SharedPreferences call() {
            return this.mContext.getSharedPreferences(this.mPrefsName, 0);
        }
    }

    public Future<SharedPreferences> loadPreferences(Context context, String str) {
        FutureTask futureTask = new FutureTask(new LoadSharedPreferences(context, str));
        this.mExecutor.execute(futureTask);
        return futureTask;
    }
}
