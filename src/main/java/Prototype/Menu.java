package Prototype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JFrame implements ActionListener {
	static Menu m = new Menu();
	static JMenuBar mb; // MenuBar
	static JMenu x; // JMenu
	static JMenuItem m1, m2, m3, m4; // MenuItems
	static JFrame f;
	private Draw object;

	public static void main(String[] args) {
		f = new JFrame();
		mb = new JMenuBar();
		x = new JMenu("Menu");

		m1 = new JMenuItem("New");
		m2 = new JMenuItem("Insert");
		m3 = new JMenuItem("Save");
		m4 = new JMenuItem("Save as");
		m1.addActionListener(m);
		m2.addActionListener(m);
		m3.addActionListener(m);
		m4.addActionListener(m);
		x.add(m1);
		x.add(m2);
		x.add(m3);
		x.add(m4);
		mb.add(x);
		f.setJMenuBar(mb);
		f.setSize(500, 500);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m1) {

		}
		if (e.getSource() == m2) {

		}
		if (e.getSource() == m3) {

		}
		if (e.getSource() == m4) {

		}

	}

}
