/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class UsersManager implements Runnable{
    private LocksManager mLocksManager;

    public UsersManager(LocksManager mLocksManager) {
        this.mLocksManager = mLocksManager;
    }

    @Override
    public void run() {
        try(ServerSocket server = new ServerSocket(8887)){
            while(true){
                Socket s = server.accept();
                UserConnection uConnection = new UserConnection(s, mLocksManager);
                Thread t = new Thread(uConnection);
                t.start();
            }
        } catch (IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
        }
        
    }
    
}
