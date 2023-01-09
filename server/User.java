package NetworkProgramming.server;

/**
 * User Class contains all user's information
 * 
 * @author imrui
 *
 */
public class User {
	private String accountName, password;

	public User(String accountName, String password) {
		super();
		this.accountName = accountName;
		this.password = password;
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
