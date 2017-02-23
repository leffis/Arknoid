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
public class BigerBall extends Eatable implements TimerObserver {

    int timeRemain;
    boolean start;
    public BigerBall(Integer _itemID, ItemType _type, Model _model, Double _orix, Double _oriy, byte _weight) {
        super(  _itemID, _type, _model, _orix, _oriy, _weight);
        Log.d("Arknoid::Barrel","Barrel::Barrel(Integer,ItenType,Model,Double,Double)");

        /* Initialize Local Paint Class */
        paint = new Paint();
        paint.setColor(Color.BLUE);

        /* set remain time*/
        timeRemain = 0;
        start = false;
    }

    @Override
    public void applyEatable() {
        start = true;
        this.timeRemain = 350;
    }

    public void draw(Canvas canvas) {
        Log.d("Arknoid::Brick", "SlowerPaddle::draw(Canvas)");
        Double left = this.orixr * model.width;
        Double top = this.oriyr * model.height;
        Double right = (this.orixr+(this.itemwr)*2) * model.width;
        Double bottom = (this.oriyr + this.itemhr) * model.height;
        /* Draw Brick as a rectangle */
        Drawable d;
        d = model.getResources().getDrawable(R.drawable.growth_ball);
        d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
        d.draw(canvas);
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems) {
        if (timeRemain == 350 && start){
            DynamicItem tmpItem;
            for (int i = 0; i < ditems.size() ;i++) {
                tmpItem = ditems.get(i);
                if (tmpItem.type == ItemType.BASIC_BALL && tmpItem.activated) {
                    ((BaiscBall)tmpItem).Bigger();
                }
            }
            timeRemain--;
        }
        else if (timeRemain == 0 && start){
            DynamicItem tmpItem;
            for (int i = 0; i < ditems.size() ;i++) {
                tmpItem = ditems.get(i);
                if (tmpItem.type == ItemType.BASIC_BALL && tmpItem.activated) {
                    ((BaiscBall)tmpItem).resetBigger();
                }
            }
            start = false;
        }
        else if(start){
            timeRemain--;
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
