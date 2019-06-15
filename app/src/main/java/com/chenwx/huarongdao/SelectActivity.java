package com.chenwx.huarongdao;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;

public class SelectActivity extends AppCompatActivity {

    public static final String LEVEL_ID = "LEVEL_ID";

    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;

    private String TAG = "SelectActivity";

    private static SelectActivity selectActivity;
    public static SelectActivity getSelectActivity(){
        return selectActivity;
    }
    public SelectActivity(){
        selectActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        TypedArray chesses_temp;
        chesses_temp = getResources().obtainTypedArray(R.array.chesses);
        for(int i = 0;i<chesses_temp.length();++i){
            mWordList.addLast(getResources().getResourceEntryName(chesses_temp.getResourceId(i, -1)));
        }

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ListAdapter(this, mWordList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void selectLevel(int position) {
        Intent intent = new Intent(this, GameActivity.class);
        String position_str = Integer.toString(position);
        intent.putExtra(LEVEL_ID, position_str);
        startActivity(intent);
        this.finish();
    }
}
