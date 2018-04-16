/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.*;

/**
 *
 * @author Vitor Castro
 */
public class ReverseProxy {

    public static void main(String args[]) throws Exception {
        /* Cria tabela de registo de servidores secundários */
        Tabela tabela = new Tabela();
        try {
            /* Parte 1 */
            // Thread para fazer pedidos de 5 em 5 segundos
            Signal threadSinal = new Signal(tabela);
            // Thread para receber as informações dos AgenteUDP
            ReceiveUDP receiveUDPDatagrams = new ReceiveUDP(tabela);
            // Thread para limpar de 20 em 20 seg a tabela
            Cleaner cleaner = new Cleaner(tabela);

            threadSinal.start();
            receiveUDPDatagrams.start();
            cleaner.start();

            /* Parte 2 */
            //Listener80 porta80 = new Listener80(tabela);
            //porta80.start();
        } catch (IOException e) {
            System.out.println("Erro UDPReverseProxy.");
        }
    }
}
