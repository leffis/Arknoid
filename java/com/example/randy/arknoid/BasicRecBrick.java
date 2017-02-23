package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zang on 16/6/15.
 */
public class BasicRecBrick extends Item {
    /* Poly info */
    protected Pair<Double, Double> p1;
    protected Pair<Double, Double> p2;
    protected Pair<Double, Double> p3;
    protected Pair<Double, Double> p4;
    String type = "rectangle"; // No idea y Sam put this here
    //the order of points
    // p1      p2
    // p4      p3

    public BasicRecBrick(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID, _type, _model, _orixr, _oriyr);
        Log.d("Arknoid::BasicRecBrick","BasicRecBrick::BasicRecBrick(Integer, ItemType, Model, Double, Double)");

        /* a variable prepared for future refresh optimization, not used yet */
        this.isChanged = true;//TODO

        /* Initialize poly info, hard coded is fine since this item is static */
        this.p1 = new Pair<>(_orixr, _oriyr);
        this.p2 = new Pair<>(_orixr+(double)1/14, _oriyr);
        this.p4 = new Pair<>(_orixr, _oriyr+(double)1/28);
        this.p3 = new Pair<>(_orixr+(double)1/14, _oriyr+(double)1/28);

        if (model.sos) Log.d("Arknoid::BasicRecBrick","BasicRecBrick::BasicRecBrick()::Poly:"+p1.toString()+p2.toString()+p3.toString()+p4.toString());

        /* Initialize item metadata */
        itemwr = (double)1/14;
        itemhr = (double)1/28;

        /* Initialize Paint class */
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    public void updatePoints() {
        this.p1 = new Pair<>(orixr, oriyr);
        this.p2 = new Pair<>(orixr+(double)1/14, oriyr);
        this.p4 = new Pair<>(orixr, oriyr+(double)1/28);
        this.p3 = new Pair<>(orixr+(double)1/14, oriyr+(double)1/28);
    }

    @Override
    public void onHit(Item visitor) {
        Log.d("Arknoid::BasicRecBrick","BasicRecBrick::onHit(Item)");

        /* Zombielize this item */
        this.activated = false;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::BasicRecBrick","BasicRecBrick::draw(Canvas)");

        /* Calculate physical poly info */
        Double left = p1.first * model.width;
        Double top = p1.second * model.height;
        Double right = p3.first * model.width;
        Double bottom = p3.second * model.height;

        /* Draw BasicRecBrick as a rectangle */
        canvas.drawRect(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue() ,paint);
    }

    @Override
    public List<Pair<Double,Double>> getPolyInfo() {
        Log.d("Arknoid::BasicRecBrick","BasicRecBrick::getPolyInfo()");

        /* Copy poly info */
        List<Pair<Double,Double>> result =  new ArrayList<>();
        result.add(0, p1);
        result.add(1, p2);
        result.add(2, p3);
        result.add(3, p4);

        return result;
    }


    @Override
    public int returnID() {
        Log.d("Arknoid::BasicRecBrick","BasicRecBrick::returnID()");
        return this.itemID;
    }
}
