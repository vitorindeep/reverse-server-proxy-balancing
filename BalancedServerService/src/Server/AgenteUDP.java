/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.*;

/**
 *
 * @author Vitor Castro
 */
public class AgenteUDP {

    public static void main(String args[]) {
        try {
            InetAddress inetAddress = InetAddress.getByName("10.1.1.1"); // mudar de acordo com o servidor
            int porta = 8888;

            ProbingHandler threadProbing = new ProbingHandler(inetAddress, porta);
            threadProbing.start();

            System.out.println("$ AgenteUDP: Waiting for reverseProxy calls.");

        } catch (Exception e) {
            System.out.println("Erro ao estabelecer conexão com o proxy reverso.");
        }
    }
}
