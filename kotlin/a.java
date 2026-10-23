package kotlin;

import com.lbe.parallel.fm0;
import com.lbe.parallel.gv0;
import com.lbe.parallel.i10;
import com.lbe.parallel.vf0;
import com.lbe.parallel.yd0;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    public static void a(Throwable th, Throwable th2) {
        fm0.w(th, "<this>");
        fm0.w(th2, "exception");
        if (th != th2) {
            Integer num = yd0.a;
            if (num == null || num.intValue() >= 19) {
                th.addSuppressed(th2);
                return;
            }
            Method method = gv0.a;
            if (method != null) {
                method.invoke(th, th2);
            }
        }
    }

    public static vf0 b(i10 i10Var) {
        return new SynchronizedLazyImpl(i10Var);
    }
}
