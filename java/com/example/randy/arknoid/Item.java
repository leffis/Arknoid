package com.example.randy.arknoid;

import android.util.Log;

import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Canvas;

import java.util.List;

public abstract class Item {
    protected Model model;                      // pointer to the model
    protected ItemType type;                    // specify the type of the item, defined as enum
    protected Double orixr;                     // virtual x coordinator of the left top point, the origin
    protected Double oriyr;                     // virtual x coordinator of the left top point, the origin
    protected Double itemwr;                    // virtual width of the item
    protected Double itemhr;                    // virtual height of the item
    protected Integer charge;                   // ignore this
    protected Paint paint = null;               // local Paint class, initialized at non-abstract class

    public int itemID;                          // item id of the item, must be unique
    public Boolean activated = true;            // true means this item is not a zombie
    public Boolean isChanged = false;           // ignore this
    public Boolean isDynamic = false;           // check if this item is dynamic

    /* Called when the current item collide the visitor
     * Change this item based on the collision
     */
    public abstract void onHit(Item visitor);

    /* Draw the item on the given canvas with local Paint class */
    public abstract void draw(Canvas canvas);

    /* Return the poly info as a List<Point>
     * The point must be in clockwise order
        * For example, if this is a rectangle, the order is: left top, riht top, right bot, left bot
        * Left top is not compulsory to be the first
     * This function should not be called if the item has arc in its shape
     */
    public abstract List<Pair<Double,Double>> getPolyInfo();
    public abstract int returnID();

    public abstract void updatePoints();

    public ItemType returnType(){
        return this.type;
    }

    public Item(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr) {
        Log.d("Arknoid::Item","Item::Item(Integer,ItenType,Model,Double,Double)");

        itemID = _itemID;
        type = _type;
        model = _model;
        orixr = _orixr;
        oriyr = _oriyr;
    }

    public Pair<Double,Double> getOri() { return new Pair<Double,Double>(orixr,oriyr); }

    public Pair<Double,Double> getWH() { return new Pair<Double,Double>(itemwr,itemhr); }

    /* This is used to check collision algorithm
    public static Boolean checkBallCollision(List<Double> ball, List<Double> line) {
        String ballstr = "Ball = (" + ball.get(0) + "," + ball.get(1) + "," + ball.get(2) + "); ";
        String linestr = "Line = (" + line.get(0) + "," + line.get(1) + ")(" + line.get(2) + "," + line.get(3) + ");";
        Log.d("Arknoid::Item","Item::checkBallCollision(): "+ballstr+linestr);

        Boolean retval = checkBallCollision(ball,line,"");

        Log.d("Arknoid::Item","Item::checkBallCollision()::retval = "+retval.toString());

        if (retval) System.exit(1);

        return retval;
    } */

