package cz.tul.vvoleman.app.post.mail;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.utils.exception.post.UnknownPackageTypeException;

import java.util.Arrays;

public class Package extends Mail{

    public static final String name = "package";
    private final static String suffix = "B";

    private final Type packageType;

    public Package(int id, Status status, User sender, Address receiverAddress, String receiverName, Type packageType, int locationId) {
        super(id, status, sender, receiverAddress, receiverName,locationId);
        this.packageType = packageType;
    }

    @Override
    public String getTextId() {
        return String.format("CZ%s%s%010d",packageType.suffix,suffix,id);
    }

    public Type getType(){
        return packageType;
    }


    public enum Type{
        S("S",35),
        M("M",50),
        L("L",100);

        private final String suffix;
        private final int maxLength;

        Type(String suffix, int maxLength){
            this.suffix = suffix;
            this.maxLength = maxLength;
        }

        public int getMaxLength(){
            return maxLength;
        }

        public static Type getByLength(int length) throws UnknownPackageTypeException,IllegalArgumentException {
            if(length <= 0) throw new IllegalArgumentException("Length has to be bigger than 0!");

            if(length>35){
                if(length>50){
                    if(length>100){
                        throw new UnknownPackageTypeException("Length "+length+" doesn't fit to any type of package!");
                    }
                    return L;
                }
                return M;
            }
            return S;
        }
    }

}
