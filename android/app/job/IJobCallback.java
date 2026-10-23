package android.app.job;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lbe.parallel.l70;

/* JADX INFO: loaded from: classes.dex */
public interface IJobCallback extends IInterface {

    public static class Default implements IJobCallback {
        @Override // android.app.job.IJobCallback
        public void acknowledgeGetTransferredDownloadBytesMessage(int i, int i2, long j) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void acknowledgeGetTransferredUploadBytesMessage(int i, int i2, long j) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void acknowledgeStartMessage(int i, boolean z) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void acknowledgeStopMessage(int i, boolean z) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.app.job.IJobCallback
        public boolean completeWork(int i, int i2) throws RemoteException {
            return false;
        }

        @Override // android.app.job.IJobCallback
        public JobWorkItem dequeueWork(int i) throws RemoteException {
            return null;
        }

        @Override // android.app.job.IJobCallback
        public void jobFinished(int i, boolean z) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void setNotification(int i, int i2, Notification notification, int i3) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void updateEstimatedNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws RemoteException {
        }

        @Override // android.app.job.IJobCallback
        public void updateTransferredNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IJobCallback {
        private static final String DESCRIPTOR = "android.app.job.IJobCallback";
        static final int TRANSACTION_acknowledgeGetTransferredDownloadBytesMessage = 1;
        static final int TRANSACTION_acknowledgeGetTransferredUploadBytesMessage = 2;
        static final int TRANSACTION_acknowledgeStartMessage = 3;
        static final int TRANSACTION_acknowledgeStopMessage = 4;
        static final int TRANSACTION_completeWork = 6;
        static final int TRANSACTION_dequeueWork = 5;
        static final int TRANSACTION_jobFinished = 7;
        static final int TRANSACTION_setNotification = 10;
        static final int TRANSACTION_updateEstimatedNetworkBytes = 8;
        static final int TRANSACTION_updateTransferredNetworkBytes = 9;

        public static class Proxy implements IJobCallback {
            public static IJobCallback sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.app.job.IJobCallback
            public void acknowledgeGetTransferredDownloadBytesMessage(int i, int i2, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeLong(j);
                    if (this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().acknowledgeGetTransferredDownloadBytesMessage(i, i2, j);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public void acknowledgeGetTransferredUploadBytesMessage(int i, int i2, long j) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeLong(j);
                    if (this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().acknowledgeGetTransferredUploadBytesMessage(i, i2, j);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public void acknowledgeStartMessage(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().acknowledgeStartMessage(i, z);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public void acknowledgeStopMessage(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().acknowledgeStopMessage(i, z);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.app.job.IJobCallback
            public boolean completeWork(int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().completeWork(i, i2);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public JobWorkItem dequeueWork(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().dequeueWork(i);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? l70.c(JobWorkItem.CREATOR.createFromParcel(parcelObtain2)) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.job.IJobCallback
            public void jobFinished(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(7, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().jobFinished(i, z);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public void setNotification(int i, int i2, Notification notification, int i3) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (notification != null) {
                        parcelObtain.writeInt(1);
                        notification.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(i3);
                    if (this.mRemote.transact(10, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setNotification(i, i2, notification, i3);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobCallback
            public void updateEstimatedNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws Throwable {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (jobWorkItem != null) {
                        parcelObtain.writeInt(1);
                        jobWorkItem.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeLong(j);
                    parcelObtain.writeLong(j2);
                    try {
                        if (this.mRemote.transact(8, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                            parcelObtain2.readException();
                            parcelObtain2.recycle();
                            parcelObtain.recycle();
                        } else {
                            Stub.getDefaultImpl().updateEstimatedNetworkBytes(i, jobWorkItem, j, j2);
                            parcelObtain2.recycle();
                            parcelObtain.recycle();
                        }
                    } catch (Throwable th) {
                        th = th;
                        parcelObtain2.recycle();
                        parcelObtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // android.app.job.IJobCallback
            public void updateTransferredNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws Throwable {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (jobWorkItem != null) {
                        parcelObtain.writeInt(1);
                        jobWorkItem.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeLong(j);
                    parcelObtain.writeLong(j2);
                    try {
                        if (this.mRemote.transact(9, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                            parcelObtain2.readException();
                            parcelObtain2.recycle();
                            parcelObtain.recycle();
                        } else {
                            Stub.getDefaultImpl().updateTransferredNetworkBytes(i, jobWorkItem, j, j2);
                            parcelObtain2.recycle();
                            parcelObtain.recycle();
                        }
                    } catch (Throwable th) {
                        th = th;
                        parcelObtain2.recycle();
                        parcelObtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IJobCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IJobCallback)) ? new Proxy(iBinder) : (IJobCallback) iInterfaceQueryLocalInterface;
        }

        public static IJobCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IJobCallback iJobCallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iJobCallback == null) {
                return false;
            }
            Proxy.sDefaultImpl = iJobCallback;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    acknowledgeGetTransferredDownloadBytesMessage(parcel.readInt(), parcel.readInt(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    acknowledgeGetTransferredUploadBytesMessage(parcel.readInt(), parcel.readInt(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    acknowledgeStartMessage(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    acknowledgeStopMessage(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    JobWorkItem jobWorkItemDequeueWork = dequeueWork(parcel.readInt());
                    parcel2.writeNoException();
                    if (jobWorkItemDequeueWork != null) {
                        parcel2.writeInt(1);
                        jobWorkItemDequeueWork.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zCompleteWork = completeWork(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(zCompleteWork ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    jobFinished(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateEstimatedNetworkBytes(parcel.readInt(), parcel.readInt() != 0 ? l70.c(JobWorkItem.CREATOR.createFromParcel(parcel)) : null, parcel.readLong(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateTransferredNetworkBytes(parcel.readInt(), parcel.readInt() != 0 ? l70.c(JobWorkItem.CREATOR.createFromParcel(parcel)) : null, parcel.readLong(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    setNotification(parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (Notification) Notification.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void acknowledgeGetTransferredDownloadBytesMessage(int i, int i2, long j) throws RemoteException;

    void acknowledgeGetTransferredUploadBytesMessage(int i, int i2, long j) throws RemoteException;

    void acknowledgeStartMessage(int i, boolean z) throws RemoteException;

    void acknowledgeStopMessage(int i, boolean z) throws RemoteException;

    boolean completeWork(int i, int i2) throws RemoteException;

    JobWorkItem dequeueWork(int i) throws RemoteException;

    void jobFinished(int i, boolean z) throws RemoteException;

    void setNotification(int i, int i2, Notification notification, int i3) throws RemoteException;

    void updateEstimatedNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws RemoteException;

    void updateTransferredNetworkBytes(int i, JobWorkItem jobWorkItem, long j, long j2) throws RemoteException;
}
