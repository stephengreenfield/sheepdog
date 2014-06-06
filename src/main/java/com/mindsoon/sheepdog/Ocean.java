package com.mindsoon.sheepdog;

import java.awt.Color;
import java.util.ArrayList;

class Ocean {
    static ArrayList<Ocean> theOcean = new ArrayList<Ocean>();
    //makes a bunch of triangles of various shades of blue as the background
    private static final int seaCount=100;
    public static final Color[] seaColors = { new Color(0,50,90),
            new Color(0,50,95),
            new Color(0,50,100),
            new Color(0,50,105),
            new Color(0,50,110) };
    public int [] x,y;
    public Color patchColor;

    //create a bunch of triangles for colorful ocean background
    void setOcean () {
        this.x = new int [3];
        this.y = new int [3];
        int r = (int) (Math.random() * seaColors.length );
        this.x[0] = (int) (Math.random() * Land.hw * Visuals.q );
        this.y[0] = (int) (Math.random() * Land.hw * Visuals.q );
        this.x[1] = (int) (Math.random() * Land.hw * Visuals.q );
        this.y[1] = (int) (Math.random() * Land.hw * Visuals.q );
        this.patchColor = seaColors[r];
        if ( this.x[0] > Land.hw * Visuals.q / 2 ) { this.x[1]=Land.hw * Visuals.q; }
        else { this.x[1] = 0; }
        if ( this.y[0] > Land.hw * Visuals.q / 2 ) { this.y[2]=Land.hw * Visuals.q; }
        else { this.y[2] = 0; }
    }

    //create a bunch of ocean objects
    static void newOcean () {
        for(int i = 0; i < Ocean.seaCount; i++) {
            Ocean oceanTriangle = new Ocean();
            oceanTriangle.setOcean();
            theOcean.add(oceanTriangle);
        }
    }

}