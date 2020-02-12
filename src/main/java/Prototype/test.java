package Prototype;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

public class test {

	
	
	
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
	
	
	///////////
	
	static private final String newline = "\n";
	JButton saveButton;
	JTextArea log;
	JFileChooser fc;
	
	////////
	int returnVal = fc.showSaveDialog(MainFrame.this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile();
		// This is where a real application would save the file.
		log.append("Saving: " + file.getName() + "." + newline);
	} else {
		log.append("Save command cancelled by user." + newline);
	}
	log.setCaretPosition(log.getDocument().getLength());
}
