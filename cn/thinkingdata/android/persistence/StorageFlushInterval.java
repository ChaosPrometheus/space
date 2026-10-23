package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class StorageFlushInterval extends SharedPreferencesStorage<Integer> {
    private final int mDefaultFlushInterval;

    public StorageFlushInterval(Future<SharedPreferences> future, int i) {
        super(future, "flushInterval");
        this.mDefaultFlushInterval = i;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Integer, java.lang.Object] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ Integer get() {
        return super.get();
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Integer] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void load(SharedPreferences sharedPreferences) {
        this.data = Integer.valueOf(sharedPreferences.getInt(this.storageKey, this.mDefaultFlushInterval));
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ void put(Integer num) {
        super.put(num);
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void save(SharedPreferences.Editor editor, Integer num) {
        editor.putInt(this.storageKey, num.intValue());
        editor.apply();
    }
}
