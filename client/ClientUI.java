package NetworkProgramming.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ClientUI {
	Client client;
	JTextArea showChatArea = new JTextArea();
	JTextPane showChatTextPane = new JTextPane();
	ClientListener listener;
	JScrollPane showChatAreaJsc;
	JTextArea chatOutMsgArea = new JTextArea();
	JTextField accountField = new JTextField();
	JTextField passwordField = new JTextField();
	JPanel centerPanel = new JPanel();
	JList<String> userList;
	JTextArea currentsShowChatArea;
	Map<String, JTextPane> userShowChatMap;
	JFrame chatFrame;
	String accountName;
	Set<String> newMessageFromSet = new HashSet<>();
	UserListRander userListRander = new UserListRander<>(this);
	Font font = new Font(null, 0, 24);

	/**
	 * Once a client is created, connects with server automatically Generate the
	 * client UI
	 * 
	 * @param user the client name
	 */
	public ClientUI() {
		userShowChatMap = new HashMap<String, JTextPane>();
		client = new Client("127.0.0.1", 8080, this);
		listener = new ClientListener(this, client);
		loginUI();

	}

	/**
	 * add new user and its showChatArea to map
	 */
	public void updateShowChatAreaToMap() {
		for (String name : client.userList) {
			if (!userShowChatMap.containsKey(name)) {
				JTextArea showChatArea = new JTextArea();
				JTextPane newShowChatTextPane = new JTextPane();
				newShowChatTextPane.setFont(font);
				userShowChatMap.put(name, newShowChatTextPane);
			}
		}
	}

	/**
	 * change showChatArea to show msg history for a certain user
	 * 
	 * @param name user accountName
	 */
	public void changeShowChatArea(String name) {
		newMessageFromSet.remove(name);
		userList.repaint();
		chatOutMsgArea.setEditable(true);
		centerPanel.removeAll();
		showChatTextPane = userShowChatMap.get(name);
		showChatTextPane.setEditable(false);
		showChatAreaJsc = new JScrollPane(showChatTextPane);
		showChatAreaJsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(showChatAreaJsc);
		centerPanel.revalidate();
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
	/**
	 * @param accountName
	 */
	/**
	 * @param accountName
	 */
	public void chatUI(String accountName) {
		chatFrame = new JFrame("client " + accountName);

		chatFrame.setSize(750, 750);

		// center alignment
		chatFrame.setLocationRelativeTo(null);

		// set how to close
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setLayout(new BorderLayout());

		// show message panel
		centerPanel.setLayout(new BorderLayout());
		chatFrame.add(centerPanel, BorderLayout.CENTER);

		// message panel
		JPanel centerSouthPanle = new JPanel();
		centerSouthPanle.setLayout(new BorderLayout());
		centerSouthPanle.setPreferredSize(new Dimension(0, 150));
		centerSouthPanle.setBackground(Color.WHITE);
		chatFrame.add(centerSouthPanle, BorderLayout.SOUTH);

		// add text area
		chatOutMsgArea.setFont(font);
		chatOutMsgArea.setEditable(false);
		JScrollPane jsc = new JScrollPane(chatOutMsgArea);
		jsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerSouthPanle.add(jsc);

		// add send button
		JPanel sendButtonPanle = new JPanel();
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
		userList = new JList<>();
		userList.setCellRenderer(userListRander);
		userList.addListSelectionListener(listener);
		userList.setFont(font);
		friendListScrollPane.setViewportView(userList);
		eastPanle.add(friendListScrollPane);
		chatFrame.add(eastPanle, BorderLayout.EAST);

//		 show message area
//		 chat history
		showChatArea.setFont(font);
		showChatArea.setEditable(false);
		showChatArea.setBackground(Color.GRAY);
		showChatArea.setText("Select a Friend and Start Messaging");
		showChatAreaJsc = new JScrollPane(showChatArea);
		showChatAreaJsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(showChatAreaJsc);
		chatFrame.setVisible(true);
	}

	public void newMessageNotification(String newMsg) {
		String[] newMsgArr = newMsg.split(":");
		newMessageFromSet.add(newMsgArr[0]);
		userList.repaint();
		showChatTextPane = userShowChatMap.get(newMsgArr[0]);
		try {
			putNewMsgInTextPane(0, newMsg);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * set text alignment in TextPane
	 * 
	 * @param side 0 put text to left; 1 put text to right
	 * @throws BadLocationException
	 */
	public void putNewMsgInTextPane(int side, String msg) throws BadLocationException {
		StyledDocument doc = showChatTextPane.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);

		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		if (side == 0) {
			doc.insertString(doc.getLength(), "\r\n" + msg, left);
			doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		} else {
			doc.insertString(doc.getLength(), "\r\n" + msg, right);
			doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		}

	}

	public void setFriendList(ArrayList<String> userList) {
		this.userList.setListData(userList.toArray(new String[userList.size()]));
	}

	public static void main(String[] args) {
		new ClientUI();
	}
}
