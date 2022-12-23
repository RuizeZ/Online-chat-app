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
		chatUI(user);

		try {
			client.writeMsg(user); // client user name to server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * create the UI for user user: name of the user
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
		sendButton.addActionListener(new ActionListener() {

			/**
			 * after click send button, three things are going to happen 1. send msg to
			 * server 2. show msg in the showChatArea 3. clear msg in the chatOutMsgArea
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
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
		});
		chatFrame.add(sendButton);

		new Thread(new Runnable() {

			/**
			 * read message from the server, show the msg in the chat history
			 */
			@Override
			public void run() {
				// put into while, let it read from server all the time
				while (true) {
					try {
						String msg = client.readMsg();
						System.out.println(msg);
						showChatArea.setText(showChatArea.getText() + "\r\n" + msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		chatFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new ClientUI("3");
	}
}
