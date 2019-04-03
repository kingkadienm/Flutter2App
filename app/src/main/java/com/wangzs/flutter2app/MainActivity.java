package com.wangzs.flutter2app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @description:
 * @autour: wangzs
 * @date:  2019-02-15 17:20
 * @version:
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button intentFlutter = findViewById(R.id.intent_flutter);
        Button intentFlutterWithParams = findViewById(R.id.intent_flutter_with_params);


        intentFlutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FlutterActivity.class));
            }
        });
        intentFlutterWithParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FlutterActivity.class));
            }
        });


    }
}
