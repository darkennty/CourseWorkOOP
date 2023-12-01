package actionListeners;

import domain.CheckersGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForContinueGame implements ActionListener {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final String panelNameToSwitchTo;
    private final CheckersGame game;

    public ActionListenerForContinueGame(CheckersGame game, CardLayout cardLayout, JPanel container, String panelNameToSwitchTo) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.panelNameToSwitchTo = panelNameToSwitchTo;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.loadBoard();
        cardLayout.show(container, panelNameToSwitchTo);
    }
}
