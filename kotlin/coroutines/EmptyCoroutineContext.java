package kotlin.coroutines;

import com.lbe.parallel.an;
import com.lbe.parallel.fm0;
import com.lbe.parallel.x10;
import com.lbe.parallel.ym;
import com.lbe.parallel.zm;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public final class EmptyCoroutineContext implements an, Serializable {
    public static final EmptyCoroutineContext a = new EmptyCoroutineContext();
    private static final long serialVersionUID = 0;

    private EmptyCoroutineContext() {
    }

    private final Object readResolve() {
        return a;
    }

    @Override // com.lbe.parallel.an
    public final Object b(Object obj, x10 x10Var) {
        fm0.w(x10Var, "operation");
        return obj;
    }

    @Override // com.lbe.parallel.an
    public final ym c(zm zmVar) {
        fm0.w(zmVar, "key");
        return null;
    }

    @Override // com.lbe.parallel.an
    public final an e(an anVar) {
        fm0.w(anVar, "context");
        return anVar;
    }

    @Override // com.lbe.parallel.an
    public final an f(zm zmVar) {
        fm0.w(zmVar, "key");
        return this;
    }

    public final int hashCode() {
        return 0;
    }

    public final String toString() {
        return "EmptyCoroutineContext";
    }
}
