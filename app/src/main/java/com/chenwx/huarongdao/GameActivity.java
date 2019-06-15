package com.chenwx.huarongdao;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.chenwx.huarongdao.MESSAGE";
    public static final String TIME_CONSUMED = "TIME_CONSUMED";
    public static final String LEVEL_ID = "LEVEL_ID";
    public static final String MESSAGE_LOVE = "LOVE_MESSAGE";
    private static String TAG = "lyq", levelId;
    private PaintBoard paintboard;
    private TextView stepsview;
    private TextView levelName;
    private Button backBtn;
    public static int mHeight, mWidth;

    private int screenHeight, screenWidth;

    private static GameActivity gameActivity;

    private static final long START_TIME_IN_MILLIS = 300000;
    private TextView mTextViewCountDown;
//    private  mWaveView;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public GameActivity() {
        gameActivity = this;
    }

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        paintboard = (PaintBoard) findViewById(R.id.paint_board);
        stepsview = (TextView) findViewById(R.id.stepsview);
        backBtn = (Button) findViewById(R.id.back_btn);
        levelName = (TextView) findViewById(R.id.levelname);

        mTextViewCountDown = findViewById(R.id.CountView);

        startTimer();
        updateCountDownText();

        Intent intent = getIntent();
        levelId = intent.getStringExtra(SelectActivity.LEVEL_ID);
        paintboard.initChesses(levelId);
        TypedArray chesses_temp;
        chesses_temp = getResources().obtainTypedArray(R.array.chesses);
        levelName.setText(getResources().getResourceEntryName(chesses_temp.getResourceId(Integer.parseInt(levelId), -1)));
    }

    public void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millusUntilFinished) {
                mTimeLeftInMillis = millusUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                Intent intent = new Intent(GameActivity.getGameActivity(), ResultActivity.class);

                intent.putExtra(MESSAGE_LOVE, "挑战失败");
                intent.putExtra(TIME_CONSUMED, "是非成败转头空");
                intent.putExtra(LEVEL_ID, levelId);
                startActivity(intent);
            }
        }.start();
        mTimerRunning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Timer", "Timer on " + mTimeLeftInMillis);
        if (mTimeLeftInMillis < 1000) {
            Log.i("Timer", "Timer");
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    public void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public void setViewText() {
        if(paintboard.getSteps()==0){
            backBtn.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concrete));
        }
        else{
            backBtn.setBackgroundColor(getResources().getColor(R.color.fbutton_color_belize_hole));
            backBtn.setTextColor(getResources().getColor(R.color.fbutton_color_clouds));
        }
        stepsview.setText(Integer.toString(paintboard.getSteps())+"步");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Log.d(TAG, "MainActivity onWindowsChange");
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
            Log.d(TAG, "screenHeight:" + screenHeight + " screenWidth:" + screenWidth);
            paintboard = findViewById(R.id.paint_board);
            ViewGroup.LayoutParams layoutParams = paintboard.getLayoutParams();
            layoutParams.width = 800;
            layoutParams.height = 1000;
            paintboard.setLayoutParams(layoutParams);
            paintboard.setWidthHeight(800, 998);
            Log.d(TAG, "onCreate: width = " + paintboard.getWidth() + "  height = " + paintboard.getHeight());
        } else {
            Log.d(TAG, "onWindowFocusChanged:" + "false");
        }
    }

    public void back_btn_click(View view) {
        if(paintboard.backClicked()==false){
            Toast.makeText(getApplicationContext(), "不能回退咯宝贝(●'◡'●)", Toast.LENGTH_SHORT).show();
        }
        else{
            if(paintboard.getSteps()==0){
                backBtn.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concrete));
            }
            else{
                backBtn.setBackgroundColor(getResources().getColor(R.color.fbutton_color_belize_hole));
                backBtn.setTextColor(getResources().getColor(R.color.fbutton_color_clouds));
            }
            stepsview.setText(Integer.toString(paintboard.getSteps())+"步");
        }
    }


    public void replay(View view) {
        paintboard.replay(levelId);
        pauseTimer();
        resetTimer();
        startTimer();
    }

    public void showResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        long timeConsumed = START_TIME_IN_MILLIS - mTimeLeftInMillis;
        String time_consumed;
        int minutes = (int) (timeConsumed / 1000) / 60;
        int seconds = (int) (timeConsumed / 1000) % 60;
        time_consumed = "总共用时" + String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        intent.putExtra(MESSAGE_LOVE, "挑战成功");
        intent.putExtra(TIME_CONSUMED, time_consumed);
        intent.putExtra(LEVEL_ID, levelId);
        startActivity(intent);
    }
}
