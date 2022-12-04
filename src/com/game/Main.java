package com.game;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(400, 400);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        Game game = new Game();
        Scanner in = new Scanner(System.in);
        String mode = in.next();
        System.out.println(mode);
        if (mode.equals("computer")) {
            game.selectMode(true);
        }
        if (mode.equals("player")) {
            game.selectMode(false);
        } else {
            System.out.println("Wrong_value");
        }
        window.add(game);

    }
}