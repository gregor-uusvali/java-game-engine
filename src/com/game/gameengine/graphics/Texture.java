package com.game.gameengine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class Texture {
    public static Render floor = loadBitmap("/textures/floor.png");

    public static Render loadBitmap(String file) {
        try {
//            BufferedImage image = ImageIO.read((new FileInputStream(file)));
            BufferedImage image = ImageIO.read(Texture.class.getResource(file));
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
