/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Vitor Castro
 */
public class Tabela {

    TreeMap<String, Servidor> listaServidores;

    public Tabela() {
        listaServidores = new TreeMap<String, Servidor>();
    }

    public synchronized void sendedProbingRequest() {
        Servidor server;

        // iterar pelos servidores e zerar para fazer conta ao rtt
        for (Map.Entry<String, Servidor> entry : listaServidores.entrySet()) {

            Servidor value = entry.getValue();

            value.sendedProbingRequest();
        }
    }

    public synchronized void receivedData(InetAddress address, double cpu, double ram) {
        Servidor server = listaServidores.get(address.toString());

        if (server == null) {
            server = new Servidor(address);
            listaServidores.put(address.toString(), server);
        }

        server.receivedData(cpu, ram);
    }

    public synchronized void cleanInactive() {
        ArrayList<String> elementosEliminar = new ArrayList<String>();

        for (Servidor s : listaServidores.values()) {
            if (s.inactive() == true) {
                elementosEliminar.add(s.getAddress().toString());
            }
        }
        for (String s : elementosEliminar) {
            listaServidores.remove(s);
        }
    }

    public synchronized void refreshBandwidth() {
        Servidor server;

        // iterar pelos servidores e zerar para fazer conta ao bandwidth
        for (Map.Entry<String, Servidor> entry : listaServidores.entrySet()) {

            Servidor value = entry.getValue();

            value.shortBandwidth();
        }
    }

    /* Melhor RTT */
    public synchronized InetAddress getBestServer() {
        InetAddress melhorAddress = null,
                melhorAddressRtt = null,
                melhorAddressCpu = null,
                melhorAddressRam = null,
                melhorAddressBand = null;
        float rtt = -1;
        double ram = -1, cpu = -1, bandwidth = -1;

        /*
         * Iniciar hashmap com par endereço,score
         * Endereço é o de cada servidor
         * Score começa a 0 e aumenta de acordo com performance encontrada
         */
        HashMap<InetAddress, Integer> scoreMap = new HashMap<InetAddress, Integer>(listaServidores.size());
        for (Servidor servidor : this.listaServidores.values()) {
            scoreMap.put(servidor.getAddress(), 0);
        }

        /*
         * Percorrer todos os servidores em TreeMap e verificar melhores valores
         * de rtt, CPU, RAM, Bandwidth.
         */
        for (Servidor servidor : this.listaServidores.values()) {
            // Verificar melhor rtt
            if (servidor.getRtt() < rtt || rtt < 0) {
                melhorAddressRtt = servidor.getAddress();
                rtt = servidor.getRtt();
            }
            // Verificar melhor cpu
            if (servidor.getAverageCpu() < cpu || cpu < 0) {
                melhorAddressCpu = servidor.getAddress();
                cpu = servidor.getAverageCpu();
            }
            // Verificar melhor ram
            if (servidor.getAverageRamLeft() < ram || ram < 0) {
                melhorAddressRam = servidor.getAddress();
                ram = servidor.getAverageRamLeft();
            }
            // Verificar melhor bandwidth
            if (servidor.getAverageBandwidth() < bandwidth || bandwidth < 0) {
                melhorAddressBand = servidor.getAddress();
                bandwidth = servidor.getAverageBandwidth();
            }
        }

        // Adicionar score ao HashMap scoreMap
        int currentValue;
        // rtt
        currentValue = scoreMap.get(melhorAddressRtt);
        scoreMap.remove(melhorAddressRtt);
        scoreMap.put(melhorAddressRtt, 3 + currentValue);
        // cpu
        currentValue = scoreMap.get(melhorAddressCpu);
        scoreMap.remove(melhorAddressCpu);
        scoreMap.put(melhorAddressCpu, 2 + currentValue);
        // ram
        currentValue = scoreMap.get(melhorAddressRam);
        scoreMap.remove(melhorAddressRam);
        scoreMap.put(melhorAddressRam, 2 + currentValue);
        // rtt
        currentValue = scoreMap.get(melhorAddressBand);
        scoreMap.remove(melhorAddressBand);
        scoreMap.put(melhorAddressBand, 2 + currentValue);

        // Encontrar melhorAddress
        for (Map.Entry<InetAddress, Integer> entry : scoreMap.entrySet()) {
            int melhor = 0;

            if (entry.getValue() > melhor) {
                melhor = entry.getValue();
                melhorAddress = entry.getKey();
            }
        }

        // Aumentar ligação do servidor escolhido
        if (melhorAddress != null) {
            /* Caso tenhamos um servidor então incrementamos o nr conexoes */
            Servidor s = listaServidores.get(melhorAddress.toString());
            s.incrementaNrConexoes();
        }

        return melhorAddress;
    }

    public void addBandBytes(String address, int bytesRead) {
        Servidor server = this.listaServidores.get(address);
        if (server == null) {
            return;
        }
        server.increaseBandBytes(bytesRead);
    }

    public synchronized void endedService(String address) {
        Servidor server = this.listaServidores.get(address);
        if (server == null) {
            return;
        }
        server.decrementaNrConexoes();
    }

    public void printEstado() {
        System.out.println();
        System.out.println("-----------------------------------");
        for (Servidor s : listaServidores.values()) {
            System.out.println(s.toString());
        }
        System.out.println("-----------------------------------");
        System.out.println();
    }

}
