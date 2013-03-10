package se.otaino2.ass2.contacts;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;

/**
 * Class responsible for showing a list of specified (tagged) contacts and showing detailed
 * information about them if clicked on.
 * 
 * @author otaino-2
 *
 */
public class TaggedContactsActivity extends ListActivity implements LoaderCallbacks<Cursor> {

    public static final String CONTACTS_IDS = "contacts_ids";
    
    private static final String DIALOG = "contacts_dialog";
    
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ContactsAdapter(this);
        setListAdapter(adapter);
        
        Bundle bundle = getIdsFromIntent(getIntent());
        getLoaderManager().initLoader(0, bundle, this);
    }

    private Bundle getIdsFromIntent(Intent intent) {
        String[] ids = intent.getStringArrayExtra(CONTACTS_IDS);
        Bundle bundle = new Bundle();
        bundle.putStringArray(CONTACTS_IDS, ids);
        return bundle;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        openContact(position);
    }
    
    private void openContact(int position) {
        Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
        if (cursor == null || cursor.isClosed()) {
            throw new IllegalStateException("Meta information for contact is not available!");
        }
        
        // Get id and name from adapter
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        ContactsDetailFragment dialog = ContactsDetailFragment.newInstance(id, name);
        dialog.show(getFragmentManager(), DIALOG);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] ids = args.getStringArray(CONTACTS_IDS);
        // Find all the wanted ids in the table
        String selection = ContactsContract.Contacts._ID + "=?";
        for (int i = 1; i < ids.length; i++) {
            selection += " OR " + ContactsContract.Contacts._ID + "=?";
        }
        return new TaggedContactsLoader(this, selection, ids);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
