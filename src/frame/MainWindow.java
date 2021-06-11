package src.frame;

import src.component.RoomCard;
import src.dao.Repository;
import src.entity.Room;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame implements WindowListener {
    private final LoginWindow pre;

    private final User user;

    private JPanel roomsBoxPanel;

    public MainWindow(LoginWindow pre, User user) throws HeadlessException {
        super("Main Window");
        this.pre = pre;
        this.user = user;

        this.setBounds(500, 250, 650, 550);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        JMenuBar menuBar = initMenu();
        JPanel infoPanel = initInfoPanel();
        JPanel roomsPanel = initRoomsPanel();

        this.setJMenuBar(menuBar);
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(roomsPanel, BorderLayout.CENTER);

        this.setVisible(true);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(this);

        updateRooms();
    }


    /**
     * 刷新房间列表
     */
    private void updateRooms() {
        SwingWorker<Boolean, Object> updateAction = new SwingWorker<Boolean, Object>() {
            @Override
            protected Boolean doInBackground() {
                if (Repository.getRepository() != null) {
                    Repository.getRepository().updateRooms(MainWindow.this);
                    return true;
                } else {
                    return false;
                }
            }
        };
        updateAction.execute();
    }


    /**
     * 接口回调该函数
     * 刷新房间列表
     * @param roomIds 房间序号集
     * @param owners 房间所有者
     */
    public void updateRoomsView(List<Integer> roomIds, List<String> owners) {
        System.out.println("刷新房间列表");
        List<Room> rooms;
        if (roomIds == null) {
            rooms = new ArrayList<>();
        } else {
            rooms = new ArrayList<>(roomIds.size());
            for (int i = 0; i < roomIds.size(); i++) {
                User owner = new User(owners.get(i));
                rooms.add(new Room(roomIds.get(i), owner));
            }
        }
        //刷新界面
        roomsBoxPanel.removeAll();

        for (Room room: rooms) {
            RoomCard card = new RoomCard(
                    room,
                    e -> {
                        try {
                            String inPwd = JOptionPane.showInputDialog(MainWindow.this, "请输入密码（默认666666）").trim();
                            if (inPwd.equals("")) {
                                JOptionPane.showMessageDialog(MainWindow.this, "请输入密码");
                            } else {
                                SwingWorker<Boolean, Object> enterThread = new SwingWorker<Boolean, Object>() {
                                    @Override
                                    protected Boolean doInBackground() {
                                        if (Repository.getRepository() != null) {
                                            Repository.getRepository().enterRoom(MainWindow.this, room, inPwd);
                                            return true;
                                        } else
                                            return false;
                                    }
                                };
                                enterThread.execute();
                            }
                        } catch (NullPointerException exception) {
                            System.out.println(exception.getMessage());
                        }
                    },
                    e -> {
                        SwingWorker<Boolean, Object> deleteThread = new SwingWorker<Boolean, Object>() {
                            @Override
                            protected Boolean doInBackground() {
                                if (Repository.getRepository() != null) {
                                    Repository.getRepository().deleteRoom(MainWindow.this, room.getId());
                                    return true;
                                } else
                                    return false;
                            }
                        };
                        deleteThread.execute();
                    });
            roomsBoxPanel.add(card);
        }
        SwingUtilities.updateComponentTreeUI(roomsBoxPanel);
    }


    /**
     * 删除房间时执行该函数回调
     * @param result 删除房间结果
     */
    public void deleteRoomResult(int result) {
        String info;
        switch (result) {
            case 0:
                info = "删除成功";
                break;
            case -1:
                info = "房间正在被使用";
                break;
            case -2:
                info = "房间不存在";
                break;
            case -3:
                info = "只有房主才能删除房间";
                break;
            default:
                info = "删除失败，请检查网络配置";
                break;
        }
        JOptionPane.showMessageDialog(this, info, "结果", JOptionPane.WARNING_MESSAGE);
    }


    /**
     * 进入房间成功时调用
     * @param roomPort 房间序号
     */
    public void enterRoomSuccess(int roomPort) {
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> new RoomWindow(roomPort, user, MainWindow.this));
    }


    /**
     * 进入房间失败时调用
     * @param result 结果
     */
    public void enterRoomFail(int result) {
        String info;
        switch (result) {
            case -1:
                info = "你已经在房间中";
                break;
            case -2:
                info = "房间不存在";
                break;
            case -3:
                info = "密码错误";
                break;
            default:
                info = "未知错误";
                break;
        }
        JOptionPane.showMessageDialog(this, info);
    }


    /**
     * 接口回调函数
     */
    public void fetchRoomsFail() {
        JOptionPane.showMessageDialog(this, "获取房间列表信息失败");
    }


    /**
     * @return 菜单栏
     */
    private JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu roomMenu = new JMenu("房间");
        JMenu logMenu = new JMenu("登录状态");
        menuBar.add(roomMenu);
        menuBar.add(logMenu);

        JMenuItem newRoomItem = new JMenuItem("新建");
        newRoomItem.addActionListener(e -> {
            try {
                String port = JOptionPane.showInputDialog(MainWindow.this, "请设置房间序号（8003~65535）", "8003").trim();
                try{
                    int roomPort = Integer.parseInt(port);
                    String pass = JOptionPane.showInputDialog(MainWindow.this, "请设置房间密码", "666666").trim();
                    SwingWorker<Boolean, Object> newRoomThread = new SwingWorker<Boolean, Object>() {
                        @Override
                        protected Boolean doInBackground() {
                            if (Repository.getRepository() != null) {
                                Repository.getRepository().newRoom(MainWindow.this, roomPort, pass);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };
                    newRoomThread.execute();
                } catch (NumberFormatException numberFormatException) {
                    System.out.println(numberFormatException.getMessage());
                    JOptionPane.showMessageDialog(MainWindow.this, "请输入数字");
                }
            } catch (NullPointerException nullPointerException) {
                System.out.println(nullPointerException.getMessage());
            }
        });
        JMenuItem logoutItem = new JMenuItem("退出登录");
        logoutItem.addActionListener(e -> MainWindow.this.dispose());

        roomMenu.add(newRoomItem);
        logMenu.add(logoutItem);

        menuBar.setBorderPainted(false);

        return menuBar;
    }


    /**
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
     * @return 房间列表面板
     */
    private JPanel initRoomsPanel() {
        JPanel totalHolder = new JPanel(new BorderLayout());

        //顶部
        JPanel topBar = new JPanel(new FlowLayout());
        JLabel title = new JLabel("房间列表：");
        title.setFont(new Font(null, Font.BOLD, 24));
        JButton update = new JButton("刷新");
        update.setFont(new Font(null, Font.BOLD, 18));
        topBar.add(title);
        topBar.add(update);
        update.addActionListener(e -> updateRooms());

        //房间列表面板
        roomsBoxPanel = new JPanel();
        roomsBoxPanel.setLayout(new BoxLayout(roomsBoxPanel, BoxLayout.Y_AXIS));

        totalHolder.add(topBar, BorderLayout.NORTH);
        totalHolder.add(
                new JScrollPane(roomsBoxPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER
        );

        return totalHolder;
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    /**
     * @param e 事件
     */
    @Override
    public void windowClosed(WindowEvent e) {
        pre.setVisible(true);
        if (Repository.getRepository() != null) {
            Repository.getRepository().logout();
        }
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
