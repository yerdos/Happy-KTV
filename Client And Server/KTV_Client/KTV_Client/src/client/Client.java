package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thread.ConnectThread;
import thread.PhoneThread;
import thread.SoundConnectThread;
import entity.ClientInfo;
import thread.ControlThread;

public class Client extends JFrame implements ActionListener, ChangeListener,
		MouseListener {

	private static final long serialVersionUID = 1L;

	private JButton btn_login;
	private JButton btn_logoff;
	private JButton btn_play;
	private JButton btn_pause;
	private JTextField textField;
	private Player player = null;
	private JProgressBar progressbar = null;
	private Timer timer;
	private JSlider slider;
	private JScrollPane scrollPanel;
	private JList list;
	private JScrollPane scrollPanel_2;
	private JList list_2;

	private boolean isConnected;
	private ClientInfo user;

	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	private ControlThread client;

	private InetAddress addr;
	
	private ServerSocket serverSocket = null;
	
	private PhoneThread phone;
	
	private Functions functions;

	public Client() {

		try {
			
			functions = Functions.getFunctions();
			
			user =  new ClientInfo();

			socket = new Socket("localhost", 8892);

			addr = InetAddress.getLocalHost();

			this.setSize(270, 400);
			this.setVisible(true);

			isConnected = false;

			JPanel panel = new JPanel(null);

			btn_login = new JButton("login");
			btn_login.setBounds(15, 15, 100, 30);
			btn_login.addActionListener(this);

			btn_logoff = new JButton("logoff");
			btn_logoff.setBounds(120, 15, 100, 30);
			btn_logoff.addActionListener(this);

			btn_play = new JButton("play");
			btn_play.setBounds(15, 60, 100, 30);
			btn_play.addActionListener(this);

			btn_pause = new JButton("pause");
			btn_pause.setBounds(120, 60, 100, 30);
			btn_pause.addActionListener(this);

			textField = new JTextField("ok");
			textField.setBounds(30, 90, 200, 30);

			progressbar = new JProgressBar();
			progressbar.setBounds(15, 130, 250, 20);
			progressbar.setOrientation(JProgressBar.HORIZONTAL);
			progressbar.setMinimum(0);
			progressbar.setMaximum(100);
			progressbar.setValue(0);
			progressbar.addChangeListener(this);
			progressbar.setBorderPainted(true);

			slider = new JSlider(0, 100);
			slider.setValue(progressbar.getValue());
			slider.setBounds(5, 115, 270, 50);
			slider.addMouseListener(this);

			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					int value = 0;
					value = (int) (functions.player.getMediaTime().getSeconds()
							/ functions.player.getDuration().getSeconds() * 100);
					progressbar.setValue(value);
					slider.setValue(value);

				}
			}, 1000, 1000);
			
			list = new JList();
			list_2 = new JList(); 
			
			list.addMouseListener(new MouseAdapter(){  
			    public void mouseClicked(MouseEvent e){  
			        if(e.getClickCount()==2){   //When double click JList  
			        	System.out.println(list.getSelectedValue().toString());
			        	try {
			        		socket = new Socket("localhost", 8892);
			        		out = new ObjectOutputStream(socket.getOutputStream());
							out.writeObject(list.getSelectedValue().toString() + "@" + user.getSongsCode() + "@ADD");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	user.getSongs().add(list.getSelectedValue().toString());
			        	list_2.setListData(user.getSongs());
			        }  
			    }  
			}); 
			
			scrollPanel = new JScrollPane(list);
			scrollPanel.setBounds(15, 160, 110, 210);
			
			list_2.addMouseListener(new MouseAdapter(){  
			    public void mouseClicked(MouseEvent e){  
			        if(e.getClickCount()==2){   //When double click JList  
			        	System.out.println(list_2.getSelectedIndex());
			        	try {
			        		socket = new Socket("localhost", 8892);
			        		out = new ObjectOutputStream(socket.getOutputStream());
							out.writeObject(list_2.getSelectedIndex() + "@" + user.getSongsCode() + "@SUB");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	File file = new File("music/" + list_2.getSelectedValue().toString() + ".wav");
			        	System.out.println(list_2.getSelectedValue().toString());
			        	functions.setFile(file);
			        	progressbar.setValue(0);
			        	functions.player.stop();
			        	functions.playMusic().start();
			        	user.getSongs().remove(list_2.getSelectedIndex());
			        	list_2.setListData(user.getSongs());
			        }  
			    }  
			});
			
			scrollPanel_2 = new JScrollPane(list_2);
			scrollPanel_2.setBounds(135, 160, 110, 210);
			
			panel.add(btn_login);
			panel.add(btn_logoff);
			panel.add(btn_play);
			panel.add(btn_pause);
			panel.add(progressbar);
			panel.add(slider);
			panel.add(textField);
			panel.add(scrollPanel);
			panel.add(scrollPanel_2);
			
			this.add(panel);
			

			this.validate();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		new Client().playMusic();

	}

	public void playMusic() {
		functions.setFile(new File("music/a.wav"));
		functions.playMusic();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == btn_login) {
			if (isConnected) {
				textField.setText("has already online!");
			} else {

				client = new ControlThread(socket, textField, list, user);
				
				new ConnectThread(user, progressbar, list_2).start();
				new SoundConnectThread(user).start();
				isConnected = true;
			}
		} else if (e.getSource() == btn_logoff) {
			if (client != null) {
//				client.interrupt();
				isConnected = false;
			}
		} else if (e.getSource() == btn_play) {
			try {

				socket = new Socket("localhost", 8892);

				client.changeState(socket, "PLAY@"
						+ client.currentClient().getSongsCode());

				in = new ObjectInputStream(socket.getInputStream());
				
				if (((String) in.readObject()).equals("PLAY")) {
					System.out.println("correct@@@@@@");
					functions.player.start();
				} else {
					System.out.println("ERROR@@@@@@");
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (e.getSource() == btn_pause) {
			try {

				socket = new Socket("localhost", 8892);

				client.changeState(socket, "PAUSE@"
						+ client.currentClient().getSongsCode());

				in = new ObjectInputStream(socket.getInputStream());

				if (((String) in.readObject()).equals("PAUSE")) {
					System.out.println("correct@@@@@@");
					functions.player.stop();
				} else {
					System.out.println("ERROR@@@@@@");
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

		progressbar.setValue(slider.getValue());

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		double value = slider.getValue();
		functions.player.stop();
		functions.player.setMediaTime(new Time(value / 100
				* functions.player.getDuration().getSeconds()));
		functions.player.start();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
