package kotlin.coroutines.jvm.internal;

import com.lbe.parallel.an;
import com.lbe.parallel.dk;
import com.lbe.parallel.fm0;
import com.lbe.parallel.hm;
import com.lbe.parallel.us;
import com.lbe.parallel.x40;
import com.lbe.parallel.ym;

/* JADX INFO: loaded from: classes.dex */
public abstract class ContinuationImpl extends BaseContinuationImpl {
    public final an b;
    public transient hm c;

    public ContinuationImpl(hm hmVar) {
        this(hmVar, hmVar != null ? hmVar.getContext() : null);
    }

    @Override // com.lbe.parallel.hm
    public final an getContext() {
        an anVar = this.b;
        fm0.t(anVar);
        return anVar;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final void h() {
        hm hmVar = this.c;
        if (hmVar != null && hmVar != this) {
            an anVar = this.b;
            fm0.t(anVar);
            ym ymVarC = anVar.c(x40.c);
            fm0.t(ymVarC);
            ((us) hmVar).k();
        }
        this.c = dk.a;
    }

    public ContinuationImpl(hm hmVar, an anVar) {
        super(hmVar);
        this.b = anVar;
    }
}
