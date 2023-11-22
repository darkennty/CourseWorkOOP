import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Menu extends JFrame {

    private JPanel panel = new JPanel();

    public JFrame frame = new JFrame();

    public Menu() {

        super("Checkers Surrender Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(480, 480);

        PoddavkiLabel label = new PoddavkiLabel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#D2B48C"));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setPreferredSize(new Dimension(480, 480));

        JButton start = new JButton("Начать новую игру");
        start.setBackground(Color.decode("#D2B48C"));
        start.setBounds(0,120, 120, 120);
        start.setFont(new Font("Copperplate", Font.PLAIN, 28));
        start.setPreferredSize(new Dimension(240, 120));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setFocusable(false);
        start.setBorderPainted(false);
        Border startBorder = start.getBorder();
        Border marginTopTwenty = new EmptyBorder(60, 60, 60, 60);
        start.setBorder(new CompoundBorder(startBorder, marginTopTwenty));

        JButton exit = new JButton("Выйти из игры");
        exit.setBackground(Color.decode("#D2B48C"));
        exit.setBounds(0,240, 120, 120);
        exit.setFont(new Font("Copperplate", Font.PLAIN, 28));
        exit.setPreferredSize(new Dimension(240, 240));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setFocusable(false);
        exit.setBorderPainted(false);
        Border exitBorder = exit.getBorder();
        exit.setBorder(new CompoundBorder(exitBorder, marginTopTwenty));

        panel.add(label);
        panel.add(start);
        panel.add(exit);

        this.add(panel);

        start.addActionListener(new ActionListenerForStartGame(this));
        exit.addActionListener(new ActionListenerForExit());
    }
}
