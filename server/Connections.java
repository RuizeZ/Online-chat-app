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
	Map<String, Socket> socketMap;
	String userName;

	public Connections(Socket socket, Map<String, Socket> socketMap, String userName) throws IOException {
		this.socket = socket;
		input = socket.getInputStream();
		output = socket.getOutputStream();
		br = new BufferedReader(new InputStreamReader(input));
		this.socketMap = socketMap;
		this.userName = userName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String clientMsg;
			try {
				clientMsg = readMsg();
				System.out.println("clientMsg: " + clientMsg);
				String[] inputStrArr = clientMsg.split(":");
				if ("G".equals(inputStrArr[0])) {
					groupChat(inputStrArr[1]);
				} else {
					if (socketMap.containsKey(inputStrArr[0])) {
						OutputStream nextOutput = socketMap.get(inputStrArr[0]).getOutputStream();
						System.out.println("in private chat");
						writeMsg(nextOutput, inputStrArr[1]);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void groupChat(String clientMsg) throws IOException {
		// group chat
		System.out.println("in group chat");
		for (Map.Entry<String, Socket> entry : socketMap.entrySet()) {
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
		nextOutput.write((userName + ": " + msg + "\r\n").getBytes());
	}

}
