package net.devcats.squirrel.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import net.devcats.squirrel.Adapters.UserAlbumsGridViewAdapter;
import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.Models.ModelAlbum;
import net.devcats.squirrel.R;
import net.devcats.squirrel.Tasks.UrlRequest;
import net.devcats.squirrel.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyAlbumsFragment extends Fragment {

    private Context mContext;
    private List<ModelAlbum> mAlbums;
    private GridView gvUserAlbums;
    private UserAlbumsGridViewAdapter userAlbumsGridViewAdapter;

    public static MyAlbumsFragment newInstance() {
        return new MyAlbumsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        mAlbums = UserHandler.getInstance(mContext).getUserAlbums();

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        gvUserAlbums = (GridView) view.findViewById(R.id.gvUserAlbums);

        if (mAlbums.size() == 0) {
            view.findViewById(R.id.tvNoAlbums).setVisibility(View.VISIBLE);
            gvUserAlbums.setVisibility(View.GONE);
        } else {

            userAlbumsGridViewAdapter = new UserAlbumsGridViewAdapter(mContext, mAlbums);
            gvUserAlbums.setAdapter(userAlbumsGridViewAdapter);

        }

        return view;
    }

    public void showAddNewAlbumDialog() {

        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_new_album, null, false);
        final EditText etName = (EditText) layout.findViewById(R.id.etName);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

        dialog.setTitle(getString(R.string.add_new_album));
        dialog.setView(layout);
        dialog.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etName.getText().toString().isEmpty()) {
                    Utils.makeToast(mContext, getString(R.string.please_enter_album_name));
                } else {
                    addNewAlbum(etName.getText().toString());
                }
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void addNewAlbum(String name) {
        try {

            final AlertDialog progressDialog = Utils.showWaitSpinner(mContext);

            JSONObject params = new JSONObject();
            params.put("albumName", name);

            new UrlRequest(mContext, UrlRequest.ACTION_ADD_NEW_ALBUM, params, new UrlRequest.UrlRequestCallbacks() {
                @Override
                public void onUrlRequestComplete(String result) {
                    Utils.log("RESPONSE: " + result);

                    if (!result.contains("ERROR")) {

                        ModelAlbum modelAlbum = new ModelAlbum();

                        mAlbums.add(modelAlbum.parseAlbumFromJSON(result));
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

}
