package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.List;

/**
 * Created by Zang on 16/7/12.
 */
public class ShorterPaddle extends Eatable implements TimerObserver {

    int timeRemain;
    boolean shortStart;
    public ShorterPaddle(Integer _itemID, ItemType _type, Model _model, Double _orix, Double _oriy, byte _weight) {
        super(  _itemID, _type, _model, _orix, _oriy, _weight);
        Log.d("Arknoid::Barrel","Barrel::Barrel(Integer,ItenType,Model,Double,Double)");

        /* Initialize Local Paint Class */
        paint = new Paint();
        paint.setColor(Color.BLUE);

        /* set remain time*/
        timeRemain = 0;

        shortStart = false;
    }

    @Override
    public void applyEatable() {
        shortStart = true;
        timeRemain = 350;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::Brick", "SlowerPaddle::draw(Canvas)");
        Double left = this.orixr * model.width;
        Double top = this.oriyr * model.height;
        Double right = (this.orixr+(this.itemwr)*2) * model.width;
        Double bottom = (this.oriyr + this.itemhr) * model.height;
        /* Draw Brick as a rectangle */
        Drawable d;
        d = model.getResources().getDrawable(R.drawable.minimizepaddle);
        d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
        d.draw(canvas);
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems) {
        if (timeRemain == 350 && shortStart){
            if (this.centerYr <= 1.0 / 2.0) {
                ((BasicPaddle)(model.paddle2)).shorter();
            }
            if (this.centerYr > 1.0 / 2.0) {
                ((BasicPaddle)(model.paddle1)).shorter();
            }
            timeRemain--;
        }
        else if(timeRemain == 0 && shortStart){
            if (this.centerYr <= 1.0 / 2.0) {
                ((BasicPaddle)(model.paddle2)).resetShorter();
            }
            if (this.centerYr > 1.0 / 2.0) {
                ((BasicPaddle)(model.paddle1)).resetShorter();
            }
            shortStart = false;
        }
        else if(shortStart) {
            timeRemain--;
            //TODO stop notify ??
        }
    }

    @Override
    public String getIdentifier() {
        return "";
    }
}