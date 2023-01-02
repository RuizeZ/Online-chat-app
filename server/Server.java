package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
	private Map<String, Socket> socketMap;
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
							e.printStackTrace();
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
		System.out.println("registing...");
		String accountName = readMsg(); // read client accountName
		String password = readMsg(); // read client password
		// store the new account and password into file
		File file = new File("accountinfo.txt");
		FileWriter fileWriter = new FileWriter(file, true);
		fileWriter.write(accountName + "<>" + password + "\r\n");
		fileWriter.close();
	}

	/**
	 * client login, check the account and password in the "accountinfo.txt"
	 * 
	 * @throws IOException
	 */
	private boolean login() throws IOException {
		String accountName = readMsg(); // read client accountName
		String password = readMsg(); // read client password
		File file = new File("accountinfo.txt");
		if (file.exists()) {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String currAccount = scan.nextLine();
				System.out.println(currAccount);
				if (currAccount.startsWith(accountName)) {
					String[] arr = currAccount.split("<>");// [accountName, password]
					for (String string : arr) {
						System.out.println(string);
					}
					if (arr.length == 2 && arr[1].equals(password)) {
						// send welcome message to client indicating the connection is successful
						System.out.println(accountName + " is online!");
						writeMsg("1");
						socketMap.put(accountName, socket); // store current client name and socket
						// create a new connection for this client
						new Thread(new Connections(socket, socketMap, accountName)).start();
						return true;
					}
				}
			}
		}

		// account and password do not match
		writeMsg("0");
		return false;
	}

	// read from client
	public String readMsg() throws IOException {
		return br.readLine();
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
