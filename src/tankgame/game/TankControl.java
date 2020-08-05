/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.game;


import tankgame.GameConstants;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author anthony-pc
 */
public class TankControl implements KeyListener {

    private Tank tank;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;
    private final int power;

    public TankControl(Tank t, int up, int down, int left, int right, int shoot, int power) {
        tank = t;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
        this.power = power;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            tank.toggleUpPressed();
        }
        if (keyPressed == down) {
            tank.toggleDownPressed();
        }
        if (keyPressed == left) {
            tank.toggleLeftPressed();
        }
        if (keyPressed == right) {
            tank.toggleRightPressed();
        }
        //Make sure I limit this for one time
        if (keyPressed == shoot) {
            tank.Shoot();
        }

        if (keyPressed == power) {
            tank.SetTriggerPowerUp();
        }

        GameConstants.keyPressed = true;

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased  == up) {
            tank.unToggleUpPressed();
        }
        if (keyReleased == down) {
            tank.unToggleDownPressed();
        }
        if (keyReleased  == left) {
            tank.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            tank.unToggleRightPressed();
        }
        GameConstants.keyPressed = false;
    }
}
