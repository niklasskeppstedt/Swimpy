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
    public void onTerminate() {
        super.onTerminate();
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
                System.out.println("CSV file was created successfully !!!");
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    void initialDummyWrite() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(getFilesDir(),"swimmers.dat"));
            //Write a new student object list to the CSV file
            fileWriter.append("297358,301255");
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }

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
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                System.out.println("Activity created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                System.out.println("Activity created");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                System.out.println("Activity created");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                System.out.println("Activity created");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(activity instanceof MainActivity) {
                    boolean destroyed = activity.isDestroyed();
                    boolean finishing = activity.isFinishing();
                    onTerminate();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                System.out.println("onActivitySaveInst");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                System.out.println("Activity destroyed");
            }
        });
    }
}
