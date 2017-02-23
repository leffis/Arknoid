package com.example.randy.arknoid;

import android.util.Log;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.ArrayList;

public class BasicPaddle extends Paddle {
    private List<Pair<Double,Double>> rectr = new ArrayList<>(); // outer shape of the paddle: List<Point> => see BasicPaddle::updatePoints()
    private Double defaultSxr = (double)1; // default speed of the paddle, note this is an absolute value
    private int paddle_type =0;

    /* Update shape info based on origin info */
    @Override
    public void updatePoints() {
        Log.d("Arknoid::BasicPaddle","BasicPaddle::updatePoints()");

        rectr.clear();
        rectr.add(new Pair<Double,Double>(orixr,oriyr)); // left top
        rectr.add(new Pair<Double,Double>(orixr+itemwr,oriyr)); // right top
        rectr.add(new Pair<Double,Double>(orixr+itemwr,oriyr+itemhr)); // right bot
        rectr.add(new Pair<Double,Double>(orixr,oriyr+itemhr)); // left bot
    }

    public BasicPaddle(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID,_type,_model,_orixr,_oriyr,(double)0,(double)0);

        Log.d("Arknoid::BasicPaddle","BasicPaddle::BasicPaddle(Integer,ItenType,Model,Double,Double)");

        /* Update MetaData of the Item */
        itemwr = (double)1/6;
        itemhr = (double)1/36;

        /* Initialize poly data */
        updatePoints();

        /* Initialize Paint class */
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void move() {
        Log.d("Arknoid::BasicPaddle","BasicPaddle::move()");

        /* Move origin */
        orixr += sxr/model.fps;
        oriyr += syr/model.fps;

        /* Update poly data */
        updatePoints();

        /* Boundary collision handler */
        if ((orixr < 0) || (orixr+itemwr > 1)) sxr = (double)0;
    }

    @Override
    public Boolean checkHit(Item visitor) {
        Log.d("Arknoid::BasicPaddle","BasicPaddle::checkHit(Item)");

        if (visitor.type == ItemType.BASIC_BALL) {
            /* Extract ball info from visitor */
            List<Double> ball = ((BaiscBall)visitor).getBall();

            /* Buffer for lines */
            List<Double> line = new ArrayList<>();

            /* Iterate thru each line on this paddle */
            int j;
            for (int i = 0; i < rectr.size(); i++) {
                /* Extract current line */
                line.clear();
                // First point that defined the line
                line.add(rectr.get(i).first);
                line.add(rectr.get(i).second);
                j = i+1 == rectr.size() ? 0 : i+1;
                // Second point that defined the line
                line.add(rectr.get(j).first);
                line.add(rectr.get(j).second);

                /* Apply collision algorithm */
                if (Item.checkBallCollision(ball,line)) {
                    visitor.onHit(this);
                    return true;
                }
            }
        } else {
            Log.d("Arknoid::BasicPaddle","BasicPaddle::checkHit(Item)::Unexpected ItemType = "+visitor.type.toString());
        }

        return false;
    }

    @Override
    public void onHit(Item visitor) {
        /* Paddle should not be changed by collision */
        return;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::BasicPaddle","BasicPaddle::draw(Canvas)");

        Float l,t,r,b;

        /* Calculate physical value of each point */
        l = rectr.get(0).first.floatValue() * model.width;
        t = rectr.get(0).second.floatValue() * model.height;
        r = rectr.get(1).first.floatValue() * model.width;
        b = rectr.get(2).second.floatValue() * model.height;

        if (model.sos) {
            String msg = "(l,t,r,b) = (" + l + "," + t + "," + r + "," + b + ")";
            Log.d("Arknoid::BasicPaddle","BasicPaddle::draw(Canvas)::"+msg);
        }

        //canvas.drawRect(l,t,r,b,paint);

        /* Fake code to enforce different colors */
        Drawable d;
        switch (paddle_type){
            case(1):
                d = model.getResources().getDrawable(R.drawable.red_paddle);
                break;
            case(2):
                d = model.getResources().getDrawable(R.drawable.yellow_paddle);
                break;
            case(3):
                d = model.getResources().getDrawable(R.drawable.blue_paddle);
                break;
            case(4):
                d = model.getResources().getDrawable(R.drawable.green_paddle);
                break;
            default:
                d = model.getResources().getDrawable(R.drawable.blue_paddle);
                break;
        }

        /* Draw the paddle as a rectangle */
        d.setBounds(l.intValue(),t.intValue(),r.intValue(),b.intValue());
        d.draw(canvas);
    }

    @Override
    public List<Pair<Double,Double>> getPolyInfo() {
        return rectr;
    }

    @Override
    public ItemType returnType() {
        return type;
    }

    @Override
    public int returnID() {
        return itemID;
    }

    /* Set the speed to the product of BasicBall::defaultSxr and coefficient
     * BasicBall:defaultSxr is a scalar
     * coefficient is a vector and should be 1 or -1
     */
    @Override
    public void setDefaultSxr(Double coefficient) {
        Log.d("Arknoid::BasicPaddle","BasicPaddle::setDefaultSxr(Double)::coefficient = "+coefficient);

        if (sxr != (double)0) {
            Log.d("Arknoid::BasicPaddle","BasicPaddle::setDefaultSxr(Double)::ERROR! sxr != 0");
            return;
        }

        sxr = defaultSxr * coefficient;
    }

    @Override
    public void setPaddle_type(int type){
        this.paddle_type = type;
        return;
    }

    public void changeSpeedEatable(Pair<Double, Double> x){
        this.defaultSxr *= x.first;
    }



        //TODO add on 7.12
    public void longer(){
        //this.rectr.get(1).first = orixr+itemwr+itemwr;
        //this.rectr.get(2).first = orixr+itemwr+itemwr;
        this.itemwr += this.itemwr;
        updatePoints();
        return;
    }

    public void resetlonger(){
        //this.rectr.get(1).first = orixr+itemwr;
        //this.rectr.get(2).first = orixr+itemwr;
        this.itemwr /= 2.0;
        updatePoints();
        return;
    }

    public void shorter(){
        //this.rectr.get(1).first = orixr + (Double)(itemwr/2.0);
        //this.rectr.get(2).first = orixr + (Double)(itemwr/2.0);
        this.itemwr /= 2.0;
        updatePoints();
        return;
    }

    public void resetShorter(){
        //this.rectr.get(1).first = orixr+itemwr;
        //this.rectr.get(2).first = orixr+itemwr;
        this.itemwr *= 2.0;
        updatePoints();
        return;
    }



}
