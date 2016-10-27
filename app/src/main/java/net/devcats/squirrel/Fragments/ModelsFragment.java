package net.devcats.squirrel.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import net.devcats.squirrel.Adapters.MainGridViewAdapter;
import net.devcats.squirrel.Handlers.ModelDataHandler;
import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.Interfaces.OnModelSelectedListener;
import net.devcats.squirrel.MainActivity;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModelsFragment extends Fragment implements ModelDataHandler.ModelDataHandlerCallbacks {

    private ModelDataHandler modelDataHandler;
    private UserHandler userHandler;

    private GridView gvModels;
    private TextView tvNoResults;
    private MainGridViewAdapter mainGridViewAdapter;

    private List<ModelData> mModels = new ArrayList<>();

    private OnModelSelectedListener mCallback;

    private boolean isFirstLoad = true;


    private FilterChangeCallbacks filterChangeCallbacks;


    public interface FilterChangeCallbacks {
        void onFilterUpdate(int count);
    }

    public static ModelsFragment newInstance() {
        return new ModelsFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnModelSelectedListener) activity;
            filterChangeCallbacks = (FilterChangeCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnModelSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_models, container, false);

        Context mContext = getActivity();
        modelDataHandler = ModelDataHandler.getInstance(mContext);
        userHandler = UserHandler.getInstance(mContext);

        mainGridViewAdapter = new MainGridViewAdapter(getActivity(), mModels);
        gvModels = (GridView) view.findViewById(R.id.gvModels);
        gvModels.setAdapter(mainGridViewAdapter);
        gvModels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onModelSelected(mModels.get(position).getID());
            }
        });
        gvModels.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        gvModels.post(new Runnable() {
                            @Override
                            public void run() {
                                downloadVisibleProfileImages();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        tvNoResults = (TextView) view.findViewById(R.id.tvNoResults);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        modelDataHandler.addOnTaskCompleteListener(this);
        if (modelDataHandler.getModelsList().size() == 0) {
            modelDataHandler.getModelListFromWeb();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        modelDataHandler.removeOnTaskCompleteListener(this);
    }

    @Override
    public void onModelTaskComplete() {
        if (mainGridViewAdapter != null) {
//            mOriginalModelsList = ModelDataHandler.getInstance(mContext).getModelsList();
            mModels = modelDataHandler.getModelsList();

            if (isFirstLoad) {

                // Apply any filters from previous session
                ((MainActivity) getActivity()).saveAndApplyFilterSettings();
                gvModels.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadVisibleProfileImages();
                    }
                });

                isFirstLoad = false;
            } else {
                mainGridViewAdapter.updateModelList(mModels);
            }
        }
    }

    public void searchModels(String search, boolean allModels, boolean female, boolean male, boolean couple, boolean group) {

        if (mainGridViewAdapter != null && mModels.size() > 0) {


            for (ModelData modelData : mModels) {
                modelData.setVisible(true);

                // Party type check
                if (!female && modelData.getPartyType() == ModelData.FEMALE) {
                    modelData.setVisible(false);
                } else if (!male && modelData.getPartyType() == ModelData.MALE) {
                    modelData.setVisible(false);
                } else if (!couple && modelData.getPartyType() == ModelData.COUPLE) {
                    modelData.setVisible(false);
                } else if (!group && modelData.getPartyType() == ModelData.GROUP) {
                    modelData.setVisible(false);
                }

                // Search text check
                if (!modelData.getUsername().contains(search)) {
                    modelData.setVisible(false);
                }

                // All/My model check
                if (!allModels) {
                    if (!userHandler.isSubscribedToModel(modelData.getID())) {
                        modelData.setVisible(false);
                    }
                }

            }

            int visibleModelCount = modelDataHandler.getVisibleModelCount();

            filterChangeCallbacks.onFilterUpdate(visibleModelCount);

            if (visibleModelCount > 0) {
//
                Collections.sort(mModels, new Comparator<ModelData>() {
                    @Override
                    public int compare(ModelData modelData1, ModelData modelData2) {
                        if (modelData1.getID() > modelData2.getID()) {
                            return 1;
                        } else if (modelData1.getID() == modelData2.getID()) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });

                mainGridViewAdapter.updateModelList(mModels);
                gvModels.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadVisibleProfileImages();
                    }
                });

                gvModels.setVisibility(View.VISIBLE);
                tvNoResults.setVisibility(View.GONE);
            } else {
                gvModels.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
            }

        }
    }

    private void downloadVisibleProfileImages() {

        if (mModels != null && mModels.size() > 0) {

            int firstPos = gvModels.getFirstVisiblePosition();
            int lastPos = gvModels.getLastVisiblePosition();

            for (int i = firstPos; i <= lastPos; i++) {
                ModelDataHandler.getInstance(getActivity()).getModelProfileImage(mModels.get(i));
            }

        }
    }
}