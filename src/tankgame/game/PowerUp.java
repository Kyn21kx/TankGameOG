package tankgame.game;

import tankgame.Ray;
import tankgame.Vector2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import static javax.imageio.ImageIO.read;

public class PowerUp {
    //HellMode: Rotates the player, while firing with no ammo limitation (but can't move)
    //Invincibility (pretty self explanatory)
    //Ghost bullet: Bullets can go through walls
    //Summoner: Randomly spawn bullets in the screen
    //EarthBender: Rebuilds the walls
    public enum Ptype {HellMode, Invincibility, GhostBullet, Summoner, EarthBender};

    public Ptype powerUpType;
    private BufferedImage img;
    public Vector2 position;
    public int frames = 1000;
    private String name;
    private Tank attachedTank;
    public AudioClip earthBending, pickup, shield;


    public PowerUp (Vector2 position) {

        this.position = position;
        powerUpType = RandGeneration();
        try {
            this.img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("PowerUp.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DrawImage (Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.position.x, this.position.y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    public boolean ExecutedPowerUp () {
        switch (powerUpType) {
            case HellMode:
                attachedTank.ammo = 100;
                attachedTank.canMove = false;
                attachedTank.reloading = false;
                attachedTank.shotOnCD = false;
                attachedTank.rotateRight();
                if (TRE.tick % 3 == 0)
                    attachedTank.Shoot();
                break;
            case Summoner:
                if (TRE.tick % 15 == 0) {
                    Ray b = new Ray(Vector2.Rand(20, 680), (int)(Math.random() * (360)), TRE.bullet);
                    b.speed = 2;
                    attachedTank.bullets.add(b);
                }
                break;
            case EarthBender:
                //Sound effects
                earthBending.play();
                TRE.listWalls.clear();
                TRE.WallsInitialization();
                return true;
        }
        return false;
    }

    public boolean Attach (Tank t1, Tank t2) {
        //CheckForCollisions
        float d1 = Vector2.Distance(position, t1.position);
        float d2 = Vector2.Distance(position, t2.position);
        if (d1 < 6 && !t1.getHoldingPowerUp()) {
            //Give it to Tank1
            t1.setActivePowerUp(this);
            attachedTank = t1;
            pickup.play();
            return true;
        }
        if (d2 < 6 && !t2.getHoldingPowerUp()) {
            //Give it to Tank2
            t2.setActivePowerUp(this);
            attachedTank = t2;
            pickup.play();
            return true;
        }
        return false;
    }

    private Ptype RandGeneration () {
        int selection = new Random().nextInt(Ptype.values().length);
        return Ptype.values()[selection];
    }

    @Override
    public String toString() {
        switch (powerUpType) {
            case HellMode:
                name = "Hell mode";
                break;
            case Summoner:
                name = "Summoner";
                break;
            case Invincibility:
                name = "Invincibility";
                break;
            case EarthBender:
                name = "EarthBender";
                break;
            case GhostBullet:
                name = "Ghost bullet";
                break;
        }
        return name;
    }
}
