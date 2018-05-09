/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vitor Castro
 */
class BandwidthCalculator extends Thread {

    Tabela tabela;

    public BandwidthCalculator(Tabela tabela) {
        // fornecer tabela de estado
        this.tabela = tabela;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // calcular bandwidth usada nos ultimos 5 segundos
                tabela.refreshBandwidth();

                System.out.println("5 seconds BANDWIDTH recalculado.");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BandwidthCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
