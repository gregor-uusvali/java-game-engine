package com.game.gameengine.graphics;

public class Render3D extends Render {
    public Render3D(int width, int height) {
        super(width, height);
    }

    double time = 0.0;


    public void floor() {
        for (int y = 0; y < height; y++) {
            double yDepth = (y - height / 2.0) / height;

            if (yDepth < 0) {
                yDepth = -yDepth;
            }


            double z = 8 / yDepth;
            time += 0.00025;
            for (int x = 0; x < width; x++) {
                double xDepth = (x - width / 2.0) / height;
                xDepth *= z;
                double xx = xDepth - time;
                double yy = z;
                int xPix = (int) xx;
                int yPix = (int) yy;
                pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
            }
        }
    }
}
