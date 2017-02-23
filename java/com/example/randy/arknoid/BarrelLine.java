package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linxuan on 7/1/2016.
 */
public class BarrelLine extends BasicRecBrick {


    public BarrelLine(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID, _type, _model, _orixr, _oriyr);
        Log.d("Arknoid::BarrelBrick","BarrelBrick::BarrelBrick(Integer, ItemType, Model, Double, Double)");

        /* a variable prepared for future refresh optimization, not used yet */
        this.isChanged = true;//TODO

        /* Initialize poly info, hard coded is fine since this item is static */
        this.p1 = new Pair<>(_orixr, _oriyr);
        this.p2 = new Pair<>(_orixr+(double)1, _oriyr);
        this.p3 = new Pair<>(_orixr+(double)1, _oriyr+(double)1/100);
        this.p4 = new Pair<>(_orixr, _oriyr+(double)1/100);

        if (model.sos) Log.d("Arknoid::BarrelBrick","BarrelBrick::BarrelBrick()::Poly:"+p1.toString()+p2.toString()+p3.toString()+p4.toString());

        /* Initialize item metadata */
        itemwr = (double)1;
        itemhr = (double)1/100;


        /* Initialize Paint class */
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }
    @Override
    public void onHit(Item visitor) {
        Log.d("Arknoid::BarrelBrick", "BarrelBrick::onHit(Item)");

        /* Zombielize this item */
        if (ItemFactory.isBall(visitor.returnType())) this.activated = false;
    }
}
