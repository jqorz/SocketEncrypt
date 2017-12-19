package com.jqorz.demo.socketencrypt.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        init();
    }

    protected void getIntentData() {
    }

    protected abstract void init();

    protected abstract int getLayoutResId();
}