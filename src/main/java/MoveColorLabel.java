import javax.swing.*;
import java.awt.*;

public class MoveColorLabel extends JLabel {

    public MoveColorLabel(Color checkerColor) {
        view(checkerColor);
    }

    public void view(Color checkerColor) {
        if (checkerColor.equals(Color.WHITE) || checkerColor.equals(Color.LIGHT_GRAY)) {
            super.setText("Ход чёрных");
        } else if (checkerColor.equals(Color.BLACK) || checkerColor.equals(Color.DARK_GRAY)) {
            super.setText("Ход белых");
        }
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.setFont(new Font("Default", Font.PLAIN, 32));
        super.setBounds(0,0, 300, 30);
        super.setPreferredSize(new Dimension(120, 30));
    }
}
