package com.game.gameengine.input;

import com.game.gameengine.Display;

public class Controls {
    public double x, y, z, rotation, x_a, z_a, rotation_a;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean walkBop = false;
    public static double bopHeight = 0.3;

    public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
        double rotationSpeed = 0.002  * Display.MouseSpeed;
        double walkSpeed = 0.5;
        double jumpHeight = 0.5;
        double crouchHeight = 0.33;
        double xMove = 0;
        double zMove = 0;

        if (forward) zMove++;
        if (back) zMove--;
        if (left) xMove--;
        if (right) xMove++;
        if (forward || back || left || right || sprint || crouch) {
            bopHeight = 0.3;
            walkBop = true;
        } else {
            walkBop = false;
        }
        if (turnLeft) rotation_a -= rotationSpeed;
        if (turnRight) rotation_a += rotationSpeed;
        if (jump) {
            y += jumpHeight;
            sprint = false;
            walkBop = false;
        }
        if (crouch) {
            y -= crouchHeight;
            bopHeight = ((forward || back || left || right) && crouch) ? 0.2 : 0;
            walkSpeed -= 0.3;
            sprint = false;
        }
        if (sprint) {
            walkSpeed += 0.5;
            bopHeight = 0.7;
        }

        x_a = (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        z_a = (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += x_a;
        y *= 0.9;
        z += z_a;
        x_a *= 0.1;
        z_a *= 0.1;
        rotation += rotation_a;
        rotation_a *= 0.5;


    }
}
