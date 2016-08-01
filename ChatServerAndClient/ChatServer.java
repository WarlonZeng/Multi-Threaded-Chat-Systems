/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author Warlon
 * using pretty much the socket server from class
 */

class ProcessClient extends Thread{
    Socket sock;
    static Vector<PrintStream> users = new Vector<PrintStream>();
    ProcessClient(Socket newSock){sock = newSock;}
    public void run(){
        try{
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());
            String username = sin.nextLine();
            users.add(sout);
            while (!sock.isClosed()){
                String line = sin.nextLine();
                //sout.print(username + ": " + line+"\r\n");
                for (PrintStream first: users) {
                    first.print(username + ": " + line+"\r\n");
                }
                if (line.equalsIgnoreCase("EXIT"))
                    sock.close();
            }
        }
        catch(IOException e){}
        System.out.println(sock.getInetAddress().getHostAddress()+" disconnected");
        
    }
}

public class ChatServer {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(5190);
            while(true){
                Socket sock = ss.accept();
                new ProcessClient(sock).start();
            }
        }
        catch(IOException e){
            System.out.println("Caught IOException!");
        }
    }
    
}
