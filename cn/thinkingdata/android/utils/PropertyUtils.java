package cn.thinkingdata.android.utils;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PropertyUtils {
    private static final Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z\\d_]{0,49}$", 2);
    private static final String TAG = "ThinkingAnalytics.PropertyUtils";

    public static boolean checkProperty(JSONObject jSONObject) {
        String str;
        if (jSONObject == null) {
            return true;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (TextUtils.isEmpty(next)) {
                str = "Empty property name is not allowed.";
            } else if (KEY_PATTERN.matcher(next).matches()) {
                try {
                    Object obj = jSONObject.get(next);
                    if (!(obj instanceof String) && !(obj instanceof Number) && !(obj instanceof Boolean) && !(obj instanceof Date)) {
                        TDLog.d(TAG, "Property value must be type String, Number, Boolean, or Date");
                        return false;
                    }
                    if (obj instanceof Number) {
                        double dDoubleValue = ((Number) obj).doubleValue();
                        if (dDoubleValue > 9.999999999999998E12d || dDoubleValue < -9.999999999999998E12d) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("The number value [");
                            sb.append(obj);
                            sb.append("] is invalid.");
                            TDLog.d(TAG, sb.toString());
                            return false;
                        }
                    }
                } catch (JSONException e) {
                    str = "Unexpected parameters." + e;
                }
            } else {
                str = "Property name[" + next + "] is not valid. The property KEY must be string that starts with English letter, and contains letter, number, and '_'. The max length of the property KEY is 50. ";
            }
            TDLog.d(TAG, str);
            return false;
        }
        return true;
    }

    public static byte[] cutToBytes(String str, int i) throws UnsupportedEncodingException {
        int i2;
        int i3;
        byte b;
        byte[] bytes = str.getBytes("UTF-8");
        if (bytes.length <= i) {
            return bytes;
        }
        if ((bytes[i] & 128) == 0) {
            return Arrays.copyOf(bytes, i);
        }
        int i4 = 0;
        while (true) {
            i2 = i - i4;
            i3 = i2 - 1;
            b = bytes[i3];
            if ((b & 128) <= 0 || (b & 64) != 0) {
                break;
            }
            i4++;
        }
        return (b & 128) > 0 ? Arrays.copyOf(bytes, i3) : Arrays.copyOf(bytes, i2);
    }

    public static boolean isInvalidName(String str) {
        return str == null || !KEY_PATTERN.matcher(str).matches();
    }
}
