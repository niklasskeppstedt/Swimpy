package se.skeppstedt.swimpy.application;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.MainActivity;
import se.skeppstedt.swimpy.SwimpyConstants;
import se.skeppstedt.swimpy.application.enumerations.Event;
import se.skeppstedt.swimpy.application.parser.OctoParser;
import se.skeppstedt.swimpy.util.ActivityLifecycleCallbacksAdapter;
import se.skeppstedt.swimpy.util.MedleyTeamComparator;
import se.skeppstedt.swimpy.util.PersonalBestComparator;

/**
 * Created by niske on 2015-11-09.
 */
public class SwimmerApplication extends Application{

    public Set<Swimmer> swimmers = new HashSet<>();
    public String[] swimmerIds = new String[]{};

    public Swimmer getSwimmer(String octoid) {
        for (Swimmer swimmer: swimmers) {
            if(swimmer.octoId.equals(octoid)) {
                return swimmer;
            }
        }
        return null;
    }

    public Set<Swimmer> getSwimmers() {
        return new HashSet<>(swimmers);
    }

    @Override
    public void onCreate() {
        //initialDummyWrite()
        super.onCreate();
        Log.i("Swimpy","Swimpy starting up!");
        try {
            Log.i("Swimpy","Swimpy trying to read file");
            File yourFile = new File(getFilesDir(),"swimmers.dat");
            if(!yourFile.exists()) {
                Log.i("Swimpy","Swimpy file doesnt exist creating it");
                yourFile.createNewFile();
                Log.i("Swimpy", "Swimpy file craeted");
            }
            Log.i("Swimpy", "Now Swimpy file exists, read it");
            FileReader fileReader = new FileReader(yourFile);
            BufferedReader br = new BufferedReader(fileReader);
            String s;
            while((s = br.readLine()) != null) {
                Log.i("Swimpy", "Swimpy read line, it is " + s);
                final String[] split = s.split(",");
                swimmerIds = split;
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
                if (activity instanceof MainActivity) {
                    FileWriter fileWriter = null;
                    try {
                        if (!swimmers.isEmpty()) {
                            fileWriter = new FileWriter(new File(getFilesDir(), "swimmers.dat"));
                            //Write a new swimmer object list to the CSV file
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
                            if(fileWriter != null) {
                                fileWriter.flush();
                                fileWriter.close();
                            }
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

    public List<Swimmer> addSwimmer(String octoid) {
        Document document;
        final URL url;
        try {
            url = new URL(SwimpyConstants.swimmerResultUrl + octoid);
            document = Jsoup.parse(url, 6000);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Could not parse swimmer url" + SwimpyConstants.swimmerResultUrl + octoid, e);
            return null;
        }
        OctoParser octoParser = new OctoParser();
        Swimmer swimmer = octoParser.parseSwimmer(document, octoid);
        swimmers.add(swimmer);
        return new ArrayList<>(swimmers); //Well maybe not the good thing to do...
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

    public List<Swimmer> searchSwimmer(String firstName, String lastName) {
        String adjustedSearchUrl;
        Log.d(getClass().getSimpleName(), "Searching for swimmer with firstname: " + firstName + " lastname: " + lastName);
        try {
            adjustedSearchUrl = SwimpyConstants.swimmerSearchUrl.replace("afirstname", URLEncoder.encode(firstName, "UTF-8"));
            adjustedSearchUrl = adjustedSearchUrl.replace("alastname", URLEncoder.encode(lastName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println(e);
            return Collections.emptyList();
        }
        Log.d(getClass().getSimpleName(), "URL is: " + adjustedSearchUrl);
        OctoParser parser = new OctoParser(adjustedSearchUrl);
        List<Swimmer> swimmers = new ArrayList<>();
        swimmers.addAll(parser.parseSearchResult());
        Log.d(getClass().getSimpleName(), "Returning list with " + swimmers.size() + " swimmers");
        return swimmers;

    }

    public List<Swimmer> deleteSwimmers(String[] octoIds) {
        List<Swimmer> swimmersToRemove = new ArrayList<>();
        for (String octoId : octoIds) {
            for (Swimmer swimmer : swimmers) {
                if(swimmer.octoId.equals(octoId)){
                    swimmersToRemove.add(swimmer);
                }
            }
        }
        swimmers.removeAll(swimmersToRemove);
        return new ArrayList<>(swimmers);
    }
}
