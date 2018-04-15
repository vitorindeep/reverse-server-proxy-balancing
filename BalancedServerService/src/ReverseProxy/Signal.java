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
/* Esta thread envia de 5 em 5 segundos um sinal a pedir dados aos Agentes UDP. */
public class Signal extends Thread {

    DatagramSocket monitorSocket;
    byte[] sendAreYouThere;
    String sentenceGiveMeData;
    DatagramPacket sendPacket;

    public Signal() throws SocketException {
        // abrir socket para o Monitor UDP fazer pedidos
        monitorSocket = new DatagramSocket();
        // espaço para mensagem de pedido
        sendAreYouThere = new byte[1024];
        // mensagem a enviar pedido
        sentenceGiveMeData = new String("INFO");
        // transformar mensagem em bytes
        sendAreYouThere = sentenceGiveMeData.getBytes();
        // pacote a enviar
        sendPacket = new DatagramPacket(sendAreYouThere, sendAreYouThere.length);
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitorSocket.send(sendPacket);
                System.out.println("Pedido de INFO enviado aos AgenteUDP.");
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error Signal");
                monitorSocket.close();
                return;
            }
        }
    }
}
