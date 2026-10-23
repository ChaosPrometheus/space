package cn.thinkingdata.android.utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import cn.thinkingdata.android.PathFinder;
import cn.thinkingdata.android.R;
import cn.thinkingdata.android.ScreenAutoTracker;
import cn.thinkingdata.android.ThinkingDataFragmentTitle;
import com.lbe.doubleagent.A1;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class TDUtils {
    public static void addViewPathProperties(Activity activity, View view, JSONObject jSONObject) {
        ViewParent parent;
        if (view == null) {
            return;
        }
        if (jSONObject == null) {
            try {
                jSONObject = new JSONObject();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        do {
            parent = view.getParent();
            int childIndex = getChildIndex(parent, view);
            StringBuilder sb = new StringBuilder();
            sb.append(view.getClass().getCanonicalName());
            sb.append("[");
            sb.append(childIndex);
            sb.append("]");
            arrayList.add(sb.toString());
            if (parent instanceof ViewGroup) {
                view = (ViewGroup) parent;
            }
        } while (parent instanceof ViewGroup);
        Collections.reverse(arrayList);
        StringBuilder sb2 = new StringBuilder();
        for (int i = 1; i < arrayList.size(); i++) {
            sb2.append((String) arrayList.get(i));
            if (i != arrayList.size() - 1) {
                sb2.append(A1.t);
            }
        }
        jSONObject.put(TDConstants.ELEMENT_SELECTOR, sb2.toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x001e, code lost:
    
        if ((r1 instanceof android.app.Activity) != false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.app.Activity getActivityFromContext(android.content.Context r1) {
        /*
            if (r1 == 0) goto L26
            boolean r0 = r1 instanceof android.app.Activity     // Catch: java.lang.Exception -> L1a
            if (r0 == 0) goto L7
            goto L20
        L7:
            boolean r0 = r1 instanceof android.content.ContextWrapper     // Catch: java.lang.Exception -> L1a
            if (r0 == 0) goto L26
        Lb:
            boolean r0 = r1 instanceof android.app.Activity     // Catch: java.lang.Exception -> L1a
            if (r0 != 0) goto L1c
            boolean r0 = r1 instanceof android.content.ContextWrapper     // Catch: java.lang.Exception -> L1a
            if (r0 == 0) goto L1c
            android.content.ContextWrapper r1 = (android.content.ContextWrapper) r1     // Catch: java.lang.Exception -> L1a
            android.content.Context r1 = r1.getBaseContext()     // Catch: java.lang.Exception -> L1a
            goto Lb
        L1a:
            r1 = move-exception
            goto L23
        L1c:
            boolean r0 = r1 instanceof android.app.Activity     // Catch: java.lang.Exception -> L1a
            if (r0 == 0) goto L26
        L20:
            android.app.Activity r1 = (android.app.Activity) r1     // Catch: java.lang.Exception -> L1a
            goto L27
        L23:
            r1.printStackTrace()
        L26:
            r1 = 0
        L27:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.thinkingdata.android.utils.TDUtils.getActivityFromContext(android.content.Context):android.app.Activity");
    }

    public static String getActivityTitle(Activity activity) {
        PackageManager packageManager;
        if (activity != null) {
            try {
                String string = !TextUtils.isEmpty(activity.getTitle()) ? activity.getTitle().toString() : null;
                String toolbarTitle = getToolbarTitle(activity);
                if (!TextUtils.isEmpty(toolbarTitle)) {
                    string = toolbarTitle;
                }
                if (!TextUtils.isEmpty(string) || (packageManager = activity.getPackageManager()) == null) {
                    return string;
                }
                ActivityInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), 0);
                return !TextUtils.isEmpty(activityInfo.loadLabel(packageManager)) ? activityInfo.loadLabel(packageManager).toString() : string;
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private static int getChildIndex(ViewParent viewParent, View view) {
        try {
            if (!(viewParent instanceof ViewGroup)) {
                return -1;
            }
            ViewGroup viewGroup = (ViewGroup) viewParent;
            String viewId = getViewId(view);
            String canonicalName = view.getClass().getCanonicalName();
            int i = 0;
            for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                View childAt = viewGroup.getChildAt(i2);
                if (PathFinder.hasClassName(childAt, canonicalName)) {
                    String viewId2 = getViewId(childAt);
                    if ((viewId == null || viewId.equals(viewId2)) && childAt == view) {
                        return i;
                    }
                    i++;
                }
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void getFragmentNameFromView(View view, JSONObject jSONObject) {
        if (view != null) {
            try {
                int i = R.id.thinking_analytics_tag_view_fragment_name;
                String str = (String) view.getTag(i);
                if (TextUtils.isEmpty(str) && view.getParent() != null && (view.getParent() instanceof View)) {
                    str = (String) ((View) view.getParent()).getTag(i);
                }
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                String strOptString = jSONObject.optString(TDConstants.SCREEN_NAME);
                if (TextUtils.isEmpty(str)) {
                    jSONObject.put(TDConstants.SCREEN_NAME, str);
                    return;
                }
                Locale locale = Locale.CHINA;
                jSONObject.put(TDConstants.SCREEN_NAME, strOptString + "|" + str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void getScreenNameAndTitleFromActivity(JSONObject jSONObject, Activity activity) {
        PackageManager packageManager;
        if (activity == null || jSONObject == null) {
            return;
        }
        try {
            jSONObject.put(TDConstants.SCREEN_NAME, activity.getClass().getCanonicalName());
            String string = activity.getTitle().toString();
            String toolbarTitle = getToolbarTitle(activity);
            if (!TextUtils.isEmpty(toolbarTitle)) {
                string = toolbarTitle;
            }
            if (TextUtils.isEmpty(string) && (packageManager = activity.getPackageManager()) != null) {
                string = packageManager.getActivityInfo(activity.getComponentName(), 0).loadLabel(packageManager).toString();
            }
            if (TextUtils.isEmpty(string)) {
                return;
            }
            jSONObject.put(TDConstants.TITLE, string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Object getTag(String str, View view, int i) {
        HashMap map = (HashMap) view.getTag(i);
        if (map == null) {
            return null;
        }
        return map.get(str);
    }

    public static double getTimezoneOffset(long j, TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        return ((double) timeZone.getOffset(j)) / 3600000.0d;
    }

    public static String getTitleFromFragment(Object obj, String str) {
        ThinkingDataFragmentTitle thinkingDataFragmentTitle;
        JSONObject trackProperties;
        String strOptString = null;
        try {
            if ((obj instanceof ScreenAutoTracker) && (trackProperties = ((ScreenAutoTracker) obj).getTrackProperties()) != null && trackProperties.has(TDConstants.TITLE)) {
                strOptString = trackProperties.optString(TDConstants.TITLE);
            }
            if (!TextUtils.isEmpty(strOptString) || !obj.getClass().isAnnotationPresent(ThinkingDataFragmentTitle.class) || (thinkingDataFragmentTitle = (ThinkingDataFragmentTitle) obj.getClass().getAnnotation(ThinkingDataFragmentTitle.class)) == null) {
                return strOptString;
            }
            if (!TextUtils.isEmpty(thinkingDataFragmentTitle.appId()) && !str.equals(thinkingDataFragmentTitle.appId())) {
                return strOptString;
            }
            return thinkingDataFragmentTitle.title();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @TargetApi(11)
    public static String getToolbarTitle(Activity activity) {
        Class<AppCompatActivity> cls;
        Method method;
        Object objInvoke;
        Method method2;
        CharSequence charSequence;
        Class<AppCompatActivity> cls2 = AppCompatActivity.class;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) {
            try {
                int i = AppCompatActivity.c;
                cls = cls2;
            } catch (Exception unused) {
                cls = null;
            }
            if (cls == null) {
                try {
                    int i2 = AppCompatActivity.c;
                } catch (Exception unused2) {
                    cls2 = cls;
                }
            } else {
                cls2 = cls;
            }
            if (cls2 != null) {
                try {
                    if (cls2.isInstance(activity) && (method = activity.getClass().getMethod("getSupportActionBar", new Class[0])) != null && (objInvoke = method.invoke(activity, new Object[0])) != null && (method2 = objInvoke.getClass().getMethod("getTitle", new Class[0])) != null && (charSequence = (CharSequence) method2.invoke(objInvoke, new Object[0])) != null) {
                        return charSequence.toString();
                    }
                } catch (Exception unused3) {
                }
            }
        } else if (!TextUtils.isEmpty(actionBar.getTitle())) {
            return actionBar.getTitle().toString();
        }
        return null;
    }

    public static String getViewId(View view) {
        return getViewId(view, null);
    }

    public static void mergeJSONObject(JSONObject jSONObject, JSONObject jSONObject2) {
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof Date) {
                obj = new SimpleDateFormat(TDConstants.TIME_PATTERN, Locale.CHINA).format((Date) obj);
            }
            jSONObject2.put(next, obj);
        }
    }

    public static synchronized void setTag(String str, View view, int i, Object obj) {
        if (str == null) {
            return;
        }
        try {
            HashMap map = (HashMap) view.getTag(i);
            if (map == null) {
                map = new HashMap();
            }
            map.put(str, obj);
            view.setTag(i, map);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:67:0x00cc A[Catch: Exception -> 0x0009, TRY_LEAVE, TryCatch #1 {Exception -> 0x0009, blocks: (B:4:0x0004, B:8:0x000c, B:10:0x0014, B:70:0x00d8, B:13:0x0020, B:15:0x0024, B:24:0x0036, B:26:0x003a, B:27:0x003c, B:65:0x00c6, B:67:0x00cc, B:69:0x00d5, B:29:0x0044, B:31:0x004a, B:33:0x0053, B:35:0x0059, B:39:0x006c, B:36:0x0060, B:38:0x0066, B:40:0x0076, B:42:0x007a, B:43:0x007d, B:45:0x0081, B:47:0x0089, B:48:0x008e, B:49:0x0093, B:51:0x0097, B:52:0x0099, B:53:0x009e, B:55:0x00a2, B:56:0x00a5, B:58:0x00a9, B:59:0x00ac, B:61:0x00b0, B:63:0x00bc, B:71:0x00dc), top: B:78:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x00d8 A[SYNTHETIC] */
    public static String traverseView(StringBuilder sb, ViewGroup viewGroup) {
        Class cls;
        CharSequence string;
        TextView textView;
        TextView textView2;
        try {
            if (viewGroup == null) {
                return sb.toString();
            }
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    if (childAt instanceof ViewGroup) {
                        traverseView(sb, (ViewGroup) childAt);
                    } else {
                        CharSequence text = null;
                        try {
                            int i2 = SwitchCompat.a;
                            cls = SwitchCompat.class;
                        } catch (Exception unused) {
                            cls = null;
                        }
                        if (cls == null) {
                            try {
                                int i3 = SwitchCompat.a;
                                cls = SwitchCompat.class;
                            } catch (Exception unused2) {
                            }
                        }
                        if (childAt instanceof CheckBox) {
                            textView2 = (CheckBox) childAt;
                        } else {
                            if (cls == null || !cls.isInstance(childAt)) {
                                if (childAt instanceof RadioButton) {
                                    textView2 = (RadioButton) childAt;
                                } else if (childAt instanceof ToggleButton) {
                                    ToggleButton toggleButton = (ToggleButton) childAt;
                                    string = toggleButton.isChecked() ? toggleButton.getTextOn() : toggleButton.getTextOff();
                                } else {
                                    if (childAt instanceof Button) {
                                        textView = (Button) childAt;
                                    } else if (childAt instanceof CheckedTextView) {
                                        textView = (CheckedTextView) childAt;
                                    } else if (childAt instanceof TextView) {
                                        textView = (TextView) childAt;
                                    } else if (childAt instanceof ImageView) {
                                        ImageView imageView = (ImageView) childAt;
                                        if (!TextUtils.isEmpty(imageView.getContentDescription())) {
                                            string = imageView.getContentDescription().toString();
                                        }
                                    }
                                    string = textView.getText();
                                }
                                if (!TextUtils.isEmpty(string)) {
                                    sb.append(string.toString());
                                    sb.append("-");
                                }
                            } else {
                                text = (String) (((CompoundButton) childAt).isChecked() ? childAt.getClass().getMethod("getTextOn", new Class[0]) : childAt.getClass().getMethod("getTextOff", new Class[0])).invoke(childAt, new Object[0]);
                            }
                            string = text;
                            if (!TextUtils.isEmpty(string)) {
                                sb.append(string.toString());
                                sb.append("-");
                            }
                        }
                        text = textView2.getText();
                        string = text;
                        if (!TextUtils.isEmpty(string)) {
                            sb.append(string.toString());
                            sb.append("-");
                        }
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return sb.toString();
        }
    }

    public static String getViewId(View view, String str) {
        try {
            String str2 = (String) getTag(str, view, R.id.thinking_analytics_tag_view_id);
            try {
                return (!TextUtils.isEmpty(str2) || view.getId() == -1) ? str2 : view.getContext().getResources().getResourceEntryName(view.getId());
            } catch (Exception unused) {
                return str2;
            }
        } catch (Exception unused2) {
            return null;
        }
    }
}
