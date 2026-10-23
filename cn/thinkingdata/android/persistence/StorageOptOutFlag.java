package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class StorageOptOutFlag extends SharedPreferencesStorage<Boolean> {
    public StorageOptOutFlag(Future<SharedPreferences> future) {
        super(future, "optOutFlag");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Boolean, java.lang.Object] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ Boolean get() {
        return super.get();
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Boolean] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void load(SharedPreferences sharedPreferences) {
        this.data = Boolean.valueOf(sharedPreferences.getBoolean(this.storageKey, false));
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ void put(Boolean bool) {
        super.put(bool);
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void save(SharedPreferences.Editor editor, Boolean bool) {
        editor.putBoolean(this.storageKey, bool.booleanValue());
        editor.apply();
    }
}
