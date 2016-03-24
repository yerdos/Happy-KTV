package thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import entity.ClientInfo;

public class ConnectThread extends Thread{
	
	private ServerSocket serverSocket;
	
	private ClientInfo user;
	
	public ConnectThread(ClientInfo user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}


	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(9090);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread playThread = new PhoneThread(socket,user);
				playThread.start();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
