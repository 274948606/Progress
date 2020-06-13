package com.calm.progress.base;

import android.app.Activity;
import android.os.Bundle;

import com.calm.injectlay.InjectUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Create by Calm
 * 2020/6/12 20:55
 * 基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    protected abstract void init();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //只需在父类调用这句，然后在使用的地方进行注解ContentView即可自动注入布局 不需要在写onCreate
        InjectUtil.inject(this);
        mActivity = this;
        init();
    }
}
