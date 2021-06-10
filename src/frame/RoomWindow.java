package src.frame;

import src.entity.House;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RoomWindow extends JFrame implements ActionListener, WindowListener {
    private final House room;
    private final User currentUser;

    private final MainWindow mainWindow;

    private JTextArea recordArea;
    private JTextArea inputArea;

    private JButton leaveButton;
    private JButton sendButton;
    private JButton clearButton;

    public RoomWindow(House room, User currentUser, MainWindow mainWindow) throws HeadlessException {
        super(room.getName());
        this.room = room;
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
    }

    /**
     *
     * @return 窗口左侧panel
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
     * 为按钮设置监听器
     */
    private void setActionListener() {
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        RoomWindow.this,
                        "确认离开？",
                        "提示",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (result == JOptionPane.YES_OPTION){
                    RoomWindow.this.dispose();
                    mainWindow.setVisible(true);
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = inputArea.getText();
                if (content.equals("")) {
                    JOptionPane.showMessageDialog(RoomWindow.this, "消息内容不能为空", "提醒", JOptionPane.WARNING_MESSAGE);
                } else {
                    updateRecord(currentUser, content);
                    inputArea.setText("");
                }
            }
        });
    }


    private JPanel setRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("聊天室成员：");
        Font font = new Font(null, Font.ITALIC, 20);
        titleLabel.setFont(font);
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        GridBagLayout listLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 3, 2, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        JPanel container = new JPanel(new FlowLayout());
        JPanel listPanel = new JPanel(listLayout);
        listPanel.setSize(titleLabel.getWidth(), titleLabel.getHeight() * room.getUsers().size());
        for (User user: room.getUsers()) {
            System.out.println("a user");
            JLabel label = new JLabel(user.getId());
            label.setFont(font);
            listLayout.addLayoutComponent(label, c);
            listPanel.add(label);
        }
        container.add(listPanel);
        rightPanel.add(
                new JScrollPane(container, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER
        );

        return rightPanel;
    }


    private void updateRecord(User sender, String content) {
        recordArea.setText(
                recordArea.getText() +
                sender.getId() + " : \n   " +
                content + "\n");
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("window opened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("window closing");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        mainWindow.setVisible(true);
        System.out.println("window closed");
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
