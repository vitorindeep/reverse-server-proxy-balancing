/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Vitor Castro
 */
// Recebe as informações dos AgenteUDP
public class ReceiveUDP extends Thread {

    DatagramSocket serverSocket;
    byte[] receiveData;
    Tabela tabela;
    String sentence;
    DatagramPacket receivePacket;
    long time;

    public ReceiveUDP(Tabela tabela) throws SocketException {
        /* Fica à escuta na porta 8888 */
        serverSocket = new DatagramSocket(8888);
        // espaço para dados recebidos
        receiveData = new byte[1024];
        this.tabela = tabela;
        time = 0;
    }

    @Override
    public void run() {
        try {
            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // Ficar à espera pelos dados dos AgenteUDP
                serverSocket.receive(receivePacket);
                System.out.println("% MonitorUDP: Pacote recebido em ReceiveUDP.");
                sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

                // Recebido um HELLO e pedido de informações diretamente
                if (sentence.equals("HELLO")) {
                    ProbingRequest sendProbingRequest = new ProbingRequest(receivePacket, tabela);
                    sendProbingRequest.start();
                } // Recebidos dados do Servidor
                else if (sentence.equals("DATA")) {
                    tabela.receivedData(receivePacket.getAddress(), System.nanoTime());
                }
                tabela.printEstado();
            }
        } catch (IOException e) {
            System.out.println("Error ReceiveUDP.");
        }
    }
}
