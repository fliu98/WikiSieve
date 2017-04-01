package mypackage.frankliu.com.bareentities;


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

    public void setImageUri(@Nullable String imageUri){
        this.imageUri=imageUri;
    }

    public String getQueryString(){
        return queryString;
    }

    public void setQueryString(String queryString){
        this.queryString=queryString;
    }

    public int getConceptCount(){
        return conceptCount;
    }

    public void setConceptCount(int conceptCount){
        this.conceptCount=conceptCount;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public static String TABLE_NAME = "PastQueries";

    public static class Columns{
        public static final String IMAGE_URI = "ImageUri",
                                QUERY_STRING = "QueryString",
                                CONCEPT_COUNT = "ConceptCount",
                                TIME_STAMP = "Timestamp";
        public static String[] allColumns = {IMAGE_URI,QUERY_STRING,CONCEPT_COUNT,TIME_STAMP};
    }

}
