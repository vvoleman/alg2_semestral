package cz.tul.vvoleman;

import cz.tul.vvoleman.io.Storage;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.MenuLogout;

import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        Section s = new MenuLogout(new Scanner(System.in));

        s.run();
    }
}
