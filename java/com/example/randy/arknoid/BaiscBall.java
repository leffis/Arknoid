package com.example.randy.arknoid;

import android.util.Log;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class BaiscBall extends DynamicItem {
    protected Double centerXr; // x of the virtual centre of the circle
    protected Double centerYr; // y of the virtual centre of the circle
    protected Double radiusr; // virtual radius of the circle

    /* Constructor of BasicBall */
    public BaiscBall(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr) {
        super(_itemID,_type,_model,_orixr,_oriyr,(double)1/8,(double)1/8,(byte)5);

        Log.d("Arknoid::BasicBall","BasicBall::BasicBall(Integer,ItenType,Model,Double,Double)");

        /* Set Default Radius */
        radiusr = (double)1/60;

        /* Update MetaData of the Item */
        itemwr = radiusr * 2;
        itemhr = radiusr * 2;

        /* Update Virtual Centre of the Circle */
        centerXr = _orixr + radiusr;
        centerYr = _oriyr + radiusr;

        /* Initialize Local Paint Class */
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void move() {
        Log.d("Arknoid::BasicBall","BasicBall::move()");

        /* Move Item Origin */
        orixr += sxr/model.fps;
        oriyr += syr/model.fps;

        /* Move Virtual Circle Centre */
        centerXr += sxr/model.fps;
        centerYr += syr/model.fps;

        /* Boundary Collision Handler */
        if (centerXr < 0 || centerXr > 1) sxr *= -1;
        if (centerYr < 0 || centerYr > 1) syr *= -1;
    }

    @Override
    public void updatePoints() {
        centerXr = orixr + radiusr;
        centerYr = oriyr + radiusr;
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
                    if(visitor.type.equals(ItemType.BASIC_PADDLE)){
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
                    } else {
                        newVelocity = Item.bounceBall(ball, velocity, line);
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
        /* Ball modified itself during ball.checkHit(), so ball.onHit() do nothing */
        return;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::BasicBall","BasicBall::draw(Canvas)");

        /* Calculate physical value of the ball */
        Double cx = centerXr * model.width;
        Double cy = centerYr * model.height;
        Double r = radiusr * ((model.width + model.height) / 2);

        canvas.drawCircle(cx.floatValue(),cy.floatValue(),r.floatValue(),paint);
    }

    @Override
    public List<Pair<Double,Double>> getPolyInfo() {
        /* Since this is a ball, this method should never be called */
        return null;
    }

    @Override
    public ItemType returnType() {
        return type;
    }

    @Override
    public int returnID() {
        return itemID;
    }

    /* Prepare and return ball info */
    public List<Double> getBall() {
        Log.d("Arknoid::BasicBall","BasicBall::getBall()");

        List<Double> retval = new ArrayList<>();
        retval.add(centerXr);
        retval.add(centerYr);
        retval.add(radiusr);

        return retval;
    }

    public void Bigger(){
        this.radiusr = (Double)this.radiusr * 2.0;
        this.itemwr *= 2.0;
        this.itemhr *= 2.0;
        updatePoints();
    }

    public void resetBigger(){
        this.radiusr = (Double)this.radiusr / 2.0;
        this.itemwr /= 2.0;
        this.itemhr /= 2.0;
        updatePoints();
    }

    public void Smaller(){
        this.radiusr = (Double)this.radiusr / 2.0;
        this.itemwr /= 2.0;
        this.itemhr /= 2.0;
        updatePoints();
    }

    public void resetSmall (){
        this.radiusr = (Double)this.radiusr * 2.0;
        this.itemwr *= 2.0;
        this.itemhr *= 2.0;
        updatePoints();
    }


}
