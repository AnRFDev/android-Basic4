package com.rustfisher.appndkground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rustfisher.appndkground.frag.FunctionListFragment;

public class NdkMainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ndk_main);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new FunctionListFragment()).commit();

    }

}
