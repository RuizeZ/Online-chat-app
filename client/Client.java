package NetworkProgramming.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	InputStream is;
	OutputStream os;
	BufferedReader br;

	/**
	 * connects with server, and receive server welcome message
	 * 
	 * @param host server IP
	 * @param port server port
	 * @param user client name
	 */
	public Client(String host, int port, String user) {
		try {
			Socket socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String inputMsg = readMsg(); // read welcome message from server
			System.out.println("client " + user + " connected, " + inputMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String readMsg() throws Exception {
		return br.readLine();
	}

	public void writeMsg(String msg) throws Exception {
		os.write((msg + "\r\n").getBytes());
	}
}
