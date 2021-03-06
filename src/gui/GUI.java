package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import gui.mode.HammingCodeGUI;
import gui.mode.MNISTGUI;
import gui.mode.ModeGUI;
import gui.mode.VierBitXorGUI;
import gui.mode.XORGUI;

public class GUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private Thread t;
	private String threadName;
	public ModeGUI currGui;
	public static void main(String[] args) {
		GUI g = new GUI();
	}

	public GUI() {
		this.threadName = "FabiMl-Interface";
		start_gui();
	}

	public void start_gui() {
		if (this.t == null) {
			t = new Thread(this, this.threadName);
			t.start();
		}
	}

	@Override
	public void run() {
		GUI outer = this;
		setTitle("FabiMl-Interface");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		GraphicsDevice gd2 = null;
		if (gs.length > 1) {
			JFrame dummy = new JFrame(gs[1].getDefaultConfiguration());
			setLocationRelativeTo(dummy);
			dummy.dispose();
			gd2 = gs[1];
		} else {
			JFrame dummy = new JFrame(gs[0].getDefaultConfiguration());
			setLocationRelativeTo(dummy);
			dummy.dispose();
			gd2 = gs[0];
		}
		final GraphicsDevice gd = gd2;
		getContentPane().setLayout(new BorderLayout());
		JMenuBar jmb = new JMenuBar();
		JMenu men = new JMenu("Training");

		JMenu tests = new JMenu("Backpropagation");
		JMenuItem hammingtest = new JMenuItem("HammingCode");
		hammingtest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!outer.switch_allowed()){
					return;
				}
				// Gr�sse des Bildschirms bestimmen
				getContentPane().removeAll();
				int width = gd.getDisplayMode().getWidth();
				int height = gd.getDisplayMode().getHeight();

				HammingCodeGUI g = new HammingCodeGUI((int) (width * 0.8), height, (int) (width * 0.2), height);
				outer.currGui= g;
				getContentPane().add(g.centerPane, BorderLayout.CENTER);
				getContentPane().add(g.eastPane, BorderLayout.EAST);
				pack();
				repaint();
			}

		});
		JMenuItem vierbitxor = new JMenuItem("VierBitXor");
		vierbitxor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!outer.switch_allowed()){
					return;
				}
				// Gr�sse des Bildschirms bestimmen
				getContentPane().removeAll();
				int width = gd.getDisplayMode().getWidth();
				int height = gd.getDisplayMode().getHeight();
				VierBitXorGUI g = new VierBitXorGUI((int) (width * 0.8), height, (int) (width * 0.2), height);
				outer.currGui= g;
				getContentPane().add(g.centerPane, BorderLayout.CENTER);
				getContentPane().add(g.eastPane, BorderLayout.EAST);
				pack();
				repaint();
			}

		});
		JMenuItem xor = new JMenuItem("XOR");
		xor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!outer.switch_allowed()){
					return;
				}
				getContentPane().removeAll();
				int width = gd.getDisplayMode().getWidth();
				int height = gd.getDisplayMode().getHeight();
				XORGUI g = new XORGUI((int) (width * 0.8), height, (int) (width * 0.2), height);
				outer.currGui= g;
				getContentPane().add(g.centerPane, BorderLayout.CENTER);
				getContentPane().add(g.eastPane, BorderLayout.EAST);
				pack();
				repaint();
			}

		});
		JMenuItem mnist = new JMenuItem("MNIST");
		mnist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!outer.switch_allowed()){
					return;
				}
				getContentPane().removeAll();
				int width = gd.getDisplayMode().getWidth();
				int height = gd.getDisplayMode().getHeight();
				MNISTGUI g = new MNISTGUI((int) (width * 0.8), height, (int) (width * 0.2), height);
				outer.currGui= g;
				getContentPane().add(g.centerPane, BorderLayout.CENTER);
				getContentPane().add(g.eastPane, BorderLayout.EAST);
				pack();
				repaint();

			}

		});
		tests.add(hammingtest);
		tests.add(vierbitxor);
		tests.add(xor);
		tests.add(mnist);
		men.add(tests);
		JMenu neat = new JMenu("NEAT");
		JMenuItem neat_xor = new JMenuItem("XOR");
		neat_xor.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!outer.switch_allowed()){
					return;
				}
				ModeGUI g = null;
				outer.currGui= g;
			}
			
		});
		neat.add(neat_xor);
		men.add(neat);
		jmb.add(men);
		setJMenuBar(jmb);

		toFront();
		setVisible(true);
	}
	public boolean switch_allowed(){
		if(this.currGui==null){
			return true;
		}
		return !this.currGui.getManager().inTraining;
	}
}

class GraphContentPane extends JPanel {

	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.YELLOW);
	}
}

class ButtonPane extends JPanel {

	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.GREEN);
	}

}
