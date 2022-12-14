package NetworkProgramming.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientUI {
	private Client client;
	private JTextArea showChatArea = new JTextArea();
	private ClientListener listener;
	private JTextArea chatOutMsgArea = new JTextArea();
	private JTextField accountField = new JTextField();
	private JTextField passwordField = new JTextField();

	/**
	 * Once a client is created, connects with server automatically Generate the
	 * client UI
	 * 
	 * @param user the client name
	 */
	public ClientUI() {
		client = new Client("127.0.0.1", 8080);
		listener = new ClientListener(chatOutMsgArea, showChatArea, accountField, passwordField, client);
		loginUI();
		// start the client thread
		new Thread(new ClientThread(client, showChatArea, this)).start();
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
		accountField.setPreferredSize(new Dimension(350, 30));
		loginFrame.add(accountField);

		// password name
		JLabel passwordLabel = new JLabel("Password");
		loginFrame.add(passwordLabel);
		// account name text field
		passwordField.setPreferredSize(new Dimension(350, 30));
		loginFrame.add(passwordField);
		// login button
		JButton loginButton = new JButton("Login");
		loginFrame.add(loginButton);
		loginButton.addActionListener(listener);
		// create new account button
		JButton newAccountButton = new JButton("New account");
		loginFrame.add(newAccountButton);
		newAccountButton.addActionListener(listener);
		loginFrame.setVisible(true);
	}

	/**
	 * create the chat UI
	 * 
	 * @param user name
	 */
	public void chatUI(String accountName) {
		JFrame chatFrame = new JFrame("client " + accountName);
		chatFrame.setSize(500, 600);

		// center alignment
		chatFrame.setLocationRelativeTo(null);

		// set how to close
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setLayout(new FlowLayout());

		// show message area
		// chat history
		Font font = new Font(null, 0, 24);
		showChatArea.setFont(font);
		showChatArea.setPreferredSize(new Dimension(450, 400));
		chatFrame.add(showChatArea);

		// add text area
		chatOutMsgArea.setFont(font);
		chatOutMsgArea.setPreferredSize(new Dimension(450, 100));
		chatFrame.add(chatOutMsgArea);

		// add send button
		JButton sendButton = new JButton("  send  ");
		sendButton.addActionListener(listener);
		chatFrame.add(sendButton);
		chatFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new ClientUI();
	}
}
