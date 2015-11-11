package se.skeppstedt.swimpy.listadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.skeppstedt.swimpy.R;
import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.application.Swimmer;

/**
 * Created by niske on 2015-11-11.
 */
public class PersonalBestListAdapter extends BaseAdapter {

    private Context ctx;
    List<PersonalBest> personalBestList;

    public PersonalBestListAdapter(Context ctx, List<PersonalBest> personalBests) {
        this.ctx = ctx;
        this.personalBestList = personalBests;
    }

    @Override
    public int getCount() {
        return personalBestList.size();
    }

    @Override
    public Object getItem(int position) {
        return personalBestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.personalbest_advanced, parent, false);
        }
        //
        PersonalBest c = personalBestList.get(position);
        if ( position % 2 == 0 ) {
            v.setBackgroundDrawable(null);
        } else {
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector);
            gradient.mutate();
            v.setBackgroundDrawable(gradient);
        }
        TextView event = (TextView) v.findViewById(R.id.personalBestEvent);
        TextView time = (TextView) v.findViewById(R.id.personalBestTime);
        TextView date = (TextView) v.findViewById(R.id.personalBestDate);
        TextView competition = (TextView) v.findViewById(R.id.personalBestCompetition);

        event.setText(c.event.toString());
        time.setText(c.getTimeString());
        date.setText(c.date);
        competition.setText(c.competition);
        return v;
    }
}
