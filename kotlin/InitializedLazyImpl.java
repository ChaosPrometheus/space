package kotlin;

import com.lbe.parallel.vf0;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public final class InitializedLazyImpl<T> implements vf0, Serializable {
    public final Object a;

    public InitializedLazyImpl(Object obj) {
        this.a = obj;
    }

    @Override // com.lbe.parallel.vf0
    public final Object getValue() {
        throw null;
    }

    public final String toString() {
        return String.valueOf(this.a);
    }
}
