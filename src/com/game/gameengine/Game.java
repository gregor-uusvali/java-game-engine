package com.game.gameengine;

import com.game.gameengine.input.Controls;
import com.game.gameengine.level.Level;

import java.awt.event.KeyEvent;

public class Game {
    public int time;
    public Controls controller;
    public Level level;

    public Game() {
        controller = new Controls();
        level = new Level(20, 20);

    }

    public void tick(boolean[] key) {
        time++;
        boolean forward = key[KeyEvent.VK_W];
        boolean back = key[KeyEvent.VK_S];
        boolean left = key[KeyEvent.VK_A];
        boolean right = key[KeyEvent.VK_D];
        boolean jump = key[KeyEvent.VK_SPACE];
        boolean crouch = key[KeyEvent.VK_CONTROL];
        boolean sprint = key[KeyEvent.VK_SHIFT];

        controller.tick(forward, back, left, right, jump, crouch, sprint);
    }
}
