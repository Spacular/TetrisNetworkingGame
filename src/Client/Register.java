package Client;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Register extends JPanel {
	protected JButton btnRegister;
	protected JButton btnBack;
	private JPasswordField passwordField_1;
	private JTextField textField_1;
	private JLabel label_2;
	private JTextField textField;
	private JLabel label_3;
	private JPasswordField passwordField;
	private JLabel label_id;
	private JLabel label_pw;
	private JLabel label_email;
	private JLabel label_desc;
	

	/**
	 * Create the panel.
	 */
	public Register() {
		
		initialize();
	}
	private final void initialize(){
		setSize(800,600);
		
		JPanel panel = new JPanel();
		
		
		JLabel label = new JLabel("\uC774\uBA54\uC77C :");
		label.setFont(new Font("굴림", Font.BOLD, 12));
		
		btnRegister = new JButton("\uD68C\uC6D0\uAC00\uC785");
		
		
		btnBack = new JButton("\uB3CC\uC544\uAC00\uAE30");
		
		
		JLabel label_1 = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778 :");
		label_1.setFont(new Font("굴림", Font.BOLD, 12));
		
		passwordField_1 = new JPasswordField();
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		label_2 = new JLabel("\uC544\uC774\uB514 :");
		label_2.setFont(new Font("굴림", Font.BOLD, 12));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		label_3 = new JLabel("\uBE44\uBC00\uBC88\uD638 :");
		label_3.setFont(new Font("굴림", Font.BOLD, 12));
		
		passwordField = new JPasswordField();
		
		label_id = new JLabel("");
		
		label_pw = new JLabel("");
		
		label_email = new JLabel("");
		
		label_desc = new JLabel("");
		label_desc.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createSequentialGroup()
										.addGap(13)
										.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_panel.createSequentialGroup()
										.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
											.addGroup(gl_panel.createSequentialGroup()
												.addComponent(btnRegister)
												.addGap(31))
											.addGroup(gl_panel.createSequentialGroup()
												.addComponent(label)
												.addGap(18)))
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(textField_1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnBack, Alignment.TRAILING)))
									.addGroup(gl_panel.createSequentialGroup()
										.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(label_id, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_pw, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_email, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE))
							.addGap(76))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(label_desc, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(58, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(label_2))
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_id, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)))
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(label_3))
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(label_1))
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_pw, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(label_email, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBack)
						.addComponent(btnRegister))
					.addGap(31)
					.addComponent(label_desc, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
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
		
		checkEnableRegister();
		
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			    if(new EmailValidator().validate(textField_1.getText())){
			    	label_email.setText("v");
			    }
			    else{
			    	label_email.setText("x");
			    	label_desc.setText("이메일을 정확하게 입력해 주세요.");
			    }
			    checkEnableRegister();
			  }
			public void removeUpdate(DocumentEvent e) {
				if(new EmailValidator().validate(textField_1.getText())){
			    	label_email.setText("v");
			    }
			    	
			    else{
			    	label_email.setText("x");
			    	label_desc.setText("이메일을 정확하게 입력해 주세요.");
			    }
				checkEnableRegister();
			}
			public void insertUpdate(DocumentEvent e) {
				if(new EmailValidator().validate(textField_1.getText())){
			    	label_email.setText("v");
			    }
			    	
			    else{
			    	label_email.setText("x");
			    	label_desc.setText("이메일을 정확하게 입력해 주세요.");
			    }
				checkEnableRegister();
			}
		});
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().length() <= 7){
						  label_pw.setText("x");
						  label_desc.setText("비밀번호를 8 자 이상 입력해 주세요.");
						  btnRegister.setEnabled(false);
					  }
				  checkEnableRegister();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().length() <= 7){
					  label_pw.setText("x");
					  label_desc.setText("비밀번호를 8 자 이상 입력해 주세요.");
					  btnRegister.setEnabled(false);
				  }
				  checkEnableRegister();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().length() <= 7){
					  label_pw.setText("x");
					  label_desc.setText("비밀번호를 8 자 이상 입력해 주세요.");
					  btnRegister.setEnabled(false);
				  }
				  checkEnableRegister();
			  }
			});
		
		passwordField_1.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().equals(passwordField.getText())){
					  label_pw.setText("v");
				  }
				  else{
					  label_pw.setText("x");
					  label_desc.setText("입력한 두 개의 패스워드가 일치하지 않습니다.");
				  }
				  checkEnableRegister();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().equals(passwordField.getText())){
					  label_pw.setText("v");
				  }
				  else{
					  label_pw.setText("x");
					  label_desc.setText("입력한 두 개의 패스워드가 일치하지 않습니다.");
				  }
				  checkEnableRegister();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  if(passwordField_1.getText().equals(passwordField.getText())){
					  label_pw.setText("v");
				  }
				  else{
					  label_pw.setText("x");
					  label_desc.setText("입력한 두 개의 패스워드가 일치하지 않습니다.");
				  }
				  checkEnableRegister();
			  }
			});
	}
	
	private void checkEnableRegister(){
		if(new EmailValidator().validate(textField_1.getText()) && passwordField_1.getText().length() > 7 && 
				passwordField_1.getText().equals(passwordField.getText())){
			label_desc.setText("회원가입하여 주십시오");
			btnRegister.setEnabled(true);
		}
		else
			btnRegister.setEnabled(false);
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
	String GetPWvaild(){
		return passwordField_1.getText().trim();
	}
	void SetPWvaild(String PW){
		passwordField_1.setText(PW);
	}
	String GetEmail(){
		return textField_1.getText().trim();
	}
	void SetEmail(String email){
		textField_1.setText(email);
	}
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		//clear textfields
		textField.setText("");
		textField_1.setText("");
		passwordField.setText("");
		passwordField.setText("");
		label_desc.setText("");
	}
}
class EmailValidator {
	//http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private java.util.regex.Pattern pattern;
	private java.util.regex.Matcher matcher;

	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		pattern = java.util.regex.Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
}
