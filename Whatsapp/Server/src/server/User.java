package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.util.Vector;

public class User{
	private final String username="root";
	private final String password="1234";
	private final String serverName="localhost";
	private final int portNumber=3306;
	private final String dbName="lab";
	private final String tablename="User";
	private Statement stmt;
	
	public Connection getConnection() throws SQLException{
		Connection conn=null;
		Properties connectionProps=new Properties();
		connectionProps.put("user",this.username);
		connectionProps.put("password",this.password);
		//System.out.println("trying to get connection!!");
		conn=DriverManager.getConnection("jdbc:mysql://"
		+this.serverName+":"+this.portNumber+"/"
		+this.dbName,connectionProps);
		//System.out.println(" Connection achieved!! ");
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
			String sql="delete FROM User";
			int a=stmt.executeUpdate(sql);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public boolean insert(String name, String userName, String phoneNumber, int port, String IP, boolean status, String password, String imagePath){
	try{	
            Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO User VALUES ("+null+", ?, ?, "+null+", ?, ?, ?, ?, ?)");
		pstmt.setString(1, name);
		pstmt.setString(2, userName);
		//pstmt.setString(3, phoneNumber);
		pstmt.setInt(3, port);
		pstmt.setString(4, IP);
		pstmt.setBoolean(5, status);
		pstmt.setString(6, password);
		pstmt.setString(7, imagePath);
                
		pstmt.executeUpdate();
                return true;
                }catch(MySQLIntegrityConstraintViolationException e)
                {
                    return false;
                }catch(SQLException e)
                {
                    return false;
                }
		//this.executeUpdate(conn, input);
	}
        
    public int getUserId(String email) throws SQLException
    {
	    Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("select * from User where userName = ?");
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		int userId = -1;
		while(rs.next())
		{
                    userId = rs.getInt("USERID");
		}
                    return userId;
    }
        
    public boolean verify(String email, String password) throws SQLException
    {
	    Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("select * from User where userName = ?");
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		String passwordDB="";
		while(rs.next())
		{
                    passwordDB = rs.getString("password");
		}
                System.out.println(passwordDB);
		if (password.equals(passwordDB))
			return true;
		else
			return false;
    }
    
    public void online(String email)
    {   try{
	    Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("update User set status = true where userName = ?;");
		pstmt.setString(1, email);
		pstmt.executeUpdate();
                
        conn.close();
            }catch (SQLException e)
            {
                
            }
    
    }
    
    public void offline(String email) throws SQLException
    {
	    Connection conn = getConnection();
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("update User set status = false where userName = ?;");
		pstmt.setString(1, email);
		pstmt.executeUpdate();
                
        conn.close();
    }
        
    
    public int getFriendCount(String email) throws SQLException
    {
        Connection conn = getConnection();
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("SELECT count(*) as c1 FROM User U LEFT JOIN Friend F ON U.userid = F.friendid WHERE F.userid = (select userid from user where userName = ?) and U.status = true;");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        int count=0;
        while(rs.next())
        {
            count = rs.getInt("c1");
        }
        conn.close();
        return count;
    }
        
    
    public int getFriendCountOffline(String email) throws SQLException
    {
        Connection conn = getConnection();
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("SELECT count(*) as c1 FROM User U LEFT JOIN Friend F ON U.userid = F.friendid WHERE F.userid = (select userid from user where userName = ?) and U.status = false;");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        int count=0;
        while(rs.next())
        {
            count = rs.getInt("c1");
        }
        conn.close();
        return count;
    }
    
    public Vector getFriendOffline(String email) throws SQLException
    {
        Connection conn = getConnection();
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("SELECT * FROM User U LEFT JOIN Friend F ON U.userid = F.friendid WHERE F.userid = (select userid from user where userName = ?) and U.status = false;");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        int count=getFriendCount(email);
        Vector<String> names = new Vector<String>();
        while(rs.next())
        {
            names.add(rs.getString("USERNAME"));
        }
        conn.close();
        return names;
    }
    
    public Vector getFriend(String email) throws SQLException
    {
        Connection conn = getConnection();
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement("SELECT * FROM User U LEFT JOIN Friend F ON U.userid = F.friendid WHERE F.userid = (select userid from user where userName = ?) and U.status = true;");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        int count=getFriendCount(email);
        Vector<String> names = new Vector<String>();
        while(rs.next())
        {
            names.add(rs.getString("USERNAME"));
        }
        conn.close();
        return names;
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
		
		/*String createString=
				"CREATE TABLE "+this.tablename + " ( "+
		"userId INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "+
		"name varchar(40) NOT NULL, "+
		"userName varchar(40) NOT NULL UNIQUE, "+
		"phoneNumber varchar(15), "+
		"port INTEGER NOT NULL, "+
		"IP varchar(15) NOT NULL, "+
		"status BOOL, "+
		"password varchar(20) NOT NULL) ";
		
		this.executeUpdate(conn1, createString);*/
		
		String input="INSERT INTO User "+
		"VALUES ("+null+", 'Saad Arsahd', 'saad.arshad', '051-4864428', 1234, 'local host', 0, '1234',"+null+")";
		
		this.executeUpdate(conn1, input);
		
		String input1="INSERT INTO User "+
		"VALUES ("+null+", 'Asad Ali', 'asad.ali', '051-1234567', 1234, 'local host', 0, '1234',"+null+")";
		
		this.executeUpdate(conn1, input1);
		
		
		String input2="INSERT INTO User "+
		"VALUES ("+null+", 'Umar Farooq', 'umar.farooq', '051-1234567', 1234, 'local host', 0, '1234',"+null+")";
		
		this.executeUpdate(conn1, input2);
	
		
		//Query
	
	

	}
	catch(Exception e){
		System.out.println("Error: Could not create the table");
		return;
	}
	
	//deleteAll(conn1);
}

}

