package storage;

import android.content.Context;

public class PersistentPictureStoreFactory {

    public static PersistentPictureStore getPersistentPictureStore(Context context) {
        return new PictureDAO(context);
    }
}
