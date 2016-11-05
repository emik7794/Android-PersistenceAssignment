package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RedditDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbreddit.db";
    public static final String POST_TABLE = "post";
    public static final String POST_TABLE_ID = "_id";
    public static final String POST_TABLE_TITLE = "title";
    public static final String POST_TABLE_AUTHOR = "author";
    public static final String POST_TABLE_DATE = "date";
    public static final String POST_TABLE_COMMENTS= "comments";
    public static final String POST_TABLE_URLSTRING = "urlString";

    public RedditDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSentence = "create table "
                + POST_TABLE + " ( _id integer primary key autoincrement,"
                + POST_TABLE_TITLE + " text not null,"
                + POST_TABLE_AUTHOR + " text not null,"
                + POST_TABLE_DATE + " text not null,"
                + POST_TABLE_COMMENTS + " text not null,"
                + POST_TABLE_URLSTRING + " text not null"
                + " );";
        sqLiteDatabase.execSQL(createSentence);

        Log.d("DB", "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("DB", "Database Updated");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        this.onCreate(sqLiteDatabase);
    }
}
