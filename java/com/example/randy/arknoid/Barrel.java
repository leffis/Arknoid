package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linxuan on 7/1/2016.
 */
public class Barrel extends Eatable {

    public Barrel(Integer _itemID, ItemType _type, Model _model, Double _orix, Double _oriy, byte _weight) {
        super(  _itemID, _type, _model, _orix, _oriy, _weight);
        Log.d("Arknoid::Barrel","Barrel::Barrel(Integer,ItenType,Model,Double,Double)");

        /* Initialize Local Paint Class */
        paint = new Paint();
        paint.setColor(Color.GRAY);

    }
    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::Brick", "SlowerPaddle::draw(Canvas)");
        Double left = this.orixr * model.width;
        Double top = this.oriyr * model.height;
        Double right = (this.orixr+this.itemwr) * model.width;
        Double bottom = (this.oriyr + this.itemhr) * model.height;
        /* Draw Brick as a rectangle */
        Drawable d;
        d = model.getResources().getDrawable(R.drawable.shield);
        d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
        d.draw(canvas);
    }

    @Override
    public void applyEatable() {
        if (this.centerYr <= 1.0 / 2.0) model.topLine.activated = true;
        if (this.centerYr > 1.0 / 2.0) model.botLine.activated = true;
        return;
    }
}
