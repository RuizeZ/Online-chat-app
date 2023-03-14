package NetworkProgramming.client;

import java.awt.BorderLayout;
import java.awt.Color;
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
		Font font = new Font(null, 0, 24);
		chatFrame.setSize(750, 750);

		// center alignment
		chatFrame.setLocationRelativeTo(null);

		// set how to close
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(Color.white);
		chatFrame.add(centerPanel, BorderLayout.CENTER);

		// message panel
		JPanel centerSouthPanle = new JPanel();
		centerSouthPanle.setLayout(new BorderLayout());
		centerSouthPanle.setPreferredSize(new Dimension(0, 150));
		centerSouthPanle.setBackground(Color.WHITE);
		centerPanel.add(centerSouthPanle, BorderLayout.SOUTH);
		
		// add text area
		chatOutMsgArea.setFont(font);
		JScrollPane jsc = new JScrollPane(chatOutMsgArea);
		jsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerSouthPanle.add(jsc);
		
		// add send button
		JPanel sendButtonPanle = new JPanel();
//		sendButtonPanle.setLayout(null);
		JButton sendButton = new JButton("  send  ");
		sendButtonPanle.add(sendButton);
		sendButton.addActionListener(listener);
		sendButton.setBounds(400, 114, 80, 30);
		centerSouthPanle.add(sendButtonPanle, BorderLayout.SOUTH);

		// friends list panel
		JPanel eastPanle = new JPanel();
		eastPanle.setLayout(new BorderLayout());
		eastPanle.setPreferredSize(new Dimension(150, 0));
		eastPanle.setBackground(Color.WHITE);
		// friends list
		JScrollPane friendListScrollPane = new JScrollPane();
		friendListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		friendListScrollPane.setPreferredSize(new Dimension(150, 400));
		JList<String> userList = new JList<>(new String[] { "a", "b", "c" });
		userList.setFont(font);
		friendListScrollPane.setViewportView(userList);
		eastPanle.add(friendListScrollPane);
		chatFrame.add(eastPanle, BorderLayout.EAST);

		// show message area
		// chat history
		JPanel centerCentPanel = new JPanel();
		centerCentPanel.setLayout(new BorderLayout());
		centerCentPanel.setBackground(Color.WHITE);
		centerPanel.add(centerCentPanel, BorderLayout.CENTER);
		
		showChatArea.setFont(font);
		showChatArea.setEditable(false);
		jsc = new JScrollPane(showChatArea);
		jsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerCentPanel.add(jsc);


		chatFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new ClientUI();
	}
}
