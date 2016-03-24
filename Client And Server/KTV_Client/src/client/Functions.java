package client;

import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class Functions {
	
	private static Functions functions;
	
	private Functions() {
		File file = new File("music/a.wav");
		MediaLocator locator = new MediaLocator("file:"
				+ file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		try {
			player = Manager.createRealizedPlayer(locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static Functions getFunctions() {
		if (functions == null) {
			functions = new Functions();
		}
		return functions;
	}
	
	public Player player = null;
	
	public Player playMusic() {
		player.prefetch();
		System.out.println(player.getDuration().getSeconds());
		System.out.println(player.getMediaTime().getSeconds());
		return this.player;
	}

}
