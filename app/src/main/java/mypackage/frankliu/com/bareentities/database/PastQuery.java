package mypackage.frankliu.com.bareentities.database;


import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PastQuery {

    @Nullable
    private String imageUri;
    private String queryString;
    private int conceptCount;
    private String timestamp;

    @Nullable
    public String getImageUri(){
        return imageUri;
    }

    void setImageUri(@Nullable String imageUri){
        this.imageUri=imageUri;
    }

    public String getQueryString(){
        return queryString;
    }

    void setQueryString(String queryString){
        this.queryString=queryString;
    }

    public int getConceptCount(){
        return conceptCount;
    }

    void setConceptCount(int conceptCount){
        this.conceptCount=conceptCount;
    }

    public String getTimestamp(){
        return timestamp;
    }

    void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    static String TABLE_NAME = "PastQueries";

    static class Columns{
        static final String IMAGE_URI = "ImageUri",
                                QUERY_STRING = "QueryString",
                                CONCEPT_COUNT = "ConceptCount",
                                TIME_STAMP = "Timestamp";
        static String[] allColumns = {IMAGE_URI,QUERY_STRING,CONCEPT_COUNT,TIME_STAMP};
    }

}
