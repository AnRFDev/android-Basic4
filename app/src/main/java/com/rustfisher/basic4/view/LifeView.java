package com.rustfisher.basic4.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * For lifecycle
 * Created by Rust on 2018/5/31.
 */
public class LifeView extends View {
    private static final String TAG = "rustAppLifeView";
    private static final String LOG_PRE = "View生命周期 ";
    private Paint mPaint = new Paint();

    public LifeView(Context context) {
        super(context, null);
        Log.d(TAG, LOG_PRE + "LifeView(Context context)");
    }

    public LifeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, LOG_PRE + "LifeView(Context context, @Nullable AttributeSet attrs)");
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, LOG_PRE + "onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, LOG_PRE + "onAttachedToWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, LOG_PRE + "onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, LOG_PRE + "onSizeChanged " + "; size: " + w + ", " + h);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, LOG_PRE + "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Lifecycle", 0, 0, mPaint);
        Log.d(TAG, LOG_PRE + "onDraw, " + System.currentTimeMillis());
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, LOG_PRE + "onWindowFocusChanged" + "  " + hasWindowFocus);
    }
}
