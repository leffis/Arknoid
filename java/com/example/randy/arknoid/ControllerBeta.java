package com.example.randy.arknoid;

import android.util.Log;

import android.view.View;
import android.view.MotionEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import java.lang.Math;

public class ControllerBeta implements View.OnTouchListener {
    private Model model = null;

    // bot = P1, top = P2
    private Integer playerNum = 0;
    private final Integer touchRadius = 14;
    private final Pair<Double,Double> zeroPair = new Pair<>((double)0,(double)0);
    private final Pair<Double,Double> flipPair = new Pair<>((double)-1,(double)0);

    private final Float p1Bound = (float)3/4;
    private Float p1Dest = null;

    private final Float p2Bound = (float)1/4;
    private Float p2Dest = null;

    private Float getMid(Paddle pad) {
        Float paddle_pl = pad.getOri().first.floatValue() * model.width;
        Float paddle_pr = (pad.getOri().first.floatValue() + pad.getWH().first.floatValue()) * model.width;
        Float paddle_mid = (paddle_pl + paddle_pr) / 2;

        return paddle_mid;
    }

    /* direction of the touch WRT the pad */
    private Integer getDirect(Paddle pad, Float px) {
        Float mid = getMid(pad);

        if (Math.abs(px-mid) < touchRadius) return 0;

        /* 1 for rightward and -1 for leftward */
        Integer direct = px <= mid ? -1 : 1;

        return direct;
    }

    private void handleTouch(Float px, Float py) {
        Log.d("Arknoid::ControllerBeta","ControllerBeta::handleTouch("+px+","+py+")");

        if (playerNum == 2 && py <= model.height*p2Bound) { // p2
            p2Dest = px;
        } else if (py >= model.height*p1Bound) { // p1
            p1Dest = px;
        }

        Log.d("Arknoid::ControllerBeta","ControllerBeta::after::dest(1,2) = ("+p1Dest+","+p2Dest+")");
    }

    public ControllerBeta(Model _model) {
        super();

        Log.d("Arknoid::ControllerBeta","ControllerBeta::ControllerBeta(Model,Paddle,Paddle)");

        model = _model;

        switch (model.getMode()) {
            case P2S1:      playerNum = 2; break;
            case P1S1:      playerNum = 1; break;
            case BLUETOOTH: playerNum = 1; break;
            case CLASSIC:   playerNum = 1; break;
            default:        break;
        }
    }

    public void timerNotify() {
        Integer direct;
        Double sxr;
        String msg = "";

        if (p1Dest == null) {

            msg += "p1Dest = null / ";

        } else {

            msg += "dest(1,2) = (" + p1Dest + "," + p2Dest + ") / ";

            msg += "check p1 / ";
            model.p1_lk.lock();

            direct = getDirect(model.paddle1, p1Dest);
            msg += "direct = " + direct + " / ";

            if (direct == 0) {
                model.paddle1.changeSpeed(zeroPair);
            } else {
                sxr = model.paddle1.getSpeed().first;
                msg += "sxr = " + sxr + " / ";

                if (sxr.equals((double) 0) || sxr == (double) 0) {
                    model.paddle1.setDefaultSxr(direct.doubleValue());
                } else if (sxr * direct < 0) {
                    model.paddle1.changeSpeed(flipPair);
                }
            }

            model.p1_lk.unlock();
        }

        msg += "dest(1,2) = (" + p1Dest + "," + p2Dest + ") / ";

        if (playerNum == 1 || p2Dest == null) {

            msg += "playerNum = 1 or p2Dest = null / ";

        } else {

            msg += "cehck p2 / ";

            model.p2_lk.lock();

            direct = getDirect(model.paddle2, p2Dest);
            msg += "direct = " + direct + " / ";

            if (direct == 0) {
                model.paddle2.changeSpeed(zeroPair);
            } else {
                sxr = model.paddle2.getSpeed().first;
                msg += "sxr = " + sxr + " / ";

                if (sxr.equals((double) 0) || sxr == (double) 0) {
                    model.paddle2.setDefaultSxr(direct.doubleValue());
                } else if (sxr * direct < 0) {
                    model.paddle2.changeSpeed(flipPair);
                }
            }

            model.p2_lk.unlock();
        }

        msg += "dest(1,2) = (" + p1Dest + "," + p2Dest + ") / ";

        // Log.d("Arknoid::ControllerBeta","ControllerBeta::timerNotify: "+msg);
    }

    @Override
    public boolean onTouch(View v, MotionEvent m) {
        int action,actionIndex,i,count;
        Float px,py;

        action = m.getAction();
        actionIndex = m.getActionIndex();
        count = m.getPointerCount();

        switch (action) {
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                px = m.getX(actionIndex);
                py = m.getY(actionIndex);
                handleTouch(px,py);
                break;
            case MotionEvent.ACTION_MOVE:
                for (i = 0; i < count; i++) {
                    px = m.getX(i);
                    py = m.getY(i);
                    handleTouch(px, py);
                }
                break;
            default:
                Log.d("Arknoid::ControllerBeta","ControllerBeta::onTouch()::NO_CASE_HIT");
                break;
        }

        return true;
    }
}
