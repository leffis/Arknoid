package com.example.randy.arknoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // xyz
    }
    //go to setting menu
    public void GotoSettingActivity(View v) {
        Intent intent = new Intent(MainActivity.this, SettingMenu.class);
        startActivity(intent);
    }
    //select classic game mode
    public void gotoclassicgamemode(View v) {
        Mode mode = Mode.CLASSIC;
        Intent intent = new Intent(MainActivity.this, select_paddle_menu.class);
        intent.putExtra("Mode","classic");//pass value to paddle setting activity
        startActivity(intent);
    }
    //select one player game mode
    public void gotooneplayergamemode(View v) {
        Mode mode = Mode.P1S1;
        Intent intent = new Intent(MainActivity.this, select_paddle_menu.class);
        intent.putExtra("Mode","P1S1");//pass value to paddle setting activity
        startActivity(intent);
    }
    //select two player game mode
    public void gototwoplayergamemode(View v) {
        Mode mode = Mode.P2S1;
        Intent intent = new Intent(MainActivity.this, select_paddle_menu.class);
        intent.putExtra("Mode","P2S1");//pass value to paddle setting activity
        startActivity(intent);
    }
    //select bluetooth game mode
    public void gotobluetoothgamemode(View v) {
        Mode mode = Mode.BLUETOOTH;
        Intent intent = new Intent(MainActivity.this, select_paddle_menu.class);
        intent.putExtra("Mode","BT");//pass value to paddle setting activity
        startActivity(intent);
    }
}
