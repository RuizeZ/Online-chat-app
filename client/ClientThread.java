package NetworkProgramming.client;

import javax.swing.JTextArea;

public class ClientThread implements Runnable {
	private Client client;
	private JTextArea showChatArea;

	/**
	 * read message from the server, show the msg in the chat history
	 */
	@Override
	public void run() {
		// put into while, let it read from server all the time
		while (true) {
			try {
				String msg = client.readMsg();
				switch (msg) {
				case "1":
					// show char UI
					break;
				case "0":
					break;
				}
				showChatArea.setText(showChatArea.getText() + "\r\n" + msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
