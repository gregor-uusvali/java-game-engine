package com.game.gameengine.graphics;

import com.game.gameengine.Game;

import java.awt.image.PixelGrabber;
import java.util.Random;

import static com.game.gameengine.input.Controls.bopHeight;
import static com.game.gameengine.input.Controls.walkBop;

public class Render3D extends Render {

    public double[] zBuffer;
    public double renderDistance = 5000.0;
    private double forward, side, up, cosine, sine;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void floor(Game game) {

        double floorPosition = 8;
        double ceilingPosition = 80;
        forward = game.controller.z; // forward and right demo game.time / 10.0;
        side = game.controller.x;
        up = game.controller.y;
        double walking = Math.sin(game.time / 6.0) * bopHeight;

        double rotation = Math.sin(game.time /40.0) * 0.5;//game.controller.rotation;
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);


        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;

            double z = (floorPosition + up + (walkBop ? walking : 0)) / ceiling;

            if (ceiling < 0) {
                z = (ceilingPosition - up - (walkBop ? walking : 0)) / -ceiling;
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + side);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
//                pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
//                if (z < 400) pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
            }
        }
    }

    public void rednerWall(double xLeft, double xRight, double zDistance, double yHeight) {
        double xcLeft = ((xLeft) - side) * 2;
        double zcLeft = ((zDistance) - forward) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTopLeft = ((-yHeight) - up) * 2;
        double yCornerBottomLeft = ((+0.5 - yHeight) - up) * 2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = ((xRight) - side) * 2;
        double zcRight = ((zDistance) - forward) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTopRight = ((-yHeight) - up) * 2;
        double yCornerBottomRight = ((+0.5 - yHeight) - up) * 2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);


        int xPixelLeftInt = (int) xPixelLeft;
        int xPixelRightInt = (int) xPixelRight;
        if (xPixelLeftInt >= xPixelRightInt) return;

        if (xPixelLeftInt < 0) xPixelLeftInt = 0;
        if (xPixelRightInt > width) xPixelRightInt = width;

        double yPixelLeftTop = (int) (yCornerTopLeft / rotLeftSideZ * height + height / 2);
        double yPixelLeftBottom = (int) (yCornerBottomLeft / rotLeftSideZ * height + height / 2);
        double yPixelRightTop = (int) (yCornerTopRight / rotRightSideZ * height + height / 2);
        double yPixelRightBottom = (int) (yCornerBottomRight / rotRightSideZ * height + height / 2);

        for (int x = xPixelLeftInt - 24; x < xPixelRightInt; x++) {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if (yPixelTopInt < 0) yPixelTopInt = 0;
            if (yPixelTopInt > height) yPixelTopInt = height;

            for (int y = yPixelTopInt; y <= yPixelBottomInt; y++) {
                pixels[x + y * width] = 0xff5733;
                zBuffer[x + y * width] = 0;
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
