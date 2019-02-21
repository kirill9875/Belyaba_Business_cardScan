package com.blogspot.atifsoftwares.imagetotextapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author fonclub <sfonclub@gmail.com>
 * @created 06.04.2017.
 */

public class SwipeTouchListener implements View.OnTouchListener {

    private static final String LOG_TAG = "SwipeTouchListener";
    private Activity activity;
    private LinearLayout liner;
    private static int MIN_DISTANCE;
    private float downX;
    private float downY;
    private int id;
    SQLiteDatabase DB;

    public SwipeTouchListener(Activity _activity, LinearLayout _liner, int _id, SQLiteDatabase _DB) {
        activity = _activity;
        liner = _liner;
        id = _id;
        DB = _DB;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
    }

    private void onRightToLeftSwipe() {
        Log.i(LOG_TAG, "Справа налево!");
    }

    private void onLeftToRightSwipe() {
        Log.i(LOG_TAG, "Слева направо!");
        // удаляем файл или делаем любое действие с активити
        activity.deleteFile("file");
    }

    private void onTopToBottomSwipe() {
        Log.i(LOG_TAG, "Сверху вниз!");
    }

    private void onBottomToTopSwipe() {
        Log.i(LOG_TAG, "Снизу вверх!");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int noxx = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                noxx = (int)event.getX() + (int)liner.getX();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // горизонтальный свайп
                if (Math.abs(deltaX) > MIN_DISTANCE) { // если дистанция не меньше минимальной
                    // слева направо
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    //справа налево
                    if (deltaX > 0) {
                        System.out.print(1);
                        View par = (View) liner.getParent();
                        ((LinearLayout) par).removeView(liner);
                        DB.delete(DBHelper.TABLE_NAME, "_id = " + id, null);
                        return true;
                    }
                }

                // вертикальный свайп
                if (Math.abs(deltaY) > MIN_DISTANCE) { //если дистанция не меньше минимальной
                    // сверху вниз
                    if (deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    // снизу вверх
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                }

                return false;
            }
        }
        return false;
    }
}