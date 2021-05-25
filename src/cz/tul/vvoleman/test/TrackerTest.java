package cz.tul.vvoleman.test;

import cz.tul.vvoleman.app.post.tracker.Tracker;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackerTest {

    @Test
    @DisplayName("Vypisování zásilek")
    public void print() throws StorageException {
        Tracker t = new Tracker().setPSC(40010);
        System.out.println(t.print());
    }

}