package kotlin;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {
    public static final void a(Object obj) throws Throwable {
        if (obj instanceof Result.Failure) {
            throw ((Result.Failure) obj).a;
        }
    }
}
