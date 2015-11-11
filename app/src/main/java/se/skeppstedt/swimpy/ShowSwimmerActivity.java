package se.skeppstedt.swimpy;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.listadapter.PersonalBestListAdapter;

public class ShowSwimmerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String swimmerid = bundle.getString("swimmerid");
        Swimmer swimmer = ((SwimmerApplication) getApplication()).getSwimmer(swimmerid);
        setContentView(R.layout.activity_show_swimmer);
        setUpHeader(swimmer);
        setUpPersonalBests(swimmer.getPersonalBests());
    }

    private void setUpHeader(Swimmer swimmer) {
        TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(swimmer.name);
        TextView clubText = (TextView) findViewById(R.id.clubText);
        clubText.setText(swimmer.club);
        TextView yearOfBirthText = (TextView) findViewById(R.id.yearText);
        yearOfBirthText.setText(swimmer.yearOfBirth);
    }

    private void setUpPersonalBests(List<PersonalBest> personalBests) {
        ListView personalBestList = (ListView) findViewById(R.id.personalBestList);
        //personalBestList.setAdapter(new ArrayAdapter<PersonalBest>(this,R.layout.personalbest, personalBests));
        personalBestList.setAdapter(new PersonalBestListAdapter(this, personalBests));
    }
}
