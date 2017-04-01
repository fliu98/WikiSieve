package mypackage.frankliu.com.bareentities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TextAnalysis implements Parcelable{
    private String timestamp;
    private String lang;
    private ArrayList<Concept> annotations;

    protected TextAnalysis(Parcel in) {
        timestamp = in.readString();
        lang = in.readString();
        annotations = in.createTypedArrayList(Concept.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(lang);
        dest.writeTypedList(annotations);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TextAnalysis> CREATOR = new Creator<TextAnalysis>() {
        @Override
        public TextAnalysis createFromParcel(Parcel in) {
            return new TextAnalysis(in);
        }

        @Override
        public TextAnalysis[] newArray(int size) {
            return new TextAnalysis[size];
        }
    };

    public String getTimestamp() {
        return timestamp;
    }

    public ArrayList<Concept> getAnnotations() {
        return annotations;
    }
}
