package com.chenwx.huarongdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private String levelId;
    private static String TAG = "lyq";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent  = getIntent();
        String infoTvText = intent.getStringExtra(GameActivity.MESSAGE_LOVE);
        TextView textview = findViewById(R.id.timeConsumed);
        TextView infoView = findViewById(R.id.infoTv);
        levelId = intent.getStringExtra(GameActivity.LEVEL_ID);
        String time_consumed = intent.getStringExtra(GameActivity.TIME_CONSUMED);
        textview.setText(time_consumed);
        infoView.setText(infoTvText);
    }

    public void backToSelect(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void replay(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(SelectActivity.LEVEL_ID, levelId);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
