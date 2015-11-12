package se.skeppstedt.swimpy;

import android.content.Intent;
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
import android.widget.ListView;

import java.util.ArrayList;

import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.listadapter.SwimmerListAdapterListener;
import se.skeppstedt.swimpy.listadapter.SwimmerListAdapter;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> selectedSwimmers = new ArrayList<>();
    private SwimmerListAdapter swimmerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        ListView swimmersList = (ListView) findViewById(R.id.swimmersList);
        swimmersList.setAdapter(swimmerListAdapter = new SwimmerListAdapter(this, new ArrayList<Swimmer>(getSwimmerApplication().swimmers)));
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
        Button medley50button = (Button) findViewById(R.id.medley50Button);
        medley50button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                /**Intent intent = new Intent(MainActivity.this, ShowBestMedleyTeams50Activity.class);
                intent.putStringArrayListExtra("selectedswimmers", selectedSwimmers);
                startActivity(intent);**/
            }
        });
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
}
