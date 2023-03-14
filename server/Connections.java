package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class Connections implements Runnable {
	Socket socket;
	private BufferedReader br;
	InputStream input;
	OutputStream output;
	Map<User, Socket> socketMap;
	User user;

	public Connections(Socket socket, Map<User, Socket> socketMap, User user) throws IOException {
		this.socket = socket;
		input = socket.getInputStream();
		output = socket.getOutputStream();
		br = new BufferedReader(new InputStreamReader(input));
		this.socketMap = socketMap;
		this.user = user;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String clientMsg;
			try {
				clientMsg = readMsg();
			} catch (IOException e) {
				// client disconnected with server
				System.out.println(this.user.getAccountName() + " disconnected");
				socketMap.remove(this.user);
				System.out.println("Number of online user: " + socketMap.size());
				break;
			}
			try {
				System.out.println("clientMsg: " + clientMsg);
				String[] inputStrArr = clientMsg.split(":");
				if ("G".equals(inputStrArr[0])) {
					groupChat(inputStrArr[1]);
				} else {
					privateChar(clientMsg, inputStrArr);
				}
			} catch (IOException e) {
				// client disconnected with server
				e.printStackTrace();
			}
		}

	}

	private void privateChar(String clientMsg, String[] inputStrArr) throws IOException {
		for (User user : socketMap.keySet()) {
			if (user.getAccountName().equals(inputStrArr[0])) {
				OutputStream nextOutput = socketMap.get(user).getOutputStream();
				writeMsg(nextOutput, inputStrArr[1]);
			}
		}
	}

	public void groupChat(String clientMsg) throws IOException {
		// group chat
		System.out.println("in group chat");
		for (Map.Entry<User, Socket> entry : socketMap.entrySet()) {
			if (entry.getValue() != socket) {
				OutputStream nextOutput = entry.getValue().getOutputStream();
				writeMsg(nextOutput, clientMsg);
			}
		}
	}

	// read from client
	public String readMsg() throws IOException {
		String str = br.readLine();
		return str;
	}

	public void writeMsg(OutputStream nextOutput, String msg) throws IOException {
		nextOutput.write((user.getAccountName() + ": " + msg + "\r\n").getBytes());
	}

}
