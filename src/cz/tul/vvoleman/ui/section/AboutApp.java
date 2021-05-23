package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.ui.Section;

import java.util.Scanner;

public class AboutApp extends Section {

    //private final String name = "O aplikaci";

    public AboutApp(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        drawUnderscore("O aplikaci");
        System.out.println("Aplikace slouží k předregistraci a pohybu zásilek");
        System.out.println("Vytvořeno v rámci kurzu ALG2");
        System.out.println("Autor: Vojtěch Voleman, 2021");

        return true;
    }
}
