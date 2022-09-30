package NetworkProgramming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
	public void createServer(int port) throws IOException {
		Map<String, Socket> socketMap = new HashMap<>();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server Started at " + port);
		while (true) {
			Socket socket = serverSocket.accept();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						writeMsg(socket.getOutputStream(), "Who are you?\r\n");
						String userName = readMsg(socket.getInputStream());
						socketMap.put(userName, socket);
						System.out.println(userName + " is online!");
						OutputStream output = socket.getOutputStream();
						String sendMsg = "Connection Established with Server\r\n";
						output.write(sendMsg.getBytes());
						writeMsg(socket.getOutputStream(), "Welcome, " + userName + "\r\n");
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
