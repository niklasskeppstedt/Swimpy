package se.skeppstedt.swimpy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import se.skeppstedt.swimpy.application.SwimmerApplication;

/**
 * Created by niske
 */
public class SplashScreen extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
 
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    Log.d("SplashScreen", "Creating SwimmerApplication instance");
                    SwimmerApplication.getInstance();
                    Log.d("SplashScreen", "Created SwimmerApplication instance");
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
 
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
 
}