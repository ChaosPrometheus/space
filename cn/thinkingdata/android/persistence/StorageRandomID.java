package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.UUID;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class StorageRandomID extends SharedPreferencesStorage<String> {
    public StorageRandomID(Future<SharedPreferences> future) {
        super(future, "randomID");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, java.lang.String] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ String get() {
        return super.get();
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public String create() {
        return UUID.randomUUID().toString();
    }
}
