package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/* Esta thread envia de 5 em 5 segundos um sinal a avisar que está ativa. */ 
/* Vamos retirar daqui e colocar na parte do MonitorUDP do lado do proxy server,
uma vez que vai passar a ser este o responsável por fazer a procura por novos
servidores de X em X tempo. */
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
