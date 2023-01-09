package NetworkProgramming.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements MsgHeader {
	InputStream is;
	OutputStream os;
	BufferedReader br;
	String accountName;

	/**
	 * connects with server, and receive server welcome message implements all API
	 * of client
	 * 
	 * @param host server IP
	 * @param port server port
	 * @param user client name
	 */
	public Client(String host, int port) {
		try {
			Socket socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String readMsg() throws Exception {
		return br.readLine();
	}

	public void writeMsg(String msg) throws Exception {
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
		writeMsg(accountName);
		writeMsg(password);
		this.accountName = accountName;
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
		writeMsg(accountName);
		writeMsg(password);
	}

}
