/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.net.InetAddress;
import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Vitor Castro
 */
public class Servidor {

    InetAddress address;
    long rtt;
    double cpu;
    Queue<Double> ramFifo; // fifo to store the last 12 ram values (1 minute)
    Queue<Double> bandwidthFifo; // fifo to store the last 12 bandwidth values (1 minute)
    int totalBandBytes;
    int port, pacotesTotais, pacotesPerdidos, nrConexoesTCP, nrVezesRTT;
    long lastSended, lastReceived;

    public InetAddress getAddress() {
        return address;
    }

    public Servidor(InetAddress address) {
        this.address = address;
        rtt = 0;
        cpu = 0;
        ramFifo = new CircularFifoQueue<Double>(12);
        bandwidthFifo = new CircularFifoQueue<Double>(12);
        totalBandBytes = 0;
        pacotesTotais = pacotesPerdidos = nrConexoesTCP = nrVezesRTT = 0;
        lastReceived = lastSended = 0;
    }

    public float getRtt() {
        return rtt;
    }

    // This is called by a thread that runs every 5 seconds
    // to calculate the bandwidth used in that period of time
    public void shortBandwidth() {
        // calculate bandwidth over the last 5 seconds
        double bandwidth = totalBandBytes / 5;
        // clean amount of bytes until now
        totalBandBytes = 0;

        bandwidthFifo.add(bandwidth);
    }

    public double getAverageCpu() {
        return cpu;
    }

    public double getAverageRamLeft() {
        double total = 0;

        for (Double ram : ramFifo) {
            total += ram;
        }

        return (total / 12);
    }

    public double getAverageBandwidth() {
        double total = 0;

        for (Double band : bandwidthFifo) {
            total += band;
        }

        return (total / 12);
    }

    public void increaseBandBytes(int bytesRead) {
        this.totalBandBytes += bytesRead;
    }

    public void decrementaNrConexoes() {
        this.nrConexoesTCP--;
    }

    public void incrementaNrConexoes() {
        this.nrConexoesTCP++;
    }

    public synchronized void sendedProbingRequest() {
        /* Significa que mandamos um pedido de probing mas nao obtivemos ainda uma resposta, logo falhou*/
        if (lastSended != 0 && pacotesTotais != 0) {
            pacotesPerdidos++;
            /* Ultimo pacote foi perdido */
        }
        lastSended = System.nanoTime();
        pacotesTotais++;
    }

    public synchronized void receivedData(double cpu, double ram) { // add bandwidth
        if (lastSended == 0) {
            return;
        }

        lastReceived = System.nanoTime();
        long tempo = lastReceived - lastSended;
        rtt = (rtt * nrVezesRTT + tempo) / (++nrVezesRTT);
        this.cpu = cpu;
        ramFifo.add(ram);
        lastSended = 0;
    }

    /* Caso esteja mais que 30 segundos ausente devolve true */
    // FAZER BLACKLIST
    public boolean inactive() {
        long nanosAusente = (System.nanoTime() - lastReceived);
        /* segundos */
        long segundosAusente = TimeUnit.NANOSECONDS.toSeconds(nanosAusente);
        if (segundosAusente >= 30 && this.nrConexoesTCP == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return address.toString() + " / RTT: " + rtt
                + " / CPUUsage: " + cpu
                + " / RAMLeft: " + getAverageRamLeft()
                + " / Conexoes: " + nrConexoesTCP
                + " / Inativo há " + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - lastReceived))
                + " / Pacotes perdidos: " + pacotesPerdidos + "/" + pacotesTotais;
    }

}
