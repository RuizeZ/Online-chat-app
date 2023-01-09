package NetworkProgramming.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * implements all client listener
 * 
 * @author imrui
 *
 */
public class ClientListener implements ActionListener {
	private JTextArea chatOutMsgArea, showChatArea;
	private JTextField accountField, passwordField;
	private Client client;

	public ClientListener(JTextArea chatOutMsgArea, JTextArea showChatArea, JTextField accountField,
			JTextField passwordField, Client client) {
		super();
		this.chatOutMsgArea = chatOutMsgArea;
		this.showChatArea = showChatArea;
		this.accountField = accountField;
		this.passwordField = passwordField;
		this.client = client;
	}

	/**
	 * actions for each button 1. "Login": if account and password are correct, show
	 * the chat UI 2. "New account": create a new account 3. "send": send msg
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		try {
			switch (str) {
			case "Login":
				login();
				break;
			case "New account":
				register();
				break;
			case "  send  ":
				send();
				break;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	/**
	 * call client.register() to create a new account
	 * 
	 * @throws Exception
	 */
	private void register() throws Exception {
		client.register(accountField.getText(), passwordField.getText());

	}

	/**
	 * call client.login() to validate the account and password
	 * 
	 * @throws Exception
	 */
	private void login() throws Exception {
		client.login(accountField.getText(), passwordField.getText());

	}

	/**
	 * after click send button, three things are going to happen 1. send msg to
	 * server 2. show msg in the showChatArea 3. clear msg in the chatOutMsgArea
	 */
	private void send() {
		// 1. send msg to server
		// get text from the chatOutMsgArea
		String msg = chatOutMsgArea.getText();
		// 2. show msg in the showChatArea
		showChatArea.setText(showChatArea.getText() + "\r\n" + msg.split(":")[1]);
		try {
			client.writeMsg(msg);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 3. clear msg in the chatOutMsgArea
		chatOutMsgArea.setText("");
	}

}
