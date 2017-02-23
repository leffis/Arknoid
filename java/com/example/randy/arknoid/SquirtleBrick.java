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
public class SquirtleBrick extends BasicRecBrick {
    private int gotHit;

    public SquirtleBrick(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID, _type, _model, _orixr, _oriyr);
        Log.d("Arknoid::SquirtleBrick","SquirtleBrick::SquirtleBrick(Integer, ItemType, Model, Double, Double)");

        /* a variable prepared for future refresh optimization, not used yet */
        this.isChanged = true;//TODO

        /* Initialize poly info, hard coded is fine since this item is static */
        this.p1 = new Pair<>(_orixr, _oriyr);
        this.p2 = new Pair<>(_orixr+(double)1/14, _oriyr);
        this.p4 = new Pair<>(_orixr, _oriyr+(double)1/28);
        this.p3 = new Pair<>(_orixr+(double)1/14, _oriyr+(double)1/28);

        if (model.sos) Log.d("Arknoid::SquirtleBrick","SquirtleBrick::SquirtleBrick()::Poly:"+p1.toString()+p2.toString()+p3.toString()+p4.toString());

        /* Initialize item metadata */
        itemwr = (double)1/14;
        itemhr = (double)1/28;
        gotHit = 0;

        /* Initialize Paint class */
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }
    @Override
    public void onHit(Item visitor) {
        Log.d("Arknoid::SquirtleBrick","SquirtleBrick::onHit(Item)");
        gotHit++;
        /* Zombielize this item */
        switch (gotHit) {
            case 1:
                paint = new Paint();
                paint.setColor(Color.WHITE);
                break;
            default:
                break;
        }
        if(gotHit == 2) this.activated = false;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::SquirtleBrick","SquirtleBrick::draw(Canvas)");

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
        Log.d("Arknoid::SquirtleBrick","SquirtleBrick::getPolyInfo()");

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
        Log.d("Arknoid::SquirtleBrick","SquirtleBrick::returnID()");
        return this.itemID;
    }

    @Override
    public void updatePoints() {
        return ;
    }

}
