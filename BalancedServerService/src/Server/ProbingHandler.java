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

/**
 *
 * @author Vitor Castro
 */
public class ProbingHandler extends Thread {

    DatagramSocket serverSocket, serverSocketEnvio;
    DatagramPacket sendPacket, receivePacket, sendPacketHello;
    byte[] receiveData, sendData, sendHello;
    String enviar, enviarHello;
    InetAddress iPAddress;
    OperatingSystemMXBean bean;

    public ProbingHandler(InetAddress inetAddress, int porta) throws Exception {

        serverSocket = new DatagramSocket(8888); // nao funciona na mm maquina por causa das ports iguais
        serverSocketEnvio = new DatagramSocket();
        iPAddress = inetAddress;
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // HELLO do Server
        sendHello = new byte[1024];
        enviarHello = new String("HELLO");
        sendHello = enviarHello.getBytes();

        // DATA do Server
        sendData = new byte[1024];
        enviar = new String("DATA");
        sendData = enviar.getBytes();
    }

    @Override
    public void run() {
        while (true) {
            try {
                /* Fica a espera de pedido de informação */
                System.out.println("Waiting for SIGNAL.");
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

                // Recebido pedido de identificação
                if (sentence.equals("ANYONE")) {
                    System.out.println("Recebido SINAL.");
                    sendPacketHello = new DatagramPacket(sendHello, sendHello.length, receivePacket.getAddress(), 8888);
                    /* Se a mensagem for INFO envia dados */
                    serverSocketEnvio.send(sendPacketHello);
                    System.out.println("Enviado HELLO.");
                } // Recebido pedido de informação
                else if (sentence.equals("INFO")) {
                    System.out.println("Recebido pedido de info's.");
                    sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), 8888);
                    // Obter os dados de CPU e RAM deste momento
                    System.out.println(bean.getProcessCpuLoad()); // teste
                    System.out.println(bean.getSystemCpuLoad()); // teste
                    /* Se a mensagem for INFO envia dados */
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
