package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Server implements MsgHeader {
	InputStream is;
	OutputStream os;
	BufferedReader br;
	Map<String, User> userMap;
	List<Connections> connectionsList;
	Socket socket;
	ObjectOutputStream oos;
	Connections newConnections;

	public void createServer(int port) throws IOException {
		userMap = new HashMap<>();
		connectionsList = new ArrayList<>();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server Started at " + port);
		while (true) {
			socket = serverSocket.accept();
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			oos = new ObjectOutputStream(os);

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
		boolean result = UserManager.register(accountName, password);
		if (!result) {
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
		User user = new User(accountName, password, socket);
		boolean result = UserManager.longin(user, accountName, password);
		if (result && !userMap.containsKey(accountName)) {
			// send welcome message to client indicating the connection is successful
			System.out.println(accountName + " is online!");
			userMap.put(accountName, user); // store current client name and socket
			newConnections = new Connections(socket, userMap, user, Server.this);
			connectionsList.add(newConnections);
			writeMsg(LOGINHEADER);
			// create a new connection for this client
			new Thread(newConnections).start();
			updateAllFriendList();
			return true;
		}
		writeMsg(FAIL);
		return false;
	}

	/**
	 * loop through all client by Connections and send userMap list to all clients.
	 */
	public void updateAllFriendList() {
		for (Connections connections : connectionsList) {
			try {
				connections.output.write(FRIENDLIST);
				connections.oos.writeObject(new ArrayList<>(userMap.keySet()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void writeMsg(byte hearder) throws IOException {
		os.write(hearder);
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
