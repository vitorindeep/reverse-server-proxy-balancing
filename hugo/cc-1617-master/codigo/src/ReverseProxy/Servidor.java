package ReverseProxy;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

class Servidor{
    InetAddress address;
    long rtt;
    int port, pacotesTotais, pacotesPerdidos, nrConexoesTCP, nrVezesRTT;
    long lastSended, lastReceived;

    public InetAddress getAddress() {
        return address;
    }

    public Servidor(InetAddress address){
        this.address = address;
        rtt = 0;
        pacotesTotais = pacotesPerdidos = nrConexoesTCP = nrVezesRTT = 0;
        lastReceived = lastSended = 0;
    }

    public float getRtt() {
        return rtt;
    }
    public void decrementaNrConexoes(){this.nrConexoesTCP--;}
    public void incrementaNrConexoes(){this.nrConexoesTCP++;}

    public synchronized void sendedProbingRequest(){
        /* Significa que mandamos um pedido de probing mas nao obtivemos ainda uma resposta, logo falhou*/
        if(lastSended != 0 && pacotesTotais != 0){
            pacotesPerdidos++; /* Ultimo pacote foi perdido */
        }
        lastSended = System.nanoTime();
        pacotesTotais ++ ;
    }

    public synchronized void receivedData(){
        if(lastSended == 0){
            return;
        }

        lastReceived = System.nanoTime();
        long tempo = lastReceived - lastSended;
        rtt = (rtt*nrVezesRTT + tempo)/(++nrVezesRTT);
        lastSended = 0;
    }
       
    /* Caso esteja mais que 30 segundos ausente devolve true */
    public boolean inactive(){
        long nanosAusente = (System.nanoTime() - lastReceived); /* segundos */
        long segundosAusente = TimeUnit.NANOSECONDS.toSeconds(nanosAusente);
        if(segundosAusente >= 30 && this.nrConexoesTCP ==0) return true; 
        return false;
    }
    
    @Override
    public String toString(){
        return address.toString() + " / RTT: " + rtt + " / Conexoes: " + nrConexoesTCP + " / Inativo à " + 
          TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - lastReceived)) + " / Pacotes perdidos: " + 
                pacotesPerdidos + "/" + pacotesTotais;
    }
}
