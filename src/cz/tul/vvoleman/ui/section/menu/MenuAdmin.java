package cz.tul.vvoleman.ui.section.menu;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.ui.section.SelectWorkstation;

import java.util.Scanner;

public class MenuAdmin extends Section{

    public MenuAdmin(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        //Vyber pracoviště
        if(!PostLibrary.hasWorkstation()){
            new SelectWorkstation(sc).run();
            if(!PostLibrary.hasWorkstation()) return true;
        }

        Section s;

        if(PostLibrary.getWorkstation() instanceof PostOffice){
            s = new MenuAdminOffice(sc);
        }else{
            s = new MenuAdminWarehouse(sc);
        }

        s.run();
        return true;
    }

}
