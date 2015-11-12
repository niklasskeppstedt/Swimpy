package se.skeppstedt.swimpy.listadapter;

import android.widget.CheckBox;

/**
 * Created by Niklas on 2015-11-12.
 */
public interface SwimmerListAdapterListener {
    void onChecked(String octoId, boolean checked);
    void onLongClick(String octoId);
}
