package cz.tul.vvoleman.ui;

import cz.tul.vvoleman.utils.tools.StringLibrary;

import java.util.Scanner;

public abstract class Section {

    protected Scanner sc;

    protected Section(Scanner sc) {
        this.sc = sc;
    }

    public abstract boolean run();

    protected void drawUnderscore(String s){
        System.out.println(s);
        System.out.println(StringLibrary.drawLine(s.length(),'-'));
    }

}
