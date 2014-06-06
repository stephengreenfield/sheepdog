package com.mindsoon.sheepdog;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Sheepdog extends JPanel implements ActionListener, KeyListener {
    enum gameState { startGAME, playGAME, endGAME, endLEVEL, sinkUPDATE }
    static gameState theState;

    public Sheepdog() {
        Timer t = new Timer(5, this);
        t.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(Ocean.seaColors[0]);
    }

    //associate actions with keys
    public void keyPressed(KeyEvent e) {
        String dir=null;
        Dog.lastDirection = e.getKeyCode();
        switch (Dog.lastDirection) {
            case KeyEvent.VK_NUMPAD1: dir="dl"; break;
            case KeyEvent.VK_NUMPAD3: dir="dr"; break;
            case KeyEvent.VK_NUMPAD7: dir="ul"; break;
            case KeyEvent.VK_NUMPAD9: dir="ur"; break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_NUMPAD2: dir="d"; break;
            case KeyEvent.VK_UP: case KeyEvent.VK_NUMPAD8: dir="u"; break;
            case KeyEvent.VK_LEFT: case KeyEvent.VK_NUMPAD4: dir="l"; break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_NUMPAD6: dir="r"; break;
            case KeyEvent.VK_NUMPAD5: dir="x"; break;
            case KeyEvent.VK_X: System.exit(0); break;
            case KeyEvent.VK_SPACE: Visuals.fadeCount++; break;
        }
        if ( ( dir != null ) && ( theState==gameState.playGAME ) ) { Dog.moveDog (dir.toCharArray() ); }
    }

    //mandatory key-related functions left blank
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    //depending on gameState, call different draw functions
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (theState) {
            case playGAME:
                Visuals.drawGame(g);
                break;
            case endGAME:
                Visuals.drawGameEnd(g);
                break;
            case endLEVEL:
                Visuals.drawEndLevel(g);
                break;
            case startGAME:
                Visuals.drawStartingScreen(g);
                break;
            case sinkUPDATE:
                Visuals.drawSinkingUpdate(g);
                break;
        }
    }

    //automatic update screen when something happens
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame("sheepdog - bitch of the archipelago");
        Sheepdog s = new Sheepdog();
        Visuals.startGame();
        f.add(s);
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(966,668);
        f.setVisible(true);
        f.setResizable(false);
    }
}