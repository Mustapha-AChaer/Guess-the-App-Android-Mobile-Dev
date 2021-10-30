package com.lau.appguessproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //StrictMode.enableDefaults();
    }

    public void changeActivity(View view){


        if(view.getTag().toString().equals("easy")){
            Intent intent = new Intent(this, EasyModed.class);
            startActivity(intent);

        }else if(view.getTag().toString().equals("medium")){

            Intent intent = new Intent(this, MediumMode.class);
            startActivity(intent);

        }else if(view.getTag().toString().equals("hard")){

            Intent intent = new Intent(this, HardMode.class);
            startActivity(intent);

        }
    }


}