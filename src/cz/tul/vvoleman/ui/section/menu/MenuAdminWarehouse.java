package cz.tul.vvoleman.ui.section.menu;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.*;

import java.util.Scanner;

public class MenuAdminWarehouse extends Section {

    private Menu menu;

    public MenuAdminWarehouse(Scanner sc) {
        super(sc);
        initMenuWarehouse();
    }

    private void initMenuWarehouse(){
        menu = new Menu(new String[]{
                "Sledování zásilky",
                "Odeslání transportu",
                "Odhlásit"
        }
        );
    }

    @Override
    public boolean run() {
        Section s = null;
        menu.drawMenuUI();
        //TODO: InputMismatch - zjednoti input
        switch (menu.chooseOption()) {
            case 1:
                PostTracker pt = new PostTracker(sc);
                pt.externalData(PostLibrary.getCenterWarehouse().getMailList());
                return true;
            case 2:
                s = new OutgoingFromWarehouse(sc);
                break;
            case 3:
                PostLibrary.setWorkstation(null);
                Auth.logout();
                return true;
        }

        s.run();
        return true;
    }

}
