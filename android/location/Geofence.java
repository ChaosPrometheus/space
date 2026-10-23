package android.location;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class Geofence implements Parcelable {
    public static final Parcelable.Creator<Geofence> CREATOR = new Parcelable.Creator<Geofence>() { // from class: android.location.Geofence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence createFromParcel(Parcel parcel) {
            return new Geofence(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence[] newArray(int i) {
            return new Geofence[i];
        }
    };

    public Geofence(Parcel parcel) {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
    }
}
