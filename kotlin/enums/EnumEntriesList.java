package kotlin.enums;

import com.lbe.parallel.fm0;
import com.lbe.parallel.n;
import com.lbe.parallel.q;
import com.lbe.parallel.zv;
import java.io.Serializable;
import java.lang.Enum;

/* JADX INFO: loaded from: classes.dex */
final class EnumEntriesList<T extends Enum<T>> extends n implements zv, Serializable {
    public final Enum[] a;

    public EnumEntriesList(Enum[] enumArr) {
        this.a = enumArr;
    }

    private final Object writeReplace() {
        return new EnumEntriesSerializationProxy(this.a);
    }

    @Override // kotlin.collections.a
    public final int a() {
        return this.a.length;
    }

    @Override // kotlin.collections.a, java.util.Collection
    public final boolean contains(Object obj) {
        if (!(obj instanceof Enum)) {
            return false;
        }
        Enum r6 = (Enum) obj;
        fm0.w(r6, "element");
        int iOrdinal = r6.ordinal();
        Enum[] enumArr = this.a;
        fm0.w(enumArr, "<this>");
        return ((iOrdinal < 0 || iOrdinal > enumArr.length - 1) ? null : enumArr[iOrdinal]) == r6;
    }

    @Override // java.util.List
    public final Object get(int i) {
        Enum[] enumArr = this.a;
        int length = enumArr.length;
        if (i < 0 || i >= length) {
            throw new IndexOutOfBoundsException(q.h("index: ", i, ", size: ", length));
        }
        return enumArr[i];
    }

    @Override // com.lbe.parallel.n, java.util.List
    public final int indexOf(Object obj) {
        if (!(obj instanceof Enum)) {
            return -1;
        }
        Enum r5 = (Enum) obj;
        fm0.w(r5, "element");
        int iOrdinal = r5.ordinal();
        Enum[] enumArr = this.a;
        fm0.w(enumArr, "<this>");
        if (((iOrdinal < 0 || iOrdinal > enumArr.length + (-1)) ? null : enumArr[iOrdinal]) == r5) {
            return iOrdinal;
        }
        return -1;
    }

    @Override // com.lbe.parallel.n, java.util.List
    public final int lastIndexOf(Object obj) {
        if (!(obj instanceof Enum)) {
            return -1;
        }
        Enum r2 = (Enum) obj;
        fm0.w(r2, "element");
        return indexOf(r2);
    }
}
