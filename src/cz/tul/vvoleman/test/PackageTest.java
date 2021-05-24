package cz.tul.vvoleman.test;

import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.utils.exception.post.UnknownPackageTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PackageTest {

    @Test
    @DisplayName("Typ podle délky")
    public void getTypeByLength() throws UnknownPackageTypeException {
        int s = 15;
        int m = 44;
        int l = 78;

        assertEquals(Package.Type.S,Package.Type.getByLength(s));
        assertEquals(Package.Type.M,Package.Type.getByLength(m));
        assertEquals(Package.Type.L,Package.Type.getByLength(l));
    }

}