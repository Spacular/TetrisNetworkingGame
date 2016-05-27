package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Server.DB;

public class Server extends Thread{
    
    ServerSocket ss;
    Receiver receiver;
    Client client;
    public Server(Client c) {
        try {
        	client = c;
            ss = new ServerSocket(3333);
            System.out.println("서버 시작!");
            start();
        } catch (Exception e) {
        	System.err.println("Server.Server : " + e);
        }
    }
    @Override
    public void run() {
        while(true){
            try {
            	//JOptionPane.showMessageDialog(null,"회원가입이 되었습니다.");
                Socket s = ss.accept();
                System.out.println("클라이언트 접속완료");
                receiver = new Receiver(s, this);
                receiver.start();
            } catch (Exception e) {
            }
        }
    }
    public void closeServer(){
    	try{
    		ss.close();
    	}catch(Exception e){
    		
    	}
    }
    public void GameStart(){
    	Protocol p = new Protocol();
        p.setCmd(1004);
        try {
        	
        	receiver.out.writeObject(p);
        } catch (Exception e) {
        }    					
    	
    }
    public void SynctoClient(boolean [][] IsBlock_Back, int cmd){
        try {
        	Protocol p = new Protocol();
        	if(cmd == 1009)
        		p.SetBlockState(IsBlock_Back);
            p.setCmd(cmd);
            receiver.out.writeObject(p);
            receiver.out.reset();
        } catch (Exception e) {
        }
    }
    public String getClientIP(){
    	return ss.getInetAddress().toString();
    }
}
class Receiver extends Thread{
    Socket s;
    ObjectInputStream in;
    ObjectOutputStream out;
    String nickName;
    Server server;
    
    public Receiver(Socket s, Server server) {
        this.s = s;
        this.server = server;
        try {
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
        }
        server.client.HostAddress=server.getClientIP();
    }

    @Override
    public void run() {
        boolean repeat = true;  // 반복문 빠져나가기 위한 변수
        while(repeat){
            try {
                Protocol p = (Protocol) in.readObject();
                switch(p.getCmd()){
                case 2001: //상대가 접속함
                	server.client.setLabelMulti(server.client.NickName, p.getMsg());
                	p.setCmd(1003);
			        p.setMsg(server.client.NickName);
			        try {
			            out.writeObject(p);
			        } catch (Exception e) {
			        	System.err.println("Receiver.run.2001 : " + e);
			        }
                	//JOptionPane.showMessageDialog(null,"2001");
                	break;
                case 2002: //상대 준비완료
                	server.client.setLabelMulti(server.client.NickName, p.getMsg() + ": 준비완료");
                	server.client.enableStartbutton(true, true);
                	//JOptionPane.showMessageDialog(null,"2002");
                	break;
                case 2003: //상대 대기중
                	server.client.MultiLabelRedraw();
                	server.client.setLabelMulti(server.client.NickName, p.getMsg());
                	server.client.enableStartbutton(false, true);
                	System.out.println("2003" + p.getCmd());
                	//JOptionPane.showMessageDialog(null,"2003");
                	break;
                case 2004: //상대 나감
                	server.client.setLabelMulti(server.client.NickName, "상대");
                	server.client.enableStartbutton(false, true);
                	this.stop();
                	//JOptionPane.showMessageDialog(null,"2004");
                	break;
                case 2009:
                    server.client.redrawBackBlockMulti(p);
                    break;
                case 2010: //서버 승리
                	server.client.resettingAfterEndGame(true);
			        p.setCmd(1011);
			        p.setMsg(server.client.NickName);
			        try {
			            out.writeObject(p);
			        } catch (Exception e) {
			        	System.err.println("Receiver.run.2010 : " + e);
			        }
			        //JOptionPane.showMessageDialog(null,"2010");
			        break;
                case 2011: //서버 패배
                	server.client.resettingAfterEndGame(false);
                	server.client.setLabelMulti(server.client.NickName, p.getMsg());
                	server.client.enableStartbutton(false, true);
			        p.setCmd(1003);
			        p.setMsg(server.client.NickName);
			        try {
			            out.writeObject(p);
			        } catch (Exception e) {
			        	System.err.println("Receiver.run.2011 : " + e);
			        }
			        //JOptionPane.showMessageDialog(null,"2011");
			        break;
                }
            }catch(java.net.SocketException e){
            	System.err.println("Receiver.run : " + e);
            		this.stop();
            }catch (Exception e) {
                	System.err.println("Receiver.run : " + e);
            } 
        }
        
        try {
            // 스트림, 소켓 close
            if(in != null)
                in.close();
            if(out != null)
                out.close();
            if(s != null)
                s.close();
            
            System.out.println("소켓, 스트림 정상종료");
            
            
            
        } catch (Exception e) {
        }
    }

    public String getNickName() {
        return nickName;
    }
    
    
}
