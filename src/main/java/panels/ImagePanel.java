package panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;
    private final String cwd = System.getProperty("user.dir");
    private final String finalPath = "\\src\\main\\resources\\images\\checkers.jpg";

    public ImagePanel() {
        try {
            backgroundImage = ImageIO.read(new File(cwd + finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
