package com.chenwx.huarongdao;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Map;
import java.util.Stack;

public class PaintBoard extends View {

    private int startX, startY;
    private String TAG = "PaintBoard";
    private int len_unit;
    private int len_unitY;
    private chess Chesses[];
    private Paint mPaint = null;
    private Paint mLTPaint = null;
    private Paint mRBPaint = null;
    private Paint RBPaint = null;
    private Canvas mCanvas = null;

    public int margin = 10;
    private int matchedId = -1;

    private int mWidth;
    private int mHeight;
    private int direction;

    private Stack<int[]> backs;
    private int steps;

    private Map idNameMap;


    public PaintBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "PaintBoard PaintBoard");
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.fbutton_color_carrot));
        mLTPaint = new Paint();
        mLTPaint.setColor(getResources().getColor(R.color.fbutton_color_sun_flower));
//        mLTPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_OVER));
        mRBPaint = new Paint();
//        mRBPaint.setColor(getResources().getColor(R.color.fbutton_color_carrot));
        mRBPaint.setColor(getResources().getColor(R.color.fbutton_color_pomegranate));
//        mRBPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_OVER));


        mCanvas = new Canvas();
        mCanvas.drawColor(Color.GRAY);


        direction = -1;
        backs = new Stack<>();
    }

    public void initChesses(String levelId) {
        String[] chesses_get;
        TypedArray chesses_temp;
        chesses_temp = getResources().obtainTypedArray(R.array.chesses);
        Log.d(TAG, "getResources().getResourceEntryName(chesses_temp.getResourceId(Integer.parseInt(levelId), -1))"+getResources().getResourceEntryName(chesses_temp.getResourceId(Integer.parseInt(levelId), -1)));
        chesses_get = getResources().getStringArray(chesses_temp.getResourceId(Integer.parseInt(levelId), -1));
        Log.d(TAG, "chesses_get.length:"+chesses_get.length);
        Chesses = new chess[chesses_get.length];
        chesses_temp.recycle();
        for (int i = 0; i < chesses_get.length; ++i) {
            String[] temp = chesses_get[i].split(" ");
            Chesses[i] = new chess(Integer.parseInt(temp[0]) + margin, Integer.parseInt(temp[1]) + margin, Integer.parseInt(temp[2]) - margin, Integer.parseInt(temp[3]) - margin);
            Log.d("PaintBoard chess", " " + Chesses[i].x + "," + Chesses[i].y + "," + Chesses[i].width + "," + Chesses[i].height);
        }
    }

    public void adjustChesses(String levelId) {
        TypedArray chesses_temp;
        String[] chesses_get;
        chesses_temp = getResources().obtainTypedArray(R.array.chesses);
        chesses_get = getResources().getStringArray(chesses_temp.getResourceId(Integer.parseInt(levelId), -1));
        chesses_temp.recycle();
        for (int i = 0; i < chesses_get.length; ++i) {
            String[] temp = chesses_get[i].split(" ");
            Chesses[i].adjustChess(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            Log.d("PaintBoard chess", " " + Chesses[i].x + "," + Chesses[i].y + "," + Chesses[i].width + "," + Chesses[i].height);
        }
    }

    public void setWidthHeight(int width, int height) {
        Log.d(TAG, "PaintBoard setWidthHeight");
        this.mHeight = height;
        this.mWidth = width;
        this.len_unit = (int) (width / 4);
        this.len_unitY = (int) (height / 5);
        for (int i = 0; i < this.Chesses.length; ++i) {
            Chesses[i].x = ((int) Math.rint(Chesses[i].x / 200.0)) * len_unit;
            Chesses[i].y = ((int) Math.rint(Chesses[i].y / 200.0)) * len_unitY;
            Chesses[i].width = ((int) Math.rint(Chesses[i].width / 200.0)) * len_unit;
            Chesses[i].height = ((int) Math.rint(Chesses[i].height / 200.0)) * len_unitY;
            Log.d("PaintBoard", "setWidthHeight chess:" + Chesses[i].x + "," + Chesses[i].y + "," + Chesses[i].width + "," + Chesses[i].height);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged len_unit:" + len_unit + "len_unitY" + len_unitY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paint_margin = margin*2;
        Log.d(TAG, "PaintBoard onDraw");
        for (int i = 0; i < Chesses.length; ++i) {
            canvas.drawRect(Chesses[i].x, Chesses[i].y, Chesses[i].x + Chesses[i].width, Chesses[i].y + Chesses[i].height, mRBPaint);
            canvas.drawRect(Chesses[i].x , Chesses[i].y, Chesses[i].x + Chesses[i].width-paint_margin, Chesses[i].y + Chesses[i].height-paint_margin, mLTPaint);
            canvas.drawRect(Chesses[i].x + paint_margin, Chesses[i].y+paint_margin, Chesses[i].x + Chesses[i].width-paint_margin, Chesses[i].y + Chesses[i].height-paint_margin, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "~~~~~~~~~~~~~~~~~~ACTION_DOWN");
                startX = (int) event.getX();
                startY = (int) event.getY();
                matchedId = matchId(startX, startY);
                direction = -1;
                if (matchedId >= 0) {
                    int[] temp = {matchedId, Chesses[matchedId].x, Chesses[matchedId].y};
                    pushInBacks(temp);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (matchedId >= 0) {
                    Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~ACTION_MOVE");
                    int stopX = (int) event.getX();
                    int stopY = (int) event.getY();
                    moveAction(stopX, stopY);
                    invalidate();//call onDraw()
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (matchedId >= 0) {
                    Log.d(TAG, "~~~~~~~~~~~~~~ACTION_CANCEL");
                    int stopX = (int) event.getX();
                    int stopY = (int) event.getY();
                    moveActionAfter(stopX, stopY);
                    invalidate();//call onDraw();
                    int[] temp = {matchedId, Chesses[matchedId].x, Chesses[matchedId].y};
                    confirmBacks(temp);
                    GameActivity.getGameActivity().setViewText();
                    isSuccess(matchedId);
                }
        }
        return true;
    }

    public void isSuccess(int matchedId) {
        Log.d(TAG, "isSuccess");
        Log.d(TAG, "Chesses[matchedId].isGoal=" + Chesses[matchedId].isGoal);
        Log.d(TAG, "Chesses[matchedId].x/len_unit = " + Chesses[matchedId].x / len_unit);
        Log.d(TAG, "Chesses[matchedId].y/len_unitY = " + Chesses[matchedId].y / len_unitY);
        if (Chesses[matchedId].isGoal == true) {
            if (Chesses[matchedId].x / len_unit == 1 && Chesses[matchedId].y / len_unitY == 3) {
                GameActivity.getGameActivity().pauseTimer();
                GameActivity.getGameActivity().showResult();
            }
        }
    }

    public void confirmBacks(int[] temp) {
        if (!backs.empty()) {
            int[] top = backs.peek();
            int i;
            for (i = 0; i < top.length; ++i) {
                if (top[i] != temp[i]) break;
            }
            if (i == top.length) {
                backs.pop();
                steps -= 1;
                return;
            }
        }
    }

    public void pushInBacks(int[] temp) {
        backs.push(temp);
        steps += 1;
    }

    public boolean backClicked() {
        if (backs.empty()) {
            return false;
        }
        int[] backToState = backs.pop();
        Chesses[backToState[0]].adjustChess(backToState[1], backToState[2]);
        invalidate();
        steps -= 1;
        return true;
    }

    public int getSteps() {
        return steps;
    }

    public int matchId(int x, int y) {
        for (int i = 0; i < Chesses.length; ++i) {
            if (Chesses[i].isIn(x, y)) {
                Log.d(TAG, "matchedId->" + i);
                return i;
            }
        }
        return -1;
    }


    public void moveActionAfter(int x, int y) {
        int diffX = 0;
        int diffY = 0;
        if (direction == 0) {
            int delta = (Chesses[matchedId].y + diffY) % len_unitY;
            Log.d(TAG, "delta:" + delta);
            Log.d(TAG, "len_unitY:" + len_unitY);
            Log.d(TAG, "(int)0.5*len_unitY:" + (int) ((0.5) * len_unitY));
            Chesses[matchedId].y = (delta > (int) ((0.5) * len_unitY)) ? (Chesses[matchedId].y + diffY + (len_unitY - delta)) : (Chesses[matchedId].y + diffY - delta);
        } else if (direction == 1) {
            int delta = (Chesses[matchedId].x + diffX) % len_unit;
            Log.d(TAG, "delta:" + delta);
            Log.d(TAG, "len_unit:" + len_unit);
            Log.d(TAG, "(int)0.5*len_unit:" + (int) ((0.5) * len_unit));
            Chesses[matchedId].x = (delta > (int) ((0.5) * len_unit)) ? (Chesses[matchedId].x + diffX + (len_unit - delta)) : (Chesses[matchedId].x + diffX - delta);
        } else if (direction == 2) {
            int delta = (Chesses[matchedId].y + diffY) % len_unitY;
            Log.d(TAG, "delta:" + delta);
            Log.d(TAG, "len_unitY:" + len_unitY);
            Log.d(TAG, "(int)0.5*len_unitY:" + (int) ((0.5) * len_unitY));
            Chesses[matchedId].y = (delta > (int) ((0.5) * len_unitY)) ? (Chesses[matchedId].y + diffY + (len_unitY - delta)) : (Chesses[matchedId].y + diffY - delta);
        } else if (direction == 3) {
            int delta = (Chesses[matchedId].x + diffX) % len_unit;
            Log.d(TAG, "delta:" + delta);
            Log.d(TAG, "len_unit:" + len_unit);
            Log.d(TAG, "(int)0.5*len_unit:" + (int) ((0.5) * len_unit));
            Chesses[matchedId].x = (delta > (int) ((0.5) * len_unit)) ? (Chesses[matchedId].x + diffX + (len_unit - delta)) : (Chesses[matchedId].x + diffX - delta);
        }
    }

    public void moveAction(int x, int y) {
        int diffX = x - startX;
        int diffY = y - startY;
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX <= 0) {
                Log.d(TAG, "left");
                Log.d(TAG, "(Chesses[matchedId].y)%len_unitY" + (Chesses[matchedId].y) % len_unitY);
                if ((direction == 0 || direction == 2) && (Chesses[matchedId].y + diffY) % len_unitY != 0) {
                    Log.d(TAG, "can't left");
                    Log.d(TAG, "(Chesses[matchedId].y+diffY)%len_unitY" + (Chesses[matchedId].y + diffY) % len_unitY);
                    int delta = (Chesses[matchedId].y + diffY) % len_unitY;
                    Chesses[matchedId].y = (delta > (int) ((0.5) * len_unitY)) ? (Chesses[matchedId].y + diffY + (len_unitY - delta)) : (Chesses[matchedId].y + diffY - delta);
                    return;
                }
                if (Chesses[matchedId].x + diffX > 0) {
                    if (canMove(Chesses[matchedId].x + diffX, Chesses[matchedId].y)) {
                        Log.d(TAG, "can left");
                        Chesses[matchedId].x += diffX;
                        direction = 3;
                    }
                }
            } else if (diffX > 0) {

                Log.d(TAG, "Right");
                if ((direction == 0 || direction == 2) && (Chesses[matchedId].y + diffY) % len_unitY != 0) {
                    Log.d(TAG, "can't right");
                    Log.d(TAG, "(Chesses[matchedId].y+diffY)%len_unitY" + (Chesses[matchedId].y + diffY) % len_unitY);
                    int delta = (Chesses[matchedId].y + diffY) % len_unitY;
                    Chesses[matchedId].y = (delta > (int) ((0.5) * len_unitY)) ? (Chesses[matchedId].y + diffY + (len_unitY - delta)) : (Chesses[matchedId].y + diffY - delta);
                    return;
                }
                if (Chesses[matchedId].x + diffX + Chesses[matchedId].width < this.mWidth) {
                    if (canMove(Chesses[matchedId].x + diffX, Chesses[matchedId].y)) {
                        Log.d(TAG, "can right");
                        Chesses[matchedId].x += diffX;
                        direction = 1;
                    }
                }
            }
        } else {
            if (diffY <= 0) {
                Log.d(TAG, "up");
                if ((direction == 1 || direction == 3) && (Chesses[matchedId].x + diffX) % len_unit != 0) {
                    Log.d(TAG, "can't up");
                    Log.d(TAG, "" + (Chesses[matchedId].x + diffX) % len_unit);
                    int delta = (Chesses[matchedId].x + diffX) % len_unit;
                    Chesses[matchedId].x = (delta > (int) ((0.5) * len_unit)) ? (Chesses[matchedId].x + diffX + (len_unit - delta)) : (Chesses[matchedId].x + diffX - delta);
                    return;
                }
                if (Chesses[matchedId].y + diffY > 0) {
                    if (canMove(Chesses[matchedId].x, Chesses[matchedId].y + diffY)) {
                        Log.d(TAG, "can up");
                        Chesses[matchedId].y += diffY;
                        direction = 0;
                    }
                }
            } else if (diffY > 0) {
                Log.d(TAG, "down");
                if ((direction == 1 || direction == 3) && (Chesses[matchedId].x + diffX) % len_unit != 0) {
                    Log.d(TAG, "can't down");
                    Log.d(TAG, "(Chesses[matchedId].x+diffX)%len_unit" + (Chesses[matchedId].x + diffX) % len_unit);
                    int delta = (Chesses[matchedId].x + diffX) % len_unit;
                    Chesses[matchedId].x = (delta > (int) ((0.5) * len_unit)) ? (Chesses[matchedId].x + diffX + (len_unit - delta)) : (Chesses[matchedId].x + diffX - delta);
                    return;
                }
                if (Chesses[matchedId].y + diffY + Chesses[matchedId].height < this.mHeight) {
                    if (canMove(Chesses[matchedId].x, Chesses[matchedId].y + diffY)) {
                        Log.d(TAG, "can down");
                        Chesses[matchedId].y += diffY;
                        direction = 2;
                    }
                }
            }
        }
    }

    public boolean canMove(int x, int y) {
        chess chessTemp = new chess(x, y, Chesses[matchedId].width, Chesses[matchedId].height);
        Log.d(TAG, "canMove? at (" + x + "," + y + "," + (x + Chesses[matchedId].width) + "," + (y + Chesses[matchedId].height) + ")");
        for (int i = 0; i < Chesses.length; ++i) {
            if (i == matchedId) {
            } else {
                if (!isNotAdjacent(chessTemp, Chesses[i])) {
                    Log.d(TAG, "Id:" + matchedId + " can't move with " + i);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isNotAdjacent(chess a, chess b) {
        return a.x + a.width <= b.x || a.x >= b.x + b.width || a.y >= b.y + b.height || b.y >= a.y + a.height;
    }

    public void replay(String levelId) {
        this.adjustChesses(levelId);
        direction = -1;
        backs.clear();
        steps = 0;
        invalidate();
        GameActivity.getGameActivity().setViewText();
    }
}
