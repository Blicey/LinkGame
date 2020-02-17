package linkgame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class DisplayWindow {
	
	private JPanel gameArea;
	private MainGame game;
	private final Color[] colors = {Color.white, Color.red, Color.blue, Color.green, Color.orange,
			Color.pink, Color.yellow, Color.magenta, Color.cyan};
	
	public void addGame(MainGame game) {
		game.init();
		this.game = game;
	}
	
	public void createWindow() {
		
		JFrame frame = new JFrame("LinkGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponent(frame.getContentPane());
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	private void addComponent(Container pane) {
		JPanel infoPanel = new JPanel();
		JLabel label = new JLabel("LinkGame");
		infoPanel.add(label);
		
		gameArea = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D)g;
				// TODO Draw the blocks
				for(int x = 0; x < MainGame.Width; x++) {
					for(int y = 0; y < MainGame.Height; y++) {
						int[] block = new int[] { x, y };
						int c = game.color(block);
						if(c != 0) {
							g2d.setColor(colors[c]);
							g2d.fillRect(x * MainGame.Size, y * MainGame.Size, MainGame.Size, MainGame.Size);
							if(game.isSelected(block)) {
								g2d.setColor(Color.black);
								g2d.setStroke(new BasicStroke(5.0f));
								g2d.drawRect(x * MainGame.Size, y * MainGame.Size, MainGame.Size, MainGame.Size);
							}
							else {
								g2d.setColor(Color.black);
								g2d.setStroke(new BasicStroke(2.0f));
								g2d.drawRect(x * MainGame.Size, y * MainGame.Size, MainGame.Size, MainGame.Size);
							}
						}
					}
				}
			}
		};
		gameArea.setPreferredSize(new Dimension(MainGame.Width * MainGame.Size, MainGame.Height * MainGame.Size));
		gameArea.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int xc = e.getX();
				int yc = e.getY();
				int x = xc / MainGame.Size;
				int y = yc / MainGame.Size;
				if(x < 0) { x = 0; }
				if(x > MainGame.Width - 1) { x = MainGame.Width - 1; }
				if(y < 0) { y = 0; }
				if(y > MainGame.Height - 1) { y = MainGame.Height - 1; }
				System.out.printf("%d %d\n", x, y);
				int[] block = new int[] { x, y };
				game.select(block);
				gameArea.repaint();
			}
		});
		gameArea.setBackground(Color.white);
		
		JPanel ctrlPanel = new JPanel();
		JButton rst = new JButton("reset");
		rst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.paint();
				gameArea.repaint();
			}
		});
		ctrlPanel.add(rst);
		
		pane.add(infoPanel, BorderLayout.PAGE_START);
		pane.add(gameArea, BorderLayout.CENTER);
		pane.add(ctrlPanel, BorderLayout.PAGE_END);
	}
	
}
