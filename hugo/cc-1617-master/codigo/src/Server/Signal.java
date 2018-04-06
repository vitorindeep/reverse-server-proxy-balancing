package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/* Esta thread envia de 10 em 10 segundos um sinal a avisar que está ativa. */ 
class Signal extends Thread{
	
	DatagramSocket clientSocket;
	InetAddress iPAddress;
	byte[] sendDataEstouAqui;
	String sentenceEstouAqui;
	DatagramPacket sendPacket;

	public Signal(InetAddress inetAddress, int porta) throws SocketException{
		clientSocket = new DatagramSocket();
		iPAddress = inetAddress;
		sendDataEstouAqui = new byte[1024];
		sentenceEstouAqui = new String("AQUI");
		sendDataEstouAqui = sentenceEstouAqui.getBytes();
		sendPacket = new DatagramPacket(sendDataEstouAqui, sendDataEstouAqui.length, iPAddress, porta);
	}

	public void run () {
		while(true){
			try{	
				clientSocket.send(sendPacket);
				System.out.println("Sinal enviado pela porta " + sendPacket.getPort());
				Thread.sleep(5000);
			}
			catch(Exception e){	
				System.out.println("Error Signal");
				clientSocket.close();
				return;
			}
		}
  	}
}
