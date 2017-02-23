package com.example.randy.arknoid;

import android.util.Log;

import java.util.List;

/**
 * Created by Zang on 16/7/1.
 */
public class Player implements TimerObserver{
    int playerType; //  1 火龙 2 皮卡丘 3 妙蛙 4 杰尼龟
    boolean isRelease=false;
    boolean isDead = false;
    final int cdMin = 0;
    final int cdMax = 700;//easy for set progress bar
    int cd = 0;
    int modual_for_type_4 = 0;//use to slowdowm 妙蛙 progress bar
    Model model;
    final int hpMin = 0;
    final int hpMax = 5;
    int hp = 3;
    int upDown; //0 up 1 down
    Oneplayer_game_mode oneplayer_game_mode = null;

    public  Player(Model model,int upDown,int playerType){
        this.playerType = playerType;
        this.model = model;
        this.upDown = upDown;
    }


    public void getHp(){
        if(hp<hpMax){
            ++hp;
        }
        return;
    }
    public void loseHp(){
        if(hp>hpMin){
            --hp;
        } else{
            //dead
            isDead = true;
            oneplayer_game_mode.isDead();
        }
        return;
    }

    public void addCd(){
        if(playerType ==4){
            if(modual_for_type_4 == 0){
                ++modual_for_type_4;
                if(cd<cdMax){
                    ++cd;
                }
            } else if(modual_for_type_4 == 2){
                modual_for_type_4 = 0;
            } else {
                ++modual_for_type_4;
            }
        }
        else if(cd<cdMax){
            cd++;
        }
        return;
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems){
        Log.d("Arknoid::Player","Player::timerNotify()");

        if(isRelease&&(cd == cdMax)){
            Log.d("Arknoid::Player","Player::Release");
            switch (playerType){
                case 1:
                    ability1(ditems);
                    break;
                case 2:
                    Log.d("Arknoid::Player","Player::Release(ability2)");
                    ability2(ditems,sitems);
                    break;
                case 3:
                    ability3();
                    break;
                case 4:
                    ability4();
                    break;
                default:
                    break;
            }
            cd = 0;
            if(oneplayer_game_mode!= null){
                oneplayer_game_mode.setActivityProgress(upDown,cd,hp);
            }
        }else{
            isRelease=false;
        }
        addCd();
        if(oneplayer_game_mode!= null){
            oneplayer_game_mode.setActivityProgress(upDown,cd,hp);
        }

        return;
    }

    public void getProgressView(Oneplayer_game_mode oneplayer_game_mode){
        Log.d("Arknoid::Player","Player::getProgressView init");
        this.oneplayer_game_mode = oneplayer_game_mode;
    }


    @Override
    public String getIdentifier(){
        return "";
    }

    public void ability1(List<DynamicItem> ditems){
        DynamicItem tmpItem;
        for (int i = 0; i < ditems.size() ;i++) {
            tmpItem = ditems.get(i);
            if (tmpItem.type == ItemType.BASIC_BALL && tmpItem.activated) {
                double oldXspeed = tmpItem.getXspeed();
                double oldYspeed = tmpItem.getYspeed();
                double xpos = tmpItem.orixr;
                double ypos = tmpItem.oriyr;
                tmpItem.activated = false;
                fireBall newFireball = (fireBall) model.itemFactory.createItem(ItemType.FIRE_BALL, xpos, ypos);
                newFireball.initSpeed(oldXspeed, oldYspeed);
                model.addItem(newFireball);
            }
        }
        return;
    }

    public void ability2(List<DynamicItem> ditems, List <Item> sitems){
        Integer i;
        Double mid,y,h;
        Pair<Double,Double> oldori;
        Item sitembuf;

        /* Clear cd */
        cd = cdMin;
        isRelease = false;

        /* Set Mid Line */
        mid = 0.5;

        Log.d("Arknoid::Player","Player::ability2: ditems.size = "+ditems.size()+";"+" sitems.size() = "+sitems.size());

        /* Flip all items */
        for (i = 0; i < sitems.size(); i++) {
            /* Get old data */
            sitembuf = sitems.get(i);
            oldori = sitembuf.getOri();

            /* Skip paddle */
            if (ItemFactory.isPaddle(sitembuf.returnType()) || ItemFactory.isEdge(sitembuf.returnType())) continue;

            /* Calculating new data */
            y = oldori.second;                  Log.d("Arknoid::Player","Player::ability2::y1="+y);
            h = sitembuf.getWH().second;        Log.d("Arknoid::Player","Player::ability2::h="+h);
            y = y + h/2;                   Log.d("Arknoid::Player","Player::ability2::y2="+y);
            y += 2*(mid-y) - h/2;               Log.d("Arknoid::Player","Player::ability2::y3="+y);
            oldori.second = y;

            Log.d("Arknoid::Player","Player::ability2::newori="+oldori.toString());

            /* Set new data */
            sitembuf.setOri(oldori);
            sitembuf.updatePoints();
        }

        /* Flip all ditems's speed */
        for (i = 0; i < ditems.size(); i++) {
            ditems.get(i).changeSpeed(new Pair<>((double)1,(double)-1));
        }

        return;
    }

    public void ability4(){
        if (hp < hpMax) hp++;
        return;
    }

    public void ability3(){
        Double posr = (double)0;
        Double gapr = (double)5/100;
        while (posr < 1) {
            if(upDown == 0) model.addItem(model.itemFactory.createItem(ItemType.SQUIRTLE_BRICK,posr,(double)3/4));
            else model.addItem(model.itemFactory.createItem(ItemType.SQUIRTLE_BRICK,posr,(double)1/4));
            posr += gapr;
        }

        return;
    }

    public void Player_click() {
        isRelease = true;
        return;
    }

    public boolean Clickable(){
        return (cd == cdMax);
    }

}
