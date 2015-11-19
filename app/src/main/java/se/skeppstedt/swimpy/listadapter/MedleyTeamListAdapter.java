package se.skeppstedt.swimpy.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import se.skeppstedt.swimpy.R;
import se.skeppstedt.swimpy.application.MedleyTeam;
import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.util.DurationUtil;

/**
 * Created by niske on 2015-11-11.
 */
public class MedleyTeamListAdapter extends BaseAdapter {

    private Context ctx;
    List<MedleyTeam> medleyTeams;

    public MedleyTeamListAdapter(Context ctx, List<MedleyTeam> medleyTeams) {
        this.ctx = ctx;
        this.medleyTeams = medleyTeams;
    }

    @Override
    public int getCount() {
        return medleyTeams.size();
    }

    @Override
    public Object getItem(int position) {
        return medleyTeams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        //if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.medleyteam, parent, false);
        //}
        //
        MedleyTeam team = medleyTeams.get(position);
        TextView time = (TextView) v.findViewById(R.id.teamTime);
        time.setText(DurationUtil.getTimeString(team.getTime()));

        PersonalBest relay = team.relays.get(0);
        boolean backStrokeIsFresh = !relay.official;
        TextView discipline = (TextView) v.findViewById(R.id.relayBackstroke);
        discipline.setText(relay.event.getDiscipline().toString());
        TextView swimmerName = (TextView) v.findViewById(R.id.relayBackstrokeSwimmerName);
        swimmerName.setText(relay.swimmer.name);
        TextView relayTime = (TextView) v.findViewById(R.id.relayBackstrokeTime);
        relayTime.setText(DurationUtil.getTimeString(relay.time));
        if(backStrokeIsFresh) {
            final View medleyBackstrokeLine = v.findViewById(R.id.medleyBackstrokeLine);
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector_fresh);
            gradient.mutate();
            medleyBackstrokeLine.setBackgroundDrawable(gradient);
            //relayTime.setBackgroundColor(Color.CYAN);
        }

        relay = team.relays.get(1);
        boolean butterflyIsfresh = !relay.official;
        discipline = (TextView) v.findViewById(R.id.relayButterfly);
        discipline.setText(relay.event.getDiscipline().toString());
        swimmerName = (TextView) v.findViewById(R.id.relayButterflySwimmerName);
        swimmerName.setText(relay.swimmer.name);
        relayTime = (TextView) v.findViewById(R.id.relayButterflyTime);
        relayTime.setText(DurationUtil.getTimeString(relay.time));
        if(butterflyIsfresh) {
            final View medleyButterflyLine = v.findViewById(R.id.medleyButterflyLine);
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector_fresh);
            gradient.mutate();
            medleyButterflyLine.setBackgroundDrawable(gradient);
            //relayTime.setBackgroundColor(Color.CYAN);
        }

        relay = team.relays.get(2);
        boolean breaststrokeIsFresh = !relay.official;
        discipline = (TextView) v.findViewById(R.id.relayBreaststroke);
        discipline.setText(relay.event.getDiscipline().toString());
        swimmerName = (TextView) v.findViewById(R.id.relayBreaststrokeSwimmerName);
        swimmerName.setText(relay.swimmer.name);
        relayTime = (TextView) v.findViewById(R.id.relayBreaststrokeTime);
        relayTime.setText(DurationUtil.getTimeString(relay.time));
        if(breaststrokeIsFresh) {
            final View medleyBreaststrokeLine = v.findViewById(R.id.medleyBreaststrokeLine);
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector_fresh);
            gradient.mutate();
            medleyBreaststrokeLine.setBackgroundDrawable(gradient);
            //relayTime.setBackgroundColor(Color.CYAN);
        }

        relay = team.relays.get(3);
        boolean freeStyleIsFresh = !relay.official;
        discipline = (TextView) v.findViewById(R.id.relayFreestyle);
        discipline.setText(relay.event.getDiscipline().toString());
        swimmerName = (TextView) v.findViewById(R.id.relayFreestyleSwimmerName);
        swimmerName.setText(relay.swimmer.name);
        relayTime = (TextView) v.findViewById(R.id.relayFreestyleTime);
        relayTime.setText(DurationUtil.getTimeString(relay.time));
        if(freeStyleIsFresh) {
            final View medleyFreestyleLine = v.findViewById(R.id.medleyFreestyleLine);
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector_fresh);
            gradient.mutate();
            medleyFreestyleLine.setBackgroundDrawable(gradient);
            //relayTime.setBackgroundColor(Color.CYAN);
        }

        return v;
    }
}
