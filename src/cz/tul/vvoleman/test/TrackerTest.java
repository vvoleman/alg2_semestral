package cz.tul.vvoleman.test;

import cz.tul.vvoleman.app.post.tracker.Tracker;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrackerTest {

    @Test
    @DisplayName("Vypisování zásilek")
    @Disabled
    public void print() throws StorageException, PostException {
        Tracker t = new Tracker().setPSC(40003);
        System.out.println(t.print());
    }

}