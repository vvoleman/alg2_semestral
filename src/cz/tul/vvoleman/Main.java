package cz.tul.vvoleman;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        String[] data = {"1","vojtavol@email.cz","heslo123"};

        Predicate<String[]> test = parts -> parts[0].equalsIgnoreCase("1");


    }
}
