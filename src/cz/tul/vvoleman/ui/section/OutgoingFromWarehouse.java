package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.Warehouse;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OutgoingFromWarehouse extends Section {

    public OutgoingFromWarehouse(Scanner sc) {
        super(sc);
    }

    @Override
    public boolean run() {
        Warehouse w = PostLibrary.getCenterWarehouse();
        Map<Integer, List<Mail>> list = w.splitMailsByPSC();

        drawUnderscore("Zásilky dle PSČ:");
        System.out.println(makeStatsString(list));

        if(w.numberOfMails() == 0){
            System.out.println("Žádné zásilky na skladu!");
            return true;
        }

        System.out.println("Jaké PSČ chcete odeslat?");
        Menu m = new Menu(new String[]{"Všechny","Jedno"});
        m.drawMenuUI();

        int sel = m.chooseOption();
        if(sel == 1){
            list.forEach((i,l)->{
                sendTo(w,i);
            });
        }else{
            boolean isOk = false;

            do{
                System.out.print("Zadejte PSČ: ");
                int psc = sc.nextInt();
                if(!sendTo(w,psc)){
                    System.out.println("Přejete si to zkusit znovu? [y/n]");
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
                }else{
                    isOk = true;
                }
            }while(!isOk);
        }
        return true;
    }

    private String makeStatsString(Map<Integer, List<Mail>> list){
        StringBuilder sb = new StringBuilder();
        list.forEach((i,m) ->{
            sb.append(i).append("(").append(m.size()).append(") ");
        });
        return sb.toString();
    }

    private boolean sendTo(Warehouse w, int psc){
        try {
            w.outgoingTransport(PostLibrary.getOfficeByPSC(psc));
            System.out.println("Zásilka do PSČ="+psc+" úspěšně odeslána!");
            return true;
        } catch (Exception e) {
            System.out.println("Nebylo možné odeslat zásilky na pobočku PSČ="+psc);
            return false;
        }
    }
}
