package NetworkProgramming.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Client implements MsgHeader {
	InputStream is;
	OutputStream os;
	ObjectInputStream ois;
	BufferedReader br;
	String accountName;
	Socket socket;
	ClientUI clientUI;
	ArrayList<String> userList;

	/**
	 * connects with server, and receive server welcome message implements all API
	 * of client
	 * 
	 * @param host server IP
	 * @param port server port
	 * @param user client name
	 */
	public Client(String host, int port, ClientUI clientUI) {
		try {
			socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			ois = new ObjectInputStream(is);
			this.clientUI = clientUI;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int readHeader() throws Exception {
		System.out.println("in readHeader");
		int header = is.read();
		System.out.println(header);
		return header;
	}

	public String readMsg() throws Exception {
		String readStr = br.readLine();
		System.out.println(readStr);
		return readStr;
	}

	/**
	 * send message to server
	 * 
	 * @param msg    message content
	 * @param msgOut true if msg is to another user
	 * @throws Exception
	 */
	public void writeMsg(String msg, boolean msgOut) throws Exception {
		if (msgOut) {
			String msgOutTo = clientUI.userList.getSelectedValue();
			os.write((msgOutTo + ":" + msg + "\r\n").getBytes());
		}
		System.out.println("send: " + msg);
		os.write((msg + "\r\n").getBytes());
		os.flush();
	}

	/**
	 * send the login header(1) to server; send account name and password to server
	 * 
	 * @param accountName
	 * @param password
	 * @throws Exception
	 */
	public void login(String accountName, String password) throws Exception {
		os.write(LOGINHEADER);// send the header of login msg
		writeMsg(accountName, false);
		writeMsg(password, false);
		this.accountName = accountName;
		int header = is.read();
		System.out.println(header);
		if (header == LOGINHEADER) {
			// start the client thread
			new Thread(new ClientThread(this, clientUI)).start();
		} else if (header == FAIL) {// login fail
			System.out.println("account name and password do not match");
		}
	}

	/**
	 * send the login header(2) to server; send account name and password to server
	 * 
	 * @param accountName
	 * @param password
	 * @throws Exception
	 */
	public void register(String accountName, String password) throws Exception {
		System.out.println("registing...");
		os.write(REGISTERHEADER);// send the header of register msg
		writeMsg(accountName, false);
		writeMsg(password, false);
	}

	/**
	 * receive friend list from server and remove current client from the list, and
	 * display the list in UI
	 */
	public void updateFriendList() {

		try {
			userList = (ArrayList<String>) ois.readObject();
			userList.remove(accountName);
			clientUI.setFriendList(userList);
			clientUI.updateShowChatAreaToMap();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
