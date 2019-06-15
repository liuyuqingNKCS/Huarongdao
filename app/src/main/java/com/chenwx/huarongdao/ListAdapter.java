package com.chenwx.huarongdao;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.Shape;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends android.support.v7.widget.RecyclerView.Adapter<ListAdapter.ItemViewHolder> {

    private final LinkedList<String> mLevelList;
    private LayoutInflater mInflater;
    private static String TAG = "ListAdapter";


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final Button ButtonItemView;
        final ListAdapter mAdapter;

        public ItemViewHolder(View itemView, ListAdapter adapter) {
            super(itemView);
            ButtonItemView = itemView.findViewById(R.id.item);
            this.mAdapter = adapter;
            ButtonItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int mPosition = getLayoutPosition();
            Log.d(TAG, ""+mPosition);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            final float pos_x = location[0];
            final float pos_y = location[1];
            final float view_width = view.getWidth();
            final float view_height = view.getHeight();
            String[] chesses_temp;
            chesses_temp = SelectActivity.getSelectActivity().getResources().getStringArray(R.array.descriptions);
            final String description = chesses_temp[mPosition];
            Log.d(TAG, ""+pos_x+","+pos_y+","+view_width+","+view_height);
            SimpleTarget simpleTarget = new SimpleTarget.Builder(SelectActivity.getSelectActivity())
                    .setPoint(pos_x, pos_y)
                    .setDuration(1L)
                    .setShape(new Shape() {
                        private float x = pos_x;
                        private float y = pos_y;
                        private float width = view_width;
                        private float height = view_height;

                        @Override
                        public void draw(Canvas canvas, PointF point, float value, Paint paint) {
                            canvas.drawRect(pos_x, pos_y, view_width+pos_x, view_height+pos_y, paint);
                        }

                        @Override
                        public int getHeight() {
                            return (int)view_height;
                        }

                        @Override
                        public int getWidth() {
                            return (int)view_width;
                        }
                    }) // or RoundedRectangle()
                    .setTitle(ButtonItemView.getText().toString())
                    .setDescription(description)
                    .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                        @Override
                        public void onStarted(SimpleTarget target) {
                            // do something
                        }

                        @Override
                        public void onEnded(SimpleTarget target) {
                            // do something
                        }
                    })
                    .build();
            Spotlight.with(SelectActivity.getSelectActivity())
                    .setOverlayColor(R.color.background)
                    .setDuration(1L)
                    .setAnimation(new DecelerateInterpolator(2f))
                    .setTargets(simpleTarget)
                    .setClosedOnTouchedOutside(true)
                    .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                        @Override
                        public void onStarted() {
                            //Toast.makeText(SelectActivity.getSelectActivity(), "spotlight is started", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onEnded() {
                            SelectActivity.getSelectActivity().selectLevel(mPosition);
                        }
                    })
                    .start();
        }

    }

    public ListAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mLevelList = wordList;
    }
    @NonNull
    @Override
    public ListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mItemView = mInflater.inflate(R.layout.list_item,
                parent, false);
        return new ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ItemViewHolder itemViewHolder, int i) {
        String mCurrent = mLevelList.get(i);
        itemViewHolder.ButtonItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mLevelList.size();
    }
}
