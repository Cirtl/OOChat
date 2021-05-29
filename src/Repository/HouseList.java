import javax.swing.*;
import java.util.Vector;

public class HouseList {
    private static HouseList list;
    private Vector<House> houseList;

    private HouseList() {
        houseList = new Vector<House>();
    }

    public static HouseList getInstance() {
        if (list == null) {
            list = new HouseList();
        }
        return list;
    }

    public Vector<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(Vector<House> houseList) {
        this.houseList = houseList;
    }

    public void printInfo() {
        System.out.println("HouseList:");
        for (House item : houseList) {
            System.out.println("id:" + item.getId() + " name:" + item.getName() +
                    " password:" + item.getPass() + " host:" + item.getHost_id());
        }
    }

    public House searchById(int id) {
        for (House item : houseList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void removeById(int id) {
        houseList.remove(searchById(id));
    }

    public boolean judgeHouseExist(int houseId){
        if(searchById(houseId) == null){
            JOptionPane.showMessageDialog(null, "该房间已被销毁", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
