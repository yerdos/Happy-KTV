package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MySQL {
	
	static String url = "jdbc:mysql://55fea50417850.sh.cdb.myqcloud.com:8354/happyKTV";
	static String username = "xiaoyu";
	static String password = "wangzeyuwoaini";
	static Connection con = null;
	static PreparedStatement ps = null;
	static ResultSet resultSet = null;
	
	@SuppressWarnings("null")
	public static Vector<Song> getAllSongs(){
		
		Vector<Song> songs = new Vector<Song>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("加载驱动成功!");
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动失败!");
			e.printStackTrace();
		}
		// 建立连接
		try {
			con = DriverManager.getConnection(url, username, password);
			System.out.println("数据库连接成功!");
		} catch (SQLException e) {
			System.out.println("数据库连接失败!");
		}
		
		String sql = "select * from music";
		try {
			ps = con.prepareStatement(sql);
			resultSet = ps.executeQuery();
			
			while(resultSet.next()){
				Song tem = new Song();
				tem.setName(resultSet.getString("name"));
				tem.setStyle(resultSet.getString("style"));
				tem.setMusician(resultSet.getString("musician"));
				tem.setAlbumName(resultSet.getString("albumName"));
				songs.add(tem);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return songs;
		
	}

}
