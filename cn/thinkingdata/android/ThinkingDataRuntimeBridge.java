package cn.thinkingdata.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.thinkingdata.android.utils.PropertyUtils;
import cn.thinkingdata.android.utils.TDConstants;
import cn.thinkingdata.android.utils.TDLog;
import cn.thinkingdata.android.utils.TDUtils;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ThinkingDataRuntimeBridge {
    private static final String TAG = "ThinkingAnalytics.ThinkingDataRuntimeBridge";

    private static boolean fragmentGetUserVisibleHint(Object obj) {
        try {
            Method method = obj.getClass().getMethod("getUserVisibleHint", new Class[0]);
            if (method != null) {
                return ((Boolean) method.invoke(obj, new Object[0])).booleanValue();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    private static boolean fragmentIsNotHidden(Object obj) {
        try {
            Method method = obj.getClass().getMethod("isHidden", new Class[0]);
            if (method != null) {
                return !((Boolean) method.invoke(obj, new Object[0])).booleanValue();
            }
        } catch (Exception unused) {
        }
        return true;
    }

    private static boolean fragmentIsResumed(Object obj) {
        try {
            Method method = obj.getClass().getMethod("isResumed", new Class[0]);
            if (method != null) {
                return ((Boolean) method.invoke(obj, new Object[0])).booleanValue();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    private static boolean isNotFragment(Object obj) {
        Class<Fragment> cls;
        Class<Fragment> cls2 = Fragment.class;
        try {
            int i = Fragment.a;
            cls = cls2;
        } catch (Exception unused) {
            cls = null;
        }
        try {
            int i2 = Fragment.a;
        } catch (Exception unused2) {
            cls2 = null;
        }
        if (cls == null && cls2 == null) {
            return true;
        }
        if (cls != null) {
            try {
                if (cls.isInstance(obj)) {
                    return false;
                }
                if (cls2 == null && cls2.isInstance(obj)) {
                    return false;
                }
            } catch (Exception unused3) {
            }
        } else if (cls2 == null) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isViewIgnored(ThinkingAnalyticsSDK thinkingAnalyticsSDK, View view) {
        if (view == null) {
            return true;
        }
        try {
            List<Class> ignoredViewTypeList = thinkingAnalyticsSDK.getIgnoredViewTypeList();
            if (ignoredViewTypeList != null) {
                Iterator<Class> it = ignoredViewTypeList.iterator();
                while (it.hasNext()) {
                    if (it.next().isAssignableFrom(view.getClass())) {
                        return true;
                    }
                }
            }
            return "1".equals(TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view, R.id.thinking_analytics_tag_view_ignored));
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void onAdapterViewItemClick(final View view, final View view2, final int i) {
        if (view == null || view2 == null || !(view instanceof AdapterView)) {
            return;
        }
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.6
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                Context context;
                try {
                    if (!thinkingAnalyticsSDK.isAutoTrackEnabled() || thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK) || (context = view2.getContext()) == null) {
                        return;
                    }
                    Activity activityFromContext = TDUtils.getActivityFromContext(context);
                    if ((activityFromContext == null || !thinkingAnalyticsSDK.isActivityAutoTrackAppClickIgnored(activityFromContext.getClass())) && !ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, view.getClass())) {
                        JSONObject jSONObject = new JSONObject();
                        if (thinkingAnalyticsSDK.getIgnoredViewTypeList() != null) {
                            View view3 = view;
                            if (view3 instanceof ListView) {
                                jSONObject.put(TDConstants.ELEMENT_TYPE, "ListView");
                                if (ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, ListView.class)) {
                                    return;
                                }
                            } else if (view3 instanceof GridView) {
                                jSONObject.put(TDConstants.ELEMENT_TYPE, "GridView");
                                if (ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, GridView.class)) {
                                    return;
                                }
                            } else if (view3 instanceof Spinner) {
                                jSONObject.put(TDConstants.ELEMENT_TYPE, "Spinner");
                                if (ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, Spinner.class)) {
                                    return;
                                }
                            }
                        }
                        Adapter adapter = ((AdapterView) view).getAdapter();
                        if (adapter instanceof ThinkingAdapterViewItemTrackProperties) {
                            try {
                                JSONObject thinkingItemTrackProperties = ((ThinkingAdapterViewItemTrackProperties) adapter).getThinkingItemTrackProperties(i);
                                if (thinkingItemTrackProperties != null && PropertyUtils.checkProperty(thinkingItemTrackProperties)) {
                                    TDUtils.mergeJSONObject(thinkingItemTrackProperties, jSONObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        TDUtils.addViewPathProperties(activityFromContext, view2, jSONObject);
                        String viewId = TDUtils.getViewId(view, thinkingAnalyticsSDK.getToken());
                        if (!TextUtils.isEmpty(viewId)) {
                            jSONObject.put(TDConstants.ELEMENT_ID, viewId);
                        }
                        if (activityFromContext != null) {
                            jSONObject.put(TDConstants.SCREEN_NAME, activityFromContext.getClass().getCanonicalName());
                            String activityTitle = TDUtils.getActivityTitle(activityFromContext);
                            if (!TextUtils.isEmpty(activityTitle)) {
                                jSONObject.put(TDConstants.TITLE, activityTitle);
                            }
                        }
                        jSONObject.put(TDConstants.ELEMENT_POSITION, String.valueOf(i));
                        View view4 = view2;
                        String strTraverseView = null;
                        if (view4 instanceof ViewGroup) {
                            try {
                                strTraverseView = TDUtils.traverseView(new StringBuilder(), (ViewGroup) view2);
                                if (!TextUtils.isEmpty(strTraverseView)) {
                                    strTraverseView = strTraverseView.substring(0, strTraverseView.length() - 1);
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        } else if (view4 instanceof TextView) {
                            strTraverseView = ((TextView) view4).getText().toString();
                        }
                        if (!TextUtils.isEmpty(strTraverseView)) {
                            jSONObject.put(TDConstants.ELEMENT_CONTENT, strTraverseView);
                        }
                        TDUtils.getFragmentNameFromView(view, jSONObject);
                        JSONObject jSONObject2 = (JSONObject) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view2, R.id.thinking_analytics_tag_view_properties);
                        if (jSONObject2 != null) {
                            TDUtils.mergeJSONObject(jSONObject2, jSONObject);
                        }
                        thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject);
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    TDLog.i(ThinkingDataRuntimeBridge.TAG, " AdapterView.OnItemClickListener.onItemClick AOP ERROR: " + e3.getMessage());
                }
            }
        });
    }

    public static void onDialogClick(Object obj, final int i) {
        if (obj instanceof Dialog) {
            final Dialog dialog = (Dialog) obj;
            ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.5
                @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
                public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                    Class<?> cls;
                    ListView listView;
                    Object item;
                    Object text;
                    try {
                        if (thinkingAnalyticsSDK.isAutoTrackEnabled() && !thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK)) {
                            Activity activityFromContext = TDUtils.getActivityFromContext(dialog.getContext());
                            if (activityFromContext == null) {
                                activityFromContext = dialog.getOwnerActivity();
                            }
                            if ((activityFromContext == null || !thinkingAnalyticsSDK.isActivityAutoTrackAppClickIgnored(activityFromContext.getClass())) && !ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, Dialog.class)) {
                                JSONObject jSONObject = new JSONObject();
                                try {
                                    if (dialog.getWindow() != null) {
                                        String str = (String) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), dialog.getWindow().getDecorView(), R.id.thinking_analytics_tag_view_id);
                                        if (!TextUtils.isEmpty(str)) {
                                            jSONObject.put(TDConstants.ELEMENT_ID, str);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (activityFromContext != null) {
                                    jSONObject.put(TDConstants.SCREEN_NAME, activityFromContext.getClass().getCanonicalName());
                                    String activityTitle = TDUtils.getActivityTitle(activityFromContext);
                                    if (!TextUtils.isEmpty(activityTitle)) {
                                        jSONObject.put(TDConstants.TITLE, activityTitle);
                                    }
                                }
                                jSONObject.put(TDConstants.ELEMENT_TYPE, "Dialog");
                                Button button = null;
                                try {
                                    cls = Class.forName("androidx.appcompat.app.AlertDialog)");
                                } catch (Exception unused) {
                                    cls = null;
                                }
                                if (cls == null) {
                                    try {
                                        int i2 = AlertDialog.a;
                                        cls = AlertDialog.class;
                                    } catch (Exception unused2) {
                                    }
                                }
                                Dialog dialog2 = dialog;
                                if (dialog2 instanceof android.app.AlertDialog) {
                                    android.app.AlertDialog alertDialog = (android.app.AlertDialog) dialog2;
                                    Button button2 = alertDialog.getButton(i);
                                    if (button2 == null) {
                                        ListView listView2 = alertDialog.getListView();
                                        if (listView2 != null && (text = listView2.getAdapter().getItem(i)) != null && (text instanceof String)) {
                                            jSONObject.put(TDConstants.ELEMENT_CONTENT, text);
                                        }
                                    } else if (!TextUtils.isEmpty(button2.getText())) {
                                        text = button2.getText();
                                        jSONObject.put(TDConstants.ELEMENT_CONTENT, text);
                                    }
                                } else if (cls != null && cls.isInstance(dialog2)) {
                                    try {
                                        Method method = dialog.getClass().getMethod("getButton", Integer.TYPE);
                                        if (method != null) {
                                            button = (Button) method.invoke(dialog, Integer.valueOf(i));
                                        }
                                    } catch (Exception unused3) {
                                    }
                                    if (button == null) {
                                        try {
                                            Method method2 = dialog.getClass().getMethod("getListView", new Class[0]);
                                            if (method2 != null && (listView = (ListView) method2.invoke(dialog, new Object[0])) != null && (item = listView.getAdapter().getItem(i)) != null && (item instanceof String)) {
                                                jSONObject.put(TDConstants.ELEMENT_CONTENT, item);
                                            }
                                        } catch (Exception unused4) {
                                        }
                                    } else if (!TextUtils.isEmpty(button.getText())) {
                                        text = button.getText();
                                        jSONObject.put(TDConstants.ELEMENT_CONTENT, text);
                                    }
                                }
                                thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject);
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        TDLog.i(ThinkingDataRuntimeBridge.TAG, " DialogInterface.OnClickListener.onClick AOP ERROR: " + e2.getMessage());
                    }
                }
            });
        }
    }

    public static void onExpandableListViewOnChildClick(final View view, final View view2, final int i, final int i2) {
        final Context context;
        if (view == null || (context = view.getContext()) == null) {
            return;
        }
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.4
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                Object obj;
                try {
                    if (thinkingAnalyticsSDK.isAutoTrackEnabled() && !thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK)) {
                        Activity activityFromContext = TDUtils.getActivityFromContext(context);
                        if ((activityFromContext != null && thinkingAnalyticsSDK.isActivityAutoTrackAppClickIgnored(activityFromContext.getClass())) || ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, ExpandableListView.class) || ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, view) || ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, view2)) {
                            return;
                        }
                        JSONObject jSONObject = new JSONObject();
                        TDUtils.addViewPathProperties(activityFromContext, view2, jSONObject);
                        if (activityFromContext != null) {
                            jSONObject.put(TDConstants.SCREEN_NAME, activityFromContext.getClass().getCanonicalName());
                            String activityTitle = TDUtils.getActivityTitle(activityFromContext);
                            if (!TextUtils.isEmpty(activityTitle)) {
                                jSONObject.put(TDConstants.TITLE, activityTitle);
                            }
                        }
                        String viewId = TDUtils.getViewId(view);
                        if (!TextUtils.isEmpty(viewId)) {
                            jSONObject.put(TDConstants.ELEMENT_ID, viewId);
                        }
                        if (i2 < 0) {
                            obj = String.format(Locale.CHINA, "%d", Integer.valueOf(i));
                        } else {
                            obj = String.format(Locale.CHINA, "%d:%d", Integer.valueOf(i), Integer.valueOf(i2));
                        }
                        jSONObject.put(TDConstants.ELEMENT_POSITION, obj);
                        jSONObject.put(TDConstants.ELEMENT_TYPE, "ExpandableListView");
                        View view3 = view2;
                        String strTraverseView = null;
                        if (view3 instanceof ViewGroup) {
                            try {
                                strTraverseView = TDUtils.traverseView(new StringBuilder(), (ViewGroup) view2);
                                if (!TextUtils.isEmpty(strTraverseView)) {
                                    strTraverseView = strTraverseView.substring(0, strTraverseView.length() - 1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (view3 instanceof TextView) {
                            strTraverseView = (String) ((TextView) view3).getText();
                        }
                        if (!TextUtils.isEmpty(strTraverseView)) {
                            jSONObject.put(TDConstants.ELEMENT_CONTENT, strTraverseView);
                        }
                        TDUtils.getFragmentNameFromView(view, jSONObject);
                        JSONObject jSONObject2 = (JSONObject) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view2, R.id.thinking_analytics_tag_view_properties);
                        if (jSONObject2 != null) {
                            TDUtils.mergeJSONObject(jSONObject2, jSONObject);
                        }
                        ExpandableListAdapter expandableListAdapter = ((ExpandableListView) view).getExpandableListAdapter();
                        if (expandableListAdapter != null && (expandableListAdapter instanceof ThinkingExpandableListViewItemTrackProperties)) {
                            try {
                                ThinkingExpandableListViewItemTrackProperties thinkingExpandableListViewItemTrackProperties = (ThinkingExpandableListViewItemTrackProperties) expandableListAdapter;
                                int i3 = i2;
                                JSONObject thinkingGroupItemTrackProperties = i3 < 0 ? thinkingExpandableListViewItemTrackProperties.getThinkingGroupItemTrackProperties(i) : thinkingExpandableListViewItemTrackProperties.getThinkingChildItemTrackProperties(i, i3);
                                if (thinkingGroupItemTrackProperties != null && PropertyUtils.checkProperty(thinkingGroupItemTrackProperties)) {
                                    TDUtils.mergeJSONObject(thinkingGroupItemTrackProperties, jSONObject);
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject);
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    TDLog.i(ThinkingDataRuntimeBridge.TAG, " ExpandableListView.OnChildClickListener.onGroupClick AOP ERROR: " + e3.getMessage());
                }
            }
        });
    }

    public static void onExpandableListViewOnGroupClick(View view, View view2, int i) {
        onExpandableListViewOnChildClick(view, view2, i, -1);
    }

    public static void onFragmentCreateView(Object obj, View view) {
        try {
            if (isNotFragment(obj)) {
                return;
            }
            String name = obj.getClass().getName();
            view.setTag(R.id.thinking_analytics_tag_view_fragment_name, name);
            if (view instanceof ViewGroup) {
                traverseView(name, (ViewGroup) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onFragmentHiddenChanged(Object obj, boolean z) {
        Object objInvoke;
        if (isNotFragment(obj)) {
            return;
        }
        try {
            Method method = obj.getClass().getMethod("getParentFragment", new Class[0]);
            objInvoke = method != null ? method.invoke(obj, new Object[0]) : null;
        } catch (Exception unused) {
        }
        if (z) {
            return;
        }
        if ((objInvoke == null && fragmentIsResumed(obj) && fragmentIsNotHidden(obj)) || (fragmentIsResumed(obj) && fragmentIsNotHidden(obj) && fragmentGetUserVisibleHint(obj))) {
            trackFragmentViewScreen(obj);
        }
    }

    public static void onFragmentOnResume(Object obj) {
        Object objInvoke;
        if (isNotFragment(obj)) {
            return;
        }
        try {
            Method method = obj.getClass().getMethod("getParentFragment", new Class[0]);
            objInvoke = method != null ? method.invoke(obj, new Object[0]) : null;
        } catch (Exception unused) {
        }
        if (objInvoke == null) {
            if (!fragmentIsNotHidden(obj) || !fragmentGetUserVisibleHint(obj)) {
                return;
            }
        } else if (!fragmentIsNotHidden(obj) || !fragmentGetUserVisibleHint(obj) || !fragmentIsNotHidden(objInvoke) || !fragmentGetUserVisibleHint(objInvoke)) {
            return;
        }
        trackFragmentViewScreen(obj);
    }

    public static void onFragmentSetUserVisibleHint(Object obj, boolean z) {
        Object objInvoke;
        if (isNotFragment(obj)) {
            return;
        }
        try {
            Method method = obj.getClass().getMethod("getParentFragment", new Class[0]);
            objInvoke = method != null ? method.invoke(obj, new Object[0]) : null;
        } catch (Exception unused) {
        }
        if (z) {
            if ((objInvoke == null && fragmentIsResumed(obj) && fragmentIsNotHidden(obj)) || (fragmentIsResumed(obj) && fragmentIsNotHidden(obj) && fragmentGetUserVisibleHint(obj))) {
                trackFragmentViewScreen(obj);
            }
        }
    }

    public static void onMenuItemSelected(final Object obj, final MenuItem menuItem) {
        if (menuItem == null) {
            return;
        }
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.7
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                Object obj2;
                try {
                    if (!thinkingAnalyticsSDK.isAutoTrackEnabled() || thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK) || ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, MenuItem.class) || (obj2 = obj) == null) {
                        return;
                    }
                    String resourceEntryName = null;
                    Context context = obj2 instanceof Context ? (Context) obj2 : null;
                    if (context == null) {
                        return;
                    }
                    Activity activityFromContext = TDUtils.getActivityFromContext(context);
                    if (activityFromContext == null || !thinkingAnalyticsSDK.isActivityAutoTrackAppClickIgnored(activityFromContext.getClass())) {
                        try {
                            resourceEntryName = context.getResources().getResourceEntryName(menuItem.getItemId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONObject jSONObject = new JSONObject();
                        if (activityFromContext != null) {
                            jSONObject.put(TDConstants.SCREEN_NAME, activityFromContext.getClass().getCanonicalName());
                            String activityTitle = TDUtils.getActivityTitle(activityFromContext);
                            if (!TextUtils.isEmpty(activityTitle)) {
                                jSONObject.put(TDConstants.TITLE, activityTitle);
                            }
                        }
                        if (!TextUtils.isEmpty(resourceEntryName)) {
                            jSONObject.put(TDConstants.ELEMENT_ID, resourceEntryName);
                        }
                        if (!TextUtils.isEmpty(menuItem.getTitle())) {
                            jSONObject.put(TDConstants.ELEMENT_CONTENT, menuItem.getTitle());
                        }
                        jSONObject.put(TDConstants.ELEMENT_TYPE, "MenuItem");
                        thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    TDLog.i(ThinkingDataRuntimeBridge.TAG, "track MenuItem click error: " + e2.getMessage());
                }
            }
        });
    }

    public static void onTabHostChanged(final String str) {
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.8
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                try {
                    if (!thinkingAnalyticsSDK.isAutoTrackEnabled() || thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK) || ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, TabHost.class)) {
                        return;
                    }
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(TDConstants.ELEMENT_CONTENT, str);
                    jSONObject.put(TDConstants.ELEMENT_TYPE, "TabHost");
                    thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    TDLog.i(ThinkingDataRuntimeBridge.TAG, " onTabChanged AOP ERROR: " + e.getMessage());
                }
            }
        });
    }

    public static void onViewOnClick(final View view, final Object obj) {
        if (view == null) {
            return;
        }
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.3
            /* JADX WARN: Code duplicated, block: B:162:0x0295 A[Catch: Exception -> 0x003d, PHI: r3 r9
  0x0295: PHI (r3v15 android.view.View) = (r3v14 android.view.View), (r3v16 android.view.View) binds: [B:167:0x02aa, B:161:0x0293] A[DONT_GENERATE, DONT_INLINE]
  0x0295: PHI (r9v21 java.lang.Object) = (r9v20 java.lang.Object), (r9v22 java.lang.Object) binds: [B:167:0x02aa, B:161:0x0293] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #3 {Exception -> 0x003d, blocks: (B:3:0x000a, B:6:0x0011, B:9:0x001a, B:11:0x001e, B:13:0x0022, B:15:0x002e, B:21:0x0041, B:23:0x0045, B:25:0x0051, B:28:0x0060, B:38:0x008e, B:40:0x00a9, B:43:0x00b4, B:46:0x00bd, B:49:0x00d9, B:52:0x00e0, B:55:0x00f7, B:71:0x0111, B:73:0x0121, B:208:0x038a, B:211:0x0392, B:213:0x039b, B:215:0x03b3, B:217:0x03b8, B:76:0x012d, B:78:0x0133, B:80:0x013d, B:82:0x0145, B:86:0x015a, B:83:0x014c, B:85:0x0154, B:90:0x016d, B:110:0x01db, B:111:0x01e0, B:113:0x01e6, B:115:0x01ee, B:117:0x01f7, B:120:0x01ff, B:116:0x01f3, B:121:0x0205, B:123:0x0209, B:134:0x0231, B:136:0x0235, B:138:0x023f, B:140:0x0243, B:143:0x024d, B:144:0x0253, B:145:0x0259, B:147:0x025d, B:149:0x0267, B:151:0x026b, B:153:0x0275, B:155:0x0279, B:157:0x0283, B:159:0x0287, B:162:0x0295, B:169:0x02ad, B:163:0x029a, B:165:0x029e, B:170:0x02b2, B:172:0x02b6, B:174:0x02c3, B:176:0x02c7, B:178:0x02d4, B:188:0x030c, B:190:0x0310, B:192:0x0322, B:199:0x035d, B:195:0x0334, B:197:0x0338, B:200:0x0362, B:37:0x008b, B:30:0x0078, B:34:0x0086), top: B:229:0x000a, inners: #6 }] */
            /* JADX WARN: Code duplicated, block: B:210:0x0390  */
            /* JADX WARN: Code duplicated, block: B:215:0x03b3 A[Catch: Exception -> 0x003d, TRY_LEAVE, TryCatch #3 {Exception -> 0x003d, blocks: (B:3:0x000a, B:6:0x0011, B:9:0x001a, B:11:0x001e, B:13:0x0022, B:15:0x002e, B:21:0x0041, B:23:0x0045, B:25:0x0051, B:28:0x0060, B:38:0x008e, B:40:0x00a9, B:43:0x00b4, B:46:0x00bd, B:49:0x00d9, B:52:0x00e0, B:55:0x00f7, B:71:0x0111, B:73:0x0121, B:208:0x038a, B:211:0x0392, B:213:0x039b, B:215:0x03b3, B:217:0x03b8, B:76:0x012d, B:78:0x0133, B:80:0x013d, B:82:0x0145, B:86:0x015a, B:83:0x014c, B:85:0x0154, B:90:0x016d, B:110:0x01db, B:111:0x01e0, B:113:0x01e6, B:115:0x01ee, B:117:0x01f7, B:120:0x01ff, B:116:0x01f3, B:121:0x0205, B:123:0x0209, B:134:0x0231, B:136:0x0235, B:138:0x023f, B:140:0x0243, B:143:0x024d, B:144:0x0253, B:145:0x0259, B:147:0x025d, B:149:0x0267, B:151:0x026b, B:153:0x0275, B:155:0x0279, B:157:0x0283, B:159:0x0287, B:162:0x0295, B:169:0x02ad, B:163:0x029a, B:165:0x029e, B:170:0x02b2, B:172:0x02b6, B:174:0x02c3, B:176:0x02c7, B:178:0x02d4, B:188:0x030c, B:190:0x0310, B:192:0x0322, B:199:0x035d, B:195:0x0334, B:197:0x0338, B:200:0x0362, B:37:0x008b, B:30:0x0078, B:34:0x0086), top: B:229:0x000a, inners: #6 }] */
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                Class<SwitchCompat> cls;
                Class<ViewPager> cls2;
                StringBuilder sb;
                Object obj2;
                View view2;
                CharSequence charSequenceSubstring;
                JSONObject jSONObject;
                Class<ViewPager> cls3 = ViewPager.class;
                Class<SwitchCompat> cls4 = SwitchCompat.class;
                try {
                    if (thinkingAnalyticsSDK.isAutoTrackEnabled() && !thinkingAnalyticsSDK.isAutoTrackEventTypeIgnored(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK)) {
                        Object obj3 = obj;
                        if (obj3 != null) {
                            if (obj3 instanceof ThinkingDataIgnoreTrackOnClick) {
                                ThinkingDataIgnoreTrackOnClick thinkingDataIgnoreTrackOnClick = (ThinkingDataIgnoreTrackOnClick) obj3;
                                if (TextUtils.isEmpty(thinkingDataIgnoreTrackOnClick.appId())) {
                                    return;
                                }
                                if (thinkingAnalyticsSDK.getToken().equals(thinkingDataIgnoreTrackOnClick.appId())) {
                                    return;
                                }
                            } else if (obj3 instanceof ThinkingDataTrackViewOnClick) {
                                ThinkingDataTrackViewOnClick thinkingDataTrackViewOnClick = (ThinkingDataTrackViewOnClick) obj3;
                                if (!TextUtils.isEmpty(thinkingDataTrackViewOnClick.appId()) && !thinkingAnalyticsSDK.getToken().equals(thinkingDataTrackViewOnClick.appId())) {
                                    return;
                                }
                            }
                        }
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        String str = (String) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view, R.id.thinking_analytics_tag_view_onclick_timestamp);
                        if (!TextUtils.isEmpty(str)) {
                            try {
                                if (jCurrentTimeMillis - Long.parseLong(str) < 500) {
                                    TDLog.i(ThinkingDataRuntimeBridge.TAG, "This onClick maybe extends from super, IGNORE");
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        TDUtils.setTag(thinkingAnalyticsSDK.getToken(), view, R.id.thinking_analytics_tag_view_onclick_timestamp, String.valueOf(jCurrentTimeMillis));
                        Activity activityFromContext = TDUtils.getActivityFromContext(view.getContext());
                        if ((activityFromContext == null || !thinkingAnalyticsSDK.isActivityAutoTrackAppClickIgnored(activityFromContext.getClass())) && !ThinkingDataRuntimeBridge.isViewIgnored(thinkingAnalyticsSDK, view)) {
                            JSONObject jSONObject2 = new JSONObject();
                            TDUtils.addViewPathProperties(activityFromContext, view, jSONObject2);
                            String viewId = TDUtils.getViewId(view, thinkingAnalyticsSDK.getToken());
                            if (!TextUtils.isEmpty(viewId)) {
                                jSONObject2.put(TDConstants.ELEMENT_ID, viewId);
                            }
                            if (activityFromContext != null) {
                                jSONObject2.put(TDConstants.SCREEN_NAME, activityFromContext.getClass().getCanonicalName());
                                String activityTitle = TDUtils.getActivityTitle(activityFromContext);
                                if (!TextUtils.isEmpty(activityTitle)) {
                                    jSONObject2.put(TDConstants.TITLE, activityTitle);
                                }
                            }
                            CharSequence charSequenceValueOf = null;
                            strTraverseView = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            strTraverseView = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            charSequenceValueOf = null;
                            strTraverseView = null;
                            String strTraverseView = null;
                            try {
                                int i = SwitchCompat.a;
                                cls = cls4;
                            } catch (Exception unused) {
                                cls = null;
                            }
                            if (cls == null) {
                                try {
                                    int i2 = SwitchCompat.a;
                                } catch (Exception unused2) {
                                    cls4 = cls;
                                }
                            } else {
                                cls4 = cls;
                            }
                            try {
                                int i3 = ViewPager.SCROLL_STATE_IDLE;
                                cls2 = cls3;
                            } catch (Exception unused3) {
                                cls2 = null;
                            }
                            if (cls2 == null) {
                                try {
                                    int i4 = ViewPager.SCROLL_STATE_IDLE;
                                } catch (Exception unused4) {
                                    cls3 = cls2;
                                }
                            } else {
                                cls3 = cls2;
                            }
                            Object canonicalName = view.getClass().getCanonicalName();
                            View view3 = view;
                            if (view3 instanceof CheckBox) {
                                canonicalName = "CheckBox";
                                charSequenceValueOf = ((CheckBox) view3).getText();
                            } else {
                                if (cls4 == null || !cls4.isInstance(view3)) {
                                    if (cls3 == null || !cls3.isInstance(view)) {
                                        View view4 = view;
                                        if (view4 instanceof Switch) {
                                            Switch r3 = (Switch) view4;
                                            CharSequence textOn = r3.isChecked() ? r3.getTextOn() : r3.getTextOff();
                                            boolean zIsEmpty = TextUtils.isEmpty(textOn);
                                            canonicalName = "SwitchButton";
                                            charSequenceSubstring = textOn;
                                            if (zIsEmpty) {
                                                charSequenceSubstring = r3.getText();
                                            }
                                        } else if (view4 instanceof RadioGroup) {
                                            int checkedRadioButtonId = ((RadioGroup) view4).getCheckedRadioButtonId();
                                            canonicalName = "RadioGroup";
                                            if (activityFromContext != null) {
                                                try {
                                                    RadioButton radioButton = (RadioButton) activityFromContext.findViewById(checkedRadioButtonId);
                                                    if (radioButton != null && !TextUtils.isEmpty(radioButton.getText())) {
                                                        charSequenceValueOf = radioButton.getText().toString();
                                                    }
                                                } catch (Exception e2) {
                                                    e = e2;
                                                    e.printStackTrace();
                                                    charSequenceValueOf = strTraverseView;
                                                }
                                            }
                                        } else if (view4 instanceof RadioButton) {
                                            canonicalName = "RadioButton";
                                            charSequenceValueOf = ((RadioButton) view4).getText();
                                        } else if (view4 instanceof ToggleButton) {
                                            ToggleButton toggleButton = (ToggleButton) view4;
                                            canonicalName = "ToggleButton";
                                            charSequenceValueOf = toggleButton.isChecked() ? toggleButton.getTextOn() : toggleButton.getTextOff();
                                        } else if (view4 instanceof Button) {
                                            canonicalName = "Button";
                                            charSequenceValueOf = ((Button) view4).getText();
                                        } else if (view4 instanceof CheckedTextView) {
                                            canonicalName = "CheckedTextView";
                                            charSequenceValueOf = ((CheckedTextView) view4).getText();
                                        } else if (view4 instanceof TextView) {
                                            canonicalName = "TextView";
                                            charSequenceValueOf = ((TextView) view4).getText();
                                        } else if (view4 instanceof ImageButton) {
                                            view2 = (ImageButton) view4;
                                            canonicalName = "ImageButton";
                                            if (!TextUtils.isEmpty(view2.getContentDescription())) {
                                                charSequenceValueOf = view2.getContentDescription().toString();
                                            }
                                        } else if (view4 instanceof ImageView) {
                                            view2 = (ImageView) view4;
                                            canonicalName = "ImageView";
                                            if (!TextUtils.isEmpty(view2.getContentDescription())) {
                                                charSequenceValueOf = view2.getContentDescription().toString();
                                            }
                                        } else if (view4 instanceof RatingBar) {
                                            canonicalName = "RatingBar";
                                            charSequenceValueOf = String.valueOf(((RatingBar) view4).getRating());
                                        } else if (view4 instanceof SeekBar) {
                                            canonicalName = "SeekBar";
                                            charSequenceValueOf = String.valueOf(((SeekBar) view4).getProgress());
                                        } else if (view4 instanceof Spinner) {
                                            canonicalName = "Spinner";
                                            try {
                                                String strTraverseView2 = TDUtils.traverseView(new StringBuilder(), (ViewGroup) view);
                                                boolean zIsEmpty2 = TextUtils.isEmpty(strTraverseView2);
                                                strTraverseView = strTraverseView2;
                                                if (!zIsEmpty2) {
                                                    strTraverseView = strTraverseView2.toString().substring(0, strTraverseView2.length() - 1);
                                                }
                                                jSONObject2.put(TDConstants.ELEMENT_POSITION, ((Spinner) view).getSelectedItemPosition());
                                                charSequenceValueOf = strTraverseView;
                                            } catch (Exception e3) {
                                                e = e3;
                                                e.printStackTrace();
                                                charSequenceValueOf = strTraverseView;
                                            }
                                        } else {
                                            if (view4 instanceof TimePicker) {
                                                sb = new StringBuilder();
                                                sb.append(((TimePicker) view).getCurrentHour());
                                                sb.append(":");
                                                sb.append(((TimePicker) view).getCurrentMinute());
                                                obj2 = "TimePicker";
                                            } else if (view4 instanceof DatePicker) {
                                                DatePicker datePicker = (DatePicker) view4;
                                                sb = new StringBuilder();
                                                sb.append(datePicker.getYear());
                                                sb.append("-");
                                                sb.append(datePicker.getMonth());
                                                sb.append("-");
                                                sb.append(datePicker.getDayOfMonth());
                                                obj2 = "DatePicker";
                                            } else if (view4 instanceof ViewGroup) {
                                                try {
                                                    strTraverseView = TDUtils.traverseView(new StringBuilder(), (ViewGroup) view);
                                                    boolean zIsEmpty3 = TextUtils.isEmpty(strTraverseView);
                                                    charSequenceValueOf = strTraverseView;
                                                    if (!zIsEmpty3) {
                                                        charSequenceSubstring = strTraverseView.toString().substring(0, strTraverseView.length() - 1);
                                                    }
                                                } catch (Exception e4) {
                                                    e = e4;
                                                    e.printStackTrace();
                                                    charSequenceValueOf = strTraverseView;
                                                }
                                            }
                                            canonicalName = obj2;
                                            charSequenceSubstring = sb.toString();
                                        }
                                    } else {
                                        canonicalName = "ViewPager";
                                        try {
                                            Method method = view.getClass().getMethod("getAdapter", new Class[0]);
                                            if (method != null) {
                                                Object objInvoke = method.invoke(view, new Object[0]);
                                                Method method2 = view.getClass().getMethod("getCurrentItem", new Class[0]);
                                                if (method2 != null) {
                                                    Integer num = (Integer) method2.invoke(view, new Object[0]);
                                                    num.intValue();
                                                    jSONObject2.put(TDConstants.ELEMENT_POSITION, String.format(Locale.CHINA, "%d", num));
                                                    Method method3 = objInvoke.getClass().getMethod("getPageTitle", Integer.TYPE);
                                                    if (method3 != null) {
                                                        charSequenceValueOf = (String) method3.invoke(objInvoke, num);
                                                    }
                                                }
                                            }
                                        } catch (Exception e5) {
                                            e = e5;
                                            e.printStackTrace();
                                            charSequenceValueOf = strTraverseView;
                                        }
                                    }
                                    if (!TextUtils.isEmpty(charSequenceSubstring)) {
                                        jSONObject2.put(TDConstants.ELEMENT_CONTENT, charSequenceSubstring.toString());
                                    }
                                    jSONObject2.put(TDConstants.ELEMENT_TYPE, canonicalName);
                                    TDUtils.getFragmentNameFromView(view, jSONObject2);
                                    jSONObject = (JSONObject) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view, R.id.thinking_analytics_tag_view_properties);
                                    if (jSONObject != null) {
                                        TDUtils.mergeJSONObject(jSONObject, jSONObject2);
                                    }
                                    thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject2);
                                }
                                canonicalName = "SwitchCompat";
                                charSequenceValueOf = (String) (((CompoundButton) view).isChecked() ? view.getClass().getMethod("getTextOn", new Class[0]) : view.getClass().getMethod("getTextOff", new Class[0])).invoke(view, new Object[0]);
                            }
                            charSequenceSubstring = charSequenceValueOf;
                            if (!TextUtils.isEmpty(charSequenceSubstring)) {
                                jSONObject2.put(TDConstants.ELEMENT_CONTENT, charSequenceSubstring.toString());
                            }
                            jSONObject2.put(TDConstants.ELEMENT_TYPE, canonicalName);
                            TDUtils.getFragmentNameFromView(view, jSONObject2);
                            jSONObject = (JSONObject) TDUtils.getTag(thinkingAnalyticsSDK.getToken(), view, R.id.thinking_analytics_tag_view_properties);
                            if (jSONObject != null) {
                                TDUtils.mergeJSONObject(jSONObject, jSONObject2);
                            }
                            thinkingAnalyticsSDK.autoTrack(TDConstants.APP_CLICK_EVENT_NAME, jSONObject2);
                        }
                    }
                } catch (Exception e6) {
                    TDLog.e(ThinkingDataRuntimeBridge.TAG, "onViewClickMethod error: " + e6.toString());
                    e6.printStackTrace();
                }
            }
        });
    }

    public static void trackEvent(Object obj) {
        if (obj instanceof ThinkingDataTrackEvent) {
            ThinkingDataTrackEvent thinkingDataTrackEvent = (ThinkingDataTrackEvent) obj;
            final String strEventName = thinkingDataTrackEvent.eventName();
            String strProperties = thinkingDataTrackEvent.properties();
            final String strAppId = thinkingDataTrackEvent.appId();
            if (TextUtils.isEmpty(strEventName)) {
                return;
            }
            final JSONObject jSONObject = new JSONObject();
            if (!TextUtils.isEmpty(strProperties)) {
                try {
                    TDUtils.mergeJSONObject(new JSONObject(strProperties), jSONObject);
                } catch (JSONException e) {
                    TDLog.e(TAG, "Exception occurred in trackEvent");
                    e.printStackTrace();
                }
            }
            ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.2
                @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
                public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                    if (thinkingAnalyticsSDK.isAutoTrackEnabled()) {
                        if (TextUtils.isEmpty(strAppId) || thinkingAnalyticsSDK.getToken().equals(strAppId)) {
                            thinkingAnalyticsSDK.track(strEventName, jSONObject);
                        }
                    }
                }
            });
        }
    }

    private static void trackFragmentViewScreen(final Object obj) {
        ThinkingAnalyticsSDK.allInstances(new ThinkingAnalyticsSDK.InstanceProcessor() { // from class: cn.thinkingdata.android.ThinkingDataRuntimeBridge.1
            /* JADX WARN: Code duplicated, block: B:29:0x0082 A[Catch: JSONException -> 0x00f9, TRY_ENTER, TryCatch #1 {JSONException -> 0x00f9, blocks: (B:20:0x005e, B:26:0x007b, B:29:0x0082, B:31:0x00a7, B:33:0x00ad, B:35:0x00b9, B:46:0x00ef, B:36:0x00bd, B:38:0x00cb, B:40:0x00d5, B:42:0x00e3, B:49:0x00f5, B:30:0x00a4, B:24:0x0071), top: B:56:0x005e }] */
            /* JADX WARN: Code duplicated, block: B:30:0x00a4 A[Catch: JSONException -> 0x00f9, TryCatch #1 {JSONException -> 0x00f9, blocks: (B:20:0x005e, B:26:0x007b, B:29:0x0082, B:31:0x00a7, B:33:0x00ad, B:35:0x00b9, B:46:0x00ef, B:36:0x00bd, B:38:0x00cb, B:40:0x00d5, B:42:0x00e3, B:49:0x00f5, B:30:0x00a4, B:24:0x0071), top: B:56:0x005e }] */
            /* JADX WARN: Code duplicated, block: B:33:0x00ad A[Catch: JSONException -> 0x00f9, TryCatch #1 {JSONException -> 0x00f9, blocks: (B:20:0x005e, B:26:0x007b, B:29:0x0082, B:31:0x00a7, B:33:0x00ad, B:35:0x00b9, B:46:0x00ef, B:36:0x00bd, B:38:0x00cb, B:40:0x00d5, B:42:0x00e3, B:49:0x00f5, B:30:0x00a4, B:24:0x0071), top: B:56:0x005e }] */
            /* JADX WARN: Code duplicated, block: B:35:0x00b9 A[Catch: JSONException -> 0x00f9, TryCatch #1 {JSONException -> 0x00f9, blocks: (B:20:0x005e, B:26:0x007b, B:29:0x0082, B:31:0x00a7, B:33:0x00ad, B:35:0x00b9, B:46:0x00ef, B:36:0x00bd, B:38:0x00cb, B:40:0x00d5, B:42:0x00e3, B:49:0x00f5, B:30:0x00a4, B:24:0x0071), top: B:56:0x005e }] */
            /* JADX WARN: Code duplicated, block: B:36:0x00bd A[Catch: JSONException -> 0x00f9, TryCatch #1 {JSONException -> 0x00f9, blocks: (B:20:0x005e, B:26:0x007b, B:29:0x0082, B:31:0x00a7, B:33:0x00ad, B:35:0x00b9, B:46:0x00ef, B:36:0x00bd, B:38:0x00cb, B:40:0x00d5, B:42:0x00e3, B:49:0x00f5, B:30:0x00a4, B:24:0x0071), top: B:56:0x005e }] */
            /* JADX WARN: Instruction removed from duplicated block: B:29:0x0082, please report this as an issue */
            @Override // cn.thinkingdata.android.ThinkingAnalyticsSDK.InstanceProcessor
            public void process(ThinkingAnalyticsSDK thinkingAnalyticsSDK) {
                Activity activity;
                Object obj2;
                ThinkingDataAutoTrackAppViewScreenUrl thinkingDataAutoTrackAppViewScreenUrl;
                JSONObject trackProperties;
                if (thinkingAnalyticsSDK.isTrackFragmentAppViewScreenEnabled()) {
                    ThinkingDataIgnoreTrackAppViewScreen thinkingDataIgnoreTrackAppViewScreen = (ThinkingDataIgnoreTrackAppViewScreen) obj.getClass().getAnnotation(ThinkingDataIgnoreTrackAppViewScreen.class);
                    if (thinkingDataIgnoreTrackAppViewScreen == null || !(TextUtils.isEmpty(thinkingDataIgnoreTrackAppViewScreen.appId()) || thinkingAnalyticsSDK.getToken().equals(thinkingDataIgnoreTrackAppViewScreen.appId()))) {
                        JSONObject jSONObject = new JSONObject();
                        String canonicalName = obj.getClass().getCanonicalName();
                        try {
                            Method method = obj.getClass().getMethod("getActivity", new Class[0]);
                            activity = method != null ? (Activity) method.invoke(obj, new Object[0]) : null;
                        } catch (Exception unused) {
                        }
                        try {
                            String titleFromFragment = TDUtils.getTitleFromFragment(obj, thinkingAnalyticsSDK.getToken());
                            if (TextUtils.isEmpty(titleFromFragment)) {
                                if (activity != null) {
                                    titleFromFragment = TDUtils.getActivityTitle(activity);
                                    if (!TextUtils.isEmpty(titleFromFragment)) {
                                    }
                                }
                                if (activity != null) {
                                    Locale locale = Locale.CHINA;
                                    jSONObject.put(TDConstants.SCREEN_NAME, activity.getClass().getCanonicalName() + "|" + canonicalName);
                                } else {
                                    jSONObject.put(TDConstants.SCREEN_NAME, canonicalName);
                                }
                                obj2 = obj;
                                if (!(obj2 instanceof ScreenAutoTracker)) {
                                    ScreenAutoTracker screenAutoTracker = (ScreenAutoTracker) obj2;
                                    canonicalName = screenAutoTracker.getScreenUrl();
                                    trackProperties = screenAutoTracker.getTrackProperties();
                                    if (trackProperties != null) {
                                        TDUtils.mergeJSONObject(trackProperties, jSONObject);
                                    }
                                } else {
                                    thinkingDataAutoTrackAppViewScreenUrl = (ThinkingDataAutoTrackAppViewScreenUrl) obj2.getClass().getAnnotation(ThinkingDataAutoTrackAppViewScreenUrl.class);
                                    if (thinkingDataAutoTrackAppViewScreenUrl != null || (!TextUtils.isEmpty(thinkingDataAutoTrackAppViewScreenUrl.appId()) && !thinkingAnalyticsSDK.getToken().equals(thinkingDataAutoTrackAppViewScreenUrl.appId()))) {
                                        thinkingAnalyticsSDK.autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
                                        return;
                                    } else {
                                        String strUrl = thinkingDataAutoTrackAppViewScreenUrl.url();
                                        if (!TextUtils.isEmpty(strUrl)) {
                                            canonicalName = strUrl;
                                        }
                                    }
                                }
                                thinkingAnalyticsSDK.trackViewScreenInternal(canonicalName, jSONObject);
                            }
                            jSONObject.put(TDConstants.TITLE, titleFromFragment);
                            if (activity != null) {
                                Locale locale2 = Locale.CHINA;
                                jSONObject.put(TDConstants.SCREEN_NAME, activity.getClass().getCanonicalName() + "|" + canonicalName);
                            } else {
                                jSONObject.put(TDConstants.SCREEN_NAME, canonicalName);
                            }
                            obj2 = obj;
                            if (!(obj2 instanceof ScreenAutoTracker)) {
                                thinkingDataAutoTrackAppViewScreenUrl = (ThinkingDataAutoTrackAppViewScreenUrl) obj2.getClass().getAnnotation(ThinkingDataAutoTrackAppViewScreenUrl.class);
                                if (thinkingDataAutoTrackAppViewScreenUrl != null) {
                                }
                                thinkingAnalyticsSDK.autoTrack(TDConstants.APP_VIEW_EVENT_NAME, jSONObject);
                                return;
                            } else {
                                ScreenAutoTracker screenAutoTracker2 = (ScreenAutoTracker) obj2;
                                canonicalName = screenAutoTracker2.getScreenUrl();
                                trackProperties = screenAutoTracker2.getTrackProperties();
                                if (trackProperties != null) {
                                    TDUtils.mergeJSONObject(trackProperties, jSONObject);
                                }
                            }
                            thinkingAnalyticsSDK.trackViewScreenInternal(canonicalName, jSONObject);
                        } catch (JSONException unused2) {
                            TDLog.d(ThinkingDataRuntimeBridge.TAG, "JSONException occurred when track fragment events");
                        }
                    }
                }
            }
        });
    }

    private static void traverseView(String str, ViewGroup viewGroup) {
        try {
            if (TextUtils.isEmpty(str) || viewGroup == null) {
                return;
            }
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                childAt.setTag(R.id.thinking_analytics_tag_view_fragment_name, str);
                if (childAt instanceof ViewGroup) {
                    traverseView(str, (ViewGroup) childAt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isViewIgnored(ThinkingAnalyticsSDK thinkingAnalyticsSDK, Class cls) {
        if (cls == null) {
            return true;
        }
        try {
            List<Class> ignoredViewTypeList = thinkingAnalyticsSDK.getIgnoredViewTypeList();
            if (ignoredViewTypeList == null) {
                return false;
            }
            Iterator<Class> it = ignoredViewTypeList.iterator();
            while (it.hasNext()) {
                if (it.next().isAssignableFrom(cls)) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }
}
