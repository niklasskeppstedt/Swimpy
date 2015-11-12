package se.skeppstedt.swimpy.listadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import se.skeppstedt.swimpy.R;
import se.skeppstedt.swimpy.application.Swimmer;

/**
 * Created by niske on 2015-11-11.
 */
public class SwimmerListAdapter extends BaseAdapter {

    private Context ctx;
    List<Swimmer> swimmers;
    SwimmerListAdapterListener listener = null;

    public SwimmerListAdapter(Context ctx, List<Swimmer> swimmers) {
        this.ctx = ctx;
        this.swimmers = swimmers;
    }

    @Override
    public int getCount() {
        return swimmers.size();
    }

    @Override
    public Object getItem(int position) {
        return swimmers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox v = (CheckBox)convertView;
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (CheckBox)inflater.inflate(R.layout.swimmerwithcheckbox, parent, false);
        }
        //
        final Swimmer c = swimmers.get(position);
        v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onChecked(c.octoId, isChecked);
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(c.octoId);
                return true;
            }
        });
        if ( position % 2 == 0 ) {
            v.setBackgroundDrawable(null);
        } else {
            Drawable gradient = ctx.getResources().getDrawable(R.drawable.table_line_selector);
            gradient.mutate();
            v.setBackgroundDrawable(gradient);
        }
        v.setText(c.name);
        return v;
    }

    public void setOnCheckBoxCheckedListener(SwimmerListAdapterListener checkBoxClickedListener) {
        listener = checkBoxClickedListener;
    }
}
