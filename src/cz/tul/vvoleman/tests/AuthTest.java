package cz.tul.vvoleman.tests;

import cz.tul.vvoleman.app.auth.Auth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    @Test
    @DisplayName("Hashování")
    void hash() {
        String password = "mojetajneheslo";

        assertEquals(
                "9255d313f3f60aadae470eb548d04dec0b92a7efe2ddf00b0a4b5e92a95ed0beafff09d96cca61abe43df04cbab27642005b62482af400963c1b0595b8276a48",
                Auth.hash(password),
                "Hash"
        );

    }
}