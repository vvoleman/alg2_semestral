package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.post.PostOffice;

public interface PostOfficeStoreInterface {

    public PostOffice getByPSC(int psc);

    //public PostOffice getByDistrict(int districtId);

}
