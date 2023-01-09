package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements MsgHeader {
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	private Map<User, Socket> socketMap;
	private Socket socket;

	public void createServer(int port) throws IOException {
		socketMap = new HashMap<>();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server Started at " + port);
		while (true) {
			socket = serverSocket.accept();
			System.out.println(socket);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							// read msg header: login: 1; register: 2
							int msgHeader = is.read();
							boolean isLogin = false;
							switch (msgHeader) {
							case LOGINHEADER:
								isLogin = login();
								break;
							case REGISTERHEADER:
								register();
								break;
							}
							if (isLogin) {
								break;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							break;
						}
					}
				}
			}).start();
		}
	}

	/**
	 * client register a new account
	 * 
	 * @throws IOException
	 */
	private void register() throws IOException {
		String accountName = br.readLine(); // read client accountName
		String password = br.readLine(); // read client password
		User user = new User(accountName, password);
		boolean result = UserManager.register(accountName, password);
		if(!result) {
			System.out.println("account exist");
		}
	}

	/**
	 * client login, check the account and password in the "accountinfo.txt"
	 * 
	 * @throws IOException
	 */
	private boolean login() throws IOException {
		String accountName = br.readLine(); // read client accountName
		String password = br.readLine(); // read client password
		// create a User object
		User user = new User(accountName, password);
		boolean result = UserManager.longin(user, accountName, password);
		if (result) {
			// send welcome message to client indicating the connection is successful
			System.out.println(accountName + " is online!");
			writeMsg("1");
			socketMap.put(user, socket); // store current client name and socket
			// create a new connection for this client
			new Thread(new Connections(socket, socketMap, user)).start();
			return true;
		}
		writeMsg("0");
		return false;
	}

	public void writeMsg(String msg) throws IOException {
		os.write((msg + "\r\n").getBytes());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Server().createServer(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
