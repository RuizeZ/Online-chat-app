package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class Connections implements Runnable, MsgHeader {
	Socket socket;
	private BufferedReader br;
	InputStream input;
	OutputStream output;
	ObjectOutputStream oos;
	Map<String, User> userMap;
	User user;
	Server server;

	public Connections(Socket socket, Map<String, User> socketMap, User user, Server server) throws IOException {
		this.socket = socket;
		input = server.is;
		output = server.os;
		br = server.br;
		oos = server.oos;
		this.userMap = socketMap;
		this.user = user;
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			String clientMsg;
			try {
				clientMsg = readMsg();
			} catch (IOException e) {
				// client disconnected with server
				System.out.println(this.user.getAccountName() + " disconnected");
				userMap.remove(user.getAccountName());
				server.connectionsList.remove(this);
				server.updateAllFriendList();
				System.out.println("Number of online user: " + userMap.size());
				break;
			}
			try {
				System.out.println("clientMsg: " + clientMsg);
				String[] inputStrArr = clientMsg.split(":");
				if ("G".equals(inputStrArr[0])) {
					groupChat(inputStrArr[1]);
				} else {
					privateChat(clientMsg, inputStrArr);
				}
			} catch (IOException e) {
				// client disconnected with server
				e.printStackTrace();
			}
		}

	}

	private void privateChat(String clientMsg, String[] inputStrArr) throws IOException {
		for (String accountName : userMap.keySet()) {
			if (accountName.equals(inputStrArr[0])) {
				OutputStream nextOutput = userMap.get(accountName).getSocket().getOutputStream();
				writeMsg(nextOutput, inputStrArr[1]);
			}
		}
	}

	public void groupChat(String clientMsg) throws IOException {
		// group chat
		for (Map.Entry<String, User> entry : userMap.entrySet()) {
			if (entry.getKey() != user.getAccountName()) {
				OutputStream nextOutput = entry.getValue().getSocket().getOutputStream();
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
		nextOutput.write(NEWMSG);
		byte[] arr = (user.getAccountName() + ": " + msg + "\r\n").getBytes();
		nextOutput.write(arr);
	}

	public void writeMsg(byte hearder) throws IOException {
		output.write(hearder);
	}
}
