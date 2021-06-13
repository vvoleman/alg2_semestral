package cz.tul.vvoleman.test.io;

import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.io.TextFileReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderTest {

    @Test
    @Disabled
    @DisplayName("Kontrola čtení")
    void readFile() throws IOException {
        List<String[]> result = TextFileReader.readFile(
                new File(Datastore.getAuthStorageFile()),
                ",",
                true
        );

        List<String[]> correct = new ArrayList<>();
        correct.add(new String[]{"1", "vojtavol@email.cz", "fds", "Vojtěch", "Voleman", "17901448", "2021-05-10 16:20:00", "2021-05-10 16:20:00", "1", "admin"});
        correct.add(new String[]{"2", "vvoleman@email.cz", "fdst", "Vojtěch", "Voleman", "17901448", "2021-05-10 16:20:00", "2021-05-10 16:20:00", "1", "admin"});

        for (int i = 0; i < correct.size(); i++) {
            assertArrayEquals(correct.get(i),result.get(i),"Kontrola indexu "+i);
        }
    }

    @Test
    @DisplayName("Kontrola filterů")
    @Disabled
    void readWithFilter() throws IOException {
        List<Predicate<String[]>> tests = new ArrayList<>();
        tests.add((parts) -> parts[0].equalsIgnoreCase("1"));
        tests.add((parts) -> parts[0].equalsIgnoreCase("1") && parts[1].equalsIgnoreCase("vojtavol@email.cz"));



        List<String[]> correct = new ArrayList<>();
        correct.add(new String[]{"1", "vojtavol@email.cz", "fds", "Vojtěch", "Voleman", "17901448", "2021-05-10 16:20:00", "2021-05-10 16:20:00", "1", "admin"});

        List<String[]> result;
        for (int i = 0; i < tests.size(); i++) {
            result = TextFileReader.readWithFilter(
                    new File(Datastore.getAuthStorageFile()),
                    ",",
                    true,
                    tests.get(i),
                    1
            );
            assertArrayEquals(correct.get(0),result.get(0),"Kontrola testu "+i);
        }

    }

    @Test
    @DisplayName("Poslední řádek")
    @Disabled
    void readLastLine() throws IOException {
        String correct = "2,vvoleman@email.cz,fdst,Vojtěch,Voleman,17901448,2021-05-10 16:20:00,2021-05-10 16:20:00,1,admin";
        assertEquals(correct,TextFileReader.readLastLine(new File(Datastore.getAuthStorageFile()),","),"Kontrola posledního řádku");
    }
}