package com.game.gameengine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.game.gameengine.Display.TITLE;

public class RunGame {
    public RunGame() {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setCursor(blank);
        frame.setTitle(TITLE);

        GraphicsEnvironment graphicsenvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        System.out.println("Running...");

        game.start();
    }
}

