package NetworkProgramming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
	InputStream is;
	BufferedReader br;

	public void createServer(int port) throws IOException {
		Map<String, Socket> socketMap = new HashMap<>();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server Started at " + port);
		while (true) {
			Socket socket = serverSocket.accept();
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						writeMsg(socket.getOutputStream(), "Welcome\r\n"); // send welcome message to client indicating the connection is successful																		// successfully connected
						String userName = readMsg(socket.getInputStream()); // read client name
						socketMap.put(userName, socket); // store current client name and socket
						System.out.println(userName + " is online!");
						// create a new connection for this client
						new Thread(new Connections(socket, socketMap, userName)).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	// read from client
	public String readMsg(InputStream input) throws IOException {
		return br.readLine();
	}

	public void writeMsg(OutputStream nextOutput, String msg) throws IOException {
		nextOutput.write((msg).getBytes());
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
