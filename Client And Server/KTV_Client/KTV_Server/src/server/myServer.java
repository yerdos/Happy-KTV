package server;  

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entity.MySQL;
import entity.Song;
import Thread.ClientThread;
import Thread.ServerThread;
  
public class myServer extends JFrame implements ActionListener{  
	
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;
	private JTextArea textField;
	private JButton btn_start;
	private JButton btn_stop;
	
	private Boolean isStarted;

	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private ArrayList<ClientThread> clients;

	public myServer(){
		this.setSize(300, 300);
		this.setVisible(true);
		
		isStarted = false;
		
		panel = new JPanel(null);
		
		textField = new JTextArea("ok!");
		textField.setBounds(30, 60, 200, 200);
		
		btn_start = new JButton("start");
		btn_start.setBounds(15, 15, 100, 30);
		btn_start.addActionListener(this);
		
		btn_stop = new JButton("stop");
		btn_stop.setBounds(120, 15, 100, 30);
		btn_stop.addActionListener(this);
		
		panel.add(btn_start);
		panel.add(btn_stop);
		panel.add(textField);
		this.add(panel);
		
		this.validate();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    public static void main(String[] args) throws ClassNotFoundException { 
    	
    	new myServer();
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btn_start){
			if(isStarted){
				textField.setText("Error!");
			} else {
				clients = new ArrayList<ClientThread>();
				try {
					serverSocket = new ServerSocket(8892);
					serverThread = new ServerThread(serverSocket, clients, textField);
					serverThread.start();  
		            isStarted = true; 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == btn_stop){
			if(serverThread != null){
				serverThread.interrupt();
				isStarted = false;
			}
		}
		
	}  
}