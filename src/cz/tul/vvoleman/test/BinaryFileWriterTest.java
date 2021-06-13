package cz.tul.vvoleman.test;

import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.io.BinaryFileWriter;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryFileWriterTest {

    @Test
    void saveUsersBinary() throws StorageException, UnknownUserException, IOException {
        List<User> users= Datastore.getStorageContainer().authStore.get(new int[]{1, 2, 3});
        File f = new File(Datastore.getFileStorageFolder()+"/users_bin");

        BinaryFileWriter.saveUsersBinary(f,users);
    }
}