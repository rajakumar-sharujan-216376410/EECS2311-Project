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
	static JMenuItem m1; // MenuItems
	static JFrame f;
	private Draw object;
	
	public static void main(String[] args) {
		f = new JFrame();
		mb = new JMenuBar();
		x = new JMenu("Menu"); 
		
		m1 = new JMenuItem("Insert");
		m1.addActionListener(m);
		x.add(m1);
		mb.add(x);
		f.setJMenuBar(mb);
		f.setSize(500, 500);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m1) {
			
		}
	}
	
}
