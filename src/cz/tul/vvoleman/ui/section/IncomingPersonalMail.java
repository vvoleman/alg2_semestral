package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.Scanner;

public class IncomingPersonalMail extends Section {

    public IncomingPersonalMail(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        String id = "";
        boolean shouldContinue = false;
        Mail m;
        do{
            try {
                System.out.print("Zadejte textové ID zásilky: ");
                id = sc.next();
                m = PostLibrary.getMailByTextId(id);
                if(m.getStatus() == Status.Registered){
                    getPostOffice().incomingPersonalMail(m);
                }else{
                    System.out.println("Tato zásilka je již zpracována!");
                    return true;
                }

                shouldContinue = true;
            } catch (PostException e) {
                System.out.println("Zásilka s ID "+id+" nenalezena! Chcete to zkusit znovu? [y/n]");
                boolean temp;
                String input;
                do{
                    sc.nextLine();
                    input = sc.next();
                    if(input.equalsIgnoreCase("n")){
                        return true;
                    }
                    temp = input.equalsIgnoreCase("y");
                    if(!temp){
                        System.out.println("Neplatný vstup, zkuste to znovu!");
                    }

                }while(!temp);
            } catch (StorageException e) {
                System.out.println("Nelze načíst zásilky, zkuste to prosím později! - "+e.getMessage());
                return true;
            }
        }while(!shouldContinue);

        System.out.println("Zásilka přijata!");
        return true;
    }

    private PostOffice getPostOffice(){
        return ((PostOffice) PostLibrary.getWorkstation());
    }

}
