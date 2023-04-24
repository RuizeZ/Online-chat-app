package NetworkProgramming.client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements MsgHeader {
	InputStream is;
	OutputStream os;
	ObjectOutputStream oos;
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
			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);
			this.clientUI = clientUI;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int readHeader() throws Exception {
		int header = is.read();
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
			os.write(NEWMSG);// send the header of new msg
			String msgOutTo = clientUI.userList.getSelectedValue();
			os.write((msgOutTo + ":" + msg + "\r\n").getBytes());
		} else {
			os.write((msg + "\r\n").getBytes());
		}
	}

	public void writeImg() throws IOException {
		os.write(NEWIMG);// send the header of new msg
		String msgOutTo = clientUI.userList.getSelectedValue();
		os.write((msgOutTo + "\r\n").getBytes());
		BufferedImage bi = new BufferedImage(150, 250, 1);
		bi.getGraphics().drawImage(clientUI.image, 0, 0, null);
		int w = bi.getWidth();
		int h = bi.getHeight();
		System.out.println("w: " + w);
		System.out.println("h: " + h);
		List<List<Integer>> pixelList = new ArrayList<>();
		List<Integer> rowList = new ArrayList<>();
		for (int i = 0; i < h; i++) {
			rowList = new ArrayList<>();
			for (int j = 0; j < w; j++) {
				rowList.add(bi.getRGB(j, i));
			}
			pixelList.add(new ArrayList<>(rowList));
		}
		oos.writeObject(pixelList);

	}

	public void readImg() {
		List<List<Integer>> newPixelList = new ArrayList<>();
		try {
			newPixelList = (ArrayList<List<Integer>>) ois.readObject();
			int w = newPixelList.get(0).size();
			int h = newPixelList.size();
			System.out.println("w:" + w);
			System.out.println("h:" + h);

			clientUI.buffImage = new BufferedImage(w, h, 1);
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					clientUI.buffImage.setRGB(j, i, newPixelList.get(i).get(j));
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
