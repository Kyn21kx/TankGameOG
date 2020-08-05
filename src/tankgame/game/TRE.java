/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.game;


import java.applet.Applet;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.applet.AudioClip;
import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private BufferedImage world;
    AudioClip clip;
    private BufferedImage ground;
    public static BufferedImage bullet, explosion, walls, breakWalls, miniWall, miniTank, heart, heart2;
    public static boolean gameOver = false;
    private static long powerUpTimer = 0;
    public static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    private Tank t1, t2;
    private Vector2 spawnPos1 = new Vector2(30, 30), spawnPos2 = new Vector2(936, 688);
    public static ArrayList<Wall> listWalls = new ArrayList<Wall>();
    BufferedImage t1img = null;
    BufferedImage t2img = null;
    private Launcher lf;
    public static Tank winner = null;
    public static long tick = 0;

    public TRE(Launcher lf){
        this.lf = lf;
    }

    @Override
    public void run(){
       try {
           this.resetGame();
           while (true) {
                this.tick++;

                for (int i = 0; i < listWalls.size(); i++) {
                    listWalls.get(i).Update(t1,t2);
                    if (listWalls.get(i).isBreakWall())
                        listWalls.remove(listWalls.get(i));
                }

                SpawnPowerUp((int)(Math.random() * (3000 - 1300)) + 1300);

                for (int i = 0; i < powerUps.size(); i++) {
                       if (powerUps.get(i).Attach(t1,t2))
                           powerUps.remove(powerUps.get(i));
                }

                this.t1.update(); // update tank
                this.t2.update(); // update tank
               if (gameOver) {
                   this.lf.setFrame("end");
                   return;
               }
                this.repaint();   // redraw game
                Thread.sleep(1000 / 144); //sleep for a few milliseconds
            }
       } catch (InterruptedException ignored) {
           System.out.println(ignored);
       }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        gameOver = false;
        winner = null;
        this.tick = 0;
        listWalls.clear();
        powerUps.clear();
        WallsInitialization();
        //Replace this with a transform.position / vector like component
        this.t1.Revive(spawnPos1, 0);
        this.t2.Revive(spawnPos2, -180);
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                                       GameConstants.GAME_SCREEN_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);


        try {
            t1img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
            t2img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank2.png")));
            bullet = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Shell.png")));
            explosion = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Explosion_small.png")));
            walls = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall1.gif")));
            breakWalls = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall2.gif")));
            ground = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.jpg")));
            miniWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("WallMiniMap.jpg")));
            miniTank = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("MiniTank.jpg")));
            heart = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Heart.png")));
            heart2 = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Heart.png")));
            clip = Applet.newAudioClip(TRE.class.getClassLoader().getResource("Music.wav"));
            clip.loop();

        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        TanksInitialization();
        WallsInitialization();
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_F);
        this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        DrawGround(buffer);
        for (Wall wall : listWalls) {
            wall.DrawImage(buffer);
        }
        for (PowerUp p : powerUps) {
            p.DrawImage(buffer);
        }
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        //buffer.setColor(Color.BLACK);
        //buffer.fillRect(30, 590, 40 * 5, 610 * 5);
        int camera1Pos = t1.position.x + 80;
        int camera2Pos = t2.position.x + 80;
        int directionalDrawing = camera1Pos, directionalDrawing2 = camera2Pos;
        //Clamp X
        if (camera1Pos > GameConstants.GAME_SCREEN_WIDTH / 2) {
            if (t1.position.x <= 432) {
                camera1Pos *= (GameConstants.GAME_SCREEN_WIDTH);
            }
            else {
                camera1Pos = t1.position.x + 80;
                directionalDrawing -= camera1Pos - (GameConstants.GAME_SCREEN_WIDTH /2);
            }
        }
        else {
            camera1Pos = t1.position.x -30;
            directionalDrawing = camera1Pos / 4;
        }
        if (camera2Pos > GameConstants.GAME_SCREEN_WIDTH / 2) {
            if (t2.position.x <= 432) {
                camera2Pos *= (GameConstants.GAME_SCREEN_WIDTH);
            }
            else {
                camera2Pos = t2.position.x + 80;
                directionalDrawing2 -= camera2Pos - (GameConstants.GAME_SCREEN_WIDTH / 2);
            }
        }
        else {
            camera2Pos = t2.position.x - 30;
            directionalDrawing2 = camera2Pos / 4;
        }
        //buffer.drawImage(heart, 5 + camera1Pos.x, 560, 240 , 200, null);
        //DrawMiniMap(buffer, Vector2.Add(new Vector2(28 * 5, 590 * 5), Vector2.Mult(camera1Pos, 5)));
        DrawPowerUp(buffer, t1);
        DrawPowerUp(buffer, t2);
        if (winner != null) {
            buffer.setFont(new Font("TimesRoman", Font.ITALIC, 24));
            buffer.setColor(Color.black);
            buffer.drawString("Winner!", winner.position.x, winner.position.y);
        }
        DrawLives(buffer, t1);
        DrawLives(buffer, t2);
        buffer.scale(0.01, 0.01);
        buffer.drawImage(heart, t1.position.x * 100, (t1.position.y - 60) * 100, null);
        buffer.drawImage(heart, t2.position.x * 100, (t2.position.y - 60) * 100, null);
        buffer.scale(1, 1);
        BufferedImage firstHalf = world.getSubimage(directionalDrawing,0, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage secondHalf = world.getSubimage(directionalDrawing2,0, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage miniMap = world.getSubimage(0,0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(firstHalf,0,0,null);
        g2.drawImage(secondHalf,GameConstants.GAME_SCREEN_WIDTH/2 + 2,0,null);
        g2.scale(0.2, 0.2);
        g2.drawImage(miniMap,(28 * 5), 590 * 5,null);
        //g2.drawImage(secondHalf,0,0,null);
    }

    private void TanksInitialization () {
        t1 = new Tank(spawnPos1, Vector2.Zero(), 0, t1img, bullet);
        t2 = new Tank(spawnPos2, Vector2.Zero(), -180, t2img, bullet);
        t1.explosionBasic = explosion;
        t2.explosionBasic = explosion;
        t1.enemy = t2;
        t2.enemy = t1;
        t1.name = "Player #1";
        t2.name = "Player #2";
        t1.shooting = Applet.newAudioClip(TRE.class.getClassLoader().getResource("Explosion_small.wav"));
        t2.shooting = Applet.newAudioClip(TRE.class.getClassLoader().getResource("Explosion_small.wav"));
    }

    public static void WallsInitialization () {
        //TODO: Make sure tiles do not overlap // Do rand vertical
        boolean nextIsBreakable = false;
        int n = (int)(Math.random() * (30 - 12)) + 12;
        //Main wall
        for (int i = 0; i < n; i++) {
            Vector2 randPos = Vector2.Rand(30, 900 , 30, 700);
            //Check if there's another wall too close
            if (CloseToWall(randPos))
                randPos.IncreaseBy((int)(Math.random() * (4 - 2)) + 2);
            listWalls.add(new Wall(randPos , walls, breakWalls));//First block
            if (new Random().nextBoolean()) {
                //Horizontal walls
                for (int j = 0; j < (int)(Math.random() * (10 - 1)) + 1; j++) {
                    Vector2 nextPos = new Vector2(randPos.x + walls.getWidth(), randPos.y);
                    listWalls.add(new Wall(nextPos , walls, breakWalls));
                    randPos = nextPos;
                }
            }
            else {
                //Vertical walls
                for (int j = 0; j < (int)(Math.random() * (10 - 1)) + 1; j++) {
                    Vector2 nextPos = new Vector2(randPos.x, randPos.y + walls.getHeight());
                    listWalls.add(new Wall(nextPos , walls, breakWalls));
                    randPos = nextPos;
                }
            }

        }
    }


    private void DrawGround (Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(0, 0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.ground, rotation, null);
    }

    private static boolean CloseToWall (Vector2 pos) {
        for (Wall wall: listWalls) {
            float threshold = (int)(Math.random() * (10 - 4)) + 4;
            if (Vector2.Distance(wall.position, pos) < threshold)
                return true;
        }
        return false;
    }

    private static void SpawnPowerUp(int hearts) {
        if (powerUpTimer == 0) {
            powerUpTimer = tick;
        }
        if (tick - powerUpTimer >= hearts) {
            PowerUp p = new PowerUp(Vector2.Rand(30, 600));
            p.earthBending = Applet.newAudioClip(TRE.class.getClassLoader().getResource("EarthBending.wav"));
            p.shield = Applet.newAudioClip(TRE.class.getClassLoader().getResource("Shield.wav"));
            p.pickup = Applet.newAudioClip(TRE.class.getClassLoader().getResource("Pickup.wav"));
            powerUps.add(p);
            powerUpTimer = 0;
        }
    }

    private void DrawPowerUp (Graphics g, Tank t) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
        g2d.setColor(Color.cyan);
        String indicator = "";
        if (t.getActivePowerUp() != null)
            indicator += t.getActivePowerUp();
        indicator = t.getPowerTimer() != 0  ? "ACTIVATED: " + t.getActivePowerUp() : indicator;
        g2d.drawString(indicator, t.position.x - 10, t.position.y + 30);
    }

    private void DrawWinner (Graphics g, Vector2 pos) {

    }


    private void DrawLives (Graphics g, Tank t) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        g2d.drawString("x" + t.getLives(), t.position.x + 35, t.position.y - 40);
    }

}
