package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// Eatable should not be realized
public abstract class Eatable extends DynamicItem {
    protected Double centerXr; // x of the virtual centre of the circle
    protected Double centerYr; // y of the virtual centre of the circle
    protected Double radiusr; // virtual radius of the circle

    protected Eatable(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr, byte _weight) {
        super(  _itemID,
                _type,
                _model,
                _orixr,
                _oriyr,
                (double)0,
                (_model.getMode() != Mode.CLASSIC && _oriyr < 0.5) ? (double)-1/28 : (double)1/28,
                _weight);


        /* Set Default Radius */
        radiusr = (double)1/60;

        /* Update MetaData of the Item */
        itemwr = radiusr * 2;
        itemhr = radiusr * 2;

        /* Update Virtual Centre of the Circle */
        centerXr = _orixr + radiusr;
        centerYr = _oriyr + radiusr;
    }


    @Override
    public void updatePoints() {
        centerXr = orixr + radiusr;
        centerYr = oriyr + radiusr;
    }

    @Override
    protected void move() {
        Log.d("Arknoid::Barrel","Barrel::move()");

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
    public void onHit(Item visitor) { // child class should not override this
        return;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::Barrel","Barrel::draw(Canvas)");

        /* Calculate physical value of the ball */
        Double cx = centerXr * model.width;
        Double cy = centerYr * model.height;
        Double r = radiusr * ((model.width + model.height) / 2);

        canvas.drawCircle(cx.floatValue(),cy.floatValue(),r.floatValue(),paint);

    }

    @Override
    public List<Pair<Double, Double>> getPolyInfo() {
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
        if (ItemFactory.isPaddle(visitor.returnType()) || ItemFactory.isEdge(visitor.returnType())){
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
                    if (ItemFactory.isPaddle(visitor.returnType())) {
                        applyEatable();
                    }
                    this.activated = false;
                    return true;
                }

            }
        }
        return false;
    }

    public abstract void applyEatable();

}
