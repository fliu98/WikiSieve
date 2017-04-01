package mypackage.frankliu.com.bareentities;

import android.os.Parcel;
import android.os.Parcelable;

public class WikiLinks implements Parcelable{
    private String wikipedia;
    private String dbpedia;

    protected WikiLinks(Parcel in) {
        wikipedia = in.readString();
        dbpedia = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wikipedia);
        dest.writeString(dbpedia);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WikiLinks> CREATOR = new Creator<WikiLinks>() {
        @Override
        public WikiLinks createFromParcel(Parcel in) {
            return new WikiLinks(in);
        }

        @Override
        public WikiLinks[] newArray(int size) {
            return new WikiLinks[size];
        }
    };

    public String getWikipedia(){
        return wikipedia;
    }

    public String getDbpedia(){
        return dbpedia;
    }
}
