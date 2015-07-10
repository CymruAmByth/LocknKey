/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 *
 * @author joey
 */
public class LockDevice implements Runnable {

    private final SocketChannel socket;
    private final LocksManager manager;
    private String deviceSerialNo;
    private String command;
    private ByteBuffer buf;

    public LockDevice(SocketChannel socket, LocksManager manager) {
        this.socket = socket;
        this.manager = manager;
        buf = ByteBuffer.allocate(80);
        buf.clear();
    }

    @Override
    public void run() {
        try {
            while (true) {
                //sending data is a command is there
                if (command != null) {
                    buf.clear();
                    buf.put(command.getBytes());
                    buf.flip();
                    while (buf.hasRemaining()) {
                        socket.write(buf);
                    }
                    command = null;
                }
                //attempting to receive data
                buf.clear();
                if (socket.read(buf) > 0){
                    buf.flip();
                    String data = new String(buf.array(), buf.position(),buf.limit());
                    data = data.trim();
                    System.out.println("Received: " + data + " @ " + new Date().toString());
                    switch (data) {
                        case "Hello":
                            command = "Hello there";
                            break;
                        case "Ping":
                            command = "Pong";
                            break;
                        default:
                            String header = data.substring(0, 4);
                            String content = data.substring(4);
                            switch(header){
                                case "DEV:":
                                    deviceSerialNo = content;
                                    this.bindWithManager();
                                    break;
                                default:
                                    System.out.println(content);
                                    System.out.println(header);
                                    break;
                            }
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

private void bindWithManager() {
        manager.addDevice(deviceSerialNo, this);
    }

}
