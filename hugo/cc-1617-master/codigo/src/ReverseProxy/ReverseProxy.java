package ReverseProxy;

import java.io.*;
import java.net.*;
import java.util.TreeMap;

class ReverseProxy{

    public static void main(String args[]) throws Exception{
        Tabela tabela = new Tabela();
        try{
            /* Parte 1 */
            RecieveUDP recieveUDPDatagrams = new RecieveUDP(tabela);
            CleanInactives cleaner = new CleanInactives(tabela);

            recieveUDPDatagrams.start();
            cleaner.start();
            
            /* Parte 2 */
            Listener80 porta80 = new Listener80(tabela);
            porta80.start();

        }
        catch(Exception e){
                System.out.println("Erro UDPReverseProxy.");
        }
    }
}