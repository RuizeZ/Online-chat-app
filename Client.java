package NetworkProgramming;

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

	public Client(String host, int port) {
		try {
			Socket socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br = new BufferedReader(new InputStreamReader(is));
	}

	public String readMsg() throws Exception {
		return br.readLine();
	}

	public void writeMsg(String msg) throws Exception {
		os.write(msg.getBytes());
	}

	public static void main(String[] args) {
		Client client = new Client("10.26.46.32", 8080);
		// read successful connect msg from server
		try {
			System.out.println("Client established");
			String msg = client.readMsg();
			System.out.println(msg);
			Scanner scan = new Scanner(System.in);
			String userName = scan.nextLine() + "\r\n";
			client.writeMsg(userName);
			// read from server thread
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						try {
							String msg = client.readMsg();
							System.out.println(msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

			// write to server
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							String msg = scan.nextLine();
							client.writeMsg(msg + "\r\n");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
