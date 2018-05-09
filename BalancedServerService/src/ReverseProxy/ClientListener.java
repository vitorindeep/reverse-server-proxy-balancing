/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Vitor Castro
 */
public class ClientListener extends Thread {

    OutputStream streamToServer;
    InputStream streamFromClient;
    byte[] request;

    public ClientListener(OutputStream streamToServer, InputStream streamFromClient) throws IOException {
        this.streamToServer = streamToServer;
        this.streamFromClient = streamFromClient;
        request = new byte[1024];
    }

    @Override
    public void run() {

        int bytesRead;

        try {
            while ((bytesRead = streamFromClient.read(request)) != -1) {
                streamToServer.write(request, 0, bytesRead);
                streamToServer.flush();
            }
        } catch (IOException e) {
        }

        // the client closed the connection to us, so close our
        // connection to the server.
        try {
            System.out.println("Client closed connection to us.");
            streamToServer.close();
        } catch (IOException e) {
        }
    }
}
