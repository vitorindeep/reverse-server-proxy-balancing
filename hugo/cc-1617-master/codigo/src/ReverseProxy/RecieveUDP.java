package ReverseProxy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/* 
	Esta class lida com as sinalizações.
	Sempre que recebe uma sinalizacao cria uma Thread (UDPProbing) para realizar o probing.
*/
class RecieveUDP extends Thread{
    DatagramSocket serverSocket;
    byte[] receiveData;
    Tabela tabela;
    String sentence;
    DatagramPacket receivePacket;
    long time;

    public RecieveUDP(Tabela tabela) throws SocketException{
            serverSocket = new DatagramSocket(5555); /* Fica à escuta na porta 5555 */
            receiveData = new byte[1024];
            this.tabela = tabela;
            time = 0;
    }

    public void run(){
        try{
            while(true){
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                sentence = new String( receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                
                if(sentence.equals("AQUI")){
                    SendUDP sendProbingRequest = new SendUDP(receivePacket, tabela);
                    sendProbingRequest.start();
                }
                else if(sentence.equals("DATA")){
                    tabela.receivedData(receivePacket.getAddress(),System.nanoTime());
                }
                tabela.printEstado();
            }
        }
        catch(Exception e){ 
            System.out.println("Error UDPReverseProxy.");
        }
    }	
}
