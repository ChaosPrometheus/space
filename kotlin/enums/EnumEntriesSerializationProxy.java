package kotlin.enums;

import com.lbe.parallel.fm0;
import java.io.Serializable;
import java.lang.Enum;

/* JADX INFO: loaded from: classes.dex */
public final class EnumEntriesSerializationProxy<E extends Enum<E>> implements Serializable {
    private static final long serialVersionUID = 0;
    public final Class a;

    public EnumEntriesSerializationProxy(Enum[] enumArr) {
        fm0.w(enumArr, "entries");
        Class<?> componentType = enumArr.getClass().getComponentType();
        fm0.t(componentType);
        this.a = componentType;
    }

    private final Object readResolve() {
        Object[] enumConstants = this.a.getEnumConstants();
        fm0.v(enumConstants, "getEnumConstants(...)");
        return a.a((Enum[]) enumConstants);
    }
}
