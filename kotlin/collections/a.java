package kotlin.collections;

import com.lbe.parallel.fm0;
import com.lbe.parallel.j40;
import com.lbe.parallel.t10;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements Collection {
    public abstract int a();

    @Override // java.util.Collection
    public final boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        if (isEmpty()) {
            return false;
        }
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (fm0.d(it.next(), obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Collection
    public final boolean containsAll(Collection collection) {
        fm0.w(collection, "elements");
        Collection collection2 = collection;
        if (collection2.isEmpty()) {
            return true;
        }
        Iterator it = collection2.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection
    public final boolean isEmpty() {
        return a() == 0;
    }

    @Override // java.util.Collection
    public final boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ int size() {
        return a();
    }

    @Override // java.util.Collection
    public final Object[] toArray() {
        return j40.J(this);
    }

    public final String toString() {
        t10 t10Var = new t10() { // from class: kotlin.collections.AbstractCollection$toString$1
            {
                super(1);
            }

            @Override // com.lbe.parallel.t10
            public final Object d(Object obj) {
                return obj == this.b ? "(this Collection)" : String.valueOf(obj);
            }
        };
        StringBuilder sb = new StringBuilder();
        sb.append((CharSequence) "[");
        int i = 0;
        for (Object obj : this) {
            i++;
            if (i > 1) {
                sb.append((CharSequence) ", ");
            }
            sb.append((CharSequence) t10Var.d(obj));
        }
        sb.append((CharSequence) "]");
        String string = sb.toString();
        fm0.v(string, "toString(...)");
        return string;
    }

    @Override // java.util.Collection
    public final Object[] toArray(Object[] objArr) {
        fm0.w(objArr, "array");
        return j40.K(this, objArr);
    }
}
