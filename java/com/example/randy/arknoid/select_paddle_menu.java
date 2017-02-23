package com.example.randy.arknoid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by randy on 6/18/2016.
 */
public class select_paddle_menu extends AppCompatActivity{
    private Mode mode = null;
    String type;
    int NumPaddle = 1;
    int Paddleindex = 1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_paddle);
        this.intent = new Intent(select_paddle_menu.this, Oneplayer_game_mode.class);
        Intent intent = getIntent();//get intent from main menu
        if(intent != null){
            type = intent.getStringExtra("Mode");//receive the string by key Mode
            switch (type){
                case "P1S1":
                    mode = Mode.P1S1;
                    Random R  = new Random();
                    this.intent.putExtra("Paddle2", R.nextInt(4-1)+1);//random paddle 2
                    NumPaddle = 1;
                    break;
                case "P2S1":
                    mode = Mode.P2S1;
                    NumPaddle = 2;
                    break;
                case "BT":
                    mode = Mode.BLUETOOTH;
                    //to be determine
                    break;
                case "classic":
                    mode = Mode.CLASSIC;
                    NumPaddle = 1;
                    break;
                default:
                    break;
            }
        }

    }
    //click red paddle buttom
    public void select_red_paddle(View v) {
        if(Paddleindex == 1) {
            intent.putExtra("Paddle", 1);//red,paddle 1
        } else {
            intent.putExtra("Paddle2", 1);//red, paddle 2
        }
        intent.putExtra("Mode",type);
        if(Paddleindex == NumPaddle) {
            RestoreSelectMenu();
            startActivity(intent);
        } else {
            Paddleindex++;
            askSelectPaddle2();

        }
    }
    //click yellow paddle buttom
    public void select_yellow_paddle(View v) {
        if(Paddleindex == 1) {
            intent.putExtra("Paddle", 2);//yellow,paddle 1
        } else {
            intent.putExtra("Paddle2", 2);//yellow, paddle 2
        }
        intent.putExtra("Mode",type);
        if(Paddleindex == NumPaddle) {
            RestoreSelectMenu();
            startActivity(intent);
        } else {
            Paddleindex++;
            askSelectPaddle2();
        }
    }
    public void select_blue_paddle(View v) {
        if(Paddleindex == 1) {
            intent.putExtra("Paddle", 3);//blue,paddle 1
        } else {
            intent.putExtra("Paddle2", 3);//blue, paddle 2
        }
        intent.putExtra("Mode",type);
        if(Paddleindex == NumPaddle) {
            RestoreSelectMenu();
            startActivity(intent);
        } else {
            Paddleindex++;
            askSelectPaddle2();
        }
    }
    public void select_green_paddle(View v) {
        if(Paddleindex == 1) {
            intent.putExtra("Paddle", 4);//green,paddle 1
        } else {
            intent.putExtra("Paddle2", 4);//green, paddle 2
        }
        intent.putExtra("Mode",type);
        if(Paddleindex == NumPaddle) {
            RestoreSelectMenu();
            startActivity(intent);
        } else {
            Paddleindex++;
            askSelectPaddle2();
        }
    }
    public void askSelectPaddle2(){
        TextView t = (TextView)findViewById(R.id.textView2);
        t.setText("The Second Paddle");
    }
    public void RestoreSelectMenu(){
        TextView t = (TextView)findViewById(R.id.textView2);
        t.setText(R.string.select_paddle);
        Paddleindex = 1;

    }
}
