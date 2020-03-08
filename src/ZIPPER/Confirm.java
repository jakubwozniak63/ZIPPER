package ZIPPER;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Color;

public class Confirm extends JFrame{

    public Confirm() {
        getContentPane().setBackground(Color.DARK_GRAY);
        setTitle("Usuwanie pliku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setSize(400, 300);
        getContentPane().setLayout(null);
    }

    public static void main(String[] args){
        ImageIcon icon = new ImageIcon("Deleting.png");
        int input = JOptionPane.showConfirmDialog(new Confirm(), 
                "Czy na pewno chcesz usunąć plik z archiwum?!", "Usuwanie pliku", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon);

        // 0=ok, 2=cancel
        System.out.println(input);
    }
}

