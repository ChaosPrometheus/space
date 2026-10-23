package kotlin.coroutines.jvm.internal;

import com.lbe.parallel.fm0;
import com.lbe.parallel.g20;
import com.lbe.parallel.hm;
import com.lbe.parallel.qz0;

/* JADX INFO: loaded from: classes.dex */
public abstract class SuspendLambda extends ContinuationImpl implements g20 {
    public final int d;

    public SuspendLambda(hm hmVar) {
        super(hmVar);
        this.d = 2;
    }

    @Override // com.lbe.parallel.g20
    public final int b() {
        return this.d;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final String toString() {
        if (this.a != null) {
            return super.toString();
        }
        qz0.a.getClass();
        String string = getClass().getGenericInterfaces()[0].toString();
        if (string.startsWith("kotlin.jvm.functions.")) {
            string = string.substring(21);
        }
        fm0.v(string, "renderLambdaToString(...)");
        return string;
    }
}
