package mypackage.frankliu.com.bareentities.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Concept implements Parcelable{
    private String label;
    private String uri;
    private Image image;
    private WikiLinks lod;

    private Concept(Parcel in) {
        label = in.readString();
        uri = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
        lod = in.readParcelable(WikiLinks.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(uri);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(lod, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Concept> CREATOR = new Creator<Concept>() {
        @Override
        public Concept createFromParcel(Parcel in) {
            return new Concept(in);
        }

        @Override
        public Concept[] newArray(int size) {
            return new Concept[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public String getUri() {
        return uri;
    }

    public Image getImage(){
        return image;
    }

    public WikiLinks getLod(){
        return lod;
    }

}