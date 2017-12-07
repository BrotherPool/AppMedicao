package com.example.user.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class testederange extends AppCompatActivity {

    TextView mTxvSeekBarValue;
    SeekBar mSkbSample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testederange);
        mTxvSeekBarValue = (TextView) this.findViewById(R.id.txvSeekBarValue);
        mSkbSample = (SeekBar) this.findViewById(R.id.skbSample);
        mSkbSample.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ShowSeekValue((int)event.getX(), mTxvSeekBarValue.getTop());
                    mTxvSeekBarValue.setVisibility(View.VISIBLE);
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    ShowSeekValue((int)event.getX(), mTxvSeekBarValue.getTop());
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ShowSeekValue((int)event.getX(), mTxvSeekBarValue.getTop());
                    //mTxvSeekBarValue.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    private void ShowSeekValue(int x, int y)
    {
        if(x > 0 && x < mSkbSample.getWidth())
        {
            AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, x, y);
            mTxvSeekBarValue.setLayoutParams(lp);
            mTxvSeekBarValue.setText(""+mSkbSample.getProgress());
        }
    }

}
