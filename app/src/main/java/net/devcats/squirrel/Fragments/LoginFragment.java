package net.devcats.squirrel.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.MainActivity;
import net.devcats.squirrel.R;
import net.devcats.squirrel.Tasks.UrlRequest;
import net.devcats.squirrel.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private Context mContext;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.setHintTextColor(Color.GRAY);

        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPassword.setHintTextColor(Color.GRAY);

        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make sure we have both an email address and password
                if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, getString(R.string.login_error), Toast.LENGTH_LONG).show();
                    return;
                }

                try {

                    final AlertDialog progressDialog = Utils.showWaitSpinner(mContext);

                    JSONObject params = new JSONObject();
                    params.put("email", etEmail.getText().toString());
                    params.put("password", etPassword.getText().toString());

                    new UrlRequest(mContext, UrlRequest.ACTION_LOGIN, params, new UrlRequest.UrlRequestCallbacks() {
                        @Override
                        public void onUrlRequestComplete(String result) {

                            Utils.log("RESPONSE: " + result);

                            if (!result.contains("ERROR")) {
                                UserHandler.getInstance(mContext).parseUserDataFromJson(result);
                            } else {
                                Toast.makeText(mContext, getString(R.string.an_error_occured), Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();

                        }
                    }).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Register
        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onRegisterClicked();
            }
        });

        return view;
    }
}
