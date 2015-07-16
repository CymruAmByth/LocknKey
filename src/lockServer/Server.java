/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

/**
 *
 * @author joey
 */
public class Server {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocksManager lManager = new LocksManager();
        Thread t1 = new Thread(lManager);
        t1.start();
        UsersManager uManager = new UsersManager(lManager);
        Thread t2 = new Thread(uManager);
        t2.start();
    }    
}
