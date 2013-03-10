package se.otaino2.ass2.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * The ContactsLoader is responsible for querying the Contacts content provider 
 * for contacts stored on the phone. Only contacts that have phone numbers are
 * of interest...
 * 
 * @author otaino-2
 *
 */
public class ContactsLoader extends CursorLoader {
    
    private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
    
    // We want name from db
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
                                                 ContactsContract.Contacts.DISPLAY_NAME};
    
    // Only show contacts that have name and number
    private static final String SELECTION = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                                              + ContactsContract.Contacts.DISPLAY_NAME + " != '' ) AND ("
                                              + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1))";;
    private static final String[] SELECTION_ARGS = {};
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public ContactsLoader(Context context) {
        super(context, URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
    }
}
