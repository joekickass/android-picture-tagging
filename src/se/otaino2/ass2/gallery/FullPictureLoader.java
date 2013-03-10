package se.otaino2.ass2.gallery;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * The FullPictureLoader is responsible for loading a specific picture from the
 * MediaStore database
 * 
 * @author otaino-2
 *
 */
public class FullPictureLoader extends CursorLoader {
    
    // Assume most pictures are stored on SD-card
    private static final Uri URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    
    // We want DATA and IMAGE_ID from db
    private static final String[] PROJECTION = {MediaStore.Images.Media._ID,
                                                 MediaStore.Images.Media.DATA};
    
    // Filter on ID
    private static final String SELECTION = MediaStore.Images.Media._ID + "=?";
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public FullPictureLoader(Context context, String id) {
        super(context, URI, PROJECTION, SELECTION, new String[]{id}, SORT_ORDER);
    }
}
