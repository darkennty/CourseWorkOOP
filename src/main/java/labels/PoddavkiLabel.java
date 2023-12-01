package labels;

import javax.swing.*;
import java.awt.*;

public class PoddavkiLabel extends JLabel {
    public PoddavkiLabel() {
        view();
    }

    public void view() {
        super.setText("ПОДДАВКИ");
        super.setFont(new Font("Arial", Font.BOLD, 48));
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.setForeground(Color.WHITE);
        super.setBounds(0,0, 120, 240);
        super.setPreferredSize(new Dimension(120, 120));
    }
}
