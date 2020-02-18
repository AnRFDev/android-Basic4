package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.R;

/**
 * Created on 2020-2-14
 */
public class TextViewSpanAct extends Activity {
    private static final String TAG = "rustAppTvSpan";

    private TextView mTv1;
    private EditText mEt1;
    ForegroundColorSpan mColorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));

    private String mRefStr = "This is the ref string. Copy me.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tv_span_1);

        mTv1 = findViewById(R.id.tv1);
        mEt1 = findViewById(R.id.et1);
        mEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = cmpIgnoreCase(mRefStr, s.toString());
                if (index > 0) {
                    SpannableString spannableString = new SpannableString(mRefStr);
                    spannableString.setSpan(mColorSpan, 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    mTv1.setText(spannableString);
                } else {
                    mTv1.setText(mRefStr);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTv1.setText(mRefStr);


    }

    public static int cmpStr(String ref, String input) {
        int res = -1;
        if (TextUtils.isEmpty(input)) {
            return res;
        }
        for (int i = 0; i < input.length(); i++) {
            if (i < ref.length() && input.charAt(i) == ref.charAt(i)) {
                res = i + 1;
            } else {
                break;
            }
        }
        return res;
    }

    public static int cmpIgnoreCase(String ref, String input) {
        return cmpStr(ref.toLowerCase(), input.toLowerCase());
    }
}
