package com.mindsoon.sheepdog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

abstract class Visuals {
    private static final Font f1 = new Font("Arial", Font.BOLD, 80);
    private static final Font f2 = new Font("Arial", Font.BOLD, 68);
    private static final Font f3 = new Font("Arial", Font.BOLD, 56);
    private static final Font f4 = new Font("Arial", Font.BOLD, 30);
    private static final Font f5 = new Font("Arial", Font.BOLD, 22);
    private static final Font f6 = new Font("Arial", Font.BOLD, 13);
    private static final Color sidebarColor = new Color(70,70,70);
    static int q;
    static int fadeCount;
    private static BufferedImage sheepImage;
    private static BufferedImage dogImage;
    private static BufferedImage boneImage;
    static BufferedImage[] grassImages;

    //set initial gameState and load images for the game
    static void startGame() {
        Sheepdog.theState = Sheepdog.gameState.startGAME;
        loadImages();
    }

    //screen that shows on load
    static void drawStartingScreen(Graphics g) {
        g.drawImage( grassImages[0], 0, 0, 966, 668, null);
        g.setColor( Sheep.sheepColor );
        g.setFont( f1 );
        g.drawString( "SHEEPDOG", 260, 300 );
        g.drawImage( dogImage, 408, 80, 150, 150, null );

        g.setFont( f5 );
        g.setColor( Color.black );
        g.drawString( "You are a sheepdog, loyal and strong. You roam seaside", 180, 380 );
        g.drawString( "grasslands tending to your flock. They live happily under", 180, 410 );
        g.drawString( "your vigilance. Life is good. But something is happening.", 180, 440 );
        g.drawString( "A creature from the ocean depths has awakened.", 180, 470 );
        g.drawString( "And it is hungry...", 180, 500 );

        g.setColor( Ocean.seaColors[0] );
        g.fillRect( 0, 570, 966, 98 );

        g.setFont( f6 );
        g.setColor( Sheep.sheepColor );
        g.drawString( "Move the sheepdog with arrows or number keys.", 330, 600 );
        g.drawString( "Press space to continue or x to quit.", 370, 620 );

        if ( ( fadeCount >= 1 ) && ( Sheepdog.theState == Sheepdog.gameState.startGAME ) ) {
            try {
                Land.newMap();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    //just before level 5, let the user know the land is now sinking...
    static void drawSinkingUpdate(Graphics g) {
        g.drawImage( grassImages[0], 0, 0, 966, 668, null);
        g.drawImage( dogImage, 408, 80, 150, 150, null );
        g.setColor( Sheep.sheepColor );
        g.setFont( f1 );
        g.drawString( "Oh no!", 360, 300 );

        g.setFont( f5 );
        g.setColor( Color.black );
        g.drawString( "The island is starting to sink...", 335, 400 );
        g.drawString( "Save the flock from drowning!", 335, 430 );

        g.setColor( Ocean.seaColors[0] );
        g.fillRect( 0, 570, 966, 98 );

        g.setFont( f6 );
        g.setColor( Sheep.sheepColor );
        g.drawString( "Press space to continue.", 400, 610 );

        if ( ( fadeCount > 72 ) && ( Sheepdog.theState == Sheepdog.gameState.sinkUPDATE ) ) {
            Sheepdog.theState = Sheepdog.gameState.endLEVEL;
        }
    }

    //draw objects during gameplay
    static void drawGame(Graphics g) {
        drawOcean(g);
        drawLand(g);
        drawBones(g);
        drawDog(g);
        drawTentacles(g);
        drawSheep(g);
        drawMenu(g);
    }

    //at end of game, draw final screen
    private static void drawGameFinalSummary(Graphics g) {
        g.drawImage( grassImages[0], 0, 0, 966, 668, null );
        g.drawImage( dogImage, 408, 80, 150, 150, null );
        g.setColor( Ocean.seaColors[0] );
        g.setFont( f4 );
        g.drawString( "You survived "+Land.maxLevels+" tentacle attacks!", 255, 330 );
        g.drawString( "But "+Sheep.totalSheepDead+" sheep died on your watch.", 260, 380 );
        g.drawString( "Still consider yourself a good dog?", 245, 430 );
    }

    //during fade screens, allow for a few milliseconds of delay
    private static void sleep(int ticks) throws InterruptedException {
        Thread.sleep( ticks );
    }

    //final fade effect and also close program. note fadeCount is increased by hitting space bar
    static void drawGameEnd(Graphics g) {
        if ( fadeCount < 250 ) {
            Color fadeColor = new Color( fadeCount, fadeCount, fadeCount );
            g.setColor( fadeColor );
            g.fillRect( 0, 0, 966, 668 );
            fadeCount++;
            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if ( fadeCount == 250 ) {
            drawGameFinalSummary(g);
        }
        else if ( fadeCount > 250 ) {
            g.dispose();
            System.exit(0);
        }
    }

    //draws a grid of icons during the game summary to show level stats
    private static int drawIconBox(Graphics g,
                                   String textString,
                                   int startingBox,
                                   int numberOfBoxes,
                                   int pixelsFromTop,
                                   BufferedImage theImage,
                                   Color colorOfBox,
                                   boolean addToPixelsFromTop) {

        int indentFromLeft = 280, pixelsBetweenSections = 40, pixelsBetweenBoxes = 4;
        g.setFont( f6 );
        g.setColor( Ocean.seaColors[0] );
        g.drawString( textString, indentFromLeft - 80, pixelsFromTop + 18 );

        for ( int i = startingBox; i < numberOfBoxes; i++ ) {
            int x = ( i % 12 ) * ( q + pixelsBetweenBoxes ) + indentFromLeft;
            int y = pixelsFromTop + ( ( q + pixelsBetweenBoxes ) * ( i / 12 ) );
            if (theImage != null) {
                g.drawImage(theImage, x, y, q, q, null);
            }
            else if ( colorOfBox != null ) {
                g.setColor( colorOfBox );
                g.fillRect( x, y, q, q );
            }
        }
        if ( addToPixelsFromTop ) { pixelsFromTop += ( ( q + pixelsBetweenBoxes ) * ( ( ( numberOfBoxes - 1 ) / 12 ) + 1 ) ) + pixelsBetweenSections; }
        return pixelsFromTop;
    }

    //summary screen at end of each level
    private static void drawSummary(Graphics g) {
        g.drawImage( grassImages[0], 0, 0, 966, 668, null );
        g.setColor( Ocean.seaColors[0] );
        g.setFont( f3 );
        g.drawString( "LEVEL " + Land.currentLevel, 370, 100 );
        g.setFont( f4 );
        g.drawString( "press space to continue", 320, 600 );
        if ( ! Sheep.enoughSheepSaved() ) {
            g.setFont( f6 );
            g.drawString( "save " + ( ( Sheep.startingSheep * Land.percentToContinue / 100 ) - Sheep.currentSheep )+" more for the next level...", 390, 150 );
        }
        //draw respectively: eaten sheep, drowned sheep, live sheep, tentacles, bones
        int pixelsFromTop = 180;
        pixelsFromTop = drawIconBox ( g, "", Sheep.startingSheep-Sheep.countEaten()-Sheep.countDrowned(), Sheep.startingSheep-Sheep.countDrowned(), pixelsFromTop, null, Tentacles.tentacleColors[2], false );
        pixelsFromTop = drawIconBox ( g, "", Sheep.startingSheep-Sheep.countDrowned(), Sheep.startingSheep, pixelsFromTop, null, Ocean.seaColors[0], false );
        pixelsFromTop = drawIconBox ( g, "      sheep", 0, Sheep.startingSheep, pixelsFromTop, sheepImage, null, true );
        pixelsFromTop = drawIconBox ( g, " tentacles", 0, Tentacles.totalAttacks, pixelsFromTop, null, Tentacles.tentacleColors[2], true );
        drawIconBox ( g, "      bones", 0, Bones.countEaten(), pixelsFromTop, boneImage, null, true );
    }

    //fade to green at end of level, then call next level when space bar is pressed by incrementing fadeCount
    static void drawEndLevel(Graphics g) {
        if ( fadeCount < 70 ) {
            Color fadeColor = new Color( (int) ( fadeCount * 1.5 ), fadeCount * 2, fadeCount );
            g.setColor( fadeColor );
            g.fillRect( 0, 0, 966, 668 );
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fadeCount++;
        }
        else if ( fadeCount == 70 ) {
            q = 30;
            fadeCount++;
        }
        else if ( fadeCount == 71 ) {
            drawSummary(g);
        }
        else if ( fadeCount == 72 ) {
            if ( ( Sheep.enoughSheepSaved() ) && ( Land.currentLevel == Land.sinkingStartsLevel - 1) ) {
                Sheepdog.theState = Sheepdog.gameState.sinkUPDATE;
            }
            else fadeCount++;
        }
        else if ( fadeCount > 72 ) {
            Sheep.totalSheepDead += Sheep.startingSheep - Sheep.currentSheep;
            g.dispose();
            loadNewLevel();
        }
    }

    //clear objects and static values to prepare for new level
    private static void loadNewLevel() {
        if ( Sheep.enoughSheepSaved() ) { Land.currentLevel++; }
        Bones.theBones.clear();
        Sheep.theSheep.clear();
        Tentacles.theMonster.clear();
        Land.theLand.clear();
        fadeCount = 0;
        Dog.numMoves = 0;
        Bones.totalBones = 0;
        Sheep.startingSheep = 0;
        Sheep.currentSheep = 0;
        Tentacles.finishedAttacks = 0;
        if ( Land.currentLevel > Land.maxLevels ) {
            Sheepdog.theState = Sheepdog.gameState.endGAME;
        }
        else {
            Sheepdog.theState = Sheepdog.gameState.playGAME;
            try {
                Land.newMap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //draw dog image at location coordinates
    private static void drawDog(Graphics g) {
        int x = Dog.getLoc().x * q,
                y = Dog.getLoc().y * q;
        g.drawImage( dogImage, x, y, q, q, null );
    }

    //draw bone icon at location coordinates
    private static void drawBones(Graphics g) {
        int x, y;
        for(Bones b : Bones.theBones) {
            if ( ! b.eaten ) {
                x = b.getLoc().x * q;
                y = b.getLoc().y * q;
                g.drawImage( boneImage, x, y, q, q, null );
            }
        }
    }

    //draw sheep at location coordinates
    private static void drawSheep(Graphics g) {
        int x, y;
        for( Sheep s : Sheep.theSheep ) {
            if ( ( Math.abs( Dog.getLoc().x - s.getLoc().x ) <= Sheep.sensitivity ) &&
                    ( Math.abs( Dog.getLoc().y - s.getLoc().y ) <= Sheep.sensitivity ) )
            { Sheep.moveSheep(s); }

            if ( ( ! s.eaten ) && ( ! s.drowned) ) {
                x = s.getLoc().x * q;
                y = s.getLoc().y * q;
                g.drawImage( sheepImage, x, y, q, q, null );
            }
        }
    }

    //draw ocean triangles for some background color
    private static void drawOcean(Graphics g) {
        for( Ocean o : Ocean.theOcean ) {
            g.setColor( o.patchColor );
            g.fillPolygon( o.x, o.y, o.x.length );
        }
    }

    //draw tentacle polygons between side of map and tip of tentacle
    private static void drawTentacles(Graphics g) {
        for(Tentacles t : Tentacles.theMonster) {
            if ( ! t.returned ) {
                int x1 = t.getStartLoc().x * q,
                        x2 = t.getStartLoc().x * q,
                        x3 = t.getLoc().x * q,
                        x4 = t.getLoc().x * q,
                        y1 = t.getStartLoc().y * q,
                        y2 = t.getStartLoc().y * q,
                        y3 = t.getLoc().y * q,
                        y4 = t.getLoc().y * q;
                if ( ( t.fromSide == 'r' ) || ( t.fromSide == 'l') ) { y1 += 4 * q; y4 += q; }
                else { x1 += 4 * q; x4 += q; }
                int [] x = { x1, x2, x3, x4};
                int [] y = { y1, y2, y3, y4};
                g.setColor( t.thisColor );
                g.fillPolygon( x, y, x.length );
            }
        }
    }

    //draw each land block with its image
    private static void drawLand(Graphics g) {
        int x, y;
        for ( Land thisBlock : Land.theLand ) {
            x = thisBlock.getLoc().x * q;
            y = thisBlock.getLoc().y * q;
            g.drawImage( thisBlock.thisGrassImage, x, y, q, q, null );
        }
    }

    //right-side menu display
    private static void drawMenu(Graphics g) {
        String lineOne, lineTwo;
        int x = 680,
                y=160,
                timeLeft,
                sheepToContinue = Sheep.startingSheep * Land.percentToContinue / 100;

        g.setColor( sidebarColor );
        int sidebarWidth = 320;
        int sidebarHeight = 640;
        g.fillRect( 640, 0, sidebarWidth, sidebarHeight);
        g.setColor( Sheep.sheepColor );
        g.setFont( f4 );
        g.drawString( "Level " + Land.currentLevel, 750, 70 );
        g.setFont( f6 );
        g.drawString( "save " + sheepToContinue + " sheep to continue", 720, 95 );
        g.setFont( f6 );
        g.setColor( Sheep.sheepColor );

        //show sheep info
        if ( Sheep.countEaten() == 0) { lineOne = ""; }
        else { lineOne = Sheep.countEaten() + " sheep eaten"; }
        if ( Sheep.countDrowned() == 0 ) { lineTwo = ""; }
        else { lineTwo = Sheep.countDrowned()+" sheep drowned"; }
        drawBox( Sheep.currentSheep - Sheep.countGrabbed(), Sheep.sheepColor, x, y, lineOne, lineTwo, g );

        //show tentacle info
        y += 160;
        String plural = "s";
        if ( Tentacles.totalAttacks - Tentacles.currentAttack == 1 ) { plural = ""; }
        if ( Tentacles.totalAttacks - Tentacles.currentAttack == 0 ) { lineOne=""; }
        else { lineOne = ( Tentacles.totalAttacks - Tentacles.currentAttack ) + " more tentacle" + plural; }
        if ( Tentacles.timeUntilNextAttack() >= 0 ) { timeLeft = Tentacles.timeUntilNextAttack() + 1; }
        else { timeLeft = Tentacles.everyAttack; }
        if ( timeLeft > 0 ) { drawBox( timeLeft, Tentacles.tentacleColors[2], x, y, lineOne, lineTwo, g ); }

        //show bone info
        y += 160;
        lineOne = "boneImage";
        lineTwo = "";
        if ( Dog.timeLeftPowered > 0 ) { drawBox( Dog.timeLeftPowered, Dog.dogColor, x, y, lineOne, lineTwo, g ); }
    }

    //draw each colored box in the menu with a number at stats
    private static void drawBox(int displayNumber, Color theColor, int x, int y, String lineOne, String lineTwo, Graphics g) {
        g.setColor( theColor );
        g.fillRect( x, y, 125, 125 );
        g.setFont( f6 );
        if ( lineOne.equals("boneImage") ) { g.drawImage(boneImage, x+142, y+13, 100, 100, null ); }
        else g.drawString( lineOne, x + 142, y + 50 );
        g.drawString( lineTwo, x + 142, y + 80 );
        if ( displayNumber <= 9)	 { g.setFont( f1 ); x += 40; y += 90; }
        else if (displayNumber<=99)	 { g.setFont( f2 ); x += 24; y += 86; }
        else if (displayNumber<=999) { g.setFont( f3 ); x += 14; y += 82; }
        g.setColor( sidebarColor );
        if ( displayNumber > 0 ) { g.drawString( displayNumber + "", x, y ); }
    }

    private static void loadImages() {
        String dir = "/images";
        String sheepImagePath = dir + "/sheep15p.gif";
        String dogImagePath = dir + "/dog15p.gif";
        String boneImagePath = dir + "/bone15p.gif";
        String grassImagePath = dir + "/grass";
        int numberOfGrassPatches = 3;

        try {
            sheepImage = ImageIO.read( Sheepdog.class.getResource( sheepImagePath ) );
        } catch (IOException e) {}
        try {
            dogImage = ImageIO.read( Sheepdog.class.getResource( dogImagePath ) );
        } catch (IOException e) {}
        try {
            boneImage = ImageIO.read( Sheepdog.class.getResource( boneImagePath ) );
        } catch (IOException e) {}
        try {
            grassImages = new BufferedImage[ numberOfGrassPatches ];
            for ( int i = 0; i < numberOfGrassPatches; i++ ) {
                grassImages[i] = ImageIO.read( Sheepdog.class.getResource( grassImagePath + i + ".gif" ) );
            }
        } catch (IOException e) {}
    }

}