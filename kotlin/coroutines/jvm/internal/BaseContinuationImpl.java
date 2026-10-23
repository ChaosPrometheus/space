package kotlin.coroutines.jvm.internal;

import com.lbe.doubleagent.client.hook.r0;
import com.lbe.parallel.fm0;
import com.lbe.parallel.hm;
import com.lbe.parallel.hn;
import com.lbe.parallel.mp0;
import com.lbe.parallel.mq;
import com.lbe.parallel.y40;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import kotlin.Result;
import kotlin.coroutines.intrinsics.CoroutineSingletons;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseContinuationImpl implements hm, hn, Serializable {
    public final hm a;

    public BaseContinuationImpl(hm hmVar) {
        this.a = hmVar;
    }

    @Override // com.lbe.parallel.hn
    public final hn a() {
        hm hmVar = this.a;
        if (hmVar instanceof hn) {
            return (hn) hmVar;
        }
        return null;
    }

    @Override // com.lbe.parallel.hm
    public final void d(Object obj) {
        hm hmVar = this;
        while (true) {
            BaseContinuationImpl baseContinuationImpl = (BaseContinuationImpl) hmVar;
            hm hmVar2 = baseContinuationImpl.a;
            fm0.t(hmVar2);
            try {
                obj = baseContinuationImpl.g(obj);
                if (obj == CoroutineSingletons.COROUTINE_SUSPENDED) {
                    return;
                }
            } catch (Throwable th) {
                obj = new Result.Failure(th);
            }
            baseContinuationImpl.h();
            if (!(hmVar2 instanceof BaseContinuationImpl)) {
                hmVar2.d(obj);
                return;
            }
            hmVar = hmVar2;
        }
    }

    public hm e(hm hmVar) {
        fm0.w(hmVar, "completion");
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }

    public final StackTraceElement f() {
        int iIntValue;
        String strC;
        mq mqVar = (mq) getClass().getAnnotation(mq.class);
        String str = null;
        if (mqVar == null) {
            return null;
        }
        int iV = mqVar.v();
        if (iV > 1) {
            throw new IllegalStateException(("Debug metadata version mismatch. Expected: 1, got " + iV + ". Please update the Kotlin standard library.").toString());
        }
        try {
            Field declaredField = getClass().getDeclaredField("label");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            Integer num = obj instanceof Integer ? (Integer) obj : null;
            iIntValue = (num != null ? num.intValue() : 0) - 1;
        } catch (Exception unused) {
            iIntValue = -1;
        }
        int i = iIntValue >= 0 ? mqVar.l()[iIntValue] : -1;
        y40 y40Var = mp0.d;
        y40 y40Var2 = mp0.c;
        if (y40Var == null) {
            try {
                y40 y40Var3 = new y40(Class.class.getDeclaredMethod("getModule", new Class[0]), getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]), getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod(r0.q, new Class[0]));
                mp0.d = y40Var3;
                y40Var = y40Var3;
            } catch (Exception unused2) {
                mp0.d = y40Var2;
                y40Var = y40Var2;
            }
        }
        if (y40Var != y40Var2) {
            Method method = (Method) y40Var.a;
            Object objInvoke = method != null ? method.invoke(getClass(), new Object[0]) : null;
            if (objInvoke != null) {
                Method method2 = (Method) y40Var.b;
                Object objInvoke2 = method2 != null ? method2.invoke(objInvoke, new Object[0]) : null;
                if (objInvoke2 != null) {
                    Method method3 = (Method) y40Var.c;
                    Object objInvoke3 = method3 != null ? method3.invoke(objInvoke2, new Object[0]) : null;
                    if (objInvoke3 instanceof String) {
                        str = (String) objInvoke3;
                    }
                }
            }
        }
        if (str == null) {
            strC = mqVar.c();
        } else {
            strC = str + '/' + mqVar.c();
        }
        return new StackTraceElement(strC, mqVar.m(), mqVar.f(), i);
    }

    public abstract Object g(Object obj);

    public abstract void h();

    public String toString() {
        StringBuilder sb = new StringBuilder("Continuation at ");
        Object objF = f();
        if (objF == null) {
            objF = getClass().getName();
        }
        sb.append(objF);
        return sb.toString();
    }
}
