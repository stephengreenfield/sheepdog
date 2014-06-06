package com.mindsoon.sheepdog;

import java.awt.Color;

abstract class Dog {
    static int lastDirection, numMoves, timeLeftPowered;
    static final Color dogColor = new Color (80,40,0);
    private static Loc loc;

    private static void setLoc(Loc thisLoc) {
        loc = thisLoc;
    }

    public static Loc getLoc() {
        return loc;
    }

    //set dog's x,y coordinates at game start
    static void newDog( int x, int y ) {
        Loc thisLoc = new Loc( x, y );
        setLoc ( thisLoc );
    }

    //move dog if in bounds and on land, then check for bones
    static void moveDog( char[] direction ) {
        if ( loc.move( direction, true ) ) {
            Bones.checkForBone();
            Land.sinkTheIsland();
            Sheep.moveRandom();
            if ( Tentacles.checkForNewAttack() ) { Tentacles.newAttack(); }
            if ( timeLeftPowered == 0 ) {
                Tentacles.moveTentacles();
                numMoves++;
            }
            else {
                timeLeftPowered--;
            }
        }
        Sheep.currentSheep = Sheep.startingSheep - Sheep.countDrowned() - Sheep.countEaten();
    }

}