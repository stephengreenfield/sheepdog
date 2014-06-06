package com.mindsoon.sheepdog;

import java.awt.Color;
import java.util.ArrayList;

class Tentacles {
    static ArrayList<Tentacles> theMonster = new ArrayList<Tentacles>();
    static int totalAttacks;
    private static int firstAttack;
    static int everyAttack;
    static int currentAttack;
    private static int tentaclesPerAttack;
    private static int tentacleRange;
    static int finishedAttacks;
    static final Color[] tentacleColors = { new Color(50,0,50), new Color(60,0,60), new Color(70,0,70), new Color(80,0,80), new Color(90,0,90) };
    private static char[] canAttackFrom;
    Color thisColor;
    private Loc loc, targetLoc, startLoc;
    private int moves;
    char fromSide;
    private boolean returning;
    boolean returned;

    public Loc getLoc() {
        return this.loc;
    }

    public Loc getStartLoc() {
        return this.startLoc;
    }

    void setLoc(Loc thisLoc) {
        this.loc.x = thisLoc.x;
        this.loc.y = thisLoc.y;
    }

    void setStartLoc(Loc thisLoc) {
        this.startLoc.x = thisLoc.x;
        this.startLoc.y = thisLoc.y;
    }

    //set tentacle to returned and eat sheep after moving off screen
    void endTentacle() {
        for ( Sheep thisSheep : Sheep.theSheep ) {
            if ( ( thisSheep.getLoc().sameLoc(this.startLoc) ) && ( ! thisSheep.eaten ) )  {
                thisSheep.eaten=true;
                Sheep.currentSheep--;
            }
        }
        this.returned = true;
        if ( ++finishedAttacks >= totalAttacks ) {
            Sheepdog.theState = Sheepdog.gameState.endLEVEL;
        }
    }

    //set new monster at level beginning
    static void newMonster ( int newNumAttacks, int newFirstAttack, int newEveryAttack, int newTentaclesPerAttack, int newTentacleRange, char[] newCanAttackFrom ) {
        totalAttacks = newNumAttacks;
        firstAttack = newFirstAttack;
        everyAttack = newEveryAttack;
        tentaclesPerAttack = newTentaclesPerAttack;
        tentacleRange = newTentacleRange;
        currentAttack = 0;
        canAttackFrom = newCanAttackFrom;
    }

    //new tentacles triggered during level
    static void newAttack () {
        for ( int i = 1; i <= tentaclesPerAttack; i++ ) {
            Tentacles.currentAttack++;
            Tentacles thisTentacle = new Tentacles();
            thisTentacle.setTentacle();
            thisTentacle.targetLoc.getClosestSheep( thisTentacle.getLoc() );
            int r = (int) (Math.random() * tentacleColors.length );
            thisTentacle.thisColor = Tentacles.tentacleColors[r];
            theMonster.add( thisTentacle );
        }
    }

    //if tentacle reaches a sheep, grab it
    private void grabSheep() {
        for ( Sheep thisSheep : Sheep.theSheep ) {
            if ( this.loc.sameLoc( thisSheep.getLoc() ) ) {
                thisSheep.grabbed=true;
                this.returning=true;
            }
        }
        // recalculate tentacle targets
        for ( Tentacles t : theMonster ) {
            t.targetLoc.getClosestSheep( t.getLoc() );
        }
    }

    //move sheep to new spot when already grabbed by a tentacle
    void takeSheep( Loc thisLoc ) {
        for ( Sheep thisSheep : Sheep.theSheep ) {
            if ( (thisLoc.sameLoc(thisSheep.getLoc() ) ) && ( thisSheep.grabbed ) && ( ! thisSheep.drowned ) ) {
                thisSheep.setLoc(this.loc);
            }
        }
    }

    //check timing to see if new tentacles should arrive
    static Boolean checkForNewAttack () {
        return (Tentacles.currentAttack < Tentacles.totalAttacks) && (timeUntilNextAttack() == 0);
    }

    //calculate time until next tentacle attack
    static int timeUntilNextAttack () {
        int t;
        if ( Tentacles.currentAttack >= Tentacles.totalAttacks ) t = -1;
        else t = ( Tentacles.everyAttack ) + (Tentacles.currentAttack * Tentacles.everyAttack / Tentacles.tentaclesPerAttack) - Tentacles.firstAttack - Dog.numMoves;
        return t;
    }

    //if tentacle is returning, go back to screen edge; otherwise move toward nearest sheep
    static void moveTentacles() {
        for ( Tentacles t : theMonster ) {
            if ( t.returning ) {
                Loc sheepLoc = new Loc();
                sheepLoc.setLoc( t.loc );
                if ( ( t.loc.sameLoc( t.startLoc ) ) && ( ! t.returned ) ) { t.endTentacle(); }
                if ( t.loc.isRight( t.startLoc ) ) { t.loc.left(); }
                else if (t.loc.isLeft( t.startLoc ) ) { t.loc.right(); }
                if ( t.loc.isUp( t.startLoc ) ) { t.loc.down(); }
                else if ( t.loc.isDown( t.startLoc ) ) { t.loc.up(); }
                t.takeSheep( sheepLoc );
            }
            else {
                t.targetLoc.getClosestSheep( t.getLoc() );
                if ( t.loc.isUp( t.targetLoc ) ) { t.loc.down(); }
                else if ( t.loc.isDown( t.targetLoc ) ) { t.loc.up(); }
                if ( t.loc.isLeft( t.targetLoc ) ) { t.loc.right(); }
                else if ( t.loc.isRight( t.targetLoc ) ) { t.loc.left(); }
                if ( t.loc.sameLoc( t.targetLoc ) ) { t.grabSheep(); }
            }
            t.moves++;
            if ( t.moves >= Tentacles.tentacleRange) { t.returning = true; }
        }
    }

    //set new tentacle's starting location by checking which sides of map are available
    private void setTentacle() {
        int whichSide = (int) ( Math.random() * canAttackFrom.length );
        this.fromSide = canAttackFrom[ whichSide ];
        this.loc = new Loc();
        this.startLoc = new Loc();
        this.targetLoc = new Loc();
        int r = (int) ( Math.random() * ( ( Land.hw ) +1 ) );
        switch ( this.fromSide ) {
            case 'l':
                loc.x = 0;
                loc.y = r;
                break;
            case 'r':
                loc.x = Land.hw;
                loc.y = r;
                break;
            case 'u':
                loc.x = r;
                loc.y = 0;
                break;
            case 'd':
                loc.x = r;
                loc.y = Land.hw;
                break;
        }
        this.setLoc ( loc );
        this.setStartLoc ( loc );
        this.returning = false;
        this.moves = 0;
    }

}