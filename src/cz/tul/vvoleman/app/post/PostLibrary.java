package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.post.storage.PostOfficeStoreInterface;

import java.util.HashMap;
import java.util.Map;

public class PostLibrary {

    private static PostOfficeStoreInterface store;

    //PSC,PostOffice
    private static Map<Integer,PostOffice> offices = new HashMap<>();
    private static Warehouse centrum = new Warehouse();

    public static PostOffice getOfficeByDistrictID(int districtID){
        if(!offices.containsKey(districtID)){
            offices.put(districtID,store.getByPSC(districtID));
        }
        return offices.get(districtID);
    }





}
