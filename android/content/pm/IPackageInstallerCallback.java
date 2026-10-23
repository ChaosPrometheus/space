package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IPackageInstallerCallback extends IInterface {

    public static class Default implements IPackageInstallerCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.content.pm.IPackageInstallerCallback
        public void onSessionActiveChanged(int i, boolean z) throws RemoteException {
        }

        @Override // android.content.pm.IPackageInstallerCallback
        public void onSessionBadgingChanged(int i) throws RemoteException {
        }

        @Override // android.content.pm.IPackageInstallerCallback
        public void onSessionCreated(int i) throws RemoteException {
        }

        @Override // android.content.pm.IPackageInstallerCallback
        public void onSessionFinished(int i, boolean z) throws RemoteException {
        }

        @Override // android.content.pm.IPackageInstallerCallback
        public void onSessionProgressChanged(int i, float f) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IPackageInstallerCallback {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerCallback";
        static final int TRANSACTION_onSessionActiveChanged = 3;
        static final int TRANSACTION_onSessionBadgingChanged = 2;
        static final int TRANSACTION_onSessionCreated = 1;
        static final int TRANSACTION_onSessionFinished = 5;
        static final int TRANSACTION_onSessionProgressChanged = 4;

        public static class Proxy implements IPackageInstallerCallback {
            public static IPackageInstallerCallback sDefaultImpl;
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

            @Override // android.content.pm.IPackageInstallerCallback
            public void onSessionActiveChanged(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(3, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSessionActiveChanged(i, z);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstallerCallback
            public void onSessionBadgingChanged(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(2, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSessionBadgingChanged(i);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstallerCallback
            public void onSessionCreated(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(1, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSessionCreated(i);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstallerCallback
            public void onSessionFinished(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(5, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSessionFinished(i, z);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstallerCallback
            public void onSessionProgressChanged(int i, float f) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeFloat(f);
                    if (this.mRemote.transact(4, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSessionProgressChanged(i, f);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageInstallerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IPackageInstallerCallback)) ? new Proxy(iBinder) : (IPackageInstallerCallback) iInterfaceQueryLocalInterface;
        }

        public static IPackageInstallerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IPackageInstallerCallback iPackageInstallerCallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iPackageInstallerCallback == null) {
                return false;
            }
            Proxy.sDefaultImpl = iPackageInstallerCallback;
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
                onSessionCreated(parcel.readInt());
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onSessionBadgingChanged(parcel.readInt());
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onSessionActiveChanged(parcel.readInt(), parcel.readInt() != 0);
                return true;
            }
            if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                onSessionProgressChanged(parcel.readInt(), parcel.readFloat());
                return true;
            }
            if (i == 5) {
                parcel.enforceInterface(DESCRIPTOR);
                onSessionFinished(parcel.readInt(), parcel.readInt() != 0);
                return true;
            }
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
    }

    void onSessionActiveChanged(int i, boolean z) throws RemoteException;

    void onSessionBadgingChanged(int i) throws RemoteException;

    void onSessionCreated(int i) throws RemoteException;

    void onSessionFinished(int i, boolean z) throws RemoteException;

    void onSessionProgressChanged(int i, float f) throws RemoteException;
}
