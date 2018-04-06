package ReverseProxy;

import java.util.logging.Level;
import java.util.logging.Logger;

 class CleanInactives extends Thread{
    Tabela tabela;
    
    public CleanInactives(Tabela tabela) {
        this.tabela = tabela;
    }
    
    /* Acorda de 20 em 20 segundos */
    public void run(){
        while(true){
            try {
                Thread.sleep(20000);
                tabela.cleanInactive();
            } catch (InterruptedException ex) {
                Logger.getLogger(CleanInactives.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
}
