package com.game.gameengine;

import com.game.gameengine.graphics.Render;
import com.game.gameengine.graphics.Screen;
import com.game.gameengine.input.Controls;
import com.game.gameengine.input.InputHandler;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.sql.SQLOutput;

public class Display extends Canvas implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Engine";

    private Thread thread;
    private boolean running = false;
    private Screen screen;
    private BufferedImage img;
    private int[] pixels;
    private int fps;
    private Game game;
    private InputHandler input;

    private int prevX = 0;
    private int prevY = 0;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        game = new Game();
        screen = new Screen(WIDTH, HEIGHT);
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public static void main(String[] args) {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setCursor(blank);
        frame.setTitle(TITLE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        System.out.println("Running...");

        game.start();
    }

    public synchronized void run() {
        prevX = InputHandler.MouseX;
        int frames = 0;
        double unprocessedSeconds = 0;
        long prevTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        prevX = InputHandler.MouseX;
        prevY = InputHandler.MouseY;
        int xDirection = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - prevTime;
            prevTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;

                if (tickCount % 60 == 0) {
                    prevTime += 1000;
                    fps = frames;
                    frames = 0;
                }
            }

            if (ticked) {
                render();
                frames++;
            }


            render();
            frames++;
            if(InputHandler.MouseX > prevX) {
                System.out.println("right");
                Controls.turnRight = true;
                Controls.turnLeft = false;
            } else if (InputHandler.MouseX < prevX) {
                System.out.println("left");
                Controls.turnLeft = true;
                Controls.turnRight = false;
            } else {
                System.out.println("not moving");
                Controls.turnLeft = false;
                Controls.turnRight = false;


            }
            prevX = InputHandler.MouseX;
            prevY = InputHandler.MouseY;
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        g.setColor(Color.CYAN);
        g.drawString(fps + " FPS", 5, 15);
        g.dispose();
        bs.show();
    }

    private void tick() {
        game.tick(input.key);
    }

    private void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
