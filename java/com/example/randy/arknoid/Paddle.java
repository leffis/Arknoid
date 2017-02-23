package com.example.randy.arknoid;

public abstract class Paddle extends DynamicItem {
    public abstract void setDefaultSxr(Double coefficient);
    public abstract void setPaddle_type(int type);

    public Paddle(Integer _itemID, ItemType _type, Model _model, Double _orixr, Double _oriyr, Double _sxr, Double _syr) {
        super(_itemID,_type,_model,_orixr,_oriyr,_sxr,_syr,(byte)0);
    }
}
