/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 *
 * @author Vitor Castro
 */
public class ClientSpeaker extends Thread {

    OutputStream streamToClient;
    InputStream streamFromServer;
    Tabela tabela;
    byte[] reply;
    InetAddress address;

    public ClientSpeaker(OutputStream streamToClient, InputStream streamFromServer, Tabela tabela, InetAddress address) throws IOException {
        this.streamFromServer = streamFromServer;
        this.streamToClient = streamToClient;
        this.tabela = tabela;
        reply = new byte[4096];
        this.address = address;
    }

    @Override
    public void run() {

        // Read the server's responses
        // and pass them back to the client.
        int bytesRead;

        try {
            while ((bytesRead = streamFromServer.read(reply)) != -1) {
                streamToClient.write(reply, 0, bytesRead);
                streamToClient.flush();
            }
        } catch (IOException e) {
        }

        try {
            System.out.println("Server closed connection to us.");
            streamToClient.close();
            tabela.endedService(address.toString());
        } catch (IOException e) {
        }
    }
}
