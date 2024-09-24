package com.game.gameengine.graphics;

import com.game.gameengine.Game;

import java.util.Random;

public class Screen extends Render {

    private Render test;
    private Random random = new Random();
    private Render3D render3d;

    public Screen(int width, int height) {
        super(width, height);

        render3d = new Render3D(width, height);

        test = new Render(256, 256);
        for (int i = 0; i < 256 * 256; i++) {
//            test.pixels[i] = random.nextInt();
//            alpha - transparent pixles
            test.pixels[i] = random.nextInt() * (random.nextInt(5)/4);
        }
    }

    public void render(Game game) {
//        testing pixels
//
//        Noise
//        for (int i = 0; i < 256 * 256; i++) {
//            test.pixels[i] = random.nextInt();
//        }
//
//        Remove trailing pixles
//        for (int i = 0; i < width * height; i++) {
//            pixels[i] = 0;
//        }
//
//        for (int i = 0; i < 30; i++) {
//            int anim = (int) (Math.sin((game.time + i * 4) % 1000.0 / 100) * 100);
//            int anim2 = (int) (Math.cos((game.time + i * 4) % 1000.0 / 100) * 100);
//            draw(test, (width - 256) / 2 + anim, (height - 256) / 2 - anim2);
//        }
        render3d.floor(game);
        draw(render3d, 0,0);
    }
}
