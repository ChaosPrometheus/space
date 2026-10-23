package kotlin.coroutines;

import com.lbe.parallel.an;
import com.lbe.parallel.cf1;
import com.lbe.parallel.fm0;
import com.lbe.parallel.widgets.SwitchView;
import com.lbe.parallel.x10;
import com.lbe.parallel.ym;
import com.lbe.parallel.zm;
import java.io.Serializable;
import kotlin.jvm.internal.Ref$IntRef;

/* JADX INFO: loaded from: classes.dex */
public final class CombinedContext implements an, Serializable {
    public final an a;
    public final ym b;

    public static final class Serialized implements Serializable {
        private static final long serialVersionUID = 0;
        public final an[] a;

        public Serialized(an[] anVarArr) {
            this.a = anVarArr;
        }

        private final Object readResolve() {
            an anVarE = EmptyCoroutineContext.a;
            for (an anVar : this.a) {
                anVarE = anVarE.e(anVar);
            }
            return anVarE;
        }
    }

    public CombinedContext(ym ymVar, an anVar) {
        fm0.w(anVar, SwitchView.LEFT_BANNER);
        fm0.w(ymVar, "element");
        this.a = anVar;
        this.b = ymVar;
    }

    private final Object writeReplace() {
        int iD = d();
        final an[] anVarArr = new an[iD];
        final Ref$IntRef ref$IntRef = new Ref$IntRef();
        b(cf1.a, new x10() { // from class: kotlin.coroutines.CombinedContext.writeReplace.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // com.lbe.parallel.x10
            public final Object c(Object obj, Object obj2) {
                ym ymVar = (ym) obj2;
                fm0.w((cf1) obj, "<anonymous parameter 0>");
                fm0.w(ymVar, "element");
                Ref$IntRef ref$IntRef2 = ref$IntRef;
                int i = ref$IntRef2.a;
                ref$IntRef2.a = i + 1;
                anVarArr[i] = ymVar;
                return cf1.a;
            }
        });
        if (ref$IntRef.a == iD) {
            return new Serialized(anVarArr);
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    @Override // com.lbe.parallel.an
    public final Object b(Object obj, x10 x10Var) {
        fm0.w(x10Var, "operation");
        return x10Var.c(this.a.b(obj, x10Var), this.b);
    }

    @Override // com.lbe.parallel.an
    public final ym c(zm zmVar) {
        fm0.w(zmVar, "key");
        CombinedContext combinedContext = this;
        while (true) {
            ym ymVarC = combinedContext.b.c(zmVar);
            if (ymVarC != null) {
                return ymVarC;
            }
            an anVar = combinedContext.a;
            if (!(anVar instanceof CombinedContext)) {
                return anVar.c(zmVar);
            }
            combinedContext = (CombinedContext) anVar;
        }
    }

    public final int d() {
        int i = 2;
        CombinedContext combinedContext = this;
        while (true) {
            an anVar = combinedContext.a;
            combinedContext = anVar instanceof CombinedContext ? (CombinedContext) anVar : null;
            if (combinedContext == null) {
                return i;
            }
            i++;
        }
    }

    @Override // com.lbe.parallel.an
    public final an e(an anVar) {
        return a.a(this, anVar);
    }

    public final boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof CombinedContext) {
                CombinedContext combinedContext = (CombinedContext) obj;
                if (combinedContext.d() == d()) {
                    CombinedContext combinedContext2 = this;
                    while (true) {
                        ym ymVar = combinedContext2.b;
                        if (!fm0.d(combinedContext.c(ymVar.getKey()), ymVar)) {
                            break;
                        }
                        an anVar = combinedContext2.a;
                        if (anVar instanceof CombinedContext) {
                            combinedContext2 = (CombinedContext) anVar;
                        } else {
                            fm0.u(anVar, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                            ym ymVar2 = (ym) anVar;
                            if (fm0.d(combinedContext.c(ymVar2.getKey()), ymVar2)) {
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override // com.lbe.parallel.an
    public final an f(zm zmVar) {
        fm0.w(zmVar, "key");
        ym ymVar = this.b;
        ym ymVarC = ymVar.c(zmVar);
        an anVar = this.a;
        if (ymVarC != null) {
            return anVar;
        }
        an anVarF = anVar.f(zmVar);
        if (anVarF == anVar) {
            return this;
        }
        return anVarF == EmptyCoroutineContext.a ? ymVar : new CombinedContext(ymVar, anVarF);
    }

    public final int hashCode() {
        return this.b.hashCode() + this.a.hashCode();
    }

    public final String toString() {
        return "[" + ((String) b("", new x10() { // from class: kotlin.coroutines.CombinedContext.toString.1
            @Override // com.lbe.parallel.x10
            public final Object c(Object obj, Object obj2) {
                String str = (String) obj;
                ym ymVar = (ym) obj2;
                fm0.w(str, "acc");
                fm0.w(ymVar, "element");
                if (str.length() == 0) {
                    return ymVar.toString();
                }
                return str + ", " + ymVar;
            }
        })) + ']';
    }
}
