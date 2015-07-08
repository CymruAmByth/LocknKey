/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class LockDevice implements Runnable {

    private final Socket socket;
    private final LockManager manager;
    private String deviceSerialNo;
    private final SocketReader reader;
    private String command;

    public LockDevice(Socket socket, LockManager manager) {
        this.socket = socket;
        this.manager = manager;
        this.reader = new SocketReader();
        Thread t = new Thread(reader);
        t.start();
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
            while (true) {
                if (command != null) {
                    out.print(command);
                    out.flush();
                    command = null;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private class SocketReader implements Runnable {

        @Override
        public void run() {

            String data;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (true) {
                    data = in.readLine();
                    System.out.println(data);
                    switch (data) {
                        case "Hello":
                            command = "Hello there";
                            deviceSerialNo = in.readLine();
                            command = "OK";
                            bindWithManager();
                            break;
                        case "Ping":
                            command = "Pong";
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

    private void bindWithManager() {
        manager.addDevice(deviceSerialNo, this);
    }

}
