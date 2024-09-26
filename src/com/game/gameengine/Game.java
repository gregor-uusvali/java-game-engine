package com.game.gameengine;

import com.game.gameengine.input.Controls;

import java.awt.event.KeyEvent;

public class Game {
    public int time;
    public Controls controller;

    public Game() {
        controller = new Controls();

    }

    public void tick(boolean[] key) {
        time++;
        boolean forward = key[KeyEvent.VK_W];
        boolean back = key[KeyEvent.VK_S];
        boolean left = key[KeyEvent.VK_A];
        boolean right = key[KeyEvent.VK_D];

        controller.tick(forward, back, left, right);
    }
}
