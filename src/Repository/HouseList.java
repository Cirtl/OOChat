package Repository;

import javax.swing.*;
import java.util.Map;
import java.util.Vector;

public class HouseList {
    private static HouseList list;
    private Map<String,String> houseList;

    private HouseList() {
    }

    public static HouseList getInstance() {
        if (list == null) {
            list = new HouseList();
        }
        return list;
    }

    public Map<String,String> getHouseList() {
        houseList = new HandleSearchHouseListOfAll().queryVerify();
        return houseList;
    }

    public void setHouseList(Map<String,String> houseList) {
        this.houseList = houseList;
    }

    public void printInfo() {
        System.out.println("HouseList:");
        for (String item : this.getHouseList().keySet()) {
            System.out.println("id: " + item + " name: " + houseList.get(item));
        }
    }

}
