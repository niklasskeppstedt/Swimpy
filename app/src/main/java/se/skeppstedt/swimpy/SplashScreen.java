package se.skeppstedt.swimpy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.application.parser.OctoParser;

/**
 * Created by niske
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        /**Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();**/
        //Read saved stuff from disk here
        new DownloadSwimmersTask().execute(((SwimmerApplication)getApplication()).swimmerIds);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    class DownloadSwimmersTask extends AsyncTask<String, Integer, Set<Swimmer>> {
        // Do the long-running work in here
        protected Set<Swimmer> doInBackground(String... swimmerIds) {
            Set<Swimmer> newSwimmers = new HashSet<>();
            int count = swimmerIds.length;
            OctoParser parser = new OctoParser();
            for (int i = 0; i < swimmerIds.length; i++) {
                Document document;
                final URL url;
                try {
                    url = new URL(SwimpyConstants.swimmerResultUrl + swimmerIds[i]);
                    document = Jsoup.parse(url, 6000);
                } catch (IOException e) {
                    Log.e("SplashScreen", "Could not parse swimmer url" + SwimpyConstants.swimmerResultUrl + swimmerIds[i], e);
                    return Collections.emptySet();
                }
                Swimmer swimmer = parser.parseSwimmer(document, swimmerIds[i]);
                newSwimmers.add(swimmer);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return newSwimmers;
        }

        @Override
        protected void onPostExecute(Set<Swimmer> loadedSwimmers) {
            final SwimmerApplication application = (SwimmerApplication) getApplication();
            application.swimmers.addAll(loadedSwimmers);
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }

}