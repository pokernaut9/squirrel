package net.devcats.squirrel.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import net.devcats.squirrel.Adapters.SquirrelSpinnerAdapter;
import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.R;
import net.devcats.squirrel.Tasks.UrlRequest;
import net.devcats.squirrel.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private Context mContext;

    private RegisterFragmentCallbacks callbacks;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmailAddress;
    private EditText etUsername;

    private Spinner spMonth;
    private Spinner spDay;
    private Spinner spYear;

    private CheckBox chkAgreeToTerms;

    public interface RegisterFragmentCallbacks {
        void onAnimationComplete();
        void onRegisterSuccessful();
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mContext = getActivity();

        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etEmailAddress = (EditText) view.findViewById(R.id.etEmailAddress);
        etUsername = (EditText) view.findViewById(R.id.etUsername);

        spMonth = (Spinner) view.findViewById(R.id.spMonth);
        spMonth.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, getResources().getStringArray(R.array.months)));

        spDay = (Spinner) view.findViewById(R.id.spDay);
        spDay.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, getResources().getStringArray(R.array.days)));

        int startingYear = Calendar.getInstance().get(Calendar.YEAR) - 18;
        String[] years = new String[100];

        for (int i = 0; i <= 99; i++) {
            years[i] = "" + startingYear--;
        }

        spYear = (Spinner) view.findViewById(R.id.spYear);
        spYear.setAdapter(new SquirrelSpinnerAdapter(view.getContext(), 0, years));

        chkAgreeToTerms = (CheckBox) view.findViewById(R.id.chkAgreeToTerms);

        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkForm()) {

                    try {

                        final AlertDialog progressDialog = Utils.showWaitSpinner(mContext);

                        JSONObject params = new JSONObject();

                        params.put("firstname", etFirstName.getText().toString());
                        params.put("lastname", etLastName.getText().toString());
                        params.put("email", etEmailAddress.getText().toString());
                        params.put("username", etUsername.getText().toString());
                        params.put("dob_month", spMonth.getSelectedItem().toString());
                        params.put("dob_day", spDay.getSelectedItem().toString());
                        params.put("dob_year", spYear.getSelectedItem().toString());
                        params.put("created_at", System.currentTimeMillis());

                        new UrlRequest(mContext, UrlRequest.ACTION_REGISTER, params, new UrlRequest.UrlRequestCallbacks() {
                            @Override
                            public void onUrlRequestComplete(String result) {

                                Utils.log("RESPONSE: " + result);

                                if (!result.contains("ERROR")) {
                                    // Parse response into UserHandler
                                    UserHandler.getInstance(mContext).parseUserDataFromJson(result);
                                    callbacks.onRegisterSuccessful();
                                } else {
                                    Utils.makeToast(mContext, getString(R.string.an_error_occured));
                                }

                                progressDialog.dismiss();

                            }
                        }).execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    private boolean checkForm() {

        if (etFirstName.getText().toString().isEmpty()) {
            Utils.makeToast(mContext, getString(R.string.please_enter_firstname));
            return false;
        }

        if (etLastName.getText().toString().isEmpty()) {
            Utils.makeToast(mContext, getString(R.string.please_enter_lastname));
            return false;
        }

        if (etEmailAddress.getText().toString().isEmpty()) {
            Utils.makeToast(mContext, getString(R.string.please_enter_lastname));
            return false;
        }

        if (etUsername.getText().toString().isEmpty()) {
            Utils.makeToast(mContext, getString(R.string.please_enter_lastname));
            return false;
        }

        if (!chkAgreeToTerms.isChecked()) {
            Utils.makeToast(mContext, getString(R.string.please_agree_to_terms));
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (RegisterFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RegisterFragmentCallbacks");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    callbacks.onAnimationComplete();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            return animation;
        } else {
            return super.onCreateAnimation(transit, true, nextAnim);
        }
    }

}
