/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.TreeMap;

/**
 *
 * @author joey
 */
public class LocksManager implements Runnable {

    private final TreeMap<String, LockDevice> devices = new TreeMap<>();

    @Override
    public void run() {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(8889));
            serverSocket.configureBlocking(false);
            while (true) {
                SocketChannel s = serverSocket.accept();
                if (s != null) {
                    System.out.println("Connecting device");
                    LockDevice device = new LockDevice(s, this);
                    Thread t = new Thread(device);
                    t.start();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addDevice(String deviceSerialNo, LockDevice device) {
        devices.put(deviceSerialNo, device);
    }
}
