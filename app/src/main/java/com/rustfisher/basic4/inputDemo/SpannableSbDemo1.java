package com.rustfisher.basic4.inputDemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * spannable string builder
 */
public class SpannableSbDemo1 extends Activity {
    private static final String TAG = "rustApp";

    private static final String ORIGIN_TEXT = "This is <<99>> text. And there <<100>> people.";
    private static final String REG = "<<(.*?)>>"; // Java正则表达式，截取尖括号里的内容
    private TextView mTv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_spannable_sb_demo1);
        mTv1 = findViewById(R.id.tv1);

        splitWord(ORIGIN_TEXT);
        splitWord("<<1>> is a good day.");
        splitWord("Today is a good <<2>>");
    }

    private class TextItem {
        private String en;
        private String ch;
        private int questionCount; // 几个问题  每个问题对应的状态是什么

        public String text;
        public boolean isSpace = false;
        public String uiTxt; // 用来显示的

        public TextItem(String text, boolean isSpace) {
            this.text = text;
            this.isSpace = isSpace;
        }

        public String getUiTxt() {
            return uiTxt;
        }
    }

    Pattern p = Pattern.compile("<<(.*?)>>");

    private void readInText(String input) {
        Matcher matcher = p.matcher(input);
        String workTxt = input;
        while (matcher.find()) {
//            Log.d(TAG, "readInText: " + matcher.group(1));
            workTxt = workTxt.replaceFirst("<<(.*?)>>", "_____________");
            Log.d(TAG, "readInText: " + workTxt);
        }

        for (String s : input.split(REG)) {
            Log.d(TAG, "->" + s);
        }
    }

    private void splitWord(String input) {
        String[] sp = input.split(REG);
        Log.d(TAG, "splitWord: length: " + sp.length);
        for (String s : sp) {
            Log.d(TAG, "->" + s);
        }
    }
}
