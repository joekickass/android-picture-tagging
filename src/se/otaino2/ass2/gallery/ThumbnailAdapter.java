package se.otaino2.ass2.gallery;

import se.otaino2.ass2.R;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.SimpleCursorAdapter;

/**
 * The ThumbnailAdapter is responsible for creating ImageViews from
 * Cursor columns.  
 * 
 * Note: Somewhere far far down the hierarchy tree, this adapter implements the ListAdapter interface
 * as required by the GridView.
 * 
 * @author otaino-2
 */
class ThumbnailAdapter extends SimpleCursorAdapter {
    
    private static final String[] FROM = {MediaStore.Images.Thumbnails.DATA};
    
    private static final int[] TO = {R.id.grid_item};

    public ThumbnailAdapter(Context context) {
        // Set Cursor to null. It will be set using CursorAdapter.swapCursor when loaded.
        super(context, R.layout.view_grid_item, null, FROM, TO, 0);
    }
    
    /**
     * Returns the row id to the full picture in the
     * MediaStore.Images.Media table.
     * 
     * @param position the position of the item in the list
     * @return the id of the picture in the MediaStore.Images.Media table
     * @throws IllegalStateException if Cursor is not available or closed
     */
    public String getIdForBigPicture(int position) throws IllegalStateException {
        Cursor cursor = getCursor();
        if (cursor != null && !cursor.isClosed()) {
            int index = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            long id = cursor.getLong(index);
            return Long.toString(id);
        } 
        throw new IllegalStateException("Meta information of image is not available!");
    }
}