package se.skeppstedt.swimpy;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.SwimmerApplication;
import se.skeppstedt.swimpy.application.enumerations.Event;

public class AddPersonalBestActivity extends AppCompatActivity {

    String octoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        octoId = bundle.getString("swimmerid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_best);
        final Spinner spinner = (Spinner) findViewById(R.id.eventSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this,
                android.R.layout.simple_list_item_1, Arrays.asList(Event.values()));
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        final EditText minutesText = (EditText) findViewById(R.id.addPersonalBestMinutes);
        minutesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(getClass().getSimpleName(), "Changed text to " + s.toString());
                if (!s.toString().isEmpty() && Integer.parseInt(s.toString()) > 12) {//Unreasonably long one
                    Toast.makeText(AddPersonalBestActivity.this, "Really " + s.toString() + " minutes?", Toast.LENGTH_SHORT).show();
                }
                validate();

            }
        });
        final EditText secondsText = (EditText) findViewById(R.id.addPersonalBestSeconds);
        secondsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(getClass().getSimpleName(), "Changed text to " + s.toString());
                if (!s.toString().isEmpty() && Integer.parseInt(s.toString()) > 59) {//Unreasonably long one
                    Toast.makeText(AddPersonalBestActivity.this, "Really " + s.toString() + " seconds?", Toast.LENGTH_SHORT).show();
                }
                validate();
            }
        });
        final EditText millisText = (EditText) findViewById(R.id.addPersonalBestMillis);
        millisText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(getClass().getSimpleName(), "Changed text to " + s.toString());
                if (!s.toString().isEmpty() && Integer.parseInt(s.toString()) > 99) {//Unreasonably long one
                    Toast.makeText(AddPersonalBestActivity.this, "Really " + s.toString() + " hundreds?", Toast.LENGTH_SHORT).show();
                }
                validate();
            }
        });
        Button saveButton = (Button) findViewById(R.id.savePersonalBestButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = (Event) spinner.getSelectedItem();
                SwimmerApplication application = (SwimmerApplication) getApplication();
                Swimmer swimmer = application.getSwimmer(octoId);
                Log.d(getClass().getSimpleName(), "Trying to parse m:" + minutesText.toString().trim() + " s:" + secondsText.toString().trim() + " h:" + millisText.toString().trim());
                Long minutesValue = Long.valueOf(minutesText.getEditableText().toString().trim()) * 60000;
                Long secondsValue = Long.valueOf(secondsText.getEditableText().toString().trim()) * 1000;
                Long millisValue = Long.valueOf(millisText.getEditableText().toString().trim()) * 10;
                Duration time = new Duration(Long.valueOf(minutesValue + secondsValue + millisValue));
                android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date());
                final PersonalBest today = new PersonalBest(event, time, "Today", android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString(), swimmer, false);
                boolean added = swimmer.addFreshPersonalBest(today);
                if(added) {
                    Toast.makeText(AddPersonalBestActivity.this, "Lade till nytt personbästa för " + swimmer.name, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddPersonalBestActivity.this, "Det gamla resultatet var bättre, kontrollera uppgifter " + swimmer.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validate() {
        final EditText minutesText = (EditText) findViewById(R.id.addPersonalBestMinutes);
        final EditText secondsText = (EditText) findViewById(R.id.addPersonalBestSeconds);
        final EditText millisText = (EditText) findViewById(R.id.addPersonalBestMillis);
        Button saveButton = (Button) findViewById(R.id.savePersonalBestButton);
        if ((!minutesText.getText().toString().isEmpty() && Integer.parseInt(minutesText.getText().toString()) <= 12) &&
                (!secondsText.getText().toString().isEmpty() && Integer.parseInt(secondsText.getText().toString()) < 60) &&
                (!millisText.getText().toString().isEmpty() && Integer.parseInt(millisText.getText().toString()) < 100))
        {
            Log.d(getClass().getSimpleName(), "Enabling button");
            saveButton.setEnabled(true);
            return;
        }
        Log.d(getClass().getSimpleName(), "Disabling button");
        saveButton.setEnabled(false);

    }
}
