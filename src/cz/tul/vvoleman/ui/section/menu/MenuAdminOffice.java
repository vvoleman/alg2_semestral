package cz.tul.vvoleman.ui.section.menu;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.AboutApp;
import cz.tul.vvoleman.ui.section.IncomingPersonalMail;
import cz.tul.vvoleman.ui.section.Login;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.Scanner;

public class MenuAdminOffice extends Section {

    private Menu menu;
    private PostOffice po;

    public MenuAdminOffice(Scanner sc) {
        super(sc);
        po = (PostOffice) PostLibrary.getWorkstation();
        initMenu();
    }

    private void initMenu() {
        menu = new Menu(new String[]{
                "Zásilky na skladě (" + po.numberOfMails() + ")",
                "Přijmutí zásilky",
                "Odeslat transport do centrálního skladu (" + po.numberOfOutgoing() + ")",
                "Roznést zásilky v našem PSČ (" + (po.numberOfMails()-po.numberOfOutgoing()) + ")",
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
                s = new AboutApp(sc);
                break;
            case 2:
                s = new IncomingPersonalMail(sc);
                break;
            case 3:
                sendToWarehouse();
                return true;
            case 4:
                if ((po.numberOfMails()-po.numberOfOutgoing()) == 0) {
                    System.out.println("Žádné zásilky pro naše PSČ");
                }else{
                    try {
                        po.localOutgoingTransport();
                        System.out.println("Zásilky rozneseny!");
                    } catch (StorageException e) {
                        System.out.println("Nebylo možné roznést zásilky!");
                    }
                }
                return true;
            case 5:
                PostLibrary.setWorkstation(null);
                Auth.logout();
                return true;
        }

        s.run();
        return true;
    }

    private void sendToWarehouse() {
        int outgoing = po.numberOfOutgoing();
        if (outgoing == 0) {
            System.out.println("Žádné zásilky k odeslání!");
        } else {
            try {
                po.outgoingTransport();
                System.out.println("Zásilky (" + outgoing + ") byly úspěšně odeslány do centrálního skladu!");
            } catch (StorageException e) {
                System.out.println("Nelze odeslat transport, zkuste to prosím později! - " + e);
            }
        }
    }

}
