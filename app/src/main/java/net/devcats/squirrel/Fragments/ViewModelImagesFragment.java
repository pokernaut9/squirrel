package net.devcats.squirrel.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.devcats.squirrel.Adapters.SquirrelPagerAdapter;
import net.devcats.squirrel.Handlers.ModelDataHandler;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.R;

public class ViewModelImagesFragment extends Fragment implements ModelDataHandler.ModelDataHandlerCallbacks {

    private ModelDataHandler modelDataHandler;

    private ModelData modelData;
    private SquirrelPagerAdapter pagerAdapter;
    private ViewPager vpModelImages;

    public static ViewModelImagesFragment newInstance() {
        return new ViewModelImagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_model_images, container, false);

        Context mContext = getActivity();
        modelDataHandler = ModelDataHandler.getInstance(mContext);

        modelData = ModelDataHandler.getInstance(mContext).getModelByID(getArguments().getInt(ModelData.MODEL_ID));

        pagerAdapter = new SquirrelPagerAdapter(getActivity(), modelData.getImageList());

        vpModelImages = (ViewPager) view.findViewById(R.id.vfModelImages);
        vpModelImages.setAdapter(pagerAdapter);
        vpModelImages.setCurrentItem(getArguments().getInt(ModelData.MODEL_SELECTED_IMAGE_POSITION));

        modelDataHandler.addOnTaskCompleteListener(this);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        modelDataHandler.removeOnTaskCompleteListener(this);
    }

    private void updateViewPager() {
        pagerAdapter.updateList(modelData.getImageList());
    }

    @Override
    public void onModelTaskComplete() {
        if (pagerAdapter != null && modelData != null) {
            updateViewPager();
        }
    }

    public int getImagePos() {
        return vpModelImages.getCurrentItem();
    }
}