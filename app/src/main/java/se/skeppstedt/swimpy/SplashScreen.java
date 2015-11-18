package se.skeppstedt.swimpy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        new DownloadSwimmersTask().execute(((SwimmerApplication) getApplication()).swimmerIds.toArray(new String[0]));
        finish();
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

    class DownloadSwimmersTask extends AsyncTask<String, Integer, List<Swimmer>> {
        // Do the long-running work in here
        protected List<Swimmer> doInBackground(String... swimmerIds) {
            if(!((SwimmerApplication)getApplication()).getSwimmers().isEmpty()) {
                return ((SwimmerApplication)getApplication()).getSwimmers();
            }
            List<Swimmer> newSwimmers = new ArrayList<>();
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
                    return Collections.emptyList();
                }
                Swimmer swimmer = parser.parseSwimmer(document, swimmerIds[i]);
                if(!newSwimmers.contains(swimmer)) {
                    newSwimmers.add(swimmer);
                }
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return newSwimmers;
        }

        @Override
        protected void onPostExecute(List<Swimmer> loadedSwimmers) {
            final SwimmerApplication application = (SwimmerApplication) getApplication();
            for (Swimmer swimmer: loadedSwimmers) {
                if(application.getSwimmers().contains(swimmer)) {
                    Log.e("SplashScreen", "Loaded swimmer already in memory");
                } else {
                    Log.e("SplashScreen", "Adding loaded swimmer to memory");
                    application.getSwimmers().add(swimmer);
                }
            }
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }

}