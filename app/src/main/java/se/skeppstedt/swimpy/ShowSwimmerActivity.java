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

public class ShowSwimmerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String swimmerid = bundle.getString("swimmerid");
        System.out.println("Got me a swimmerid: " + swimmerid);
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
        /*
        SimpleAdapter (Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
        Parameters
        context	The context where the View associated with this SimpleAdapter is running
        data	A List of Maps. Each entry in the List corresponds to one row in the list. The Maps contain the data for each row, and should include all the entries specified in "from"
        resource	Resource identifier of a view layout that defines the views for this list item. The layout file should include at least those named views defined in "to"
        from	A list of column names that will be added to the Map associated with each item.
        to	The views that should display column in the "from" parameter. These should all be TextViews. The first N views in this list are given the values of the first N columns in the from parameter.
         */
        personalBestList.setAdapter(new ArrayAdapter<PersonalBest>(this,R.layout.personalbest, personalBests));
    }
}
