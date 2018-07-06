package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.annotation.FindView;
import com.rustfisher.basic4.annotation.FindViewHelper;

/**
 * 注解
 * Created by Rust on 2018/7/6.
 */
public class AnnotationDemoActivity extends Activity {

    @FindView(idValue = R.id.tv)
    TextView mTv1;
    @FindView(idValue = R.id.btn)
    Button mBtn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_a_demo);
        FindViewHelper.initFindView(this);

        mTv1.setText("已经被注解的方式拿到了");
        mBtn1.setText("注解");
    }
}
