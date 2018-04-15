/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Vitor Castro
 */
public class ProbingHandler extends Thread {

    DatagramSocket serverSocket, serverSocketEnvio;
    DatagramPacket sendPacket, receivePacket;
    byte[] receiveData, sendData;
    String enviar;
    InetAddress iPAddress;

    public ProbingHandler(InetAddress inetAddress, int porta) throws Exception {
        serverSocket = new DatagramSocket(porta);
        serverSocketEnvio = new DatagramSocket();
        iPAddress = inetAddress;
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);

        sendData = new byte[1024];
        enviar = new String("DATA");
        sendData = enviar.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, iPAddress, porta);
    }

    @Override
    public void run() {
        while (true) {
            try {
                /* Fica a espera de pedido de informação */
                serverSocket.receive(receivePacket);
                System.out.println("Recebido pedido de info's.");
                String sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                /* Se a mensagem for INFO envia dados */

                if (sentence.equals("INFO")) {
                    serverSocketEnvio.send(sendPacket);
                    System.out.println("Enviado data.");
                }
            } catch (IOException e) {
                System.out.println("Error ProbingHandler.");
                return;
            }
        }
    }
}
