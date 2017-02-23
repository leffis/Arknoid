package com.example.randy.arknoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

/**
 * Created by Zang on 16/7/1.
 */
public class upperEdge extends BasicRecBrick {
    //player 2

    public upperEdge(Integer _itemID, ItemType _type, Model _model){
        super(_itemID, _type, _model, 0.0, 0.0);

        this.p1 = new Pair<>(0.0,0.0);
        this.p2 = new Pair<>(0.0+1.0, 0.0);
        this.p4 = new Pair<>(0.0, 0.0+(double)1/28);
        this.p3 = new Pair<>(0.0+1.0, 0.0+(double)1/28);
        paint = new Paint();
        paint.setColor(Color.YELLOW);

    }

    @Override
    public void onHit(Item visitor) {
        model.player2.loseHp();
        visitor.activated = false;

        //TODO check remain ball
        if (ItemFactory.isBall(visitor.returnType())) {
            model.addItem(model.itemFactory.createItem(ItemType.BASIC_BALL,(double)1/2,(double)1/3));
        }

        return;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("Arknoid::upperEdge","upperEdge::draw(Canvas)");

        /* Calculate physical poly info */
        Double left = p1.first * model.width;
        Double top = p1.second * model.height;
        Double right = p3.first * model.width;
        Double bottom = p3.second * model.height;

        /* Draw BasicRecBrick as a rectangle */
        canvas.drawRect(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue() ,paint);
    }


}
