package com.github.atzcx.appverupdater.sample;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.atzcx.appverupdater.AppVerUpdater;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("");

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)  findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_github).color(Color.WHITE).sizeDp(24));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/atzcx/AppVerUpdater")));
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
