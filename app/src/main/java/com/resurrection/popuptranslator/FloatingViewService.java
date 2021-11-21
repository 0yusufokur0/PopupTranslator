package com.resurrection.popuptranslator;

import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.resurrection.popuptranslator.ui.main.MainActivity;

public abstract class FloatingViewService<T extends ViewDataBinding> extends Service {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private WindowManager mWindowManager;
    View mFloatingView;
    private WindowManager.LayoutParams params;
    private int layoutId;
    T binding;

    public FloatingViewService (int layoutId) {
        this.layoutId = layoutId;
    }

    abstract void init();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingView = LayoutInflater.from(this).inflate(layoutId,null);
        binding = DataBindingUtil.bind(mFloatingView);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
         else
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        params = new WindowManager.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                LAYOUT_FLAG,
                FLAG_NOT_FOCUSABLE,
                TRANSLUCENT);
        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        init();
    }

    public View.OnTouchListener moveableTouchListener(@NonNull boolean openAppWhenClicked){
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (openAppWhenClicked) if (Xdiff < 10 && Ydiff < 10) openApp(); //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking. //So that is click event.

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        };
        return onTouchListener;
    }

    public View.OnTouchListener moveableTouchListener(){
        return moveableTouchListener(false);
    }

    public void addMyView() {
        //Add the view to the window
        if (mFloatingView != null) {
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mFloatingView, params);
        }
    }

    public void openApp(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fromwhere", "ser");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}



