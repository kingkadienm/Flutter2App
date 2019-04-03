package com.wangzs.flutter2app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * @description:
 * @autour: wangzs
 * @date:  2019-02-15 17:43
 * @version:
 *
*/

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String test = getIntent().getStringExtra("test");
        TextView textView = findViewById(R.id.my_text);
        if (!TextUtils.isEmpty(test)) {
            textView.setText(test);
        }
    }
}
