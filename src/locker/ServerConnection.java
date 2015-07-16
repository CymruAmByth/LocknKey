/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author joey
 */
public class ServerConnection implements Runnable{

    private final String deviceSerialNo = "TestDevice";
    private final ByteBuffer buf;
    private String command;

    public ServerConnection() {
        
        buf = ByteBuffer.allocate(80);
        buf.clear();
        //Set up pingpong
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                command = "Ping";
            }
        }, 60 * 1000, 60 * 1000);
    }    

    @Override
    public void run(){
        try(SocketChannel s = SocketChannel.open();){
            s.configureBlocking(false);
            s.connect(new InetSocketAddress("soerendonk.iwa.nu", 8889));
            while(!s.finishConnect()){
                System.out.println("Connecting");
            }
            System.out.println("Connected");
            command = "Hello";
            while(true){
                //sending data is a command is there
                if(command != null){
                    buf.clear();
                    buf.put(command.getBytes());
                    buf.flip();
                    while(buf.hasRemaining()){
                        s.write(buf);
                    }
                    command = null;
                }
                //attempting to receive data
                buf.clear();
                if(s.read(buf) > 0){
                    buf.flip();
                    String data = new String(buf.array(), buf.position(),buf.limit());
                    data = data.trim();
                    System.out.println("Received: " + data + " @ " + new Date().toString());
                        switch (data) {
                            case "Hello there":
                                command = "DEV:" + deviceSerialNo;
                                break;
                            case "Pong":
                                break;
                            default:
                                System.out.println(data);
                                break;
                        }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
}
