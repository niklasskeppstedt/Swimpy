package se.skeppstedt.swimpy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.listadapter.SwimmerListAdapterListener;
import se.skeppstedt.swimpy.listadapter.SwimmerListAdapter;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> selectedSwimmers = new ArrayList<>();
    private ArrayList<Swimmer> allSwimmers = new ArrayList<>();
    private SwimmerListAdapter swimmerListAdapter;
    private ListView swimmersList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        swimmersList = (ListView) findViewById(R.id.swimmersList);
        swimmersList.setAdapter(swimmerListAdapter = new SwimmerListAdapter(this, allSwimmers = new ArrayList<Swimmer>(getSwimmerApplication().swimmers)));
        swimmerListAdapter.setOnCheckBoxCheckedListener(new SwimmerListAdapterListener() {
            @Override
            public void onChecked(String octoId, boolean checked) {
                if (checked) {
                    selectedSwimmers.add(octoId);
                } else {
                    selectedSwimmers.remove(octoId);
                }
            }

            @Override
            public void onLongClick(String octoId) {
                Intent intent = new Intent(MainActivity.this, ShowSwimmerActivity.class);
                intent.putExtra("swimmerid", octoId);
                startActivity(intent);
            }
        });
        Button medley50button = (Button) findViewById(R.id.medley50button);
        medley50button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSwimmers.size() < 4) {
                    Toast toast = Toast.makeText(getBaseContext(), "Det behövs minst 4 simmare, stupid!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ShowBestMedleyTeams50Activity.class);
                intent.putStringArrayListExtra("selectedswimmers", selectedSwimmers);
                startActivity(intent);
            }
        });

        FloatingActionButton newSwimmerButton = (FloatingActionButton) findViewById(R.id.mainSwimmerButton);
        newSwimmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(), "New swimmer button clicked");
                Intent intent = new Intent(MainActivity.this, SearchSwimmerActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton deleteSwimmerButton = (FloatingActionButton) findViewById(R.id.mainSwimmerDeleteButton);
        deleteSwimmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteSwimmerTask().execute(selectedSwimmers.toArray(new String[selectedSwimmers.size()]));
            }
        });
        CheckBox selectAll = (CheckBox) findViewById(R.id.selectAllCheckbox);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAllCheckboxes(isChecked);
            }
        });
    }

    private void setAllCheckboxes(boolean isChecked) {
        for(int i=0; i < swimmersList.getChildCount(); i++){
            View child = swimmersList.getChildAt(i);
            CheckBox cb = (CheckBox)child.findViewById(R.id.swimmerCheckBox);
            cb.setChecked(isChecked);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        allSwimmers.clear();
        allSwimmers.addAll(((SwimmerApplication) getApplication()).getSwimmers());
        swimmerListAdapter.notifyDataSetChanged();
        setAllCheckboxes(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    SwimmerApplication getSwimmerApplication() {
        return (SwimmerApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class DeleteSwimmerTask extends AsyncTask<String, Integer, List<Swimmer>> {
        // Do the long-running work in here
        protected List<Swimmer> doInBackground(String... octoIds) {
            Set<Swimmer> searchResult = new HashSet<>();
            SwimmerApplication application = (SwimmerApplication) getApplication();
            return application.deleteSwimmers(octoIds);
        }

        @Override
        protected void onPostExecute(List<Swimmer> swimmers) {
            super.onPostExecute(swimmers);
            Log.d(getClass().getSimpleName(), "onPostExecute with " + swimmers.size() + " swimmers");
            setAllCheckboxes(false);
            allSwimmers.clear();
            allSwimmers.addAll(swimmers);
            swimmerListAdapter.notifyDataSetChanged();
        }
    }

}
