package se.skeppstedt.swimpy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import se.skeppstedt.swimpy.application.MedleyTeam;
import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.listadapter.MedleyTeamListAdapter;
import se.skeppstedt.swimpy.listadapter.PersonalBestListAdapter;

public class ShowBestMedleyTeams50Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_best_medley50_teams);
        Bundle bundle = getIntent().getExtras();
        List<String> selectedSwimmers = bundle.getStringArrayList("selectedswimmers");
        SwimmerApplication application = (SwimmerApplication) getApplication();
        List<Swimmer> swimmers = application.getSwimmers(selectedSwimmers);
        List<MedleyTeam> medleyTeams = application.getBestMedleyTeams(swimmers);
        setUpMedleyTeams(medleyTeams);
    }

    private void setUpMedleyTeams(List<MedleyTeam> teams) {
        Log.d(getClass().getSimpleName(), "Setting up medley team list with " + teams.size() + " teams");
        ListView personalBestList = (ListView) findViewById(R.id.medleyTeamList);
        //personalBestList.setAdapter(new ArrayAdapter<PersonalBest>(this,R.layout.personalbest, personalBests));
        personalBestList.setAdapter(new ArrayAdapter<MedleyTeam>(this, R.layout.medleyteam, teams));
        personalBestList.setAdapter(new MedleyTeamListAdapter(this,teams));
    }
}
