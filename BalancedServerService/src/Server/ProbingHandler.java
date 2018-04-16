/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Vitor Castro
 */
public class ProbingHandler extends Thread {

    final static String INET_ADDR = "239.8.8.8";
    final static int PORT = 8888;
    // multicast
    MulticastSocket multicastSocket;
    byte[] receiveData;
    DatagramPacket receivePacket;
    String receivedSentence;
    // unicast resposta
    DatagramSocket serverSocketEnvio;
    DatagramPacket sendPacket;
    byte[] sendData;
    String sendSentence;
    InetAddress iPAddress;
    OperatingSystemMXBean bean;

    public ProbingHandler(InetAddress inetAddress, int porta) throws Exception {

        // multicast
        multicastSocket = new MulticastSocket(PORT); // ficar na porta 8888
        multicastSocket.joinGroup(InetAddress.getByName(INET_ADDR)); // subnet 239.8.8.8
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // resposta unicast
        serverSocketEnvio = new DatagramSocket();
        iPAddress = inetAddress;

        bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // DATA do Server
        sendData = new byte[1024];
        sendSentence = "DATA";
    }

    @Override
    public void run() {
        while (true) {
            try {

                multicastSocket.receive(receivePacket);

                receivedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

                if (receivedSentence.equals("INFO")) {
                    System.out.println("Recebido pedido de info's.");
                    sendPacket = new DatagramPacket(sendSentence.getBytes(), sendSentence.getBytes().length, receivePacket.getAddress(), 8888);
                    // Obter os dados de CPU e RAM deste momento
                    System.out.println(bean.getProcessCpuLoad()); // teste
                    System.out.println(bean.getSystemCpuLoad()); // teste
                    // Se a mensagem for INFO envia dados
                    serverSocketEnvio.send(sendPacket);
                    System.out.println("Enviado data.");
                }

                /*
                // Fica a espera de pedido de informação
                System.out.println("Waiting for SIGNAL.");
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

                // Recebido pedido de identificação
                if (sentence.equals("ANYONE")) {
                    System.out.println("Recebido SINAL.");
                    sendPacketHello = new DatagramPacket(sendHello, sendHello.length, receivePacket.getAddress(), 8888);
                    // Se a mensagem for INFO envia dados
                    serverSocketEnvio.send(sendPacketHello);
                    System.out.println("Enviado HELLO.");
                } // Recebido pedido de informação
                else if (sentence.equals("INFO")) {
                    System.out.println("Recebido pedido de info's.");
                    sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), 8888);
                    // Obter os dados de CPU e RAM deste momento
                    System.out.println(bean.getProcessCpuLoad()); // teste
                    System.out.println(bean.getSystemCpuLoad()); // teste
                    // Se a mensagem for INFO envia dados
                    serverSocketEnvio.send(sendPacket);
                    System.out.println("Enviado data.");
                }
                 */
            } catch (IOException e) {
                System.out.println("Error ProbingHandler.");
                return;
            }
        }
    }
}
