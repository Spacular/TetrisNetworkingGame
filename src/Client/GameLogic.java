package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javazoom.jl.player.Player;

import java.awt.*;

public class GameLogic extends JPanel implements Runnable {
	////////// �̹�������
	ImageIcon block_img1 = new ImageIcon("./image/Blocks/11.jpg");
	ImageIcon block_img2 = new ImageIcon("./image/Blocks/22.jpg");
	ImageIcon block_img3 = new ImageIcon("./image/Blocks/33.jpg");
	ImageIcon block_img4 = new ImageIcon("./image/Blocks/44.jpg");
	ImageIcon block_img5 = new ImageIcon("./image/Blocks/55.jpg");
	ImageIcon block_img6 = new ImageIcon("./image/Blocks/66.jpg");
	ImageIcon block_img7 = new ImageIcon("./image/Blocks/77.jpg");
	ImageIcon back_block_img = new ImageIcon("./image/Blocks/88.jpg");
	ImageIcon small_lock_img = new ImageIcon("./image/Blocks/99.jpg");
	ImageIcon background_img = new ImageIcon("./image/Blocks/���ӹ��.jpg");
	ImageIcon end_img = new ImageIcon("./image/Blocks/end.jpg");
	ImageIcon win_img = new ImageIcon("./image/Blocks/win.jpg");
	///��������
	java.io.File wav1 = new java.io.File("./soundtrack/PrinceOfTetris.mp3");
		
	////���� ��纰
	String block_type[] = { "����", "�׸�", "�⿪", "���⿪", "����", "����", "������" };
	////���� �������� ���� ������ �������� ����� �����ϴ� ����
	String nowblock_type, nextblock_type;

	boolean Gamestart = true;
	boolean GameEnd = false;
	///// ���� �ٴڿ� ��ѳ�/ ���� ��ѳ� Ȯ��
	boolean Bottom = false;
	boolean UnBottom = false;

	///// ���� �� ��ǥ�� �����ϴ� �迭
	int X_point[] = new int[] { 0, 0, 0, 0 };
	int Y_point[] = new int[] { 0, 0, 0, 0 };
	int Next_X[] = new int[] { -30, -30, -30, -30 };
	int Next_Y[] = new int[] { -30, -30, -30, -30 };
	///// ���� ����
	int score = 0;
	//// ȸ���Ҷ� ���� ���¸� �����ϴ� ����
	int Block_state = 0;
	///// ȸ���Ҷ� �ӽ÷� ��ǥ�� �����ϴ� �迭
	int Temp_X[] = new int[4];
	int Temp_Y[] = new int[4];

	///// ��Ʈ���� â�� �޹���� ��ǥ�� �����ϴ� �迭.
	int Back_X_point[][] = new int[11][18];
	int Back_Y_point[][] = new int[11][18];

	///// ��Ʈ����â�� �޹�濡 ���� �׿����� üũ ���ִ� �迭.
	boolean IsBlock_Back[][] = new boolean[11][18];
	
	private int delay_state = 300;
	int delay;
	Random Ran = new Random();
	Image image;
	Graphics bground;
	Thread thread;
	int endState_WL = 0;
	Player player;
	
