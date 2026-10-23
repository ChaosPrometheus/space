package cn.thinkingdata.android;

import android.view.View;
import android.view.ViewGroup;
import cn.thinkingdata.android.utils.TDLog;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PathFinder {
    private static final String TAG = "ThinkingAnalytics.PathFinder";
    private final IntStack mIndexStack = new IntStack();

    public interface Accumulator {
        void accumulate(View view);
    }

    public static class IntStack {
        private static final int MAX_INDEX_STACK_SIZE = 256;
        private final int[] mStack = new int[256];
        private int mStackSize = 0;

        public int alloc() {
            int i = this.mStackSize;
            this.mStackSize = i + 1;
            this.mStack[i] = 0;
            return i;
        }

        public void free() {
            int i = this.mStackSize - 1;
            this.mStackSize = i;
            if (i < 0) {
                throw new ArrayIndexOutOfBoundsException(i);
            }
        }

        public boolean full() {
            return this.mStack.length == this.mStackSize;
        }

        public void increment(int i) {
            int[] iArr = this.mStack;
            iArr[i] = iArr[i] + 1;
        }

        public int read(int i) {
            return this.mStack[i];
        }
    }

    public static class PathElement {
        public static final int SHORTEST_PREFIX = 1;
        public static final int ZERO_LENGTH_PREFIX = 0;
        public final int index;
        public final int prefix;
        public final String viewClassName;
        public final int viewId;

        public PathElement(int i, String str, int i2, int i3) {
            this.prefix = i;
            this.viewClassName = str;
            this.index = i2;
            this.viewId = i3;
        }

        public String toString() {
            try {
                JSONObject jSONObject = new JSONObject();
                if (this.prefix == 1) {
                    jSONObject.put("prefix", "shortest");
                }
                String str = this.viewClassName;
                if (str != null) {
                    jSONObject.put("view_class", str);
                }
                int i = this.index;
                if (i > -1) {
                    jSONObject.put("index", i);
                }
                int i2 = this.viewId;
                if (i2 > -1) {
                    jSONObject.put("id", i2);
                }
                return jSONObject.toString();
            } catch (JSONException e) {
                throw new RuntimeException("Can't serialize PathElement to String", e);
            }
        }
    }

    private View findPrefixedMatch(PathElement pathElement, View view, int i) {
        View viewFindPrefixedMatch;
        int i2 = this.mIndexStack.read(i);
        if (matches(pathElement, view)) {
            this.mIndexStack.increment(i);
            int i3 = pathElement.index;
            if (i3 == -1 || i3 == i2) {
                return view;
            }
        }
        if (pathElement.prefix != 1 || !(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = viewGroup.getChildAt(i4);
            if (childAt != null && (viewFindPrefixedMatch = findPrefixedMatch(pathElement, childAt, i)) != null) {
                return viewFindPrefixedMatch;
            }
        }
        return null;
    }

    private void findTargetsInMatchedView(View view, List<PathElement> list, Accumulator accumulator) {
        if (list.isEmpty()) {
            accumulator.accumulate(view);
            return;
        }
        if (this.mIndexStack.full()) {
            TDLog.i(TAG, "Path is too deep, there is no memory to perfrom the finding");
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            PathElement pathElement = list.get(0);
            List<PathElement> listSubList = list.subList(1, list.size());
            int childCount = viewGroup.getChildCount();
            int iAlloc = this.mIndexStack.alloc();
            for (int i = 0; i < childCount; i++) {
                View viewFindPrefixedMatch = findPrefixedMatch(pathElement, viewGroup.getChildAt(i), iAlloc);
                if (viewFindPrefixedMatch != null) {
                    findTargetsInMatchedView(viewFindPrefixedMatch, listSubList, accumulator);
                }
                if (pathElement.index >= 0 && this.mIndexStack.read(iAlloc) > pathElement.index) {
                    break;
                }
            }
            this.mIndexStack.free();
        }
    }

    public static boolean hasClassName(Object obj, String str) {
        for (Class<?> superclass = obj.getClass(); superclass.getCanonicalName() != null; superclass = superclass.getSuperclass()) {
            if (superclass.getCanonicalName().equals(str)) {
                return true;
            }
            if (superclass == Object.class) {
                return false;
            }
        }
        return false;
    }

    private boolean matches(PathElement pathElement, View view) {
        String str = pathElement.viewClassName;
        if (str == null || hasClassName(view, str)) {
            return -1 == pathElement.viewId || view.getId() == pathElement.viewId;
        }
        return false;
    }

    public void findTargetsInRoot(View view, List<PathElement> list, Accumulator accumulator) {
        if (list.isEmpty()) {
            return;
        }
        if (this.mIndexStack.full()) {
            TDLog.i(TAG, "Path is too deep, there is no memory to perfrom the finding");
            return;
        }
        PathElement pathElement = list.get(0);
        List<PathElement> listSubList = list.subList(1, list.size());
        View viewFindPrefixedMatch = findPrefixedMatch(pathElement, view, this.mIndexStack.alloc());
        this.mIndexStack.free();
        if (viewFindPrefixedMatch != null) {
            findTargetsInMatchedView(viewFindPrefixedMatch, listSubList, accumulator);
        }
    }
}
