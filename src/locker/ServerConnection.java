/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class ServerConnection implements Runnable {

    private String command;
    private final String deviceSerialNo = "TestDevice";

    public ServerConnection() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                command = "Ping";
            }
        }, 60*1000, 60*1000);
    }
    
    

    @Override
    public void run() {
        try(
                Socket socket = new Socket("localhost", 8889);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                ){
            connectionReader reader = new connectionReader(socket);
            Thread readThreat = new Thread(reader);
            readThreat.start();
            out.println("Hello");
            out.flush();
            while(true){
                if(command != null){
                    out.print(command);
                    out.flush();
                    command = null;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private class connectionReader implements Runnable {

        private final Socket s;

        public connectionReader(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            
            String data;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                while (true) {
                    data = reader.readLine();
                    System.out.println(data);
                    switch (data) {
                        case "Hello there":
                            command = deviceSerialNo;
                            data = reader.readLine();
                            if(!data.equals("OK"))
                                System.out.println("Error enlisting to server");
                            break;
                        case "Pong":
                            break;
                        default:
                            command = "error: invalid data";
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
