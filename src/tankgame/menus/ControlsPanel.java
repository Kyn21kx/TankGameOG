package tankgame.menus;

import tankgame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ControlsPanel extends JPanel {

    private JButton back;
    private Launcher lf;
    private BufferedImage menuBackground;

    public ControlsPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Controls.jpg"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);



        back = new JButton("Back");
        back.setFont(new Font("Courier New", Font.BOLD ,24));
        back.setBounds(20,50,175,50);
        back.addActionListener((actionEvent -> {
            this.lf.setFrame("help");
        }));

        this.add(back);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }

}
