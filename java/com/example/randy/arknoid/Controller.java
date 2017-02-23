package com.example.randy.arknoid;

import android.util.Log;

import android.view.View;
import android.view.MotionEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import java.lang.Math;

public class Controller implements View.OnTouchListener {
    private Model model = null;

    // bot = P1, top = P2;
    private Integer playerNum = 0;
    private Paddle p1 = null;
    private Paddle p2 = null;
    private Float p1Bound = (float)3/4;
    private Float p2Bound = (float)1/4;
    private AtomicBoolean p1Dirty = new AtomicBoolean(false);
    private AtomicBoolean p2Dirty = new AtomicBoolean(false);

    private final Integer touchRadius = 14;

    /* analyze the paddle and return List<Float>(pl,pr,pmid,vsx)
        * pl = physical left bound x-coordinator
        * pr = physical right bound x-coordinator
        * pmid = physical mid point x-coordinator
        * vsx = virtual x-axis speed as a vector (矢量)
     */
    private List<Float> analyzePaddle(Paddle pad) {
        List<Float> retval = new ArrayList<>();

        Float paddle_pl = pad.getOri().first.floatValue() * model.width;
        Float paddle_pr = (pad.getOri().first.floatValue() + pad.getWH().first.floatValue()) * model.width;
        Float paddle_mid = (paddle_pl + paddle_pr) / 2;
        Float paddle_vsx = pad.getSpeed().first.floatValue();

        retval.add(paddle_pl);
        retval.add(paddle_pr);
        retval.add(paddle_mid);
        retval.add(paddle_vsx);

        return retval;
    }

    /* direction of the touch WRT the pad */
    private Integer getDirect(Paddle pad, Float px) {
        List<Float> info = analyzePaddle(pad);

        if (Math.abs(px-info.get(2)) < touchRadius) return 0;

        /* 1 for rightward and -1 for leftward */
        Integer direct = px <= info.get(2) ? -1 : 1;

        return direct;
    }

    private TouchPos analyzeTouch(Float px, Float py) {
        if ((py <= model.height*p2Bound) && (playerNum == 2)) { // p2
            switch (getDirect(p2,px)) {
                case -1:    return TouchPos.LEFT_OF_PADDLE2;
                case 0:     return TouchPos.ON_PADDLE2;
                case 1:     return TouchPos.RIGHT_OF_PADDLE2;
                default:    break;
            }
        } else if (py >= model.height*p1Bound) { // p1
            switch (getDirect(p1,px)) {
                case -1:    return TouchPos.LEFT_OF_PADDLE1;
                case 0:     return TouchPos.ON_PADDLE1;
                case 1:     return TouchPos.RIGHT_OF_PADDLE1;
                default:    break;
            }
        } else {
            return TouchPos.IGNORE;
        }

        Log.d("Arknoid::Controller","Controller::analyzeTouch::NO_CASE_HIT");
        return TouchPos.IGNORE;
    }

    private Boolean paddleGoto(Paddle pad, Integer direction) {
        Double sxr = pad.getSpeed().first;

        Log.d("Arknoid::Controller","Controller::paddleGoto()::(direction,sxr) = " + new Pair<Integer,Double>(direction,sxr).toString());

        if (direction == 0) {
            pad.changeSpeed(new Pair<>((double)0,(double)0));
            return !sxr.equals((double)0);
        }

        if (sxr.equals((double)0) || sxr == (double)0) {
            Log.d("Arknoid::Controller","Controller::paddleGoto()::sxr is 0");
            pad.setDefaultSxr(direction.doubleValue());
            return true;
        }

        if (sxr*direction < 0) {
            pad.changeSpeed(new Pair<>((double)-1,(double)0));
            return true;
        }

        return false;
    }

    private Boolean handleTouch(Float px, Float py) {
        TouchPos event = analyzeTouch(px,py);

        Log.d("Arknoid::Controller","Controller::handleTouch()::event = "+event.toString());

        switch (event) {
            case LEFT_OF_PADDLE1:   p1Dirty.set(true); return paddleGoto(p1,-1);
            case RIGHT_OF_PADDLE1:  p1Dirty.set(true); return paddleGoto(p1,1);
            case LEFT_OF_PADDLE2:   p2Dirty.set(true); return paddleGoto(p2,-1);
            case RIGHT_OF_PADDLE2:  p2Dirty.set(true); return paddleGoto(p2,1);
            case ON_PADDLE1:        p1Dirty.set(true); return paddleGoto(p1,0);
            case ON_PADDLE2:        p2Dirty.set(true); return paddleGoto(p2,0);
            case IGNORE:            return false;
            default:
                Log.d("Arknoid::Controller","Controller::handleTouch()::NO_CASE_HIT");
                return false;
        }
    }

    public Controller(Model _model) {
        super();

        Log.d("Arknoid::Controller","Controller::Controller(Model,Paddle,Paddle)");

        model = _model;

        switch (model.getMode()) {
            case P2S1:      playerNum = 2; break;
            case P1S1:      playerNum = 1; break;
            case BLUETOOTH: playerNum = 1; break;
            case CLASSIC:   playerNum = 1; break;
            default:        break;
        }
    }

    public void setP1(Paddle pad) { p1 = pad; }

    public void setP2(Paddle pad) { p2 = pad; }

    public void timerNotify() {
        if (p1 != null) {
            if (!p1Dirty.get()) p1.changeSpeed(new Pair<>((double) 0, (double) 0));
            else p1Dirty.set(false);
        }

        if (p2 != null) {
            if (!p2Dirty.get()) p2.changeSpeed(new Pair<>((double) 0, (double) 0));
            else p2Dirty.set(false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent m) {
        int count,i,action,actionIndex;
        Float px,py;

        Log.d("Arknoid::Controller","Controller::onTouch()");

        action = m.getAction();
        Log.d("Arknoid::Controller","Controller::onTouch()::action = "+action);

        count = m.getPointerCount();

        if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) {
            actionIndex = m.getActionIndex();

            px = m.getX(actionIndex);
            py = m.getY(actionIndex);

            if (py <= model.height*p2Bound) { // p2 up
                p2.changeSpeed(new Pair<>((double)0,(double)0));
                p2Dirty.set(true);
            } else if (py >= model.height*p1Bound) { // p1 up
                p1.changeSpeed(new Pair<>((double) 0, (double) 0));
                p1Dirty.set(true);
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN) {
            actionIndex = m.getActionIndex();

            px = m.getX(actionIndex);
            py = m.getY(actionIndex);

            handleTouch(px,py);
        } else if (action == MotionEvent.ACTION_MOVE) {
            for (i = 0; i < count; i++) {
                px = m.getX(i);
                py = m.getY(i);

                handleTouch(px, py);
            }
        } else {
            Log.d("Arknoid::Controller","Controller::onTouch()::NO_CASE_HIT");
        }

        return true;
    }
}
