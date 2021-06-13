package cz.tul.vvoleman.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TextFileReader {

    /**
     * Reads file
     * @param file file
     * @param regexSeparator separator
     * @param header does the file have first-line header?
     * @return List of lines
     * @throws IOException when something goes wrong
     */
    public static List<String[]> readFile(File file, String regexSeparator, boolean header) throws IOException {
        return readWithFilter(file,regexSeparator,header,(s) -> true,-1);
    }

    /**
     * Reads all lines from file
     * @param f File
     * @return List of lines
     * @throws IOException Problem with file
     */
    public static List<String> readFileLines(File f) throws IOException {
        List<String> data = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            while((line = br.readLine()) != null){
                data.add(line);
            }
        }
        return data;
    }

    /**
     * Returns lines which meet filter's requirements
     * @param file File
     * @param regexSeparator separator
     * @param header Does the file have first line header?
     * @param filter Predicate<String[]> that serves as filter for individual lines
     * @param limit How many lines in do you want
     * @return list of lines
     * @throws IOException File not found
     */
    public static List<String[]> readWithFilter(File file, String regexSeparator, boolean header, Predicate<String[]> filter, int limit) throws IOException {
        List<String[]> data = new ArrayList<>();

        //Po opuštění try catch se resource uzavře
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;

            //Přeskočí hlavičku
            if(header) br.readLine();

            //Do té doby, dokuď je možné načíst další řádek
            while((line = br.readLine()) != null){
                //rozdělí řádek podle regulárního výrazu
                String[] parts = line.split(regexSeparator);

                //Pokud je value null, tak to přiradíme
                //Pokud se part rovná value, přiřadíme
                if(parts.length > 1 && filter.test(parts)){
                    data.add(parts);
                }

                //Pokud list dosáhl požadovaného limitu
                if(data.size() == limit) break;

            }
        }

        return data;
    }

    /**
     * Reads last line from file
     * @param file File
     * @param regexSeparator Separator
     * @return parts
     * @throws IOException Problem with file
     */
    public static String[] readLastLine(File file, String regexSeparator) throws IOException {
        String line = "";

        //Po opuštění try catch se resource uzavře
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String temp;
            while((temp = br.readLine()) != null) line = temp;
        }

        return line.split(regexSeparator);
    }

    /**
     * Returns index of line by filter
     * @param f File
     * @param regexSeparator separator
     * @param header Is there a header?
     * @param filter Predicat<String[]>
     * @return index
     * @throws IOException Problem with file
     */
    public static int getIndexOfLine(File f, String regexSeparator,boolean header,Predicate<String[]> filter) throws IOException {
        int counter = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;

            //Přeskočí hlavičku
            if(header) br.readLine();

            //Do té doby, dokuď je možné načíst další řádek
            while((line = br.readLine()) != null){
                counter++;
                //rozdělí řádek podle regulárního výrazu
                String[] parts = line.split(regexSeparator);

                //Pokud je value null, tak to přiradíme
                //Pokud se part rovná value, přiřadíme
                if(parts.length > 1 && filter.test(parts)){
                    return counter;
                }
            }
        }

        return -1;
    }


}
