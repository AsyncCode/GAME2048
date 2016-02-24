package com.zq1451349.game2048;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnStartNewGame).setOnClickListener(this);
            findViewById(R.id.btnContinueGame).setOnClickListener(this);
        findViewById(R.id.btnLoadProgress).setOnClickListener(this);
        findViewById(R.id.btnOpenLeaderBoard).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (new File(getFilesDir(), ".AUTO_SAVED").exists()) {
            findViewById(R.id.btnContinueGame).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btnContinueGame).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnStartNewGame:
                intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("progress", ".DO_NOT_LOAD");
                startActivity(intent);
                break;
            case R.id.btnContinueGame:
                intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("progress", ".AUTO_SAVED");
                startActivity(intent);
                break;
            case R.id.btnLoadProgress:
                intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOpenLeaderBoard:
                intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
