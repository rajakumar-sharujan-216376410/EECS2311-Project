package Venn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;

public class Menu extends main implements ActionListener {
	private static final long serialVersionUID = 1L;
	public JMenu createMenuMethod() {
		JMenu menuBar;
		menuBar = new JMenu();
		menuBar.add("File");
		return menuBar;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		e.getActionCommand();
	}
}
