package com.example.randy.arknoid;

import android.util.Log;

import java.util.List;

public abstract class DynamicItem extends Item {
    protected Double sxr;// x-axis virtual speed
    protected Double syr;// y-axis virtual speed
    private byte weight = 0;

    /* Move the current item
     * This function will be called when refresh if the the item is activated
     */
    protected abstract void move();

    /* Check if collide with visitor
     * If collided
        * Modify this item itself based on the collision
        * Call visitor.onHit(this);
        * return true;
     * Else
        * return false;
     */
    public abstract Boolean checkHit(Item visitor);

    /*
    public DynamicItem (List<Object> param){
        super(param);
        this.isDynamic = true;
        // for dynamic item the 7th and 8th element in param is for sxr syr
        this.sxr = (double)param.get(7);
        this.syr = (double)param.get(8);
    } */

    public DynamicItem (Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr, Double _sxr, Double _syr, byte weight) {
        super(_itemID,_type,_model,_orixr,_oriyr);

        Log.d("Arknoid::DynamicItem","DynamicItem::DynamicItem(Integer,ItenType,Model,Double,Double,Double,Double)");

        sxr = _sxr;
        syr = _syr;
        this.weight = weight;
        isDynamic = true;
    }

    public void changeSpeed(Pair<Double,Double> offsetRatio){
        Log.d("Arknoid::DynamicItem","DynamicItem::"+type.toString()+"::changeSpeed(Pair<Double,Double>)::offsetRatio = "+offsetRatio.toString());

        sxr *= offsetRatio.first;
        syr *= offsetRatio.second;

        return;
    }

    public Pair<Double,Double> getSpeed() { return new Pair<Double,Double>(sxr,syr); }

    public byte getWeight(){
        return weight;
    }

    public double getXspeed(){
        return this.sxr;
    }

    public double getYspeed(){
        return  this.syr;
    }

    public void setSpeedr(Double x, Double y){
        this.sxr = x;
        this.syr = y;
    }
}
