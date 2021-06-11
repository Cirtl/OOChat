package src.frame;

import src.dao.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RegisterWindow extends JFrame implements ActionListener, WindowListener {
    private final LoginWindow pre;

    private final JTextField textName;
    private final JPasswordField textPwd;
    private final JButton jButtonRegister;
    private final JButton jButtonBack;

    public RegisterWindow(LoginWindow pre) throws HeadlessException {
        super("Register Window");
        this.pre = pre;

        this.setBounds(500, 250, 650, 450);
        this.setLayout(new GridLayout(3, 1));
        this.setResizable(false);


        JPanel panelWelcome, panelInput, panelButton;
        panelWelcome = new JPanel(null);
        panelInput = new JPanel(null);
        panelButton = new JPanel(null);


        //welcome panel
        JLabel labelTitle = new JLabel("This is Register Window", SwingConstants.CENTER);
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
        jButtonRegister = new JButton("注册");
        jButtonRegister.setFont(new Font("宋体", Font.BOLD, 22));
        jButtonRegister.setBounds(100, 30, 145, 40);
        jButtonRegister.addActionListener(this);

        jButtonBack = new JButton("返回");
        jButtonBack.setFont(new Font("宋体", Font.BOLD, 22));
        jButtonBack.setBounds(300, 30, 145, 40);
        jButtonBack.addActionListener(this);


        panelWelcome.add(labelTitle);

        panelInput.add(labelName);
        panelInput.add(textName);
        panelInput.add(labelPwd);
        panelInput.add(textPwd);

        panelButton.add(jButtonRegister);
        panelButton.add(jButtonBack);

        this.add(panelWelcome);
        this.add(panelInput);
        this.add(panelButton);

        this.setVisible(true);
        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonRegister) {
            registerCommands();
        } else if (e.getSource() == jButtonBack) {
            backCommands();
        }
    }


    /**
     * 注册按钮操作
     */
    private void registerCommands() {
        String name = textName.getText().trim();
        String pwd = textPwd.getText().trim();
        if (name.equals("") || pwd.equals("")) {
            JOptionPane.showMessageDialog(null, "请正确输入用户名和密码", "【输入错误】", JOptionPane.PLAIN_MESSAGE);
        } else {
            SwingWorker<Boolean, Object> register = new SwingWorker<Boolean, Object>() {
                @Override
                protected Boolean doInBackground() {
                    if (Repository.getRepository() != null) {
                        Repository.getRepository().register(RegisterWindow.this, name, pwd);
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            register.execute();
        }
    }


    /**
     * 返回按钮操作
     */
    private void backCommands() {
        this.dispose();
    }


    /**
     * 回调函数中注册成功时调用该方法
     */
    public void registerSuccess() {
        JOptionPane.showMessageDialog(null, "注册成功");
        this.dispose();
    }


    /**
     * 回调函数中注册失败时调用该方法
     */
    public void registerFail() {
        JOptionPane.showMessageDialog(null, "注册失败，请在此尝试");
        textName.setText("");
        textPwd.setText("");
    }


    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {
        pre.setVisible(true);
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
