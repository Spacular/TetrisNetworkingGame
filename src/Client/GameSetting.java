package Client;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class GameSetting extends JPanel {
	protected JButton btnApply;
	protected JButton btnBack;
	private JLabel lblNull;
	protected JButton btnLeftArrow;
	protected JButton btnRightArrow;

	public GameSetting() {
		initialize();
	}

	private final void initialize(){
		setSize(800,600);
		
		JPanel panel = new JPanel();
		JLabel label_1 = new JLabel("\uC18D\uB3C4 :");
		btnApply = new JButton("\uC801\uC6A9");
		btnBack = new JButton("\uB098\uAC00\uAE30");
		lblNull = new JLabel("NULL");
		btnLeftArrow = new JButton("");
		btnRightArrow = new JButton("");
		
		try{
			java.awt.Image left = javax.imageio.ImageIO.read(new java.io.File("./image/Arrow_Left.png")).getScaledInstance(5, 10, java.awt.Image.SCALE_SMOOTH);
			java.awt.Image right = javax.imageio.ImageIO.read(new java.io.File("./image/Arrow_Right.png")).getScaledInstance(5, 10, java.awt.Image.SCALE_SMOOTH);
			btnLeftArrow.setIcon(new ImageIcon(left));
			btnRightArrow.setIcon(new ImageIcon(right));

		}catch(Exception e){}
		
		//btnLeftArrow.setIcon(new ImageIcon(MainWindow.class.getResource(Arrow_Left)));
		label_1.setFont(new Font("±¼¸²", Font.BOLD, 12));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNull)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLeftArrow)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRightArrow)
					.addContainerGap(172, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(220, Short.MAX_VALUE)
					.addComponent(btnApply)
					.addGap(18)
					.addComponent(btnBack)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_1)
								.addComponent(lblNull)
								.addComponent(btnLeftArrow))
							.addPreferredGap(ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnBack)
								.addComponent(btnApply)))
						.addComponent(btnRightArrow))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(200)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(200))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(250)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(50))
		);
		setLayout(groupLayout);
		//LoginPanel
	}
	
	public void setSpeed(int value){
		lblNull.setText(String.valueOf(value));
	}
	public int getSpeed(){
		return Integer.parseInt(lblNull.getText());
	}
}


	