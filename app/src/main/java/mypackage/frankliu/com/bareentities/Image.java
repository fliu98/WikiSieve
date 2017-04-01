package mypackage.frankliu.com.bareentities;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable{
    private String full;
    private String thumbnail;

    protected Image(Parcel in) {
        full = in.readString();
        thumbnail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(full);
        dest.writeString(thumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getThumbnail(){
        return thumbnail;
    }
}