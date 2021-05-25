package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.mail.Letter;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.post.UnknownPackageTypeException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.Scanner;

public class MailRegister extends Section {

    public MailRegister(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        Menu mainType = new Menu(new String[]{"Psaní", "Balíček"});

        MailContainer mc = new MailContainer();
        System.out.print("Zadejte jméno příjemce: ");
        sc.nextLine();
        mc.receiverName = sc.nextLine();
        mc.receiverAddress = Register.getAddress("Zadejte prosím adresu příjemce: ",sc);
        mc.sender = Auth.getUser();
        mc.status = Status.Registered;

        drawUnderscore("Vyberte typ zásilky");
        mainType.drawMenuUI();
        int sel = mainType.chooseOption();

        if(sel == 1){
            mc.type = Letter.name;

            drawUnderscore("Vyberte typ psaní");
            Menu subType = new Menu(new String[]{"Doporučené psaní","Cenné psaní"});
            subType.drawMenuUI();
            sel = subType.chooseOption();
            Letter.Type s;

            if(sel == 1){
                s = Letter.Type.Recorded;
            }else{
                s = Letter.Type.Valuable;
            }

            mc.info = s.toString();

        }else if(sel == 2){
            mc.type = Package.name;
            boolean isOk = false;
            Package.Type type = null;
            int length;
            do{
                System.out.print("Zadejte délku největší strany balíčku (cm): ");
                length = sc.nextInt();
                try {
                    type = Package.Type.getByLength(length);
                    isOk = true;
                } catch (UnknownPackageTypeException e) {
                    System.out.println("Balíček má větší délku strany, než je podporováno, omlouváme se!");
                    return true;
                }catch (IllegalArgumentException e){
                    System.out.println("Velikost balíčku musí být větší než 0, zkuste to prosím znovu!");
                }
            }while(!isOk);

            System.out.println("Pro velikost strany "+length+" cm vybrána kategorie "+type.name());

            mc.info = type.toString();

        }else{
            System.out.println("Nastala chyba, zkuste to prosím později!");
            return true;
        }

        try {
            PostLibrary.create(mc);
        } catch (StorageException e) {
            System.out.println("Nebylo možné vytvořit zásilku, zkuste to prosím později!");
        }

        return true;
    }
}
