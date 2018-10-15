package com.rustfisher.basic4.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rustfisher.basic4.R;

/**
 * 截屏功能
 */
public class ScreenshotActivity extends AppCompatActivity {
    ImageView mShotIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_screenshot);
        mShotIv = findViewById(R.id.shot_iv);
        findViewById(R.id.take_screenshot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShotIv.setImageBitmap(screenshotWholeScreen(0.10, 0.10));
            }
        });
    }

    /**
     * 对当前整个界面截图并裁剪
     *
     * @param topRatio    顶部裁掉的比例
     * @param bottomRatio 底部裁掉的比例
     */
    private Bitmap screenshotWholeScreen(double topRatio, double bottomRatio) {
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        int top = (int) (bitmap.getHeight() * topRatio);
        int targetHeight = (int) (bitmap.getHeight() * (1 - topRatio - bottomRatio));
        return Bitmap.createBitmap(bitmap, 0, top, bitmap.getWidth(), targetHeight);
    }
}
