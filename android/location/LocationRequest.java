package android.location;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class LocationRequest implements Parcelable {
    public static final Parcelable.Creator<LocationRequest> CREATOR = new Parcelable.Creator<LocationRequest>() { // from class: android.location.LocationRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationRequest createFromParcel(Parcel parcel) {
            return new LocationRequest(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationRequest[] newArray(int i) {
            return new LocationRequest[i];
        }
    };

    public LocationRequest(Parcel parcel) {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
    }
}
