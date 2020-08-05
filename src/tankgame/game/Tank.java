package tankgame.game;


import tankgame.Collision;
import tankgame.GameConstants;
import tankgame.Ray;
import tankgame.Vector2;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


/**
 *
 * @author anthony-pc
 */
public class Tank {
    //TODO: Make the tank disappear and finish the game once one of the tanks reaches 0 health // Make the game look prettier
    // Maybe change the health to a float for the health bars and have the lives variable go down when they hit 0 health
    // Add a timer class

    public Vector2 position;
    private Vector2 velocity;
    private Rectangle hitBox;


    private PowerUp activePowerUp;
    public boolean canMove = true;
    private long powerTimer = 0;
    private boolean holdingPowerUp = false;
    public String name;
    public boolean alive = true;
    private float angle;
    public Tank enemy;
    private int health = 5;
    private int lives = 3;
    public int ammo = 10;
    private int auxAmmo = ammo;
    private long coolDownFrameCount = 0;
    private long fxFrameCount = 0;
    public boolean reloading = false;
    public boolean shotOnCD = false;
    private boolean drawingEffect = false;
    private final int R = 2;
    private final float ROTATIONSPEED = 3.0f;

    private Vector2 fxPos = new Vector2();
    public Graphics2D drawingGraphics;
    public ArrayList<Ray> bullets = new ArrayList<Ray>();



    public AudioClip shooting;
    private BufferedImage img;
    private BufferedImage bulletImg;
    public BufferedImage explosionBasic;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;


