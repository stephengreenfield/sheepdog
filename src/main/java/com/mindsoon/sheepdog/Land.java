package com.mindsoon.sheepdog;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

class Land {
    static ArrayList<Land> theLand = new ArrayList<Land>();
    static int hw;
    static int percentToContinue;
    static int currentLevel=1;
    static final int maxLevels=5;
    static final int sinkingStartsLevel=5;
    private static boolean isSinking;
    private Loc loc;
    BufferedImage thisGrassImage;

    public Loc getLoc() {
        return this.loc;
    }

    static void newMap() throws IOException {
        Sheepdog.theState = Sheepdog.gameState.playGAME;
        String levelPath = "/levels/level"+Land.currentLevel+".json";
        final String JSON_DATA = IOUtils.toString( Sheepdog.class.getResourceAsStream( levelPath ) );
        final JSONObject mapFileData = new JSONObject(JSON_DATA).getJSONArray("mapdata").getJSONObject(0);
        Land.setMap( mapFileData.getInt("heightwidth"),
                mapFileData.getInt("percentToContinue"),
                mapFileData.getBoolean("isSinking"),
                mapFileData.optJSONArray("grid") );
        Ocean.newOcean();
        Dog.newDog( mapFileData.getInt("startX"), mapFileData.getInt("startY") );
        Bones.newBone( mapFileData.getInt("bones") );
        Sheep.newSheep( mapFileData.getInt("sheep") );
        Tentacles.newMonster( mapFileData.getInt("tentacles"),
                mapFileData.getInt("firstAttack"),
                mapFileData.getInt("everyAttack"),
                mapFileData.getInt("tentaclesPerAttack"),
                mapFileData.getInt("tentacleRange"),
                mapFileData.getString("canAttackFrom").toCharArray() );
    }

    //set basic stats of the map
    private static void setMap(int newHeightWidth, int newPercentToContinue, boolean newIsSinking, JSONArray grid) {
        percentToContinue=newPercentToContinue;
        hw = newHeightWidth;
        Visuals.q = 640 / hw;
        isSinking = newIsSinking;
        for (int y = 0; y < hw; y++) {
            for (int x = 0; x < hw; x++) {
                if ( ( y < grid.length() ) && ( x < grid.getJSONArray(y).length() ) ) {
                    if ( grid.getJSONArray(y).getInt(x)>0 ) {
                        Land aBlock = new Land();
                        Loc aLoc = new Loc ( x, y );
                        aBlock.setLand( aLoc );
                        Land.theLand.add(aBlock);
                    }
                }
            }
        }
    }

    //set a block of land with location and image (0.gif is 25x more likely)
    void setLand ( Loc thisLoc ) {
        int r = (int) ( Math.random() * Visuals.grassImages.length * 25 );
        if ( r > Visuals.grassImages.length - 1 ) { r = 0; }
        this.thisGrassImage = Visuals.grassImages[r];
        this.loc = thisLoc;
    }

    //return true if a land is coast (any orthogonal square has water)
    boolean isCoast () {
        boolean hasUp=false, hasDown=false, hasLeft=false, hasRight=false;
        for ( Land thisBlock : theLand ) {
            //check for adjacent blocks
            if      ( this.loc.adjacentUp( thisBlock.loc ) ) { hasUp=true; }
            else if ( this.loc.adjacentDown( thisBlock.loc ) ) { hasDown=true; }
            else if ( this.loc.adjacentLeft( thisBlock.loc ) ) { hasLeft=true; }
            else if ( this.loc.adjacentRight( thisBlock.loc ) ) { hasRight=true; }
        }
        //blocks on map edge don't necessarily count as coast
        if ( this.loc.y == 0 ) hasUp=true;
        if ( this.loc.y == hw -1 ) hasDown=true;
        if ( this.loc.x == 0 ) hasLeft=true;
        if ( this.loc.x == hw -1 ) hasRight=true;
        //isCoast if any orthogonal adjacent tiles are water
        return !((hasUp) && (hasDown) && (hasRight) && (hasLeft));
    }

    //cycle through each land, if coastal chance to sink
    static void sinkTheIsland() {
        if ( Land.isSinking ) {
            int chanceToSink = 200;
            ArrayList<Land> tempLand = new ArrayList<Land>();
            for ( Land thisBlock : theLand ) {
                int r = (int) ( Math.random() * chanceToSink );
                if ( ( ! thisBlock.isCoast() ) || ( thisBlock.isCoast() && r != 1 ) ) { tempLand.add(thisBlock); }
            }
            theLand=tempLand;
            Sheep.drownSheep();
            Bones.drownBones();
        }
    }

}