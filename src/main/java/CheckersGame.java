import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static consts.Consts.SIZE;

public class CheckersGame extends JFrame implements ActionListener {

    private final JPanel panel = new JPanel();
    private final JFrame frame;
    private final JButton[][] board = new JButton[SIZE][SIZE];
    private CheckerPiecesColors[][] pieces;
    private JButton selectedButton;
    private Color movablePieceColor;
    private Color lastMovablePieceColor = Color.BLACK;
    private String cwd = System.getProperty("user.dir");

    private MoveColorLabel label;
    private GridBagConstraints c;
    private GridBagLayout layout;

    public CheckersGame(JFrame frame) {
        super("Checkers Surrender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame = frame;

        layout = new GridBagLayout();
        panel.setLayout(layout);

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
        c.gridwidth = 5;
        c.gridheight = 1;

        layout.setConstraints(label, c);
        panel.add(label);

        JButton menu = new JButton("Выход в меню");
        menu.addActionListener(new ActionListenerForMainMenu());
        layout.setConstraints(menu, c);
        panel.add(menu, GridBagConstraints.RELATIVE);

        c.gridwidth = 1;
        c.gridy = c.gridy + 2;

        initializeBoard();

        this.add(panel);
    }

    public void initializeBoard() {
        pieces = new CheckerPiecesColors[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 == 1) {
                    if (i <= 2) {
                        board[i][j] = new JButton(new ImageIcon(cwd + "/src/resources/black_checker.PNG"));
                        pieces[i][j] = new CheckerPiecesColors(Color.BLACK);
                    } else if (i >= 5) {
                        board[i][j] = new JButton(new ImageIcon(cwd + "/src/resources/white_checker.PNG"));
                        pieces[i][j] = new CheckerPiecesColors(Color.WHITE);
                    } else {
                        board[i][j] = new JButton();
                    }
                    board[i][j].setBackground(Color.decode("#755C48"));
                } else {
                    board[i][j] = new JButton();
                    board[i][j].setBackground(Color.decode("#D2B48C"));
                }
                board[i][j].setPreferredSize(new Dimension(60,60));
                board[i][j].addActionListener(this);
                board[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
                board[i][j].setBorderPainted(false);
                layout.setConstraints(board[i][j], c);
                panel.add(board[i][j], GridBagConstraints.RELATIVE);
            }
            c.gridy++;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        int row = findRow(buttonClicked);
        int col = findCol(buttonClicked);

        if ((row + col) % 2 == 1) {
            if (selectedButton == null && pieces[row][col] != null && lastMovablePieceColor != pieces[row][col].getColor()) {
                selectedButton = buttonClicked;
                movablePieceColor = pieces[row][col].getColor();

                selectedButton.setBorderPainted(true);
            } else if (buttonClicked == selectedButton) {
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

                if (movablePieceColor.equals(Color.LIGHT_GRAY) && pieces[toRow][toCol] == null) {
                    int eatableCheckers = -1;
                    if (fromRow > toRow && fromCol > toCol && pieces[toRow + 1][toCol + 1] != null && pieces[toRow + 1][toCol + 1].getColor().equals(Color.BLACK)) {
                        eatableCheckers = checkEatableCheckers(toRow, fromRow, toCol, fromCol);
                        if (eatableCheckers == 1) {
                            eatCheckersByDamka(toRow, fromRow, toCol, fromCol);
                        }
                    } else if (fromRow > toRow && fromCol < toCol && pieces[toRow + 1][toCol - 1] != null && pieces[toRow + 1][toCol - 1].getColor().equals(Color.BLACK)) {
                        eatableCheckers = checkEatableCheckers(toRow, fromRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            eatCheckersByDamka(toRow, fromRow, fromCol, toCol);
                        }
                    } else if (fromRow < toRow && fromCol > toCol && pieces[toRow - 1][toCol + 1] != null && pieces[toRow - 1][toCol + 1].getColor().equals(Color.BLACK)) {
                        eatableCheckers = checkEatableCheckers(fromRow, toRow, toCol, fromCol);
                        if (eatableCheckers == 1) {
                            eatCheckersByDamka(fromRow, toRow, toCol, fromCol);
                        }
                    } else if (fromRow < toRow && fromCol < toCol && pieces[toRow - 1][toCol - 1] != null && pieces[toRow - 1][toCol - 1].getColor().equals(Color.BLACK)) {
                        eatableCheckers = checkEatableCheckers(fromRow, toRow, fromCol, toCol);
                        if (eatableCheckers == 1) {
                            eatCheckersByDamka(fromRow, toRow, fromCol, toCol);
                        }
                    }

                    if (eatableCheckers == 1 || eatableCheckers == 0) {
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = null;
                    }

                    checkRightMove = true;
                } else if (movablePieceColor.equals(Color.DARK_GRAY)) {
                    int eatableCheckers = -1;
                    if (fromRow > toRow && fromCol > toCol && pieces[toRow + 1][toCol + 1] != null && pieces[toRow + 1][toCol + 1].getColor().equals(Color.WHITE)) {
                        eatableCheckers = checkEatableCheckers(toRow, fromRow, toCol, fromCol);
                    } else if (fromRow > toRow && fromCol < toCol && pieces[toRow + 1][toCol - 1] != null && pieces[toRow + 1][toCol - 1].getColor().equals(Color.WHITE)) {
                        eatableCheckers = checkEatableCheckers(toRow, fromRow, fromCol, toCol);
                    } else if (fromRow < toRow && fromCol > toCol && pieces[toRow - 1][toCol + 1] != null && pieces[toRow - 1][toCol + 1].getColor().equals(Color.WHITE)) {
                        eatableCheckers = checkEatableCheckers(fromRow, toRow, toCol, fromCol);
                    } else if (fromRow < toRow && fromCol < toCol && pieces[toRow - 1][toCol - 1] != null && pieces[toRow - 1][toCol - 1].getColor().equals(Color.WHITE)) {
                        eatableCheckers = checkEatableCheckers(fromRow, toRow, fromCol, toCol);
                    }

                    if (eatableCheckers == 1) {
                        eatCheckersByDamka(toRow, fromRow, toCol, fromCol);
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = null;
                    } else if (eatableCheckers == 0) {
                        pieces[toRow][toCol] = pieces[fromRow][fromCol];
                        pieces[fromRow][fromCol] = null;
                    }

                    checkRightMove = true;
                } else if ((movablePieceColor.equals(Color.WHITE) && (toRow + 1 == fromRow) && colDiff == 1 && pieces[toRow][toCol] == null) || (movablePieceColor.equals(Color.BLACK) && fromRow + 1 == toRow && colDiff == 1 && pieces[toRow][toCol] == null)) {
                    pieces[toRow][toCol] = pieces[fromRow][fromCol];
                    pieces[fromRow][fromCol] = null;
                    checkRightMove = true;
                } else if (rowDiff == 2 && colDiff == 2 && pieces[toRow][toCol] == null && !(pieces[middleCheckerRow][middleCheckerCol].getColor().equals(movablePieceColor))) {
                    pieces[toRow][toCol] = pieces[fromRow][fromCol];
                    pieces[fromRow][fromCol] = null;
                    pieces[middleCheckerRow][middleCheckerCol] = null;
                    checkRightMove = true;
                }

                final int whiteDamkaRow = 0;
                final int blackDamkaRow = 7;

                if (pieces[toRow][toCol] != null && pieces[toRow][toCol].getColor().equals(Color.BLACK) && toRow == blackDamkaRow) {
                    pieces[toRow][toCol] = new CheckerPiecesColors(Color.DARK_GRAY);
                }
                if (pieces[toRow][toCol] != null && pieces[toRow][toCol].getColor().equals(Color.WHITE) && toRow == whiteDamkaRow) {
                    pieces[toRow][toCol] = new CheckerPiecesColors(Color.LIGHT_GRAY);
                }

                updateMove(movablePieceColor);
                updateBoard();

                if (checkRightMove) {
                    selectedButton.setBorderPainted(false);
                    selectedButton = null;
                    lastMovablePieceColor = movablePieceColor;
                }
            }
        }
    }

    private int checkEatableCheckers(int fromRow, int toRow, int fromCol, int toCol) {
        int counter = 0;
        for (int i = fromRow + 1; i < toRow; i++) {
            for (int j = fromCol + 1; j < toCol; j++) {
                if (pieces[i][j] != null && (pieces[i][j].getColor().equals(Color.BLACK) || pieces[i][j].getColor().equals(Color.DARK_GRAY))) {
                    counter++;
                }
                if (pieces[i][j] != null && (pieces[i][j].getColor().equals(Color.WHITE) || pieces[i][j].getColor().equals(Color.DARK_GRAY))) {
                    counter = 0;
                    break;
                }
            }
        }
        return counter;
    }

    private void eatCheckersByDamka(int fromRow, int toRow, int fromCol, int toCol) {
        for (int i = fromRow; i < toRow; i++) {
            for (int j = fromCol; j < toCol; j++) {
                pieces[i][j] = null;
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

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (pieces[i][j] != null) {
                    if (pieces[i][j].getColor().equals(Color.WHITE)) {
                        ++whiteCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\resources\\white_checker.png"));
                    } else if (pieces[i][j].getColor().equals(Color.BLACK)) {
                        ++blackCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\resources\\black_checker.png"));
                    } else if (pieces[i][j].getColor().equals(Color.LIGHT_GRAY)) {
                        ++whiteCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\resources\\white_damka.png"));
                    } else if (pieces[i][j].getColor().equals(Color.DARK_GRAY)) {
                        ++blackCheckersCounter;
                        board[i][j].setIcon(new ImageIcon(cwd + "\\src\\resources\\black_damka.png"));
                    }
                } else {
                    board[i][j].setIcon(null);
                }
            }
        }

        if (whiteCheckersCounter == 0) {

        } else if (blackCheckersCounter == 0) {

        }
    }

    public void updateMove(Color color) {
        label.view(color);
    }
}
