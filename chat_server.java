/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package chat_server; // do not need for standalones

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author Warlon
 *
 */

class process_client extends Thread{
    Socket sock;
    static Vector<PrintStream> users = new Vector<PrintStream>();
    static Map<String, Integer> usernames_and_entry = new HashMap<String, Integer>();
    static Integer entry = 0;
    
    process_client(Socket newSock){sock = newSock;}
    public void run(){
        try{
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());
            
            entry++; // position entered.
            String temp = sin.nextLine(); // the username is the first thing i get.
            String username; // java effectively final avoid
            if (temp == null)
                username = "anon" + Integer.toString(entry);
            else
                username = temp;

            usernames_and_entry.put(username, entry); // username and entry added to map collection.
            users.add(sout);
            users.stream().forEach((client) -> {
                client.print(">> " + "' " + username + " '" + " connected" + "\r\n");
            });
            
            Iterator entries = usernames_and_entry.entrySet().iterator();
            sout.print("<----------- ACTIVE CLIENTS  ----------->\r\n");
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Integer value = (Integer) entry.getValue();
                sout.print("-- " + key + " entered at " + Integer.toString(value) + "\r\n"); // for string print methods " > ' cuz java.
            }
            sout.print("<----------- ACTIVE CLIENTS  ----------->\r\n");
            
            while (!sock.isClosed()){
                String line; // java effectively final avoid
                if (sin.hasNextLine())
                    line = sin.nextLine();
                else {
                    usernames_and_entry.remove(username);
                    users.stream().forEach((client) -> {
                        client.print("<< " + "' " + username + " '" + " disconnected" + "\r\n");
                    });
                    break;
                }
                //String line = sin.nextLine();
                users.stream().forEach((client) -> {
                    client.print(username + ": " + line + "\r\n");
                });
                if (line.equalsIgnoreCase("EXIT"))
                    sock.close();
            }
        }
        catch(IOException e){System.out.println(sock.getInetAddress().getHostAddress()+" disconnected");}
    }
}

public class chat_server {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(50000);
            System.out.println(InetAddress.getLocalHost());
            while(true){
                Socket sock = ss.accept();
                new process_client(sock).start();
            }
        }
        catch(IOException e){
            System.out.println("Instance of server already running!");
        }
    }
    
}
