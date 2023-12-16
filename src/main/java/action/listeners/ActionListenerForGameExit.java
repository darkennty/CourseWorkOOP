package action.listeners;

import domain.CheckerPiecesColors;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForGameExit implements ActionListener {

    private final CheckersGame game;
    public ActionListenerForGameExit(CheckersGame game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CheckerPiecesColors[][] pieces = game.getPieces();
        Color color = game.getLastMoveColor();
        game.saveBoard(pieces, color);

        System.exit(0);
    }
}
