/*
 * Copyright 2017 Aleksandr Tarakanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.atzcx.appverupdater.sample;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.atzcx.appverupdater.AppVerUpdater;
import com.github.atzcx.appverupdater.Callback;
import com.github.atzcx.appverupdater.enums.UpdateErrors;
import com.github.atzcx.appverupdater.models.Update;
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
                .setUpdateJSONUrl("http://example.net/example.json")
                .setShowNotUpdated(true)
                .setViewNotes(false)
                .setCallback(new Callback() {
                    @Override
                    public void onFailure(UpdateErrors error) {

                        if (error == UpdateErrors.NETWORK_NOT_AVAILABLE){
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        } else if (error == UpdateErrors.NETWORK_DISCONNECTED){
                            Toast.makeText(MainActivity.this, "Internet Disconnected", Toast.LENGTH_LONG).show();
                        } else if(error == UpdateErrors.SOCET_TIMEOUT_EXCEPTION){
                            Toast.makeText(MainActivity.this, "Socet timeout exception, Please try again.", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(MainActivity.this, "Exception: " + error, Toast.LENGTH_LONG).show();

//                       UpdateErrors.NETWORK_NOT_AVAILABLE
//                       UpdateErrors.NETWORK_DISCONNECTED
//                       UpdateErrors.SOCET_TIMEOUT_EXCEPTION
//                       UpdateErrors.UNKNOWN_HOST_EXCEPTION
//                       UpdateErrors.SOCET_EXCEPTION
//                       UpdateErrors.STRING_URL_IS_EMPTY
//                       UpdateErrors.JSON_FILE_IS_MISSING
//                       UpdateErrors.FILE_JSON_NO_DATA

                    }
                })
                .build();

    }


}
