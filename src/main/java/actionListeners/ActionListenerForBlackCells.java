/*package actionListeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static consts.Consts.SIZE;

public class ActionListenerForBlackCells implements ActionListener {

    private JButton[][] board;

    private CheckerPiecesColors[][] pieces;

    private JButton selectedButton;

    public ActionListenerForBlackCells(JButton[][] board, CheckerPiecesColors[][] pieces, JButton selectedButton) {
        this.board = board;
        this.pieces = pieces;
        this.selectedButton = selectedButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();

        if (selectedButton == null) {
            selectedButton = buttonClicked;
        } else {
            int fromRow = findRow(selectedButton);
            int fromCol = findCol(selectedButton);
            int toRow = findRow(buttonClicked);
            int toCol = findCol(buttonClicked);

            CheckerPiecesColors tempPiece = pieces[fromRow][fromCol];
            pieces[fromRow][fromCol] = pieces[toRow][toCol];
            pieces[toRow][toCol] = tempPiece;

            updateBoard();

            selectedButton = null;
        }
    }

    private int findRow(JButton button) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == button) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findCol(JButton button) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == button) {
                    return j;
                }
            }
        }
        return -1;
    }

    private void updateBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (pieces[i][j] != null) {
                    if (pieces[i][j].getColor().equals(Color.WHITE)) {
                        board[i][j].setIcon(new ImageIcon("C:\\white_checker.png"));
                    } else if (pieces[i][j].getColor().equals(Color.BLACK)) {
                        board[i][j].setIcon(new ImageIcon("C:\\black_checker.png"));
                    }
                } else {
                    board[i][j].setIcon(null);
                }
            }
        }
    }
}*/
