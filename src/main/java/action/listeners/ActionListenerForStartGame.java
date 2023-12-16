package action.listeners;

import database.MyDataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForStartGame implements ActionListener {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final String panelNameToSwitchTo;
    private final MyDataBase db = MyDataBase.getInstance();
    private final CheckersGame game;

    public ActionListenerForStartGame(CheckersGame game, CardLayout cardLayout, JPanel container, String panelNameToSwitchTo) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.panelNameToSwitchTo = panelNameToSwitchTo;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        db.deleteCheckers();
        db.deleteLastMove();
        game.updateMove(Color.BLACK);
        game.initializeBoard();
        game.setSelectedButtonNull();
        cardLayout.show(container, panelNameToSwitchTo);
    }
}
