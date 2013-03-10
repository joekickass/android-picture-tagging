package se.otaino2.ass2.gallery;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * The ThumbnailLoader is responsible for querying the MediaStore content provider 
 * for thumbnails of local pictures.
 * 
 * @author otaino-2
 *
 */
public class ThumbnailLoader extends CursorLoader {
    
    // Assume most pictures are stored on SD-card
    private static final Uri URI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    
    // We want DATA and IMAGE_ID from db
    private static final String[] PROJECTION = {MediaStore.Images.Thumbnails._ID,
                                                 MediaStore.Images.Thumbnails.IMAGE_ID,
                                                 MediaStore.Images.Thumbnails.DATA};
    
    // We want all pictures so no filtering
    private static final String SELECTION = null;
    private static final String[] SELECTION_ARGS = {};
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public ThumbnailLoader(Context context) {
        super(context, URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
    }
}