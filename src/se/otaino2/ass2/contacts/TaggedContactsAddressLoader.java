package se.otaino2.ass2.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * The TaggedContactsAddressLoader is responsible for querying the Contacts content provider 
 * for the address of a specific contacts.
 * 
 * @author otaino-2
 *
 */
public class TaggedContactsAddressLoader extends CursorLoader {
    
    private static final Uri URI = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
    
    // We want name from db
    private static final String[] PROJECTION = {ContactsContract.CommonDataKinds.StructuredPostal._ID,
                                                 ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID,
                                                 ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME,
                                                 ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS};
    
    private static final String SELECTION = ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=?";
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public TaggedContactsAddressLoader(Context context, String id) {
        super(context, URI, PROJECTION, SELECTION, new String[]{id}, SORT_ORDER);
    }
}
