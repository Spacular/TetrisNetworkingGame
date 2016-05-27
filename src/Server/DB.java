package Server;

import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DB {
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	private static int CheckUseOnlineStatus = 0;
	
	public DB(){
		try {			
			conn = DriverManager.getConnection("jdbc:mysql://59.23.179.28/tetrisng", "guest", "");
			stmt = conn.createStatement();
			//System.out.println(stmt.get);
			//
			System.out.println("Connection Success");
		}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			JOptionPane.showMessageDialog(null,"인터넷이 원할하지 않거나 DB서버와 연결되지 않습니다.","경고",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		catch(Exception e){
			System.err.println("DB : " + e);
			
		}
	}
	public static boolean UserInfoCheck(String ID){
		try {
			String query = "select uid from userinfo where uid=\"" + ID + "\"";

			rs = stmt.executeQuery(query);
			if(rs.next()){
				if(ID.equals(rs.getString("uid"))){
					//System.out.println("ID duplication");
					return false;
				}
				else{
					System.out.println("Error");
					return false;
				}
			}
			else{
				//System.out.println("Dont use ID");
				return true;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return false;
	}
	
	public static boolean UserInfoCheck(String ID, String Passwd){
		try {
			String query = "select uid, passwd from userinfo where uid=\"" + ID + "\"";
			rs = stmt.executeQuery(query);
			if(rs.next()){
				if(Passwd.equals(rs.getString("passwd"))){
					//System.out.println("Login Success");
					return true;
				}
				else{
					System.out.println("error");
					return false;
				}
			}
			else{
				//System.out.println("Login Failed");
				return false;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return false;
	}
	
	public static boolean EmailInfoCheck(String Email){
		try {
			String query = "select email from userinfo where email=\"" + Email + "\"";

			rs = stmt.executeQuery(query);
			//System.out.println(rs.getString("email"));
			System.out.println(Email);
			if(rs.next()){
				if(Email.equals(rs.getString("email"))){
					//System.out.println("Email duplication");
					return false;
				}
				else{
					System.out.println("error");
					return false;
				}
			}
			else{
				//System.out.println("Dont use Email");
				return true;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return false;
	}
	
	public static boolean setScore(String ID, int Score){
		try{
			String query = "update userinfo set TScore = " + Score + " where uid=\"" + ID + "\"";
			
			if(getScore(ID) < Score)
				if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
					return true;
				else return false;
			else return false;
		}catch(Exception e){
			System.err.println("DB.SetScore : " + e);
		}
		return false;
	}
	public static int getScore(String ID){
		try{
			String query = "select uid, TScore from userinfo where uid=\"" + ID + "\"";

			rs = stmt.executeQuery(query);

			if(rs.next()){
				return rs.getInt("TScore");
			}
			else{
				return 0;
			}
		}catch(Exception e){
			System.err.println("DB.GetScore : " + e);
		}
		return 0;
	}
	public static boolean SetRecord(String ID, boolean Record){ //true : 승리
		try{
			String query;
			if(Record){ //승리
				query = "update userinfo set TWin = " + (getRecord(ID,true)+1) + " where uid=\"" + ID + "\"";
			}
			else
				query = "update userinfo set TLose = " + (getRecord(ID,false)+1) + " where uid=\"" + ID + "\"";
			if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
					return true;
			else return false;
		}catch(Exception e){
			System.err.println("DB.SetScore : " + e);
		}
		return false;
	}
	public static int getRecord(String ID, boolean Record){
		try{
			String query = "select uid, TWin, TLose from userinfo where uid=\"" + ID + "\"";

			rs = stmt.executeQuery(query);

			if(rs.next()){
				if(Record)
					return rs.getInt("TWin");
				else
					return rs.getInt("TLose");
			}
			else{
				return 0;
			}
		}catch(Exception e){
			System.err.println("DB.GetRecord : " + e);
		}
		return 0;
	}
	
	public static boolean SetOnlineState(String ID,int onlineState){
		try{
			String query = "update userinfo set OnlineStatus = " + onlineState + " where uid=\"" + ID + "\"";
			
			if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
			{
				return true;
			}
		}catch(Exception e){
			System.err.println("DB.SetOnlineState : " + e);
		}
		return false;
	}
	
	public int GetOnlineState(String ID){
		try{
			String query = "select uid, OnlineStatus from userinfo where uid=\"" + ID + "\"";
				rs = stmt.executeQuery(query);

				if(rs.next()){
					return rs.getInt("OnlineStatus");
				}
				else{
					return 0;
				}
			
		}catch(Exception e){
			System.err.println("DB.GetOnlineState : " + e);
		}
		return 0;
	}
	
	public boolean createRoom(String ID){
		try{
			//http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			java.io.BufferedReader in = new java.io.BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));

			String ip = in.readLine(); //you get the IP as a String
			
			String query_s = "select rid from roominfo";
			rs = stmt.executeQuery(query_s);
			
			int rid=0;
			for(rid=0;rs.next();rid++)
				if(rs.getInt("rid") == rid)
					continue;
			String query = "INSERT INTO roominfo(rid,rname,rhost,hostaddress) VALUES(\"" +
						rid + "\",\"" + ID + "님의 방\",\"" + ID + "\",\"" + ip + "\")";
			
			if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
				return true;
		}catch(Exception e){
			System.err.println("DB.createRoom : " + e);
		}
		return false;	
	}
	
	public void enterRoom(String ip){
		try{
			String query = "update roominfo set persons = " + 2 + " where hostaddress=\"" + ip + "\"";
			
			if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
				return;
		}catch(Exception e){
			System.err.println("DB.enterRoom : " + e);
		}
		return;
	}
	public void exitRoom(String ip){
		try{
			String query = "update roominfo set persons = " + 1 + " where hostaddress=\"" + ip + "\"";
			
			if(stmt.executeUpdate(query) == 1) //http://okky.kr/article/118193
				return;
		}catch(Exception e){
			System.err.println("DB.exitroom : " + e);
		}
		return;
	}
	public String getID(String ip){
		try{
			String query = "select rhost from roominfo where hostaddress=\"" + ip + "\"";

			rs = stmt.executeQuery(query);

			if(rs.next()){
				return rs.getString("rhost");
			}
			else{
				return null;
			}
		}catch(Exception e){
			System.err.println("DB.GetID : " + e);
		}
		return null;
	}
	public boolean deleteRoom(String ID){
		try{
			String query = "delete from roominfo where rhost=\"" + ID + "\"";
			
			if(stmt.executeUpdate(query) == 1){ //http://okky.kr/article/118193
				return true;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		
		return false;
	}

	public java.util.Vector<String> GetRoomList(RoomInfo roominfo[]){
		java.util.Vector<String> vector = new java.util.Vector<String>(20);
		try{
			String query = "select * from roominfo";
			int i =0;
			rs = stmt.executeQuery(query);
			while(rs.next()){
				roominfo[i] = new RoomInfo();
				roominfo[i].rid = rs.getInt("rid");
				roominfo[i].RoomHost = rs.getString("rhost");
				roominfo[i].RoomName = rs.getString("rname");
				roominfo[i].Persons = rs.getInt("persons");
				roominfo[i].HostAddress = rs.getString("hostaddress");
				
				vector.add("<html>" + (roominfo[i].rid+1) + ".  " + roominfo[i].RoomName + 
						"<br>" + roominfo[i].RoomHost + "  " + roominfo[i].Persons + "/2</span></html>");
				i++;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return vector;
	}
	public java.util.Vector<String> GetUserList(){
		java.util.Vector<String> vector = new java.util.Vector<String>(100);
		try{
			String query = "select uid, onlinestatus from userinfo";
			int i =0;
			rs = stmt.executeQuery(query);
			while(rs.next()){
				if(rs.getInt("onlinestatus") == 3)
					vector.add(rs.getString("uid"));
				i++;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return vector;
	}
	
	public DefaultTableModel GetRankeTable(DefaultTableModel table){
		DefaultTableModel model = table;
		try{
        	
        	String query = "select uid, TScore from userinfo order by TScore desc";   
            rs = stmt.executeQuery(query);
            int i =1;
            while(rs.next()){
                model.addRow(new Object[]{i, rs.getString("uid"),rs.getInt("TScore")});
                i++;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
		return model;
	}
	
	public static boolean RegisterUser(String ID, String Passwd, String Email){
		try{
			java.sql.Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
			String query = "INSERT INTO userinfo(uid,passwd,email,Register_Day) VALUES(\"" + 
			          ID + "\",\"" + Passwd + "\",\"" + Email + "\",\"" + timestamp + "\")";

			if(stmt.executeUpdate(query) == 1){ //http://okky.kr/article/118193
				return true;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return false;
	}
	
	public class RoomInfo{
		public int rid; 		//방번호
		public String RoomName; //방이름
		public String RoomHost; //방장
		public int Persons; //+인원
		public String HostAddress;
	}
	
}
//No enclosing instance of type... 컴파일 오류 : http://blog.daum.net/_blog/BlogTypeView.do?blogid=0NhTQ&articleno=251
