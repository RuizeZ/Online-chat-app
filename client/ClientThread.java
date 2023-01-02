package NetworkProgramming.client;

import javax.swing.JTextArea;

public class ClientThread implements Runnable {

	private Client client;
	private JTextArea showChatArea;
	private ClientUI clientUI;

	public ClientThread(Client client, JTextArea showChatArea, ClientUI clientUI) {
		super();
		this.client = client;
		this.showChatArea = showChatArea;
		this.clientUI = clientUI;
	}

	/**
	 * read message from the server, show the msg in the chat history
	 */
	@Override
	public void run() {
		// put into while, let it read from server all the time
		while (true) {
			try {
				String msg = client.readMsg(); // the response from server for validation of account and passport
				 if ("1".equals(msg)) { // login success
					// show the chat UI
					clientUI.chatUI();
				} else if ("0".equals(msg)) {// login fail
					System.out.println("account name and password do not match");
				} else {
					showChatArea.setText(showChatArea.getText() + "\r\n" + msg); // show message history
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
