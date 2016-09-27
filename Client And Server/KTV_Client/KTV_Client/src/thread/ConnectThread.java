package thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JList;
import javax.swing.JProgressBar;

import entity.ClientInfo;

public class ConnectThread extends Thread{
	
	private ServerSocket serverSocket;
	private ServerSocket serverSocket_sound;
	
	private ClientInfo user;
	private JProgressBar progressbar;
	private JList list;
	
	public ConnectThread(ClientInfo user, JProgressBar progressbar, JList list) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.progressbar = progressbar;
		this.list = list;
	}


	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(9090);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread playThread = new PhoneThread(socket,user,progressbar,list);
				
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
