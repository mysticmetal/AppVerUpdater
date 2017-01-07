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
                .setDialogTitle("Availabe a new update!")
                .setDialogContent("Update %s mobile app to version %s \nFeatures: \n%s")
                .setDialogPositiveText("Update")
                .setDialogNegativeText("Later")
                .setProgressDialogMessage("Download updates...")
                .build();

    }
}
