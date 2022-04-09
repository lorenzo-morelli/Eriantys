package it.polimi.ingsw.server.controller_server;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TestFrame extends javax.swing.JFrame {
    private JButton status=new JButton("Current State");
    /** Creates new form TestFrame */
    public TestFrame() {
        initComponents();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }


    private void initComponents() {
        JPanel jp=new JPanel();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jp.setLayout(new BorderLayout());
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jp.add(jTextArea1, "Center");
        setSize(200,200);
        getContentPane().add(jp);
        jp.add(status, "South");
        pack();
    }

    public void setStatus(String newStatus) {
        status.setText(newStatus);
    }
    public JTextArea getTextArea() {
        return jTextArea1;
    }

    private javax.swing.JTextArea jTextArea1;

}