/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReverseProxy;

import java.net.InetAddress;
import java.util.ArrayList;
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

    /* Melhor RTT */ // -> A MODIFICAR PARA CALCULAR COM CPU E RAM TMB
    public synchronized InetAddress getBestServer() {
        InetAddress melhorAddress = null;
        float rtt = -1;

        for (Servidor servidor : this.listaServidores.values()) {
            if (servidor.getRtt() < rtt || rtt < 0) {
                melhorAddress = servidor.getAddress();
                rtt = servidor.getRtt();
            }
        }

        if (melhorAddress != null) {
            /* Caso tenhamos um servidor então incrementamos o nr conexoes */
            Servidor s = listaServidores.get(melhorAddress.toString());
            s.incrementaNrConexoes();
        }

        return melhorAddress;
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
