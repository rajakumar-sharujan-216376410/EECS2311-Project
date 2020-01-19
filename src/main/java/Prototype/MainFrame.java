package Prototype;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.*;

//Creates the frame for the application
public class MainFrame extends JFrame implements ActionListener {
	
	private JPanel panel;
	private JButton show;
	private JButton hide;
	private Draw object;
	
	public MainFrame(){
		//Creates empty JFrame window with the name 'Venn App'
		super("Venn App");
		
		//Sets the Layout to Border Layout style
		setLayout(new BorderLayout());
		
		//Creates panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		//Creates show & hide buttons
		show = new JButton("Show");
		hide = new JButton("Hide");
		
		//Adds the button click Listener
		show.addActionListener(this);
		hide.addActionListener(this);

		//Sets the settings for the window 
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//creates the new drawing
		object = new Draw();
		
		//adds the Components 
		panel.add(show);
		panel.add(hide);
		add(panel, BorderLayout.WEST);
		add(object, BorderLayout.CENTER);
		
		//Draws the two circles and hides the Venn diagram
		object.drawing();
		object.setVisible(false);
	}
	
	//This method is run once a button is clicked
	public void actionPerformed (ActionEvent e){
		//Action for Show Button
		if (e.getSource() == show){
			
			//Shows the drawing
			object.setVisible(true);
		}
		//Action for Hide button
		else if (e.getSource() == hide){
			
			//Hides the drawing 
			object.setVisible(false);
		}
	}
	
	
}
