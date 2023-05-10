package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class Connections implements Runnable, MsgHeader {
	Socket socket;
	private BufferedReader br;
	InputStream input;
	OutputStream output;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectInputStream ois;
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
		ois = server.ois;
		dis = server.dis;
		dos = server.dos;
		this.userMap = socketMap;
		this.user = user;
		this.server = server;

	}

	@Override
	public void run() {
		while (true) {
			String clientMsg;
			int header;
			try {
				header = readHeader();

				if (header == NEWMSG) {
					clientMsg = readMsg();
					System.out.println("clientMsg: " + clientMsg);
					String[] inputStrArr = clientMsg.split(":");
					if ("G".equals(inputStrArr[0])) {
						groupChat(inputStrArr[1]);
					} else {
						privateChat(clientMsg, inputStrArr);
					}
				} else if (header == NEWIMG) {
					String clientName = readMsg();
					User nextUser = userMap.get(clientName);
					OutputStream nextOutput = nextUser.getSocket().getOutputStream();
					processImg(nextUser, nextOutput);
					nextOutput.write((user.getAccountName() + "\r\n").getBytes());
				} else if (header == NEWVIDEOCHAT) {
					String clientName = readMsg();
					processVideoChat(clientName);
				}

			} catch (Exception e) {
				// client disconnected with server
				System.out.println(this.user.getAccountName() + " disconnected");
				userMap.remove(user.getAccountName());
				server.connectionsList.remove(this);
				server.updateAllFriendList();
				System.out.println("Number of online user: " + userMap.size());
				break;
			}
		}

	}

	/**
	 * process the video image
	 */
	private void processVideoChat(String clientName) {
		int width = 0;
		int height = 0;
		try {
			OutputStream nextOutput = userMap.get(clientName).getSocket().getOutputStream();
			DataOutputStream nextDos = new DataOutputStream(nextOutput);
			nextOutput.write(NEWVIDEOCHAT);
			nextOutput.write((user.getAccountName() + "\n\r").getBytes());
			while (width != -1 && height != -1) {
				width = input.read();
				nextOutput.write(width);
				height = input.read();
				nextOutput.write(height);
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						nextDos.writeInt(dis.readInt());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * read pixelList from client, and forward to another client
	 */
	private void processImg(User nextUser, OutputStream nextOutput) {

		List<List<Integer>> pixelList = new ArrayList<>();
		try {
			pixelList = (ArrayList<List<Integer>>) ois.readObject();
			System.out.println("h: " + pixelList.size());
			System.out.println("w: " + pixelList.get(0).size());
			nextOutput.write(NEWIMG);
			nextUser.connection.oos.writeObject(pixelList);

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public int readHeader() throws Exception {
		int header = input.read();
		return header;
	}

	// read from client
	public String readMsg() throws IOException {
		System.out.println(10);
		String str = br.readLine();
		System.out.println(11);
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
