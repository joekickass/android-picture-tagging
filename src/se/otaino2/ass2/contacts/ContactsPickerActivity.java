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
 * Class responsible for displaying a list of all contacts on the phone and let the user
 * pick on of them.
 * 
 * @author otaino-2
 *
 */
public class ContactsPickerActivity extends ListActivity implements LoaderCallbacks<Cursor> {

    public static final String CONTACTS_ID = "contacts_id";
    
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ContactsAdapter(this);
        setListAdapter(adapter);
        
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String contactId = getContactId(position);
        Intent i = createResult(contactId);
        setResult(RESULT_OK, i);
        finish();
    }
    
    private String getContactId(int position) {
        Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
        if (cursor != null && !cursor.isClosed()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        } 
        throw new IllegalStateException("Meta information for contact is not available!");
    }

    /**
     * Creates an intent holding the specified contact
     * @param name the name of the contact
     * @return an Intent
     */
    private Intent createResult(String id) {
        Intent intent = new Intent();
        intent.putExtra(CONTACTS_ID, id);
        return intent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ContactsLoader(this);
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
