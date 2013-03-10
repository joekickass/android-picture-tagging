package se.otaino2.ass2.contacts;

import se.otaino2.ass2.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ContactsDetailFragment extends DialogFragment implements LoaderCallbacks<Cursor> {
    
    private static final int LOADER_PHONENBR_ID = 102;
    private static final int LOADER_ADDRESS_ID = 103;
    
    private static final String CONTACTS_ID = "contacts_id";
    private static final String CONTACTS_NAME = "contacts_name";
    
    private View content;
    
    private String name;
    private String phone;
    private String address;
    
    static ContactsDetailFragment newInstance(String id, String name) {
        ContactsDetailFragment frag = new ContactsDetailFragment();
        Bundle args = new Bundle();
        args.putString(CONTACTS_ID, id);
        args.putString(CONTACTS_NAME, name);
        frag.setArguments(args);
        return frag;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String id = getArguments().getString(CONTACTS_ID);
        name = getArguments().getString(CONTACTS_NAME);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        content = inflater.inflate(R.layout.fragment_dialog, null);
        
        Bundle bundle = new Bundle();
        bundle.putString(CONTACTS_ID, id);
        getLoaderManager().initLoader(LOADER_PHONENBR_ID, bundle, this);
        getLoaderManager().initLoader(LOADER_ADDRESS_ID, bundle, this);
        
        // Get address
        
        return new AlertDialog.Builder(getActivity())
                .setTitle(name)
                .setView(content)
                .create();
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String contactId = args.getString(CONTACTS_ID);
        switch (id) {
        case LOADER_PHONENBR_ID:
            return new TaggedContactsPhoneNumberLoader(getActivity(), contactId);
            
        case LOADER_ADDRESS_ID:
            return new TaggedContactsAddressLoader(getActivity(), contactId);

        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
        case LOADER_PHONENBR_ID:
            setPhone(data);
            break;
        case LOADER_ADDRESS_ID:
            setAddress(data);
            break;
        default:
            throw new IllegalArgumentException();
        }        
    }

    private void setPhone(Cursor data) {
        data.moveToFirst();
        if (!data.isAfterLast()) {
            phone = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        setPhoneNumber(phone);
    }
    

    private void setAddress(Cursor data) {
        data.moveToFirst();
        if (!data.isAfterLast()) {
            address = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        setAddress(address);
    }
    
    private void setPhoneNumber(String phone) {
        TextView titleView = (TextView) content.findViewById(R.id.phoneTitle);
        TextView phoneView = (TextView) content.findViewById(R.id.phone);
        
        if (phone == null || phone.isEmpty()) {
            titleView.setVisibility(View.GONE);
            phoneView.setVisibility(View.GONE);
            return;
        }
        
        titleView.setVisibility(View.VISIBLE);
        phoneView.setVisibility(View.VISIBLE);
        phoneView.setText(phone);
    }
    
    private void setAddress(String address) {
        TextView titleView = (TextView) content.findViewById(R.id.addressTitle);
        TextView addrView = (TextView) content.findViewById(R.id.address);
        
        if (address == null || address.isEmpty()) {
            titleView.setVisibility(View.GONE);
            addrView.setVisibility(View.GONE);
            return;
        }
        
        titleView.setVisibility(View.VISIBLE);
        addrView.setVisibility(View.VISIBLE);
        addrView.setText(address);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
        case LOADER_PHONENBR_ID:
            clearContactInfo();
            break;
        case LOADER_ADDRESS_ID:
            clearContactInfo();
            break;
        default:
            throw new IllegalArgumentException();
        }        
    }
    
    private void clearContactInfo() {
        phone = null;
        address = null;
    }
}
