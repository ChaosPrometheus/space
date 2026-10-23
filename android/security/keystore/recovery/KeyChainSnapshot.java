package android.security.keystore.recovery;

import android.os.Parcel;
import android.os.Parcelable;
import com.lbe.parallel.cf0;

/* JADX INFO: loaded from: classes.dex */
public final class KeyChainSnapshot implements Parcelable {
    public static final Parcelable.Creator<KeyChainSnapshot> CREATOR = new cf0(0);

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
    }
}
