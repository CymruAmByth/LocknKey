/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locker;

/**
 *
 * @author joey
 */
public class Locker {

    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) {
        ServerConnection conn = new ServerConnection();
        Thread t = new Thread(conn);
        t.start();
    }            
}
