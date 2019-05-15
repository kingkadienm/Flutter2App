package com.wangzs.flutter2app.handler;

import io.flutter.view.FlutterView;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.wangzs.flutter2app.FlutterActivity;
import com.wangzs.flutter2app.Main2Activity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Flutter端主动向Native端发送消息
 * Create by wangzs 2019-02-13
 */
public class FlutterMethodHandler implements MethodChannel.MethodCallHandler {


    /**
     * 名称可自定义，保持唯一即可
     */
    public static final String CHANNEL_NAME = "com.wangzs.flutter2app";
    public static final String TAG = "FlutterMethodHandler";
    private MethodChannel methodChannel;

    private FlutterView flutterView;

    private Activity mActivity;

    private FlutterMethodHandler() {

    }

    private static FlutterMethodHandler instance;

    public static FlutterMethodHandler getInstance() {
        if (instance == null) {
            synchronized (FlutterMethodHandler.class) {
                if (instance == null) {
                    instance = new FlutterMethodHandler();
                }
            }
        }
        return instance;
    }

    private FlutterMethodHandler(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 接收Flutter传来的指令，进一步处理
     *
     * @param methodCall
     * @param result
     */
    @Override
    public void onMethodCall(final MethodCall methodCall, final MethodChannel.Result result) {
        if (methodCall == null) {
            return;
        }
        Log.e(TAG, "method is " + methodCall.method + " arguments is " + (methodCall.arguments == null ? "" : methodCall.arguments.toString()));
        Intent intent = new Intent(mActivity, Main2Activity.class);
        switch (methodCall.method) {
            case "withoutParams":
                //跳转到指定Activity
                mActivity.startActivity(intent);
                //返回给flutter的参数
                result.success("success");
                break;
            case "withParams":
                //解析参数
                String text = methodCall.arguments.toString();
                //带参数跳转到指定Activity
                intent.putExtra("test", text);
                mActivity.startActivity(intent);
                //返回给flutter的参数
                result.success("success");
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    public MethodChannel getMethodChannel() {
        if (methodChannel == null) {
            methodChannel = new MethodChannel(flutterView, CHANNEL_NAME);
        }
        return methodChannel;
    }

    /**
     * 注册处理回调
     */
    public void registerWith(FlutterView flutterView) {
        this.flutterView = flutterView;
        methodChannel = new MethodChannel(flutterView, CHANNEL_NAME);
        FlutterMethodHandler instance = new FlutterMethodHandler((Activity) flutterView.getContext());
        methodChannel.setMethodCallHandler(instance);
    }


}
