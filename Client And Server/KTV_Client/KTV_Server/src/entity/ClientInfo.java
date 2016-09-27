package entity;

import java.util.Vector;

public class ClientInfo {
	
	private String ip;
	private String songsCode;
	private Vector<String> songs = new Vector<String>();
	
	public Vector<String> getSongs() {
		return songs;
	}

	public void setSongs(Vector<String> songs) {
		this.songs = songs;
	}

	public String getSongsCode() {
		return songsCode;
	}


	public void setSongsCode(String songsCode) {
		this.songsCode = songsCode;
	}

	public ClientInfo(String ip){
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

}
