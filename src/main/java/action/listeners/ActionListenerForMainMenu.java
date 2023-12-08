package action.listeners;

import domain.CheckerPiecesColors;
import domain.CheckersGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForMainMenu implements ActionListener {

    private final CardLayout cardLayout;
    private final Container container;
    private final String panelNameToSwitchTo;
    private final CheckersGame game;

    public ActionListenerForMainMenu(CardLayout cardLayout, Container container, String panelNameToSwitchTo, CheckersGame game) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.panelNameToSwitchTo = panelNameToSwitchTo;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CheckerPiecesColors[][] pieces = game.getPieces();
        Color color = game.getLastMoveColor();
        game.saveBoard(pieces, color);

        cardLayout.show(container, panelNameToSwitchTo);
    }
}
