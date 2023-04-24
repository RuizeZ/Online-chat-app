package NetworkProgramming.server;

import java.net.Socket;

/**
 * User Class contains all user's information
 * 
 * @author imrui
 *
 */
public class User {
	private String accountName, password;
	private Socket socket;
	Connections connection;

	public User(String accountName, String password, Socket socket) {
		super();
		this.accountName = accountName;
		this.password = password;
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
