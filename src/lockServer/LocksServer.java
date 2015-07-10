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
public class LocksServer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocksManager manager = new LocksManager();
        Thread t = new Thread(manager);
        t.start();
    }    
}
