package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.*;

import model.*;

public class Game2048View extends JFrame {
	
	private Game2048 game;
	
	public Game2048View() {
		super("2048");
		
		game = new Game2048();
		initGUI();
		
		this.setResizable(false);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	private void initGUI() {
		setSize(450, 600);
		
		BoardView boardView = new BoardView(game.getBoard());
		add(boardView);
	}
}
