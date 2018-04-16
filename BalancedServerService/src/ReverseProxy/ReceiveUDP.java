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

                // Obter dados da sentence
                String[] result = sentence.split(","); // separar nas vírgulas
                for (String token : result) {
                    System.out.println(token); // Só para testar RAM e CPU
                }

                if (result[0].equals("DATA")) { // se a posição 0 diz DATA
                    double cpu = Double.parseDouble(result[1]);
                    double ram = Double.parseDouble(result[2]);
                    tabela.receivedData(receivePacket.getAddress(), cpu, ram); // result[1] = cpu; result[2] = ram;
                }
                tabela.printEstado();
            }
        } catch (IOException e) {
            System.out.println("Error ReceiveUDP.");
        }
    }
}
