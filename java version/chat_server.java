/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package chat_server;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author Warlon
 *
 */

class process_client extends Thread{
    // server-client initialization
    Socket sock;
    static Vector<PrintStream> users = new Vector<PrintStream>();
    static Vector<String> log = new Vector<String>();
    static Map<String, Integer> usernames_and_entry = new HashMap<String, Integer>();
    static Integer entry = 0;
    
    process_client(Socket newSock){sock = newSock;}
    // client initialization
    public void run(){
        try{
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());
            
            // this client only, invisible to client: initialize user information
            entry++; // position entered since server startup
            String temp = sin.nextLine(); // the username is the first thing i get.
            String username; // java effectively final avoid
            System.out.println(temp);
            if (temp.isEmpty())
                username = "anon" + Integer.toString(entry);
            else
                username = temp;
            
            // logging specifications: log CONNECTS, DISCONNECTS, MESSAGES. MESSAGES are LINES and they are dynamic, specified below.
            String connected_msg = ">> " + "' " + username + "' " + " connected" + "\r\n";
            String disconnected_msg = "<< " + "' " + username + "' " + " disconnected" + "\r\n";
            
            // all clients: tell all clients connected that a client connected including self
            usernames_and_entry.put(username, entry); // username and entry added to active clients map
            users.add(sout); // add user to active listening vector
            users.stream().forEach((client) -> {
                client.print(connected_msg);
            });
            
            // this client only: prints all active connections for user to see
            Iterator entries = usernames_and_entry.entrySet().iterator();
            sout.print("<----------- ACTIVE CLIENTS  ----------->\r\n");
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Integer value = (Integer) entry.getValue();
                sout.print(">> " + key + " entered at " + Integer.toString(value) + " since server started\r\n"); // for string print methods " > ' cuz java.
            }
            //sout.print("<----------- ACTIVE CLIENTS  ----------->\r\n");
            sout.print("<----------- LAST 50 MESSAGES ----------->\r\n");
            // this client only: prints to user the last 50 msgs
            log.forEach((msg) -> {
                sout.print(msg);
            });
            //sout.print("<----------- LAST 50 MESSAGES ----------->\r\n");
            sout.print(">> To disconnect from session type: EXIT \r\n");
            
            if (log.size() >= 100) {
                log.subList(0, 50).clear(); // remove the older 50 messages.
            }
            log.add(connected_msg);
            
            // main source of the chat system. logs client messages here.
            while (!sock.isClosed()){
                String line; // java effectively final avoid
                if (sin.hasNextLine())
                    line = sin.nextLine();
                else {
                    // client forcibly disconnected
                    usernames_and_entry.remove(username);
                    users.stream().forEach((client) -> {
                        client.print(disconnected_msg);
                    });
                    if (log.size() >= 100) {
                        log.subList(0, 50).clear(); // remove the older 50 messages.
                    }
                    log.add(disconnected_msg);
                    break;
                }
                
                // client sends msg to all clients
                users.stream().forEach((client) -> {
                    client.print(username + ": " + line + "\r\n");
                });
                if (log.size() >= 100) {
                    log.subList(0, 50).clear(); // remove the older 50 messages.
                }
                log.add(username + ": " + line + "\r\n"); // line obj (var) is cached when = 
                
                // client exited manually, prints to all clients connected including self right before closing.
                if (line.equalsIgnoreCase("EXIT")) {
                    usernames_and_entry.remove(username);
                    users.stream().forEach((client) -> {
                        client.print(disconnected_msg);
                    });
                    if (log.size() >= 100) {
                        log.subList(0, 50).clear(); // remove the older 50 messages.
                    }
                    log.add(disconnected_msg);
                    sock.close();
                }
            }
        }
        catch(IOException e){System.out.println(sock.getInetAddress().getHostAddress()+" disconnected\r\n");}
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
            System.out.println("Instance of server already running!\r\n");
        }
    }
    
}
