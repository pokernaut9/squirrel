package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.R;

import java.util.List;

public class MainGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ModelData> mModels;

    public MainGridViewAdapter(Context context, List<ModelData> modelData) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mModels = modelData;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public ModelData getItem(int position) {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mModels.get(position).getID();
    }

    public void updateModelList(List<ModelData> modelList) {
        mModels = modelList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_main, parent, false);
        }

        ModelData model = getItem(position);

        // Get pointers to objects
        ImageView imgModelAvatar = (ImageView) convertView.findViewById(R.id.imgModelAvatar);
        TextView tvModelName = (TextView) convertView.findViewById(R.id.tvModelName);
        TextView tvGender = (TextView) convertView.findViewById(R.id.tvGender);
        TextView tvAge = (TextView) convertView.findViewById(R.id.tvAge);
        TextView tvImageCount = (TextView) convertView.findViewById(R.id.tvImageCount);
        ImageView imgCheckMark = (ImageView) convertView.findViewById(R.id.imgCheckMark);
        RatingBar rbModelStarRating = (RatingBar) convertView.findViewById(R.id.rbModelStarRating);

        // Set username
        tvModelName.setText(model.getUsername());

        // Set gender specific data

        switch (model.getPartyType()) {

            case ModelData.FEMALE:
                if (model.getProfileImage() != null) {
                    imgModelAvatar.setImageBitmap(model.getProfileImage());
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imgModelAvatar.setImageResource(R.drawable.ic_female_shadow);
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER);
                }
                tvGender.setText(mContext.getString(R.string.female));
                tvGender.setTextColor(mContext.getResources().getColor(R.color.female_color));

                break;

            case ModelData.MALE:
                if (model.getProfileImage() != null) {
                    imgModelAvatar.setImageBitmap(model.getProfileImage());
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imgModelAvatar.setImageResource(R.drawable.ic_male_shadow);
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER);
                }
                tvGender.setText(mContext.getString(R.string.male));
                tvGender.setTextColor(mContext.getResources().getColor(R.color.male_color));

                break;

            case ModelData.COUPLE:
                if (model.getProfileImage() != null) {
                    imgModelAvatar.setImageBitmap(model.getProfileImage());
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imgModelAvatar.setImageResource(R.drawable.ic_couple_shadow);
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER);
                }
                tvGender.setText(mContext.getString(R.string.couple));
                tvGender.setTextColor(mContext.getResources().getColor(R.color.couple_color));
                break;

            case ModelData.GROUP:
                if (model.getProfileImage() != null) {
                    imgModelAvatar.setImageBitmap(model.getProfileImage());
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imgModelAvatar.setImageResource(R.drawable.ic_group_shadow);
                    imgModelAvatar.setScaleType(ImageView.ScaleType.CENTER);
                }
                tvGender.setText(mContext.getString(R.string.group));
                tvGender.setTextColor(mContext.getResources().getColor(R.color.group_color));
                break;

            default:

                break;
        }

        // Set age
        tvAge.setText(mContext.getString(R.string.age, model.getAge()));

        // Set image count
        tvImageCount.setText(mContext.getString(R.string.image_count, model.getImageCount()));

        // Set star rating
        LayerDrawable stars = (LayerDrawable) rbModelStarRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        rbModelStarRating.setRating(model.getStarRating());

        // Set check mark
        if (UserHandler.getInstance(mContext).isSubscribedToModel(model.getID())) {
            imgCheckMark.setVisibility(View.VISIBLE);
        } else {
            imgCheckMark.setVisibility(View.GONE);
        }


        if (model.isVisible()) {
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            convertView.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        }

        return convertView;
    }

}