package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;

public interface Mail {

    public Address getSenderAddress();

    public Address getDeliveryAddress();

    public String getSenderName();

    public String getDeliveryName();

    public int getPSC();

    public int getId();

    public String getTextId();

}
