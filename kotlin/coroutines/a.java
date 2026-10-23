package kotlin.coroutines;

import com.lbe.parallel.an;
import com.lbe.parallel.fm0;
import com.lbe.parallel.im;
import com.lbe.parallel.x10;
import com.lbe.parallel.x40;
import com.lbe.parallel.ym;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    public static an a(an anVar, an anVar2) {
        fm0.w(anVar2, "context");
        return anVar2 == EmptyCoroutineContext.a ? anVar : (an) anVar2.b(anVar, new x10() { // from class: kotlin.coroutines.CoroutineContext$plus$1
            @Override // com.lbe.parallel.x10
            public final Object c(Object obj, Object obj2) {
                CombinedContext combinedContext;
                an anVar3 = (an) obj;
                ym ymVar = (ym) obj2;
                fm0.w(anVar3, "acc");
                fm0.w(ymVar, "element");
                an anVarF = anVar3.f(ymVar.getKey());
                EmptyCoroutineContext emptyCoroutineContext = EmptyCoroutineContext.a;
                if (anVarF == emptyCoroutineContext) {
                    return ymVar;
                }
                x40 x40Var = x40.c;
                im imVar = (im) anVarF.c(x40Var);
                if (imVar == null) {
                    combinedContext = new CombinedContext(ymVar, anVarF);
                } else {
                    an anVarF2 = anVarF.f(x40Var);
                    if (anVarF2 == emptyCoroutineContext) {
                        return new CombinedContext(imVar, ymVar);
                    }
                    combinedContext = new CombinedContext(imVar, new CombinedContext(ymVar, anVarF2));
                }
                return combinedContext;
            }
        });
    }
}
