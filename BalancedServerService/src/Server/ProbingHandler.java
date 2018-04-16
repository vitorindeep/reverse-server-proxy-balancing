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
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Esperar por mensagens
                multicastSocket.receive(receivePacket);
                // Obter a mensagem
                receivedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

                // Se a mensagem for INFO envia dados
                if (receivedSentence.equals("INFO")) {
                    System.out.println("Recebido pedido de info's.");
                    // Obter os dados de CPU e RAM deste momento
                    double cpu = bean.getSystemCpuLoad();
                    double ram = bean.getFreePhysicalMemorySize();
                    //System.out.println("CPU: " + cpu); // teste
                    //System.out.println("RAM: " + ram); // teste
                    sendSentence = "DATA," + cpu + "," + ram;
                    // criar o PDU
                    sendPacket = new DatagramPacket(sendSentence.getBytes(), sendSentence.getBytes().length, receivePacket.getAddress(), 8888);
                    // enviar o PDU
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
