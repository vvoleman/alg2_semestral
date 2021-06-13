package cz.tul.vvoleman.io;

import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.resource.Datastore;

import java.io.*;
import java.util.List;

public class BinaryFileWriter {

    public static void saveUsersBinary(File f, List<User> users) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))) {
            for (User u : users) {
                dos.writeInt(u.getId());
                dos.writeUTF(u.getEmail());
                dos.writeUTF(u.getFirstname());
                dos.writeUTF(u.getLastname());
                dos.writeUTF(u.getCreatedAt().format(Datastore.getDtf()));
                dos.writeUTF(u.getRole().getName());
            }
        }
    }

}
