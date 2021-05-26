package cz.tul.vvoleman.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class TextFileWriter {

    public static void writeToFile(File f, String line) throws IOException {
        writeToFile(f,line,true);
    }

    public static void writeToFile(File f, String line, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f,append))) {
            bw.write(line);
            bw.newLine();
        }
    }

    public static void writeToFile(File f, List<String> lines, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f,append))) {
            for(String s : lines){
                bw.write(s);
                bw.newLine();
            }
        }
    }



}
