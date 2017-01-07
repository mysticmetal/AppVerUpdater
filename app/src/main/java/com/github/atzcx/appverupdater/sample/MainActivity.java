package com.github.atzcx.appverupdater.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.atzcx.appverupdater.AppVerUpdater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        update();
    }


    private void update(){

        new AppVerUpdater(MainActivity.this)
                .setJSONUrl("http://mypkgt.azurewebsites.net/updateinfo.json")
                .setDialogTitle("Доступно новое обновление!")
                .setDialogContent("Обновите мобильное приложение %s до версии %s \nИзменения: \n%s")
                .setDialogPositiveText("Обновить")
                .setDialogNegativeText("Позже")
                .setProgressDialogMessage("Загрузка обновлений...")
                .build();

    }
}
