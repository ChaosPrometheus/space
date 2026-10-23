package kotlin.collections;

import com.lbe.parallel.fm0;
import com.lbe.parallel.zj0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Pair;

/* JADX INFO: loaded from: classes.dex */
public abstract class b extends zj0 {
    public static Map E() {
        EmptyMap emptyMap = EmptyMap.a;
        fm0.u(emptyMap, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.emptyMap, V of kotlin.collections.MapsKt__MapsKt.emptyMap>");
        return emptyMap;
    }

    public static Map F(ArrayList arrayList) {
        int size = arrayList.size();
        if (size == 0) {
            return E();
        }
        if (size != 1) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(zj0.o(arrayList.size()));
            G(arrayList, linkedHashMap);
            return linkedHashMap;
        }
        Pair pair = (Pair) arrayList.get(0);
        fm0.w(pair, "pair");
        Map mapSingletonMap = Collections.singletonMap(pair.a, pair.b);
        fm0.v(mapSingletonMap, "singletonMap(...)");
        return mapSingletonMap;
    }

    public static final void G(ArrayList arrayList, LinkedHashMap linkedHashMap) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            linkedHashMap.put(pair.a, pair.b);
        }
    }
}
