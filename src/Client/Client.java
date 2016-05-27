/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import Server.DB;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author USER
 */
public class Client extends javax.swing.JFrame implements Runnable {

	CardLayout card;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Thread t;
    String NickName;
    String HostAddress;
    /* 온라인 상태
     * 0: 오프라인
     * 1: 온라인(갓 로그인 상태)
     * 2: 싱글플레이(싱글플레이중)
     * 3: 로비
     * 4: 멀티플레이(멀티플레이중)
     * 7: 게임중(멀티플레이 중)
     * 5: 옵션
     * 6: 게임세팅
     */
    DB.RoomInfo roominfo[];
    final String colNames[] = {"Rank", "ID", "Score" };
    DefaultTableModel model;
    int onlineState = 0;
    
    int readystate = 0;
    DB db = new DB();
    
    public Client() {
    	initialize();
		initText();
		initPanel();
		initButton();
		OnlineChecker();
		
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                int i=JOptionPane.showOptionDialog(null, "게임을 종료하시겠습니까?", "alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if(i==JOptionPane.OK_OPTION){
                	if(db.GetOnlineState(NickName) == 7){
                		db.SetRecord(NickName, false);
                		db.SetRecord(db.getID(HostAddress), true);
                		try{
                			if(server != null){
                				server.SynctoClient(null, 1010);
                				db.deleteRoom(NickName);
                				server.closeServer();
                			}
                			else{
                				SynctoServer(null,2010);
                				db.exitRoom(HostAddress);
                				Protocol p = new Protocol();
            			        p.setCmd(2004);
            			        p.setMsg(NickName);
                				out.writeObject(p);
                				socket.close();
                        		in.close();
                        		out.close();
                			}
                				
                			
                		}catch(Exception e1){
                			System.err.println("Client.OnClose : " + e1);
                		}
                	}
                	DB.SetOnlineState(NickName, onlineState = 0);
                	System.exit(0);//cierra aplicacion
                }
                	
                else if(i==JOptionPane.CANCEL_OPTION);
                	setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
            }
        });
		
		 // 설정된 레이아웃을 가져온다.
		card = (CardLayout) ControlPanel.getLayout();
		card.show(ControlPanel, "LoginP");
		
    }

    private void initialize() {
    	//General Variable
    			ControlPanel = new javax.swing.JPanel();
    			MainPanel = new javax.swing.JPanel();
    			SingleplayPanel = new javax.swing.JPanel();
    			LobbyPanel = new javax.swing.JPanel();
    			MultiplayPanel = new javax.swing.JPanel();
    			OptionPanel = new javax.swing.JPanel();
    			//MainPanel = new ImagePanel();
    			//SingleplayPanel = new ImagePanel();
    			//LobbyPanel = new ImagePanel();
    			//MultiplayPanel = new ImagePanel();
    			//OptionPanel = new ImagePanel();
    			Btn_Singleplay = new javax.swing.JButton();
    			Btn_Multiplay = new javax.swing.JButton();
    			Btn_Option = new javax.swing.JButton();
    			Btn_Exit = new javax.swing.JButton();
    			Btn_Back_Single = new javax.swing.JButton();
    			Btn_GameStart_Single = new javax.swing.JButton();
    			GamePanel_Single = new GameLogic(){
    				public void drawEnd(Graphics g) {
    					int oldScore = db.getScore(NickName);
    					if(db.setScore(NickName, this.getScore())){
    						g.drawImage(win_img.getImage(), 0, 0, null);
    						JOptionPane.showConfirmDialog(null, "최고 기록을 갱신 했습니다.\n이전 점수 : " + oldScore + "\n현재 점수 : " + this.getScore(),
    				                   "정보", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
    					}
    					else{
    						g.drawImage(end_img.getImage(), 0, 0, null);
    						JOptionPane.showConfirmDialog(null, "현재 점수 : " + this.getScore() + "\n최고 점수 : " + oldScore,
    			                   "정보", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE ,null);
    					}
    					Btn_GameSetting_Single.setEnabled(true);
    	    			Btn_GameStart_Single.setEnabled(true);
    				}
    			};
    			//GamePanel_Single = new javax.swing.JPanel();
    			Btn_Create_Lobby = new javax.swing.JButton();
    			Btn_Back_Lobby = new javax.swing.JButton();
    			Btn_Enter_Lobby = new javax.swing.JButton();
    			Btn_Refresh_Lobby = new javax.swing.JButton();
    			Btn_GameStart_Multi = new javax.swing.JButton();
    			Btn_Back_Multi = new javax.swing.JButton();
    			jList_room = new javax.swing.JList();
    			Label_Stranger = new JLabel();
    			Label_Mine = new JLabel();
    			GamePanel_Multi = new GameLogic(){
    				@Override
    				public void run() {
    					while (!GameEnd) {
    						try {
    							Thread.sleep(delay);
    						} catch (Exception ex) {
    						}
    						if (CheckDrop() == false) {
    							for (int i = 0; i < 4; i++)
    								Y_point[i] += 30;
    							repaint();
    						}
    						else{
    							syncGame(IsBlock_Back); 					
    						}
    					}
    				}
    				public void drawEnd(Graphics g) {
    					if(endState_WL == 1)
    						g.drawImage(win_img.getImage(), 0, 0, null);
    					else
    						g.drawImage(end_img.getImage(), 0, 0, null);
    					
    					if(db.GetOnlineState(NickName) == 7)
    						gameEnded();
    				}
    			};
    			
    			label_2 = new JLabel("New label");
    			GamePanel_Multi.add(label_2);
    			GameStrangerPanel_Multi = new GameLogic();
    			//GamePanel_Multi = new javax.swing.JPanel();
    			//GameStrangerPanel_Multi = new javax.swing.JPanel();
    			scrollPane = new javax.swing.JScrollPane();
    			Btn_Back_Option = new javax.swing.JButton();
    			Btn_Apply = new javax.swing.JButton();
    			scrollPane_1 = new JScrollPane();
    			lblUsers = new JLabel();
    	   		jList_name = new JList();
    	   		login = new Login();
    	   		register = new Register();
    	   		gamesetting = new GameSetting();
    	   		Btn_GameSetting_Single = new JButton();
    	    	Btn_GameSetting_Multi = new JButton();
    	    	lblRooms = new JLabel();
    	    	Label_Win = new JLabel();
    	    	Label_Lose = new JLabel();
    	    	
    	    	roominfo = new DB.RoomInfo[20];
    			WinSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    			model = new DefaultTableModel(colNames, 0);
    	        table = new JTable(model);
    	        //table.getColumnModel().getColumn(0).setResizable(false);
    			
    			getContentPane();
    			setTitle("TetrisNetworkingGame");
    			//set size
    			setSize(800, 600);
    			setCenterWindow();
    			setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    			setResizable(false);
    			
    			setContentPane(ControlPanel);
    			ControlPanel.setLayout(new CardLayout(0, 0));
    			//ControlPanel.setBackground(java.awt.Color.WHITE);
    			//MainPanel.setBackground(java.awt.Color.WHITE);
    			ControlPanel.add(MainPanel, "MainP");
    			//SingleplayPanel.setBackground(java.awt.Color.WHITE);
    			ControlPanel.add(SingleplayPanel, "SingleP");
    			//LobbyPanel.setBackground(java.awt.Color.WHITE);
    			ControlPanel.add(LobbyPanel, "LobbyP");
    			//OptionPanel.setBackground(java.awt.Color.WHITE);
    			ControlPanel.add(OptionPanel, "OptionP");
    			//MultiplayPanel.setBackground(java.awt.Color.WHITE);
    			ControlPanel.add(MultiplayPanel, "MultiP");
    			ControlPanel.add(login, "LoginP");
    			ControlPanel.add(register, "RegisterP");
    			ControlPanel.add(gamesetting, "GameSettingP");
    			//Enter Code
    			GameStrangerPanel_Multi.init();
    			GameStrangerPanel_Multi.BackPoint(25);
    	    	gamesetting.setSpeed(GamePanel_Single.getSpeed());
    }
    
    public void initText(){
    	//MainPanel_Text
    	Btn_Singleplay.setText("Singleplay");
    	Btn_Multiplay.setText("Multiplay");
    	Btn_Option.setText("Option");
    	Btn_Exit.setText("Exit");
    	
    	//SingleplayPanel_Text
    	Btn_GameStart_Single.setText("\uAC8C\uC784 \uC2DC\uC791");
    	Btn_Back_Single.setText("\uB098\uAC00\uAE30");
    	Btn_GameSetting_Single.setText("\uAC8C\uC784 \uC124\uC815");
    	
    	//LobbyPanel_Text
		Btn_Create_Lobby.setText("Create");
		Btn_Refresh_Lobby.setText("Refresh");
		Btn_Back_Lobby.setText("Back");
		Btn_Enter_Lobby.setText("Enter");
		lblUsers.setText("\uC811\uC18D\uC790");
		lblRooms.setText("\uBC29\uBAA9\uB85D");

		//MultiplayPanel_Text
		Btn_GameStart_Multi.setText("\uAC8C\uC784 \uC2DC\uC791");
		Btn_Back_Multi.setText("\uB098\uAC00\uAE30");
		Label_Mine.setText("Null");
		Label_Stranger.setText("Null");
		Btn_GameSetting_Multi.setText("\uAC8C\uC784 \uC124\uC815");
		Label_Win.setText("\uC2B9 : ");
		Label_Lose.setText("\uD328 :");
		
		//OptionPanel_Text
		Btn_Back_Option.setText("Back");
		Btn_Apply .setText("Apply");
    }
    
    public void initPanel(){
    	//MainPanel
    	GroupLayout gl_MainPanel = new GroupLayout(MainPanel);
    	gl_MainPanel.setHorizontalGroup(
  			gl_MainPanel.createParallelGroup(Alignment.TRAILING)
 			.addGroup(gl_MainPanel.createSequentialGroup()
 				.addContainerGap(353, Short.MAX_VALUE)
 				.addGroup(gl_MainPanel.createParallelGroup(Alignment.LEADING)
 				.addComponent(Btn_Exit, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
 				.addComponent(Btn_Option, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
 				.addComponent(Btn_Multiplay, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
 				.addComponent(Btn_Singleplay, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
 			.addContainerGap(341, Short.MAX_VALUE))
    	);
 		gl_MainPanel.setVerticalGroup(
 			gl_MainPanel.createParallelGroup(Alignment.TRAILING)
 			.addGroup(gl_MainPanel.createSequentialGroup()
    			.addContainerGap(350, Short.MAX_VALUE)
    			.addComponent(Btn_Singleplay)
    			.addGap(18, 18, 18)
    			.addComponent(Btn_Multiplay)
    			.addGap(18, 18, 18)
    			.addComponent(Btn_Option)
    			.addGap(18, 18, 18)
    			.addComponent(Btn_Exit)
    		.addGap(70))
    	);
    	MainPanel.setLayout(gl_MainPanel);
    			
    	//MainPanel_Set_BackGround
    			
    	GamePanel_Single.init();
    	
    	JScrollPane scrollPane_2 = new JScrollPane();
    	
    	
    	//SingleplayPanel
    	GroupLayout gl_SingleplayPanel = new GroupLayout(SingleplayPanel);
    	gl_SingleplayPanel.setHorizontalGroup(
    		gl_SingleplayPanel.createParallelGroup(Alignment.TRAILING)
    			.addGroup(gl_SingleplayPanel.createSequentialGroup()
    				.addContainerGap()
    				.addComponent(GamePanel_Single, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
    				.addGroup(gl_SingleplayPanel.createParallelGroup(Alignment.TRAILING)
    					.addGroup(gl_SingleplayPanel.createSequentialGroup()
    						.addGroup(gl_SingleplayPanel.createParallelGroup(Alignment.TRAILING)
    							.addComponent(Btn_GameSetting_Single, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    							.addComponent(Btn_Back_Single, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    							.addComponent(Btn_GameStart_Single, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
    						.addGap(96))
    					.addGroup(gl_SingleplayPanel.createSequentialGroup()
    						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
    						.addContainerGap())))
    	);
    	gl_SingleplayPanel.setVerticalGroup(
    		gl_SingleplayPanel.createParallelGroup(Alignment.LEADING)
    			.addGroup(gl_SingleplayPanel.createSequentialGroup()
    				.addGap(10)
    				.addGroup(gl_SingleplayPanel.createParallelGroup(Alignment.LEADING)
    					.addGroup(Alignment.TRAILING, gl_SingleplayPanel.createSequentialGroup()
    						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE)
    						.addPreferredGap(ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
    						.addComponent(Btn_GameStart_Single)
    						.addGap(18)
    						.addComponent(Btn_GameSetting_Single)
    						.addGap(18)
    						.addComponent(Btn_Back_Single)
    						.addGap(30))
    					.addGroup(gl_SingleplayPanel.createSequentialGroup()
    						.addComponent(GamePanel_Single, GroupLayout.PREFERRED_SIZE, 540, GroupLayout.PREFERRED_SIZE)
    						.addContainerGap(21, Short.MAX_VALUE))))
    	);
    	
    	JLabel label = new JLabel("\uB7AD\uD0B9");
    	label.setHorizontalAlignment(SwingConstants.CENTER);
    	scrollPane_2.setColumnHeaderView(label);
    	
    	
    	
    	scrollPane_2.setViewportView(table);
    	SingleplayPanel.setLayout(gl_SingleplayPanel);
    					
 		//SingleplayPanel_Set_Background
  		/*
   		try{
  			SingleplayPanel.setBackground(ImageIO.read(new File("./image/SingleBG_800x600.png")));
  		}catch(IOException e){
    		e.getStackTrace();
    	}
    	*/
   		
    	//LobbyPanel
   		GroupLayout gl_LobbyPanel = new GroupLayout(LobbyPanel);
   		gl_LobbyPanel.setHorizontalGroup(
   			gl_LobbyPanel.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_LobbyPanel.createSequentialGroup()
   					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
   					.addGroup(gl_LobbyPanel.createParallelGroup(Alignment.LEADING)
   						.addGroup(gl_LobbyPanel.createSequentialGroup()
   							.addGap(48)
   							.addGroup(gl_LobbyPanel.createParallelGroup(Alignment.LEADING)
   								.addComponent(Btn_Back_Lobby, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
   								.addComponent(Btn_Enter_Lobby, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
   								.addComponent(Btn_Create_Lobby, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
   								.addComponent(Btn_Refresh_Lobby, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
   						.addGroup(gl_LobbyPanel.createSequentialGroup()
   							.addPreferredGap(ComponentPlacement.RELATED)
   							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
   					.addContainerGap())
   		);
   		gl_LobbyPanel.setVerticalGroup(
   			gl_LobbyPanel.createParallelGroup(Alignment.TRAILING)
   				.addGroup(gl_LobbyPanel.createParallelGroup(Alignment.BASELINE)
   					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
   					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE))
   				.addGroup(gl_LobbyPanel.createSequentialGroup()
   					.addContainerGap(407, Short.MAX_VALUE)
   					.addComponent(Btn_Refresh_Lobby)
   					.addGap(18)
   					.addComponent(Btn_Create_Lobby)
   					.addGap(18)
   					.addComponent(Btn_Enter_Lobby)
   					.addGap(18)
   					.addComponent(Btn_Back_Lobby)
   					.addGap(18))
   		);
   		lblUsers.setHorizontalAlignment(SwingConstants.CENTER);
   		scrollPane_1.setColumnHeaderView(lblUsers);
   		scrollPane_1.setViewportView(jList_name);
    	scrollPane.setViewportView(jList_room);
    	
    	
    	
    	lblRooms.setHorizontalAlignment(SwingConstants.CENTER);
    	scrollPane.setColumnHeaderView(lblRooms);
    	LobbyPanel.setLayout(gl_LobbyPanel);
    	
    	
    	
    			
    	//MultiplayPanel
    	GroupLayout gl_MultiplayPanel = new GroupLayout(MultiplayPanel);
    	gl_MultiplayPanel.setHorizontalGroup(
    		gl_MultiplayPanel.createParallelGroup(Alignment.TRAILING)
    			.addGroup(gl_MultiplayPanel.createSequentialGroup()
    				.addContainerGap()
    				.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.LEADING)
    					.addComponent(GamePanel_Multi, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
    					.addGroup(gl_MultiplayPanel.createSequentialGroup()
    						.addComponent(Label_Mine)
    						.addGap(169)
    						.addComponent(Label_Win)
    						.addGap(58)
    						.addComponent(Label_Lose)))
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.TRAILING)
    					.addComponent(GameStrangerPanel_Multi, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
    					.addComponent(Label_Stranger)
    					.addGroup(gl_MultiplayPanel.createSequentialGroup()
    						.addComponent(Btn_GameStart_Multi, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    						.addPreferredGap(ComponentPlacement.UNRELATED)
    						.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.LEADING)
    							.addComponent(Btn_GameSetting_Multi, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    							.addComponent(Btn_Back_Multi, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))))
    				.addContainerGap())
    	);
    	gl_MultiplayPanel.setVerticalGroup(
    		gl_MultiplayPanel.createParallelGroup(Alignment.LEADING)
    			.addGroup(gl_MultiplayPanel.createSequentialGroup()
    				.addContainerGap()
    				.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.BASELINE)
    					.addComponent(Label_Mine)
    					.addComponent(Label_Stranger)
    					.addComponent(Label_Win)
    					.addComponent(Label_Lose))
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.TRAILING, false)
    					.addGroup(gl_MultiplayPanel.createSequentialGroup()
    						.addComponent(GameStrangerPanel_Multi, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
    						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    						.addGroup(gl_MultiplayPanel.createParallelGroup(Alignment.BASELINE)
    							.addComponent(Btn_GameSetting_Multi)
    							.addComponent(Btn_GameStart_Multi))
    						.addPreferredGap(ComponentPlacement.UNRELATED)
    						.addComponent(Btn_Back_Multi)
    						.addContainerGap())
    					.addComponent(GamePanel_Multi, GroupLayout.PREFERRED_SIZE, 540, GroupLayout.PREFERRED_SIZE)))
    	);
   		MultiplayPanel.setLayout(gl_MultiplayPanel);
    			
   		GroupLayout gl_OptionPanel = new GroupLayout(OptionPanel);
    	gl_OptionPanel.setHorizontalGroup(
    		gl_OptionPanel.createParallelGroup(Alignment.TRAILING)
    			.addGroup(gl_OptionPanel.createSequentialGroup()
    				.addContainerGap(577, Short.MAX_VALUE)
   					.addComponent(Btn_Apply, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(Btn_Back_Option, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
    				.addContainerGap())
    	);
   		gl_OptionPanel.setVerticalGroup(
    		gl_OptionPanel.createParallelGroup(Alignment.TRAILING)
    			.addGroup(gl_OptionPanel.createSequentialGroup()
    				.addContainerGap(538, Short.MAX_VALUE)
    				.addGroup(gl_OptionPanel.createParallelGroup(Alignment.BASELINE)
    					.addComponent(Btn_Back_Option)
    					.addComponent(Btn_Apply))
    				.addContainerGap())
   		);
    	OptionPanel.setLayout(gl_OptionPanel);
    	
    	
    }
    void initButton(){
    	//MainPanel_unenable_button
    	//Btn_Singleplay.setEnabled(false);
    	//Btn_Multiplay.setEnabled(false);
    					
    	//MainPanel_Button
    	Btn_Singleplay.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
            table.setModel(db.GetRankeTable(model));
    		card.show(ControlPanel, "SingleP");
			DB.SetOnlineState(NickName, onlineState = 2);
    		}
    	});
    					
    	Btn_Multiplay.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			card.show(ControlPanel, "LobbyP");
    			DB.SetOnlineState(NickName, onlineState = 3);
    			RefreshRoomList();
    		}
    	});
    			
    	Btn_Option.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			card.show(ControlPanel, "OptionP");
    			DB.SetOnlineState(NickName, onlineState = 5);
    		}
    	});
    
    	Btn_Exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			DB.SetOnlineState(NickName, onlineState = 0);
    			System.exit(0);
    		}
    	});
    			
    	//SigleplayPanel_Button
    	Btn_GameStart_Single.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//GameStart
    			GamePanel_Single.requestFocus();
    			GamePanel_Single.startgame();
    			Btn_GameSetting_Single.setEnabled(false);
    			Btn_GameStart_Single.setEnabled(false);
    		}
    	});
    	
    	Btn_Back_Single.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			card.show(ControlPanel, "MainP");
    			DB.SetOnlineState(NickName, onlineState = 1);
    			Btn_GameSetting_Single.setEnabled(true);
    			Btn_GameStart_Single.setEnabled(true);
    			GamePanel_Single.stopGame();
    		}
    	});
    	
    	Btn_GameSetting_Single.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				card.show(ControlPanel, "GameSettingP");
   			}
   		});
    			
    			
    	//LobbyPanel_Button
    	Btn_Enter_Lobby.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			GameStrangerPanel_Multi.startgame();
		    	GameStrangerPanel_Multi.BackPoint(25);
		        GameStrangerPanel_Multi.stopGame();
		        GamePanel_Multi.init();
    			enter();
    		}
    	});
    	Btn_Back_Lobby.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			card.show(ControlPanel, "MainP");
    			DB.SetOnlineState(NickName, onlineState = 1);
    		}
    	});
    	Btn_Refresh_Lobby.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			RefreshRoomList();
    		}
    	});
    	Btn_Create_Lobby.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(db.createRoom(NickName)){
    				card.show(ControlPanel, "MultiP");
    				DB.SetOnlineState(NickName, onlineState = 4);
    				Label_Stranger.setText("상대");
    				Label_Mine.setText(NickName);
    				MultiLabelRedraw();
    				GameStrangerPanel_Multi.startgame();
    		    	GameStrangerPanel_Multi.BackPoint(25);
    		        GameStrangerPanel_Multi.stopGame();
    		        GamePanel_Multi.init();
    				enableStartbutton(false, true);
    				makeroom();
    			}
  			}
  		});
    			
    	//MultiplayPanel_Button
    	Btn_GameStart_Multi.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			if(server != null){
    				Btn_GameSetting_Multi.setEnabled(false);
        			Btn_GameStart_Multi.setEnabled(false);
    				server.GameStart();
    				resetGameStrangerPanel();
    				DB.SetOnlineState(NickName, onlineState = 7);
    				GamePanel_Multi.requestFocus();
        			GamePanel_Multi.startgame();
    			}
    			else{
    				if(readystate ==0){
    					readystate = 1;
    					Btn_GameSetting_Multi.setEnabled(false);
    					Btn_GameStart_Multi.setText("준비해제");
    					Label_Mine.setText(NickName + ": 준비완료");
    					Protocol p = new Protocol();
    			        p.setCmd(2002);
    			        p.setMsg(NickName);
    			        try {
    			            out.writeObject(p);
    			        } catch (Exception e) {
    			        }    					
    				}
    				else{
    					readystate = 0;
    					Btn_GameSetting_Multi.setEnabled(true);
    					Btn_GameStart_Multi.setText("준비하기");
    					Label_Mine.setText(NickName);
    					Protocol p = new Protocol();
    			        p.setCmd(2003);
    			        p.setMsg(NickName);
    			        try {
    			            out.writeObject(p);
    			        } catch (Exception e) {
    			        }
    				}
    			}
    		
    		}
    	});
    	Btn_Back_Multi.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
                if(db.GetOnlineState(NickName) == 7){
                	int i=JOptionPane.showOptionDialog(null, "게임을 나가시겠습니까?", "alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                	if(i==JOptionPane.OK_OPTION){
                		db.SetRecord(NickName, false);
                		db.SetRecord(db.getID(HostAddress), true);
                		try{
                			card.show(ControlPanel, "LobbyP");
                    		DB.SetOnlineState(NickName, onlineState = 3);
                    		Btn_GameSetting_Multi.setEnabled(true);
                    		Btn_GameStart_Multi.setEnabled(true);
                			if(server != null){
                				server.SynctoClient(null, 1010);
                				db.deleteRoom(NickName);
                				server.closeServer();
                				server = null;
                			}
                			else{
                				SynctoServer(null,2010);
                				db.exitRoom(HostAddress);
                				Protocol p = new Protocol();
                				p.setCmd(2004);
                				p.setMsg(NickName);
                				out.writeObject(p);
                			}
                			RefreshRoomList();
                			
                		}catch(Exception e1){
                			System.err.println("Client.OnClose : " + e1);
                		}
                	}
                }
                else{
                	card.show(ControlPanel, "LobbyP");
            		DB.SetOnlineState(NickName, onlineState = 3);
            		Btn_GameSetting_Multi.setEnabled(true);
            		Btn_GameStart_Multi.setEnabled(true);	
            		if(server != null){
            			db.deleteRoom(NickName);
            			server.closeServer();
            			server = null;
            		}
            		else{
            			db.exitRoom(HostAddress);
            			Protocol p = new Protocol();
            			p.setCmd(2004);
            			p.setMsg(NickName);
            			try{
            				out.writeObject(p);
            			}catch(Exception e1){
            				System.err.println("Btn_Bac_Multi.Listener : " + e1);
            			}
            		}
            		RefreshRoomList();
            	}
    		}
    	});
    	Btn_GameSetting_Multi.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			card.show(ControlPanel, "GameSettingP");
    		}
    	});
    	
    	//OptionPanel
    	Btn_Back_Option.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			card.show(ControlPanel, "MainP");
    			DB.SetOnlineState(NickName, onlineState = 1);
    		}
    	});
    	
    	//Login_Panel
    	login.btnRegister.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			card.show(ControlPanel, "RegisterP");
    		}
    	});
    
    	login.btnLogin.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			NickName = login.GetID();
    			if(DB.UserInfoCheck(NickName,login.GetPW())){
    				if(db.GetOnlineState(NickName) > 0)
    				{
    					if(JOptionPane.showConfirmDialog(null, "이미 온라인 상태입니다. 접속을 끊고 다시 로그인 하시겠습니까?",
    					                               		"alert", JOptionPane.OK_CANCEL_OPTION) == 0){
    						JOptionPane.showMessageDialog(null,"로그인되었습니다.");
    	    				DB.SetOnlineState(NickName, onlineState = 1);
    	    				card.show(ControlPanel, "MainP");
    					}
    				}
    				else{
    					JOptionPane.showMessageDialog(null,"로그인되었습니다.");
        				DB.SetOnlineState(NickName, onlineState = 1);
        				card.show(ControlPanel, "MainP");
    				}
    				db.deleteRoom(NickName);
    			}
    			else{
    				JOptionPane.showMessageDialog(null,"아이디가 없거나 패스워드가 잘못되었습니다.","경고",JOptionPane.WARNING_MESSAGE);
    				login.SetPW("");
    			}
    				
    		}
    	});
    	
    	login.btnExit.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			System.exit(0);
    		}
    	});
    	
    	//Register_Panel
    	register.btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//empty field check : http://tazz.tistory.com/30
				//duplication check
				if(DB.UserInfoCheck(register.GetID())){
					if(DB.EmailInfoCheck(register.GetEmail()))
						if(DB.RegisterUser(register.GetID(), register.GetPW(), register.GetEmail())){
							JOptionPane.showMessageDialog(null,"회원가입이 되었습니다.");
							card.show(ControlPanel, "LoginP");
							//자동 정보 입력
							login.SetID(register.GetID());
							login.SetPW(register.GetPW());
						}
						else{
							System.out.println("error");
							return;
						}		
					else
						JOptionPane.showMessageDialog(null,"중복된 이메일 입니다.","경고",JOptionPane.WARNING_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null,"중복된 아이디 입니다.","경고",JOptionPane.WARNING_MESSAGE);
			}
		});
    	register.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.show(ControlPanel, "LoginP");
			}
		});
    	
    	//GameSetting_Panel
    	gamesetting.btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GamePanel_Single.setSpeed(gamesetting.getSpeed());
				GamePanel_Multi.setSpeed(gamesetting.getSpeed());
				System.out.println(GamePanel_Single.getSpeed());
			}
		});
    	gamesetting.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(db.GetOnlineState(NickName) == 2) //싱글플레이
					card.show(ControlPanel, "SingleP");
				else if(db.GetOnlineState(NickName) == 4) //멀티플레이
					card.show(ControlPanel, "MultiP");
				else{
					System.out.println("gamesetting.btnBack error");
				}
			}
		});
    	gamesetting.btnLeftArrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamesetting.setSpeed(gamesetting.getSpeed()-50);
				
			}
		});
    	gamesetting.btnRightArrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamesetting.setSpeed(gamesetting.getSpeed()+50);
				
			}
		});
    	
    }
    public void setLabelMulti(String Mine, String Stranger){
    	Label_Mine.setText(Mine);
    	Label_Stranger.setText(Stranger);
    }
    public void enableStartbutton(boolean b, boolean s){
    	Btn_GameStart_Multi.setEnabled(b);
    	Btn_GameSetting_Multi.setEnabled(s);
    }
    public void resettingAfterEndGame(boolean win){
    		if(win){
    			GamePanel_Multi.set_endstate(true);
    			GamePanel_Multi.endGame();
    		}
    			
    		Btn_GameStart_Multi.setEnabled(true);
    		
    }
    void gameEnded(){
    	db.SetRecord(NickName, false);
        db.SetRecord(db.getID(HostAddress), true);
        DB.SetOnlineState(NickName, onlineState = 4);
    	if(server != null)
    		server.SynctoClient(null, 1010);
		else
			SynctoServer(null,2010);
    }
    public void resetGameStrangerPanel(){
    	GameStrangerPanel_Multi.startgame();
    	GameStrangerPanel_Multi.BackPoint(25);
        GameStrangerPanel_Multi.stopGame();
    }
    
    public void redrawBackBlockMulti(Protocol p){
    	GameStrangerPanel_Multi.setBackXYPoint(p.getBlockState());
    	GameStrangerPanel_Multi.drawBack_Block();
    }
    void makeroom(){
    	server = new Server(this);
    }
    
    void syncGame(boolean [][] IsBlock_Back){
    	
		if(server != null)
			server.SynctoClient(IsBlock_Back,1009);
		else 
			SynctoServer(IsBlock_Back,2009);
    }
    void SynctoServer(boolean [][] IsBlock_Back, int cmd){			
        try {
        	Protocol p = new Protocol();
    		if(cmd == 2009)
    			p.SetBlockState(IsBlock_Back);
        	p.setCmd(cmd);		
        	p.setMsg(NickName);
            out.writeObject(p);
            if(cmd == 2009)
            	out.reset();
        } catch (Exception e) {
        }
    }
    
    private void enter(){
        if(connected()){
        	card.show(ControlPanel, "MultiP");
			DB.SetOnlineState(NickName, onlineState = 4);
			Label_Mine.setText(NickName);
			Btn_GameStart_Multi.setText("준비하기");
			
            t = new Thread(this);
            t.start();
            
            try {
                Protocol p = new Protocol();
                p.setCmd(2001);
                p.setMsg(NickName);
                
                out.writeObject(p);
            } catch (Exception e) {
            }
        }
        
    }
    private void RefreshRoomList(){
    	java.util.Vector<String> vector = new java.util.Vector<String>(20);
		jList_room.clearSelection();
		vector = db.GetRoomList(roominfo);
		jList_room.setListData(vector);
		
		java.util.Vector<String> vector2 = new java.util.Vector<String>(100);
		jList_name.clearSelection();
		vector = db.GetUserList();
		jList_name.setListData(vector);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
    
    void setCenterWindow(){
		//reference : http://stackoverflow.com/questions/3680221/how-can-i-get-the-monitor-size-in-java
        setLocation((int)WinSize.width / 2 - getWidth() / 2, (int)WinSize.height / 2 - getHeight() / 2);
        /* different set size
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension dim = new Dimension(tool.getScreenSize());
        int height = (int) dim.getHeight();
        int width = (int) dim.getWidth();
        setSize((int)(width / 1.2), (int)(height / 1.2));
        setLocation(width / 2 - getWidth() / 2, height / 2 - getHeight() / 2);
        */
	}
    public void MultiLabelRedraw(){
    	Label_Win.setText("승 : " + db.getRecord(NickName, true));
    	Label_Lose.setText("패 : " + db.getRecord(NickName, false));
    }
    
    class ImagePanel extends javax.swing.JPanel {
		//reference : http://stackoverflow.com/questions/13038411/how-to-fit-image-size-to-jframe-size
    	java.awt.Image image;

	    public void setBackground(java.awt.Image image) {
	        this.image = image;
	    }

	    @Override
	    public void paintComponent(java.awt.Graphics G) {
	        super.paintComponent(G);
	        G.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	    }
	}
    
    //General Panel
  	private javax.swing.JPanel ControlPanel;
  	private javax.swing.JPanel MainPanel;
  	private javax.swing.JPanel SingleplayPanel;
  	private javax.swing.JPanel LobbyPanel;
  	private javax.swing.JPanel MultiplayPanel;
  	private javax.swing.JPanel OptionPanel;
  	//private ImagePanel MainPanel;
  	//private ImagePanel SingleplayPanel;
  	//private ImagePanel LobbyPanel;
  	//private ImagePanel MultiplayPanel;
  	//private ImagePanel OptionPanel;
  	private javax.swing.JButton Btn_Singleplay;
  	private javax.swing.JButton Btn_Multiplay;
  	private javax.swing.JButton Btn_Option;
  	private javax.swing.JButton Btn_Exit;
  	private javax.swing.JButton Btn_Back_Single;
  	private javax.swing.JButton Btn_GameStart_Single;
  	private GameLogic GamePanel_Single;
  	//private javax.swing.JPanel GamePanel_Single;
  	private javax.swing.JList jList_room;
  	private javax.swing.JButton Btn_Back_Lobby;
  	private javax.swing.JButton Btn_Enter_Lobby;
  	private javax.swing.JButton Btn_Create_Lobby;
  	private javax.swing.JButton Btn_Refresh_Lobby;
  	private javax.swing.JButton Btn_GameStart_Multi;
  	private javax.swing.JButton Btn_Back_Multi;
  	public static java.awt.Dimension WinSize;
  	private GameLogic GamePanel_Multi;
  	private GameLogic GameStrangerPanel_Multi;
  	//private javax.swing.JPanel GamePanel_Multi;
  	//private javax.swing.JPanel GameStrangerPanel_Multi;
  	private javax.swing.JLabel Label_Mine;
  	private javax.swing.JLabel Label_Stranger;
  	private javax.swing.JScrollPane scrollPane;
  	private javax.swing.JButton Btn_Back_Option;
	private javax.swing.JButton Btn_Apply;
	private java.awt.Image image;
	private JScrollPane scrollPane_1;
	private JLabel lblUsers;
	private JList jList_name;
	private Login login;
	private Register register;
	private GameSetting gamesetting;
	private Server server;
	private JButton Btn_GameSetting_Single;
	private JButton Btn_GameSetting_Multi;
	private JLabel lblRooms;
	private JTable table;
	private JLabel Label_Win;
	private JLabel label_2;
	private JLabel Label_Lose;
	
    private boolean connected() {
        // 소켓 생성과 함께 스트림 준비
        try {
        	// 리스트가 없거나, 선택되지 않았을 경우
            // 방에 들어가지 못하게 한다.
            if(jList_room.getSelectedIndex() == -1){    
                return false;
            }
            if(roominfo[jList_room.getSelectedIndex()].Persons == 1){
            	HostAddress = roominfo[jList_room.getSelectedIndex()].HostAddress;
            	Label_Stranger.setText(roominfo[jList_room.getSelectedIndex()].RoomHost + ": 방장");
            
            	socket = new Socket(HostAddress, 3333);

            	if(socket.isConnected()){
                db.enterRoom(HostAddress);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("서버접속 성공");
                return true;
            	}
            	
            }
            else{
            	JOptionPane.showMessageDialog(null,"방이 꽉 찼습니다.","경고",JOptionPane.WARNING_MESSAGE);
            	RefreshRoomList();
            	return false;
            }

        } catch(java.net.SocketException e){
        	JOptionPane.showMessageDialog(null,roominfo[jList_room.getSelectedIndex()].RoomName + "방과 연결할 수 없습니다.","경고",JOptionPane.WARNING_MESSAGE);
        	db.deleteRoom(roominfo[jList_room.getSelectedIndex()].RoomHost);
        	RefreshRoomList();
        }catch(Exception e){
            System.err.println("Client.connected : " + e);
        }
        return false;
    }

    @Override
    public void run() {
        boolean repeat = true;
        while(repeat){
            try {
                Protocol p = (Protocol) in.readObject();
                switch(p.getCmd()){
                case 1003: //게임 대기
                	MultiLabelRedraw();
                	//JOptionPane.showMessageDialog(null,"1003");
                	break;
                case 1004: //게임 시작
                	Btn_GameStart_Multi.setEnabled(false);
        			readystate = 0;
					Btn_GameStart_Multi.setText("준비하기");
					if(db.GetOnlineState(NickName) == 4)
						resetGameStrangerPanel();
					DB.SetOnlineState(NickName, onlineState = 7);
                	GamePanel_Multi.requestFocus();
        			GamePanel_Multi.startgame();
        			//JOptionPane.showMessageDialog(null,"1004");
        			break;
                case 1009:
                	redrawBackBlockMulti(p);
                	break;
                case 1010: //클라이언트 승리
                	resettingAfterEndGame(true);
					Label_Mine.setText(NickName);
			        p.setCmd(2011);
			        p.setMsg(NickName);
			        try {
			            out.writeObject(p);
			        } catch (Exception e) {
			        	System.err.println("Client.run.1010 : " + e);
			        }
			        //JOptionPane.showMessageDialog(null,"1010");
                	break;
                case 1011: //클라이언트 패배
                	resettingAfterEndGame(false);
					Label_Mine.setText(NickName);
			        p.setCmd(2003);
			        p.setMsg(NickName);
			        try {
			            out.writeObject(p);
			        } catch (Exception e) {
			        	System.err.println("Client.run.1011 : " + e);
			        }
			        //JOptionPane.showMessageDialog(null,"1011");
			        break;
                case 1023:
                	card.show(ControlPanel, "LobbyP");
            		DB.SetOnlineState(NickName, onlineState = 3);
            		Btn_GameSetting_Multi.setEnabled(true);
            		Btn_GameStart_Multi.setEnabled(true);	
            		db.exitRoom(HostAddress);
        			p.setCmd(2004);
        			p.setMsg(NickName);
        			try{
        				out.writeObject(p);
        			}catch(Exception e1){
        				System.err.println("Btn_Bac_Multi.Listener : " + e1);
        			}
        			RefreshRoomList();
                }
                
            }catch(java.net.SocketException e){
            	t.stop();
            }
            catch (Exception e) {
            	System.err.println("Client.run : " + e);
            }
        }
        try {
            if(in != null)
                in.close();
            if(out != null)
                out.close();
            if(socket != null)
                socket.close();
            System.out.println("클라이언트 소켓, 스트림 클로즈");
            this.setTitle("접속종료!");
        } catch (Exception e) {}
    }
    
    public void OnlineChecker() {
        // run in new thread to play in background
        new Thread() {
        	int curStatus = db.GetOnlineState(NickName);
            public void run() {
                try {
                	while(true){
                		if(onlineState == 3){
                			java.util.Vector<String> vector = new java.util.Vector<String>(20);
                			vector = db.GetRoomList(roominfo);
                			jList_room.setListData(vector);
                			
                			java.util.Vector<String> vector2 = new java.util.Vector<String>(100);
                			vector = db.GetUserList();
                			jList_name.setListData(vector);
                			
                		}
                		else if(onlineState == 2)
                			table.setModel(db.GetRankeTable(model));
                		sleep(3000);
                		/*
                		//온라인상태인데 누가 접속할 경우 : 종료
                		if((curStatus = db.GetOnlineState(NickName)) != onlineState){
                			JOptionPane.showMessageDialog(null,"다른 이용자가 접속하여 로그아웃합니다.");
                            	if(db.GetOnlineState(NickName) == 7){
                            		db.SetRecord(NickName, false);
                            		db.SetRecord(db.getID(HostAddress), true);
                            		try{
                            			if(server != null){
                            				server.SynctoClient(null, 1010);
                            				db.deleteRoom(NickName);
                            				server.closeServer();
                            			}
                            			else{
                            				SynctoServer(null,2010);
                            				db.exitRoom(HostAddress);
                            				Protocol p = new Protocol();
                        			        p.setCmd(2004);
                        			        p.setMsg(NickName);
                            				out.writeObject(p);
                            			}
                            				
                            			card.show(ControlPanel, "LoginP");
                            		}catch(Exception e1){
                            			System.err.println("Client.OnClose : " + e1);
                            		}
                            	}
                            }
                		//온라인상태인데 상태가 변할 때 -> 임계영역
                		sleep(1000);
                		}*/
                	}
                	}catch (Exception e) { System.err.println(e); }
            }
        }.start();
    }
}