package thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.media.rtp.event.NewParticipantEvent;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JList;
import javax.swing.JProgressBar;

import com.google.gson.Gson;

import client.Functions;

import entity.ClientInfo;

public class PhoneThread extends Thread {

	private InputStream input;
	private AudioFormat format = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
	private SourceDataLine line;
	private byte[] data;
	private ClientInfo user;
	
	private JProgressBar progressbar;

	private Functions functions;
	
	private JList list;

	private DataInputStream in;
	private DataOutputStream out;

	private Socket socket;

	public PhoneThread(Socket socket, ClientInfo user, JProgressBar progressbar, JList list) {
		try {
			this.socket = socket;
			this.user = user;
			this.progressbar = progressbar;
			this.list = list;
			input = socket.getInputStream();
			in = new DataInputStream(input);
			String get;
			while (true) {
				if (!((get = in.readUTF()) == null)) {
					System.out.println(get);
					out = new DataOutputStream(
							socket.getOutputStream());
					Gson gson = new Gson();
					String json = gson.toJson(user);
					System.out.println(json);
					String str = "[";
					for (int i = 0; i < user.getSongs().size(); i++) {
						str = str + "{\"name\":" + "\""
								+ user.getSongs().elementAt(i) + "\"" + "}";
						if (i != user.getSongs().size() - 1)
							str = str + ",";
					}
					str = str + "]";
					System.out.println(str);
					out.writeUTF(str);
					break;
				}
			}
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			line.open(format);
			line.start();
			data = new byte[2];

//			boolean first = true;
			while (true) {
				input = socket.getInputStream();
				in = new DataInputStream(input);
				String play;
				if (!((play = in.readUTF()) == null)) {
					System.out.println(play);
					if (play.contains("PLAY")) {
						System.out.println("ok");
						StringTokenizer st = new StringTokenizer(play, "@");
						String str_1 = st.nextToken();
						str_1 = st.nextToken();
						System.out.println(str_1);
						
						Functions functions = Functions.getFunctions();
						functions.setFile(new File("music/" + str_1 + ".wav"));
			        	progressbar.setValue(0);
			        	functions.player.stop();
			        	functions.playMusic().start();
					} else if (play.contains("STOP")) {
						System.out.println("ok");
						StringTokenizer st = new StringTokenizer(play, "@");
						String str_1 = st.nextToken();
						System.out.println(str_1);
						
						Functions functions = Functions.getFunctions();
//						functions.setFile(new File("music/" + str_1 + ".wav"));
//						progressbar.setValue(0);
						functions.player.stop();
					} else if (play.contains("ADD")) {
						System.out.println("ok");
						StringTokenizer st = new StringTokenizer(play, "@");
						String str_1 = st.nextToken();
						str_1 = st.nextToken();
						System.out.println(str_1);
						
						user.getSongs().add(str_1);
			        	list.setListData(user.getSongs());
						
					} else if (play.contains("REFRESH")) {
						System.out.println("ok");
						StringTokenizer st = new StringTokenizer(play, "@");
						String str_1 = st.nextToken();
						System.out.println(str_1);
						
						String str = "[";
						for (int i = 0; i < user.getSongs().size(); i++) {
							str = str + "{\"name\":" + "\""
									+ user.getSongs().elementAt(i) + "\"" + "}";
							if (i != user.getSongs().size() - 1)
								str = str + ",";
						}
						str = str + "]";
						System.out.println(str);
						out.writeUTF(str);
					} 
				} else {
					System.out.println("error");
//					input.read(data);
//					line.write(data, 0, data.length);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
