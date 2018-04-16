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
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Vitor Castro
 */
/* Esta thread envia de 5 em 5 segundos um sinal a pedir dados aos AgenteUDP. */
public class Signal extends Thread {

    DatagramSocket monitorSocket;
    byte[] sendAreYouThere;
    String sentenceGiveMeData;
    DatagramPacket sendPacket;

    /*
    String msg;
    DatagramPacket hi;
    InetAddress group;
    MulticastSocket multicastSocket;
     */
    public Signal() throws SocketException, UnknownHostException, IOException {

        // abrir socket para o Monitor UDP fazer pedidos
        monitorSocket = new DatagramSocket();
        // espaço para mensagem de pedido
        sendAreYouThere = new byte[1024];
        // mensagem a enviar pedido
        sentenceGiveMeData = new String("ANYONE");
        // transformar mensagem em bytes
        sendAreYouThere = sentenceGiveMeData.getBytes();
        // pacote a enviar
        sendPacket = new DatagramPacket(sendAreYouThere, sendAreYouThere.length, InetAddress.getByName("10.1.1.1"), 8888);

        /*
        // join a Multicast group and send the group salutations
        msg = "ANYONE";
        group = InetAddress.getByName("255.255.255.0");
        multicastSocket = new MulticastSocket(); // multicast feito pela porta
        multicastSocket.joinGroup(group);
        hi = new DatagramPacket(msg.getBytes(), msg.length(), group, 8888); // para a porta 8888
         */
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitorSocket.send(sendPacket);
                /*
                // Enviar a mensagem em multicast para o grupo
                multicastSocket.send(hi);
                 */
                System.out.println("ANYONE enviado aos AgenteUDP em Multicast.");
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error Signal");
                monitorSocket.close();
                return;
            }
        }
    }
}
