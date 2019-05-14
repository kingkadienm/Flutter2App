package com.wangzs.flutter2app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wangzs.flutter2app.handler.FlutterMethodHandler;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

/**
 * @description:
 * @autour: wangzs
 * @date: 2019-02-15 17:43
 * @version:
 */
public class FlutterActivity extends AppCompatActivity {
    private FlutterView flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flutter_acticity);
        String params = getIntent().getStringExtra("params");
        String route = getIntent().getStringExtra("route");
        FrameLayout rootView = findViewById(R.id.rl_flutter);
        flutterView = Flutter.createView(this, getLifecycle(),route);
        FlutterMethodHandler.getInstance().registerWith(flutterView);
        rootView.addView(flutterView);

        flutterView.post(new Runnable() {
            @Override
            public void run() {
                FlutterMethodHandler.getInstance().getMethodChannel().invokeMethod(route, params);
            }
        });

    }

    /**
     * 屏蔽跳转flutter页面 按下返回键直接推出flutter页面，而不是返回上一级
     */
    @Override
    public void onBackPressed() {
        if (this.flutterView != null) {
            this.flutterView.popRoute();
        } else {
            super.onBackPressed();
        }
    }
}
