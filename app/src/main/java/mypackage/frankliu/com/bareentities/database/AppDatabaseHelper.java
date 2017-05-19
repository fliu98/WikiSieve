package mypackage.frankliu.com.bareentities.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AppDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AppDatabase";

    private static final String CREATE_PAST_QUERY_TABLE = "create table "
            + PastQuery.TABLE_NAME + " ( " + PastQuery.Columns.IMAGE_URI +
            " varchar(255), " + PastQuery.Columns.QUERY_STRING + " varchar(255), "
            +PastQuery.Columns.CONCEPT_COUNT + " int, "
            +PastQuery.Columns.TIME_STAMP + " varchar(255) );";

    AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PAST_QUERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Do nothing for now.
    }

}
