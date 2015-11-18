package se.skeppstedt.swimpy;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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
        SwimmerApplication application = (SwimmerApplication) getApplication();
        final Swimmer swimmer = application.getSwimmer(octoId);
        final NumberPicker minutesPicker = (NumberPicker) findViewById(R.id.addPersonalBestMinutes);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(20);
        final NumberPicker secondsPicker = (NumberPicker) findViewById(R.id.addPersonalBestSeconds);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        final NumberPicker millisPicker = (NumberPicker) findViewById(R.id.addPersonalBestMillis);
        millisPicker.setMinValue(0);
        millisPicker.setMaxValue(99);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Event selectedEvent = (Event) spinner.getSelectedItem();
                final PersonalBest personalBest = swimmer.getPersonalBest(selectedEvent);
                if(personalBest != null) {
                    Duration time = personalBest.time;
                    final int minutes = time.toStandardMinutes().getMinutes();
                    final int seconds = time.toStandardSeconds().getSeconds();
                    minutesPicker.setValue(minutes);
                    secondsPicker.setValue(seconds);
                    long millisWithoutMinSec = (time.getMillis() - (minutes * 60000L) - (seconds * 1000L));
                    Log.d(getClass().getSimpleName(), "Millis without minutes and seconds " + millisWithoutMinSec);
                    long millis = millisWithoutMinSec / 10L;
                    millisPicker.setValue((int)millis);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button saveButton = (Button) findViewById(R.id.savePersonalBestButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = (Event) spinner.getSelectedItem();
                Log.d(getClass().getSimpleName(), "Trying to parse m:" + minutesPicker.getValue() + " s:" + secondsPicker.getValue() + " h:" + millisPicker.getValue());
                Long minutesValue = minutesPicker.getValue() * 60000L;
                Long secondsValue = secondsPicker.getValue() * 1000L;
                Long millisValue = millisPicker.getValue() * 10L;
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
        /*
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
        */
    }
}
