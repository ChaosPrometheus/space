package android.accounts;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IAccountAuthenticatorResponse extends IInterface {

    public static class Default implements IAccountAuthenticatorResponse {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.accounts.IAccountAuthenticatorResponse
        public void onError(int i, String str) throws RemoteException {
        }

        @Override // android.accounts.IAccountAuthenticatorResponse
        public void onRequestContinued() throws RemoteException {
        }

        @Override // android.accounts.IAccountAuthenticatorResponse
        public void onResult(Bundle bundle) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IAccountAuthenticatorResponse {
        private static final String DESCRIPTOR = "android.accounts.IAccountAuthenticatorResponse";
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onRequestContinued = 2;
        static final int TRANSACTION_onResult = 1;

        public static class Proxy implements IAccountAuthenticatorResponse {
            public static IAccountAuthenticatorResponse sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.accounts.IAccountAuthenticatorResponse
            public void onError(int i, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (this.mRemote.transact(3, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onError(i, str);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.accounts.IAccountAuthenticatorResponse
            public void onRequestContinued() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onRequestContinued();
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.accounts.IAccountAuthenticatorResponse
            public void onResult(Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onResult(bundle);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccountAuthenticatorResponse asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IAccountAuthenticatorResponse)) ? new Proxy(iBinder) : (IAccountAuthenticatorResponse) iInterfaceQueryLocalInterface;
        }

        public static IAccountAuthenticatorResponse getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IAccountAuthenticatorResponse iAccountAuthenticatorResponse) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iAccountAuthenticatorResponse == null) {
                return false;
            }
            Proxy.sDefaultImpl = iAccountAuthenticatorResponse;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onResult(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onRequestContinued();
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onError(parcel.readInt(), parcel.readString());
                return true;
            }
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
    }

    void onError(int i, String str) throws RemoteException;

    void onRequestContinued() throws RemoteException;

    void onResult(Bundle bundle) throws RemoteException;
}
