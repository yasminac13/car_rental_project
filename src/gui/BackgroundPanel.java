package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BackgroundPanel extends JPanel {
    private final Image image;

    public BackgroundPanel(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new IllegalArgumentException("Background image not found: " + f.getAbsolutePath());
        }
        ImageIcon icon = new ImageIcon(f.getAbsolutePath());
        this.image = icon.getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
