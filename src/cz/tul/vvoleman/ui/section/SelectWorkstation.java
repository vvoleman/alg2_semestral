package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostalInterface;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.Scanner;

public class SelectWorkstation extends Section {

    public SelectWorkstation(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        drawUnderscore("Vyberte pracoviště");

        Menu menu = new Menu(new String[]{"Sklad","Pobočka pošty"});
        menu.drawMenuUI();

        int sel = menu.chooseOption();
        PostalInterface pi = null;
        
        if(sel == 1){
            pi = PostLibrary.getCenterWarehouse();
        }else if(sel == 2){
            //Vyberte poštu dle psč
            boolean isOk = false;
            do{
                System.out.print("Zadejte PSČ pošty: ");
                sel = sc.nextInt();
                try {
                    pi = PostLibrary.getOfficeByPSC(sel);
                    isOk = true;
                } catch (StorageException e) {
                    return true;
                } catch (PostException e) {
                    System.out.println("Pobočka pošty s touto PSČ neexistuje, zkuste to prosím znovu!");
                }
            }while(!isOk);
        }else{
            System.out.println("Vyskytl se problém s výběrem pracoviště, zkuste to posím pozdějí (sel="+sel+")");
            return true;
        }

        PostLibrary.setWorkstation(pi);
        return false;
    }
}
