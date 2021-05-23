package cz.tul.vvoleman;

import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.Bootstrap;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Section s = new Bootstrap(new Scanner(System.in));

        s.run();
    }
}
