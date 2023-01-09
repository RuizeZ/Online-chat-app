package NetworkProgramming.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * verify user's account and password
 * 
 * @author imrui
 *
 */
public class UserManager {
	public static boolean longin(User user, String accountName, String password) {
		File file = new File("accountinfo.txt");
		if (file.exists()) {
			Scanner scan;
			try {
				scan = new Scanner(file);
				while (scan.hasNextLine()) {
					String currAccount = scan.nextLine();
					if (currAccount.startsWith(user.getAccountName())) {
						String[] arr = currAccount.split("<>");// [accountName, password]
						if (arr.length == 2 && arr[0].equals(user.getAccountName())
								&& arr[1].equals(user.getPassword())) {
							// send welcome message to client indicating the connection is successful
							scan.close();
							return true;
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// account and password do not match
		return false;
	}

	public static boolean register(String accountName, String password) {
		// store the new account and password into file
		File file = new File("accountinfo.txt");
		FileWriter fileWriter;
		Scanner scan;
		try {
			fileWriter = new FileWriter(file, true);
			scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String currAccount = scan.nextLine();
				if (currAccount.startsWith(accountName)) {
					String[] arr = currAccount.split("<>");// [accountName, password]
					if (arr.length == 2 && arr[0].equals(accountName)) {
						// send welcome message to client indicating the connection is successful
						scan.close();
						fileWriter.close();
						return false;
					}
				}
			}
			fileWriter.write(accountName + "<>" + password + "\r\n");
			fileWriter.close();
			scan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
