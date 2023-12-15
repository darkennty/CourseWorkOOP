package domain;

import action.listeners.ActionListenerForGameExit;
import action.listeners.ActionListenerForMainMenu;
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
    private final String[][] borderColor = new String[SIZE][SIZE];
    private JButton selectedButton;
    private Color movablePieceColor;
    private Color lastMovablePieceColor = Color.BLACK;
    private final String cwd = System.getProperty("user.dir");
    private final MoveColorLabel label;
    private final GridBagConstraints c;
    private final GridBagLayout layout;
    private final CheckersPersistence persistence = new CheckersPersistence();
    private final LastMovePersistence movePersistence = new LastMovePersistence();
    private final MyDataBase db = MyDataBase.getInstance();
    private int requiredMovesCounter = 0;

    public CheckersGame(CardLayout cardLayout, JPanel cardPanel) {

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
        menu.addActionListener(new ActionListenerForMainMenu(cardLayout, cardPanel, "menu", this));
        layout.setConstraints(menu, c);
        gamePanel.add(menu);

        JButton exit = new JButton("Выход из игры");
        exit.addActionListener(new ActionListenerForGameExit(this));
        layout.setConstraints(exit, c);
        gamePanel.add(exit);

        c.gridwidth = 1;
        c.gridy = c.gridy + 2;
    }

    public JPanel getGamePanel() {
        return gamePanel;
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
                borderColor[i][j] = Color.GREEN.toString();
                board[i][j].setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.GREEN));
                board[i][j].setBorderPainted(false);

                layout.setConstraints(board[i][j], c);
                gamePanel.add(board[i][j], GridBagConstraints.RELATIVE);
            }
            c.gridy++;
        }
    }

    public void initializeBoard() {
        lastMovablePieceColor = Color.BLACK;
        movePersistence.createLastMove();
        movePersistence.updateLastMove(lastMovablePieceColor.toString());

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
                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
                board[i][j].setBorderPainted(false);

                persistence.createChecker(id, i, j, borderColor[i][j], pieces[i][j].getColor().toString());

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

        Map<Integer, String[]> whiteCheckers = db.getWhiteCheckers();
        Map<Integer, String[]> blackCheckers = db.getBlackCheckers();
        Map<Integer, String[]> whiteQueens = db.getWhiteQueens();
        Map<Integer, String[]> blackQueens = db.getBlackQueens();
        Map<Integer, String[]> otherFields = db.getAllFields();

        String[] data;

        int x;
        int y;

        for (int i = 0; i < CELLS_QUANTITY; i++) {
            if (whiteCheckers.containsKey(i)) {
                data = whiteCheckers.get(i);

                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);

                board[x][y].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_checker.PNG"));
                pieces[x][y] = new CheckerPiecesColors(Color.WHITE);
                borderColor[x][y] = data[2];
            } else if (blackCheckers.containsKey(i)) {
                data = blackCheckers.get(i);

                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);


                board[x][y].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_checker.PNG"));
                pieces[x][y] = new CheckerPiecesColors(Color.BLACK);
                borderColor[x][y] = data[2];
            } else if (whiteQueens.containsKey(i)) {
                data = whiteQueens.get(i);

                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);

                board[x][y].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_damka.png"));
                pieces[x][y] = new CheckerPiecesColors(Color.LIGHT_GRAY);
                borderColor[x][y] = data[2];
            } else if (blackQueens.containsKey(i)) {
                data = blackQueens.get(i);

                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);

                board[x][y].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_damka.png"));
                pieces[x][y] = new CheckerPiecesColors(Color.DARK_GRAY);
                borderColor[x][y] = data[2];
            } else {
                data = otherFields.get(i);

                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);

                board[x][y].setIcon(null);
                pieces[x][y] = new CheckerPiecesColors(Color.YELLOW);
                borderColor[x][y] = data[2];
            }
            persistence.updateChecker(i, x, y, borderColor[x][y], pieces[x][y].getColor().toString());

            if (borderColor[x][y].equals(Color.RED.toString())) {
                board[x][y].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                board[x][y].setBorderPainted(true);
                requiredMovesCounter++;
            } else {
                board[x][y].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
                board[x][y].setBorderPainted(false);
            }
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

            if (selectedButton == null && flag && (requiredMovesCounter == 0 || borderColor[row][col].equals(Color.RED.toString()))) {
                selectedButton = buttonClicked;
                movablePieceColor = pieces[row][col].getColor();
                selectedButton.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.GREEN));
                selectedButton.setBorderPainted(true);
            } else if (selectedButton != null && buttonClicked == selectedButton) {
                if (borderColor[row][col].equals(Color.GREEN.toString())) {
                    selectedButton.setBorderPainted(false);
                } else {
                    selectedButton.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.RED));
                }

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

                    if (eatableCheckers == 1 || (eatableCheckers == 0 && requiredMovesCounter == 0)) {
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

                    if (eatableCheckers == 1 || (eatableCheckers == 0 && requiredMovesCounter == 0)) {
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                        checkRightMove = true;
                    }

                } else if (requiredMovesCounter == 0 && ((movablePieceColor.equals(Color.WHITE) && (toRow + 1 == fromRow) && colDiff == 1 && pieces[toRow][toCol].getColor().equals(Color.YELLOW)) || (movablePieceColor.equals(Color.BLACK) && fromRow + 1 == toRow && colDiff == 1 && pieces[toRow][toCol].getColor().equals(Color.YELLOW)))) {
                    pieces[toRow][toCol] = pieces[fromRow][fromCol];
                    pieces[fromRow][fromCol] = new CheckerPiecesColors(Color.YELLOW);
                    checkRightMove = true;
                } else if ((movablePieceColor.equals(Color.WHITE) || movablePieceColor.equals(Color.BLACK)) && rowDiff == 2 && colDiff == 2 && pieces[toRow][toCol].getColor().equals(Color.YELLOW) && !pieces[middleCheckerRow][middleCheckerCol].getColor().equals(movablePieceColor) && !pieces[middleCheckerRow][middleCheckerCol].getColor().equals(Color.YELLOW)) {
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

                    updateBoard();
                }
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

        if (pieces[i][j].getColor().equals(Color.YELLOW)) {
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

        requiredMovesCounter = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
                board[i][j].setBorderPainted(false);
                borderColor[i][j] = Color.GREEN.toString();

                if (!pieces[i][j].getColor().equals(Color.YELLOW)) {
                    if (pieces[i][j].getColor().equals(Color.WHITE) || pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                        ++whiteCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_checker.png"));
                        if (checkIfCanEat(i, j)) {
                            board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                            board[i][j].setBorderPainted(true);
                            borderColor[i][j] = Color.RED.toString();
                            requiredMovesCounter++;
                        }
                    }

                    if (pieces[i][j].getColor().equals(Color.BLACK) || pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                        ++blackCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_checker.png"));
                        if (checkIfCanEat(i, j)) {
                            board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                            board[i][j].setBorderPainted(true);
                            borderColor[i][j] = Color.RED.toString();
                            requiredMovesCounter++;
                        }
                    }

                    if (pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\white_damka.png"));

                        if (movablePieceColor == Color.BLACK) {
                            cycle:
                            for (int k = 0; k < SIZE ; k++) {
                                for (int l = 0; l < SIZE; l++) {
                                    int eatableCheckers;

                                    int rowDiff = Math.abs(i - k);
                                    int colDiff = Math.abs(j - l);

                                    if (rowDiff == colDiff && i > k && j > l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k + 1][l + 1].getColor().equals(Color.BLACK) || pieces[k + 1][l + 1].getColor().equals(Color.DARK_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k + 2][l + 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i > k && j < l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k + 1][l - 1].getColor().equals(Color.BLACK) || pieces[k + 1][l - 1].getColor().equals(Color.DARK_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k + 2][l - 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i < k && j > l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k - 1][l + 1].getColor().equals(Color.BLACK) || pieces[k - 1][l + 1].getColor().equals(Color.DARK_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k - 2][l + 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i < k && j < l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k - 1][l - 1].getColor().equals(Color.BLACK) || pieces[k - 1][l - 1].getColor().equals(Color.DARK_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k - 2][l - 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\main\\resources\\images\\black_damka.png"));

                        if (movablePieceColor == Color.WHITE) {
                            cycle:
                            for (int k = 0; k < SIZE ; k++) {
                                for (int l = 0; l < SIZE; l++) {
                                    int eatableCheckers;

                                    int rowDiff = Math.abs(i - k);
                                    int colDiff = Math.abs(j - l);

                                    if (rowDiff == colDiff && i > k && j > l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k + 1][l + 1].getColor().equals(Color.WHITE) || pieces[k + 1][l + 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k + 2][l + 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i > k && j < l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k + 1][l - 1].getColor().equals(Color.WHITE) || pieces[k + 1][l - 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k + 2][l - 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i < k && j > l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k - 1][l + 1].getColor().equals(Color.WHITE) || pieces[k - 1][l + 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k - 2][l + 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    } else if (rowDiff == colDiff && i < k && j < l) {
                                        eatableCheckers = countCheckersBetweenCells(i, k, j, l);
                                        if (eatableCheckers == -1) {
                                            if ((pieces[k - 1][l - 1].getColor().equals(Color.WHITE) || pieces[k - 1][l - 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[k][l].getColor().equals(Color.YELLOW) && pieces[k - 2][l - 2].getColor().equals(Color.YELLOW)) {
                                                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
                                                board[i][j].setBorderPainted(true);
                                                borderColor[i][j] = Color.RED.toString();
                                                requiredMovesCounter++;
                                                break cycle;
                                            }
                                        } else if (eatableCheckers != 0) {
                                            break cycle;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    board[i][j].setIcon(null);
                }
            }
        }

        if (whiteCheckersCounter == 0) {
            updateMove(Color.GREEN);
            db.deleteCheckers();
            db.deleteLastMove();
        } else if (blackCheckersCounter == 0) {
            updateMove(Color.RED);
            db.deleteCheckers();
            db.deleteLastMove();
        }
    }

    public boolean checkIfCanEat(int i, int j) {
        if ((pieces[i][j].getColor().equals(Color.WHITE) || (pieces[i][j].getColor().equals(Color.LIGHT_GRAY))) && lastMovablePieceColor == Color.BLACK) {
            if (i >= 2 && j >= 2) {
                if ((pieces[i - 1][j - 1].getColor().equals(Color.BLACK) || pieces[i - 1][j - 1].getColor().equals(Color.DARK_GRAY)) && pieces[i - 2][j - 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i <= 5 && j <= 5) {
                if ((pieces[i + 1][j + 1].getColor().equals(Color.BLACK) || pieces[i + 1][j + 1].getColor().equals(Color.DARK_GRAY)) && pieces[i + 2][j + 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i <= 5 && j >= 2) {
                if ((pieces[i + 1][j - 1].getColor().equals(Color.BLACK) || pieces[i + 1][j - 1].getColor().equals(Color.DARK_GRAY)) && pieces[i + 2][j - 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i >= 2 && j <= 5) {
                if ((pieces[i - 1][j + 1].getColor().equals(Color.BLACK) || pieces[i - 1][j + 1].getColor().equals(Color.DARK_GRAY)) && pieces[i - 2][j + 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }
        } else if ((pieces[i][j].getColor().equals(Color.BLACK) || pieces[i][j].getColor().equals(Color.DARK_GRAY)) && lastMovablePieceColor == Color.WHITE) {
            if (i >= 2 && j >= 2) {
                if ((pieces[i - 1][j - 1].getColor().equals(Color.WHITE) || pieces[i - 1][j - 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[i - 2][j - 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i <= 5 && j <= 5) {
                if ((pieces[i + 1][j + 1].getColor().equals(Color.WHITE) || pieces[i + 1][j + 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[i + 2][j + 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i <= 5 && j >= 2) {
                if ((pieces[i + 1][j - 1].getColor().equals(Color.WHITE) || pieces[i + 1][j - 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[i + 2][j - 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }

            if (i >= 2 && j <= 5) {
                if ((pieces[i - 1][j + 1].getColor().equals(Color.WHITE) || pieces[i - 1][j + 1].getColor().equals(Color.LIGHT_GRAY)) && pieces[i - 2][j + 2].getColor().equals(Color.YELLOW)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void updateMove(Color color) {
        label.view(color);
        movePersistence.updateLastMove(color.toString());
    }

    public void saveBoard(CheckerPiecesColors[][] pieces, Color color) {
        this.pieces = pieces;

        int id = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                persistence.updateChecker(id, i, j, borderColor[i][j], pieces[i][j].getColor().toString());
                id++;
            }
        }

        movePersistence.updateLastMove(color.toString());
    }

    public Color getLastMoveColor() {
        return lastMovablePieceColor;
    }

    public CheckerPiecesColors[][] getPieces() {
        return pieces;
    }

    public void getSelectedButtonNull() {
        selectedButton = null;
    }
}