	public GameLogic() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch (key) {
				case 37: //����
					doMove(key);
					break;
				case 39:  // ������
					doMove(key);
					break;
				case 40: //�Ʒ�
					doMove(key);
					break;
				case 38:  // ��
					doMove(key);
					break;
				case 32:  // �����̽���
					doMove(key);
					break;
				}
			}
		});
	}

	///////////�ʱ�ȭ
	public void init(){
		Gamestart = true;
		GameEnd = false;
		Bottom = false;
		UnBottom = false;
		nextblock_type =null;
		endState_WL = 0;
		score = 0;
		for(int i =0; i<4; i++){
			X_point[i] = -30;
			Y_point[i] = -30;
			Next_X[i]= -30 ;
			Next_Y[i]= -30;
		}		
		for(int i=0; i< 11; i++){
			for(int j=0; j<18; j++){
				IsBlock_Back[i][j] = false;
			}
		}
	}
	/////////////////// �� ����/////////
	public void startgame() {
		init();
		BackPoint(30);
		play();
		Istype(NewBlock());
		

		thread = new Thread(this);
		thread.start();
	}
	
	public void stopGame(){
		if(thread != null){
			init();
			repaint();
			close();
			thread.stop();
		}
	}
	public void endGame(){
		if(thread != null){
			GameEnd= true;
			close();
			thread.stop();
		}
	}
	/////// ������ ���� �Լ� ////
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
		}
	}

	////////// �� ���ϴ°�//////////
	public String NewBlock() {
		if (Gamestart) {
			nowblock_type = block_type[Ran.nextInt(7)];
			Gamestart = false;
		} else {
			nowblock_type = nextblock_type;
		}
		nextblock_type = block_type[Ran.nextInt(7)];

		Block_state = 0;
		delay_state -= 1;
		delay = delay_state ;
		
		return nowblock_type;
	}
	///////// Ÿ���� ���� ��ǥ ����.
	public void Istype(String type) {

		switch (type) {
		case "����": {
			X_point[0] = 90;
			X_point[1] = 120;
			X_point[2] = 150;
			X_point[3] = 180;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 0;
			Y_point[3] = 0;
			break;
		}
		case "�׸�": {
			X_point[0] = 120;
			X_point[1] = 150;
			X_point[2] = 120;
			X_point[3] = 150;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 30;
			Y_point[3] = 30;
			break;
		}
		case "�⿪": {
			X_point[0] = 120;
			X_point[1] = 150;
			X_point[2] = 180;
			X_point[3] = 180;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 0;
			Y_point[3] = 30;
			break;
		}
		case "���⿪": {
			X_point[0] = 120;
			X_point[1] = 150;
			X_point[2] = 180;
			X_point[3] = 120;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 0;
			Y_point[3] = 30;
			break;
		}
		case "����": {
			X_point[0] = 150;
			X_point[1] = 120;
			X_point[2] = 150;
			X_point[3] = 180;

			Y_point[0] = 0;
			Y_point[1] = 30;
			Y_point[2] = 30;
			Y_point[3] = 30;
			break;
		}
		case "����": {
			X_point[0] = 120;
			X_point[1] = 150;
			X_point[2] = 150;
			X_point[3] = 180;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 30;
			Y_point[3] = 30;
			break;
		}
		case "������": {
			X_point[0] = 150;
			X_point[1] = 180;
			X_point[2] = 120;
			X_point[3] = 150;

			Y_point[0] = 0;
			Y_point[1] = 0;
			Y_point[2] = 30;
			Y_point[3] = 30;
			break;
		}
		}
	}
	//////// ��� ��ǥ ����//////
	public void BackPoint(int size) {
		for (int i = 0; i < 11; i++) { // ���� ĭ��
			for (int j = 0; j < 18; j++) { // ����ĭ��
				Back_X_point[i][j] = i * size; // ��ǥ
				Back_Y_point[i][j] = j * size; // ��ǥ
			}
		}
	}

	////////////// ���� �ٴ��̳� ���� ��Ҵ��� üũ �غ�. ///////////
	public boolean CheckDrop() {
		/// �ٴڿ� ��Ѵٸ�.
		if (Y_point[0] >= 510 || Y_point[1] >= 510 || Y_point[2] >= 510 || Y_point[3] >= 510) {
			Bottom = true;
		}
		/// �ٴڿ��� �ȴ������
		else {
			for (int i = 0; i < 4; i++) {
				//// ���� ������ ��ǥ�� ��� �迭�� ���� ������
				if (IsBlock_Back[(X_point[i]) / 30][((Y_point[i] + 30) / 30)] == true) {
					UnBottom = true;
					break;
				}
			}
		}
		/// ���ұ��� �Ӱų�, �������� ������
		if (Bottom | UnBottom) {
			for (int i = 0; i < 4; i++)
				IsBlock_Back[(X_point[i]) / 30][(Y_point[i]) / 30] = true;

			Bottom = false;
			UnBottom = false;
			///���� �ϼ����� üũ
			CheckLine();
			/////// end üũ
			for (int i = 0; i < 11; i++) {
				if (IsBlock_Back[i][0]) {
					GameEnd = true;
					delay_state = 300;
				}
			}
			if (GameEnd == false) {
				Istype(NewBlock());
			} else
				repaint();
			return true;
		}
		// ���� ���� �ʾѴ�
		else
			return false;
	}
	
	public void draw_Background(Graphics g){
		g.drawImage(background_img.getImage(), 0,0, null);
	}
	/// ���� �������� �̹��� �׸���
	public void drawEnd(Graphics g) {
		if(endState_WL == 1)
			g.drawImage(win_img.getImage(), 0, 0, null);
		else
			g.drawImage(end_img.getImage(), 0, 0, null);
	}

	/// ���� ���� �׸��°�/////
	public void drawNext(Graphics g){
		int x = 340, y = 0;
		if(nextblock_type == "����"){
			g.drawImage(small_lock_img.getImage(), x + 10, y + 30, null);
			g.drawImage(small_lock_img.getImage(), x + 30, y + 30, null);
			g.drawImage(small_lock_img.getImage(), x + 50, y + 30, null);
			g.drawImage(small_lock_img.getImage(), x + 70, y + 30, null);			
		}
		else if(nextblock_type == "�׸�"){		
			g.drawImage(small_lock_img.getImage(), x + 30, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 50, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 30, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 50, y + 40, null);			
		}
		else if(nextblock_type ==  "�⿪"){
			g.drawImage(small_lock_img.getImage(), x + 20, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 40, null);
		}
		else if(nextblock_type == "���⿪"){
			g.drawImage(small_lock_img.getImage(), x + 20, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 20, y + 40, null);
		}
		else if(nextblock_type == "����"){
			g.drawImage(small_lock_img.getImage(), x + 20, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 20, null);
			}
		else if(nextblock_type == "����"){
			g.drawImage(small_lock_img.getImage(), x + 20, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 40, null);
		}
		else if(nextblock_type == "������"){
			g.drawImage(small_lock_img.getImage(), x + 40, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 60, y + 20, null);
			g.drawImage(small_lock_img.getImage(), x + 20, y + 40, null);
			g.drawImage(small_lock_img.getImage(), x + 40, y + 40, null);
		}
	}
	public void drawBack_Block() {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 18; j++) {
				if (IsBlock_Back[i][j] == true) {
					bground.drawImage(back_block_img.getImage(), Back_X_point[i][j], Back_Y_point[i][j], null);
				}
			}
		}

	}
	public void drawMove(Graphics g) {
		// �� �׸���!
		if (nowblock_type == "����") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img1.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "�׸�") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img2.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "�⿪") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img3.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "���⿪") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img4.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "����") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img5.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "����") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img6.getImage(), X_point[i], Y_point[i], null);
			}
		} else if (nowblock_type == "������") {
			for (int i = 0; i < 4; i++) {
				g.drawImage(block_img7.getImage(), X_point[i], Y_point[i], null);
			}
		}
		g.setColor(Color.red);
		Font f = new Font(String.valueOf(score), Font.BOLD,30);
		g.setFont(f);
		g.drawString(String.valueOf(score), 350, 185);

		g.setColor(Color.green);
		f = new Font(String.valueOf("���� "), Font.BOLD, 25);
		g.setFont(f);
		g.drawString("���� ", 340, 145);

	}
	public void paint(Graphics g) {
		image = createImage(460, 540);
		bground = image.getGraphics();
		draw_Background(bground);
		drawBack_Block();
		drawMove(bground);
		drawNext(bground);
		if (GameEnd) {
			drawEnd(bground);
		}		
		g.drawImage(image, 0, 0, this);
	}
	////////////// Ű �Է� ������ �ҷ����� �Լ�/////////////
	public void doMove(int key) {
		if (key == 37) { // Left
			if (LeftableCheck()) {
				X_point[0] = X_point[0] - 30;
				X_point[1] = X_point[1] - 30;
				X_point[2] = X_point[2] - 30;
				X_point[3] = X_point[3] - 30;
			}
		}
		if (key == 39) { // Right
			if (RightableCheck()) {
				X_point[0] = X_point[0] + 30;
				X_point[1] = X_point[1] + 30;
				X_point[2] = X_point[2] + 30;
				X_point[3] = X_point[3] + 30;
			}
		}
		if (key == 40) { // Down
			if (DownableCheck()) {
				Y_point[0] = Y_point[0] + 30;
				Y_point[1] = Y_point[1] + 30;
				Y_point[2] = Y_point[2] + 30;
				Y_point[3] = Y_point[3] + 30;
			}
		}
		if (key == 38) // up
			BlockTurn();
		if (key == 32) { // space bar
			SpacebarDrop();
		}
		repaint();
	}
	/////////// �������� ���� ���� �մ��� üũ////////
	public boolean DownableCheck() {
		/// ���� �������� ���ٸ� �� //
		for (int i = 0; i < 4; i++) {
			if (Y_point[i] >= 510)
				return false;
		}
		CheckDrop();
		return true;
	}
	public boolean LeftableCheck() {
		int i;
		/// ���� �������� ���ٸ� �� //
		for (i = 0; i < 4; i++) {
			if (X_point[i] <= 0)
				return false;
			else {
				if (IsBlock_Back[(X_point[i] - 30) / 30][Y_point[i] / 30] == true)
					return false;
			}
		}
		return true;
	}
	public boolean RightableCheck() {
		int i;
		/// ���� ������������ ���ٸ� �� //
		for (i = 0; i < 4; i++) {
			if (X_point[i] >= 300)
				return false;
			else {
				if (IsBlock_Back[(X_point[i] + 30) / 30][Y_point[i] / 30] == true)
					return false;
			}
		}
		return true;
	}

	public void BlockTurn() {
		if (nowblock_type == "����")
			TurnLong();
		else if (nowblock_type == "�⿪")
			TurnGiyuk();
		else if (nowblock_type == "���⿪")
			TurnUngiyuk();
		else if (nowblock_type == "����")
			TurnO();
		else if (nowblock_type == "����")
			TurnRieul();
		else if (nowblock_type == "������")
			TurnUnRieul();
		Block_state++;
	}
	public void TurnLong() {
		int i = 0;
		Block_state = Block_state % 4; ///// 4�Ѿ�� ����ֱ�
		//// ������ ȭ�� ������ �ȳ������� üũ ����ߴ�.
		if (Block_state == 0 | Block_state == 2) {
			Temp_X[0] = X_point[0];
			Temp_Y[0] = Y_point[0];
			for (i = 1; i < 4; i++) { // �� >>>>��
				Temp_X[i] = X_point[0];
				Temp_Y[i] = Y_point[i] + i * 30;
			}
		} else {
			Temp_X[0] = X_point[0];
			Temp_Y[0] = Y_point[0];
			for (i = 1; i < 4; i++) { // �� >>>>>>> ��
				Temp_X[i] = X_point[i] + i * 30;
				Temp_Y[i] = Y_point[0];
			}
		}
		if (TurnCheck()) {
			for (i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public void TurnGiyuk() {
		int i;
		Block_state = Block_state % 4;
		switch (Block_state) {
		case 0:
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2] - 60;
			Temp_X[3] = X_point[3] - 60;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2] + 30;
			Temp_Y[3] = Y_point[3] + 30;
			break;
		case 1:
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1] - 30;
			Temp_X[2] = X_point[2] + 30;
			Temp_X[3] = X_point[3] + 60;

			Temp_Y[0] = Y_point[0] + 30;
			Temp_Y[1] = Y_point[1] + 60;
			Temp_Y[2] = Y_point[2] + 30;
			Temp_Y[3] = Y_point[3];
			break;

		case 2:
			Temp_X[0] = X_point[0] + 30;
			Temp_X[1] = X_point[1] + 30;
			Temp_X[2] = X_point[2] - 30;
			Temp_X[3] = X_point[3] - 30;

			Temp_Y[0] = Y_point[0] - 30;
			Temp_Y[1] = Y_point[1] - 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3];
			break;
		case 3:
			Temp_X[0] = X_point[0] - 30;
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2] + 60;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] - 30;
			Temp_Y[2] = Y_point[2] - 60;
			Temp_Y[3] = Y_point[3] - 30;
			break;
		}
		if (TurnCheck()) {
			for (i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public void TurnUngiyuk() {
		int i;
		Block_state = Block_state % 4;
		switch (Block_state) {
		case 0:
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1] - 30;
			Temp_X[2] = X_point[2] - 60;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] + 30;
			Temp_Y[2] = Y_point[2] + 60;
			Temp_Y[3] = Y_point[3] + 30;
			break;
		case 1:
			Temp_X[0] = X_point[0] + 60;
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2] + 30;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2] - 30;
			Temp_Y[3] = Y_point[3] - 30;
			break;

		case 2:
			Temp_X[0] = X_point[0] - 30;
			Temp_X[1] = X_point[1] + 60;
			Temp_X[2] = X_point[2] + 30;
			Temp_X[3] = X_point[3];

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] - 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] + 30;
			break;
		case 3:
			Temp_X[0] = X_point[0] - 30;
			Temp_X[1] = X_point[1] - 30;
			Temp_X[2] = X_point[2];
			Temp_X[3] = X_point[3] - 60;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2] - 30;
			Temp_Y[3] = Y_point[3] - 30;
			break;
		}
		if (TurnCheck()) {
			for (i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public void TurnO() {
		Block_state = Block_state % 4;
		switch (Block_state) {
		case 0:
			Temp_X[0] = X_point[0] - 30;
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2];
			Temp_X[3] = X_point[3] - 60;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] + 30;
			break;
		case 1:
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1] + 30;
			Temp_X[2] = X_point[2] + 30;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0] + 30;
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3];
			break;

		case 2:
			Temp_X[0] = X_point[0] + 30;
			Temp_X[1] = X_point[1] - 30;
			Temp_X[2] = X_point[2] - 30;
			Temp_X[3] = X_point[3];

			Temp_Y[0] = Y_point[0] - 30;
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3];
			break;
		case 3:
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2];
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1];
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] - 30;
			break;
		}
		if (TurnCheck()) {
			for (int i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public void TurnRieul() {
		Block_state = Block_state % 4; ///// 4�Ѿ�� ����ֱ�
		//// ������ ȭ�� ������ �ȳ������� üũ ����ߴ�.
		if (Block_state == 0 | Block_state == 2) {
			Temp_X[0] = X_point[0] + 60;
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2] + 30;
			Temp_X[3] = X_point[3] - 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] + 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] + 30;
		} else {
			Temp_X[0] = X_point[0] - 60;
			Temp_X[1] = X_point[1];
			Temp_X[2] = X_point[2] - 30;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] - 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] - 30;
		}
		if (TurnCheck()) {
			for (int i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public void TurnUnRieul() {
		Block_state = Block_state % 4; ///// 4�Ѿ�� ����ֱ�
		//// ������ ȭ�� ������ �ȳ������� üũ ����ߴ�.
		if (Block_state == 0 | Block_state == 2) {
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1] - 30;
			Temp_X[2] = X_point[2] + 60;
			Temp_X[3] = X_point[3] + 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] + 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] + 30;
		}

		else {
			Temp_X[0] = X_point[0];
			Temp_X[1] = X_point[1] + 30;
			Temp_X[2] = X_point[2] - 60;
			Temp_X[3] = X_point[3] - 30;

			Temp_Y[0] = Y_point[0];
			Temp_Y[1] = Y_point[1] - 30;
			Temp_Y[2] = Y_point[2];
			Temp_Y[3] = Y_point[3] - 30;
		}
		if (TurnCheck()) {
			for (int i = 0; i < 4; i++) {
				X_point[i] = Temp_X[i];
				Y_point[i] = Temp_Y[i];
			}
		}
	}
	public boolean TurnCheck() {
		for (int i = 0; i < 4; i++) {
			if (Temp_X[i] < 0 | Temp_X[i] > 300 | Temp_Y[i] > 450)
				return false;
		}
		return true;
	}

	public void SpacebarDrop() {
		delay = 0;
	}
	public void DeleteLine(int y) {
		int i, k;
		score += 1000;
		for (i = 0; i < 11; i++) {
			IsBlock_Back[i][y] = false;
		}

		for (k = y; k > 0; k--) {
			for (i = 0; i < 11; i++) {
				IsBlock_Back[i][k] = IsBlock_Back[i][k - 1];
			}
		}
	}
	public void CheckLine() {
		int k;
		for (int j = 0; j < 18; j++) {
			k = 0;
			for (int i = 0; i < 11; i++) {
				if (IsBlock_Back[i][j] == true)
					k++;
			}
			if (k == 11) {
				DeleteLine(j);
			}
		}
	}
	public void setBackXYPoint(boolean IsBlock_Back[][]){
		/*for(int j= 0; j < 18; j++){
			for(int i= 0;i<11;i++){
				this.IsBlock_Back[i][j] = IsBlock_Back[i][j];
			}
		}*/
		this.IsBlock_Back = IsBlock_Back;
		repaint();
	}
	public boolean[][] getBackXYPoint(){
		return IsBlock_Back;
	}
	public void set_endstate(boolean win){
		if(win)
			endState_WL = 1;
		else endState_WL = 0;
	}
	
	public int getSpeed(){
		return delay_state;
	}
	public void setSpeed(int value){
		delay_state = value;
	}
	
	
	public boolean getState(){
		if(thread != null){
			return true;
		}
		else
			return false;
	}
	public int getScore(){
		return score;
	}
	public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() { //http://stackoverflow.com/questions/10185314/java-thread-ending-with-a-delay
        try {
            FileInputStream fis     = new FileInputStream(wav1);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + wav1);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { while(true)
                	//if(!player.isComplete())
                		player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
}
