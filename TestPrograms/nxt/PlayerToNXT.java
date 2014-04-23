package nxt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Joystick;

public class PlayerToNXT extends JFrame implements KeyListener {

	private static int x = 0, y = 0, topvalue = 31;
	private static boolean u = false, d = false, l = false, r = false;
	private static InstructionsSender sender;

	public PlayerToNXT() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new controlsPanel());
		addKeyListener(this);
		setPreferredSize(new Dimension(100, 120));
		sender = new InstructionsSender("usb://");
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
			g.drawRect(10, 10, 63, 63);
			g.fillOval(10 + 31 + x - 5, 10 + 31 - y - 5, 10, 10);
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
