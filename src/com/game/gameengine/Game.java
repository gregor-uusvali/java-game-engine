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
        boolean turnLeft = key[KeyEvent.VK_LEFT];
        boolean turnRight = key[KeyEvent.VK_RIGHT];

        controller.tick(forward, back, left, right, turnLeft, turnRight);
    }
}
