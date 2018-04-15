/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vitor Castro
 */
public class Cleaner extends Thread {

    Tabela tabela;

    public Cleaner(Tabela tabela) {
        this.tabela = tabela;
    }

    /* Acorda de 20 em 20 segundos */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(20000);
                tabela.cleanInactive();
            } catch (InterruptedException ex) {
                Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
