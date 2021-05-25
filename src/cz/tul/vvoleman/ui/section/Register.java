package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.model.Role;
import cz.tul.vvoleman.app.auth.model.UserContainer;
import cz.tul.vvoleman.ui.Regex;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.EmailExistsException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.sql.SQLException;
import java.util.Scanner;

public class Register extends Section {

    public Register(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        if(Auth.isLoggedIn()){
            System.out.println("Již jste přihlášeni!");
            return true;
        }



        drawUnderscore("Registrace");
        UserContainer uc = new UserContainer();
        uc.role = Role.Customer;

        uc.firstName = getString("jméno",2,32);
        uc.lastName = getString("příjmení",2,32);
        uc.password = getPassword();
        sc.nextLine();
        uc.address = Register.getAddress("Zadejte prosím své bydliště: ",sc);
        if(uc.address == null){
            return true;
        }

        boolean isOk = false;
        do{
            try {
                uc.email = getEmail();
                Auth.register(uc);
                isOk = true;
            } catch (EmailExistsException e) {
                System.out.println("Zadaný email je již použit. Zkuste to prosím znovu!");
            } catch (AuthException|StorageException e) {
                System.out.println("Nelze vytvořit nový účet. Zkuste to prosím později!");
            }
        }while(!isOk);
        System.out.println("Účet byl úspěšně vytvořen!");
        return true;
    }

    private String getEmail(){
        boolean isOk = false;
        String email;
        do{
            System.out.print("Zadejte email: ");
            email = sc.next();
            if(false){
                System.out.println("Neplatný email, zkuste to prosím znovu!");
            }else{
                isOk = true;
            }

        }while(!isOk);

        return email;
    }

    private String getString(String name,int min, int max){
        boolean isOk = false;
        String s;
        do{
            System.out.print("Zadejte "+name+": ");
            s = sc.next();
            if(s.length() < min || s.length() > max){
                System.out.printf("Neplatná délka: řetězec musí mít délku %d-%d. Zkuste to prosím znovu!%n",min,max);
            }else{
                isOk = true;
            }

        }while(!isOk);

        return s;
    }

    private String getPassword(){
        boolean isOk = false;
        String password;
        do{
            System.out.print("Zadejte heslo: ");
            password = sc.next();
            if(!password.matches(Regex.PASSWORD)){
                System.out.println("Heslo musí obsahovat 8-32 znaků, jedno velké a jedno číslo. Zkuste to prosím znovu!");
                continue;
            }

            System.out.print("Zadejte heslo znovu: ");
            if(password.equals(sc.next())){
                isOk = true;
            }else{
                System.out.println("Hesla se neshodují, zkuste to prosím znovu!");
            }

        }while(!isOk);

        return password;
    }

    static Address getAddress(String question, Scanner sc){
        Address a = null;
        boolean isOk = false;
        String line;
        do{
            System.out.println(question);
            try {
                line = sc.nextLine();
                a = AddressLibrary.getAddressByInput(line);
                if(a == null){
                    System.out.println("Zadaná adresa neexistuje, zkuste to prosím znovu!");
                }else{
                    isOk = true;
                }
            } catch (BadAddressFormatException e) {
                System.out.println("Neplatný formát adresy, zkuste to znovu!");
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Nelze ověřit správnost adresy, zkuste to prosím později!");
                return null;
            }
        }while(!isOk);

        return a;
    }
}
