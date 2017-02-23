package com.example.randy.arknoid;

import android.util.Log;

import java.util.List;

/**
 * Created by randy on 6/30/2016.
 */
public class Paddle_Controller_AI implements TimerObserver{
    Boolean P1S1mode = false;
    private Model model = null;
    private Player player = null;
    private int paddle_type = 0;
    public  Paddle_Controller_AI(Model model,Player player){
        super();
        P1S1mode = true;
        this.model = model;
        this.player = player;
    }


    @Override
    public void timerNotify(Paddle pad1, Paddle paddle2, List<DynamicItem> ditems, List<Item> sitems) {
        //AI part-------------------------------------------------
        if((P1S1mode)&&(paddle2 != null)){
            //move part
            int move = 0;
            Pair<Double,Double> paddle_ori = paddle2.getOri();
            Pair<Double,Double> paddle_wh = paddle2.getWH();
            move = GetMove(paddle2,ditems);
            MoveAI(move,paddle2,paddle_ori.first,paddle_ori.first+paddle_wh.first);
            //ability part
            switch (player.playerType){
                case 1:
                    ability1(pad1,ditems,sitems);
                    break;
                case 2:
                    Log.d("Arknoid::Player","Player::Release(ability2)");
                    ability2(ditems);
                    break;
                case 3:
                    ability3(ditems);
                    break;
                case 4:
                    ability4();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    public void MoveAI(int move, Paddle paddle, Double left,Double right ){
        paddle.changeSpeed(new Pair<>((double)0,(double)0));
        ((BasicPaddle)paddle).setDefaultSxr((double)1);
        if((move>0)&&(right<1)) {
            paddle.changeSpeed(new Pair<>((double) 1, (double) 0));
        } else if((move<0)&&(left>0)) {
            paddle.changeSpeed(new Pair<>((double) -1, (double) 0));
        } else {
            paddle.changeSpeed(new Pair<>((double)0,(double)0));
        }
    }

    public int GetMove(Paddle paddle2, List<DynamicItem> ditems){
        DynamicItem tmpDynamicItem = null;
        Item Max_weight_item = null;
        double max_rel_weight = 0;
        int move = 0;
        Pair<Double,Double> paddle_ori = paddle2.getOri();
        Pair<Double,Double> paddle_wh = paddle2.getWH();
        double buttom_height = paddle_ori.second+paddle_wh.second;
        double paddle_midsize = paddle_wh.first/3.5;
        int tmpmove = 0;
        for (int i=0; i< ditems.size();++i) {//iterator on all ditems
            tmpDynamicItem = ditems.get(i);//get ditems i
            //calculate weight
            byte tmp_weight = tmpDynamicItem.getWeight();
            if((tmp_weight>0)&&(tmpDynamicItem.activated)){//item with positive weight
                Pair<Double,Double> tmpitem_ori = tmpDynamicItem.getOri();
                double d = tmpitem_ori.second-buttom_height;
                if(d>0){//item is lower than the paddle -> can be catch
                    Pair<Double,Double> tmpitem_speed = tmpDynamicItem.getSpeed();
                    double t = d/(Math.abs(tmpitem_speed.second));
                    double y_speed = tmpitem_speed.second;
                    double rel_weight = tmp_weight/t;
                    if(y_speed>=0){//ball moving away, reduce rel weight
                        rel_weight = rel_weight/2;
                    }
                    double angle = Math.atan((tmpitem_speed.first)/(y_speed));
                    double displacement = Math.tan(angle) * d;//distance that paddle will move to reach the ball
                    if(y_speed>=0){
                        displacement = 0;
                    }
                    double paddle_right = paddle_ori.first+paddle_wh.first;
                    double tar_x = (-displacement)+(tmpitem_ori.first);
                    //calculate move for tmp item
                    if(tar_x > paddle_right-paddle_midsize){
                        tmpmove = 1;//move right
                    } else if(tar_x < paddle_ori.first+paddle_midsize){
                        tmpmove = -1;//move left
                    } else {//item in the paddle range
                        tmpmove = 0;
                    }
                    //find max rel weight
                    if(Max_weight_item == null){
                        max_rel_weight = rel_weight;
                        Max_weight_item = tmpDynamicItem;
                        move = tmpmove;
                    }else {
                        Pair<Double,Double> pad_speed = paddle2.getSpeed();
                        double t2 = Math.abs(move)/(pad_speed.first);
                        if(t2>t){//don't have time to catch
                            if(Max_weight_item == null){
                                max_rel_weight = rel_weight;
                                Max_weight_item = tmpDynamicItem;
                                move = tmpmove;
                                continue;
                            }
                        }
                        if(rel_weight>max_rel_weight){
                            max_rel_weight = rel_weight;
                            Max_weight_item = tmpDynamicItem;
                            move = tmpmove;
                        }
                    }
                }
            }
        }
        return  move;
    }

    private void ability1(Paddle pad1,List<DynamicItem> ditems, List<Item> sitems){
        if(player.Clickable()) {
            DynamicItem tmpDynamicItem = null;
            Item tmpitem = null;
            double total_ball = 0;
            double danger_ball = 0;
            double heightest_ball_y = 0;
            double total_brick = 0;
            double danger_brick = 0;
            double rate_ball = 0;
            double rate_brick = 0;
            for (int i = 0; i < ditems.size(); ++i) {//iterator on all ditems
                tmpDynamicItem = ditems.get(i);//get ditems i
                //check path betweem ball and paddle
                if((tmpDynamicItem.getXspeed()!=0)&&(tmpDynamicItem.activated)){
                    ++total_ball;
                    if(tmpDynamicItem.getYspeed()>0){
                        ++danger_ball;
                        Pair<Double,Double> tmpitem_ori = tmpDynamicItem.getOri();
                        heightest_ball_y = Math.max(heightest_ball_y,tmpitem_ori.second);

                    }
                }

            }
            for (int i = 0;i<sitems.size();++i){
                tmpitem = sitems.get(i);
                if((tmpitem.activated)&&(!tmpitem.isDynamic)){
                    ++total_brick;
                    Pair<Double,Double> tmpitem_ori = tmpitem.getOri();
                    if(tmpitem_ori.second>heightest_ball_y){
                        ++danger_brick;
                    }
                }
            }
            rate_ball = danger_ball/total_ball;
            rate_brick = danger_brick/total_brick;
            if((rate_ball>0.4)&&(rate_brick<0.3)){
                player.Player_click();
            }
        }
    }

    private void ability2(List<DynamicItem> ditems){
        if(player.Clickable()) {
            DynamicItem tmpDynamicItem = null;
            double total_ball = 0;
            double danger_ball = 0;
            double rate = 0;
            for (int i = 0; i < ditems.size(); ++i) {//iterator on all ditems
                tmpDynamicItem = ditems.get(i);//get ditems i
                if((tmpDynamicItem.getXspeed()!= 0)&&(tmpDynamicItem.activated)){//guess this item is a ball as x speed rarely gose 0
                    ++total_ball;
                    Pair<Double,Double> tmpitem_ori = tmpDynamicItem.getOri();
                    if((tmpitem_ori.second < 0.25)){
                        ++danger_ball;
                    } else if ((tmpitem_ori.second < 0.4)&&(tmpDynamicItem.getYspeed()<0)){
                        ++danger_ball;
                    }
                }
            }
            rate = danger_ball/total_ball;
            if(rate>0.5){
                player.Player_click();
            }
        }
    }

    private void ability3(List<DynamicItem> ditems){
        if(player.Clickable()) {
            DynamicItem tmpDynamicItem = null;
            double total_ball = 0;
            double danger_ball = 0;
            double rate = 0;
            for (int i = 0; i < ditems.size(); ++i) {//iterator on all ditems
                tmpDynamicItem = ditems.get(i);//get ditems i
                if((tmpDynamicItem.getXspeed()!= 0)&&(tmpDynamicItem.activated)){//guess this item is a ball as x speed rarely gose 0
                    ++total_ball;
                    Pair<Double,Double> tmpitem_ori = tmpDynamicItem.getOri();
                    if((tmpitem_ori.second > 0.8)){
                        ++danger_ball;
                    }
                }
            }
            rate = danger_ball/total_ball;
            if(rate>0.5){
                player.Player_click();
            }
        }
    }

    private void ability4(){
        player.Player_click();
    }
}
