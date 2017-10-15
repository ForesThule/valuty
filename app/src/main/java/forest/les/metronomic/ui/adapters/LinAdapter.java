package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;

/**
 * Created by root on 05.10.17.
 */

public class LinAdapter extends ArrayAdapter<Valute> {

    LayoutInflater lInflater;

    List<Valute> data;


    public LinAdapter(Context ctx, List<Valute> data) {
        super(ctx,R.layout.item_layout,data);
        this.data = data;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_layout, parent, false);
        }

        TextView viewById = (TextView) view.findViewById(R.id.textViewItem);
        viewById.setText(data.get(position).name);
        return view;
    }
}
