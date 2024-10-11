package com.game.gameengine.graphics;

import com.game.gameengine.Game;

import static com.game.gameengine.input.Controls.bopHeight;
import static com.game.gameengine.input.Controls.walkBop;

public class Render3D extends Render {

    public double[] zBuffer;
    public double renderDistance = 5000.0;
    private double forward, side, up, cosine, sine, walking;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void floor(Game game) {

        double floorPosition = 8;
        double ceilingPosition = 8;
        forward = game.controller.z; // forward and right demo game.time / 10.0;
        side = game.controller.x;
        up = game.controller.y;
        walking = Math.sin(game.time / 6.0) * bopHeight;

        double rotation = game.controller.rotation;
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);

        for (int y = 0; y < height; y++) {
            double ceiling = (y + -height / 2.0) / height;

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
                pixels[x + y * width] = Texture.tex.pixels[(xPix & 7) + (yPix & 7) * 16];
//                pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
//                if (z < 400) pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
            }
        }
    }

    public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
        double upCorrect = 0.0625;
        double sideCorrect = 0.0625;
        double forwardCorrect = 0.0625;
        double walkCorrect = -0.0625;


        double xcLeft = ((xLeft) - (side * sideCorrect)) * 2;
        double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTopLeft = ((-yHeight) - (-up * upCorrect + (walkBop ? walking * walkCorrect : 0))) * 2;
        double yCornerBottomLeft = ((+0.5 - yHeight) - (-up * upCorrect + (walkBop ? walking * walkCorrect : 0))) * 2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = ((xRight) - (side * sideCorrect)) * 2;
        double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTopRight = ((-yHeight) - (-up * upCorrect + (walkBop ? walking * walkCorrect : 0))) * 2;
        double yCornerBottomRight = ((+0.5 - yHeight) - (-up * upCorrect + (walkBop ? walking * walkCorrect : 0))) * 2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

        double texture30 = 0;
        double texture40 = 8;
        double clip = 0.5;

        if(rotLeftSideZ < clip && rotRightSideZ < clip) return;

        if (rotLeftSideZ < clip) {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            texture30 = texture30 + (texture40 - texture30) * clip0;

        }

        if (rotRightSideZ < clip) {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            texture30 = texture30 + (texture40 - texture30) * clip0;

        }

        if (xPixelLeft >= xPixelRight) return;

        int xPixelLeftInt = (int) xPixelLeft;
        int xPixelRightInt = (int) xPixelRight;

        if (xPixelLeftInt < 0) xPixelLeftInt = 0;
        if (xPixelRightInt > width) xPixelRightInt = width;

        double yPixelLeftTop = (yCornerTopLeft / rotLeftSideZ * height + height / 2.0);
        double yPixelLeftBottom = (yCornerBottomLeft / rotLeftSideZ * height + height / 2.0);
        double yPixelRightTop = (yCornerTopRight / rotRightSideZ * height + height / 2.0);
        double yPixelRightBottom = (yCornerBottomRight / rotRightSideZ * height + height / 2.0);

        double texture1 = 1 / rotLeftSideZ;
        double texture2 = 1 / rotRightSideZ;
        double texture3 = texture30 / rotLeftSideZ;
        double texture4 = texture40 / rotRightSideZ - texture3;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

            int xTexture = (int) ((texture3 + texture4 * pixelRotation) / (texture1 + (texture2 - texture1) * pixelRotation));

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if (yPixelTopInt < 0) yPixelTopInt = 0;
            if (yPixelBottomInt > height) yPixelBottomInt = height;

            for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
//                pixels[x + y * width] = 0xff5733;
                double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                int yTexture = (int) (8 * pixelRotationY);
//                pixels[x + y * width] = xTexture * 100 + yTexture * 100;
                pixels[x + y * width] = Texture.tex.pixels[((xTexture & 7) + 8) + (yTexture & 7) * 16];
                zBuffer[x + y * width] = 1 / (texture1 + (texture2 - texture1) * pixelRotation) * 8;
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
