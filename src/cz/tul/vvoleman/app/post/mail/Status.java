package cz.tul.vvoleman.app.post.mail;

public enum Status {

    Registered("Registrováno",1),
    SenderOffice("Pobočka odesílatele",2),
    Warehouse("Centrální sklad",3),
    ReceiverOffice("Pobočka adresáta",4),
    Delivered("Doručeno",5);

    private final int number;
    private final String name;

    Status(String name,int number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber(){
        return number;
    }

}
