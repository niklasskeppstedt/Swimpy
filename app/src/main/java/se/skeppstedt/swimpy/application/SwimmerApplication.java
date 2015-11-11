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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.MainActivity;
import se.skeppstedt.swimpy.application.enumerations.Event;
import se.skeppstedt.swimpy.util.ActivityLifecycleCallbacksAdapter;
import se.skeppstedt.swimpy.util.MedleyTeamComparator;
import se.skeppstedt.swimpy.util.PersonalBestComparator;

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

    public List<Swimmer> getSwimmers(List<String> swimmerIds) {
        List<Swimmer> retVal = new ArrayList<>();
        for (String swimmerId : swimmerIds) {
            for (Swimmer swimmer : swimmers) {
                if(swimmer.octoId.equals(swimmerId)) {
                    retVal.add(swimmer);
                    break;
                }

            }
        }
        return retVal;
    }

    public List<MedleyTeam> getBestMedleyTeams(List<Swimmer> swimmers) {
        List<PersonalBest> backstroke = getBestTimesForEvent(Event.BACKSTROKE_50, swimmers);
        List<PersonalBest> butterfly = getBestTimesForEvent(Event.BUTTERFLY_50, swimmers);
        List<PersonalBest> breaststroke = getBestTimesForEvent(Event.BREASTSTROKE_50, swimmers);
        List<PersonalBest> freestyle = getBestTimesForEvent(Event.FREESTYLE_50, swimmers);
        List<MedleyTeam> allTeams = new ArrayList<>();
        for (PersonalBest backstrokeBest : backstroke) {
            for (PersonalBest butterflyBest : butterfly) {
                for (PersonalBest breaststrokeBest : breaststroke) {
                    for (PersonalBest freestyleBest : freestyle) {
                        MedleyTeam medleyTeam = new MedleyTeam(backstrokeBest, butterflyBest, breaststrokeBest, freestyleBest);
                        if(medleyTeam.isValid()) {
                            allTeams.add(medleyTeam);
                        }
                    }
                }
            }
        }
        Collections.sort(allTeams, new MedleyTeamComparator());
        return allTeams;
    }

    public List<PersonalBest> getBestTimesForEvent(Event event, List<Swimmer> availableSwimmers) {
        List<PersonalBest> bestTimes = new ArrayList<>();
        for (Swimmer swimmer : availableSwimmers) {
            PersonalBest personalBest = swimmer.getPersonalBest(event);
            if(personalBest != null) {
                bestTimes.add(personalBest);
            }
        }
        Collections.sort(bestTimes, new PersonalBestComparator());
        return bestTimes;
    }
}
