package tankgame;

import tankgame.game.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.annotation.Target;

public class Ray {
    public boolean collided, attackCollided = false;
    public Vector2 position;
    public Vector2 velocity = new Vector2();
    public int speed = 5;
    public BufferedImage img;
    Rectangle hitBox;
    public float angle;

    public Ray (Vector2 position, float angle, BufferedImage img) {
        this.position = new Vector2(position.x, position.y);
        this.angle = angle;
        this.img = img;
        this.hitBox = new Rectangle(this.position.x, this.position.y, this.img.getWidth(), this.img.getHeight());
    }

    public void Move () {
        if (!collided) {
            this.velocity.x = (int) Math.round(speed * Math.cos(Math.toRadians(angle)));
            this.velocity.y = (int) Math.round(speed * Math.sin(Math.toRadians(angle)));
            this.position.MoveTowards(this.velocity);
            collided = Collision.CheckBorder(position);
        }
    }

    public Rectangle getHitBox() {
        hitBox.setLocation(position.x, position.y);
        return hitBox.getBounds();
    }

    public void Update () {
        Move();
    }

    public void DrawImg(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.position.x, this.position.y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        //g2d.setColor(Color.GREEN);
        //g2d.drawRect(position.x, position.y, this.img.getWidth(), this.img.getHeight());
    }

}
