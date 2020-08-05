package tankgame;

import tankgame.GameConstants;
import tankgame.Vector2;

import java.awt.*;

public abstract class Collision {
    public static boolean CheckBorder (Vector2 position) {
        boolean collided = false;
        if (position.x < 30) {
            position.x = 30;
            collided = true;
        }
        if (position.x >= GameConstants.GAME_SCREEN_WIDTH - 88) {
            position.x = GameConstants.GAME_SCREEN_WIDTH - 88;
            collided = true;
        }
        if (position.y < 40) {
            position.y = 40;
            collided = true;
        }
        if (position.y >= GameConstants.GAME_SCREEN_HEIGHT - 80) {
            position.y = GameConstants.GAME_SCREEN_HEIGHT - 80;
            collided = true;
        }
        return collided;
    }
    //For the walls, make it so it is bullet.collided = CollidedWith(u, v)
    public static boolean CollidedWith (Rectangle hitBox1, Rectangle other) {
        return (hitBox1.intersects(other));
    }

}

