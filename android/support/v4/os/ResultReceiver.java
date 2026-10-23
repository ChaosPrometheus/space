package android.support.v4.os;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.lbe.parallel.b80;
import com.lbe.parallel.c80;
import com.lbe.parallel.cf0;
import com.lbe.parallel.q01;

/* JADX INFO: loaded from: classes.dex */
@SuppressLint({"BanParcelableUsage"})
public class ResultReceiver implements Parcelable {
    public static final Parcelable.Creator<ResultReceiver> CREATOR = new cf0(1);
    public c80 a;

    public ResultReceiver(Parcel parcel) {
        c80 c80Var;
        IBinder strongBinder = parcel.readStrongBinder();
        int i = q01.n;
        if (strongBinder == null) {
            c80Var = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface(c80.X1);
            if (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof c80)) {
                b80 b80Var = new b80();
                b80Var.m = strongBinder;
                c80Var = b80Var;
            } else {
                c80Var = (c80) iInterfaceQueryLocalInterface;
            }
        }
        this.a = c80Var;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        synchronized (this) {
            try {
                if (this.a == null) {
                    this.a = new q01(this);
                }
                parcel.writeStrongBinder(this.a.asBinder());
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
