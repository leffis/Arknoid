package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linxuan on 7/12/2016.
 */
public class Brick extends Item {
    /* Poly info */
    protected Pair<Double, Double> p1;
    protected Pair<Double, Double> p2;
    protected Pair<Double, Double> p3;
    protected Pair<Double, Double> p4;
    //the order of points
    // p1      p2
    // p4      p3
    private int life;
    private int randNum;

    public Brick(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID, _type, _model, _orixr, _oriyr);
        Log.d("Arknoid::Brick","Brick::Brick(Integer, ItemType, Model, Double, Double)");

        /* a variable prepared for future refresh optimization, not used yet */
        this.isChanged = true;//TODO

        /* Initialize poly info, hard coded is fine since this item is static */
        this.p1 = new Pair<>(_orixr, _oriyr);
        this.p2 = new Pair<>(_orixr+(double)1/14, _oriyr);
        this.p4 = new Pair<>(_orixr, _oriyr+(double)1/28);
        this.p3 = new Pair<>(_orixr+(double)1/14, _oriyr+(double)1/28);

        if (model.sos) Log.d("Arknoid::Brick","Brick::Brick()::Poly:"+p1.toString()+p2.toString()+p3.toString()+p4.toString());

        /* Initialize item metadata */
        itemwr = (double)1/14;
        itemhr = (double)1/28;

        /* Initialize item life and Paint class */
        switch (_type) {
            case STEEL_BRICK:
                life = 4;
                break;
            case WOOD_BRICK:
                life = 2;
                break;
            case PAPER_BRICK:
                life = 1;
                break;
            default:
                break;
        }
        randNum = model.getNum();
        Log.d("Arknoid::Brick","Brick::constructor"+randNum);
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
        Log.d("Arknoid::Brick","Brick::onHit(Item)");
        if (ItemFactory.isBall(visitor.returnType())) life--;
        if (life == 0) {
            /* Zombielize this item, might drop eatable */
            if (randNum == 1 || randNum == 0) model.addItem(model.itemFactory.createItem(ItemType.ADD_HP,orixr,oriyr));
            else if (randNum > 1 && randNum < 5) model.addItem(model.itemFactory.createItem(ItemType.BARREL,orixr,oriyr));
            else if (randNum > 6 && randNum < 10) {
                OneMoreBall neweatable = (OneMoreBall) model.itemFactory.createItem(ItemType.ONE_MORE_BALL,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }
            else if (randNum > 11 && randNum < 15) {
                LongerPaddle neweatable = (LongerPaddle) model.itemFactory.createItem(ItemType.LONGER_PADDLE,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            else if (randNum > 16 && randNum < 20) {
                ShorterPaddle neweatable = (ShorterPaddle) model.itemFactory.createItem(ItemType.SHORTER_PADDLE,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            else if (randNum > 21 && randNum < 25){
                FasterBall neweatable = (FasterBall) model.itemFactory.createItem(ItemType.FASTER_BALL,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            else if (randNum > 26 && randNum < 30) {
                SlowerBall neweatable = (SlowerBall) model.itemFactory.createItem(ItemType.SLOWER_BALL,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }


            else if (randNum > 31 && randNum < 35) {
                BigerBall neweatable = (BigerBall) model.itemFactory.createItem(ItemType.BIGGER_BALL,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }


            else if (randNum > 36 && randNum < 40){
                SmallerBall neweatable = (SmallerBall) model.itemFactory.createItem(ItemType.SMALLER_BALL,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            else if (randNum > 41 && randNum < 45){
                FasterPaddle neweatable = (FasterPaddle) model.itemFactory.createItem(ItemType.FASTER_PADDLE,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            else if (randNum > 45 && randNum < 50){
                SlowerPaddle neweatable = (SlowerPaddle) model.itemFactory.createItem(ItemType.SLOWER_PADDLE,orixr,oriyr);
                model.addItem((Item)neweatable);
                model.addItem((TimerObserver)neweatable);
            }

            this.activated = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::Brick","Brick::draw(Canvas)");

        /* Calculate physical poly info */
        Double left = p1.first * model.width;
        Double top = p1.second * model.height;
        Double right = p3.first * model.width;
        Double bottom = p3.second * model.height;

        /* Draw Brick as a rectangle */
        Drawable d;
        switch (life) {
            case 4:
                d = model.getResources().getDrawable(R.drawable.iron_brick_4);
                d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                d.draw(canvas);
                break;
            case 3:
                d = model.getResources().getDrawable(R.drawable.iron_brick_3);
                d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                d.draw(canvas);
                break;
            case 2:
                if (type == ItemType.STEEL_BRICK) {
                    d = model.getResources().getDrawable(R.drawable.iron_brick_2);
                    d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                    d.draw(canvas);
                } else {
                    d = model.getResources().getDrawable(R.drawable.wood_brick_2);
                    d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                    d.draw(canvas);
                }
                break;
            case 1:
                if (type == ItemType.STEEL_BRICK) {
                    d = model.getResources().getDrawable(R.drawable.iron_brick_1);
                    d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                    d.draw(canvas);
                } else if (type == ItemType.WOOD_BRICK) {
                    d = model.getResources().getDrawable(R.drawable.wood_brick_1);
                    d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                    d.draw(canvas);
                } else {
                    d = model.getResources().getDrawable(R.drawable.white_brick);
                    d.setBounds(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
                    d.draw(canvas);
                }
                break;
            default:
                break;
        }
        //canvas.drawRect(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue() ,paint);
    }

    @Override
    public List<Pair<Double,Double>> getPolyInfo() {
        Log.d("Arknoid::Brick","Brick::getPolyInfo()");

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
        Log.d("Arknoid::Brick","Brick::returnID()");
        return this.itemID;
    }
}
