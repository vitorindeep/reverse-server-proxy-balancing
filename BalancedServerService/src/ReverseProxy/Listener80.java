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
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Vitor Castro
 */
public class Listener80 extends Thread {

    ServerSocket server;
    Tabela tabela;

    public Listener80(Tabela tabela) throws IOException {
        server = new ServerSocket(80);
        this.tabela = tabela;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket client = server.accept();
                Socket servidor = null;
                InetAddress addressServidor = this.tabela.getBestServer(); // aumenta o nr conexoes na tabela para aquele servidor

                if (addressServidor == null) {
                    /* Se nao existir servidores é descartado a tentativa de conexao */
                    System.out.println("Nao temos servidores.");
                    client.close();
                    continue;
                } else {
                    try {
                        // Abrir socket 80 para receber pedidos externos
                        servidor = new Socket(addressServidor, 80);
                        System.out.println("Servidor escolhido: " + addressServidor.toString());
                        final InputStream streamFromServer = servidor.getInputStream();
                        final OutputStream streamToServer = servidor.getOutputStream();
                        final InputStream streamFromClient = client.getInputStream();
                        final OutputStream streamToClient = client.getOutputStream();

                        // Mecanismos de diálogo entre cliente e servidor transparentes
                        // Passar qual o servidor para podermos indicar o total de bytes usados relativamente ao mesmo
                        ClientListener listenToClient = new ClientListener(streamToServer, streamFromClient, tabela, addressServidor);
                        ClientSpeaker speakToClient = new ClientSpeaker(streamToClient, streamFromServer, tabela, addressServidor);
                        listenToClient.start();
                        speakToClient.start();

                    } catch (IOException e) {
                        System.out.println("Something wrong here! " + e.getMessage());
                        client.close();
                        if (servidor != null) {
                            servidor.close();
                        }
                        tabela.endedService(addressServidor.toString());
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro!" + e.getMessage());
        }
    }
}
