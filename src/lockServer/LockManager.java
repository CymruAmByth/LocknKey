/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class LockManager implements Runnable{
    
    private TreeMap<String, LockDevice> devices;

    @Override
    public void run() {
        try(ServerSocket server = new ServerSocket(8889)) {
            while(true){
                Socket s = server.accept();
                LockDevice device = new LockDevice(s, this);
                Thread t = new Thread(device);
                t.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }        
    }
    
    public void addDevice(String deviceSerialNo, LockDevice device){
        devices.put(deviceSerialNo, device);
    }
}
