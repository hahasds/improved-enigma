package com.example.czp.cookbook.base.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.czp.cookbook.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by chenzipeng on 2018/1/17.
 * function: activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ViewGroup ll_status;
    private ViewGroup.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID());
        ButterKnife.bind(this);
        initView();
        initData();
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//4.4-5.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//清除flag，为了5.0全透明
                int opint = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(opint);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }

        }
    }

    protected void initData() {
    }


    public void setStatus() {
        ll_status = (ViewGroup) findViewById(R.id.ll_status);
        params = ll_status.getLayoutParams();
        if (Build.VERSION.SDK_INT >= 19) {
            ll_status.post(new Runnable() {
                @Override
                public void run() {
                    params.height = getStatusHeight() + ll_status.getHeight();
                }
            });
        }

    }


    protected abstract void initView();

    protected abstract int layoutResID();


    public int getStatusHeight() {
        try {
            //通过反射获取类
            Class<?> aClass = Class.forName("com.android.internal.R$dimen");
            //实例化
            Object o = aClass.newInstance();
            Field field = aClass.getField("status_bar_height");
            Object o1 = field.get(o);
            int statusHeight = Integer.parseInt(o1.toString());
            return getResources().getDimensionPixelSize(statusHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void initToolBar(final Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    public void goActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in_right, R.anim.activity_out_left);
    }

    /**
     * 传递数据跳转activity
     *
     * @param intent
     * @param cls
     */
    public void goActivityData(Intent intent, Class<?> cls) {
        intent.setClass(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in_right, R.anim.activity_out_left);
    }

    /**
     * 传递数据跳转activity
     *
     * @param intent
     * @param cls
     */
    public void goActivityData(Intent intent, Class<?> cls, Bundle bundle) {
        intent.setClass(this, cls);
        startActivity(intent, bundle);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_finsh_in, R.anim.activity_finsh_out);
    }

    /**
     * 隐藏软键盘
     *
     * @return true 成功 false 失败
     */
    protected boolean hiddenKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    protected void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(this.getCurrentFocus().getWindowToken(), 0);
    }


}
