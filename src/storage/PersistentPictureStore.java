package storage;

import java.util.List;

/**
 * Allows storing and fetching tags for specific picture Uri's
 * 
 * @author otaino-2
 *
 */
public interface PersistentPictureStore {
    
    /**
     * Creates a new tag in the persistent storage associated
     * with the specified picture.
     * 
     * @param pictureUri
     * @param tag
     */
    void addTag(String pictureUri, String tag);
    
    /**
     * Get all tags associated with the specified picture.
     * 
     * @param pictureUri
     */
    List<String> getTags(String pictureUri);
}
