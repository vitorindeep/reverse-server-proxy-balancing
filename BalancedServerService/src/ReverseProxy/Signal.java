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

    String msg;
    DatagramPacket hi;
    InetAddress group;
    MulticastSocket multicastSocket;

    public Signal() throws SocketException, UnknownHostException, IOException {
        // join a Multicast group and send the group salutations
        msg = "ANYONE";
        group = InetAddress.getByName("239.8.8.8");
        multicastSocket = new MulticastSocket(8888);
        multicastSocket.joinGroup(group);
        hi = new DatagramPacket(msg.getBytes(), msg.length(), group, 8888);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Enviar a mensagem em multicast para o grupo
                multicastSocket.send(hi);
                System.out.println("ANYONE enviado aos AgenteUDP em Multicast.");
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error Signal");
                multicastSocket.close();
                return;
            }
        }
    }
}
