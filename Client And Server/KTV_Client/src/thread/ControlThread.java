package thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.media.Player;
import javax.swing.JList;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.ClientInfo;
import entity.Song;

public class ControlThread{

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private JTextField textField;

	private ClientInfo user;
	private String songsCode;
	private InetAddress addr;
	private String ip;
	private JList list;

	public ControlThread(Socket socket,
			JTextField textField, JList list, ClientInfo user) {
		// TODO Auto-generated constructor stub

		this.socket = socket;
		this.textField = textField;
		this.list = list;
		this.user = user;
		
		try {
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			addr = InetAddress.getLocalHost();
			out.writeObject(addr.getHostAddress().toString());
			out.flush();
			this.ip = addr.getHostAddress().toString();

			String message = "";

			String str = "";
			str = (String) in.readObject();

			StringTokenizer st = new StringTokenizer(str, "@");
			message = st.nextToken();
			if (st.hasMoreTokens()) {
				songsCode = st.nextToken();
				user.setIp(this.ip);
				user.setSongsCode(songsCode);
				if(st.hasMoreTokens()){
					Gson gson = new Gson();
					String json = st.nextToken();
					Vector<Song> persons2 = gson.fromJson(json, new TypeToken<Vector<Song>>(){}.getType());
					Vector<String> songs = new Vector<String>(); 
			    	for(int i = 0; i < persons2.size(); i++){
			    		songs.add(persons2.elementAt(i).getName());
			    	}
			    	list.setListData(songs);
				}
			}
			System.out.println(user.getIp() + "\n" + user.getSongsCode());

			textField.setText(message);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ClientInfo currentClient() {
		return this.user;
	}
	
	public void changeState(Socket socket, String message){
		try {
			this.socket = socket;
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setIn(ObjectInputStream in){
		this.in = in;
	}

}
