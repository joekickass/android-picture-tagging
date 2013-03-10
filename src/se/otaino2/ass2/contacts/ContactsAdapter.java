package se.otaino2.ass2.contacts;

import se.otaino2.ass2.R;
import android.content.Context;
import android.provider.ContactsContract;
import android.widget.SimpleCursorAdapter;

/**
 * The ContactsAdapter is responsible for serving the ListActivity with
 * TextView based on Cursor columns.  
 * 
 * Note: Somewhere far far down the hierarchy tree, this adapter implements the ListAdapter interface
 * as required by the GridView.
 * 
 * @author otaino-2
 */
class ContactsAdapter extends SimpleCursorAdapter {
    
    private static final String[] FROM = {ContactsContract.Contacts.DISPLAY_NAME};
    
    private static final int[] TO = {R.id.list_item};

    public ContactsAdapter(Context context) {
        // Set Cursor to null. It will be set using CursorAdapter.swapCursor when loaded.
        super(context, R.layout.view_list_item, null, FROM, TO, 0);
    }
}