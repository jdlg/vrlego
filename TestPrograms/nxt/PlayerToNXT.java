package nxt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlayerToNXT extends JFrame implements KeyListener {

	private int x = 0, y = 0, topvalue = 511, squareWidth = 255;
	private boolean u = false, d = false, l = false, r = false;
	private InstructionsSender sender;

	public PlayerToNXT() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlsPanel cp = new controlsPanel();
		cp.setPreferredSize(new Dimension(squareWidth + 20, squareWidth + 20));
		setContentPane(cp);
		addKeyListener(this);
		sender = new InstructionsSender("usb://");
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				sender.close();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}

	public static void main(String[] args) {
		PlayerToNXT frame = new PlayerToNXT();
		frame.setVisible(true);
		frame.pack();
	}

	class controlsPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int c = 10 + squareWidth / 2;

			g.setColor(Color.gray);
			g.drawRect(10, 10, squareWidth, squareWidth);

			g.drawLine(c - 10, c, c + 10, c);
			g.drawLine(c, c - 10, c, c + 10);

			g.setColor(Color.black);
			g.fillOval(c + x / 4 - 5, c + y / 4 - 5, 10, 10);
		}
	}

	private void utdateXY() {
		x = 0;
		y = 0;
		if (u)
			y -= topvalue;
		if (d)
			y += topvalue;
		if (l)
			x -= topvalue;
		if (r)
			x += topvalue;
		repaint();
		sender.sendInstruction(x, y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			sender.close();
			System.exit(0);
		} else {
			if (code == KeyEvent.VK_UP)
				u = true;
			if (code == KeyEvent.VK_DOWN)
				d = true;
			if (code == KeyEvent.VK_LEFT)
				l = true;
			if (code == KeyEvent.VK_RIGHT)
				r = true;
			utdateXY();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code != KeyEvent.VK_ESCAPE) {
			if (code == KeyEvent.VK_UP)
				u = false;
			if (code == KeyEvent.VK_DOWN)
				d = false;
			if (code == KeyEvent.VK_LEFT)
				l = false;
			if (code == KeyEvent.VK_RIGHT)
				r = false;
			utdateXY();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
