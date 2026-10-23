package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lbe.parallel.l70;

/* JADX INFO: loaded from: classes.dex */
public interface IJobService extends IInterface {

    public static class Default implements IJobService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.app.job.IJobService
        public void getTransferredDownloadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException {
        }

        @Override // android.app.job.IJobService
        public void getTransferredUploadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException {
        }

        @Override // android.app.job.IJobService
        public void onNetworkChanged(JobParameters jobParameters) throws RemoteException {
        }

        @Override // android.app.job.IJobService
        public void startJob(JobParameters jobParameters) throws RemoteException {
        }

        @Override // android.app.job.IJobService
        public void stopJob(JobParameters jobParameters) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IJobService {
        private static final String DESCRIPTOR = "android.app.job.IJobService";
        static final int TRANSACTION_getTransferredDownloadBytes = 4;
        static final int TRANSACTION_getTransferredUploadBytes = 5;
        static final int TRANSACTION_onNetworkChanged = 3;
        static final int TRANSACTION_startJob = 1;
        static final int TRANSACTION_stopJob = 2;

        public static class Proxy implements IJobService {
            public static IJobService sDefaultImpl;
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

            @Override // android.app.job.IJobService
            public void getTransferredDownloadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jobParameters != null) {
                        parcelObtain.writeInt(1);
                        jobParameters.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (jobWorkItem != null) {
                        parcelObtain.writeInt(1);
                        jobWorkItem.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(4, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().getTransferredDownloadBytes(jobParameters, jobWorkItem);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobService
            public void getTransferredUploadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jobParameters != null) {
                        parcelObtain.writeInt(1);
                        jobParameters.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (jobWorkItem != null) {
                        parcelObtain.writeInt(1);
                        jobWorkItem.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(5, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().getTransferredUploadBytes(jobParameters, jobWorkItem);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobService
            public void onNetworkChanged(JobParameters jobParameters) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jobParameters != null) {
                        parcelObtain.writeInt(1);
                        jobParameters.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onNetworkChanged(jobParameters);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobService
            public void startJob(JobParameters jobParameters) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jobParameters != null) {
                        parcelObtain.writeInt(1);
                        jobParameters.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().startJob(jobParameters);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.app.job.IJobService
            public void stopJob(JobParameters jobParameters) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jobParameters != null) {
                        parcelObtain.writeInt(1);
                        jobParameters.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, parcelObtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().stopJob(jobParameters);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IJobService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IJobService)) ? new Proxy(iBinder) : (IJobService) iInterfaceQueryLocalInterface;
        }

        public static IJobService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IJobService iJobService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iJobService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iJobService;
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
                startJob(parcel.readInt() != 0 ? (JobParameters) JobParameters.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                stopJob(parcel.readInt() != 0 ? (JobParameters) JobParameters.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onNetworkChanged(parcel.readInt() != 0 ? (JobParameters) JobParameters.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                getTransferredDownloadBytes(parcel.readInt() != 0 ? (JobParameters) JobParameters.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? l70.c(JobWorkItem.CREATOR.createFromParcel(parcel)) : null);
                return true;
            }
            if (i == 5) {
                parcel.enforceInterface(DESCRIPTOR);
                getTransferredUploadBytes(parcel.readInt() != 0 ? (JobParameters) JobParameters.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? l70.c(JobWorkItem.CREATOR.createFromParcel(parcel)) : null);
                return true;
            }
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
    }

    void getTransferredDownloadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException;

    void getTransferredUploadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem) throws RemoteException;

    void onNetworkChanged(JobParameters jobParameters) throws RemoteException;

    void startJob(JobParameters jobParameters) throws RemoteException;

    void stopJob(JobParameters jobParameters) throws RemoteException;
}
