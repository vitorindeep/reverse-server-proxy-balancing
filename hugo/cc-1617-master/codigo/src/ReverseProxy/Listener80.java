package ReverseProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Listener80 extends Thread{
    ServerSocket server;
    Tabela tabela;
    
    public Listener80(Tabela tabela) throws IOException{
        server = new ServerSocket(80);
        this.tabela = tabela;
    }
    
    public void run(){
        try{
            while(true){
                Socket client = server.accept();
                Socket servidor = null;
                InetAddress addressServidor = this.tabela.getBestServer();
                
                if(addressServidor == null){ /* Se nao existir servidores é descartado a tentativa de conexao */
                    System.out.println("Nao temos servidores.");
                    client.close();
                    continue;
                }
                else{
                    try {
                        servidor = new Socket(addressServidor,80);
                        System.out.println("Servidor escolhido: " + addressServidor.toString());
                        final InputStream streamFromServer = servidor.getInputStream();
                        final OutputStream streamToServer = servidor.getOutputStream();
                        final InputStream streamFromClient = client.getInputStream();
                        final OutputStream streamToClient = client.getOutputStream();

                        ClientListener listener = new ClientListener(streamToServer,streamFromClient);
                        ClientSpeaker speaker = new ClientSpeaker(streamToClient,streamFromServer,tabela, addressServidor);

                        listener.start();
                        speaker.start();
                        } catch (IOException e) {
                            System.out.println("Something wrong here! " + e.getMessage());
                            client.close();
                            if(servidor != null){
                                servidor.close();
                            }
                            tabela.endedService(addressServidor.toString());
                            continue;
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Erro!" + e.getMessage());
        }
    }
}
