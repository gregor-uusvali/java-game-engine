package com.game.gameengine.graphics;

import com.game.gameengine.Game;

import java.util.Random;

public class Screen extends Render {

    private Render test;
    private Random random = new Random();

    public Screen(int width, int height) {
        super(width, height);
        test = new Render(256, 256);
        for (int i = 0; i < 256 * 256; i++) {
//            test.pixels[i] = random.nextInt();
//            alpha - transparent pixles
            test.pixels[i] = random.nextInt() * (random.nextInt(5)/4);
        }
    }

    public void render(Game game) {
//        Noise
//        for (int i = 0; i < 256 * 256; i++) {
//            test.pixels[i] = random.nextInt();
//        }
        for (int i = 0; i < width * height; i++) {
            pixels[i] = 0;
        }
        for (int i = 0; i < 30; i++) {
            int anim = (int) (Math.sin((game.time + i * 4) % 1000.0 / 100) * 100);
            int anim2 = (int) (Math.cos((game.time + i * 4) % 1000.0 / 100) * 100);
            draw(test, (width - 256) / 2 + anim, (height - 256) / 2 - anim2);
        }
    }
}
