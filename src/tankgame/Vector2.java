package tankgame;

import java.util.Random;

public class Vector2 {

    public int x, y;

    public Vector2 () {
        x = 0;
        y = 0;
    }

    public Vector2 (int _x, int _y) {
        x = _x;
        y = _y;
    }


    public void MoveTowards(Vector2 dir) {
        x += dir.x;
        y += dir.y;
    }

    //Returns the distance between two vectors (simplified by a factor of 1/10)
    public static float Distance (Vector2 from, Vector2 to) {
        float p1 = (float)Math.pow(to.x - from.x, 2);
        float p2 = (float)Math.pow(to.y - from.y, 2);
        return (float)Math.sqrt(p1 + p2) / 10;
    }

    public void IncreaseBy (int factor) {
        x *= factor;
        y *= factor;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }


    public static Vector2 Zero () {
        return new Vector2();
    }
    public static Vector2 Rand (int min, int max) {return new Vector2((int)(Math.random() * (max - min)) + min, (int)(Math.random() * (max - min)) + min);}
    public static Vector2 Rand (int minX, int maxX, int minY, int maxY) {return new Vector2((int)(Math.random() * (maxX - minX)) + minX, (int)(Math.random() * (maxY - minY)) + minY);}
    public static Vector2 Add (Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

}
