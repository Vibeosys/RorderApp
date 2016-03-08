package com.vibeosys.rorderapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by akshay on 07-03-2016.
 */
public class CustomCardView extends CardView {

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intiateComponenet();
    }

    private void intiateComponenet() {
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
