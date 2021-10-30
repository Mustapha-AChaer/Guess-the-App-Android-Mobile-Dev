package com.lau.appguessproject;

import static java.util.Arrays.asList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HardMode extends AppCompatActivity {

    ArrayList<String> imgLinks;
    ArrayList<Bitmap> images;
    ArrayList<String> appNames;


    ImageView view;
    Button btn1,btn2,btn3, btn4, restartBtn;
    TextView view2, scoretxt, timerTxt;
    MediaPlayer player;




    Boolean done = false;
    int score = 0;
    int answer = 1000;

    public class getHTMLContent extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            imgLinks = new ArrayList<>();
            appNames =  new ArrayList<>();
            images = new ArrayList<>();
            view = (ImageView) findViewById(R.id.appView);
            InputStream in = null;
            URL url = null;
            HttpURLConnection urlConnection = null;

            String result= "";
            try {
                System.out.println(strings[0].toString());
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                System.out.println("you stuck here?");
                in = urlConnection.getInputStream();


                Log.i("Test", "you are here");
                System.out.println("you stuck here?");

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                Log.i("Test", "you are here");

                StringBuilder str = new StringBuilder();
                String line=null;
                while((line = reader.readLine()) != null){
                    str.append(line + '\n');
                }
                in.close();
                String html = str.toString();
                //Log.i("Test", html);

                return html;
            }catch (Exception e){
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String html) {
            super.onPostExecute(html);
            Pattern p = Pattern.compile("data-image-loader=\"(.*?)\"");
            Matcher m = p.matcher(html);

            Pattern p2 = Pattern.compile("alt=\"(.*?) Image\"");
            Matcher m2 = p2.matcher(html);


            while(m.find()){
                imgLinks.add(m.group(1));
            }
            while(m2.find()){
                appNames.add(m2.group(1));
            }


            System.out.println("Image size " + imgLinks.size());
            System.out.println(imgLinks.toString());
            System.out.println( "App size " + appNames.size());
            System.out.println(appNames.toString());



            try {
                Bitmap image = null;
                InputStream stream = null;
                Log.i("Test", "you are here");

                for (int i = 0; i < imgLinks.size(); i++) {
                    stream = new URL(imgLinks.get(i)).openStream();
                    image = BitmapFactory.decodeStream(stream);
                    images.add(image);
                    Log.i("Test", String.valueOf(i));
                }
                stream.close();

                Log.i("Test", "you are here");
                //game(images, appNames);

            }catch (Exception e){
                System.out.println("you have failed again");
                e.printStackTrace();
            }

            /*view.setImageBitmap(images.get(imgLinks.size()-91));
            System.out.println(appNames.get(imgLinks.size()-91));*/
            try{
                System.out.println(appNames.size());
                answer = game(images, appNames);
            }catch(Exception e){
                e.printStackTrace();
            }


        }
    }

    public int game(ArrayList<Bitmap> images, ArrayList<String> appNames){

        Random rand = new Random();
        int realAnswer = rand.nextInt(appNames.size()-3);
        view.setImageBitmap(images.get(realAnswer));
        ArrayList<Integer> alreadyChoosen = new ArrayList<>();
        alreadyChoosen.add(realAnswer);
        ArrayList<Button> btns = new ArrayList<>(asList(btn1,btn2,btn3,btn4));
        int realButton = rand.nextInt(4);
        for (Button b :
                btns) {
            if (b.getTag().equals(String.valueOf(realButton))) {
                b.setText(appNames.get(realAnswer));
            }else{
                b.setText(appNames.get(rand.nextInt(appNames.size()-3)));
            }

        }
        return realButton;

    }
    public void changeApps(View view){

        if(view.getTag().equals(String.valueOf(answer))){
            score ++;
            System.out.println("Score " + score);
            scoretxt.setText("Score: " + String.valueOf(score));
        }
        answer = game(images, appNames);
        System.out.println("Answer "+String.valueOf(answer));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_mode);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        player = MediaPlayer.create(this, R.raw.beat);
        player.start();
        scoretxt = (TextView) findViewById(R.id.scoreText);
        timerTxt = (TextView) findViewById(R.id.timeText);

        restartBtn = (Button) findViewById(R.id.restartButton);
        restartBtn.setEnabled(false);
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(26000, 1000) {

                    @Override
                    public void onTick(long l) {

                        view2.setText("Wait " + String.valueOf(l/1000) +" Seconds for the game to start");

                    }

                    @Override
                    public void onFinish() {
                        view2.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                        btn3.setVisibility(View.VISIBLE);
                        btn4.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        timerTxt.setVisibility(View.VISIBLE);
                        scoretxt.setVisibility(View.VISIBLE);

                        new CountDownTimer(30000, 1000){

                            @Override
                            public void onTick(long l) {
                                timerTxt.setText("Time Left: " + String.valueOf(l/1000) + "s");
                            }

                            @Override
                            public void onFinish() {
                                btn1.setEnabled(false);
                                btn2.setEnabled(false);
                                btn3.setEnabled(false);
                                btn4.setEnabled(false);
                                timerTxt.setText("You time has finished");

                                restartBtn.setVisibility(View.VISIBLE);
                                restartBtn.setEnabled(true);
                            }
                        }.start();

                    }
                }.start();
            }
        });
        t2.run();

        view = (ImageView) findViewById(R.id.appView);
        view2 = (TextView) findViewById(R.id.textView3);
        btn1 = (Button) findViewById(R.id.button7);
        btn2 = (Button) findViewById(R.id.button8);
        btn3 = (Button) findViewById(R.id.button9);
        btn4 = (Button) findViewById(R.id.button10);

        Thread t1 = new Thread( new thread1());
        t1.run();
    }


    public class thread1 extends Thread{
        @Override
        public void run() {
            getHTMLContent get = new getHTMLContent();
            get.execute("https://www.pcmag.com/picks/best-android-apps");
        }
    }

    public void playAgain(View view){
        score =0;
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn4.setEnabled(true);
        restartBtn.setVisibility(View.INVISIBLE);
        restartBtn.setEnabled(false);

        new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long l) {
                timerTxt.setText("Time Left: " + String.valueOf(l/1000) + "s");
            }

            @Override
            public void onFinish() {
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);
                timerTxt.setText("You time has finished");
                restartBtn.setVisibility(View.VISIBLE);
                restartBtn.setEnabled(true);
            }
        }.start();
        /*Intent intent = getIntent();
        finish();
        startActivity(intent);*/
    }


}