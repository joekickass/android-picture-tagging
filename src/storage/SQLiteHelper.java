package storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Sqlite database for storing picture tags information
 * 
 * @author otaino-2
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Database common stuff
    private static final String DATABASE_NAME = "tags.db";
    private static final int DATABASE_VERSION = 5;
    
    // The pics table stores all pics that have tags
    public static final String TABLE_PICS = "pics";
    public static final String COLUMN_PIC_ID = "_id";
    public static final String COLUMN_PIC_URI = "pic_uri";
    
    // The tags table maps tags to their associated pic. Note that
    // each pic may have many tags associated with it.
    public static final String TABLE_TAGS = "tags";
    public static final String COLUMN_TAG_ID = "_id";
    public static final String COLUMN_TAG_NAME = "tag_name";
    public static final String COLUMN_TAG_PIC_ID = "tag_pic_id";
    
    // Database creation sql statement
    private static final String CREATE_PICS_TABLE = "create table "
        + TABLE_PICS + "("
        + COLUMN_PIC_ID + " integer primary key autoincrement, "
        + COLUMN_PIC_URI + " text unique);";
    
    private static final String CREATE_TAGS_TABLE = "create table "
            + TABLE_TAGS + "("
            + COLUMN_TAG_ID + " integer primary key autoincrement, "
            + COLUMN_TAG_NAME + " text not null, " 
            + COLUMN_TAG_PIC_ID + " integer, "
            + "FOREIGN KEY ("+COLUMN_TAG_PIC_ID+") REFERENCES "+TABLE_PICS+" ("+COLUMN_PIC_ID+"));";

    public SQLiteHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL(CREATE_PICS_TABLE);
      database.execSQL(CREATE_TAGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
              + oldVersion + " to " + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICS);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
      onCreate(db);
    }

}
