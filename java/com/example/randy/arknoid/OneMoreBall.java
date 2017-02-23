package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;
import java.util.ArrayList;

public class OneMoreBall extends Eatable implements TimerObserver{
    boolean isApply;
    // Eatable(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr, byte _weight)
    public OneMoreBall(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr, byte _weight) {
        super(  _itemID, _type, _model, _orixr, _oriyr, _weight);

        paint = new Paint();
        paint.setColor(Color.LTGRAY);

        isApply = false;
    }

    @Override
    public void applyEatable() {
        Log.d("Arknoid::Brick", "OneMoreBall::applyEatable");
        this.isApply = true;
        return;
    }

    public void draw(Canvas canvas) {
        Log.d("Arknoid::Brick", "OneMoreBall::draw(Canvas)");
        Double left = this.orixr * model.width;
        Double top = this.oriyr * model.height;
        Double right = (this.orixr+(this.itemwr)*2) * model.width;
        Double bottom = (this.oriyr + this.itemhr) * model.height;
        /* Draw Brick as a rectangle */
        Drawable d;
        d = model.getResources().getDrawable(R.drawable.fenshen);
        d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
        d.draw(canvas);
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems) {
        Log.d("Arknoid::Brick", "OneMoreBall::timerNotify()");
        if(isApply) {
            Log.d("Arknoid::Brick", "OneMoreBall::timerNotify, isApply");
            DynamicItem tmpItem;
            for (int i = 0; i < ditems.size() ;i++) {
                tmpItem = ditems.get(i);
                if (tmpItem.type == ItemType.BASIC_BALL && tmpItem.activated) {
                    double oldXspeed = tmpItem.getXspeed();
                    double oldYspeed = tmpItem.getYspeed();
                    double xpos = tmpItem.orixr;
                    double ypos = tmpItem.oriyr;
                    BaiscBall newball = (BaiscBall) model.itemFactory.createItem(ItemType.BASIC_BALL, xpos, ypos);
                    newball.setSpeedr(oldYspeed, oldXspeed);
                    model.addItem(newball);
                }
            }
        }
        this.isApply =false;
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
