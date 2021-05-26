package cz.tul.vvoleman.app.post.mail;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.app.post.PostLibrary;

import java.time.LocalDateTime;

public class Letter extends Mail{

    public static final String name = "letter";
    public static final String suffix = "P";

    private final Letter.Type letterType;

    public Letter(int id, Status status, User sender, Address receiverAddress, String receiverName, Type letterType, int locationId, LocalDateTime lastChangedAt) {
        super(id, status, sender, receiverAddress, receiverName,locationId,lastChangedAt);
        this.letterType = letterType;
    }

    @Override
    public String getTextId() {
        return PostLibrary.makeTextId(id,name,letterType.getSuffix());
    }

    public Type getType(){
        return letterType;
    }

    @Override
    public String getTypeString() {
        return name;
    }

    @Override
    public String getInfoString() {
        return letterType.toString();
    }

    public enum Type{
        //Doporučené psaní
        Recorded("D"),
        Valuable("C");

        private final String suffix;

        public String getSuffix(){
            return suffix;
        }

        Type(String suffix){
            this.suffix = suffix;
        }
    }

}
