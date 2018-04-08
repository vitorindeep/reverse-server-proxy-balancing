package Server;

import java.io.*;
import java.net.*;
import java.lang.*;

/* Passará a chamar-se AgenteUDP e não enviará mais um Signal. */
class MonitorUDP{
    public static void main(String args[]){
        try{
            InetAddress inetAddress = InetAddress.getByName("10.1.1.1"); // mudar de acordo com o servidor
            int porta = 5555;

            Signal threadSinal = new Signal(inetAddress, porta);
            ProbingRequest threadProbing = new ProbingRequest(inetAddress, porta);
            threadProbing.start();
            threadSinal.start();
        }
        catch(Exception e){ System.out.println("Erro ao estabelecer conexão com o proxy reverso.");}
    }
}