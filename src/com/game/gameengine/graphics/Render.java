package com.game.gameengine.graphics;

public class Render {
    public final int width;
    public final int height;
    public final int[] pixels;

    public Render(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public void draw(Render render, int offSetX, int offSetY) {
        for (int y = 0; y < render.height; y++) {
            int yPix = y + offSetY;
            for (int x = 0; x < render.width; x++) {
                int xPix = x + offSetX;

                pixels[xPix + yPix * width] = render.pixels[x + y * render.width];
            }
        }
    }
}
