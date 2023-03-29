package NetworkProgramming.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JTextArea;

public class ClientThread implements Runnable, MsgHeader {

	private Client client;
	private JTextArea showChatArea;
	private ClientUI clientUI;

	public ClientThread(Client client, ClientUI clientUI) {
		super();
		this.client = client;
		this.clientUI = clientUI;
		clientUI.chatUI(client.accountName);
	}

	/**
	 * read message from the server, show the msg in the chat history
	 */
	@Override
	public void run() {
		// put into while, let it read from server all the time
		while (true) {
			int header;
			try {
				header = client.readHeader(); // the response header from server
			} catch (Exception e) {
				System.out.println("lost connection with server");
				break;
			}
			try {
				if (header == FRIENDLIST) {
					client.updateFriendList();
				} else if (header == NEWMSG) { // receive msg
					System.out.println("newMessage");
					clientUI.setChatArea(client.readMsg()); // show message history
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
