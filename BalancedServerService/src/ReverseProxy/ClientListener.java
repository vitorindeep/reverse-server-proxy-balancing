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
public class ClientListener extends Thread {

    OutputStream streamToServer;
    InputStream streamFromClient;
    Tabela tabela;
    byte[] request;
    InetAddress address;

    public ClientListener(OutputStream streamToServer, InputStream streamFromClient, Tabela tabela, InetAddress address) throws IOException {
        this.streamToServer = streamToServer;
        this.streamFromClient = streamFromClient;
        this.tabela = tabela;
        request = new byte[1024];
        this.address = address;
    }

    @Override
    public void run() {

        int bytesRead;

        try {
            while ((bytesRead = streamFromClient.read(request)) != -1) {
                // Passar a mensagem ao servidor
                streamToServer.write(request, 0, bytesRead);
                streamToServer.flush();
                // Atualizar o total de bytes que passará para o servidor (para calcular bandwidth)
                tabela.addBandBytes(address.toString(), bytesRead);
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
