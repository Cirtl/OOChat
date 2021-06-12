package src.frame;

import src.component.MemberCard;
import src.dao.Repository;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class RoomWindow extends JFrame implements WindowListener {
    private final User currentUser;

    private final MainWindow mainWindow;

    private JTextArea recordArea;
    private JTextArea inputArea;

    private JButton leaveButton;
    private JButton sendButton;
    private JButton clearButton;
    private JPanel listPanel;

    public RoomWindow(int roomPort, User currentUser, MainWindow mainWindow) throws HeadlessException {
        super(String.valueOf(roomPort));
        this.currentUser = currentUser;
        this.mainWindow = mainWindow;


        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        this.setLayout(gridBag);

        constraints.gridwidth = 5;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weightx = 5;
        JPanel leftPanel = setLeftPanel();
        gridBag.setConstraints(leftPanel, constraints);

        constraints.gridwidth = 2;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weightx = 2;
        JPanel rightPanel = setRightPanel();
        gridBag.addLayoutComponent(rightPanel, constraints);

        this.add(leftPanel);
        this.add(rightPanel);

        this.setResizable(true);
        this.setVisible(true);
        this.setBounds(500, 250, 650, 550);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(this);

        updateList();
    }


    /**
     * @return 窗口左侧界面
     */
    private JPanel setLeftPanel() {
        GridBagLayout layout = new GridBagLayout();
        JPanel leftPanel = new JPanel(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;


        Font textFont = new Font(null, Font.PLAIN, 14);

        //聊天记录区域
        recordArea = new JTextArea(30, 30);
        recordArea.setFont(textFont);
        recordArea.setFocusable(false);
        JScrollPane recordPanel = new JScrollPane(recordArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridheight = 18;
        c.weighty = 9;
        layout.addLayoutComponent(recordPanel, c);

        //输入区域
        inputArea = new JTextArea(5, 30);
        inputArea.setFont(textFont);
        JScrollPane inputPanel = new JScrollPane(inputArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridheight = 3;
        c.weighty = 3;
        layout.addLayoutComponent(inputPanel, c);

        //底部按钮操作区域
        GridBagLayout btnLayout = new GridBagLayout();
        JPanel btnPanel = new JPanel(btnLayout);
        Font btnFont = new Font(null, Font.BOLD, 12);
        GridBagConstraints btnC = new GridBagConstraints();
        btnC.fill = GridBagConstraints.NONE;
        btnC.gridwidth = 3;
        btnC.weightx = 1;
        btnC.gridheight = GridBagConstraints.REMAINDER;
        leaveButton = new JButton("离开");
        leaveButton.setFont(btnFont);
        btnLayout.addLayoutComponent(leaveButton, btnC);
        clearButton = new JButton("清空");
        clearButton.setFont(btnFont);
        btnLayout.addLayoutComponent(clearButton, btnC);
        sendButton = new JButton("发送");
        sendButton.setFont(btnFont);
        btnLayout.addLayoutComponent(sendButton, btnC);
        btnPanel.setBackground(Color.white);
        btnPanel.add(leaveButton);
        btnPanel.add(clearButton);
        btnPanel.add(sendButton);
        setActionListener();

        c.gridheight = 1;
        c.weighty = 1;
        layout.addLayoutComponent(btnPanel, c);

        leftPanel.add(recordPanel);
        leftPanel.add(inputPanel);
        leftPanel.add(btnPanel);

        return  leftPanel;
    }


    /**
     * @return 窗口右侧界面
     */
    private JPanel setRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        //抬头
        JPanel funcPanel = new JPanel();
        JLabel titleLabel = new JLabel("聊天室成员：");
        Font font = new Font(null, Font.ITALIC, 20);
        titleLabel.setFont(font);
        JButton update = new JButton("刷新");
        update.addActionListener(e -> updateList());
        funcPanel.setLayout(new BoxLayout(funcPanel, BoxLayout.X_AXIS));
        funcPanel.add(titleLabel);
        funcPanel.add(Box.createHorizontalStrut(15));
        funcPanel.add(update);
        rightPanel.add(funcPanel, BorderLayout.NORTH);

        //房间内用户列表
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        rightPanel.add(
                new JScrollPane(listPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER
        );

        return rightPanel;
    }


    /**
     * 为按钮设置监听器
     */
    private void setActionListener() {
        leaveButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    RoomWindow.this,
                    "确认离开？",
                    "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (result == JOptionPane.YES_OPTION){
                leaveRoom();
            }
        });
        clearButton.addActionListener(e -> inputArea.setText(""));
        sendButton.addActionListener(e -> sendMsg(false, null));
    }


    /**
     * 发送消息，区分私聊和群发
     * @param isWhisper 是否是私聊消息
     * @param receiver 私聊的接收者
     */
    private void sendMsg(boolean isWhisper, String receiver) {
        String content = inputArea.getText();
        if (content.equals("")) {
            JOptionPane.showMessageDialog(RoomWindow.this, "消息内容不能为空", "提醒", JOptionPane.WARNING_MESSAGE);
        } else {
            SwingWorker<Boolean, Object> sendThread = new SwingWorker<Boolean, Object>() {
                @Override
                protected Boolean doInBackground() {
                    if (Repository.getRepository() != null) {
                        if (! isWhisper) {
                            Repository.getRepository().sendMsg(RoomWindow.this, content);
                        } else {
                            Repository.getRepository().whisperMsg(RoomWindow.this, content, receiver);
                        }
                        return true;
                    } else
                        return false;
                }
            };
            sendThread.execute();
            inputArea.setText("");
            if (isWhisper) {
                updateRecord(currentUser.getId(), receiver, content, true);
            }
        }
    }


    /**
     * 刷新用户列表信息
     */
    private void updateList() {
        SwingWorker<Boolean, Object> fetchThread = new SwingWorker<Boolean, Object>() {
            @Override
            protected Boolean doInBackground()  {
                if (Repository.getRepository() != null) {
                    Repository.getRepository().updateMembers(RoomWindow.this);
                    return true;
                } else
                    return false;
            }
        };
        fetchThread.execute();
    }


    /**
     * 获取用户成功时调用
     * @param chatters 用户列表
     */
    public void fetchUserListSuccess(List<String> chatters) {
        if (chatters == null) {
            chatters = new ArrayList<>();
        }
        listPanel.removeAll();
        for (String name : chatters) {
            MemberCard card = new MemberCard(
                    name,
                    e -> {
                        if (name.equals(currentUser.getId()))
                            JOptionPane.showMessageDialog(RoomWindow.this, "不能和自己私聊");
                        else
                            sendMsg(true, name);
                    },
                    e -> {
                        JOptionPane.showMessageDialog(RoomWindow.this, "只有房主能够踢人");
                        SwingWorker<Boolean, Object> kickThread = new SwingWorker<Boolean, Object>() {
                            @Override
                            protected Boolean doInBackground() {
                                if (Repository.getRepository() != null) {
                                    Repository.getRepository().kickUser(RoomWindow.this, name);
                                    return true;
                                } else
                                    return false;
                            }
                        };
                        kickThread.execute();
                    }
            );
            listPanel.add(card);
        }
        SwingUtilities.updateComponentTreeUI(listPanel);
    }


    /**
     * 获取用户失败时调用
     */
    public void fetchFail() {
        JOptionPane.showMessageDialog(this, "获取聊天者信息失败");
    }


    /**
     * 离开房间
     */
    private void leaveRoom() {
        SwingWorker<Boolean, Object> leaveThread = new SwingWorker<Boolean, Object>() {
            @Override
            protected Boolean doInBackground() {
                if (Repository.getRepository() != null) {
                    Repository.getRepository().leaveRoom(RoomWindow.this);
                    return true;
                } else
                    return false;
            }
        };
        leaveThread.execute();
    }


    private boolean isNormalLeave = false;
    /**
     * 用户点击离开时回调该函数
     * @param result 处理结果
     */
    public void leaveOk(int result) {
        String info;
        switch (result) {
            case 0:
                info = "房间退出成功";
                break;
            case 1:
                info = "您已被房主移除群聊";
                break;
            case 2:
                info = "房间已被关闭";
                break;
            default:
                info = "未知错误";
                break;
        }
        JOptionPane.showMessageDialog(null, info);
        isNormalLeave = true;
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }


    /**
     * 离开失败时回调此函数
     */
    public void leaveFail() {
        JOptionPane.showMessageDialog(this, "离开失败，请检查自己的网络设置");
    }


    /**
     * 刷新聊天消息栏
     * @param sender 发送者
     * @param receiver 接收者
     * @param content 消息内容
     * @param isWhisper 是否为私聊消息
     */
    public synchronized void updateRecord(String sender, String receiver, String content, boolean isWhisper) {
        StringBuilder builder = new StringBuilder();
        if (isWhisper) {
            if (receiver == null) {
                builder.append(sender);
                builder.append(" 私聊你：\n        ");
                builder.append(content);
                builder.append("\n");
            } else {
                builder.append("你");
                builder.append(" 私聊 ");
                builder.append(receiver);
                builder.append("：\n        ");
                builder.append(content);
                builder.append("\n");
            }
        } else {
            builder.append(sender);
            builder.append("：\n        ");
            builder.append(content);
            builder.append("\n");
        }
        recordArea.setText(
                recordArea.getText() +
                builder.toString());
    }



    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (! isNormalLeave) {
            leaveRoom();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        mainWindow.setVisible(true);
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
