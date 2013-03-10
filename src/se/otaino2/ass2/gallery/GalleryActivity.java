package se.otaino2.ass2.gallery;

import se.otaino2.ass2.R;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * GalleryActivity displays a grid of thumbnails of the images stored on the phone.
 * When a particular thumbnail is selected, the content uri representing the full image
 * of the thumbnail is returned using @link{android.app.Activity#setResult}.
 * 
 * Note: This implementation make use of the @link{android.app.LoaderManager} and supporting
 * classes. This means that the application will only run on Android version 11 (Honeycomb)
 * and newer. As backwards compatibility is not part of the assignment, I will refrain from using
 * the support library.
 * 
 * Note2: The LoaderManager is a quite competent component. It manages one or more Loaders associated
 * with an activity (or fragment) and is in charge of starting, stopping, retaining, restarting, and 
 * destroying its Loaders. These events may be initiated directly by the client (LoaderManager.initLoader
 * or similar) or indirectly by changes in the activity/fragment life cycle. Each Loader is in turn
 * responsible for loading its data on a separate thread as well as monitoring the data for changes and
 * re-loading when necessary. 
 * 
 * @author otaino-2
 *
 */
public class GalleryActivity extends Activity implements LoaderCallbacks<Cursor>, OnItemClickListener {
    
    public static final String PICTURE_ID = "picture_id";
    public static final String PICTURE_URI = "picture_uri";
    
    private static final int THUMBNAIL_LOADER_ID = 21;
    private static final int FULL_PICTURE_LOADER_ID = 22;

    private ThumbnailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        adapter = new ThumbnailAdapter(this);
        
        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);
        
        getLoaderManager().initLoader(THUMBNAIL_LOADER_ID, null, this);
    }

    /**
     * Called when an item is clicked in the grid view. Triggers a load of the full image
     * from the MediaStore.Images.Media table.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Only the adapter has access to the current cursor. Ask it for big pic id.
        String picId = adapter.getIdForBigPicture(position);
        System.out.println("picId = " + picId);
        // Start load of full picture from MediaStore.Images table
        Bundle bundle = new Bundle();
        bundle.putString(PICTURE_ID, picId);
        getLoaderManager().initLoader(FULL_PICTURE_LOADER_ID, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return GalleryLoaderFactory.createLoader(this, id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        
        switch (loader.getId()) {
        case THUMBNAIL_LOADER_ID:
            // Swap adapter cursor to show new data
            adapter.swapCursor(cursor);
            break;
            
        case FULL_PICTURE_LOADER_ID:
            // Get data from cursor
            cursor.moveToFirst();
            String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Intent i = createResult(uri);
            // Return success to caller
            setResult(RESULT_OK, i);
            finish();
            break;
            
        default:
            throw new IllegalArgumentException("Unkown loader id");
        }
    }
    
    /**
     * Creates an intent holding the specified picture uri
     * @param uri the content uri
     * @return an Intent
     */
    private Intent createResult(String uri) {
        Intent intent = new Intent();
        intent.putExtra(PICTURE_URI, uri);
        return intent;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        
        switch (loader.getId()) {
        
        case THUMBNAIL_LOADER_ID:
            adapter.swapCursor(null);
            break;
            
        default:
            // Do nothing
            break;
        }
    }
    
    private static class GalleryLoaderFactory {
        
        public static Loader<Cursor> createLoader(Context context, int id, Bundle args) {
            switch (id) {
            case THUMBNAIL_LOADER_ID:
                return new ThumbnailLoader(context);
            case FULL_PICTURE_LOADER_ID:
                String picId = args.getString(PICTURE_ID);
                return new FullPictureLoader(context, picId);
            default:
                throw new IllegalArgumentException("Unkown loader id");
            }
        }
        
        
    }
}
