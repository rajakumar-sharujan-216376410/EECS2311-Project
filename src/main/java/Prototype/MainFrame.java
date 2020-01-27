package Prototype;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

import java.awt.event.*;

//Creates the frame for the application
	
public class MainFrame extends JFrame implements ActionListener {
	
	private JPanel panel;
	private JButton addInputs;
	private JButton hide;
	private JButton createDiagram;
	private JButton openFile;
	private Draw object;
	protected int counter = 0;
	static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
    
	
	public MainFrame() {
		//Creates empty JFrame window with the name 'Venn App'
		super("Venn App");

		//Sets the Layout to Border Layout style
		setLayout(new BorderLayout());
		
		//Creates panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		//Creates show & hide buttons
		addInputs = new JButton("Add inputs");
		hide = new JButton("Hide");
		createDiagram = new JButton("Create the diagram");
		
		//Creates open file button
		openFile = new JButton("Open");
		
		//Adds the button click Listener
		addInputs.addActionListener(this);
		hide.addActionListener(this);
		createDiagram.addActionListener(this); 
		openFile.addActionListener(this);
		
		//Setting for opening files
		
		log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        fc = new JFileChooser();

		//Sets the settings for the window 
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//creates the new drawing
		object = new Draw();
		
		//adds the Components 
		panel.add(addInputs);
		panel.add(hide);
		panel.add(createDiagram);
		panel.add(openFile);
		add(panel, BorderLayout.WEST);
		add(object, BorderLayout.CENTER);
		
		//Draws the two circles and hides the Venn diagram
		object.drawing();
		object.setVisible(false);
		
		JTextField text = new JTextField(6);
		text.setText("Similarities");
		text.moveCaretPosition(10); 
		object.add(text);
		
		object.addMenu(this); 
	}
	
	//This method is run once a button is clicked
	public void actionPerformed (ActionEvent e) {
		//Action for Show Button
		if (e.getSource() == addInputs){
			
			//Shows the drawing
			counter++;
			object.setVisible(true);
			object.addInput();		
			
		}
		//Action for Hide button
		else if (e.getSource() == hide) {
			
			//Hides the drawing 
			object.setVisible(false);
		}
		else if (e.getSource() == createDiagram) {
			
			object.drawing();
		}
		else if(e.getSource() == openFile) {
			int returnVal = fc.showOpenDialog(MainFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
		}
	}
}

