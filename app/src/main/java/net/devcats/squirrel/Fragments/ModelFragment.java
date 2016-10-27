package net.devcats.squirrel.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.devcats.squirrel.Adapters.ModelListViewAdapter;
import net.devcats.squirrel.Handlers.ModelDataHandler;
import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.Utils;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.R;

public class ModelFragment extends Fragment implements ModelDataHandler.ModelDataHandlerCallbacks {

    private ModelDataHandler modelDataHandler;
    private ModelDetailsCallbacks modelDetailsCallbacks;

    private ModelData modelData;
    private ListView lvModelImages;

    private float lastTopValue = 0;

    private RelativeLayout rlModelHeaderContainer;

    private ModelListViewAdapter modelListViewAdapter;

    public interface ModelDetailsCallbacks {
        void onAnimationComplete();
        void onImageSelected(int modelId, int position);
    }

    public static ModelFragment newInstance() {
        return new ModelFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model, container, false);

        Context mContext = getActivity();
        modelDataHandler = ModelDataHandler.getInstance(mContext);

        modelData = ModelDataHandler.getInstance(getActivity()).getModelByID(getArguments().getInt(ModelData.MODEL_ID));

        // Make sure we have model data
        if (modelData == null) {
            Utils.makeToast(getActivity(), getString(R.string.failed_to_load_model_data));
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

            return view;
        }

        // Setup ListView
        lvModelImages = (ListView) view.findViewById(R.id.lvModelImages);

        // Are we subscribed?
        if (UserHandler.getInstance(mContext).isSubscribedToModel(modelData.getID())) {
            initListView(inflater);
        } else {
            lvModelImages.setVisibility(View.GONE);
            view.findViewById(R.id.llSubscribeContainer).setVisibility(View.VISIBLE);
        }

        setupProfileDetails(view);

        modelDataHandler.addOnTaskCompleteListener(this);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        modelDataHandler.removeOnTaskCompleteListener(this);
    }

    private void initListView(LayoutInflater inflater) {

        final View lvHeader = inflater.inflate(R.layout.model_header, lvModelImages, false);
        lvModelImages.addHeaderView(lvHeader, null, false);

        rlModelHeaderContainer = (RelativeLayout) lvHeader.findViewById(R.id.rlModelHeaderContainer);

        modelListViewAdapter = new ModelListViewAdapter(getActivity(), modelData.getImageList());
        lvModelImages.setAdapter(modelListViewAdapter);

        lvModelImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modelDetailsCallbacks.onImageSelected(modelData.getID(), position - 1);
            }
        });

        lvModelImages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int lastVisibleItem, int itemCount) {

                if (lvModelImages.getCount() > 0) {

                    if (rlModelHeaderContainer.getY() != lastTopValue) {

                        float difference = lastTopValue - rlModelHeaderContainer.getY();

                        rlModelHeaderContainer.setY(rlModelHeaderContainer.getY() + (difference / 2));
                        lastTopValue = rlModelHeaderContainer.getY();
                    }
                }
            }
        });

        modelDataHandler.downloadModelImages(modelData);
    }

    private void setupProfileDetails(View view) {

        ImageView imgProfileImage = (ImageView) view.findViewById(R.id.imgProfileImage);
        imgProfileImage.setImageBitmap(modelData.getProfileImage());

        // Model Detail
        TextView tvModelUsername = (TextView) view.findViewById(R.id.tvModelUsername);
        tvModelUsername.setText(modelData.getUsername());

        TextView tvModelPartyType = (TextView) view.findViewById(R.id.tvModelPartyType);
        switch (modelData.getPartyType()) {
            case ModelData.FEMALE:
                tvModelPartyType.setText(getActivity().getString(R.string.female));
                tvModelPartyType.setTextColor(getResources().getColor(R.color.female_color));
                break;

            case ModelData.MALE:
                tvModelPartyType.setText(getActivity().getString(R.string.male));
                tvModelPartyType.setTextColor(getResources().getColor(R.color.male_color));
                break;

            case ModelData.COUPLE:
                tvModelPartyType.setText(getActivity().getString(R.string.couple));
                tvModelPartyType.setTextColor(getResources().getColor(R.color.couple_color));
                break;

            case ModelData.GROUP:
                tvModelPartyType.setText(getActivity().getString(R.string.group));
                tvModelPartyType.setTextColor(getResources().getColor(R.color.group_color));
                break;
        }

        TextView tvModelAge = (TextView) view.findViewById(R.id.tvModelAge);
        tvModelAge.setText(getString(R.string.age, modelData.getAge()));

        TextView tvImageCount = (TextView) view.findViewById(R.id.tvImageCount);
        tvImageCount.setText(getString(R.string.image_count, modelData.getImageCount()));
    }

    public void setImagePos(final int position) {
        lvModelImages.setSelection(position + 1);
        lvModelImages.post(new Runnable() {
            @Override
            public void run() {
                lvModelImages.smoothScrollToPosition(position + 1);
            }
        });
    }

    @Override
    public void onModelTaskComplete() {
        if (modelListViewAdapter != null && modelData != null) {
            modelListViewAdapter.updateData(modelData.getImageList());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            modelDetailsCallbacks = (ModelDetailsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ModelDetailsCallbacks");
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
                    modelDetailsCallbacks.onAnimationComplete();
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
