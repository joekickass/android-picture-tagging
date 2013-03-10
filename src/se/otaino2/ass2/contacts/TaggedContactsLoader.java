package se.otaino2.ass2.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * The TaggedContactsLoader is responsible for querying the Contacts content provider 
 * for specific contacts.
 * 
 * @author otaino-2
 *
 */
public class TaggedContactsLoader extends CursorLoader {
    
    private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
    
    // We want name from db
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
                                                 ContactsContract.Contacts.DISPLAY_NAME};
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public TaggedContactsLoader(Context context, String selection, String[] ids) {
        super(context, URI, PROJECTION, selection, ids, SORT_ORDER);
    }
}
