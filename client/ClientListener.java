package NetworkProgramming.client;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import com.github.sarxos.webcam.Webcam;

/**
 * implements all client listener
 * 
 * @author imrui
 *
 */
public class ClientListener implements ActionListener, ListSelectionListener {
	private JTextArea chatOutMsgArea;
	private JTextField accountField, passwordField;
	private Client client;
	private ClientUI clientUI;

	public ClientListener(ClientUI clientUI, Client client) {
		super();
		this.clientUI = clientUI;
		this.chatOutMsgArea = clientUI.chatOutMsgArea;
		this.accountField = clientUI.accountField;
		this.passwordField = clientUI.passwordField;
		this.client = client;
	}

	/**
	 * actions for each button 1. "Login": if account and password are correct, show
	 * the chat UI 2. "New account": create a new account 3. "send": send msg
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		try {
			switch (str) {
			case "Login":
				login();
				break;
			case "New account":
				register();
				break;
			case "send":
				send();
				break;
			case "image":
				BufferedImage newBuffImage = selectImage();
				clientUI.buffImage = newBuffImage;
				clientUI.putImageInTextPaneRight();

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							client.writeImg();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();

				break;
			case "Video Chat":
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showVideo();
					}
				}).start();

			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	private void showVideo() {
		try {
			client.os.write(7);
			client.os.write((client.getMsgOutTo() + "\r\n").getBytes());
			JFrame videoFrame = new JFrame("Video");
			videoFrame.setSize(640, 360);
			videoFrame.setLocationRelativeTo(null);
			videoFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			videoFrame.setLayout(new FlowLayout());
			videoFrame.setVisible(true);
			Graphics g = videoFrame.getGraphics();
			BufferedImage bufferedImage;
			Webcam webcam = Webcam.getDefault();
			webcam.open();
			while (true) {
			bufferedImage = webcam.getImage();
			g.drawImage(bufferedImage, 0, 0, 640, 360, videoFrame);
			int width = bufferedImage.getWidth();
			client.os.write(width);
			int height = bufferedImage.getHeight();
			client.os.write(height);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					client.dos.writeInt(bufferedImage.getRGB(j, i));
				}
			}
			if (!videoFrame.isShowing()) {
				client.os.write(-1);
				break;
			}
		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * open file dialog
	 */
	private BufferedImage selectImage() {
		String imagePath = System.getProperty("user.dir") + "/image/user_image";
		JFileChooser fileChooser = new JFileChooser(imagePath);
		int rVal = fileChooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				// read image and get pixel of the image.
				BufferedImage buffImage = ImageIO.read(file);
				return buffImage;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * call client.register() to create a new account
	 * 
	 * @throws Exception
	 */
	private void register() throws Exception {
		client.register(accountField.getText(), passwordField.getText());

	}

	/**
	 * call client.login() to validate the account and password
	 * 
	 * @throws Exception
	 */
	private void login() throws Exception {
		client.login(accountField.getText(), passwordField.getText());

	}

	/**
	 * after click send button, three things are going to happen 1. send msg to
	 * server 2. show msg in the showChatArea 3. clear msg in the chatOutMsgArea
	 */
	private void send() {
		// 1. send msg to server
		// get text from the chatOutMsgArea
		String msg = chatOutMsgArea.getText();
		// 2. show msg in the showChatArea
		try {
			clientUI.putNewMsgInTextPane(1, msg);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		clientUI.showChatArea.setText(clientUI.showChatArea.getText() + "\r\n" + msg);
		try {
			client.writeMsg(msg, true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 3. clear msg in the chatOutMsgArea
		chatOutMsgArea.setText("");
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		String name = clientUI.userList.getSelectedValue();
		System.out.println("select: " + name);
		if (name != null) {
			clientUI.changeShowChatArea(name);
		}
	}

}
