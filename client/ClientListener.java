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
	 * send account name and password to server
	 * 
	 */
	private void login() throws Exception {
		String accountName = accountField.getText();
		String password = passwordField.getText();
		client.writeMsg(accountName);
		client.writeMsg(password);
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
		showChatArea.setText(showChatArea.getText() + "\r\n" + msg);
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
