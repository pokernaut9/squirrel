package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.devcats.squirrel.R;

public class SquirrelSpinnerAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private String[] mValues;

    public SquirrelSpinnerAdapter(Context context, int txtViewResourceId, String[] objects) {
        super(context, txtViewResourceId, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mValues = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_spinner_dropdown, parent, false);
        }

        TextView tvText = (TextView) convertView .findViewById(R.id.tvText);
        tvText.setText(mValues[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_spinner, parent, false);
        }

        TextView tvText = (TextView) convertView .findViewById(R.id.tvText);
        tvText.setText(mValues[position]);

        return convertView;
    }

}
