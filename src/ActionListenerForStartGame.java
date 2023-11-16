import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForStartGame implements ActionListener {

    private JFrame frame;
    public ActionListenerForStartGame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CheckersGame game = new CheckersGame(frame);
        game.pack();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

        frame.setVisible(false);
    }
}
