package client;

import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class Functions {
	
	private static Functions functions;
	private File file = new File("music/a.wav");
	
	private Functions() {
		MediaLocator locator = new MediaLocator("file:"
				+ this.file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		try {
			player = Manager.createRealizedPlayer(locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public synchronized static Functions getFunctions() {
		if (functions == null) {
			functions = new Functions();
		}
		return functions;
	}
	
	public Player player = null;
	
	public Player playMusic() {
		MediaLocator locator = new MediaLocator("file:"
				+ this.file.getAbsolutePath());
		try {
			player = Manager.createRealizedPlayer(locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.prefetch();
		System.out.println(player.getDuration().getSeconds());
		System.out.println(player.getMediaTime().getSeconds());
		return this.player;
	}

}
