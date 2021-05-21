package cz.tul.vvoleman.io;

import cz.tul.vvoleman.app.auth.storage.AuthStoreInterface;

public class StorageContainer {

    public AuthStoreInterface authStore;

    public StorageContainer(AuthStoreInterface authStore) {
        this.authStore = authStore;
    }
}
