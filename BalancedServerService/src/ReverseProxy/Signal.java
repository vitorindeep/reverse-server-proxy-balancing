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

    final static String INET_ADDR = "239.8.8.8";
    final static int PORT = 8888;
    DatagramSocket monitorSocket;
    String sentence;
    DatagramPacket multicastPacket;
    Tabela tabela;

    /*
    String msg;
    DatagramPacket hi;
    InetAddress group;
    MulticastSocket multicastSocket;
     */
    public Signal(Tabela tabela) throws SocketException, UnknownHostException, IOException {

        // abrir socket para o Monitor UDP fazer pedidos
        monitorSocket = new DatagramSocket();
        // mensagem a enviar pedido
        sentence = "INFO";
        // pacote a enviar
        multicastPacket = new DatagramPacket(sentence.getBytes(), sentence.getBytes().length, InetAddress.getByName(INET_ADDR), PORT);
        // fornecer tabela de estado
        this.tabela = tabela;

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
                // enviar mensagem em multicast
                monitorSocket.send(multicastPacket);
                // informar envio de multicast para calcular rtt
                tabela.sendedProbingRequest();
                /*
                // Enviar a mensagem em multicast para o grupo
                multicastSocket.send(hi);
                 */
                System.out.println("INFO enviado aos AgenteUDP em Multicast.");
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error Signal");
                monitorSocket.close();
                return;
            }
        }
    }
}
