package thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JProgressBar;

import entity.ClientInfo;

public class SoundConnectThread extends Thread {

	private ServerSocket serverSocket_sound;
	
	private ClientInfo user;
	
	public SoundConnectThread(ClientInfo user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}


	@Override
	public void run() {
		try {
			serverSocket_sound = new ServerSocket(9091);
			while (true) {
				Socket socket_sound = serverSocket_sound.accept();				
				Thread soundThread = new SoundThread(socket_sound, user);
				soundThread.start();

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (serverSocket_sound != null) {
				try {
					serverSocket_sound.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
