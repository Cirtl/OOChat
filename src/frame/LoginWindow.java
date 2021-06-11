package src.frame;

import src.dao.Repository;
import src.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoginWindow extends JFrame implements ActionListener, WindowListener {
    private final JTextField textName;
    private final JPasswordField textPwd;
    private final JButton jButtonLogin;
    private final JButton jButtonRegister;

    private User tempUser;

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

        this.addWindowListener(this);
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
                tempUser = new User(name);
                return true;
            }
        }catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    /**
     * 隐藏窗口
     */
    private void hideSelf() {
        textName.setText("");
        textPwd.setText("");
        this.setVisible(false);
    }


    /**
     * 登录按钮操作
     */
    private void loginCommands() {
        if (checkInput()) {
            SwingWorker<Boolean, Object> login = new SwingWorker<Boolean, Object>() {
                @Override
                protected Boolean doInBackground() {
                    if (Repository.getRepository() != null) {
                        Repository.getRepository().login(LoginWindow.this, textName.getText().trim(), textPwd.getText().trim());
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            login.execute();
        }
    }


    /**
     * 注册按钮操作
     */
    private void registerCommands() {
        hideSelf();
        SwingUtilities.invokeLater(() -> new RegisterWindow(LoginWindow.this));
    }


    /**
     * 登录成功时调用此方法，跳转界面
     */
    public void loginSuccess() {
        hideSelf();
        SwingUtilities.invokeLater(() -> new MainWindow(LoginWindow.this, tempUser));
    }


    /**
     * 登录失败时调用此方法
     */
    public void loginFail() {
        JOptionPane.showMessageDialog(this, "登录失败，请检查账号密码后重试");
    }


    /**
     * 实现对本界面按钮点击事件的监听
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonLogin) {
            loginCommands();
        } else if (e.getSource() == jButtonRegister) {
            registerCommands();
        }
    }


    /**
     * 窗口事件处理
     * @param e ...
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {
        if (Repository.getRepository() != null) {
            Repository.getRepository().close();
            System.out.println("连接断开");
        }
    }
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
