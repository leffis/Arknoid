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
public class SlowerPaddle extends Eatable implements TimerObserver {

    int timeRemain;
    boolean start;
    public SlowerPaddle(Integer _itemID, ItemType _type, Model _model, Double _orix, Double _oriy, byte _weight) {
        super(  _itemID, _type, _model, _orix, _oriy, _weight);
        Log.d("Arknoid::Barrel","Barrel::Barrel(Integer,ItenType,Model,Double,Double)");


        /* set remain time*/
        start = false;
        timeRemain = 0;
    }

    @Override
    public void applyEatable() {
        start = true;
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
        d = model.getResources().getDrawable(R.drawable.slow_down_paddle);
        d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
        d.draw(canvas);
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems) {
        if (timeRemain == 350 && start){
            if (this.centerYr <= 1.0 / 2.0) {
                Pair<Double, Double> Speed = new Pair<Double, Double>(0.5, 0.0);
                ((BasicPaddle)(model.paddle2)).changeSpeedEatable(Speed);
            }
            if (this.centerYr > 1.0 / 2.0) {
                Pair<Double, Double> Speed = new Pair<Double, Double>(0.5, 0.0);
                ((BasicPaddle)(model.paddle1)).changeSpeedEatable(Speed);
            }
            timeRemain--;
        }
        else if(timeRemain == 0 && start){
            if (this.centerYr <= 1.0 / 2.0) {
                Pair<Double, Double> Speed = new Pair<Double, Double>(2.0, 0.0);
                ((BasicPaddle)(model.paddle2)).changeSpeedEatable(Speed);
            }
            if (this.centerYr > 1.0 / 2.0) {
                Pair<Double, Double> Speed = new Pair<Double, Double>(2.0, 0.0);
                ((BasicPaddle)(model.paddle1)).changeSpeedEatable(Speed);
            }
            start = false;
        }
        else if(start){
            timeRemain--;
            //TODO stop notify ??
        }

    }

    @Override
    public String getIdentifier() {
        return "";
    }
}
