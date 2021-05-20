package cz.tul.vvoleman.ui.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter {

    public static boolean writeToFile(File f, String line) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f,true))) {
            bw.write(line);
        }
        return true;
    }

}
