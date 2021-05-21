package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;

import java.util.Scanner;

public class MenuLogout extends Section {

    private String[] options;

    public MenuLogout(Scanner sc) {
        super(sc);

        initOptions();
    }

    private void initOptions(){
        options = new String[] {
                "Sledování zásilky",
                "Přihlášení",
                "Registrace",
                "O aplikaci",
                "Ukončit"
        };
    }

    @Override
    public void run() {
        Menu menu = new Menu(options,sc);
        int selected;

        do {
            menu.drawMenuUI();

            Section s;
            selected = menu.chooseOption();
            switch (selected){
                case 1:
                    s = new AboutApp(sc);
                    break;
                case 2:
                    s = new Login(sc);
                    break;
                case 3:
                    s = new AboutApp(sc);
                    break;
                case 4:
                    s = new AboutApp(sc);
                    break;
                case 5:
                    System.out.println("Nashledanou!");
                    return;
                default:
                    s = new AboutApp(sc);
            }

            s.run();
        } while(selected != 5);
        System.out.println("Nashledanou!");
    }
}
