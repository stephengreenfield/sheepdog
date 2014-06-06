package com.mindsoon.sheepdog;

import java.util.ArrayList;

class Bones {
    static ArrayList<Bones> theBones = new ArrayList<Bones>();
    static int totalBones;
    private Loc loc;
    boolean eaten;

    public Loc getLoc() {
        return loc;
    }

    //set basic Bone stats
    void setBone ( Loc thisLoc ) {
        if ( thisLoc.isValid() ) {
            this.loc = thisLoc;
            this.eaten=false;
        }
    }

    //cycle through Bones to count those that have been eaten
    static int countEaten() {
        int totalEaten=0;
        for ( Bones b : theBones ) {
            if ( b.eaten ) totalEaten++;
        }
        return totalEaten;
    }

    //if dog is newly on a Bone, eat it
    static void checkForBone () {
        int timeForBone = 10;
        for ( Bones b : theBones ) {
            if ( ( ! b.eaten ) && ( b.loc.sameLoc( Dog.getLoc() ) ) ) {
                Dog.timeLeftPowered += timeForBone;
                b.eaten=true;
            }
        }
    }

    //remove Bones from queue if on a sunk tile
    static void drownBones() {
        ArrayList<Bones> tempBones = new ArrayList<Bones>();
        for ( Bones thisBone : theBones ) {
            if ( ! thisBone.loc.isWater() )  { tempBones.add(thisBone); }
        }
        theBones = tempBones;
    }

    //check x,y if there is an available Bone there
    static boolean hasUneatenBone ( Loc thisLoc ) {
        for ( Bones b : Bones.theBones ) {
            if ( ( ! b.eaten ) && ( b.loc == thisLoc ) ) { return true; }
        }
        return false;
    }

    //create new Bones on random land blocks
    static void newBone (int numBones) {
        for(int i = 0; i < numBones; i++) {
            Loc thisLoc = new Loc();
            thisLoc.setRandomEmptyLoc();
            Bones thisBone = new Bones();
            thisBone.setBone( thisLoc );
            Bones.theBones.add( thisBone );
            Bones.totalBones++;
        }
    }

}