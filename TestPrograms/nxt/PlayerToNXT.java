package nxt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Joystick;
import game.Player;

public class PlayerToNXT extends JFrame implements KeyListener {

	private static int x = 0, y = 0;
	private static boolean u = false, d = false, l = false, r = false;
	private static SendInstructions sender;
//	private static Joystick stick = new Joystick();

	public PlayerToNXT() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new controlsPanel());
		addKeyListener(this);
		setPreferredSize(new Dimension(100, 120));
		sender = new SendInstructions("usb://");
	}

	public static void main(String[] args) {
		PlayerToNXT frame = new PlayerToNXT();
		frame.setVisible(true);
		frame.pack();
//		start();
	}

	private static void start() {
		while (true) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!u && !d && !l && !r) {
//				x=(int)(stick.getX()*31);
//				y=(int)(stick.getY()*31);
			}
			sender.sendInstruction(x, y);
		}
	}

	class controlsPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// System.out.println(x + " " + y);
			g.drawRect(10, 10, 63, 63);
			g.fillOval(10 + 31 + x - 5, 10 + 31 - y - 5, 10, 10);
		}
	}

	private void utdateXY() {
		x = 0;
		y = 0;
		if (u)
			y -= 31;
		if (d)
			y += 31;
		if (l)
			x -= 31;
		if (r)
			x += 31;
		repaint();
		sender.sendInstruction(x, y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
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
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub


	}
}
