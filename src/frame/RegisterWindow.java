package src.frame;

import src.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RegisterWindow extends JFrame implements ActionListener {
    private final JTextField textName;
    private final JPasswordField textPwd;
    private final JButton jButtonRegister;
    private final JButton jButtonBack;

    public RegisterWindow() throws HeadlessException {
        super("Register Window");
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
        jButtonRegister.setFont(new Font("宋体", Font.CENTER_BASELINE, 22));
        jButtonRegister.setBounds(100, 30, 145, 40);
        jButtonRegister.addActionListener(this);

        jButtonBack = new JButton("返回");
        jButtonBack.setFont(new Font("宋体", Font.CENTER_BASELINE, 22));
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
     * register commands
     */
    private void registerCommands() {
        try {
            String name = textName.getText().trim();
            String pwd = textPwd.getText().trim();
            if (name.equals("") || pwd.equals("")) {
                JOptionPane.showMessageDialog(null, "请正确输入用户名和密码", "【输入错误】", JOptionPane.PLAIN_MESSAGE);
            } else {
                System.out.println(name);
                System.out.println(pwd);
            }
        }catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * back commands
     */
    private void backCommands() {
        this.dispose();
        Entry.start();
    }


    /**
     * get the ip address of local computer
     * @return if return "Unknown Error", there is something wrong
     */
    private String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException exception) {
            return "Unknown Error";
        }
    }
}
