package org.cis120.snake;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A game object displayed using an image.
 *
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This is important for efficiency: your program will go very
 * slowly if you try to create a new BufferedImage every time the draw method is
 * invoked.
 */
public class SpeedBlock extends EdibleBlock {
    public static final String IMG_FILE = "files/star.png";
    public static final int SIZE = 20;
    public static final int INIT_POS_X = GameCourt.COURT_WIDTH-(Snake.SIZE / 2);
    public static final int INIT_POS_Y = GameCourt.COURT_HEIGHT-(Snake.SIZE / 2);
    public static final int INIT_VEL = 0;

    private static BufferedImage img;

    public SpeedBlock(int courtWidth, int courtHeight) {
        super(INIT_VEL, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void eatInteraction(Snake snake) {
        snake.increaseSpeed();
    }
}
