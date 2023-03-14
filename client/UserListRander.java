package NetworkProgramming.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class UserListRander<E> implements ListCellRenderer<E> {
	public JLabel label, imageLabel;
	public JPanel userPanel;

	public UserListRander() {
		// TODO Auto-generated constructor stub
		userPanel = new JPanel();
		userPanel.setPreferredSize(new Dimension(100, 50));
		imageLabel = new JLabel(new ImageIcon("/profile_img.jpg"));
		imageLabel.setPreferredSize(new Dimension(40, 40));
		label = new JLabel();
		label.setOpaque(true);
		label.setFont(new Font(null, 0, 24));
		userPanel.add(imageLabel);
		userPanel.add(label);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
			boolean cellHasFocus) {
		User user = (User)value;
		label.setText(user.name);
		
		return null;
	}

}
