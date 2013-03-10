package storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Calls that abstracts away the database access
 * 
 * @author otaino-2
 *
 */
public class PictureDAO implements PersistentPictureStore {
    
    private static final String TAG = "PictureDAO";
    
    // Database fields
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private String[] allPicColumns = { SQLiteHelper.COLUMN_PIC_ID, 
                                        SQLiteHelper.COLUMN_PIC_URI };
    private String[] allTagColumns = { SQLiteHelper.COLUMN_TAG_ID,
                                        SQLiteHelper.COLUMN_TAG_NAME,
                                        SQLiteHelper.COLUMN_TAG_PIC_ID };

    public PictureDAO(Context context) {
      dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
      dbHelper.close();
    }
    
    @Override
    public void addTag(String pictureUri, String tag) {
        
        Log.d(TAG, "Adding tag " + tag + " to " + pictureUri);
        try {
            open();
        
            // First add picture
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_PIC_URI, pictureUri);
            long insertId = db.insert(SQLiteHelper.TABLE_PICS, null, values);
            values.clear();
            
            // Picture alread added, get its id
            if (insertId == -1) {
                Cursor cursor = db.query(SQLiteHelper.TABLE_PICS,
                        allPicColumns,
                        SQLiteHelper.COLUMN_PIC_URI + " = '" + pictureUri + "'",
                        null, null, null, null);
                cursor.moveToFirst();
                if (cursor.isAfterLast()) {
                    Log.w(TAG, "Could not add tag to picture. Something is seriously wrong in the database...");
                    cursor.close();
                    return;
                }
                insertId = cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_PIC_ID));
                cursor.close();
            }
            
            // Then add tag
            Log.d(TAG, "Associating tag " + tag + " with picture id " + insertId);
            values.put(SQLiteHelper.COLUMN_TAG_NAME, tag);
            values.put(SQLiteHelper.COLUMN_TAG_PIC_ID, insertId);
            db.insert(SQLiteHelper.TABLE_TAGS, null, values);
            
        } finally {
            // Make sure we always close db
            close();
        }
    }

    @Override
    public List<String> getTags(String pictureUri) {
        Log.d(TAG, "Getting all tags for " + pictureUri);
        List<String> tags = new ArrayList<String>();

        try {
            open();
            
            // First get picture ID for the given uri
            Cursor cursor = db.query(SQLiteHelper.TABLE_PICS,
                                     allPicColumns,
                                     SQLiteHelper.COLUMN_PIC_URI + " = '" + pictureUri + "'",
                                     null, null, null, null);
            cursor.moveToFirst();
            String pictureId = null;
            if (!cursor.isAfterLast()) {
                pictureId = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PIC_ID));
            }
            cursor.close();
            
    
            if (pictureId == null || pictureId.isEmpty()) {
                Log.d(TAG, "No tags found for " + pictureId);
                close();
                return Collections.emptyList();
            }
            
            // Now find all tags for that ID
            cursor = db.query(SQLiteHelper.TABLE_TAGS,
                              allTagColumns,
                              SQLiteHelper.COLUMN_TAG_PIC_ID + " = " + pictureId,
                              null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String tag = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TAG_NAME));
                tags.add(tag);
                cursor.moveToNext();
            }
            cursor.close();
            
        } finally {
            // Make sure we always close db
            close();
        }
        
        return tags;
    }
}
