package view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.*;

public class VersusGame2048View extends JFrame {

	private Game2048 game;
	
	public VersusGame2048View() {
		super("2048");

		initVersusGUI();
		this.setResizable(false);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	private void initVersusGUI() {
		setSize(900, 600);
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 2));
		BoardView boardView1 = new BoardView((new Game2048()).getBoard(), true);
		BoardView boardView2 = new BoardView((new Game2048()).getBoard());
		container.add(boardView1);
		container.add(boardView2);
		
		this.add(container);

	}
}
