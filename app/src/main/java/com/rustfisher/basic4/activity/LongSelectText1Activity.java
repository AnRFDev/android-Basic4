package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Text内容分段点击
 * Created on 2020-1-15
 */
public class LongSelectText1Activity extends Activity {
    private static String TAG = "rustApp";
    EditText mEt;
    private TextView mTv;
    public static String mContent = "We live in a busy world, full of distractions. And finding some me time and peace during the chaos of the day isn’t an easy task anymore. But without taking breaks and resting properly we can’t work effectively. In fact, we can’t get anything done, focus or even get closer to our goals.\n" +
            "\n" +
            "That’s because being busy and tired all the time prevents us from turning our brains off even when it’s time to relax, we can’t sleep well as a result of that, and become depressed and stressed over time.\n" +
            "\n" +
            "But there’s a solution.\n" +
            "\n" +
            "Vacation time allows you to experience physical and emotional benefits\n" +
            "You may have many things on your to-do list and truly want to do your best job and feel accomplished in the end of the day. But you also want to stay healthy and be on top of your game. That’s why you’re in need of a vacation. Once you’ve dedicated all your time and energy to work and daily tasks, though, leaving them for a while doesn’t seem like the right thing to do. You may think that things will get out of control, or that you’ll be left behind and will have to hustle even more after that.\n" +
            "\n" +
            "But these are minor issues considering the actual benefits of vacations. Because, believe it or not, they are great for your health and productivity. Here’s why:\n" +
            "1. Taking a vacation helps you reduce stress.\n" +
            "There’s nothing like leaving the stressful environment of your daily life and entering a new world, full of excitement. The study carried out be American Sociological Association shows that a bigger number of vacations leads to a decline in the psychological distress of people. And when the average worker takes more vacations per year, that becomes a beneficial determinant of population health.\n" +
            "2. Vacation frequency is related to mortality.\n" +
            "According to another study on whether vacations are good for our health, “The frequency of annual vacations by middle-aged men at high risk for CHD is associated with a reduced risk of all-cause mortality and, more specifically, mortality attributed to CHD. Vacationing may be good for your health.” While this study discussed middle-aged men, this works for all people! Whether you are a hard-working student or a few years from retirement, you deserve a vacation.\n" +
            "3. It helps you grow spiritually.\n" +
            "One of the most important aspects of having a vacation is that you get to know yourself better. You learn new things throughout the journey, but you also experience changes on the inside. And once you get back to reality, you can benefit from these by trying new techniques to be more productive in life and in business, for example.\n" +
            "4. You improve your mental health.\n" +
            "It’s great for your brain too. Seeing new places and putting yourself in an unfamiliar environment are great for expanding your horizons, but it also improves your thinking and creativity.\n" +
            "\n" +
            "5. Frequent vacations lead to a happier marriage.\n" +
            "The Wisconsin Rural Women’s Health Study says that females who get vacations more often are not only less depressed and have more energy in general, but are also more satisfied with their marriage.\n" +
            "\n" +
            "But then there are those who really can’t afford to take a vacation. Be it because their work is too demanding, are starting a new job, have many things to take care of at home, or else. However, there’s another, even simpler, solution for them. It doesn’t require going away for a vacation, but it does offer similar benefits.\n" +
            "That’s how you can be relaxed, productive and healthier without having to take a vacation.\n";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_text_view_span);
        mTv = findViewById(R.id.span_tv);
        mTv.setText(mContent);

        SpannableString spannableInfo = new SpannableString("This is a text: " + ": " + "click me");
        spannableInfo.setSpan(new Clickable(clickListener), 16, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv.setText(spannableInfo);
        mTv.setMovementMethod(LinkMovementMethod.getInstance());

        mEt = findViewById(R.id.et1);
        mEt.setText(mContent);
        mEt.setCustomSelectionActionModeCallback(genActionModeCallback2());

    }

    private ActionMode.Callback genActionModeCallback2() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return new ActionMode.Callback2() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    Log.d(TAG, "onCreateActionMode:"
                            + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    int start = mEt.getSelectionStart();
                    int end = mEt.getSelectionEnd();
                    Log.d(TAG, "onPrepareActionMode: " + " selected: " + start + ", " + end);
                    if (start != end) {
                        String selectedText = (String) mContent.subSequence(start, end);
                        Log.d(TAG, "onPrepareActionMode: selected: " + selectedText);
                    }
                    menu.clear();
                    mode.getMenuInflater().inflate(R.menu.et_menu, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Log.d(TAG, "onActionItemClicked: " + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                    mode.finish();
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    Log.d(TAG, "onDestroyActionMode: " + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                }

                @Override
                public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                    super.onGetContentRect(mode, view, outRect);
                }
            };
        }
        return null;
    }

    private ActionMode.Callback genActionModeCallback1() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.d(TAG, "onCreateActionMode:"
                        + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Log.d(TAG, "onPrepareActionMode: " + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                menu.clear();
                mode.getMenuInflater().inflate(R.menu.et_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.d(TAG, "onActionItemClicked: " + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                mode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.d(TAG, "onDestroyActionMode: " + " selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
            }
        };
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Log.d(TAG, "onMenuItemSelected: selected: " + mEt.getSelectionStart() + ", " + mEt.getSelectionEnd());
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//
//        if (v.getId() == R.id.et1) {
//            menu.add(0, 1, 0, "Fetch New Username");
//            menu.add(0, 2, 0, "Check For Duplicate");
//        }
    }

    private List<Integer> getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return indices;
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();
                Log.d("rustApp", s);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "点击成功....", Toast.LENGTH_SHORT).show();
        }
    };

    class Clickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
//            ds.setColor(getResources().getColor(R.color.video_comment_like_number));
        }
    }
}
