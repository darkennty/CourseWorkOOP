package domain;

import actionListeners.ActionListenerForContinueGame;
import actionListeners.ActionListenerForExit;
import actionListeners.ActionListenerForStartGame;
import labels.PoddavkiLabel;
import panels.ImagePanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Menu extends JFrame {

    private final ImagePanel menuPanel;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public Menu() {
        super("Checkers Surrender");

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(480, 510);

        menuPanel = new ImagePanel();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
    }

    public void start() {

        PoddavkiLabel label = new PoddavkiLabel();

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.decode("#D2B48C"));
        menuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        menuPanel.setPreferredSize(new Dimension(480, 510));

        JButton startGame = new JButton("Начать новую игру");
        startGame.setOpaque(false);
        startGame.setBackground(Color.decode("#D2B48C"));
        startGame.setBounds(0,120, 120, 120);
        startGame.setFont(new Font("Arial", Font.PLAIN, 28));
        startGame.setForeground(Color.WHITE);
        startGame.setPreferredSize(new Dimension(240, 100));
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.setFocusable(false);
        startGame.setBorderPainted(false);
        Border startBorder = startGame.getBorder();
        Border marginTopTwenty = new EmptyBorder(60, 60, 60, 60);
        startGame.setBorder(new CompoundBorder(startBorder, marginTopTwenty));

        JButton continueGame = new JButton("Продолжить игру");
        continueGame.setOpaque(false);
        continueGame.setBackground(Color.decode("#D2B48C"));
        continueGame.setBounds(0,240, 120, 120);
        continueGame.setFont(new Font("Arial", Font.PLAIN, 28));
        continueGame.setForeground(Color.WHITE);
        continueGame.setPreferredSize(new Dimension(240, 120));
        continueGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueGame.setFocusable(false);
        continueGame.setBorderPainted(false);
        Border continueBorder = continueGame.getBorder();
        continueGame.setBorder(new CompoundBorder(continueBorder, marginTopTwenty));

        JButton exitGame = new JButton("Выйти из игры");
        exitGame.setOpaque(false);
        exitGame.setBackground(Color.decode("#D2B48C"));
        exitGame.setBounds(0,360, 120, 120);
        exitGame.setFont(new Font("Arial", Font.PLAIN, 28));
        exitGame.setForeground(Color.WHITE);
        exitGame.setPreferredSize(new Dimension(240, 120));
        exitGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitGame.setFocusable(false);
        exitGame.setBorderPainted(false);
        Border exitBorder = exitGame.getBorder();
        exitGame.setBorder(new CompoundBorder(exitBorder, marginTopTwenty));

        menuPanel.add(label);
        menuPanel.add(startGame);
        menuPanel.add(continueGame);
        menuPanel.add(exitGame);

        cardPanel.add(menuPanel, "menu");

        CheckersGame game = new CheckersGame(cardLayout, cardPanel);

        startGame.addActionListener(new ActionListenerForStartGame(game, cardLayout, cardPanel, "game"));
        exitGame.addActionListener(new ActionListenerForExit());
        continueGame.addActionListener(new ActionListenerForContinueGame(game, cardLayout, cardPanel, "game"));

        game.createBoard();

        JPanel gamePanel = game.getGamePanel();

        cardPanel.add(gamePanel, "game");

        this.add(cardPanel);
    }
}
