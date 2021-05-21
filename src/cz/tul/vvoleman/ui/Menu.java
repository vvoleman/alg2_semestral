package cz.tul.vvoleman.ui;

import cz.tul.vvoleman.utils.tools.StringLibrary;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private String[] options;
    private Scanner sc;

    public Menu(String[] options,Scanner sc){
        this.options = options;
        this.sc = sc;

        addNumbers();
    }

    public Menu(String[] options){
        this(options,new Scanner(System.in));
    }

    private void addNumbers(){
        for (int i = 0; i < options.length; i++) {
            options[i] = (i+1)+". "+options[i];
        }
    }

    public void drawMenuUI(){
        System.out.println(StringLibrary.drawMenu(options));
    }

    public int chooseOption(){
        int option = -1;
        boolean isOk = false;
        do{
            try{
                option = sc.nextInt();
                if(option > 0 && option <= options.length){
                    isOk = true;
                }else{
                    System.out.println("Váš vstup neodpovídá žádné možnosti, zkuste to znovu!");
                }
            } catch (InputMismatchException e){
                System.out.println("Vstup musí být číslo, zkuste to znovu!");
            }
        } while(!isOk);

        return option;
    }

}
