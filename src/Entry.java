package src;

import src.frame.LoginWindow;

import javax.swing.*;
import java.awt.*;

public class Entry {

    public static void start() throws HeadlessException {
        new LoginWindow();
    }

    private static void exceptionCather(Exception e) {
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "未知错误发生", "程序出错", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 程序入口
     * @param args
     */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(
                new Runnable(){
                    @Override
                    public void run() {
                        start();
                    }
                });
        } catch (HeadlessException e) {
            exceptionCather(e);
        }
    }
}
