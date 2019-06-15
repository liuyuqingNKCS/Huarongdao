package com.chenwx.huarongdao;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private TextSurface textSurface;
    private TextView startTv;
    private  FloatingActionButton fab;
    private  FloatingActionButton fab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        Text textDaai = TextBuilder
                .create("华容道")
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.BLACK)
                .setPosition(Align.SURFACE_CENTER).build();
        Text textBraAnies = TextBuilder
                .create("从此道可至华容也")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF, textDaai).build();

        textSurface = findViewById(R.id.textSurface);
        startTv = findViewById(R.id.startTv);
        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textDaai, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textDaai, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500)
                )
        );
        startTv.setVisibility(View.VISIBLE);
        Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_action_notify);
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        fab2.setImageDrawable(willBeWhite);
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

    public void helpInformation(View view){
        Log.d("MainActivity", "help");
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(R.id.fab)
                .setPrimaryText("What is Everything?")
                .setSecondaryText(R.string.help_information)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                        }
                    }
                })
                .show();
    }

    public void notifyInformation(View view){
        Log.d("MainActivity", "help");
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(R.id.fab2)
                .setPrimaryText("What is New?!")
                .setBackgroundColour(getResources().getColor(R.color.fbutton_color_wet_asphalt))
                .setSecondaryText(R.string.notify_information)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                        }
                    }
                })
                .show();
    }
}
