package mypackage.frankliu.com.bareentities.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataController {
    private AppDatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DataController(Context context){
        mDbHelper = new AppDatabaseHelper(context);
    }

    public void open(){
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        mDb.close();
    }

    public void insertPastQuery(@Nullable String imageUri, String queryString, int conceptCount, String timestamp){
        ContentValues values = new ContentValues();
        if(conceptCount==0){
            return;
        }
        if(imageUri!=null) {
            values.put(PastQuery.Columns.IMAGE_URI, imageUri);
        }
        values.put(PastQuery.Columns.QUERY_STRING,queryString);
        values.put(PastQuery.Columns.CONCEPT_COUNT,conceptCount);
        values.put(PastQuery.Columns.TIME_STAMP,timestamp);

        mDb.insert(PastQuery.TABLE_NAME,null,values);
    }

    public List<PastQuery> getAllPastQueries(){
        List<PastQuery> items= new ArrayList<>();
        Cursor data = mDb.query(
                PastQuery.TABLE_NAME,
                PastQuery.Columns.allColumns,
                null,
                null,
                null,
                null,
                null
        );
        while(data.moveToNext()){
            items.add(buildPastQuery(data));
        }
        data.close();
        return items;
    }

    private PastQuery buildPastQuery(Cursor cursor){
        PastQuery query = new PastQuery();
        query.setImageUri(cursor.getString(cursor.getColumnIndex(PastQuery.Columns.IMAGE_URI)));
        query.setQueryString(cursor.getString(cursor.getColumnIndex(PastQuery.Columns.QUERY_STRING)));
        query.setConceptCount(cursor.getInt(cursor.getColumnIndex(PastQuery.Columns.CONCEPT_COUNT)));
        query.setTimestamp(cursor.getString(cursor.getColumnIndex(PastQuery.Columns.TIME_STAMP)));
        return query;
    }
}
