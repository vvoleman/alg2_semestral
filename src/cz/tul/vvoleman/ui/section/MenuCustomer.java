package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;

import java.util.Scanner;

public class MenuCustomer extends Section {

    private Menu menu;

    public MenuCustomer(Scanner sc) {
        super(sc);

        initMenu();
    }

    private void initMenu() {
        menu = new Menu(new String[]{
                "Sledování zásilky",
                "Registrace zásilky",
                "Odhlásit"
            }
        );
    }

    @Override
    public boolean run() {
        Section s = null;
        menu.drawMenuUI();
        switch (menu.chooseOption()) {
            case 1:
                s = new AboutApp(sc);
                break;
            case 2:
                s = new MailRegister(sc);
                break;
            case 3:
                Auth.logout();
                return true;
        }

        s.run();
        return true;
    }
}
