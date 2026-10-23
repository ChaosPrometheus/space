package android.app;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IBinderSession extends IInterface {
    public static final String DESCRIPTOR = "android.app.IBinderSession";

    public static class Default implements IBinderSession {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.app.IBinderSession
        public void binderTransactionCompleted(long j) throws RemoteException {
        }

        @Override // android.app.IBinderSession
        public long binderTransactionStarting(String str) throws RemoteException {
            return 0L;
        }
    }

    public static abstract class Stub extends Binder implements IBinderSession {
        static final int TRANSACTION_binderTransactionCompleted = 2;
        static final int TRANSACTION_binderTransactionStarting = 1;

        public static class Proxy implements IBinderSession {
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.app.IBinderSession
            public void binderTransactionCompleted(long j) throws RemoteException {
                Parcel parcelObtain = Build.VERSION.SDK_INT >= 33 ? Parcel.obtain(asBinder()) : null;
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IBinderSession.DESCRIPTOR);
                    parcelObtain.writeLong(j);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.IBinderSession
            public long binderTransactionStarting(String str) throws RemoteException {
                Parcel parcelObtain = Build.VERSION.SDK_INT >= 33 ? Parcel.obtain(asBinder()) : null;
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IBinderSession.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readLong();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return IBinderSession.DESCRIPTOR;
            }
        }

        public Stub() {
            attachInterface(this, IBinderSession.DESCRIPTOR);
        }

        public static IBinderSession asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IBinderSession.DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IBinderSession)) ? new Proxy(iBinder) : (IBinderSession) iInterfaceQueryLocalInterface;
        }

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "binderTransactionStarting";
            }
            if (i != 2) {
                return null;
            }
            return "binderTransactionCompleted";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IBinderSession.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IBinderSession.DESCRIPTOR);
                return true;
            }
            if (i == 1) {
                String string = parcel.readString();
                if (Build.VERSION.SDK_INT >= 33) {
                    parcel.enforceNoDataAvail();
                }
                long jBinderTransactionStarting = binderTransactionStarting(string);
                parcel2.writeNoException();
                parcel2.writeLong(jBinderTransactionStarting);
                return true;
            }
            if (i != 2) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            long j = parcel.readLong();
            if (Build.VERSION.SDK_INT >= 33) {
                parcel.enforceNoDataAvail();
            }
            binderTransactionCompleted(j);
            parcel2.writeNoException();
            return true;
        }
    }

    void binderTransactionCompleted(long j) throws RemoteException;

    long binderTransactionStarting(String str) throws RemoteException;
}
