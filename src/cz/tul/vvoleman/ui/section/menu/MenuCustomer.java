package cz.tul.vvoleman.ui.section.menu;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.AboutApp;
import cz.tul.vvoleman.ui.section.MailRegister;
import cz.tul.vvoleman.ui.section.PostTracker;

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
                s = new PostTracker(sc, PostTracker.Filter.User, Auth.getUser().getId());
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
