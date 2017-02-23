package com.example.randy.arknoid;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zang on 16/7/1.
 */
public class fireBall extends BaiscBall {

    int hitNum;

    public fireBall(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr){
        super(_itemID,_type,_model,_orixr,_oriyr);
        this.hitNum = 1;
        paint.setColor(Color.RED);
    }

    void initSpeed(Double xold, Double yold){
        this.sxr = xold*10;
        this.syr = yold*10;
    }

    @Override
    public Boolean checkHit(Item visitor) {
        /* Copy out ball shape data */
        List<Double> ball = new ArrayList<>();
        ball.add(0, this.centerXr);
        ball.add(1, this.centerYr);
        ball.add(2, this.radiusr);

        /* Copy out dynamic data */
        Pair<Double,Double> velocity = new Pair<>(this.sxr, this.syr);

        /* Buffer for incoming ner velocity */
        Pair<Double,Double> newVelocity;

        /* Buffer for incoming poly data */
        List<Double> line = new ArrayList<>();

        /* Check collision line by line */
        if (!ItemFactory.isBall(visitor.returnType())){
            /* Get outter shape of the visitor: List<Point> */
            List<Pair<Double,Double>> poly =  visitor.getPolyInfo();

            /* Iterate thru each line: two continuous points define a line */
            for (int i = 0; i<poly.size(); i++){
                /* Extract current line */
                line.clear();
                // First point that defined the line
                line.add(poly.get(i).first);
                line.add(poly.get(i).second);
                int j = i + 1 == poly.size() ? 0 : i+1;
                // Second point that defined the line
                line.add(poly.get(j).first);
                line.add(poly.get(j).second);

                /* Apply Collision Algorithm */
                if (Item.checkBallCollision(ball,line)){
                    if(ItemFactory.isPaddle(visitor.returnType())){
                        hitNum = 0;
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
                        if (line_x1.equals(line_x2)) {
                            newVelocity = new Pair<>(-x_speed, y_speed);
                        } else {
                            Double paddle_right_position = Math.max(line_x1,line_x2);
                            Double paddle_width = Math.abs(line_x1-line_x2);
                            Double paddle_middle = paddle_right_position-(paddle_width/2);
                            Double ball_middle = ball_x+ball_r;
                            Double hit_width = ball_middle-paddle_middle;
                            Double angle_const = paddle_width/4;//need to be adjust;
                            Double bounce_angle = Math.atan(hit_width/angle_const);
                            Double new_x_speed = speed*Math.sin(bounce_angle);
                            Double new_y_speed = speed*Math.cos(bounce_angle);
                            if(line_x1 > line_x2){
                                new_y_speed = - new_y_speed;
                            }
                            newVelocity = new Pair<>(new_x_speed,-new_y_speed);
                        }

                        double xpos = this.orixr;
                        double ypos = this.oriyr;
                        BaiscBall newBall = (BaiscBall) model.itemFactory.createItem(ItemType.BASIC_BALL, xpos, ypos);
                        model.addItem(newBall);
                        this.activated = false;
                        newBall.setSpeedr(this.sxr / 10, this.syr / 10);

                    } else {
                        newVelocity = new Pair<>(1.0,1.0);
                        newVelocity.first =this.sxr;
                        newVelocity.second = this.syr;
                    }
                    this.sxr = newVelocity.first;
                    this.syr = newVelocity.second;
                    visitor.onHit(this);
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void onHit(Item visitor) {
        return;
    }
}
