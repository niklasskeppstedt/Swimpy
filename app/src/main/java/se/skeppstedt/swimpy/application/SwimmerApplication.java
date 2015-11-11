package se.skeppstedt.swimpy.application;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import se.skeppstedt.swimpy.MainActivity;
import se.skeppstedt.swimpy.util.ActivityLifecycleCallbacksAdapter;

/**
 * Created by niske on 2015-11-09.
 */
public class SwimmerApplication extends Application{

    public Set<Swimmer> swimmers = new HashSet<>();
    public String[] swimmerIds;

    public Swimmer getSwimmer(String octoid) {
        for (Swimmer swimmer: swimmers) {
            if(swimmer.octoId.equals(octoid)) {
                return swimmer;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        //initialDummyWrite();
        super.onCreate();
        try {
            FileReader fileReader = new FileReader(new File(getFilesDir(),"swimmers.dat"));
            BufferedReader br = new BufferedReader(fileReader);
            String s;
            while((s = br.readLine()) != null) {
                final String[] split = s.split(",");
                swimmerIds = split;
                if(swimmerIds.length == 2) {
                    String[] fakeOnes = new String[4];
                    System.arraycopy(swimmerIds,0,fakeOnes,0,2);
                    fakeOnes[2] = "295557";
                    fakeOnes[3] = "300057";
                    swimmerIds = fakeOnes;
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityStopped(Activity activity) {
                if(activity instanceof MainActivity) {
                    FileWriter fileWriter = null;
                    try {
                        if(!swimmers.isEmpty()) {
                            fileWriter = new FileWriter(new File(getFilesDir(), "swimmers.dat"));
                            //Write a new student object list to the CSV file
                            boolean comma = false;
                            for (Swimmer student : swimmers) {
                                if (comma) {
                                    fileWriter.append(",");
                                }
                                fileWriter.append(String.valueOf(student.octoId));
                                comma = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
