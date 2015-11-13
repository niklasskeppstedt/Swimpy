package se.skeppstedt.swimpy;

import android.animation.FloatArrayEvaluator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.listadapter.SwimmerListAdapter;

public class SearchSwimmerActivity extends AppCompatActivity {

    private List<Swimmer> swimmerSearchResult = new ArrayList<>();
    private SwimmerListAdapter searchResultListAdapter;
    private ArrayAdapter<Swimmer> arrayAdapter;
    private String selectedOctoId = null;
    FloatingActionButton addSwimmerButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_swimmer);
        final EditText searchText = (EditText)findViewById(R.id.searchSwimmerText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 3) {
                    String searchString = v.getText().toString();
                    Log.d(getClass().getSimpleName(), "Search for string is now " + searchString);
                    while (true) {
                        String replaced = searchString.replace("  ", " ");
                        if (replaced.length() == searchString.length()) {
                            //Nothing happened, break and continue to search func
                            break;
                        }
                        searchString = replaced;
                    }
                    String[] split = searchString.split(" ");
                    Log.d(getClass().getSimpleName(), "Searchstring contains " + split.length + " parts");
                    if (split.length < 1 || split[0].isEmpty()) {// No searchtext
                        Toast toast = Toast.makeText(getBaseContext(), "Inget namn angivet", Toast.LENGTH_SHORT);
                        toast.show();
                        return true;
                    }
                    if (split.length > 2) {// No searchtext
                        Toast toast = Toast.makeText(getBaseContext(), "För många namn angivna", Toast.LENGTH_SHORT);
                        toast.show();
                        return true;
                    }
                    String firstName = split.length == 2 ? split[0] : "";
                    String lastName = split.length == 2 ? split[1] : split[0];
                    searchForSwimmer(firstName, lastName);
                    InputMethodManager in = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v
                                    .getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        ListView searchResultList = (ListView) findViewById(R.id.searchResultList);
        searchResultList.setAdapter(arrayAdapter = new ArrayAdapter<Swimmer>(this, android.R.layout.simple_list_item_single_choice, swimmerSearchResult));
        searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Swimmer selected = swimmerSearchResult.get(position);
                Log.d(getClass().getSimpleName(), "Click: Checked radio for swimmer " + selected.name);
                selectedOctoId = selected.octoId;
                addSwimmerButton.setVisibility(View.VISIBLE);
            }
        });
        searchResultList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Swimmer selected = swimmerSearchResult.get(position);
                Log.d(getClass().getSimpleName(), "Select: Checked radio for swimmer " + selected.name);
                selectedOctoId = selected.octoId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(getClass().getSimpleName(), "Nothing selected");
            }
        });
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        addSwimmerButton = (FloatingActionButton) findViewById(R.id.addSwimmerButton);
        addSwimmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(), "Add selected swimmer...");
                String[] selected = new String[]{selectedOctoId};
                new AddSwimmerTask().execute(selected);
            }
        });
    }

    private void searchForSwimmer(String firstName, String lastName) {
        Log.d(getClass().getSimpleName(), "Searching for swimmer with firstname " + firstName + " lastname " + (lastName.isEmpty() ? "Not defined" : lastName));
        swimmerSearchResult.clear();
        new SearchSwimmersTask().execute(firstName, lastName);
    }

    class SearchSwimmersTask extends AsyncTask<String, Integer, List<Swimmer>> {
        // Do the long-running work in here
        protected List<Swimmer> doInBackground(String... firstNameLastName) {
            Set<Swimmer> searchResult = new HashSet<>();
            int count = firstNameLastName.length;
            SwimmerApplication application = (SwimmerApplication) getApplication();
            return application.searchSwimmer(firstNameLastName[0], firstNameLastName[1]);
        }

        @Override
        protected void onPostExecute(List<Swimmer> swimmers) {
            Log.d(getClass().getSimpleName(), "onPostExecute with " + swimmers.size() + " swimmers");
            super.onPostExecute(swimmers);
            swimmerSearchResult.addAll(swimmers);
            arrayAdapter.notifyDataSetChanged();
            Log.d(getClass().getSimpleName(), "onPostExecute finished");
        }
    }

    class AddSwimmerTask extends AsyncTask<String, Integer, List<Swimmer>> {
        // Do the long-running work in here
        protected List<Swimmer> doInBackground(String... octoIds) {
            Set<Swimmer> searchResult = new HashSet<>();
            SwimmerApplication application = (SwimmerApplication) getApplication();
            final List<Swimmer> swimmers = application.addSwimmer(octoIds[0]);
            return swimmers;
        }

        @Override
        protected void onPostExecute(List<Swimmer> swimmers) {
            Log.d(getClass().getSimpleName(), "onPostExecute with " + swimmers.size() + " swimmers");
            super.onPostExecute(swimmers);
            swimmerSearchResult.clear();
            arrayAdapter.notifyDataSetChanged();
            Log.d(getClass().getSimpleName(), "Done in searchactivity, moving on to MainActivity");
            Intent intent = new Intent(SearchSwimmerActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
