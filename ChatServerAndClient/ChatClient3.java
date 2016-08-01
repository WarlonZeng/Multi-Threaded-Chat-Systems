/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient3;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Warlon
 */
public class ChatClient3 {

    /**
     * @param args the command line arguments
     */
    
    static JFrame frame = new JFrame("Chat Client");
    static JTextArea textArea = new JTextArea();
    static JTextField textField = new JTextField(25);
    static JButton sendButton = new JButton("Send");
    static JPanel jp = new JPanel();
    static String username;
    static String serverName; // I USED localhost AND IT WORKS
    
    static public void makeInterface() { // make the interface first...
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sendButton.addActionListener(new ButtonPress());
        textArea.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(450, 300));    
        jp.add(areaScrollPane);
        jp.add(textField);
        jp.add(sendButton); 
        frame.add(jp);
        frame.setVisible(true);
    }
    
    static String message;
    
    static PrintStream ps;
    
    static public void main(String[] args) {
        serverName = JOptionPane.showInputDialog("server ip or name: ");
        username = JOptionPane.showInputDialog("new username: ");
        makeInterface();
        try{
            Socket s = new Socket(serverName, 5190);
            Scanner sin = new Scanner(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            ps.print(username + "\r\n");
            while(true) {
                if(sin.hasNext()) // server sent us anything
                    textArea.append(sin.nextLine() + "\n");
            }
        }
        catch(IOException e){
            System.out.println("Connection to google failed. :(");
        }
    }
    
    static class ButtonPress implements ActionListener { 
        public void actionPerformed(ActionEvent ae){ // give action event to listener
            message = textField.getText();
            ps.print(message + "\r\n");
            textField.setText("");
        }
    }
}
