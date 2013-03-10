package storage;

import java.util.List;

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
