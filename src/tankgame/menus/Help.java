package tankgame.menus;

import tankgame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Help extends JPanel {

    private JButton controls;
    private JButton back;
    private Launcher lf;
    private BufferedImage menuBackground;

    public Help(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Rules.jpg"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        controls = new JButton("Controls");
        controls.setFont(new Font("Courier New", Font.BOLD ,24));
        controls.setBounds(150,600,175,50);
        controls.addActionListener((actionEvent -> {
            this.lf.setFrame("controls");
        }));


        back = new JButton("back");
        back.setFont(new Font("Courier New", Font.BOLD ,24));
        back.setBounds(150,650,175,50);
        back.addActionListener((actionEvent -> {
            this.lf.setFrame("start");
        }));


        this.add(controls);
        this.add(back);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }

}