    /* Collisioin Algorithm
     * Movable = ball = xyr
     * Static = line = xyxy
     * Retval = is collide
     */
    public static Boolean checkBallCollision(List<Double> ball, List<Double> line){
        Double ball_x = ball.get(0);
        Double ball_y = ball.get(1);
        Double ball_r = ball.get(2);
        Double line_x1 = line.get(0);
        Double line_y1 = line.get(1);
        Double line_x2 = line.get(2);
        Double line_y2 = line.get(3);
        Double right = ball_x - line_x1;
        Double left = line_x1 - ball_x;
        Double up = ball_y - line_y1;
        Double down = line_y1 - ball_y;

        /*
        String msg = "";
        msg += "\nball_x = " + ball_x;
        msg += "\nball_y = " + ball_y;
        msg += "\nball_r = " + ball_r;
        msg += "\nline_x1 = " + line_x1;
        msg += "\nline_y1 = " + line_y1;
        msg += "\nline_x2 = " + line_x2;
        msg += "\nline_y2 = " + line_y2;
        msg += "\nright = " + right;
        msg += "\nleft =" + left;
        msg += "\nup =" + up;
        msg += "\ndown =" + down;
        Log.d("Arknoid::Item", "Item::checkBallCollision(): " + msg); */

        // check horizontal or vertical line first
        if (line_x1.equals(line_x2)) {
            if (line_y1 > line_y2) {
                if (ball_y < line_y2 || ball_y > line_y1) return false;
            } else {
                if (ball_y > line_y2 || ball_y < line_y1) return false;
            }
            if (ball_x > line_x1) {
                // Log.d("Arknoid::Item","Item::checkBallCollision()::I");
                if (right < ball_r) return true;
                else return false;
            } else {
                // Log.d("Arknoid::Item","Item::checkBallCollision()::II");
                if (left < ball_r) return true;
                else return false;
            }
        } else if (line_y1.equals(line_y2)) {
            if (line_x1 > line_x2) {
                if (ball_x < line_x2 || ball_x > line_x1) return false;
            } else {
                if (ball_x > line_x2 || ball_x < line_x1) return false;
            }
            if (ball_y > line_y1) {
                // Log.d("Arknoid::Item","Item::checkBallCollision()::III");
                if (up < ball_r) return true;
                else return  false;
            } else {
                // Log.d("Arknoid::Item","Item::checkBallCollision()::IV");
                if (down < ball_r) return true;
                else return false;
            }
        } else {
            Double cross = (line_x2 - line_x1) * (ball_x - line_x1) + (line_y2 - line_y1) * (ball_y - line_y1);
            if (cross <= 0) {
                Double dist = Math.sqrt((ball_x - line_x1) * (ball_x - line_x1) + (ball_y - line_y1) * (ball_y - line_y1));
            }

            Double d2 = (line_x2 - line_x1) * (line_x2 - line_x1) + (line_y2 - line_y1) * (line_y2 - line_y1);
            if (cross >= d2) {
                Double dist = Math.sqrt((ball_x - line_x2) * (ball_x - line_x2) + (ball_y - line_y2) * (ball_y - line_y2));
            }

            Double r = cross / d2;
            Double px = line_x1 + (line_x2 - line_x1) * r;
            Double py = line_y1 + (line_y2 - line_y1) * r;
            Double dist = Math.sqrt((ball_x - px) * (ball_x - px) + (py - ball_y) * (py - ball_y));
            // Log.d("Arknoid::Item","Item::checkBallCollision()::V");
            if (Math.abs(dist) < ball_r) return true;
            else return false;
        }
    }

    /* Bounce Algorithm
     * Movable = ball = xyr
        * velocity = (sxr,syr)
     * Static = line = xyxy;
     * Retval = new velocity of the ball
     */
    public static Pair<Double, Double> bounceBall(List<Double> ball,  Pair<Double, Double> velocity, List<Double> line){
        Double ball_x = ball.get(0);
        Double ball_y = ball.get(1);
        Double ball_r = ball.get(2);
        Double x_speed = velocity.first;
        Double y_speed = velocity.second;
        Double line_x1 = line.get(0);
        Double line_y1 = line.get(1);
        Double line_x2 = line.get(2);
        Double line_y2 = line.get(3);
        Double speed = Math.sqrt(x_speed*x_speed + y_speed*y_speed);
        Double ball_direction = Math.asin(y_speed / speed);
        if (line_x1.equals(line_x2)) {
            Pair<Double, Double> new_velocity = new Pair<>(-x_speed, y_speed);
            return new_velocity;
        }
        if (line_y1.equals(line_y2)) {
            Pair<Double, Double> new_velocity = new Pair<>(x_speed, -y_speed);
            return new_velocity;
        }
        //通过Ax+By+C=0算出斜率k
        Double A = line_y2 - line_y1;
        Double B = line_x1 - line_x2;
        Double k = - (A / B);
        Double line_direction = Math.atan(k);
        Double new_direction = 2*line_direction - ball_direction;
        Double new_y = speed * Math.sin(new_direction);
        Double new_x = speed * Math.cos(new_direction);
        Pair<Double, Double> new_velocity = new Pair<>(new_x, new_y);
        return new_velocity;
    }

    public static Boolean checkLineCollision(List<Double> l1, List<Double> l2){ return true;}

    public static List<Double> bounceLine(List<Double> movableLine, Pair<Double, Double> velocity, List<Double> line) {return movableLine;}

    public void setOri(Pair<Double,Double> newori) {
        orixr = newori.first;
        oriyr = newori.second;

        return;
    }

}
