package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class StorageLoginID extends SharedPreferencesStorage<String> {
    public StorageLoginID(Future<SharedPreferences> future) {
        super(future, "loginID");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, java.lang.String] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ String get() {
        return super.get();
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ void put(String str) {
        super.put(str);
    }
}
