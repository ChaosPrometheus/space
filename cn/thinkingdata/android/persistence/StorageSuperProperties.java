package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class StorageSuperProperties extends SharedPreferencesStorage<JSONObject> {
    public StorageSuperProperties(Future<SharedPreferences> future) {
        super(future, "superProperties");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, org.json.JSONObject] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ JSONObject get() {
        return super.get();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T, org.json.JSONObject] */
    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void load(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString(this.storageKey, null);
        if (string == null) {
            put(create());
            return;
        }
        try {
            this.data = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public /* synthetic */ void put(JSONObject jSONObject) {
        super.put(jSONObject);
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public JSONObject create() {
        return new JSONObject();
    }

    @Override // cn.thinkingdata.android.persistence.SharedPreferencesStorage
    public void save(SharedPreferences.Editor editor, JSONObject jSONObject) {
        editor.putString(this.storageKey, jSONObject == null ? null : jSONObject.toString());
        editor.apply();
    }
}
