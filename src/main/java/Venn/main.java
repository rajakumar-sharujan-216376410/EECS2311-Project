package Venn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class main extends JFrame {
	private static final long serialVersionUID = 1L;

	public static int r = 50;

	private int x, y, cX, cY;

	private MouseHandler mh;

	boolean isCircleClicked = false;

	public static void main(String[] args) {
		main c1 = new main();
		c1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public main() {

		super("Venn Diagram");

		cX = r + 150;
		cY = r + 150;

		mh = new MouseHandler();
		addMouseListener(mh);
		addMouseMotionListener(mh);

		setSize(400, 400);
		setVisible(true);

	}

	public void paint(Graphics g) {
		super.paint(g);
<<<<<<< HEAD
		g.setColor(Color.gray);
=======
		g.setColor(Color.BLACK);
>>>>>>> refs/remotes/origin/AhmedB2
		g.drawOval(cX - r, cY - r, r * 2, r * 2);
	}

	private class MouseHandler extends MouseAdapter implements MouseMotionListener {
		public void mousePressed(MouseEvent me) {
		    isCircleClicked = (cX + me.getX()) * (cX + me.getX()) + (cY + me.getY()) * (cY + me.getY()) > r * r;
		}

		public void mouseDragged(MouseEvent me) {
			if (isCircleClicked) {

				x = me.getX();
				y = me.getY();
				cX = x;
				cY = y;
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			isCircleClicked = false;
		}

	}

}