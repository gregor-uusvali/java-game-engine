package com.game.gameengine.input;

public class Controls {
    public double x, z, rotation, x_a, z_a, rotation_a;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;

    public void tick(boolean forward, boolean back, boolean left, boolean right) {
        double rotationSpeed = 0.025;
        double walkSpeed = 1;
        double xMove = 0;
        double zMove = 0;

        if (forward) zMove++;
        if (back) zMove--;
        if (left) xMove--;
        if (right) xMove++;
        if (turnLeft) rotation_a -= rotationSpeed;
        if (turnRight) rotation_a += rotationSpeed;

        x_a = (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        z_a = (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += x_a;
        z += z_a;
        x_a *= 0.1;
        z_a *= 0.1;
        rotation += rotation_a;
        rotation_a *= 0.5;


    }
}
