package Thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTextArea;

import com.google.gson.Gson;

import entity.ClientInfo;
import entity.MySQL;
import entity.Song;

public class ClientThread{

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ClientInfo user;
	private JTextArea textField;
	private Socket socket;

	public ClientThread(String message, ObjectOutputStream out,
			JTextArea textField2, Socket socket) {
		// TODO Auto-generated constructor stub

		this.textField = textField2;
		this.out = out;
		this.socket = socket;
		try {

			textField2.setText(textField2.getText() + "\n" + message);

			user = new ClientInfo(message);

			Date date = new Date();

			user.setSongsCode(date.toString());
			
			Vector<Song> songs = MySQL.getAllSongs();
	    	Gson gson = new Gson();
	    	String json = gson.toJson(songs);

			String str = "Welcome!@" + date + "@" + json;
			out.writeObject(str);
			out.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ClientInfo getClient() {
		return this.user;
	}
	
	public ObjectOutputStream getOut(){
		return this.out;
	}

	public void changeState(String str_1) {
		// TODO Auto-generated method stub
		try {
			System.out.println(str_1);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(str_1);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
