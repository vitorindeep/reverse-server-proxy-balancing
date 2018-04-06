package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* Fica a espera de um pedido de probing e lança uma resposta */
class ProbingRequest extends Thread{

    DatagramSocket serverSocket, serverSocketEnvio;
    DatagramPacket sendPacket, receivePacket;
    byte[] receiveData, sendData;
    String enviar;
    InetAddress iPAddress;

    public ProbingRequest(InetAddress inetAddress, int porta) throws Exception{
        serverSocket = new DatagramSocket(porta);
        serverSocketEnvio = new DatagramSocket();
        iPAddress = inetAddress;
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        
        sendData = new byte[1024];
        enviar = new String("DATA");
        sendData = enviar.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, iPAddress, 5555);
    }

    public void run (){
        while(true){
            try{
                /* Fica a espera de pedido de informação */
                serverSocket.receive(receivePacket);
                System.out.println("Recebido pedido de info's.");
                String sentence = new String( receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                /* Se a mensagem for INFO envia dados */
                
                if(sentence.equals("INFO")){
                    serverSocketEnvio.send(sendPacket);
                    System.out.println("Enviado data.");
                }
            }
            catch(Exception e){System.out.println("Error ProbingRequest.");return;}
        }
    }
}
