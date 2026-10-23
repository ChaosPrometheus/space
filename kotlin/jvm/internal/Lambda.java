package kotlin.jvm.internal;

import com.lbe.parallel.fm0;
import com.lbe.parallel.g20;
import com.lbe.parallel.qz0;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public abstract class Lambda<R> implements g20, Serializable {
    public final int a;

    public Lambda(int i) {
        this.a = i;
    }

    @Override // com.lbe.parallel.g20
    public final int b() {
        return this.a;
    }

    public final String toString() {
        qz0.a.getClass();
        String string = getClass().getGenericInterfaces()[0].toString();
        if (string.startsWith("kotlin.jvm.functions.")) {
            string = string.substring(21);
        }
        fm0.v(string, "renderLambdaToString(...)");
        return string;
    }
}
