package com.tnkfactory.tmat.offerer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tnkfactory.ad.TnkSession;
import com.tnkfactory.ad.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.enableLogging(true);

        // 유저 식별 값 설정
        TnkSession.setUserName(MainActivity.this, "Test_User");

        // 실행형 광고
        TnkSession.applicationStarted(this);

        Button btn_action_completed_01 = findViewById(R.id.btn_action_completed_01);
        btn_action_completed_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TnkSession.actionCompleted(MainActivity.this, "test_01");
            }
        });

        Button btn_action_completed_02 = findViewById(R.id.btn_action_completed_02);
        btn_action_completed_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TnkSession.actionCompleted(MainActivity.this, "test_02");
            }
        });

        Button btn_buy_completed_01 = findViewById(R.id.btn_buy_completed_01);
        btn_buy_completed_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TnkSession.buyCompleted(MainActivity.this, "item_01");
            }
        });

        Button btn_buy_completed_02 = findViewById(R.id.btn_buy_completed_02);
        btn_buy_completed_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TnkSession.buyCompleted(MainActivity.this, "item_02");
            }
        });
    }
}
