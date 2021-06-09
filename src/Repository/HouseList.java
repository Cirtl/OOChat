package Repository;

import Repository.Handle.House.HandleGetHouseListOfAll;

import java.util.Map;

/**
 * 房间列表类
 * 单例模式
 * 记录房间号、房间名
 *
 * @author 郭英贤
 */
public class HouseList {
    private static HouseList list;
    private Map<Integer, String[]> houseList;

    /**
     * 私有化构造
     */
    private HouseList() {
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static HouseList getInstance() {
        if (list == null) {
            list = new HouseList();
        }
        return list;
    }

    /**
     * Getter 房间号、房间名
     *
     * @return 所有房间号、房间信息【0：name；1：pass；2：host】
     */
    public Map<Integer, String[]> getHouseList() {
        houseList = new HandleGetHouseListOfAll().queryVerify();
        return houseList;
    }

    /**
     * Setter 房间列表
     *
     * @param houseList 房间列表
     */
    public void setHouseList(Map<Integer, String[]> houseList) {
        this.houseList = houseList;
    }

    /**
     * 打印房间列表
     */
    public void printInfo() {
        System.out.println("HouseList:");
        for (int item : this.getHouseList().keySet()) {
            System.out.println("id: " + item + " info: " + houseList.get(item));
        }
    }

}
