package com.game.gameengine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel window = new JPanel();
    private JButton play, options, help, quit;
    private Rectangle rplay, roptions, rhelp, rquit;

    private int width = 240;
    private int height = 320;

    private int btn_width = 80;
    private int btn_height = 40;

    public Launcher() {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        setTitle("Launcher");
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(window);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        window.setLayout(null);

        drawButtons();
    }

    private void drawButtons() {
        play = new JButton("Play!");
        rplay = new Rectangle(width/2-btn_width/2, 20, btn_width, btn_height);
        play.setBounds(rplay);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RunGame();
            }
        });
        window.add(play);

        options = new JButton("Options");
        roptions = new Rectangle(width/2-btn_width/2, 70, btn_width, btn_height);
        options.setBounds(roptions);
        window.add(options);

        help = new JButton("Help");
        rhelp = new Rectangle(width/2-btn_width/2, 120, btn_width, btn_height);
        help.setBounds(rhelp);
        window.add(help);

        quit = new JButton("Quit");
        rquit = new Rectangle(width/2-btn_width/2, 170, btn_width, btn_height);
        quit.setBounds(rquit);
        window.add(quit);

    }
}
