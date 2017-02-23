package com.example.randy.arknoid;

import android.renderscript.Double2;
import android.util.Log;

public class ItemFactory {
    private Model model = null; // pointer to the model
    private int itemIdBase = 0; // counter used to assign item id

    private int generateId() {
        Log.d("Arknoid::ItemFactory","ItemFactory::generateId()");

        int retval = itemIdBase;
        itemIdBase++;

        return retval;
    }

    private Item createBasicRecBrick(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createBasicRecBrick");
        return new BasicRecBrick(generateId(), _itemType, model, _orixr, _oriyr);
    }

    private Item createBasicBall(ItemType _itemType, Double _orixr, Double _oriyr) {
        Log.d("Arknoid::ItemFactory","ItemFactory::createBasicBall");
        return new BaiscBall(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createBasicPaddle(ItemType _itemType, Double _orixr, Double _oriyr) {
        Log.d("Arknoid::ItemFactory","ItemFactory::createBasicPaddle");
        return new BasicPaddle(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createSquirtleBrick(ItemType _itemType, Double _orixr, Double _oriyr) {
        Log.d("Arknoid::ItemFactory","ItemFactory::createSquirtleBrick");
        return new SquirtleBrick(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createFireBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new fireBall(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createBarrelBrick(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new BarrelBrick(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createBarrel(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new Barrel(generateId(),_itemType,model,_orixr,_oriyr, (byte) 5);
    }

    private Item createBarrelLine(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new BarrelLine(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private Item createUpperEdge(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new upperEdge(generateId(),_itemType,model);
    }

    private Item createDownEdge(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createFireBall");
        return  new downEdge(generateId(),_itemType,model);
    }

    private Item createBrick(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::createBrick");
        return  new Brick(generateId(),_itemType,model,_orixr,_oriyr);
    }

    private  Item createFasterBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::FasterBall");
        return  new FasterBall(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private  Item createSlowerBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::SlowerBall");
        return  new SlowerBall(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private  Item createBigerBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::BigerBall");
        return  new BigerBall(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private  Item createSmallerBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::SmallerBall");
        return  new SmallerBall(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private Item createAddHp (ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::AddHp");
        return  new AddHp(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private  Item createOneMoreBall(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::OneMoreBall");
        return  new OneMoreBall(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private Item createLongerPaddle(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::LongerPaddle");
        return  new LongerPaddle(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private Item createShorterPaddle(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::ShorterPaddle");
        return  new ShorterPaddle(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private Item createFasterPaddle(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::FasterPaddle");
        return  new FasterPaddle(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    private Item createSlowerPaddle(ItemType _itemType, Double _orixr, Double _oriyr){
        Log.d("Arknoid::ItemFactory","ItemFactory::SlowerPaddle");
        return  new SlowerPaddle(generateId(),_itemType,model,_orixr,_oriyr, (byte) 3);
    }

    public ItemFactory(Model _model) {
        model = _model;
    }

    public Item createItem(ItemType _itemType, Double _orixr, Double _oriyr) {
        Log.d("Arknoid::ItemFactory","ItemFactory::createItem(ItemType,Double,Double)");

        switch (_itemType) {
            case BASIC_BALL:        return createBasicBall(_itemType,_orixr,_oriyr);
            case BASIC_PADDLE:      return createBasicPaddle(_itemType,_orixr,_oriyr);
            case BASIC_RECBRICK:    return createBasicRecBrick(_itemType, _orixr, _oriyr);
            case SQUIRTLE_BRICK:    return createSquirtleBrick(_itemType, _orixr, _oriyr);
            case FIRE_BALL:         return createFireBall(_itemType, _orixr, _oriyr);
            case BARREL_BRICK:      return createBarrelBrick(_itemType, _orixr, _oriyr);
            case BARREL:            return createBarrel(_itemType, _orixr, _oriyr);
            case BARREL_LINE:       return createBarrelLine(_itemType, _orixr, _oriyr);
            case UPPER_EDGE:        return createUpperEdge(_itemType, _orixr, _oriyr);
            case DOWN_EDGE:         return createDownEdge(_itemType, _orixr, _oriyr);
            case STEEL_BRICK:       return createBrick(_itemType, _orixr, _oriyr);
            case WOOD_BRICK:        return createBrick(_itemType, _orixr, _oriyr);
            case PAPER_BRICK:       return createBrick(_itemType, _orixr, _oriyr);
            case ADD_HP:            return createAddHp(_itemType, _orixr, _oriyr);
            case SLOWER_PADDLE:     return createSlowerPaddle(_itemType, _orixr, _oriyr);
            case FASTER_PADDLE:     return createFasterPaddle(_itemType, _orixr, _oriyr);
            case SLOWER_BALL:       return createSlowerBall(_itemType, _orixr, _oriyr);
            case FASTER_BALL:       return createFasterBall(_itemType, _orixr, _oriyr);
            case LONGER_PADDLE:     return createLongerPaddle(_itemType, _orixr, _oriyr);
            case SHORTER_PADDLE:    return createShorterPaddle(_itemType, _orixr, _oriyr);
            case BIGGER_BALL:       return createBigerBall(_itemType, _orixr, _oriyr);
            case SMALLER_BALL:      return createSmallerBall(_itemType, _orixr, _oriyr);
            case ONE_MORE_BALL:     return createOneMoreBall(_itemType, _orixr, _oriyr);
            default:
                Log.d("Arknoid::ItemFactory","ItemFactory::createItem(ItemType,Double,Double): ERROR! ALL_CASE_MISS");
                return null;
        }
    }
    public static Boolean isPaddle(ItemType itemType) {
        switch (itemType) {
            case BASIC_PADDLE:  return true;
            default:            return false;
        }
    }

    public static Boolean isBrick(ItemType itemType) {
        switch (itemType) {
            case BASIC_RECBRICK:    return true;
            case SQUIRTLE_BRICK:    return true;
            case ONE_MORE_BALL:     return true;
            case BARREL_BRICK:      return true;
            case STEEL_BRICK:       return true;
            case WOOD_BRICK:        return true;
            case PAPER_BRICK:       return true;
            default:                return false;
        }
    }

    public static Boolean isBall(ItemType itemType) {
        switch (itemType) {
            case BASIC_BALL:        return true;
            case FIRE_BALL:         return true;
            case BARREL:            return true;
            case FASTER_PADDLE: 	return true;
            case SLOWER_BALL:   	return true;
            case FASTER_BALL:   	return true;
            case LONGER_PADDLE: 	return true;
            case SHORTER_PADDLE:	return true;
            case BIGGER_BALL:   	return true;
            case SMALLER_BALL:  	return true;
            case ONE_MORE_BALL: 	return true;
            case ADD_HP:            return true;
            case SLOWER_PADDLE:     return true;

            default:                return false;
        }
    }

    public static Boolean isEdge(ItemType itemType){
        switch (itemType){
            case UPPER_EDGE:    return true;
            case DOWN_EDGE:     return true;
            case BARREL_LINE:   return true;
            default:            return false;
        }
    }
}