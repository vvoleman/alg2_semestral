package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;

import java.util.Scanner;

public class Bootstrap extends Section {

    public Bootstrap(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        Section s;
        boolean shouldContinue = true;

        do {
            if(!Auth.isLoggedIn()){
                s = new MenuLogout(sc);
            }else{
                switch (Auth.getUser().getRole()){
                    case Admin:
                        s = new MenuAdmin(sc);
                        break;
                    default:
                        s = new MenuCustomer(sc);
                }
            }

            shouldContinue = s.run();
        } while (shouldContinue);

        System.out.println("Nashledanou!");
        return false;
    }
}
