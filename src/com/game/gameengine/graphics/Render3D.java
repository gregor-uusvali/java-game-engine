package com.game.gameengine.graphics;

import com.game.gameengine.Game;

import java.util.Random;

import static com.game.gameengine.input.Controls.bopHeight;
import static com.game.gameengine.input.Controls.walkBop;

public class Render3D extends Render {

    public double[] zBuffer;
    public double renderDistance = 5000.0;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void floor(Game game) {

        double floorPosition = 8;
        double ceilingPosition = 80;
        double forward = game.controller.z; // forward and right demo game.time / 10.0;
        double right = game.controller.x;
        double up = game.controller.y;
        double walking = Math.sin(game.time / 6.0) * bopHeight;

        double rotation = game.controller.rotation;  // rotation demo game.time / 100.0;
        double cosine = Math.cos(rotation);
        double sin = Math.sin(rotation);


        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;

            double z = (floorPosition + up + (walkBop ? walking : 0)) / ceiling;

            if (ceiling < 0) {
                z = (ceilingPosition - up - (walkBop ? walking : 0)) / -ceiling;
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sin;
                double yy = z * cosine - depth * sin;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
//                pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
//                if (z < 400) pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
            }
        }
        Random random = new Random(100);
        for (int i = 0; i < 10000; i++) {
            double xx = random.nextDouble();
            double yy = random.nextDouble();
            double zz = 2;

            int xPixel = (int) (xx / zz * height / 2 + width / 2);
            int yPixel = (int) (yy / zz * height / 2 + height / 2);
            if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
                pixels[xPixel + yPixel * width] = 0xfffff;
            }
        }

    }

    public void renderDistanceLimiter() {
        for (int i = 0; i < width * height; i++) {
//            int color = pixels[i];
            int brightness = (int) (renderDistance / (zBuffer[i]));

            if (brightness < 0) brightness = 0;
            if (brightness > 255) brightness = 255;

            int r = (pixels[i] >> 16) & 0xff;
            int g = (pixels[i] >> 8) & 0xff;
            int b = pixels[i] & 0xff;

            r = r * brightness >>> 8;
            g = g * brightness >>> 8;
            b = b * brightness >>> 8;

            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}
