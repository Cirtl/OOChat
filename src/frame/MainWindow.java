package src.frame;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() throws HeadlessException {
        super("Main Window");
        this.setBounds(500, 250, 650, 450);
        this.setLayout(new GridLayout(3, 1));
        this.setResizable(false);

        this.setVisible(true);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
