package src.frame;

import src.entity.House;

import javax.swing.*;
import java.awt.*;

public class HouseWindow extends JFrame {
    private House house;

    public HouseWindow(House house) throws HeadlessException {
        super(house.getName());
        this.house = house;
    }
}
