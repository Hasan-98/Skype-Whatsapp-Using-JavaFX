package server;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Message{
	private final String username="root";
	private final String password="1234";
	private final String serverName="localhost";
	private final int portNumber=3306;
	private final String dbName="lab";
	private final String tablename="Message";
	private Statement stmt;
	
	public Connection getConnection() throws SQLException{
		Connection conn=null;
		Properties connectionProps=new Properties();
		connectionProps.put("user",this.username);
		connectionProps.put("password",this.password);
		System.out.println("trying to get connection!!");
		conn=DriverManager.getConnection("jdbc:mysql://"
		+this.serverName+":"+this.portNumber+"/"
		+this.dbName,connectionProps);
		System.out.println(" Connection achieved!! ");
		return conn;
	}
	
	public boolean executeUpdate(Connection conn ,String command) throws SQLException{
		try{
		Statement stmt=null;
		stmt=(Statement) conn.createStatement();
		stmt.executeUpdate(command);
		return true;
		}
		finally{
			if (stmt!=null){stmt.close();}
		}
	}
	public void deleteAll(Connection conn){
		Statement stmt=null;
		try{
			stmt=(Statement) conn.createStatement();
			String sql="delete FROM Message";
			int a=stmt.executeUpdate(sql);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void insert(int fromId, int toId, String msg) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO Message VALUES ("+null+", ?, ?, ?,"+null+" , NOW())");
		//String input="INSERT INTO Message "+
		//"VALUES ("+null+", fromId, toId, msg, "+null+",NOW())";
		pstmt.setInt(1, fromId);
		pstmt.setInt(2, toId);
		pstmt.setString(3, msg);
		pstmt.executeUpdate();
		//this.executeUpdate(conn, input);
	}
	
	public void makeTable(){
		Connection conn1=null;
		try{
			conn1=this.getConnection();
			System.out.println("Connection Name is :: "+conn1.getClass().getName());
			System.out.println("Connected to database");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	
	
	try{
		
		String createString=
		"CREATE TABLE "+this.tablename + " ( "+
		"messageId INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "+
		"fromId INTEGER	NOT NULL REFERENCES User(userId), "+
		"toId INTEGER NOT NULL REFERENCES User(userId), "+
		"text varchar(1000) NOT NULL, "+
		"readDate datetime, "+
		"sendDate datetime NOT NULL )";
		
		this.executeUpdate(conn1, createString);
		
		/*String input="INSERT INTO Message "+
		"VALUES ("+null+", 1, 2, 'HELLO MONGO', "+null+",NOW())";
		
		this.executeUpdate(conn1, input);
		
		String input1="INSERT INTO Message "+
				"VALUES ("+null+", 2, 1, 'HELLO SAAD', "+null+",NOW())";
		
		this.executeUpdate(conn1, input1);
		
	*/
		
		//Query
	
	

	}
	catch(Exception e){
		System.out.println("Error: Could not create the table");
		return;
	}
	
	//deleteAll(conn1);
}

}

