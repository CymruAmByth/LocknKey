/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lockServer;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author joey
 */
public class UserConnection implements Runnable{
    
    private Socket mSocket;
    private LocksManager mLocksManager;
    private static final String SERVER_CLIENT_ID = "635248478115-khks0610shmbkpk8qh4btdgeos4c2n3e.apps.googleusercontent.com";

    public UserConnection(Socket mSocket, LocksManager mLocksManager) {
        this.mSocket = mSocket;
        this.mLocksManager = mLocksManager;
    }
    
    @Override
    public void run() {
        try(
                Scanner scanner = new Scanner(mSocket.getInputStream());
                PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true)
                ){
            if(scanner.hasNextLine()){
                String data = scanner.nextLine();
                System.out.println("Received from app: " + data);
                writer.println("Received");
                HttpTransport transport = new NetHttpTransport();
                JsonFactory json = new JacksonFactory();
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, json)
                        .setAudience(Arrays.asList(SERVER_CLIENT_ID))
                        .build();
                GoogleIdToken idToken = verifier.verify(data);
                if(idToken != null){
                    Payload payload = idToken.getPayload();
                    System.out.println("User ID: " + payload.getSubject());
                    System.out.println("User email: " + (String)payload.get("email"));                    
                }
            }
        } catch (IOException ex) {
            System.out.println("User Socket IO Error: " + ex.getMessage());
        } catch (GeneralSecurityException ex) {
            System.out.println("User Socket General Security Error: " + ex.getMessage());
        }
    }
    
}
