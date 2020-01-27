package Prototype;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) {
		
		//Runs the application
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();
			
			}
		});

	}

}

//test
