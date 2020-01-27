package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class Friend{
	private final String username="root";
	private final String password="1234";
	private final String serverName="localhost";
	private final int portNumber=3306;
	private final String dbName="lab";
	private final String tablename="Friend";
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
	
	public void MakeDB(Connection conn){
		stmt=null;
		String sql="CREATE DATABASE STUDENT";
		try{
			stmt.executeUpdate(sql);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void deleteAll(Connection conn){
		Statement stmt=null;
		try{
			stmt=(Statement) conn.createStatement();
			String sql="delete FROM Friend";
			int a=stmt.executeUpdate(sql);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean insert(int userId, int friendId) throws SQLException{
		Connection conn = getConnection();
		try
                {
                    PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO Friend (USERID,FRIENDID)\n" +
                    "SELECT * FROM (SELECT ?,?) AS tmp\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    SELECT userId FROM Friend WHERE userId = ? and friendId = ?\n" +
                    ") LIMIT 1;");
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, friendId);
                    pstmt.setInt(3, userId);
                    pstmt.setInt(4, friendId);
                    pstmt.executeUpdate();
                    
                    PreparedStatement pstmt1 = (PreparedStatement) conn.prepareStatement("INSERT INTO Friend (USERID,FRIENDID)\n" +
                    "SELECT * FROM (SELECT ?,?) AS tmp\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    SELECT userId FROM Friend WHERE userId = ? and friendId = ?\n" +
                    ") LIMIT 1;");
                    pstmt1.setInt(1, friendId);
                    pstmt1.setInt(2, userId);
                    pstmt1.setInt(3, friendId);
                    pstmt1.setInt(4, userId);
                    pstmt1.executeUpdate();


                    /*PreparedStatement pstmt1 = (PreparedStatement) conn.prepareStatement("INSERT INTO Friend VALUES ( ?, ?)");
                    pstmt1.setInt(2, userId);
                    pstmt1.setInt(1, friendId);
                    pstmt1.executeUpdate();*/
                    return true;
                }catch(MySQLIntegrityConstraintViolationException e)
                {
                    return false;
                }catch(SQLException e)
                {
                    return false;
                }
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
		"userId INTEGER NOT NULL REFERENCES User(userId), "+
		"friendId INTEGER NOT NULL REFERENCES User(userId)) ";
		
		this.executeUpdate(conn1, createString);
		
	/*	String input="INSERT INTO Friend "+
		"VALUES (1,2)";
		
		this.executeUpdate(conn1, input);
		
		String input1="INSERT INTO Friend "+
		"VALUES (2,1)";
		
		this.executeUpdate(conn1, input1);
		
		String input2="INSERT INTO Friend "+
		"VALUES (1,3)";
		
		this.executeUpdate(conn1, input2);
		
		String input4="INSERT INTO Friend "+
		"VALUES (3,1)";
		
		this.executeUpdate(conn1, input4);*/
		
		//Query
	
	

	}
	catch(Exception e){
		System.out.println("Error: Could not create the table");
		return;
	}
	
	//deleteAll(conn1);
}

}

