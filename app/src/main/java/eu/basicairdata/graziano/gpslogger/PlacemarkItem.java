package eu.basicairdata.graziano.gpslogger;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacemarkItem implements Parcelable {
    String name;
    String description;
    int category;

    PlacemarkItem(String name, String description, int category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.category);
    }

    public int getCategory() {
        return category;
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.description = source.readString();
        this.category = source.readInt();
    }

    protected PlacemarkItem(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.category = in.readInt();
    }

    public static final Parcelable.Creator<PlacemarkItem> CREATOR = new Parcelable.Creator<PlacemarkItem>() {
        @Override
        public PlacemarkItem createFromParcel(Parcel source) {
            return new PlacemarkItem(source);
        }

        @Override
        public PlacemarkItem[] newArray(int size) {
            return new PlacemarkItem[size];
        }
    };

    @Override
    public String toString() {
        return "PlacemarkItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + category +
                '}';
    }
}
