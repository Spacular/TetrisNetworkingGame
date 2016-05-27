package Client;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Font;

public class Login extends JPanel {
	private JPasswordField passwordField;
	private JTextField textField;
	protected JButton btnLogin;
	protected JButton btnRegister;
	protected JButton btnExit;
	

	/**
	 * Create the panel.
	 */
	public Login() {
		
		initialize();
	}
	private final void initialize(){
		setSize(800,600);
		
		JPanel panel = new JPanel();
		
		
		JLabel label = new JLabel("\uBE44\uBC00\uBC88\uD638 :");
		label.setFont(new Font("±¼¸²", Font.BOLD, 12));
		
		JLabel label_1 = new JLabel("\uC544\uC774\uB514 :");
		label_1.setFont(new Font("±¼¸²", Font.BOLD, 12));
		
		passwordField = new JPasswordField();
		
		textField = new JTextField();
		textField.setColumns(10);
		
		btnLogin = new JButton("\uB85C\uADF8\uC778");
		
		btnRegister = new JButton("\uD68C\uC6D0\uAC00\uC785");
		
		btnExit = new JButton("\uB098\uAC00\uAE30");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(98)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnExit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnLogin)
								.addComponent(label)
								.addComponent(label_1))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addComponent(passwordField, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnRegister))))
					.addContainerGap(99, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(126, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label))
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnRegister))
					.addGap(18)
					.addComponent(btnExit)
					.addGap(22))
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
		
	}
	String GetID(){
		return textField.getText().trim();
	}
	void SetID(String ID){
		textField.setText(ID);
	}
	
	String GetPW(){
		return passwordField.getText().trim();
	}
	void SetPW(String PW){
		passwordField.setText(PW);
	}
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		//clear textfields
		textField.setText("");
		passwordField.setText("");
	}
}
