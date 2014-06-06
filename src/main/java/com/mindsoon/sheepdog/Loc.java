package com.mindsoon.sheepdog;

class Loc {
    public int x,y;

    public Loc () {
        this.x = 0;
        this.y = 0;
    }

    public Loc ( int newX, int newY ) {
        this.x = newX;
        this.y = newY;
    }

    public boolean sameLoc ( Loc thisLoc ) {
        return (this.x == thisLoc.x) && (this.y == thisLoc.y);
    }

    public void setLoc ( Loc thisLoc ) {
        this.x = thisLoc.x;
        this.y = thisLoc.y;
    }

    public void right () {
        this.x++;
    }

    public void left () {
        this.x--;
    }

    public void up () {
        this.y--;
    }

    public void down () {
        this.y++;
    }

    public boolean adjacentUp ( Loc compareLoc ) {
        return (this.x == compareLoc.x) && (this.y + 1 == compareLoc.y);
    }

    public boolean adjacentDown ( Loc compareLoc ) {
        return (this.x == compareLoc.x) && (this.y - 1 == compareLoc.y);
    }

    public boolean adjacentLeft ( Loc compareLoc ) {
        return (this.x - 1 == compareLoc.x) && (this.y == compareLoc.y);
    }

    public boolean adjacentRight ( Loc compareLoc ) {
        return (this.x + 1 == compareLoc.x) && (this.y == compareLoc.y);
    }

    public boolean isUp (Loc compareLoc ) {
        return this.y < compareLoc.y;
    }

    public boolean isDown (Loc compareLoc ) {
        return this.y > compareLoc.y;
    }

    public boolean isLeft (Loc compareLoc ) {
        return this.x < compareLoc.x;
    }

    public boolean isRight (Loc compareLoc ) {
        return this.x > compareLoc.x;
    }

    boolean isValid() {
        return (!this.isWater()) && (this.inBounds());
    }

    public boolean move( char[] direction, boolean canCoexistWithSheep ) {
        Loc newLoc = new Loc();
        newLoc.setLoc( this );
        for (char aDirection : direction) {
            if (aDirection == 'u') { newLoc.up(); }
            else if (aDirection == 'd') { newLoc.down(); }
            else if (aDirection == 'l') { newLoc.left(); }
            else if (aDirection == 'r') { newLoc.right(); }
        }
        if ( ( ! canCoexistWithSheep ) && ( ! newLoc.hasSheep() ) ) { canCoexistWithSheep = true; }
        if ( ( newLoc.isValid() ) && ( canCoexistWithSheep ) ) {
            this.setLoc( newLoc );
            return true;
        }
        else { return false; }
    }

    //return true if a land has a sheep on it
    boolean hasSheep() {
        for ( Sheep thisSheep : Sheep.theSheep ) {
            if ( thisSheep.getLoc().sameLoc(this) ) { return true; }
        }
        return false;
    }

    //check if a location is within the map boundaries
    boolean inBounds () {
        return (this.x <= Land.hw - 1) && (this.y <= Land.hw - 1) && (this.x >= 0) && (this.y >= 0);
    }

    //if a location has a match in theLand object, it's land, otherwise it's water
    boolean isWater () {
        for (Land thisBlock : Land.theLand) {
            if ( ( thisBlock.getLoc().x == this.x ) && ( thisBlock.getLoc().y == this.y ) ) return false;
        }
        return true;
    }

    //set location to a random location without sheep, bones, or dog
    public void setRandomEmptyLoc () {
        do {
            int i = 1,
                    r = (int) (Math.random() * ( ( Land.theLand.size() ) + 1 ) );
            for ( Land thisBlock : Land.theLand ) {
                if ( i == r ) {
                    this.x = thisBlock.getLoc().x;
                    this.y = thisBlock.getLoc().y;
                    break;
                }
                else { i++; }
            }
        }
        while ( ( Bones.hasUneatenBone( this ) ) ||
                ( this.sameLoc( Dog.getLoc() ) ) ||
                ( this.hasSheep() ) );
    }

    //return location of a random adjacent land
    public void getRandomAdjacentLocWithoutSheep () {
        int r = (int) ( Math.random() * 4 );
        for ( Land thisBlock : Land.theLand ) {
            if   ( ( ! thisBlock.getLoc().hasSheep() ) &&
                    ( ( thisBlock.getLoc().adjacentUp( this ) ) ||
                            ( thisBlock.getLoc().adjacentDown( this ) ) ||
                            ( thisBlock.getLoc().adjacentRight( this ) ) ||
                            ( thisBlock.getLoc().adjacentLeft( this ) ) ) ) {
                this.setLoc( thisBlock.getLoc() );


                if ( r == 0 ) { break; }
                else { r--; }
            }
        }
    }

    //set location to closest sheep for tentacle targeting
    public void getClosestSheep ( Loc thisLoc ) {
        int bestDistance = Land.hw * 2;
        for ( Sheep thisSheep : Sheep.theSheep ) {
            int distance = ( Math.abs ( thisLoc.x - thisSheep.getLoc().x ) ) +
                    ( Math.abs ( thisLoc.y - thisSheep.getLoc().y ) );
            if ( ( distance < bestDistance ) && ( ! thisSheep.grabbed ) && ( ! thisSheep.drowned ) ) {
                bestDistance=distance;
                this.setLoc( thisSheep.getLoc() );
            }
        }
    }

}