package domain;

import actionListeners.ActionListenerForExit;
import actionListeners.ActionListenerForMainMenu;
import database.MyDataBase;
import labels.MoveColorLabel;
import persistence.CheckersPersistence;
import persistence.LastMovePersistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static consts.Consts.CELLS_QUANTITY;
import static consts.Consts.SIZE;

public class CheckersGame implements ActionListener {

    private final JPanel gamePanel = new JPanel();
    private final JButton[][] board = new JButton[SIZE][SIZE];
    private CheckerPiecesColors[][] pieces;
    private JButton selectedButton;
    private Color movablePieceColor;
    private Color lastMovablePieceColor = Color.BLACK;
    private final String cwd = System.getProperty("user.dir");
    private final MoveColorLabel label;
    private final GridBagConstraints c;
    private final GridBagLayout layout;
    private final CheckersPersistence persistence = new CheckersPersistence();
    private final LastMovePersistence movePersistence = new LastMovePersistence();
    private static CheckersGame instance;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private final MyDataBase db = MyDataBase.getInstance();

    public CheckersGame(CardLayout cardLayout, JPanel cardPanel) {

        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        layout = new GridBagLayout();
        gamePanel.setLayout(layout);

        label = new MoveColorLabel(Color.BLACK);

        c = new GridBagConstraints();

        c.anchor = GridBagConstraints.CENTER;
        c.fill   = GridBagConstraints.BOTH;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = 4;
        c.gridheight = 1;

        layout.setConstraints(label, c);
        gamePanel.add(label);

        c.gridwidth = 2;

        JButton menu = new JButton("Выход в меню");
        menu.addActionListener(new ActionListenerForMainMenu(cardLayout, cardPanel, "menu"));
        layout.setConstraints(menu, c);
        gamePanel.add(menu);

        JButton exit = new JButton("Выход из игры");
        exit.addActionListener(new ActionListenerForExit());
        layout.setConstraints(exit, c);
        gamePanel.add(exit);

        c.gridwidth = 1;
        c.gridy = c.gridy + 2;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public synchronized static CheckersGame getInstance() {
        if (instance == null) {
            instance = new CheckersGame(cardLayout, cardPanel);
        }
        return instance;
    }

    public void createBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new JButton();

                if ((i + j) % 2 == 1) {
                    board[i][j].setBackground(Color.decode("#755C48"));
                } else {
                    board[i][j].setBackground(Color.decode("#D2B48C"));
                }

                board[i][j].setPreferredSize(new Dimension(60,60));
                board[i][j].addActionListener(this);
                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
                board[i][j].setBorderPainted(false);

                layout.setConstraints(board[i][j], c);
                gamePanel.add(board[i][j], GridBagConstraints.RELATIVE);
            }
            c.gridy++;
        }
    }

    public void initializeBoard() {
        lastMovablePieceColor = Color.BLACK;
        movePersistence.updateLastMove(lastMovablePieceColor.toString());

        movePersistence.createLastMove();

        pieces = new CheckerPiecesColors[SIZE][SIZE];

        int id = 0;


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 == 1) {
                    if (i <= 2) {
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_checker.PNG"));
                        pieces[i][j] = new CheckerPiecesColors(Color.BLACK);
                    } else if (i >= 5) {
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_checker.PNG"));
                        pieces[i][j] = new CheckerPiecesColors(Color.WHITE);
                    } else {
                        board[i][j].setIcon(null);
                        pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                    }
                } else {
                    pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                }

                persistence.createChecker(id, i, j, pieces[i][j].getColor().toString());

                id++;
            }
        }
    }

    public void loadBoard() {
        pieces = new CheckerPiecesColors[SIZE][SIZE];

        String color = db.getLastMove();

        if (color.equals("java.awt.Color[r=255,g=255,b=255]") || color.equals("java.awt.Color[r=192,g=192,b=192]")) {
            lastMovablePieceColor = Color.WHITE;
        } else if (color.equals("java.awt.Color[r=0,g=0,b=0]") || color.equals("java.awt.Color[r=64,g=64,b=64]")) {
            lastMovablePieceColor = Color.BLACK;
        }

        updateMove(lastMovablePieceColor);

        Map<Integer, int[]> whiteCheckers = db.getWhiteCheckers();
        Map<Integer, int[]> blackCheckers = db.getBlackCheckers();
        Map<Integer, int[]> whiteQueens = db.getWhiteQueens();
        Map<Integer, int[]> blackQueens = db.getBlackQueens();
        Map<Integer, int[]> otherFields = db.getAllFields();

        int[] coord;

        for (int i = 0; i < CELLS_QUANTITY; i++) {
            if (whiteCheckers.containsKey(i)) {
                coord = whiteCheckers.get(i);
                board[coord[0]][coord[1]].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_checker.PNG"));
                pieces[coord[0]][coord[1]] = new CheckerPiecesColors(Color.WHITE);
            } else if (blackCheckers.containsKey(i)) {
                coord = blackCheckers.get(i);
                board[coord[0]][coord[1]].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_checker.PNG"));
                pieces[coord[0]][coord[1]] = new CheckerPiecesColors(Color.BLACK);
            } else if (whiteQueens.containsKey(i)) {
                coord = whiteQueens.get(i);
                board[coord[0]][coord[1]].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_damka.png"));
                pieces[coord[0]][coord[1]] = new CheckerPiecesColors(Color.LIGHT_GRAY);
            } else if (blackQueens.containsKey(i)) {
                coord = whiteQueens.get(i);
                board[coord[0]][coord[1]].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_damka.png"));
                pieces[coord[0]][coord[1]] = new CheckerPiecesColors(Color.DARK_GRAY);
            } else {
                coord = otherFields.get(i);
                board[coord[0]][coord[1]].setIcon(null);
                pieces[coord[0]][coord[1]] = new CheckerPiecesColors(Color.YELLOW);
            }
            persistence.updateChecker(i, coord[0], coord[1], pieces[coord[0]][coord[1]].getColor().toString());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        int row = findRow(buttonClicked);
        int col = findCol(buttonClicked);

        boolean flag = false;

        if ((row + col) % 2 == 1) {

            if (lastMovablePieceColor == Color.LIGHT_GRAY || lastMovablePieceColor == Color.WHITE) {
                flag = pieces[row][col].getColor() != Color.LIGHT_GRAY && pieces[row][col].getColor() != Color.WHITE && pieces[row][col].getColor() != Color.YELLOW;
            } else if (lastMovablePieceColor == Color.DARK_GRAY || lastMovablePieceColor == Color.BLACK) {
                flag = pieces[row][col].getColor() != Color.DARK_GRAY && pieces[row][col].getColor() != Color.BLACK && pieces[row][col].getColor() != Color.YELLOW;
            }

            if (selectedButton == null && flag) {
                selectedButton = buttonClicked;
                movablePieceColor = pieces[row][col].getColor();
                selectedButton.setBorderPainted(true);
            } else if (selectedButton != null && buttonClicked == selectedButton) {
                selectedButton.setBorderPainted(false);
                selectedButton = null;
            } else if (selectedButton != null) {
                int fromRow = findRow(selectedButton);
                int fromCol = findCol(selectedButton);
                int toRow = findRow(buttonClicked);
                int toCol = findCol(buttonClicked);

                final int rowDiff = Math.abs(fromRow - toRow);
                final int colDiff = Math.abs(fromCol - toCol);

                final int middleCheckerRow = (fromRow + toRow) / 2;
                final int middleCheckerCol = (fromCol + toCol) / 2;

                boolean checkRightMove = false;

                if (movablePieceColor.equals(Color.LIGHT_GRAY) && pieces[toRow][toCol].getColor().equals(Color.YELLOW)) {
                    int eatableCheckers = -1;

                    if (rowDiff == 1 && colDiff == 1) {
                        eatableCheckers = 0;
                    } else if (rowDiff == colDiff && fromRow > toRow && fromCol > toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow + 1][toCol + 1].getColor().equals(Color.BLACK) || pieces[toRow + 1][toCol + 1].getColor().equals(Color.DARK_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow > toRow && fromCol < toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow + 1][toCol - 1].getColor().equals(Color.BLACK) || pieces[toRow + 1][toCol - 1].getColor().equals(Color.DARK_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow < toRow && fromCol > toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow - 1][toCol + 1].getColor().equals(Color.BLACK) || pieces[toRow - 1][toCol + 1].getColor().equals(Color.DARK_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow < toRow && fromCol < toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow - 1][toCol - 1].getColor().equals(Color.BLACK) || pieces[toRow - 1][toCol - 1].getColor().equals(Color.DARK_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    }

                    if (eatableCheckers == 1 || eatableCheckers == 0) {
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                        checkRightMove = true;
                    }

                } else if (movablePieceColor.equals(Color.DARK_GRAY) && pieces[toRow][toCol].getColor().equals(Color.YELLOW)) {
                    int eatableCheckers = -1;

                    if (rowDiff == 1 && colDiff == 1) {
                        eatableCheckers = 0;
                    } else if (rowDiff == colDiff && fromRow > toRow && fromCol > toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow + 1][toCol + 1].getColor().equals(Color.WHITE) || pieces[toRow + 1][toCol + 1].getColor().equals(Color.LIGHT_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow > toRow && fromCol < toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow + 1][toCol - 1].getColor().equals(Color.WHITE) || pieces[toRow + 1][toCol - 1].getColor().equals(Color.LIGHT_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow < toRow && fromCol > toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow - 1][toCol + 1].getColor().equals(Color.WHITE) || pieces[toRow - 1][toCol + 1].getColor().equals(Color.LIGHT_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    } else if (rowDiff == colDiff && fromRow < toRow && fromCol < toCol) {
                        eatableCheckers = countCheckersBetweenCells(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            if (pieces[toRow - 1][toCol - 1].getColor().equals(Color.WHITE) || pieces[toRow - 1][toCol - 1].getColor().equals(Color.LIGHT_GRAY)) {
                                eatCheckersByQueen(fromRow, toRow, fromCol, toCol);
                            } else {
                                eatableCheckers = -1;
                            }
                        }
                    }

                    if (eatableCheckers == 1 || eatableCheckers == 0) {
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                        checkRightMove = true;
                    }

                } else if ((movablePieceColor.equals(Color.WHITE) && (toRow + 1 == fromRow) && colDiff == 1 && pieces[toRow][toCol].getColor().equals(Color.YELLOW)) || (movablePieceColor.equals(Color.BLACK) && fromRow + 1 == toRow && colDiff == 1 && pieces[toRow][toCol].getColor().equals(Color.YELLOW))) {
                    pieces[toRow][toCol] = pieces[fromRow][fromCol];
                    pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                    checkRightMove = true;
                } else if ((movablePieceColor.equals(Color.WHITE) || movablePieceColor.equals(Color.BLACK)) && rowDiff == 2 && colDiff == 2 && pieces[toRow][toCol].getColor().equals(Color.YELLOW) && !pieces[middleCheckerRow][middleCheckerCol].getColor().equals(movablePieceColor)) {
                    pieces[toRow][toCol] = pieces[fromRow][fromCol];
                    pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                    pieces[middleCheckerRow][middleCheckerCol] = new CheckerPiecesColors(Color.YELLOW);
                    checkRightMove = true;
                }

                final int whiteQueenRow = 0;
                final int blackQueenRow = 7;

                if (pieces[toRow][toCol].getColor().equals(Color.BLACK) && toRow == blackQueenRow) {
                    pieces[toRow][toCol] = new CheckerPiecesColors(Color.DARK_GRAY);
                }
                if (pieces[toRow][toCol].getColor().equals(Color.WHITE) && toRow == whiteQueenRow) {
                    pieces[toRow][toCol] = new CheckerPiecesColors(Color.LIGHT_GRAY);
                }

                if (checkRightMove) {
                    if (movablePieceColor.equals(Color.LIGHT_GRAY)) {
                        movablePieceColor = Color.WHITE;
                    } else if (movablePieceColor.equals(Color.DARK_GRAY)) {
                        movablePieceColor = Color.BLACK;
                    }

                    updateMove(movablePieceColor);
                    selectedButton.setBorderPainted(false);
                    selectedButton = null;
                    lastMovablePieceColor = movablePieceColor;
                }

                updateBoard();
            }
        }
    }

    private int countCheckersBetweenCells(int fromRow, int toRow, int fromCol, int toCol) {
        int counter = 0;

        if (fromRow < toRow) {
            if (fromCol < toCol) {
                int j = fromCol + 1;
                for (int i = fromRow + 1; i < toRow; ++i) {
                    counter = checkRightEat(i, j, counter);
                    if (counter == -1) {
                        return counter;
                    }
                    j++;
                }
            } else {
                int j = fromCol - 1;
                for (int i = fromRow + 1; i < toRow; ++i) {
                    counter = checkRightEat(i, j, counter);
                    if (counter == -1) {
                        return counter;
                    }
                    j--;
                }
            }
        } else {
            if (fromCol < toCol) {
                int j = fromCol + 1;
                for (int i = fromRow - 1; i > toRow; --i) {
                    counter = checkRightEat(i, j, counter);
                    if (counter == -1) {
                        return counter;
                    }
                    j++;
                }
            } else {
                int j = fromCol - 1;
                for (int i = fromRow - 1; i > toRow; --i) {
                    counter = checkRightEat(i, j, counter);
                    if (counter == -1) {
                        return counter;
                    }
                    j--;
                }
            }
        }

        return counter;
    }

    private int checkRightEat(int i, int j, int counter) {

        if (pieces[i][j] == null) {
            return counter;
        } else if (movablePieceColor == Color.WHITE || movablePieceColor == Color.LIGHT_GRAY) {
            if (pieces[i][j].getColor().equals(Color.BLACK) || pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                counter++;
            }
            if (pieces[i][j].getColor().equals(Color.WHITE) || pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                counter = -1;
            }
        } else if (movablePieceColor == Color.BLACK || movablePieceColor == Color.DARK_GRAY) {
            if (pieces[i][j].getColor().equals(Color.WHITE) || pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                counter++;
            }
            if (pieces[i][j].getColor().equals(Color.BLACK) || pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                counter = -1;
            }
        }

        return counter;
    }

    private void eatCheckersByQueen(int fromRow, int toRow, int fromCol, int toCol) {

        if (fromRow < toRow) {
            if (fromCol < toCol) {
                int j = fromCol + 1;
                for (int i = fromRow + 1; i < toRow; ++i) {
                    pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                    j++;
                }
            } else {
                int j = fromCol - 1;
                for (int i = fromRow + 1; i < toRow; ++i) {
                    pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                    j--;
                }
            }
        } else {
            if (fromCol < toCol) {
                int j = fromCol + 1;
                for (int i = fromRow - 1; i > toRow; --i) {
                    pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                    j++;
                }
            } else {
                int j = fromCol - 1;
                for (int i = fromRow - 1; i > toRow; --i) {
                    pieces[i][j] = new CheckerPiecesColors(Color.YELLOW);
                    j--;
                }
            }
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

    public void updateBoard() {
        int whiteCheckersCounter = 0;
        int blackCheckersCounter = 0;
        int id = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!pieces[i][j].getColor().equals(Color.YELLOW)) {
                    if (pieces[i][j].getColor().equals(Color.WHITE)) {
                        ++whiteCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_checker.png"));
                    } else if (pieces[i][j].getColor().equals(Color.BLACK)) {
                        ++blackCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_checker.png"));
                    } else if (pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                        ++whiteCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_damka.png"));
                    } else if (pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                        ++blackCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_damka.png"));
                    }
                } else {
                    board[i][j].setIcon(null);
                }
                persistence.updateChecker(id, i, j, pieces[i][j].getColor().toString());
                id++;
            }
        }

        if (whiteCheckersCounter == 0) {
            updateMove(Color.GREEN);
            db.deleteCheckers();
        } else if (blackCheckersCounter == 0) {
            updateMove(Color.RED);
        }
    }

    public void updateMove(Color color) {
        label.view(color);
        movePersistence.updateLastMove(color.toString());
    }
}
