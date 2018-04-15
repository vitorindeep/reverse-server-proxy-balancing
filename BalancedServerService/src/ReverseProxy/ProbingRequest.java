/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Vitor Castro
 */
/* É chamado após se saber que o servidor back-end existe.
   Envia um pacote a pedir "INFO" e atualiza a tabela
 */
public class ProbingRequest extends Thread {

    DatagramSocket serverSocketSend;
    byte[] receiveData, sendData;
    DatagramPacket receivePacket, sendPacket;
    String sentence;

    InetAddress address;
    int port;
    long start;
    Tabela tabela;

    public ProbingRequest(DatagramPacket receivePacket, Tabela tabela) {
        try {
            this.tabela = tabela;
            this.receivePacket = receivePacket; // pacote recebido pelo AgenteUDP
            address = receivePacket.getAddress(); // IP do AgenteUDP
            port = 8888; // porta do AgenteUDP
            serverSocketSend = new DatagramSocket(); // socket de envio do MonitorUDP
            sendData = new byte[1024];
            sentence = "INFO";
            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        } catch (SocketException e) {
            System.out.println("Error ProbingRequest by UDP.");
        }
    }

    @Override
    public void run() {
        try {
            /* Atualizar tabela */
            tabela.sendedProbingRequest(address, System.nanoTime());
            /* Enviar pacote a pedir informações */
            serverSocketSend.send(sendPacket);

        } catch (IOException e) {
        }
    }
}
