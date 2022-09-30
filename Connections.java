package NetworkProgramming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class Connections implements Runnable {
	Socket socket;
	InputStream input;
	OutputStream output;
	Map<String, Socket> socketMap;
	String userName;

	public Connections(Socket socket, Map<String, Socket> socketMap, String userName) throws IOException {
		this.socket = socket;
		input = socket.getInputStream();
		output = socket.getOutputStream();
		this.socketMap = socketMap;
		this.userName = userName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String clientMsg;
			try {
				clientMsg = readMsg(input);
				System.out.println("clientMsg: " + clientMsg);
				String[] inputStrArr = clientMsg.split(":");
				if ("G".equals(inputStrArr[0])) {
					groupChat(inputStrArr[1]);
				} else {
					if (socketMap.containsKey(inputStrArr[0])) {
						OutputStream nextOutput = socketMap.get(inputStrArr[0]).getOutputStream();
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
		for (Map.Entry<String, Socket> entry : socketMap.entrySet()) {
			if (entry.getValue() != socket) {
				OutputStream nextOutput = entry.getValue().getOutputStream();
				writeMsg(nextOutput, clientMsg);
			}
		}
	}

	// read from client
	public String readMsg(InputStream input) throws IOException {
		System.out.println("server in read");
		StringBuilder inputBuilder = new StringBuilder();
		String inputStr = "";
		int c = 0;
		while ((c = input.read()) != 13) {
			inputBuilder.append((char) c);
		}
		c = input.read();
		inputStr = inputBuilder.toString();
		return inputStr;
	}

	public void writeMsg(OutputStream nextOutput, String msg) throws IOException {
		nextOutput.write((userName + ": " + msg + "\r\n").getBytes());
	}

}
