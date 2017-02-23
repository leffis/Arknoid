package com.example.randy.arknoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Oneplayer_game_mode extends AppCompatActivity{

    private Model model;
    ProgressBar Player1_ActivityBar = null;
    ProgressBar Player2_ActivityBar = null;
    Button Player1_ActivityButtom = null;
    Button Player2_ActivityButtom = null;
    TextView Player1_hp = null;
    TextView Player2_hp = null;
    Player Player1 = null;
    Player Player2 = null;
    Boolean haveP1 = false;
    Boolean haveP2 = false;
    Boolean P1S1mode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Arknoid::Oneplaye","Oneplaye_game_mode::onCreate(Bundle)");

        setContentView(R.layout.oneplayer_game_mode);

        model = (Model)findViewById(R.id.model);

        Intent intent = getIntent();
        int type = 0;//paddle 1 type
        int type2 = 0;
        String mode;
        Mode modetype =Mode.CLASSIC;
        if(intent != null){
            type = intent.getIntExtra("Paddle",1);
            type2 = intent.getIntExtra("Paddle2",1);
            mode = intent.getStringExtra("Mode");
            switch (mode){
                case "P1S1":
                    modetype = Mode.P1S1;
                    haveP1=true;
                    haveP2 = true;
                    P1S1mode = true;
                    break;
                case "P2S1":
                    modetype = Mode.P2S1;
                    haveP1=true;
                    haveP2=true;
                    break;
                case "BT":
                    modetype = Mode.BLUETOOTH;
                    break;
                case "classic":
                    modetype = Mode.CLASSIC;
                    haveP1=true;
                    break;
                default://to be continued
                    break;
            }
        }
        if(haveP1){
            Player1_ActivityBar =(ProgressBar)findViewById(R.id.acticity_progressbar);
            Player1_ActivityButtom=(Button)findViewById(R.id.acticity_btn);
            Player1_hp = (TextView)findViewById(R.id.activity_hp);
            Player1_ActivityButtom.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Player1.Player_click();
                }
            });
        }

            Player2_ActivityBar =(ProgressBar)findViewById(R.id.acticity_progressbar2);
            Player2_ActivityButtom=(Button)findViewById(R.id.acticity_btn2);
            Player2_hp = (TextView)findViewById(R.id.activity_hp2);
        if(haveP2){
            Player2_ActivityButtom.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Player2.Player_click();
                }
            });
        }
        if(!haveP2){
            Player2_ActivityButtom.setVisibility(View.GONE);
            Player2_ActivityBar.setVisibility(View.GONE);
            Player2_hp.setVisibility(View.GONE);
        }
        if(P1S1mode){
            Player2_ActivityButtom.setEnabled(false);
        }
        try {
            model.setVariables(this, modetype,type,type2);
            if(haveP1){
                Player1 = model.getPlayer1();
                Player1.getProgressView(this);
            }
            if(haveP2){
                Player2 = model.getPlayer2();
                Player2.getProgressView(this);
            }
        } catch (Exception e) {
            Log.d("Arknoid::Oneplaye","Oneplaye_game_mode::onCreate(Bundle): " + e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Arknoid::Oneplaye","Oneplaye_game_mode::onResume()");

        if (model.getMode() != Mode.BLUETOOTH) model.setTimer();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Arknoid::Oneplaye","Oneplaye_game_mode::onPause()");

        model.pauseTimer();
    }


    Context context = this;
//START: add by Randy 6/23/2016
    @Override
    public void onBackPressed() {//Pop dialog when click back button to check if exit
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        model.setTimer();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            }
        };
        model.pauseTimer();//pause the time
        AlertDialog.Builder builder = new AlertDialog.Builder(context);//define dialog
        builder.setMessage("Game is stoped").setPositiveButton("Resume", dialogClickListener)
                .setNegativeButton("Exit", dialogClickListener).show();//show dialog
    }
//END: add by Randy 6/23/2016

    public void setActivityProgress(int upDpwn,int progress,int hp){//whichBar =0 => up, 1 => down
        if(upDpwn == 1){
            Log.d("Arknoid::Oneplaye","Oneplaye_game_mode::setProgress Down "+hp);
            Player1_ActivityBar.setProgress(progress);
            setText(Player1_hp,Integer.toString(hp));

        }else if(upDpwn == 0){
            Player2_ActivityBar.setProgress(progress);
            setText(Player2_hp,Integer.toString(hp));
        }
        return;
    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    int check_dead = 0;//not dead
    public void isDead(){
        if(Player1.isDead){
            check_dead = 1;//p1 dead
        }else if (Player2.isDead){
            check_dead = 2; // p2 dead
        }
        if(check_dead != 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    Intent intent = new Intent(Oneplayer_game_mode.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    };

                    model.pauseTimer();//pause the time
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);//define dialog
                    builder.setMessage("Ended! Player "+ check_dead+ " win!").setPositiveButton("New Game?", dialogClickListener)
                            .setNegativeButton("Exit", dialogClickListener).show();//show dialog
                }
            });

        }
    }
}
