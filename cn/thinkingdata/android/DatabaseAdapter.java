package cn.thinkingdata.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import cn.thinkingdata.android.utils.TDLog;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DatabaseAdapter {
    private static final String CREATE_EVENTS_TABLE;
    private static final String DATABASE_NAME = "thinkingdata";
    private static final int DB_OUT_OF_MEMORY_ERROR = -2;
    private static final int DB_UPDATE_ERROR = -1;
    private static final int DB_VERSION = 1;
    private static final String EVENTS_TIME_INDEX;
    private static final String KEY_CREATED_AT = "creattime";
    private static final String KEY_DATA = "clickdata";
    private static final String KEY_DATA_SPLIT_SEPARATOR = "#td#";
    private static final String KEY_TOKEN = "token";
    private static final String TAG = "ThinkingAnalytics.DatabaseAdapter";
    private static final Map<Context, DatabaseAdapter> sInstances;
    private final DatabaseHelper mDb;

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private final File mDatabaseFile;
        private final int mMinimumDatabaseLimit;

        public DatabaseHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
            this.mDatabaseFile = context.getDatabasePath(str);
            this.mMinimumDatabaseLimit = TDContextConfig.getInstance(context).getMinimumDatabaseLimit();
        }

        public boolean belowMemThreshold() {
            return !this.mDatabaseFile.exists() || Math.max(this.mDatabaseFile.getUsableSpace(), (long) this.mMinimumDatabaseLimit) >= this.mDatabaseFile.length();
        }

        public void deleteDatabase() {
            close();
            this.mDatabaseFile.delete();
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            TDLog.d(DatabaseAdapter.TAG, "Creating a new ThinkingData events database");
            sQLiteDatabase.execSQL(DatabaseAdapter.CREATE_EVENTS_TABLE);
            sQLiteDatabase.execSQL(DatabaseAdapter.EVENTS_TIME_INDEX);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            TDLog.d(DatabaseAdapter.TAG, "Upgrading ThinkingData events database");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table.EVENTS.getName());
            sQLiteDatabase.execSQL(DatabaseAdapter.CREATE_EVENTS_TABLE);
            sQLiteDatabase.execSQL(DatabaseAdapter.EVENTS_TIME_INDEX);
        }
    }

    public class OldDatabaseHelper extends SQLiteOpenHelper {
        public OldDatabaseHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        }

        public JSONArray getAllEvents() {
            JSONArray jSONArray = new JSONArray();
            Cursor cursorRawQuery = null;
            try {
                try {
                    cursorRawQuery = getReadableDatabase().rawQuery("SELECT * FROM " + Table.EVENTS + " ORDER BY creattime", null);
                    while (cursorRawQuery.moveToNext()) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(DatabaseAdapter.KEY_CREATED_AT, cursorRawQuery.getString(cursorRawQuery.getColumnIndex(DatabaseAdapter.KEY_CREATED_AT)));
                        jSONObject.put(DatabaseAdapter.KEY_DATA, cursorRawQuery.getString(cursorRawQuery.getColumnIndex(DatabaseAdapter.KEY_DATA)));
                        jSONArray.put(jSONObject);
                    }
                    close();
                } catch (Exception e) {
                    e.printStackTrace();
                    close();
                    if (cursorRawQuery != null) {
                    }
                    return jSONArray;
                }
                cursorRawQuery.close();
                return jSONArray;
            } catch (Throwable th) {
                close();
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                throw th;
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    public enum Table {
        EVENTS("events");

        private final String mTableName;

        Table(String str) {
            this.mTableName = str;
        }

        public String getName() {
            return this.mTableName;
        }
    }

    static {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        Table table = Table.EVENTS;
        sb.append(table.getName());
        sb.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, clickdata TEXT NOT NULL, creattime INTEGER NOT NULL, token TEXT NOT NULL DEFAULT '')");
        CREATE_EVENTS_TABLE = sb.toString();
        EVENTS_TIME_INDEX = "CREATE INDEX IF NOT EXISTS time_idx ON " + table.getName() + " (creattime);";
        sInstances = new HashMap();
    }

    public DatabaseAdapter(Context context) {
        this(context, DATABASE_NAME);
    }

    private boolean belowMemThreshold() {
        return this.mDb.belowMemThreshold();
    }

    public static boolean dbNotExist(Context context) {
        return (context.getDatabasePath(DATABASE_NAME).exists() || context.getDatabasePath(context.getPackageName()).exists()) ? false : true;
    }

    public static DatabaseAdapter getInstance(Context context) {
        DatabaseAdapter databaseAdapter;
        Map<Context, DatabaseAdapter> map = sInstances;
        synchronized (map) {
            try {
                Context applicationContext = context.getApplicationContext();
                if (map.containsKey(applicationContext)) {
                    databaseAdapter = map.get(applicationContext);
                } else {
                    databaseAdapter = new DatabaseAdapter(applicationContext);
                    map.put(applicationContext, databaseAdapter);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return databaseAdapter;
    }

    public int addJSON(JSONObject jSONObject, Table table, String str) throws Throwable {
        Cursor cursorRawQuery = null;
        if (!belowMemThreshold()) {
            TDLog.d(TAG, "There is not enough space left on the device to store td data, oldest data will be deleted");
            String[] strArrGenerateDataString = generateDataString(table, null, 100);
            if (strArrGenerateDataString == null || cleanupEvents(strArrGenerateDataString[0], Table.EVENTS, null) <= 0) {
                return -2;
            }
        }
        String name = table.getName();
        try {
            try {
                SQLiteDatabase writableDatabase = this.mDb.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                StringBuilder sb = new StringBuilder();
                sb.append(jSONObject.toString());
                sb.append(KEY_DATA_SPLIT_SEPARATOR);
                sb.append(jSONObject.toString().hashCode());
                contentValues.put(KEY_DATA, sb.toString());
                contentValues.put(KEY_CREATED_AT, Long.valueOf(System.currentTimeMillis()));
                contentValues.put(KEY_TOKEN, str);
                writableDatabase.insert(name, null, contentValues);
                StringBuilder sb2 = new StringBuilder("SELECT COUNT(*) FROM ");
                sb2.append(name);
                sb2.append(" WHERE token='");
                sb2.append(str);
                sb2.append("'");
                cursorRawQuery = writableDatabase.rawQuery(sb2.toString(), null);
                cursorRawQuery.moveToFirst();
                int i = cursorRawQuery.getInt(0);
                cursorRawQuery.close();
                return i;
            } catch (SQLiteException e) {
                StringBuilder sb3 = new StringBuilder("could not add data to table ");
                sb3.append(name);
                sb3.append(". Re-initializing database.");
                TDLog.e(TAG, sb3.toString(), e);
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                this.mDb.deleteDatabase();
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return -1;
            }
        } catch (Throwable th) {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
            throw th;
        }
    }

    public int cleanupEvents(String str, Table table, String str2) {
        String name = table.getName();
        Cursor cursorRawQuery = null;
        try {
            try {
                SQLiteDatabase writableDatabase = this.mDb.getWritableDatabase();
                StringBuilder sb = new StringBuilder("_id <= ");
                sb.append(str);
                if (str2 != null) {
                    sb.append(" AND token = '");
                    sb.append(str2);
                    sb.append("'");
                }
                writableDatabase.delete(name, sb.toString(), null);
                StringBuilder sb2 = new StringBuilder("SELECT COUNT(*) FROM " + name);
                if (str2 != null) {
                    sb2.append(" WHERE token='");
                    sb2.append(str2);
                    sb2.append("'");
                }
                cursorRawQuery = writableDatabase.rawQuery(sb2.toString(), null);
                cursorRawQuery.moveToFirst();
                int i = cursorRawQuery.getInt(0);
                cursorRawQuery.close();
                return i;
            } catch (SQLiteException e) {
                TDLog.e(TAG, "could not clean data from " + name, e);
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                this.mDb.deleteDatabase();
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return -1;
            }
        } catch (Throwable th) {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:59:0x00d9 A[PHI: r1 r8 r10
  0x00d9: PHI (r1v3 java.lang.String) = (r1v2 java.lang.String), (r1v6 java.lang.String) binds: [B:58:0x00d7, B:50:0x00bb] A[DONT_GENERATE, DONT_INLINE]
  0x00d9: PHI (r8v8 java.lang.String) = (r8v7 java.lang.String), (r8v12 java.lang.String) binds: [B:58:0x00d7, B:50:0x00bb] A[DONT_GENERATE, DONT_INLINE]
  0x00d9: PHI (r10v4 android.database.Cursor) = (r10v3 android.database.Cursor), (r10v6 android.database.Cursor) binds: [B:58:0x00d7, B:50:0x00bb] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:66:0x00e8  */
    public String[] generateDataString(Table table, String str, int i) throws Throwable {
        Cursor cursorRawQuery;
        String string;
        String string2;
        String name = table.getName();
        Cursor cursor = null;
        try {
            SQLiteDatabase readableDatabase = this.mDb.getReadableDatabase();
            StringBuilder sb = new StringBuilder("SELECT * FROM ");
            sb.append(name);
            if (str != null) {
                sb.append(" WHERE token = '");
                sb.append(str);
                sb.append("'");
            }
            sb.append(" ORDER BY creattime ASC LIMIT ");
            sb.append(i);
            JSONArray jSONArray = new JSONArray();
            cursorRawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (cursorRawQuery != null) {
                string2 = null;
                while (cursorRawQuery.moveToNext()) {
                    try {
                        try {
                            if (cursorRawQuery.isLast()) {
                                string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("_id"));
                            }
                            try {
                                String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex(KEY_DATA));
                                if (!TextUtils.isEmpty(string3)) {
                                    int iLastIndexOf = string3.lastIndexOf(KEY_DATA_SPLIT_SEPARATOR);
                                    if (iLastIndexOf > -1) {
                                        String strReplaceFirst = string3.substring(iLastIndexOf).replaceFirst(KEY_DATA_SPLIT_SEPARATOR, "");
                                        string3 = string3.substring(0, iLastIndexOf);
                                        if (!TextUtils.isEmpty(string3) && !TextUtils.isEmpty(strReplaceFirst) && strReplaceFirst.equals(String.valueOf(string3.hashCode()))) {
                                        }
                                    }
                                    jSONArray.put(new JSONObject(string3));
                                }
                            } catch (JSONException unused) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = cursorRawQuery;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    } catch (SQLiteException e) {
                        e = e;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not pull records out of database ");
                        sb2.append(name);
                        TDLog.e(TAG, sb2.toString(), e);
                        string = null;
                        string2 = null;
                        if (cursorRawQuery != null) {
                            cursorRawQuery.close();
                        }
                    }
                }
                string = jSONArray.length() > 0 ? jSONArray.toString() : null;
            } else {
                string = null;
                string2 = null;
            }
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        } catch (SQLiteException e2) {
            e = e2;
            cursorRawQuery = null;
        } catch (Throwable th2) {
            th = th2;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        if (string2 == null || string == null) {
            return null;
        }
        return new String[]{string2, string};
    }

    public DatabaseAdapter(Context context, String str) {
        this.mDb = new DatabaseHelper(context, str);
        try {
            File databasePath = context.getDatabasePath(context.getPackageName());
            if (databasePath.exists()) {
                JSONArray allEvents = new OldDatabaseHelper(context, context.getPackageName()).getAllEvents();
                for (int i = 0; i < allEvents.length(); i++) {
                    try {
                        JSONObject jSONObject = allEvents.getJSONObject(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY_DATA, jSONObject.getString(KEY_DATA));
                        contentValues.put(KEY_CREATED_AT, jSONObject.getString(KEY_CREATED_AT));
                        TDLog.d(TAG, contentValues.toString());
                        this.mDb.getWritableDatabase().insert(Table.EVENTS.getName(), null, contentValues);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                databasePath.delete();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void cleanupEvents(long j, Table table) {
        String name = table.getName();
        try {
            this.mDb.getWritableDatabase().delete(name, "creattime <= " + j, null);
        } catch (SQLiteException e) {
            TDLog.e(TAG, "Could not clean timed-out records. Re-initializing database.", e);
            this.mDb.deleteDatabase();
        }
    }

    public void cleanupEvents(Table table, String str) {
        String name = table.getName();
        try {
            SQLiteDatabase writableDatabase = this.mDb.getWritableDatabase();
            StringBuilder sb = new StringBuilder("token = '");
            sb.append(str);
            sb.append("'");
            writableDatabase.delete(name, sb.toString(), null);
        } catch (SQLiteException e) {
            TDLog.e(TAG, "Could not clean records. Re-initializing database.", e);
            this.mDb.deleteDatabase();
        }
    }
}
