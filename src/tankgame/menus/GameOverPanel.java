package tankgame.menus;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.game.TRE;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameOverPanel extends JPanel {
    private BufferedImage menuBackground;
    private JButton start;
    private Launcher lf;


    public GameOverPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("title.png"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        JLabel description = new JLabel();
        description.setFont(new Font("TimesRoman", Font.ITALIC, 24));
        description.setForeground(Color.GREEN);
        description.setBounds(150, 250, 200, 70);
        description.setText("GAME OVER!");

        start = new JButton("Continue...");
        start.setFont(new Font("Courier New", Font.BOLD ,20));
        start.setBounds(150,350,175,50);
        start.addActionListener((actionEvent -> {
            this.lf.setFrame("restart");
        }));

        this.add(start);
        this.add(description);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }
}
