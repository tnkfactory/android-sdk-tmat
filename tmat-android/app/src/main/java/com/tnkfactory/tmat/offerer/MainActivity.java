package com.tnkfactory.tmat.offerer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tnkfactory.ad.TnkSession;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 연동 확인용 로그(Logcat 태그 TNK_AD). 출시 빌드에서는 끄세요.
        TnkSession.enableLogging(true);

        // 유저 식별 값 설정
        TnkSession.setUserName(MainActivity.this, "Test_User");

        // 앱 실행 알림 — 첫 화면 onCreate() 에서 다른 API 보다 먼저 호출
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
