package se.otaino2.ass2.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * The TaggedContactsPhoneNumberLoader is responsible for querying the Contacts content provider 
 * for the phone number of a specific contacts.
 * 
 * @author otaino-2
 *
 */
public class TaggedContactsPhoneNumberLoader extends CursorLoader {
    
    private static final Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    
    // We want name from db
    private static final String[] PROJECTION = {ContactsContract.CommonDataKinds.Phone._ID,
                                                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                                 ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                                 ContactsContract.CommonDataKinds.Phone.NUMBER};
    
    private static final String SELECTION = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
    
    // Use default sort order
    private static final String SORT_ORDER = null;

    public TaggedContactsPhoneNumberLoader(Context context, String id) {
        super(context, URI, PROJECTION, SELECTION, new String[]{id}, SORT_ORDER);
    }
}
