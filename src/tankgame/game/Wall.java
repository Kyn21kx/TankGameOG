package tankgame.game;

import tankgame.Collision;
import tankgame.Ray;
import tankgame.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Wall {

    public Vector2 position;
    private Rectangle hitBox;
    private BufferedImage img, mainImg, breakImg;
    private boolean breakable;
    private boolean breakWall;

    public Wall (Vector2 position, BufferedImage mainImg, BufferedImage breakImg) {
        this.position = position;
        this.breakable = new Random().nextBoolean();
        //if (breakable)
          //  breakable = new Random().nextBoolean(); //Reduces the chances of getting a breakable wall
        this.img = breakable ? breakImg : mainImg;
        this.mainImg = mainImg;
        this.breakImg = breakImg;
        this.hitBox = new Rectangle(position.x, position.y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitBox() {
        hitBox.setLocation(position.x, position.y);
        return hitBox.getBounds();
    }

    public void Update (Tank p1, Tank p2) {
        CheckForCollision(p1);
        CheckForCollision(p2);
    }

    public BufferedImage getImg () {
        return this.img;
    }

    public boolean isBreakWall() {
        return breakWall;
    }

    public boolean isBreakable () {
        return breakable;
    }
    public void setBreakable (boolean v) {
        this.breakable = v;
        this.img = breakable ? this.breakImg : this.mainImg;
    }

    public void DrawImage (Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.position.x, this.position.y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    private void CheckForCollision (Tank t) {
        boolean powerCheck = t.getActivePowerUp() != null && t.getActivePowerUp().powerUpType == PowerUp.Ptype.GhostBullet && t.getPowerTimer() != 0;
        for (Ray bullet: t.bullets) {
            if (bullet != null && Collision.CollidedWith(bullet.getHitBox(), this.getHitBox()) && !powerCheck) {
                bullet.collided = true;
                breakWall = breakable;
            }
        }
    }

}
