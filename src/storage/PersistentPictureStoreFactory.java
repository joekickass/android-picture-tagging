package storage;

import android.content.Context;

/**
 * Creates PersistentPictureStores
 * 
 * @author otaino-2
 *
 */
public class PersistentPictureStoreFactory {

    public static PersistentPictureStore getPersistentPictureStore(Context context) {
        return new PictureDAO(context);
    }
}
