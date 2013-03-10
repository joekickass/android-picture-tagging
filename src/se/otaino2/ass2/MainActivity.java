package se.otaino2.ass2;

import java.util.Collections;
import java.util.List;

import se.otaino2.ass2.contacts.ContactsPickerActivity;
import se.otaino2.ass2.contacts.TaggedContactsActivity;
import se.otaino2.ass2.gallery.GalleryActivity;
import storage.PersistentPictureStore;
import storage.PersistentPictureStoreFactory;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * The main activity is responsible for displaying the full picture when
 * user selects one in the gallery. When starting the application, the
 * user will always get forwarded to the gallery application to select
 * a picture.
 * 
 * @author otaino-2
 *
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    
    private ImageView fullImage;
    
    private String imageUri;

    private List<String> tags = Collections.emptyList();
    
    // Request codes for activity results
    private static final int REQUEST_CODE_GALLERY = 11;
    private static final int REQUEST_CODE_CONTACTS = 12;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fullImage = (ImageView) findViewById(R.id.full_image);
        
        // I don't really get the instructions on how to open the
        // Gallery, so I assume it should always start there...
        openGallery();
    }
    
    private void openGallery() {
        Intent i = new Intent();
        i.setClass(this, GalleryActivity.class);
        startActivityForResult(i, REQUEST_CODE_GALLERY);
    }

    /**
     * Returns the result of activities started from this activity.
     * 
     * The GalleryActivity returns the database id for the requested
     * picture in the MediaStore.Images.Media table.
     * 
     * The ContactsActivity returns the contact picked by the user.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                String uri = data.getStringExtra(GalleryActivity.PICTURE_URI);
                showPicture(uri);
                break;
            case REQUEST_CODE_CONTACTS:
                String contact = data.getStringExtra(ContactsPickerActivity.CONTACTS_ID);
                addTag(contact);
                break;
            default:
                Log.w(TAG, "Unknown request code received. Must be some logical error in the program...");
                break;
            }
        }
    }
    
    /**
     * Shows the picture and loads any meta data (tags) associated with
     * it.
     * 
     * @param uri the uri to the picture (from MediaStore db) 
     */
    private void showPicture(String uri) {
        Log.d(TAG, "Picture selected in gallery. Showing it in fullscreen...");
        
        // Make sure image is not too large to open in an ImageView
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap b = BitmapFactory.decodeFile(uri, options);
        
        // Show image
        fullImage.setImageBitmap(b);
        
        // Load meta data
        // NOTE: Loading is done on main thread! This is fine for now, since we do nothing else on the main thread...
        PersistentPictureStore dao = PersistentPictureStoreFactory.getPersistentPictureStore(this);
        List<String> potentialTags = dao.getTags(uri);
        Log.d(TAG, "Found " + potentialTags.size() + " tags for image " + uri);
        if (potentialTags.size() > 0) {
            tags = potentialTags;
        }
        
        // Need to store uri for any future tagging...
        imageUri = uri;
    }
    
    /**
     * Associate a new tag with the current picture
     * 
     * @param tag
     */
    private void addTag(String tag) {
        Log.d(TAG, "Contact " + tag + " selected in contacts picker. Tagging picture with it...");

        if (imageUri == null || imageUri.isEmpty()) {
            Log.w(TAG, "Could not add tag! Somehow the picture uri was lost along the way...");
            return;
        }
        PersistentPictureStore dao = PersistentPictureStoreFactory.getPersistentPictureStore(this);
        dao.addTag(imageUri, tag);
        
        updateTags();
    }

    /**
     *  NOTE: Loading is done on main thread! This is fine for now, since we do nothing else on the main thread...
     */
    private void updateTags() {
        
        // Get any new tags from db
        PersistentPictureStore dao = PersistentPictureStoreFactory.getPersistentPictureStore(this);
        List<String> potentialTags = dao.getTags(imageUri);
        Log.d(TAG, "Found " + potentialTags.size() + " tags for image " + imageUri);
        if (potentialTags.size() > 0) {
            tags = potentialTags;
        }
        
        // Re-create options menu
        invalidateOptionsMenu();        
    }

    /**
     * Shows a menu option where the user can select to "tag" the
     * current picture. If the user has already tagged the picture,
     * an additional menu item is shown that displays all tags.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem view = menu.findItem(R.id.menu_view_tags);      
        view.setVisible(tags.size() > 0);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tag:
                openContactsPicker();
                return true;
            case R.id.menu_view_tags:
                showTags(tags);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openContactsPicker() {
        Intent i = new Intent();
        i.setClass(this, ContactsPickerActivity.class);
        startActivityForResult(i, REQUEST_CODE_CONTACTS);
    }
    
    private void showTags(List<String> tags) {
        Intent i = new Intent();
        i.setClass(this, TaggedContactsActivity.class);
        i.putExtra(TaggedContactsActivity.CONTACTS_IDS, tags.toArray(new String[tags.size()]));
        startActivity(i);
    }
}
