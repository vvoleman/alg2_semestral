package cz.tul.vvoleman;

import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.Bootstrap;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Section s = new Bootstrap(sc);

        s.run();
    }
}