    Tank(Vector2 pos, Vector2 vel, int angle, BufferedImage img, BufferedImage bulletImg) {
        position = pos;
        this.velocity = vel;
        this.img = img;
        this.angle = angle;
        this.bulletImg = bulletImg;
        hitBox = new Rectangle(position.x, position.y, this.img.getWidth(), this.img.getHeight());
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public long getPowerTimer() {
        return powerTimer;
    }

    public int getLives() {
        return lives;
    }

    void setActivePowerUp (PowerUp p) {
        if (!holdingPowerUp) {
            holdingPowerUp = true;
            activePowerUp = p;
        }
    }

    public void SetTriggerPowerUp () {
        if (holdingPowerUp) {
            powerTimer = TRE.tick;
            if (activePowerUp.powerUpType == PowerUp.Ptype.Invincibility)
                activePowerUp.shield.play();
        }
        else
            powerTimer = 0;
    }

    public PowerUp getActivePowerUp() {
        return activePowerUp;
    }

    private void ReleasePower () {
        if (holdingPowerUp && powerTimer != 0) {
            if (activePowerUp.ExecutedPowerUp()) {
                activePowerUp = null;
                holdingPowerUp = false;
                powerTimer = 0;
                return;
            }
            if (TRE.tick - powerTimer >= activePowerUp.frames) {
                activePowerUp = null;
                holdingPowerUp = false;
                canMove = true;
                ammo = auxAmmo;
                powerTimer = 0;
            }
        }
    }

    public boolean getHoldingPowerUp () {
        return holdingPowerUp;
    }

    public void Shoot () {
        if (AbleToShoot()) {
            ammo--;
            bullets.add(new Ray(this.position, this.angle, this.bulletImg));
        }
    }

    void update() {
        if(alive) {
            //Collision.CollidedWith(position, enemy.position);
            Movement();
            //Bullet behaviour
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i) != null) {
                    bullets.get(i).Update();
                    if (bullets.get(i).collided || bullets.get(i).attackCollided) {
                        Vector2 pos = bullets.get(i).position;
                        bullets.remove(bullets.get(i));
                        shooting.play();
                        TriggerDrawExplosion(pos);
                    }
                }
            }
            if (auxAmmo != ammo)
                ShootCD(40);
            if (ammo <= 0)
                Reload(500);
            Damage();
            ReleasePower();
        }
    }

    public Rectangle getHitBox () {
        hitBox.setLocation(position.x, position.y);
        return hitBox.getBounds();
    }

    private void Movement() {
        if (enemy.alive && canMove) {
            if (this.UpPressed) {
                this.moveForwards();
            }
            if (this.DownPressed) {
                this.moveBackwards();
            }
            if (this.LeftPressed) {
                this.rotateLeft();
            }
            if (this.RightPressed) {
                this.rotateRight();
            }
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    public void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        if (CheckAllCollisions())
            velocity.x = -2;
        else
            velocity.x = (int) -Math.round(R * Math.cos(Math.toRadians(angle)));
        velocity.y = (int) -Math.round(R * Math.sin(Math.toRadians(angle)));
        position.MoveTowards(velocity);
        Collision.CheckBorder(position);
    }

    private void moveForwards() {
        if (CheckAllCollisions())
            velocity.y = -2;
        else
            velocity.y = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        velocity.x = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        position.MoveTowards(velocity);
        Collision.CheckBorder(position);
    }

    @Override
    public String toString() {
        return "x=" + position.x + ", y=" + position.y + ", angle=" + angle;
    }



    private void Damage () {
        //Draw the health bar
        for (Ray bullet : enemy.bullets) {
            if (Collision.CollidedWith(bullet.getHitBox(), this.getHitBox())) {
                bullet.attackCollided = true;
                if (activePowerUp == null || activePowerUp.powerUpType != PowerUp.Ptype.Invincibility || powerTimer == 0)
                    health--;
            }
        }
        if (health <= 0) {
            lives--;
            health = 5;
        }
        if (lives <= 0) {
            alive = false;
            GameConstants.keyPressed = false;
        }
    }

    private void Reload (int frames) {
        if (!reloading) {
            //Code is being executed for the first time
            coolDownFrameCount = TRE.tick;
            reloading = true;
        }
        long difference = TRE.tick - coolDownFrameCount;
        //Check if the difference equals 0
        //Abs value of the difference - frames gives you the cooldown
         if (difference >= frames) {
             ammo = 10;
             reloading = false;
             shotOnCD = false;
         }
    }
    private void ShootCD (int frames) {
        if (!shotOnCD) {
            //Code is being executed for the first time
            coolDownFrameCount = TRE.tick;
            shotOnCD = true;
        }
        long difference = TRE.tick - coolDownFrameCount;
        //Check if the difference equals 0
        //Abs value of the difference - frames gives you the cooldown
        if (difference >= frames) {
            auxAmmo = ammo;
            shotOnCD = false;
        }
    }

    private boolean AbleToShoot () {
        return ammo != 0 && !reloading && !shotOnCD && enemy.alive;
    }

    void drawImage(Graphics g) {
        if (alive) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(position.x, position.y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
            Graphics2D g2d = (Graphics2D) g;
            drawingGraphics = g2d;
            for (Ray bullet : enemy.bullets) {
                if (bullet != null) {
                    bullet.DrawImg(g);
                }
            }
            g2d.drawImage(this.img, rotation, null);
            //g2d.drawRect(position.x, position.y, this.img.getWidth(), this.img.getHeight());
            //Change for a health bar
            g2d.setColor(Color.red);
            g2d.fillRect(position.x, position.y - 20, 5 * 12, 5);
            g2d.setColor(Color.green);
            g2d.fillRect(position.x, position.y - 20, health * 12, 5);
            g2d.setFont(new Font("TimesRoman", Font.ITALIC, 16));
            if (reloading)
                g2d.drawString("Reloading...", position.x, position.y + 40);
        }
        else {
            GameOver();
        }
        DrawFX();

    }

    private void GameOver () {
        TRE.winner = enemy;
        if (GameConstants.keyPressed)
            TRE.gameOver = true;
    }

    private void TriggerDrawExplosion(Vector2 pos) {
        fxPos = pos;
        drawingEffect = true;
        fxFrameCount = TRE.tick;
    }

    private void DrawFX () {
        if (drawingEffect) {
            //Explosion
            //Add timer
            if (TRE.tick - fxFrameCount < 20)
                drawingGraphics.drawImage(explosionBasic, fxPos.x, fxPos.y, null);
            else {
                drawingEffect = false;
            }
        }
    }

    private boolean  CheckAllCollisions () {
        boolean wallCollision = false;
        for (Wall wall : TRE.listWalls) {
            if (wall != null) {
                if (Collision.CollidedWith(this.getHitBox(), wall.getHitBox())) {
                    wallCollision = true;
                    break;
                }
            }
            else break;

        }
        return Collision.CollidedWith(enemy.getHitBox(), this.getHitBox()) || wallCollision;
    }

    public void Revive (Vector2 pos, int angle) {
        this.drawingEffect = false;
        this.alive = true;
        this.position = pos;
        this.lives = 3;
        this.health = 5;
        this.ammo = auxAmmo;
        this.reloading = false;
        this.shotOnCD = false;
        this.angle = angle;
        this.powerTimer = 0;
        this.holdingPowerUp = false;
        this.activePowerUp = null;
        this.canMove = true;
        bullets.clear();
    }

}
