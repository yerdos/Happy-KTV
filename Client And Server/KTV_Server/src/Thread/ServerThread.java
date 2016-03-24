package Thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

import com.google.gson.Gson;

public class ServerThread extends Thread{
	
	private ServerSocket serverSocket;
	private ArrayList<ClientThread> clients;
	private JTextArea textField;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ServerThread(ServerSocket serverSocket, ArrayList<ClientThread> clients, JTextArea textField2){
		
		this.serverSocket = serverSocket;
		this.clients = clients;
		this.textField = textField2;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			try {
				Socket socket = serverSocket.accept();
				
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
				String message = (String)in.readObject();
				System.out.println(message);
				textField.setText(message);
				
				StringTokenizer st = new StringTokenizer(message, "@");
				String str_1 = st.nextToken();
				System.out.println(str_1);
				
				if(st.hasMoreTokens()){
					String str_2 = st.nextToken();
					System.out.println(str_2);
					if(str_1.equals("LIST")){
						for (int i = 0; i < clients.size(); i++){
							if(clients.get(i).getClient().getSongsCode().equals(str_2)){
								Gson gson = new Gson();
								String json = gson.toJson(clients.get(i).getClient().getSongs());
								out.writeObject(json);
								out.flush();
							}
						}

					} else {
						for (int i = 0; i < clients.size(); i++){
							if(clients.get(i).getClient().getSongsCode().equals(str_2)){
								if(!str_1.equals("PLAY") && !str_1.equals("PAUSE")){
									String str_3 = st.nextToken();
									if(str_3.equals("ADD")){
										clients.get(i).getClient().getSongs().add(str_1);
									}
									if(str_3.equals("SUB")){
										clients.get(i).getClient().getSongs().remove(str_1);
									}
								} else {
									out.writeObject(str_1);
									out.flush();
								}
							}
						}
					}
				} else {
					ClientThread client = new ClientThread(message, out, textField, socket);
					clients.add(client);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}	
	
}
