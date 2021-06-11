package src;

import src.Client.Client;
import src.dao.Repository;
import src.frame.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Entry {

    public static void start() throws HeadlessException, IOException {
        Client client = new Client();
        Repository.init(client);
        new LoginWindow();
    }

    private static void exceptionCather(Exception e) {
        System.out.println(e.getMessage());
        if (e instanceof IOException)
            JOptionPane.showMessageDialog(null, "服务器尚未开启", "程序出错", JOptionPane.PLAIN_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "未知错误发生", "程序出错", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 程序入口
     * @param args 无
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable(){
                @Override
                public void run() {
                    try {
                        start();
                    } catch (Exception e) {
                        exceptionCather(e);
                    }
                }
            });
    }
}
