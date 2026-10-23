package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
abstract class SharedPreferencesStorage<T> {
    protected T data;
    private final Future<SharedPreferences> loadStoredPreferences;
    final String storageKey;

    public SharedPreferencesStorage(Future<SharedPreferences> future, String str) {
        this.loadStoredPreferences = future;
        this.storageKey = str;
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = this.loadStoredPreferences.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            sharedPreferences = null;
        }
        return sharedPreferences.edit();
    }

    public T create() {
        return null;
    }

    public T get() {
        SharedPreferences sharedPreferences;
        if (this.data == null) {
            synchronized (this.loadStoredPreferences) {
                try {
                    sharedPreferences = this.loadStoredPreferences.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    sharedPreferences = null;
                }
                load(sharedPreferences);
            }
        }
        return this.data;
    }

    public void load(SharedPreferences sharedPreferences) {
        T t = (T) sharedPreferences.getString(this.storageKey, null);
        if (t == null) {
            put(create());
        } else {
            this.data = t;
        }
    }

    public void put(T t) {
        this.data = t;
        synchronized (this.loadStoredPreferences) {
            save(getEditor(), this.data);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void save(SharedPreferences.Editor editor, T t) {
        editor.putString(this.storageKey, (String) t);
        editor.apply();
    }
}
