package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.Scanner;

public class Login extends Section {

    public Login(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        if(Auth.isLoggedIn()){
            System.out.println("Již jste přihlášeni!");
            return true;
        }


        boolean isOk = false;

        drawUnderscore("Přihlášení");
        String email,password,shouldEnd;
        try{
            do{
                System.out.print("Zadejte email: ");
                email = sc.next();
                System.out.print("Zadejte heslo: ");
                password = sc.next();

                if(Auth.login(email,password)){
                    isOk = true;
                }else{
                    boolean temp;
                    System.out.println("Neplatné přihlašovací údaje, přejete si zkusit znovou? (y/n)");
                    do{
                        shouldEnd = sc.next();
                        temp = shouldEnd.equalsIgnoreCase("y") || shouldEnd.equalsIgnoreCase("n");
                        if(!temp){
                            System.out.println("Neplatný vstup, zkuste to znovu!");
                        }
                    }while(!temp);

                    if(shouldEnd.equalsIgnoreCase("n")){
                        return true;
                    }
                };

            }while(!isOk);
        }catch (StorageException e){
            System.out.println("Omlouváme se, ale nelze ověřit údaje. Zkuste to prosím později!");
            return true;
        }
        return true;
    }
}
