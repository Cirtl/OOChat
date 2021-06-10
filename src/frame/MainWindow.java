package src.frame;

import src.component.RoomCard;
import src.entity.House;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

public class MainWindow extends JFrame implements WindowListener {
    private final User user;
    private final Vector<House> rooms;

    public MainWindow(User user, Vector<House> rooms) throws HeadlessException {
        super("Main Window");
        this.user = user;
        this.rooms = rooms;

        this.setBounds(500, 250, 650, 550);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        JMenuBar menuBar = initMenu();
        JPanel infoPanel = initInfoPanel();
        JScrollPane roomsPanel = initRoomsPanel();

        this.setJMenuBar(menuBar);
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(roomsPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(this);
    }


    private JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu roomMenu = new JMenu("房间");
        JMenu logMenu = new JMenu("登录状态");
        menuBar.add(roomMenu);
        menuBar.add(logMenu);

        JMenuItem newRoomItem = new JMenuItem("新建");
        newRoomItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass = JOptionPane.showInputDialog(MainWindow.this, "请设置房间密码", "666666").trim();
                System.out.println("password is " + pass);
            }
        });
        JMenuItem logoutItem = new JMenuItem("退出登录");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.dispose();
            }
        });

        roomMenu.add(newRoomItem);
        logMenu.add(logoutItem);

        menuBar.setBorderPainted(false);

        return menuBar;
    }


    /**
     *
     * @return 个人信息面板
     */
    private JPanel initInfoPanel() {
        //个人信息
        FlowLayout infoLayout = new FlowLayout();
        JPanel infoPanel = new JPanel(infoLayout);
        JLabel infoLabel = new JLabel(user.getId());
        infoLabel.setFont(new Font(null, Font.ITALIC, 30));
        infoPanel.add(infoLabel);
        return infoPanel;
    }


    /**
     *
     * @return 房间列表面板
     */
    private JScrollPane initRoomsPanel() {
        //房间列表
        GridBagLayout bagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        JPanel container = new JPanel(new FlowLayout());
        JPanel roomsContainerPanel = new JPanel(bagLayout);
        int width = 0, height = 0;
        boolean flag = true;
        for (House room: rooms) {
            RoomCard card = new RoomCard(room);
            card.getButton().addActionListener(e -> {
                MainWindow.this.setVisible(false);
                new RoomWindow(room, user, MainWindow.this);
            });
            if (flag) {
                width = card.getWidth();
                height = card.getHeight();
                flag = false;
            }
            bagLayout.addLayoutComponent(card, c);
            roomsContainerPanel.add(card);
        }
        roomsContainerPanel.setSize(width, height);
        container.add(roomsContainerPanel);
        return new JScrollPane(
                container,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    /**
     * todo:关闭窗口时实现退出登录
     * @param e 事件
     */
    @Override
    public void windowClosed(WindowEvent e) {
        new LoginWindow();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
