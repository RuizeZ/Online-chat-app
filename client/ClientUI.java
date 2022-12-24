package NetworkProgramming.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientUI {
	private Client client;

	/**
	 * Once a client is created, connects with server automatically Generate the
	 * client UI
	 * 
	 * @param user the client name
	 */
	public ClientUI(String user) {
		client = new Client("127.0.0.1", 8080, user);
		loginUI();
	}

	/**
	 * create user login UI
	 */
	public void loginUI() {
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setSize(450, 400);

		// center alignment
		loginFrame.setLocationRelativeTo(null);

		// set how to close
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLayout(new FlowLayout());

		// account name
		JLabel accountLabel = new JLabel("Account");
		loginFrame.add(accountLabel);
		// account name text field
		JTextField accountField = new JTextField();
		accountField.setPreferredSize(new Dimension(350, 30));
		loginFrame.add(accountField);

		// password name
		JLabel passwordLabel = new JLabel("Password");
		loginFrame.add(passwordLabel);
		// account name text field
		JTextField passwordField = new JTextField();
		passwordField.setPreferredSize(new Dimension(350, 30));
		loginFrame.add(passwordField);
		// login button
		JButton loginButton = new JButton("Login");
		loginFrame.add(loginButton);
		// create new account button
		JButton newAccountButton = new JButton("New account");
		loginFrame.add(newAccountButton);

		loginFrame.setVisible(true);

	}

	/**
	 * create the chating UI
	 * 
	 * @param user user name
	 */
	public void chatUI(String user) {
		JFrame chatFrame = new JFrame("client: " + user);
		chatFrame.setSize(500, 600);

		// center alignment
		chatFrame.setLocationRelativeTo(null);

		// set how to close
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setLayout(new FlowLayout());

		// show message area
		// chat history
		Font font = new Font(null, 0, 24);
		JTextArea showChatArea = new JTextArea();
		showChatArea.setFont(font);
		showChatArea.setPreferredSize(new Dimension(450, 400));
		chatFrame.add(showChatArea);

		// add text area
		JTextArea chatOutMsgArea = new JTextArea();
		chatOutMsgArea.setFont(font);
		chatOutMsgArea.setPreferredSize(new Dimension(450, 100));
		chatFrame.add(chatOutMsgArea);

		// add send button
		JButton sendButton = new JButton("  send  ");
		ClientListener listener = new ClientListener();
		sendButton.addActionListener(listener);
		chatFrame.add(sendButton);

		new Thread(new ClientThread()).start();
		chatFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new ClientUI("3");
	}
}
