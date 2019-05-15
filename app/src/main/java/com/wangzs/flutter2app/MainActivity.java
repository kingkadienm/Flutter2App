package com.wangzs.flutter2app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.wangzs.flutter2app.model.UserBean;

/**
 * @description:
 * @autour: wangzs
 * @date: 2019-02-15 17:20
 * @version:
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button intentFlutter = findViewById(R.id.intent_flutter);
        Button intentFlutterWithParams = findViewById(R.id.intent_flutter_with_params);

        UserBean userBean = new UserBean("张三","this is data","男",12);


        Gson gson = new Gson();
        String toJson = gson.toJson(userBean);
        intentFlutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlutterActivity.class);
                intent.putExtra("route", "onePage");
                startActivity(intent);
            }
        });
        intentFlutterWithParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlutterActivity.class);
                intent.putExtra("params", toJson);
                intent.putExtra("route", "twoPage");
                startActivity(intent);
            }
        });


    }
}
