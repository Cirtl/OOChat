package src.frame;

import src.Client.Client;
import src.entity.House;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class LoginWindow extends JFrame implements ActionListener {
    private final JTextField textName;
    private final JPasswordField textPwd;
    private final JButton jButtonLogin;
    private final JButton jButtonRegister;
    private Client logClient;

    public LoginWindow() throws HeadlessException {
        super("Login Window");
        this.setBounds(500, 250, 650, 450);
        this.setLayout(new GridLayout(3, 1));
        this.setResizable(false);


        JPanel panelWelcome, panelInput, panelButton;
        panelWelcome = new JPanel(null);
        panelInput = new JPanel(null);
        panelButton = new JPanel(null);


        //welcome panel
        JLabel labelTitle = new JLabel("Welcome to OOChat!", SwingConstants.CENTER);
        labelTitle.setFont(new Font("宋体", Font.ITALIC, 30));
        labelTitle.setBounds(130, 0, 350, 80);


        //input panel
        JLabel labelName = new JLabel("用户名:", SwingConstants.CENTER);
        labelName.setFont(new Font("宋体", Font.BOLD, 20));
        labelName.setBounds(90, 0, 130, 35);

        JLabel labelPwd = new JLabel("密码:", SwingConstants.CENTER);
        labelPwd.setFont(new Font("宋体", Font.BOLD, 20));
        labelPwd.setBounds(90, 80, 130, 35);


        textName = new JTextField(15);
        textName.setFont(new Font("宋体", Font.BOLD, 25));
        textName.setBounds(220, 0, 190, 30);

        textPwd = new JPasswordField(15);
        textPwd.setFont(new Font("宋体", Font.BOLD, 25));
        textPwd.setBounds(220, 80, 190, 30);


        //button panel
        jButtonLogin = new JButton("登录");
        jButtonLogin.setFont(new Font("宋体", Font.BOLD, 22));
        jButtonLogin.setBounds(100, 30, 145, 40);
        jButtonLogin.addActionListener(this);

        jButtonRegister = new JButton("注册");
        jButtonRegister.setFont(new Font("宋体", Font.BOLD, 22));
        jButtonRegister.setBounds(300, 30, 145, 40);
        jButtonRegister.addActionListener(this);


        panelWelcome.add(labelTitle);

        panelInput.add(labelName);
        panelInput.add(textName);
        panelInput.add(labelPwd);
        panelInput.add(textPwd);

        panelButton.add(jButtonLogin);
        panelButton.add(jButtonRegister);

        this.getContentPane().add(panelWelcome);
        this.getContentPane().add(panelInput);
        this.getContentPane().add(panelButton);

        this.setVisible(true);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    /**
     * 检查输入是否符合基本要求
     * @return 布尔值标志结果
     */
    public boolean checkInput() {
        try {
            String name = textName.getText().trim();
            String pwd = textPwd.getText().trim();
            if (name.equals("") || pwd.equals("")) {
                JOptionPane.showMessageDialog(this, "请输入用户名或密码", "【输入错误】", JOptionPane.PLAIN_MESSAGE);
                return false;
            } else {
                return true;
            }
        }catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * todo:实现数据获取
     * 检查用户名与密码的对应关系
     * @return 登录成功返回true, 否则返回false
     */
    private boolean checkLogin() {
        return true;
    }


    /**
     * todo:实现数据获取
     * @return 登录用户
     */
    private User getUser() {
        return new User("19373803", "123456");
    }


    /**
     * todo:实现数据获取
     * @return 当前所有房间列表
     */
    private Vector<House> getRooms() {
        Vector<House> rooms = new Vector<>();
        House room = new House(0, "0");
        Vector<User> users = new Vector<>();
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        users.add(new User("3", "3"));
        users.add(new User("1", "1"));
        users.add(new User("2", "2"));
        room.setUsers(users);
        rooms.add(room);
        rooms.add(new House(1, "1"));
        rooms.add(new House(2, "2"));
        rooms.add(new House(3, "3"));
        rooms.add(new House(4, "4"));
        rooms.add(new House(5, "5"));
        rooms.add(new House(6, "6"));
        rooms.add(new House(7, "7"));
        rooms.add(new House(8, "8"));
        rooms.add(new House(9, "9"));
        rooms.add(new House(10, "10"));
        rooms.add(new House(11, "11"));
        rooms.add(new House(12, "12"));
        rooms.add(new House(13, "13"));
        rooms.add(new House(14, "14"));
        rooms.add(new House(15, "15"));
        rooms.add(new House(16, "16"));
        return rooms;
    }


    /**
     * 实现对本界面按钮点击事件的监听
     */
    private boolean loginSuccess = false;
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonLogin) {
            loginCommands();
        } else if (e.getSource() == jButtonRegister) {
            registerCommands();
        }
    }


    /**
     * login commands
     */
    private void loginCommands() {
        System.out.println("click login");
        if (checkInput()) {
            System.out.println("thread begins");
            jButtonLogin.removeActionListener(this);
            jButtonRegister.removeActionListener(this);
            SwingWorker<Boolean, Object> login = new SwingWorker<Boolean, Object>() {
                @Override
                protected Boolean doInBackground() {
                    System.out.println("in thread");
                    loginSuccess = checkLogin();
                    return loginSuccess;
                }
            };
            login.execute();

            while (true) {
                if (login.isDone()) break;
            }

            if (loginSuccess) {
                this.dispose();
                new MainWindow(getUser(), getRooms());
            } else {
                jButtonLogin.addActionListener(this);
                jButtonRegister.addActionListener(this);
            }
            System.out.println("finished");
        }
    }


    /**
     * register commands
     */
    private void registerCommands() {
        this.dispose();
        new RegisterWindow();
    }

}
