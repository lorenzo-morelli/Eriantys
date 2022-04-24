package it.polimi.ingsw.utils.gui;

import javax.swing.*;
import java.awt.*;

public class Window {
    JFrame window;  // la finestra

    JPanel titleBar;  // barra del titolo del gioco
    JLabel gameTitle; // nome del gioco
    Container con;

    Font titleFont = new Font("Times New Roman",Font.PLAIN,90);

    public Window(){
        window = new JFrame();
        window.setSize(800,600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setLayout(null);
        con = window.getContentPane();
        titleBar = new JPanel();
        titleBar.setBounds(100,100,600,150);
        titleBar.setBackground(Color.BLACK);
        gameTitle = new JLabel("Eriantys");
        gameTitle.setForeground(Color.WHITE );
        gameTitle.setFont(titleFont);
        titleBar.add(gameTitle);
        con.add(titleBar);
        window.setVisible(true);
    }

    public static void main(String args[]){
        new Window();
    }

}
