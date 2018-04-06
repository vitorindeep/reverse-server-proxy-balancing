package ReverseProxy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class SendUDP extends Thread{
    DatagramSocket serverSocketSend;
    byte[] receiveData, sendData;
    DatagramPacket receivePacket, sendPacket;
    String sentence;

    InetAddress address;
    int port;
    long start;
    Tabela tabela;

    public SendUDP(DatagramPacket receivePacket, Tabela tabela){
        try{
            this.tabela = tabela;
            receivePacket = receivePacket;
            address = receivePacket.getAddress();
            port = 5556;
            serverSocketSend = new DatagramSocket();
            sendData = new byte[1024];
            sentence = "INFO";
            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, address, 5555);
        }
        catch(Exception e){
            System.out.println("Error UDPHandler");
        }
    }

    public void run(){
        try{
            /* Atualizar tabela */
            tabela.sendedProbingRequest(address,System.nanoTime());
            /* Enviar pacote a pedir informações */
            serverSocketSend.send(sendPacket);
            

            
        }
        catch(Exception e){}
    }
}
