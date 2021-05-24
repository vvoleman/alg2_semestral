package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;

import java.util.Scanner;

public class MenuLogout extends Section {

    private Menu menu;

    public MenuLogout(Scanner sc) {
        super(sc);

        initMenu();
    }

    private void initMenu() {
        menu = new Menu(new String[]{
                "Sledování zásilky",
                "Přihlášení",
                "Registrace",
                "O aplikaci",
                "Ukončit"
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
                s = new Login(sc);
                break;
            case 3:
                s = new Register(sc);
                break;
            case 4:
                s = new AboutApp(sc);
                break;
            case 5:
                return false;
        }

        s.run();
        return true;
    }
}
