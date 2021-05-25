package cz.tul.vvoleman.io;

import cz.tul.vvoleman.app.auth.storage.AuthStoreInterface;
import cz.tul.vvoleman.app.post.storage.PostStoreInterface;

public class StorageContainer {

    public AuthStoreInterface authStore;
    public PostStoreInterface postStore;

    public StorageContainer(AuthStoreInterface authStore, PostStoreInterface postStore) {
        this.authStore = authStore;
        this.postStore = postStore;
    }
}
