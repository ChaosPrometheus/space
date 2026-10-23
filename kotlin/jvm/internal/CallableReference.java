package kotlin.jvm.internal;

import com.lbe.parallel.au0;
import com.lbe.parallel.hi;
import com.lbe.parallel.ii;
import com.lbe.parallel.nq;
import com.lbe.parallel.qz0;
import java.io.ObjectStreamException;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallableReference implements Serializable {
    public transient PropertyReference0 a;
    public final Object b;
    public final Class c = nq.class;
    public final String d = "classSimpleName";
    public final String e = "getClassSimpleName(Ljava/lang/Object;)Ljava/lang/String;";
    public final boolean f;

    public static class NoReceiver implements Serializable {
        public static final NoReceiver a = new NoReceiver();

        private NoReceiver() {
        }

        private Object readResolve() throws ObjectStreamException {
            return a;
        }
    }

    static {
        NoReceiver noReceiver = NoReceiver.a;
    }

    public CallableReference(Object obj, boolean z) {
        this.b = obj;
        this.f = z;
    }

    public final hi b() {
        hi iiVar;
        Class cls = this.c;
        if (cls == null) {
            return null;
        }
        if (this.f) {
            qz0.a.getClass();
            iiVar = new au0(cls);
        } else {
            qz0.a.getClass();
            iiVar = new ii(cls);
        }
        return iiVar;
    }
}
