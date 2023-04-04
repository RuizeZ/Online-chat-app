package NetworkProgramming.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class UserListRander<E> implements ListCellRenderer<E> {
	DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	ClientUI clientUI;

	public UserListRander(ClientUI clientUI) {
		this.clientUI = clientUI;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component c = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (clientUI.newMessageFromSet.contains((String) value)) {
			if (isSelected) {
				clientUI.newMessageFromSet.remove((String) value);
			} else {
				c.setBackground(new Color(255, 114, 118));
			}
		}
		return c;
	}
}
