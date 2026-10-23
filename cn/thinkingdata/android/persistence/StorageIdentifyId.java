package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class StorageIdentifyId extends SharedPreferencesStorage<String> {
    public StorageIdentifyId(Future<SharedPreferences> future) {
        super(future, "identifyID");
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
