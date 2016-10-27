package net.devcats.squirrel.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.devcats.squirrel.R;

public class MyAccountFragment extends Fragment {

    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

            // Show user info
            view = inflater.inflate(R.layout.fragment_my_account, container, false);

            // TODO: This should not be here. The registration screen should be it's own fragment

            // Show registration screen
//            view = inflater.inflate(R.layout.fragment_register, container, false);
//
//            Spinner spMonth = (Spinner) view.findViewById(R.id.spMonth);
//            spMonth.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, getResources().getStringArray(R.array.months)));
//
//            Spinner spDay = (Spinner) view.findViewById(R.id.spDay);
//            spDay.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, getResources().getStringArray(R.array.days)));
//
//            int startingYear = Calendar.getInstance().get(Calendar.YEAR) - 18;
//            String[] years = new String[100];
//
//            for (int i = 0; i <= 99; i++) {
//                years[i] = "" + startingYear--;
//            }
//
//            Spinner spYear = (Spinner) view.findViewById(R.id.spYear);
//            spYear.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, years));

        return view;
    }

    public void updateUI() {}

}
