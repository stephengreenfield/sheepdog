package com.mindsoon.sheepdog;

import java.awt.Color;
import java.util.ArrayList;

class Sheep {
    static ArrayList<Sheep> theSheep = new ArrayList<Sheep>();
    static final Color sheepColor = new Color(180, 180, 180);
    static int sensitivity=3, startingSheep=0, currentSheep=0, totalSheepDead=0;
    private Loc loc;
    boolean grabbed, eaten, drowned;

    public void setLoc( Loc thisLoc ) {
        this.loc.x = thisLoc.x;
        this.loc.y = thisLoc.y;
    }

    public Loc getLoc() {
        return this.loc;
    }

    //create a new sheep on a land block
    static void newSheep(int numSheep) {
        for(int i = 0; i < numSheep; i++) {
            Loc thisLoc = new Loc();
            thisLoc.setRandomEmptyLoc();
            Sheep thisSheep = new Sheep();
            thisSheep.setSheep( thisLoc );
            Sheep.theSheep.add( thisSheep );
            Sheep.startingSheep++;
            Sheep.currentSheep++;
        }
    }

    //set basic sheep stats
    void setSheep( Loc newLoc ) {
        this.loc = newLoc;
        this.grabbed=false;
        this.drowned=false;
    }

    //1 in 40 chance for each sheep to move to a random adjacent land
    static void moveRandom() {
        for ( Sheep thisSheep : theSheep ) {
            int r = (int) (Math.random() * 40 );
            if ( ( ! thisSheep.grabbed ) && ( ! thisSheep.drowned ) && ( r == 1 ) ) {
                thisSheep.getLoc().getRandomAdjacentLocWithoutSheep();
            }
        }
    }

    //if sheep on recently sunk tile, drown it
    static void drownSheep() {
        for ( Sheep thisSheep : theSheep ) {
            if ( ( ! thisSheep.grabbed ) && ( thisSheep.loc.isWater() ) ) {
                thisSheep.drowned = true;
            }
        }
    }

    //count number of sheep eaten
    static int countEaten() {
        int sheepCount = 0;
        for ( Sheep thisSheep : theSheep ) {
            if (thisSheep.eaten) sheepCount++;
        }
        return sheepCount;
    }

    //count number of sheep drowned
    static int countDrowned() {
        int sheepCount = 0;
        for ( Sheep thisSheep : theSheep ) {
            if (thisSheep.drowned) sheepCount++;
        }
        return sheepCount;
    }

    //count number of sheep grabbed
    static int countGrabbed() {
        int sheepCount = 0;
        for ( Sheep thisSheep : theSheep ) {
            if ( ( ! thisSheep.eaten ) && ( thisSheep.grabbed ) ) sheepCount++;
        }
        return sheepCount;
    }

    //at end of level, check if minimum sheep have been saved
    static boolean enoughSheepSaved () {
        int saved = 100 * Sheep.currentSheep / Sheep.startingSheep;
        return saved >= Land.percentToContinue;
    }

    //move sheep to new land block when dog is near
    static void moveSheep(Sheep s) {
        ArrayList<String> m = new ArrayList<String>();
        if ( Dog.getLoc().sameLoc(s.getLoc() ) ) { m.add("ul");m.add("ur");m.add("dl");m.add("dr"); }
        else if ( ( Dog.getLoc().isUp( s.getLoc() ) ) && ( Dog.getLoc().isLeft( s.getLoc() ) ) ) { m.add("d");m.add("r");m.add("dr"); }
        else if ( ( Dog.getLoc().isUp( s.getLoc() ) ) && ( Dog.getLoc().isRight( s.getLoc() ) ) ) { m.add("l");m.add("d");m.add("dl"); }
        else if ( ( Dog.getLoc().isDown( s.getLoc() ) ) && ( Dog.getLoc().isLeft( s.getLoc() ) ) ) { m.add("u");m.add("r");m.add("ur"); }
        else if ( ( Dog.getLoc().isDown( s.getLoc() ) ) && ( Dog.getLoc().isRight( s.getLoc() ) ) ) { m.add("u");m.add("l");m.add("ul"); }
        else if ( Dog.getLoc().isUp( s.getLoc() ) ) { m.add("d");m.add("dr");m.add("dl"); }
        else if ( Dog.getLoc().isDown( s.getLoc() ) ) { m.add("u");m.add("ur");m.add("ul"); }
        else if ( Dog.getLoc().isLeft( s.getLoc() ) ) { m.add("r");m.add("ur");m.add("dr"); }
        else if ( Dog.getLoc().isRight( s.getLoc() ) ) { m.add("l");m.add("ul");m.add("dl"); }
        else m = null;
        if ( (m != null) && ( ! s.grabbed ) && ( ! s.drowned ) ) {
            String direction = m.get( (int) ( Math.random() * ( m.size() ) ) );
            s.loc.move( direction.toCharArray() , false );
        }
    }

}