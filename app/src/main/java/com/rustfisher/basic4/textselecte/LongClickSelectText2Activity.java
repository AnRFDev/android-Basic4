package com.rustfisher.basic4.textselecte;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.AbsAct;
import com.rustfisher.basic4.R;
import com.rustfisher.basic4.activity.LongSelectText1Activity;

public class LongClickSelectText2Activity extends AbsAct {
    SelectableTextHelper mSelectableTextHelper;
    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_long_click_select_text2);
        mTv = findViewById(R.id.tv2);
        mTv.setText(LongSelectText1Activity.mContent);
        mSelectableTextHelper = new SelectableTextHelper.Builder(mTv)
                .setSelectedColor(Color.GRAY)
                .setCursorHandleSizeInDp(18)
                .setCursorHandleColor(Color.BLUE)
                .build();
        mSelectableTextHelper.setOnNotesClickListener(new OnNoteBookClickListener() {
            @Override
            public void onTextSelect(CharSequence charSequence) {
                String content = charSequence.toString();
                Toast.makeText(getApplicationContext(), "点击的是:" + content, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
